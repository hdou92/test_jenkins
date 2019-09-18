package com.example.demo.interfaces.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * redis扩展工具类
 */
@Component
public class ExRedisUtils extends RedisUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExRedisUtils.class);

    private final RedisUtil redisUtil;

    public ExRedisUtils(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
        this.redisTemplate = redisUtil.redisTemplate;
    }


    //<editor-fold desc="hash">

    /**
     * 获取hash的值，并且json反序列化成对象
     */
    public <T> T hgetFromJson(String key, String hashKey, Class<T> clazz) {
        return (T) redisUtil.hget(key, hashKey);
    }


    /**
     * 获取hash的值，并且json反序列化成对象
     */
    public boolean hsetToJson(String key, String hashKey, Object hashVal) {
        return redisUtil.hset(key, hashKey, hashVal);
    }


    /**
     * 获取hash的数据size
     */
    public Long hsize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 是否存在指定的hash key
     */
    public Boolean hexists(String key, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    //</editor-fold>


    //<editor-fold desc="set">

    /**
     * 是否存在指定的hash key
     */
    public Boolean sExists(String key, Object hashKey) {
        return redisTemplate.opsForSet().isMember(key, hashKey);
    }

    /**
     * 从set中pop值
     */
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    //</editor-fold>


    //<editor-fold desc="list">

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取list中的值
     */
    public <T> T lGetIndexFromJson(String key, long index, Class<T> clazz) {
        return (T) lGetIndex(key, index);
    }

    /**
     * 从list中pop值
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从list中pop值
     */
    public <T> T lPopFromJson(String key, Class<T> clazz) {
        return (T) lPop(key);
    }

    /**
     * push值到list中
     */
    public Long lPush(String key, Object val) {
        return redisTemplate.opsForList().rightPush(key, val);
    }

    /**
     * push值到list中
     */
    public Long lPushToJson(String key, Object val) {
        return lPush(key, val);
    }

    /**
     * push值到list中首部
     */
    public Long lPushFirst(String key, Object val) {
        return redisTemplate.opsForList().leftPush(key, val);
    }

    /**
     * push值到list中首部
     */
    public Long lPushFirstToJson(String key, Object val) {
        return redisTemplate.opsForList().leftPush(key, val);
    }

    //</editor-fold>


    /**
     * redis锁的前缀
     */
    private static final String LOCK_KEY_EX = "r_lock:";


    /**
     * 加锁并且自动释放锁
     *
     * @param key        lock key
     * @param waitExpiry 等待锁的超时时间，秒
     * @param keyExpiry  key的超时时间，秒
     * @param doLock     是否进行锁
     */
    public boolean lockAutoUnlock(String key, int waitExpiry, int keyExpiry, Runnable action, boolean doLock) {
        if (doLock) {
            try {
                return lockAutoUnlock(key, waitExpiry, keyExpiry, action);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.toString());
                return false;
            }
        } else {
            action.run();
            return true;
        }
    }

    /**
     * 加锁并且自动释放锁
     *
     * @param key        lock key
     * @param waitExpiry 等待锁的超时时间，秒
     * @param keyExpiry  key的超时时间，秒
     */
    public boolean lockAutoUnlock(String key, int waitExpiry, int keyExpiry, Runnable action) throws UnsupportedEncodingException {
        String lock = lock(key, waitExpiry, keyExpiry);
        if (lock != null && lock.length() > 0) {
            try {
                action.run();
            } finally {
                unlock(key, lock);
            }
            return true;
        }
        return false;
    }

    /**
     * 等待加锁并且自动释放锁
     *
     * @param key        lock key
     * @param waitExpiry 等待锁的超时时间，秒
     * @param keyExpiry  key的超时时间，秒
     * @param doLock     是否进行锁
     */
    public final boolean waitAndLockAutoUnlock(String key, int waitExpiry, int keyExpiry, Runnable action, boolean doLock) {
        if (doLock) {
            return waitAndLockAutoUnlock(key, waitExpiry, keyExpiry, action);
        } else {
            action.run();
            return true;
        }
    }

    /**
     * 等待加锁并且自动释放锁
     *
     * @param key        lock key
     * @param waitExpiry 等待锁的超时时间，秒
     * @param keyExpiry  key的超时时间，秒
     */
    public final boolean waitAndLockAutoUnlock(String key, int waitExpiry, int keyExpiry, Runnable action) {
        try {
            boolean sucLock = false;
            do {
                sucLock = lockAutoUnlock(key, waitExpiry, keyExpiry, action);
            } while (!sucLock);
            return true;
        } catch (Exception e) {
            //这里捕获所有的异常
            LOGGER.error(e.toString());
            return false;
        }
    }

    /**
     * @param key        lock key
     * @param waitExpiry 等待锁的超时时间，秒
     * @param keyExpiry  key的超时时间，秒
     */
    public String lock(String key, int waitExpiry, int keyExpiry) throws UnsupportedEncodingException {

        String utf8 = "UTF-8";
        String lockKey = LOCK_KEY_EX + key;
        byte[] bsKey = lockKey.getBytes(utf8);
        String lockVal = UUID.randomUUID().toString();
        byte[] bsVal = lockVal.getBytes(utf8);

        long waitEnd = System.currentTimeMillis() + (waitExpiry * 1000);

        RedisConnectionFactory connFactory = redisTemplate.getConnectionFactory();
        RedisConnection conn = connFactory.getConnection();

        try {
            do {
                //如果成功的拿到锁，设置锁的有效时间，并返回锁
                if (conn.setNX(bsKey, bsVal)) {
                    conn.expire(bsKey, keyExpiry);
                    return lockVal;
                }

                //如果没有拿到锁，检查锁是否设置了超时时间，如果没有那么设置锁的时间
                if (conn.ttl(bsKey) == -1) {
                    conn.expire(bsKey, keyExpiry);
                }

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } while (System.currentTimeMillis() < waitEnd);
        } finally {
            RedisConnectionUtils.releaseConnection(conn, connFactory);
        }

        return null;
    }

    public boolean unlock(String key, String lockVal) throws UnsupportedEncodingException {
        String lockKey = LOCK_KEY_EX + key;
        String utf8 = "UTF-8";
        byte[] bsKey = lockKey.getBytes(utf8);

        //不要直接使用redisTemplate.watch/.multi()进行操作，因为可能造成不是同一连接的，而是获取conn，
        //在同一conn进行操作

        RedisConnectionFactory connFactory = redisTemplate.getConnectionFactory();
        RedisConnection conn = connFactory.getConnection();

        try {
            while (true) {
                conn.watch(bsKey);
                try {
                    //释放锁前必须谨慎的判断加锁时的键值和现在的键值是否相同
                    byte[] bsVal = conn.get(bsKey);
                    if (bsVal != null && lockVal.equals(new String(bsVal, utf8))) {
                        //开启事务
                        conn.multi();
                        try {

                            conn.del(bsKey);
                            List<Object> rsl = conn.exec();//执行事务
                            if (rsl != null && !rsl.isEmpty()) {
                                return true;
                            }

                        } catch (Exception e) {
                            //异常释放事务
                            conn.discard();
                            throw e;
                        }
                    } else {
                        break;
                    }
                } finally {
                    conn.unwatch();
                }
            }
        } finally {
            RedisConnectionUtils.releaseConnection(conn, connFactory);
        }

        return false;
    }

}
