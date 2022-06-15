package com.camelot.purchase.config;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * A SLF4J MDC-compatible {@link ThreadPoolExecutor}.
 * <p/>
 * In general, MDC is used to store diagnostic information (e.g. a user's session id) in per-thread variables, to facilitate
 * logging. However, although MDC data is passed to thread children, this doesn't work when threads are reused in a
 * thread pool. This is a drop-in replacement for {@link ThreadPoolExecutor} sets MDC data before each task appropriately.
 * <p/>
 * Created by jlevy.
 * Date: 6/14/13
 */
public class MdcThreadPoolExecutor extends ThreadPoolExecutor {

    final private boolean useFixedContext;
    final private Map<String, String> fixedContext;

    /**
     * Pool where task threads take MDC from the submitting thread.
     */
    public MdcThreadPoolExecutor newWithInheritedMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                            TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return new MdcThreadPoolExecutor(null, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * Pool where task threads take fixed MDC from the thread that creates the pool.
     */
    @SuppressWarnings("unchecked")
    public MdcThreadPoolExecutor newWithCurrentMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                          TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return new MdcThreadPoolExecutor(MDC.getCopyOfContextMap(), corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue);
    }

    /**
     * Pool where task threads always have a specified, fixed MDC.
     */
    public MdcThreadPoolExecutor newWithFixedMdc(Map<String, String> fixedContext, int corePoolSize,
                                                        int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                                        BlockingQueue<Runnable> workQueue) {
        return new MdcThreadPoolExecutor(fixedContext, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MdcThreadPoolExecutor(Map<String, String> fixedContext, int corePoolSize, int maximumPoolSize,
                                  long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
    }
    public MdcThreadPoolExecutor(Map<String, String> fixedContext, int corePoolSize, int maximumPoolSize,
                                  long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,
                                 RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory,handler);
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }

    /**
     * All executions will have MDC injected. {@code ThreadPoolExecutor}'s submission methods ({@code submit()} etc.)
     * all delegate to this.
     */
    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, getContextForTask()));
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return new Runnable() {
            @Override
            public void run() {
                Map previous = MDC.getCopyOfContextMap();
                if (context == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(context);
                }
                try {
                    runnable.run();
                } finally {
                    if (previous == null) {
                        MDC.clear();
                    } else {
                        MDC.setContextMap(previous);
                    }
                }
            }
        };
    }
}
