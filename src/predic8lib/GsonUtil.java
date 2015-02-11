/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predic8lib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jagadeesh.t
 */
public class GsonUtil {

    public static Map<String, String> getPredic8ReqMap(String json, String recXpathVal) {
        JsonParser parser = new JsonParser();
        JsonElement rootEle = parser.parse(json);
        Map<String, String> reqMap = new HashMap<String, String>();
        preparePredic8Map(rootEle, recXpathVal, reqMap);
        return reqMap;
    }

    public static void preparePredic8Map(JsonElement ele, String recXpathVal, Map<String, String> reqMap) {
        JsonObject jsonObj = ele.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
        for (Map.Entry<String, JsonElement> innerSet : entrySet) {
            String key = innerSet.getKey();

            JsonElement value = innerSet.getValue();

            if (value.isJsonObject()) {

                recXpathVal += key + "/";
                preparePredic8Map(value, recXpathVal, reqMap);
                recXpathVal = getSubstringToParent(recXpathVal) + "/";
            } else if (value.isJsonArray()) {
                JsonArray jArray = value.getAsJsonArray();
                for (int i = 0; i < jArray.size(); i++) {
                    JsonElement arrEle = jArray.get(i);
                    recXpathVal += key + "[" + (i + 1) + "]/";
                    if (arrEle.isJsonObject()) {
                        preparePredic8Map(arrEle, recXpathVal, reqMap);

                    } else if (arrEle.isJsonPrimitive()) {
                        reqMap.put(recXpathVal, arrEle.getAsString());

                    }
                    recXpathVal = getSubstringToParent(recXpathVal) + "/";

                }

            } else if (value.isJsonPrimitive()) {
                recXpathVal += key + "/";

                reqMap.put(recXpathVal.toString(), value.getAsString());
                recXpathVal = getSubstringToParent(recXpathVal) + "/";

            }

        }

    }

    public static String getSubstringToParent(String str) {
//        System.out.println("getting substring for " + str);
        for (int i = 1; i <= 2; i++) {
            str = getSubstringToLastTraverse(str);
        }

        return str;
    }

    public static String getSubstringToLastTraverse(String str) {
        return str.substring(0, str.lastIndexOf("/"));
    }
    
  

}
