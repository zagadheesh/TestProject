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
import com.google.gson.JsonPrimitive;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jagadeesh.t
 */
public class GsonTransfer {

    public static void main(String[] args) {
        String inJson = "{\"envelope\":{\"body\":{\"getPersonalObjResponse\":{\"getPersonalObj\":{\"person\":{\"addrs\":[{\"PIN\":\"pin1\",\"street\":\"street1\"},{\"PIN\":\"pin2\",\"street\":\"street2\"}],\"age\":\"age1\",\"name\":\"n1\"},\"personalDetails\":{\"address\":{\"PIN\":\"pin3\",\"street\":\"street3\"},\"wage\":\"wage1\",\"wname\":\"wn1\"},\"personalType\":\"ptype1\"}}}}}";
        String outJson = "{\"envelope\":{\"body\":{\"getPersonalObjResponse\":{\"getPersonalObj\":{\"person\":{\"addrs\":[{\"PIN\":\"def\",\"street\":\"abc\"},{\"PIN\":\"2\",\"street\":\"1\"}],\"age\":\"12.3\",\"name\":\"jag\"},\"personalDetails\":{\"address\":{\"PIN\":\"502325\",\"street\":\"lax\"},\"wage\":\"12.6\",\"wname\":\"wwwname\"},\"personalType\":\"sampletype\"}}}}}";
        Map<String,String> map=new HashMap<>();
        JsonParser parser=new JsonParser();
        JsonElement inJsonEle = parser.parse(inJson);
        JsonElement outJsonEle = parser.parse(outJson);
        transferJsonResponseParamMap(inJsonEle.getAsJsonObject(), outJsonEle.getAsJsonObject(), map);
        System.out.println(map);
    }

    public static void transferJsonResponseParamMap(JsonObject inJson, JsonObject outJson, Map<String, String> ctxMap) {

        Set<Map.Entry<String, JsonElement>> inJsonEntrySet = inJson.entrySet();

        for (Map.Entry<String, JsonElement> inJsonEntry : inJsonEntrySet) {
            String inJsonEntryKey = inJsonEntry.getKey();
            JsonElement inJsonEntryValue = inJsonEntry.getValue();

            JsonElement outJsonEntryValue = outJson.get(inJsonEntryKey);

            if (outJsonEntryValue != null) {
                if (inJsonEntryValue.isJsonPrimitive() && outJsonEntryValue.isJsonPrimitive()) {
                    JsonPrimitive inPrimitive = inJsonEntryValue.getAsJsonPrimitive();
                    JsonPrimitive outPrimitive = outJsonEntryValue.getAsJsonPrimitive();
                    ctxMap.put(inPrimitive.getAsString(), outPrimitive.getAsString());
                } else if (inJsonEntryValue.isJsonObject() && outJsonEntryValue.isJsonObject()) {
                    JsonObject outJsonEntryValObj = outJsonEntryValue.getAsJsonObject();
                    JsonObject inJsonEntryValObj = inJsonEntryValue.getAsJsonObject();
                    transferJsonResponseParamMap(inJsonEntryValObj, outJsonEntryValObj, ctxMap);
                } else if (inJsonEntryValue.isJsonArray() && outJsonEntryValue.isJsonArray()) {
                    JsonArray inJsonEntryValArr = inJsonEntryValue.getAsJsonArray();
                    JsonArray outJsonEntryValArr = outJsonEntryValue.getAsJsonArray();
                    int outSize = outJsonEntryValArr.size();
                    for (int i = 0; i < inJsonEntryValArr.size(); i++) {
                        if (outSize > i) {
                            JsonElement inArrEle = inJsonEntryValArr.get(i);
                            JsonElement outArrEle = outJsonEntryValArr.get(i);
                            if (inArrEle.isJsonPrimitive() && outArrEle.isJsonPrimitive()) {
                                JsonPrimitive inPrimitive = inArrEle.getAsJsonPrimitive();
                                JsonPrimitive outPrimitive = outArrEle.getAsJsonPrimitive();
                                ctxMap.put(inPrimitive.getAsString(), outPrimitive.getAsString());
                            } else if (inArrEle.isJsonObject() && outArrEle.isJsonObject()) {
                                JsonObject outJsonEntryValObj = outArrEle.getAsJsonObject();
                                JsonObject inJsonEntryValObj = inArrEle.getAsJsonObject();
                                transferJsonResponseParamMap(inJsonEntryValObj, outJsonEntryValObj, ctxMap);
                            }
                        }
                    }
                }
            }

        }

    }

}
