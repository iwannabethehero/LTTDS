package com.hanlzz.demo.lttds.utils;

import sun.misc.Unsafe;

/**
 * @author h
 */
public class ThreadSemaphore {

    private volatile int count;

    private final Unsafe unsafe = TaskUtils.getUnsafe();

    private final long count_offset = getCountOffset();

    ThreadSemaphore(int count){
        this.count = count;
    }

    public boolean tryAcquire(){
        int c = count;
        if (c != 0){
            return compareAndSetCount(c,c-1);
        }
        return false;
    }

    public void acquire(){
        for (;!tryAcquire();) {
        }
    }

    public void release(){
        for (;;){
            int c = count;
            if (compareAndSetCount(c,c+1)){
                break;
            }
        }
    }


    private boolean compareAndSetCount(int exp, int o) {
        return unsafe.compareAndSwapInt(this, count_offset, exp, o);
    }

    private long getCountOffset() {
        try {
            return unsafe.objectFieldOffset(ThreadSemaphore.class.getDeclaredField("count"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("getFieldOffset exception occurred", e);
        }
    }
}
