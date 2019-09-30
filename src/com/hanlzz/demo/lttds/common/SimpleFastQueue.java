package com.hanlzz.demo.lttds.common;

import java.util.Collection;

/**
 * @author h
 */
public class SimpleFastQueue<T> extends AbstractAtomicQueue<T> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return peek() == null;
    }

    @Override
    public boolean add(T t) {
        return offer(t);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            return true;
        }
        Node h = null, p = null;
        for (T t : c) {
            if (h == null) {
                p = h = newNode(t);
            } else {
                p.next = newNode(t);
                p = p.next;
            }
        }
        offerList(h, p);
        return false;
    }

    private void offerList(Node h, Node t) {
        //TODO
    }

    @Override
    public void clear() {
        while (poll() != null){}
    }

    @Override
    public boolean offer(T e) {
        if (e == null) {
            throw new NullPointerException("queue item can not be null !");
        }
        Node newNode = newNode(e);
        for (Node t = tail, p = t; ; ) {
            Node q = p.next;
            if (q == null) {
                if (p.casNext(null, newNode)) {
                    if (p != t) {
                        compareAndSetTail(t, newNode);
                    }
                    return true;
                }
            } else if (p == q) {
                p = (t != (t = tail)) ? t : head;
            } else {
                p = (p != t && t != (t = tail)) ? t : q;
            }
        }
    }

    @Override
    public T remove() {
        return poll();
    }

    @Override
    public T poll() {
        restart:
        for (; ; ) {
            for (Node h = head, p = h, q; ; p = q) {
                final T item;
                if ((item = p.item) != null && p.casItem(item, null)) {
                    if (p != h) {
                        lazyPutHead(h, ((q = p.next) != null) ? q : p);
                    }
                    return item;
                } else if ((q = p.next) == null) {
                    lazyPutHead(h, p);
                    return null;
                } else if (p == q) {
                    continue restart;
                }
            }
        }
    }


    @Override
    public T element() {
        T t = peek();
        if (t != null) {
            return t;
        }
        throw new NullPointerException("queue is empty!");
    }

    @Override
    public T peek() {
        restart:
        for (; ; ) {
            for (Node h = head, p = h, q; ; p = q) {
                final T item;
                if ((item = p.item) != null
                        || (q = p.next) == null) {
                    lazyPutHead(h, p);
                    return item;
                } else if (p == q) {
                    continue restart;
                }
            }
        }
    }
}
