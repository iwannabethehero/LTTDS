package com.hanlzz.demo.lttds.container;

import com.hanlzz.demo.lttds.utils.TaskUtils;
import com.hanlzz.demo.lttds.utils.ThreadSemaphore;

/**
 * @author h
 */
public class RunTask implements Runnable {
    private final Task task;
    private final ThreadSemaphore semaphore;

    @Override
    public void run() {
        try {
            if (!checkTask()) {
                return;
            }
            boolean res;
            try {
                res = task.getService().invoke(task.getParam());
            } catch (Throwable e) {
                res = false;
            }
            if (!res) {
                if (task.getRetry() > 0) {
                    TaskUtils.offerTask(task);
                } else {
                    task.getService().failedEnd(task.getParam());
                }
            }
        } finally {
            semaphore.release();
        }
    }

    private boolean checkTask() {
        if (task == null || task.getRetry() < 0) {
            return false;
        }
        return task.getService() != null;
    }

    public RunTask(Task task, ThreadSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
}
