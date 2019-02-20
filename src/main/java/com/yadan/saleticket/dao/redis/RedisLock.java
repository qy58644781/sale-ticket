package com.yadan.saleticket.dao.redis;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisLock {

    @Autowired
    private RedisHelper redisHelper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String LOCK_VALUE = "LOCK";

    private boolean holdLock(String key) {
        return redisHelper.setIfAbsent(key, LOCK_VALUE);
    }

    private void setLockExpire(String key, int expireTime) {
        redisHelper.writeValue(key, LOCK_VALUE, expireTime);
    }

    private void deleteLock(String key) {
        redisHelper.delete(key);
    }

    public Object lock(LockBusiness business, String key, int expireTime, int waitTime, int maxWaitTime) {
        Object result;
        try {
            long start = System.currentTimeMillis();

            while (!holdLock(key)) {
                if (System.currentTimeMillis() > start + maxWaitTime) {
                    throw new SystemException(ExceptionCode.SYSTEM, "获取redis锁超时");
                }

                Thread.sleep(waitTime);
            }

            setLockExpire(key, expireTime);
            result = business.doBusiness();
            deleteLock(key);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            logger.error("redis get lock error, key: " + key, e);
            throw new SystemException(ExceptionCode.SYSTEM, "获取redis锁，执行任务出错，key： " + key);
        } finally {
            deleteLock(key);
        }

        return result;
    }
}
