package com.example.demo.test.manage;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 假如 我是一个交给spring注入的bean
 */
@Component
public class Test {

    public Map<String, Object> test(String id , String id1) throws ApplicationException {
        List<String> list = new ArrayList<>();// 假如这是试点集合
        String s = "a";
        list.add("a");
        list.add("aa");
        list.add("aa");
        list.add("aa");
        if(StringUtils.isEmpty(id)){
//            return false;// 假如参数为空
        }
        if(StringUtils.isEmpty(id1)){
//            return false;// 假如参数为空
        }
        if(CollectionUtils.isEmpty(list)){
//            return false; // 假如试点为空
        }
        Stream<String> stream = list.stream().filter(i ->
            s.equals(i)
        );
        System.out.println(stream.collect(Collectors.toList()));
        System.out.println(list.toString());
        // 遍历试点集合
        for(String i :  list){
            if(s.equals(i)){
                // 是试点
//                return true;
            }
        }
        for(String i :  list){
            if(id1.equals(s)){
                // 是试点
//                return true;
            }
        }
        return null;
    }
    public static void  printValur(String str){
        System.out.println("print value : "+str);
    }

    public static void main(String[] args) {
//        new Test().test("1","2");
//        List<String> al = Arrays.asList("a", "b", "c", "d");
//        al.forEach(Test::printValur);
//        //下面的方法和上面等价的
//        Consumer<String> methodParam = Test::printValur; //方法参数
//        al.forEach(x -> methodParam.accept(x));//方法执行accept
//        String s = "2";
//        List<String> list = new ArrayList<>();// 假如这是试点集合
//        list.add("1");
//        list.add("1");
//        list.add("3");
//        String str = list.stream().filter(i -> i.equals(s)).findAny().orElse("123");
//        System.out.println(str);

//        StringBuilder sendMessage = new StringBuilder("【易普森智慧健康】您好!");
//        sendMessage.append("\n机构: 第一医院" );
//        sendMessage.append("\n编号: 3a04551c507c4aa3be6c086d52297ef0" );
//        sendMessage.append("\n名称: 机器人" );
//        sendMessage.append("\n楼层: 9" );
//        sendMessage.append("\n位置: 前台");
//        sendMessage.append("\n附近发生行驶阻挡故障，请尽快处理！");
//        System.out.println(sendMessage.toString());

        Class<?> clazz =  Test.class;
        System.out.println(clazz.getSimpleName());
    }
}
