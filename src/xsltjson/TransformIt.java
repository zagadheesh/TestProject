/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xsltjson;

import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author jagadeesh.t
 */
public class TransformIt {

    public static void main(String[] args) {
        JSONObject obj = XML.toJSONObject("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "   <S:Body>\n"
                + "      <ns2:sayByeResponse xmlns:ns2=\"http://ws.imi.com/\">\n"
                + "         <byeresp>\n"
                + "            <age>20.99</age>\n"
                + "            <name>gopal</name>\n"
                + "         </byeresp>\n"
                + "      </ns2:sayByeResponse>\n"
                + "   </S:Body>\n"
                + "</S:Envelope>");
        String s = obj.toString(4);
        System.out.println(s);
        System.out.println("ttt");
    }

}
