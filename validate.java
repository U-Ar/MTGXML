import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class validate {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Simple XML validator.");
            System.out.println("usage: java validate.java <schema> <xml>");
            System.out.println("<schema>: filename of XML schema (ex)deckSchema.xsd");
            System.out.println("<xml>       : filename of XML you want to validate based on the schema (ex)deck1.xml");
        }

        // parse an XML document into a DOM tree
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new File(args[1]));

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(new File(args[0]));
        Schema schema = factory.newSchema(schemaFile);

        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree
        try {
            validator.validate(new DOMSource(document));
            System.out.println(args[1] + " is valid.");
        } catch (SAXException e) {
            System.out.println(args[1] + " is invalid.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed to validate XML.");
            e.printStackTrace();
        }

    }
}