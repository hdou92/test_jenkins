package com.example.demo.test.manage;

import redis.clients.jedis.BinaryClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test1 {

    public static final String ATB = "ATB";
    public static final String ATC = "ATC";

//    public static void main(String[] args) {
//
////        List<Point> lists = new ArrayList<>(); // 假设是试点集合
////        lists.add(new Test1().new Point("ATB"));
////        lists.add(new Test1().new Point("ATC"));
////        Point p = lists.stream().filter(e -> ATB.equals(e.type)).findAny().orElse(null); // 常量对比
////        Point p1 = lists.stream().filter(e -> Value.ATC.name.equals(e.type)).findAny().orElse(null); // 枚举对比
////        List<Point> l = lists.stream().filter(e -> Value.ATC.name.equals(e.type)).collect(Collectors.toList());
////        System.out.println(l);
////        System.out.println(p);
////        System.out.println(p1);
//
//        ArrayList<Object> list = null; // 假如这就是你某个位置的集合
//        list = (ArrayList<Object>) new Test1().isNotEmpty(list); //  假如是null
//        System.out.println(list); // 结果为空集合
//
//
//        ArrayList<Object> list1 = new ArrayList<>(); // 假如这就是你某个位置的集合
//        list1 = (ArrayList<Object>) new Test1().isNotEmpty(list1); //  假如是空集合
//        System.out.println(list1); // 结果为空集合
//
//        ArrayList<Object> list2 = new ArrayList<>(); // 假如这就是你某个位置的集合
//        list2.add("123");
//        list2 = (ArrayList<Object>) new Test1().isNotEmpty(list2); //  假如是null
//        System.out.println(list2);// 结果为有值的集合
//    }

    public static void main(String[] areg){
        Map<String, Point> map = new ConcurrentHashMap<>();
        map.computeIfAbsent("a" , key -> new Test1().new Point("123"));

        System.out.println(map);

        System.out.println("ste".equals(null));
    }

    /**
     * 判断数组  如果参数不等如 null就 返回参数本身
     * @param list 要判断的参数
     * @return 不为null
     */
    public <T> List<T> isNotEmpty(List<T> list){
        return list == null ? new ArrayList<>() : list;
    }


    /**
     * size
     */
    public static <T> int getSize(Collection<T> list) {
        return list == null ? 0 : list.size();
    }

    public enum Value{
        ATB("ATB"),ATC("ATC");
        private String name;
        Value(String name){
            this.name = name;
        }
    }
    public class Point{
        private String type;
        Point(String type){ this.type = type;}

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "type='" + type + '\'' +
                    '}';
        }
    }
}
