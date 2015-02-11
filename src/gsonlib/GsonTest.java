/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsonlib;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class GsonTest {

    static String json;
    static Map<String, String> reqMap = new HashMap<String, String>();

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

    public static void main(String[] args) {
        JsonParser parser = new JsonParser();

        JsonElement rootEle = parser.parse(json);
//        System.out.println("rootEle "+rootEle);
//        JsonObject jsonObj = rootEle.getAsJsonObject();
//
//        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
//        for (Map.Entry<String, JsonElement> innerSet : entrySet) {
//            String key = innerSet.getKey();
//            JsonElement value = innerSet.getValue();
//
//            if (value.isJsonObject()) {
//                System.out.println("value :: " + value);
//                System.out.println("value is json object");
//            }
//            if (value.isJsonArray()) {
//                System.out.println("value :: " + value);
//                System.out.println("value is json array");
//            }
//
//        }

        makeRecursive(rootEle,null);
        if (!reqMap.isEmpty()) {
            for (Map.Entry<String, String> entrySet : reqMap.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                System.out.println("key :: " + key + "###########" + "  value :: " + value);

            }
        }

    }

    public static void main1(String[] args) {

//        String str = "\"\"";
//        System.out.println(removeQuotesFromGsonString(str));
        String str = "xpath:/create/article/price/amount/";
        System.out.println(getSubstringToParent(str) + "/");

    }

    static String xpathVal = "xpath:/printProfile/";
    static String recXpathVal = xpathVal;
    static boolean isInJsonArray = false;
    

    public static void makeRecursive(JsonElement ele,Integer objIndex) {
        JsonObject jsonObj = ele.getAsJsonObject();
//        System.out.println(jsonObj);
//        JsonElement glossaryEle = jsonObj.get("glossary");
//        System.out.println(glossaryEle);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
        for (Map.Entry<String, JsonElement> innerSet : entrySet) {
            String key = innerSet.getKey();
            JsonElement value = innerSet.getValue();
            
            if (value.isJsonObject()) {
                
                if (isInJsonArray) {
                    if (objIndex != null) {
                        recXpathVal += key + "[" + objIndex + "]/";
                    }
                } else {
                    recXpathVal += key + "/";
                }
//                System.out.println("value :: " + value);
//                System.out.println("value is json object");
                makeRecursive(value,objIndex);
            } else if (value.isJsonArray()) {
                JsonArray jArray = value.getAsJsonArray();
//                boolean isValAppended = false;
                for (int i = 0; i < jArray.size(); i++) {
                    JsonElement arrEle = jArray.get(i);

                    if (arrEle.isJsonObject()) {
                        recXpathVal += key + "/";
                        isInJsonArray = true;
                        objIndex = i + 1;
                        makeRecursive(arrEle,objIndex);

                    } else if (arrEle.isJsonPrimitive()) {
                        recXpathVal += key + "[" + (i + 1) + "]/";
                        reqMap.put(recXpathVal, arrEle.getAsString());
                        recXpathVal = getSubstringToParent(recXpathVal) + "/";

                    }
                    

                }
                objIndex = null;
                isInJsonArray = false;
//                System.out.println("value :: " + value);
//                System.out.println("value is json array");
            } else if (value.isJsonPrimitive()) {
                recXpathVal += key + "/";
//                System.out.println("###### " + value.getAsString());
                reqMap.put(recXpathVal.toString(), value.getAsString());
                recXpathVal = getSubstringToParent(recXpathVal) + "/";

            }
           

        }
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
