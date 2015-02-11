
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jagadeesh.t
 */
public class TestSOAP {

    public static void main(String[] args) {
        String s = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "   <S:Body>\n"
                + "      <ns2:getArrayObjResponse xmlns:ns2=\"http://ws.imi.com/\">\n"
                + "         <getArrayObj>\n"
                + "            <age>10.0</age>\n"
                + "            <name>name 1</name>\n"
                + "         </getArrayObj>\n"
                + "        \n"
                + "      </ns2:getArrayObjResponse>\n"
                + "   </S:Body>\n"
                + "</S:Envelope>";
        System.out.println(XML.toJSONObject(s).toString(4));

    }

    public static void main4(String[] args) {
        JSONObject jsonObj = new JSONObject("{\"personobj\":{\"type\":\"complex\",\"age\":\"20.3\",\"name\":\"olala\"}}");

        String xml = XML.toString(jsonObj);
        System.out.println(xml);
    }

    public static String getReplaceMessage(String soapEnv, String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        Document envDoc = builder.parse(new ByteArrayInputStream(soapEnv.getBytes()));
        Document xmlDoc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        System.out.println("root ele :: " + envDoc.getDocumentElement().getNodeName());

        return null;
    }

    public static void main2(String[] args) throws Exception {
        Map<String, String> inparamNVals = new HashMap<String, String>();
        inparamNVals.put("personobj", "{\"personobj\":{\"type\":\"complex\",\"age\":\"20.3\",\"name\":\"olala\"}}");

        MessageFactory factory = MessageFactory.newInstance();
        String xmlInput = "<s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>\n"
                + "  <s11:Body>\n"
                + "    <ns1:printProfile xmlns:ns1='http://ws.imi.com/'>\n"
                + "      <personobj>\n"
                + "        <age>??</age>\n"
                + "        <name>??</name>\n"
                + "      </personobj>\n"
                + "    </ns1:printProfile>\n"
                + "  </s11:Body>\n"
                + "</s11:Envelope>";

        String val = "{\"personobj\":{\"type\":\"complex\",\"age\":\"20.3\",\"name\":\"olala\"}}";
        JSONObject jsonObj = new JSONObject(val);
        val = XML.toString(jsonObj);

        System.out.println("final xml :: " + getReplaceMessage(xmlInput, val));
    }

    public static void main1(String[] args) throws SOAPException, IOException, ParserConfigurationException, SAXException {

        Map<String, String> inparamNVals = new HashMap<String, String>();
        inparamNVals.put("personobj", "{\"personobj\":{\"type\":\"complex\",\"age\":\"20.3\",\"name\":\"olala\"}}");

        MessageFactory factory = MessageFactory.newInstance();
        String xmlInput = "<s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>\n"
                + "  <s11:Body>\n"
                + "    <ns1:printProfile xmlns:ns1='http://ws.imi.com/'>\n"
                + "      <personobj>\n"
                + "        <age>??</age>\n"
                + "        <name>??</name>\n"
                + "      </personobj>\n"
                + "    </ns1:printProfile>\n"
                + "  </s11:Body>\n"
                + "</s11:Envelope>";
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xmlInput.getBytes(Charset.forName("UTF-8"))));
        SOAPBody body = message.getSOAPBody();

        NodeList list = body.getElementsByTagName("ns1:printProfile");

        for (int i = 0; i < list.getLength(); i++) {

            System.out.println("ns1 :: " + list.item(i).getNodeName());
            Node rootEle = list.item(i);

            NodeList innerList = list.item(i).getChildNodes();
            for (int j = 0; j < innerList.getLength(); j++) {
                Node innerEle = innerList.item(j);
                System.out.println("Node Name " + innerList.item(j).getNodeName());
                System.out.println("Node Attr " + innerList.item(j).hasAttributes());
                System.out.println("Node VALUE " + innerList.item(j).getNodeValue());

                String[] arr = innerList.item(j).getNodeName().split(":");
                if (arr.length == 1 && inparamNVals.get(arr[0]) != null) {
                    String val = inparamNVals.get(arr[0]);
                    try {
                        JSONObject jsonObj = new JSONObject(val);
                        val = XML.toString(jsonObj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//                    Document doc = builderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(val.getBytes()));
//
//                    System.out.println("val :: " + val);
//                    SOAPElement ele = SOAPFactory.newInstance().createElement(new QName("abc"));
//                    parent.replaceChild(ele, innerEle);
//                    parent.removeChild(innerEle);
//                    Node cloneNode = parent.cloneNode(true);
//                    cloneNode.appendChild(ele);
//                   body.removeChild(parent);
//                   body.addDocument(doc);
//                    System.out.println("doc :: "+doc.getDocumentElement());
//                    nodeItem.appendChild(doc);
//                    body.addDocument(doc);
//                    innerList.item(j).setTextContent(val);
                }

            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        System.out.println("after replacing :: " + new String(baos.toByteArray(), "UTF-8"));
    }

    public void replaceComplexValues(Node node, JSONObject obj) {

    }

}
