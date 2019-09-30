package com.hanlzz.demo.lttds.container;


/**
 * 静态代理类
 * @author h
 */
public class BaseTaskServiceImpl implements ITaskService {

    private Class obj;

    private String methodName;

    private Class fnObj;

    private String fnMethodName;

    private Object param;

    BaseTaskServiceImpl(Class obj,String methodName,Class fnObj,String fnMethodName,Object param){
        this.obj = obj;
        this.fnMethodName = fnMethodName;
        this.fnObj = fnObj;
        this.methodName = methodName;
        this.param = param;
    }




    @Override
    public boolean invoke(Object param) {

        return false;
    }

    @Override
    public void failedEnd(Object param) {

    }
}
