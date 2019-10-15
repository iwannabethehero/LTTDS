package com.hanlzz.demo.lttds.container;

import com.hanlzz.demo.lttds.common.SimpleFastQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author h
 */
public class TaskContainer {
    /**
     * 单项 线程安全 链表
     *
     * @see SimpleFastQueue
     */
    private final Queue<Task> taskQueue;

    /**
     * 优先队列,存放优先任务
     */
    private final PriorityBlockingQueue<Task> priorityQueue;


    {

//        taskQueue = new SimpleFastQueue<Task>(); //性能不好换这个
        taskQueue = new ConcurrentLinkedQueue<Task>();
        priorityQueue = new PriorityBlockingQueue<Task>();
    }

    public TaskContainer() {
    }

    public void offerQueue(Task task) {
        taskQueue.offer(task);
    }

    public void offerPriorityQueue(Task task) {
        //每次都将时间id更新
        task.setTimeId(System.currentTimeMillis() + task.getRetryInterval());
        priorityQueue.offer(task);
    }

    public Task pollQueue(){
        return taskQueue.poll();
    }

    public Task pollPriorityQueue(){
        return priorityQueue.poll();
    }

    public List<Task> pollPriorityList(long time){
        ArrayList<Task> ans = new ArrayList<Task>();
        Task temp = priorityQueue.poll();
        while (temp != null){
            if (temp.getTimeId() < time){
                ans.add(temp);
            }else{
                priorityQueue.offer(temp);
                return ans;
            }
            temp = priorityQueue.poll();
        }
        return ans;
    }

}
