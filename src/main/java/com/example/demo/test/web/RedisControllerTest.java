package com.example.demo.test.web;

import com.example.demo.common.utils.ThreadPool;
import com.example.demo.interfaces.redis.ExRedisUtils;
import com.example.demo.interfaces.redis.RedisUtil;
import com.example.demo.test.vo.Result;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.test.manage.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController

public class RedisControllerTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ExRedisUtils exRedisUtils;

    @Autowired
    private Test test; // 注入bean



    @RequestMapping(value = "/test" , method = RequestMethod.GET)
    public Result<Boolean> test(String id , String id1 , String entityType){
        Result<Boolean>  result = new Result<>();
        Map<String, Object> map = null;// 假如这就是你哪个2个参数的方法
        try {
            map = test.test(id, id1);
        } catch (ApplicationException e) {
            result.setData(false);
            return result;
        }

        boolean b = (Boolean) map.get("code");
        if(!b){ // b = true 有试点   取反就是没有试点
            result.setData(false);
            return result;
        }

        List<String> list = new ArrayList<>();// 假如这是试点集合
        String str = list.stream().filter(i -> i.equals(entityType)).findAny().orElse(null); // 匹配类型  返回试点  假如这个String就是匹配的试点类型

        // 如果不考虑  结果的Code  和  message  的话 这样会简洁一点

        result.setData(str != null);// 等于null  说明没有匹配到类型
        return result;
    }


    @RequestMapping(value = "/testRedisSet" , method = RequestMethod.GET)
    public void setRedisMethod(){
        redisUtil.set("testredisUtil","redisUtil set");
        exRedisUtils.set("testexRedisUtils","redisUtil set");
    }

    @RequestMapping(value = "/testRedisRemove" , method = RequestMethod.GET)
    public void testRedisRemove(){
        exRedisUtils.del("testredisUtil");
        exRedisUtils.del("testexRedisUtils");
    }


    private final ThreadPool threadPool = new ThreadPool(2);

    @RequestMapping(value = "/testThread" , method = RequestMethod.GET)
    public void testThreadPool(){
        threadPool.runAsync(()->{
            for(int i = 1;i <= 10; i++){
                sleep();
                System.out.println("test 1 : " + i);
            }
        });
        threadPool.runAsync(()->{
            for(int i = 1;i <= 10; i++){
                sleep();
                System.out.println("test 2 : " + i);
            }
        });
        threadPool.runAsync(()->{
            for(int i = 1;i <= 10; i++){
                sleep();
                System.out.println("test 3 : " + i);
            }
        });
        threadPool.runAsync(()->{
            for(int i = 1;i <= 10; i++){
                sleep();
                System.out.println("test 4 : " + i);
            }
        });
    }

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
