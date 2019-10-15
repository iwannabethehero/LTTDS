package com.hanlzz.demo.lttds.utils;

import com.hanlzz.demo.lttds.container.RunTask;
import com.hanlzz.demo.lttds.container.TaskContainer;
import com.hanlzz.demo.lttds.container.Task;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author h
 */
public class TaskUtils {

    private volatile static boolean isOpen = false;

    private static final int BASE_CORE = 5;
    static {
        openTaskService();
    }

    public synchronized static void openTaskService() {
        if (!isOpen) {
            //开启一个线程,监听队列
            isOpen = true;
            getExecutor().execute(TaskUtils::openCommonQueueListener);
            getExecutor().execute(TaskUtils::openPriorityQueueListener);
            //两个监听线程已开启
        }
    }

    private static void openPriorityQueueListener() {
        for (; ; ) {
            long time = System.currentTimeMillis();
            List<Task> tasks = getContainer().pollPriorityList(time);
            for (Task task:tasks) {
                getSemaphore().acquire();
                try {
                    getExecutor().execute(new RunTask(task,getSemaphore()));
                } catch (Exception e) {
                    e.printStackTrace();
                    //skip
                }
            }
            //间隔1秒捞出符合条件的task
            long t2 = 1000L - System.currentTimeMillis() + time;
            try {
                Thread.sleep(t2 > 0 ? t2 : 0);
            } catch (InterruptedException e) {
                //
            }
        }
    }

    private static void openCommonQueueListener() {
        for (; ; ) {
            Task task = getContainer().pollQueue();
            if (task == null) {
                Thread.yield();
            } else {
                getSemaphore().acquire();
                try {
                    getExecutor().execute(new RunTask(task,getSemaphore()));
                } catch (Exception e) {
                    e.printStackTrace();
                    //skip
                }
            }
        }
    }


    public static TaskContainer getContainer() {
        return ContainerHolder.CONTAINER;
    }

    private static ThreadPoolExecutor getExecutor() {
        return ContainerHolder.EXECUTOR;
    }

    private static ThreadSemaphore getSemaphore() {
        return ContainerHolder.SEMAPHORE;
    }

    private static class ContainerHolder {

        private static final ThreadFactory FACTORY = Executors.defaultThreadFactory();

        //最大线程数一定要设置,可能会存在超出核心线程情况
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(BASE_CORE, 32, 200, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), FACTORY);
        private static final TaskContainer CONTAINER = new TaskContainer();

        //两个线程用来做监听器
        private static final ThreadSemaphore SEMAPHORE = new ThreadSemaphore(BASE_CORE - 2);
    }


    public static void offerTask(Task task) {
        initTask(task);
        //offer前重试次数减1
        task.setRetry(task.getRetry() - 1);
        if (task.getRetryInterval() == 0) {
            //放到一般队列
            ContainerHolder.CONTAINER.offerQueue(task);
        } else {
            //放到优先队列里
            ContainerHolder.CONTAINER.offerPriorityQueue(task);
        }
    }

    private static void initTask(Task task) {
        if (task == null){
            throw new NullPointerException("task can not be null");
        }

        //TODO : Errors may be detected
        //检测重试次数,检测task是否符合标准
        //没有service的设置service
    }


    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            if (unsafe == null) {
                throw new RuntimeException("can not get unsafe!");
            }
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("unsafe initialization failed,msg : " + e.getMessage(), e);
        }
    }
}
