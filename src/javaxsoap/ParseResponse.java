/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaxsoap;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jagadeesh.t
 */
public class ParseResponse {

    public static String resString = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
            + "   <S:Body>\n"
            + "      <ns2:sayByeResponse xmlns:ns2=\"http://ws.imi.com/\">\n"
            + "         <byeresp>\n"
            + "            <age>20.99</age>\n"
            + "            <name>gopal</name>\n"
            + "         </byeresp>\n"
            + "      </ns2:sayByeResponse>\n"
            + "   </S:Body>\n"
            + "</S:Envelope>";

    public static void main(String[] args) throws Exception {
        parse();
    }

    public static void parse() throws Exception {
        HashMap<String, String> responseMsg = new HashMap();
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message1 = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(resString.getBytes(Charset.forName("UTF-8"))));
        SOAPBody body1 = message1.getSOAPBody();
        NodeList list1 = body1.getChildNodes();
        if (list1 != null && list1.getLength() > 0) {
            System.out.println(" Nodes Size:::" + list1.getLength());
            for (int i = 0; i < list1.getLength(); i++) {
                Node node = list1.item(i);
                System.out.println("child " + i + " :: " + node.getNodeName());

                NodeList innerList = list1.item(i).getChildNodes();
                if (innerList != null && innerList.getLength() > 0) {
                    for (int j = 0; j < innerList.getLength(); j++) {
                        responseMsg.put(innerList.item(j).getNodeName(), innerList.item(j).getTextContent());
                    }
                }
            }
        }
    }

}
