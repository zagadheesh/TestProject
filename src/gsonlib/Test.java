/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsonlib;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 *
 * @author jagadeesh.t
 */
public class Test {
    
    public static void main(String[] args) {
        String s="abc";
        JsonParser parser=new JsonParser();
        JsonElement parse = parser.parse(s);
        if(parse.isJsonObject()){
            System.out.println("json object");
        } else if(parse.isJsonPrimitive()){
            System.out.println("json primitive");
        }
    }
    
}
