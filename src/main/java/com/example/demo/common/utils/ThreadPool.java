package com.example.demo.common.utils;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class ThreadPool {

    private final ThreadPoolExecutor executor;

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 默认只为CPU数量（不会抛异常）
     */
    public ThreadPool() {
        this(getAvailableProcessors());
    }


    /**
     * （不会抛异常）
     *
     * @param coreThreadCount 默认线程数量（线程池核心线程）
     */
    public ThreadPool(int coreThreadCount) {
        this.executor = new ThreadPoolExecutor(coreThreadCount, coreThreadCount, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
    }

    /**
     * （不会抛异常）
     *
     * @param coreThreadCount 默认线程数量（线程池核心线程）
     * @param threadFactory   threadFactory
     */
    public ThreadPool(int coreThreadCount, ThreadFactory threadFactory) {
        this.executor = new ThreadPoolExecutor(coreThreadCount, coreThreadCount, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), threadFactory);
    }

    /**
     * 如果线程数量超出 最大线程数量 会抛异常
     *
     * @param coreThreadCount 默认线程数量（线程池核心线程）
     * @param maxThreadCount  最大线程数量
     */
    public ThreadPool(int coreThreadCount, int maxThreadCount) {
        this(coreThreadCount, maxThreadCount, getAvailableProcessors() * 4);
    }

    /**
     * 如果线程数量超出 最大线程数量 会抛异常
     *
     * @param coreThreadCount 默认线程数量（线程池核心线程）
     * @param maxThreadCount  最大线程数量
     * @param threadFactory   threadFactory
     */
    public ThreadPool(int coreThreadCount, int maxThreadCount, ThreadFactory threadFactory) {
        this(coreThreadCount, maxThreadCount, getAvailableProcessors() * 4, threadFactory);
    }

    /**
     * 如果线程数量超出 最大线程数量 会抛异常
     *
     * @param coreThreadCount           默认线程数量（线程池核心线程）
     * @param maxThreadCount            最大线程数量
     * @param coreThreadTaskBufferCount 核心线程任务缓存数量（如果核心线程都在使用中，那么任务会先存放在缓存队列中，超出缓存后才会创建新的线程）
     */
    public ThreadPool(int coreThreadCount, int maxThreadCount, int coreThreadTaskBufferCount) {
        this(coreThreadCount, maxThreadCount, coreThreadTaskBufferCount, 60, TimeUnit.SECONDS);
    }

    /**
     * 如果线程数量超出 最大线程数量 会抛异常
     *
     * @param coreThreadCount           默认线程数量（线程池核心线程）
     * @param maxThreadCount            最大线程数量
     * @param coreThreadTaskBufferCount 核心线程任务缓存数量（如果核心线程都在使用中，那么任务会先存放在缓存队列中，超出缓存后才会创建新的线程）
     * @param threadFactory             threadFactory
     */
    public ThreadPool(int coreThreadCount, int maxThreadCount, int coreThreadTaskBufferCount, ThreadFactory threadFactory) {
        this(coreThreadCount, maxThreadCount, coreThreadTaskBufferCount, 60, TimeUnit.SECONDS, threadFactory);
    }

    /**
     * 如果线程数量超出 最大线程数量 会抛异常
     *
     * @param coreThreadCount           默认线程数量（线程池核心线程）
     * @param maxThreadCount            最大线程数量
     * @param coreThreadTaskBufferCount 核心线程任务缓存数量（如果核心线程都在使用中，那么任务会先存放在缓存队列中，超出缓存后才会创建新的线程）
     * @param keepAliveTime             非核心线程的闲置过期销毁时间
     * @param unit                      时间单位
     */
    public ThreadPool(int coreThreadCount, int maxThreadCount, int coreThreadTaskBufferCount, long keepAliveTime,
                      TimeUnit unit) {
        this.executor = new ThreadPoolExecutor(coreThreadCount, maxThreadCount, keepAliveTime, unit,
                new LinkedBlockingDeque<>(coreThreadTaskBufferCount));
    }

    /**
     * 如果线程数量超出 最大线程数量 会抛异常
     *
     * @param coreThreadCount           默认线程数量（线程池核心线程）
     * @param maxThreadCount            最大线程数量
     * @param coreThreadTaskBufferCount 核心线程任务缓存数量（如果核心线程都在使用中，那么任务会先存放在缓存队列中，超出缓存后才会创建新的线程）
     * @param keepAliveTime             非核心线程的闲置过期销毁时间
     * @param unit                      时间单位
     * @param threadFactory             threadFactory
     */
    public ThreadPool(int coreThreadCount, int maxThreadCount, int coreThreadTaskBufferCount, long keepAliveTime,
                      TimeUnit unit, ThreadFactory threadFactory) {
        this.executor = new ThreadPoolExecutor(coreThreadCount, maxThreadCount, keepAliveTime, unit,
                new LinkedBlockingDeque<>(coreThreadTaskBufferCount), threadFactory);
    }


    /**
     * 获取线程池
     */
    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }


}
