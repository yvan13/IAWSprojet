package Java.requests;
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
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * Created by Yvan on 03/04/2016.
 */
public class OpenWeatherMapRequest {
    public static void getBulletinMeteo() {
        Client c = ClientBuilder.newClient();
        WebTarget wt = c.target("http://api.openweathermap.org/data/2.5/forecast/city?id=2972315&mode=xml&appid=4cd343650c5f762146e50e3c53525e8b");
        String[] tab = new String[1];
        tab[0] = MediaType.APPLICATION_XML;
        File pluviometrie3h = wt.request(tab).get(File.class);
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document = saxBuilder.build(pluviometrie3h);
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileWriter("bulletinMeteo.xml"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main (String args []){
        getBulletinMeteo();
    }
}
