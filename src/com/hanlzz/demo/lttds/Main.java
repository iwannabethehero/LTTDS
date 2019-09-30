package com.hanlzz.demo.lttds;

import com.hanlzz.demo.lttds.utils.TaskUtils;
import com.hanlzz.demo.lttds.container.Task;

public class Main {

    volatile private String a = "aaa";

    public static void main(String[] args) throws InterruptedException {
//        final Main main = new Main();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    main.a =  "bbbb";
//                    System.out.println(main.a);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        synchronized (main){
//            Thread.sleep(5000);
//        }

        TaskUtils.openTaskService();
        System.out.println("end");

//        for (int i = 0; i < 800; i++) {
//            final int ii = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    TaskUtils.offerTask(new Task(1,new TestService("普通"+ii),null));
////                    TaskUtils.offerTask(new Task(1,new TestService("优先"+ii),null,3000));
//                }
//            }).start();
//
//        }

        Thread.sleep(2000);
//        for (int i = 0; i < 1000; i++) {
//            final int ii = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    TaskUtils.offerTask(new Task(2,new TestService("普通"+ii),null));
////                    TaskUtils.offerTask(new Task(2,new TestService("优先"+ii),null,2000));
//                }
//            }).start();
//
//        }
//        Thread.sleep(20000);

    }


}
