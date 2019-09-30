package com.hanlzz.demo.lttds.container;

/**
 * @author h
 */
public class Task implements Comparable<Task> {


    private String taskId;
    private int retry = 1;
    private int retryInterval = 0;
    private long timeId;


    /**
     * 方法名称
     */
    private String method;
    /**
     * 全类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String finalMethod;
    /**
     * 全类名
     */
    private String finalClass;

    /**
     * 方法入参
     */
    private Object param;

    /**
     * 方法服务
     */
    private ITaskService service;

    /**
     * 是否为优先级任务
     */
    private boolean priorityTask = false;
    /**
     * 优先级
     */
    private int level = 0;





    /**
     * 一般队列任务
     *
     * @param retry   重试次数
     * @param service 服务
     * @param param   方法参数
     */
    public Task(int retry, ITaskService service, Object param) {
        this.retry = retry;
        this.service = service;
        this.param = param;
    }

    /**
     *  一般定时队列
     * @param retry 重试次数
     * @param service 调用服务
     * @param param 调用参数
     * @param retryInterval 时间间隔,单位毫秒
     */
    public Task(int retry,ITaskService service,Object param,int retryInterval){
        this.retry = retry;
        this.service = service;
        this.param = param;
        this.retryInterval = retryInterval;
    }

    //以下构造器暂不支持...有些麻烦


    /**
     * 一般队列任务-2
     * 反射获得方法并写入代理
     *
     * @param retry       重试次数
     * @param method      需要执行的方法名
     * @param className   需要执行的类名
     * @param param       方法所需参数
     * @param finalClass  失败所需执行类名
     * @param finalMethod 方法所需方法名
     */
    private Task(int retry, String method, String className, String finalMethod, String finalClass, Object param) {
        this.retry = retry;
        this.method = method;
        this.className = className;
        this.finalClass = finalClass;
        this.finalMethod = finalMethod;
        this.param = param;
    }

    private void init() {

    }


    public Object getParam() {
        return param;
    }

    public ITaskService getService() {
        return service;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public long getTimeId() {
        return timeId;
    }

    public void setTimeId(long timeId) {
        this.timeId = timeId;
    }

    @Override
    public int compareTo(Task o) {
        long res = timeId - o.timeId;
        if (res < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
