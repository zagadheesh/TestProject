/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predic8lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import gsonlib.GsonRecursive;
import static gsonlib.GsonRecursive.makeRecursive;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jagadeesh.t
 */
public class SoapRequestCreator {

    private static Map<String, Definitions> defsMap = new HashMap<String, Definitions>();
    private static WSDLParser parser = new WSDLParser();

    public static synchronized Definitions getDefinitions(String keyName, String url) {

        Definitions defs = defsMap.get(keyName);
        if (defs != null) {
            return defs;
        } else {
            Definitions wsdl = parser.parse(url);
            defsMap.put(keyName, wsdl);
            return wsdl;
        }
    }

    public static Map<String, String> getReqMapFromJson(String json) throws Exception {
        JsonParser parser = new JsonParser();

        JsonElement rootEle = parser.parse(json);

        GsonRecursive.makeRecursive(rootEle);
        return GsonRecursive.reqMap;
    }

    public static void main1(String[] args) throws Exception {

        String url = "http://localhost:3535/WSTest/sample?wsdl";

        Definitions wsdl = getDefinitions("jag", url);

        StringWriter writer = new StringWriter();

        Map<String, String> formParams = new HashMap<String, String>();
//        formParams.put("xpath:/putArrayObj/objs[1]/name", "ramana");
//        formParams.put("xpath:/putArrayObj/objs[1]/age", "20.3");
//
//        formParams.put("xpath:/putArrayObj/objs[2]/name", "ramana");
//        formParams.put("xpath:/putArrayObj/objs[2]/age", "20.3");

//        formParams = getReqMapFromJson("{\n"
//                + "    \"objs\": [\n"
//                + "        {\n"
//                + "            \"age\": \"20.3\",\n"
//                + "            \"name\": \"ranjan\"\n"
//                + "        },\n"
//                + "        {\n"
//                + "            \"age\": \"20.99\",\n"
//                + "            \"name\": \"manjan\"\n"
//                + "        }\n"
//                + "    ]\n"
//                + "}");
        String json = "{\n"
                + "    \"objs\": [\n"
                + "        {\n"
                + "            \"age\": \"20.3\",\n"
                + "            \"name\": \"ranjan\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"age\": \"20.99\",\n"
                + "            \"name\": \"manjan\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        formParams = GsonUtil.getPredic8ReqMap(json, "xpath:/putArrayObj/");
        System.out.println(formParams);

        //SOARequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(writer));
        creator.setFormParams(formParams);

        //creator.createRequest(PortType name, Operation name, Binding name);
        creator.createRequest("HelloWorld", "putpersonaldetails", "HelloWorldImplPortBinding");

        String soapRequest = writer.toString();

//        System.out.println(writer);
        String resp = postURLCall(url, soapRequest);

        System.out.println("response :: " + resp);

    }

    public static void main(String[] args) throws Exception {

        String url = "http://localhost:3535/WSTest/sample?wsdl";

        Definitions wsdl = getDefinitions("jag", url);

        StringWriter writer = new StringWriter();

        HashMap<String, String> formParams = new HashMap<String, String>();
        formParams.put("xpath:/putArray/putarrayparam[1]", "ramana");
        formParams.put("xpath:/putArray/putarrayparam[2]", "20.3");

        //SOARequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(writer));

        SOARequestCreator creator1 = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(writer));

//        creator.setFormParams(formParams);
        //creator.createRequest(PortType name, Operation name, Binding name);
        creator.createRequest("HelloWorld", "putPersonalDetails", "HelloWorldImplPortBinding");
        
        

        String soapRequest = writer.toString();

        System.out.println("soap request :: " + soapRequest);
//        String resp = postURLCall(url, soapRequest);
//
//        System.out.println("response :: " + resp);

    }

    public static String postURLCall(String strURL, String strURLParams) throws Exception {
        int nResponseCode = 500;
        URL url = null;
        HttpURLConnection httpURLConn = null;
        BufferedReader bReader = null;
        DataOutputStream dos = null;
        try {
            url = new URL(strURL);
            httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.setRequestMethod("POST");
            httpURLConn.setConnectTimeout(5000); //5 Sec
            httpURLConn.setReadTimeout(5000); //5 sec
            httpURLConn.setDoOutput(true);
            httpURLConn.setDoInput(true);
            httpURLConn.setRequestProperty("Content-Type", "text/xml");
            httpURLConn.setRequestProperty("Content-Length", strURLParams.length() + "");
            //httpURLConn.setRequestProperty("servicekey", "12345667");
            dos = new DataOutputStream(httpURLConn.getOutputStream());

            if (strURLParams != null) {
                dos.writeBytes(strURLParams);
            }
            nResponseCode = httpURLConn.getResponseCode();
            System.out.println("Response Code : " + nResponseCode);
            if (nResponseCode == 200) {
                StringBuilder strBuilder = new StringBuilder();
                bReader = new BufferedReader(new InputStreamReader(httpURLConn.getInputStream()));
                String strLine = null;
                while ((strLine = bReader.readLine()) != null) {
                    strBuilder.append(strLine);
                }
                return strBuilder.toString();
            } else {
                throw new IOException(httpURLConn.getResponseMessage());
            }
        } catch (MalformedURLException malEx) {
            System.out.println("postURLCall() : Ex : " + malEx.getMessage());
            throw new MalformedURLException(httpURLConn.getResponseMessage());

        } catch (IOException ioEx) {
            System.out.println("postURLCall() : Ex : " + ioEx.getMessage());
            throw new IOException(httpURLConn.getResponseMessage());

        } finally {
            try {
                if (bReader != null) {
                    bReader.close();
                }
                if (dos != null) {
                    dos.flush();
                    dos.close();
                }
                if (httpURLConn != null) {
                    httpURLConn.disconnect();
                }
            } catch (Exception e) {
                System.out.println("postURLCall() : While closing resources. Ex : " + e.getMessage());

            }
        }
    }

}
