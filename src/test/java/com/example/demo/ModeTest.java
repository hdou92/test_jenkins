package com.example.demo;


import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModeTest {

    public static void main(String[] args) {
        Map<String , String> map = new HashMap<>();
        map.put("a","1");
        map.put("b","2");
        map.put("c","3");
        map.put("d","4");
        map.put("e","5");
        String json = JSONObject.toJSONString(map);
        System.out.println(json);
        JSONObject jasonObject = JSONObject.parseObject(json);
        Map<String , String> map1 =(Map)jasonObject;
        System.out.println(map1);
    }
}
