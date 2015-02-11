
import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaParser;
import com.predic8.schema.Sequence;
import com.predic8.schema.TypeDefinition;
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Import;
import com.predic8.wsdl.Input;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.Types;
import com.predic8.wsdl.WSDLParser;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jagadeesh.t
 */
public class WSDLTest {

    public static void main(String[] args) throws Exception {
        String wsdlURL = "http://localhost:3535/WSTest/sample?wsdl";
        WSDLParser parser = new WSDLParser();

        Definitions mainDefs = parser.parse(wsdlURL);

        List<Import> mainImports = mainDefs.getImports();
//        System.out.println(mainImports.size());
//        System.out.println(mainDefs);
        List<Types> mainTypes = mainDefs.getTypes();
//        System.out.println(mainTypes.size());
//        System.out.println(mainTypes.get(0).getSchemas());
        List<Schema> schemas = mainTypes.get(0).getSchemas();
        Schema schema = schemas.get(0);

        com.predic8.schema.Import xsdSchema = schema.getImports().get(0);

        String schemaLocation = xsdSchema.getSchemaLocation();
        SchemaParser schemaParser = new SchemaParser();
        Schema parsedSchema = schemaParser.parse(schemaLocation);
        List<ComplexType> complexTypes = parsedSchema.getComplexTypes();

//        for (int i = 0; i < complexTypes.size(); i++) {
//            ComplexType complexType = complexTypes.get(i);
//            System.out.println("type "+i+" :: "+complexType);
//            
//        }
//        System.out.println("schemas size "+schemas.size());
//        System.out.println(schemas.get(0));
        List<Binding> bindings = mainDefs.getBindings();
//        System.out.println("bindings size "+bindings.size());
        Binding binding = bindings.get(0);
//        System.out.println("binding name : "+binding.getName());
//        System.out.println("binding style : "+binding.getStyle());

        PortType portType = binding.getPortType();
        
        List<Service> services = mainDefs.getServices();
        
//        System.out.println("portType to string :: "+portType);

        List<Operation> operations = portType.getOperations();
//        System.out.println("operations size : "+operations.size());

//        System.out.println("binding protocol : "+binding.getProtocol());
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i);
            System.out.println("operatio name " + operation.getName());
            Input input = operation.getInput();
            List<Part> parts = input.getMessage().getParts();
            System.out.println("parts size :: " + parts.size());

            for (int j = 0; j < parts.size(); j++) {
                Part part = parts.get(j);
                Element element = part.getElement();
                System.out.println("element : " + element);
                TypeDefinition type = part.getType();
                System.out.println("type : " + type);

//                System.out.println("element local part : "+element.getType().getLocalPart());
//                System.out.println("### comp type "+element.getSchema().getComplexType(element.getType().getLocalPart()));
                ComplexType ct = element.getSchema().getComplexType(element.getType().getLocalPart());

                Sequence seq = ct.getSequence();
//                System.out.println("complex type seq :: "+seq.getAsString());   
                System.out.println("sequence elements");
                List<Element> seqEles = seq.getElements();
                for (int k = 0; k < seqEles.size(); k++) {
                    Element subele = seqEles.get(k);
                    System.out.println("subele " + subele.getType().getLocalPart());
                    boolean ctype = subele.getSchema().getComplexType(subele.getType().getLocalPart()) != null;
                    System.out.println("name "+subele.getName());
                    System.out.println("is complex type ??? " + ctype);

                }
//                Thread.sleep(10000);
                Scanner scanner = new Scanner(System.in);
                String nextLine = scanner.nextLine();

            }

        }
    }

}
