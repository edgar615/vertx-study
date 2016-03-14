package com.edgar.vertx.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Administrator on 2015/8/31.
 */
public class JSONExample {

    public static void main(String[] args) {
        //Creating JSON objects
        String jsonString = "{\"foo\":\"bar\"}";
        JsonObject object = new JsonObject(jsonString);

        //Putting entries into a JSON object
        object = new JsonObject();
        object.put("foo", "bar").put("num", 123).put("mybool", true);

        //Getting values from a JSON object
        String val = object.getString("foo");
        System.out.println(val);
        int intVal = object.getInteger("num");
        System.out.println(intVal);

        //Creating JSON arrays
        String jsonArrayString = "[\"foo\",\"bar\"]";
        JsonArray array = new JsonArray(jsonArrayString);

        //Adding entries into a JSON array
        array = new JsonArray();
        array.add("foo").add(123).add(false);

        //Getting values from a JSON array
        String val1 = array.getString(0);
        System.out.println(val1);
        Integer intVal1 = array.getInteger(1);
        System.out.println(intVal1);
        Boolean boolVal1 = array.getBoolean(2);
        System.out.println(boolVal1);
    }
}
