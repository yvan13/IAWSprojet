package Java.requests;

import Java.utilitaires.Coordonnees;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by Yvan on 03/04/2016.
 */
public class OpenStreetMapRequest {
    private static final String API_URI = "http://nominatim.openstreetmap.org/search";

    private static void getCoordonneesFromAdresse(String adresse) {
        Client c = ClientBuilder.newClient();
        WebTarget wt = c.target(API_URI);
        File coordonnee = wt.queryParam("street",adresse).queryParam("format", "xml").request(MediaType.APPLICATION_XML).get(File.class);
        SAXBuilder saxBuilder = new SAXBuilder();

        try {
            Document fichierReponse = saxBuilder.build(coordonnee);
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(fichierReponse, new FileWriter("position.xml"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* RENVOYER COORDONNEES */
    private static Coordonnees coordonneeFromAdresse () {
        Coordonnees maPosition = new Coordonnees(0,0);
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document doc = null;
        Node coordonnee = null;
        try {
            doc = domFactory.newDocumentBuilder().parse("./position.xml");
            XPath xpath = XPathFactory.newInstance().newXPath();
            String xPathStr = "/searchresults/place/@lat";
            coordonnee = ((NodeList) xpath.compile(xPathStr).evaluate(doc, XPathConstants.NODESET)).item(0);
            maPosition.setLat(Float.parseFloat(coordonnee.getNodeValue()));
            xPathStr = "/searchresults/place/@lon";
            coordonnee = ((NodeList) xpath.compile(xPathStr).evaluate(doc, XPathConstants.NODESET)).item(0);
            maPosition.setLng(Float.parseFloat(coordonnee.getNodeValue()));
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return maPosition;
    }

    public static  void main(String [] args){
        getCoordonneesFromAdresse("2 Les Villageoises");
        System.out.println(coordonneeFromAdresse().toString());
    }
}
