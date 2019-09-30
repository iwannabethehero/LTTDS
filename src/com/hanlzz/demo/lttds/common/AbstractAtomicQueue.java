package com.hanlzz.demo.lttds.common;

import com.hanlzz.demo.lttds.utils.TaskUtils;
import sun.misc.Unsafe;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;


/**
 * @param <T>
 * @author h
 */
abstract public class AbstractAtomicQueue<T> implements AtomicOperation, Queue<T> {
    private final Unsafe unsafe;

    protected volatile Node head;
    protected volatile Node tail;
    protected volatile int size;

    private final long head_offset;
    private final long tail_offset;
    private final long size_offset;
    private final long node_offset;
    private final long item_offset;

    {
        unsafe = TaskUtils.getUnsafe();
        size = 0;
        head_offset = getFieldOffset(AbstractAtomicQueue.class, "head");
        tail_offset = getFieldOffset(AbstractAtomicQueue.class, "tail");
        size_offset = getFieldOffset(AbstractAtomicQueue.class, "size");
        node_offset = getFieldOffset(Node.class, "next");
        item_offset = getFieldOffset(Node.class, "item");
        head = new Node();
        tail = head;
    }


    class Node {
        T item;
        Node next;

        Node(T item) {
            this.item = item;
        }

        private Node() {
        }

        protected boolean casNext(Node exp, Node val) {
            return unsafe.compareAndSwapObject(this, node_offset, exp, val);
        }

        protected boolean casItem(T exp, T val) {
            return unsafe.compareAndSwapObject(this, item_offset, exp, val);
        }



    }

    Node newNode(T item) {
        if (item == null) {
            throw new NullPointerException("queue item can not be null");
        }
        return new Node(item);
    }

    protected boolean compareAndSetHead(Object exp, Object o) {
        return unsafe.compareAndSwapObject(this, head_offset, exp, o);
    }

    protected boolean compareAndSetTail(Object exp, Object o) {
        return unsafe.compareAndSwapObject(this, tail_offset, exp, o);
    }

    protected void lazyPutHead(Node h,Node p) {
        if (h!=p && compareAndSetHead(h,p)){
            unsafe.putOrderedObject(h,node_offset,h);
        }
    }

    public boolean compareAndSetSize(int exp, int o) {
        return unsafe.compareAndSwapInt(this, size_offset, exp, o);
    }

    public void incrSize() {
        for (; ; ) {
            int exp = size;
            boolean flag = unsafe.compareAndSwapInt(this, size_offset, exp, exp + 1);
            if (flag) {
                break;
            }
        }
    }

    public void decrSize() {
        for (; ; ) {
            int exp = size;
            if (exp > 0) {
                boolean flag = unsafe.compareAndSwapInt(this, size_offset, exp, exp - 1);
                if (flag) {
                    break;
                }
            }
        }
    }


    private long getFieldOffset(Class clazz, String name) {
        try {
            return unsafe.objectFieldOffset(clazz.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("getFieldOffset exception occurred", e);
        }
    }


    protected boolean casNodeNext(Node node, Node exp, Node val) {
        return unsafe.compareAndSwapObject(node, node_offset, exp, val);
    }


    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operations!");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operations!");
    }



}
