/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsonlib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jagadeesh.t
 */
public class GsonRecursive {

    static String json;
    public static Map<String, String> reqMap = new HashMap<String, String>();

    static {
        json = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\jagadeesh.t\\Documents\\NetBeansProjects\\TestProject\\src\\gsonlib\\retry-config.json"));
            String s = null;
            while ((s = br.readLine()) != null) {
                json += s;
            }
            System.out.println(json);
        } catch (Exception ex) {
            Logger.getLogger(GsonTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(GsonTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JsonParser parser = new JsonParser();

        JsonElement rootEle = parser.parse(json);

        makeRecursive(rootEle);
        if (!reqMap.isEmpty()) {
            for (Map.Entry<String, String> entrySet : reqMap.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                System.out.println("key :: " + key + "###########" + "  value :: " + value);

            }
        }

    }

    static String xpathVal = "xpath:/putArrayObj/";
    static String recXpathVal = xpathVal;
//    static boolean isInJsonArray = false;

    public static void makeRecursive(JsonElement ele) {
        JsonObject jsonObj = ele.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
        for (Map.Entry<String, JsonElement> innerSet : entrySet) {
            String key = innerSet.getKey();

            JsonElement value = innerSet.getValue();

            if (value.isJsonObject()) {

                recXpathVal += key + "/";
                makeRecursive(value);
                recXpathVal = getSubstringToParent(recXpathVal) + "/";
            } else if (value.isJsonArray()) {
                JsonArray jArray = value.getAsJsonArray();
                for (int i = 0; i < jArray.size(); i++) {
                    JsonElement arrEle = jArray.get(i);
                    recXpathVal += key + "[" + (i + 1) + "]/";
                    if (arrEle.isJsonObject()) {
                        makeRecursive(arrEle);

                    } else if (arrEle.isJsonPrimitive()) {
                        put(recXpathVal, arrEle.getAsString());

                    }
                    recXpathVal = getSubstringToParent(recXpathVal) + "/";

                }

            } else if (value.isJsonPrimitive()) {
                recXpathVal += key + "/";

                put(recXpathVal.toString(), value.getAsString());
                recXpathVal = getSubstringToParent(recXpathVal) + "/";

            }

        }

    }

    public static void put(String key, String value) {
//        System.out.println("putting key : " + key);
//        System.out.println("putting value : " + value);
        reqMap.put(key, value);
    }

    public static String getSubstringToParent(String str) {
        System.out.println("getting substring for " + str);
        for (int i = 1; i <= 2; i++) {
            str = getSubstringToLastTraverse(str);
        }

        return str;
    }

    public static String getSubstringToLastTraverse(String str) {
        return str.substring(0, str.lastIndexOf("/"));
    }

    public static String removeQuotesFromGsonString(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static void usingJsonReader() throws IOException {

    }
    
    

}
