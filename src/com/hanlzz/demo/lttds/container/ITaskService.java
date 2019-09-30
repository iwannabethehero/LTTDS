package com.hanlzz.demo.lttds.container;

/**
 * @author h
 */
public interface ITaskService {
    /**
     * 定时任务或者队列任务会执行该方法,抛出异常或者返回false为任务失败
     * @param param 方法需要的参数
     * @return bol类型,返回false为失败,扣除task重试次数
     * 返回 true task将不再继续
     */
    boolean invoke(Object param);


    /**
     * 此方法在任务最终失败时调用
     * 可以实现空方法体
     * @param param 方法所需参数
     */
    void failedEnd(Object param);
}
