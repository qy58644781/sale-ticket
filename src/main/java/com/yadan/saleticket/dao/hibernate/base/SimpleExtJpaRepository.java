package com.yadan.saleticket.dao.hibernate.base;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class SimpleExtJpaRepository<T extends BaseModel, ID extends Long> extends SimpleJpaRepository<T, ID> implements ExtJpaRepository<T, ID> {


    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final PersistenceProvider provider;
    private CrudMethodMetadata metadata;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public SimpleExtJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    public SimpleExtJpaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    private T findOne(Long id) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = null;
                condition1 = criteriaBuilder.equal(root.get("id"), id);
                query.where(condition1);
                return null;
            }
        };
        return super.findOne(specification).get();
    }

    @Override
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            super.save(entity);
            return entity;
        } else {
            S old = (S) this.findOne(entity.getId());
            copyProperties(entity, old);
            return super.save(old);
        }
    }

    /**
     * 扩展持久方法
     *
     * @param alwaysCheckIn 一直检查相关联依赖
     */
    @Transactional
    public void persist(T t, Boolean alwaysCheckIn) {
        if (alwaysCheckIn) makeReference(t);
        this.save(t);
    }

    /**
     * 扩展合并方法
     *
     * @param alwaysCheckIn 一直检查相关联依赖
     */
    @Transactional
    public T merge(T t, boolean alwaysCheckIn) {
        if (alwaysCheckIn) makeReference(t);
        return this.save(t);
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

}