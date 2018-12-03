package com.yadan.saleticket.dao.hibernate.base;

import com.yadan.saleticket.model.base.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class SimpleExtJpaRepository<T extends BaseModel, ID extends Long> extends SimpleJpaRepository<T, ID> implements ExtJpaRepository<T, ID> {


    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final PersistenceProvider provider;
    private CrudMethodMetadata metadata;

    public SimpleExtJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    public SimpleExtJpaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }


    private T findOne(Number id) {
        if (id == null) {
            return null;
        }
        Class<T> domainType = this.getDomainClass();
        if (this.metadata == null) {
            return this.em.find(domainType, id);
        } else {
            LockModeType type = this.metadata.getLockModeType();
            Map<String, Object> hints = this.getQueryHints();
            return type == null ? this.em.find(domainType, id, hints) : this.em.find(domainType, id, type, hints);
        }
    }

    @Override
    @Transactional
    public T merge(T t) {
        boolean contains = em.contains(t);
        if (!contains && t.getId() != null && t.getId() > 0) {
            T o = this.findOne(t.getId());
            copyProperties(t, o);
            t = saveAndFlush(o);
        } else {
            t = saveAndFlush(t);
        }
        return t;
    }

    @Override
    public Page findAllByFilterAndPageRequest(STPageRequest stPageRequest, Map<String, String> filter, Class<T> clazz) {
        StringBuilder hql = new StringBuilder("from " + clazz.getSimpleName() + " c where 1=1 ");
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
                hql.append("and c." + key + " like :" + key + " ");
            }
        }
        if (StringUtils.isNotEmpty(stPageRequest.getSortField())
                && stPageRequest.getSortOrder() != null) {
            hql.append("order by " + stPageRequest.getSortField() + " " + stPageRequest.getSortOrder() + " ");
        }

        Query query = em.createQuery("select c " + hql);
        Query total = em.createQuery("select count(1) " + hql);

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
                query.setParameter(key, "%" + value + "%");
                total.setParameter(key, "%" + value + "%");
            }
        }

        if (stPageRequest.getPage() != null && stPageRequest.getCount() != null) {
            query.setFirstResult((stPageRequest.getPage() - 1) * stPageRequest.getCount())
                    .setMaxResults(stPageRequest.getCount());
        }
        return new PageImpl(query.getResultList(), stPageRequest.genPageRequest(), (Long) total.getSingleResult());

    }


    /**
     * 扩展持久方法
     *
     * @param alwaysCheckIn 一直检查相关联依赖
     */
    @Transactional
    public void persist(T t, Boolean alwaysCheckIn) {
        if (alwaysCheckIn) makeReference(t);
        this.merge(t);
    }

    /**
     * 扩展合并方法
     *
     * @param alwaysCheckIn 一直检查相关联依赖
     */
    @Transactional
    public T merge(T t, boolean alwaysCheckIn) {
        if (alwaysCheckIn) makeReference(t);
        return this.merge(t);
    }

    /**
     * 新增对象的时候，如果当前对象有关联的对象（而关联对象只传入ID）;
     * 这个方法会根据关联对象ID查询持久对象进行关联
     *
     * @param entity 处理的持久对象
     */
    private void makeReference(T entity) {
        Class<?> actualEditable = entity.getClass();
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (isFiltered(targetPd)) {
                continue;
            }
            try {
                T value = null;
                if (BaseModel.class.isAssignableFrom(targetPd.getPropertyType())) {
                    Method writeMethod = targetPd.getWriteMethod();
                    Method readMethod = targetPd.getReadMethod();

                    if (readMethod.isAnnotationPresent(Transient.class)) continue;

                    value = (T) readMethod.invoke(entity);
                    if (value == null) continue;

                    // 如果编号为空则进行持久处理
                    if (value.getId() == null || value.getId() <= 0) {
                        this.persist(value, true);
                    }
                    // 这个关联对象尚未在持久状态中，则更新原有属性查找出来进行关联
                    if (!em.contains(value)) {
                        value = this.merge(value, true);
                        writeMethod.invoke(entity, value);
                    }
                } else if (Collection.class.isAssignableFrom(targetPd.getPropertyType())) {
                    Collection collection = (Collection) targetPd.getReadMethod().invoke(entity);
                    if (collection == null) continue;
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        Object o = iterator.next();
                        if (o == null || !(o instanceof BaseModel)) break;

                        value = (T) o;
                        // 如果编号为空则进行持久处理
                        if (value.getId() == null || value.getId() <= 0) {
                            this.persist(value, true);
                        }
                        // 这个关联对象尚未在持久状态中，则更新原有属性查找出来进行关联
                        if (!em.contains(value)) {
                            this.merge(value, true);
                        }
                    }
                }
            } catch (Throwable e) {
                String info = String.format("property name : %s, type : %s", targetPd.getDisplayName(),
                        targetPd.getPropertyType().toString());
                log.error(info, e);
                break;
            }
        }
    }

    /**
     * 需要过滤的属性,这些属性不是实体类的属性,非标处理(以后实体类的属性必须干净)
     *
     * @param pd 属性对象Setter/Getter
     * @return true/false
     */
    protected boolean isFiltered(PropertyDescriptor pd) {
        String[] names = {"currentMember", "class", "isDeleted", "version"};
        Annotation[] array = pd.getReadMethod().getDeclaredAnnotations();
        for (Annotation annotation : array) {
            if (annotation.annotationType().equals(Transient.class)) {
                return true;
            }
        }
        return ArrayUtils.contains(names, pd.getName());
    }

    /**
     * 对象属性覆盖
     *
     * @param source 承载数据对象
     * @param target 目标对象,通常是被查出来的持久对象
     */
    private void copyProperties(Object source, Object target) {
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            // replace the value without null
                            if (value == null) {
                                continue;
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

//    public <T> T toOriginRevision(Class<T> clazz, Long baseEntityId) {
//        AuditReader reader = AuditReaderFactory.get(em);
//        List<Number> list = reader.getRevisions(clazz, baseEntityId);
//        return reader.find(clazz, baseEntityId, list.get(0));
//    }


}