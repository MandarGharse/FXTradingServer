package com.fx.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorWorkerAppPool {

    static ExecutorService executorService = null;
    private static final ExecutorWorkerAppPool executorWorkerAppPool = new ExecutorWorkerAppPool();

    private ExecutorWorkerAppPool() {
        executorService = this.createExecutor();
    }

    public static ExecutorWorkerAppPool getInstance()   {
        return executorWorkerAppPool;
    }

    public ExecutorService getExecutor()    {
        return executorService;
    }
    private ExecutorService createExecutor()    {
        System.out.printf("Total available processors " + Runtime.getRuntime().availableProcessors());
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), new ForkJoinPool.ForkJoinWorkerThreadFactory() {
            final AtomicInteger num = new AtomicInteger();

            @Override
            public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                thread.setDaemon(true);
                thread.setName("grpc-server-processor--" + this.num
                        .getAndIncrement());
                return thread;
            }
        }, new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("FATAL error Unhandled Exception");
            }
        }, true);
    }

}
