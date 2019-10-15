package com.hanlzz.demo.lttds;

import com.hanlzz.demo.lttds.container.ITaskService;

public class TestService implements ITaskService {
    String name;
    @Override
    public boolean invoke(Object param) {
        System.out.println(name+"任务被执行啦");
        //
        return false;
    }

    @Override
    public void failedEnd(Object param) {
        System.out.println(name+"任务失败啦");
    }

    TestService(String name){
        this.name = name;
    }
    TestService(){}
}
