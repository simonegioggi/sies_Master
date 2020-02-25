 package f3b.util.xml;
 
 import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;



/**
 * Classe di utilita' per la gestione e manipolazione degliXml.
 * @author Giselda De Vita
 *
 */
public class XMLUtils
{
 
 
public static void serialize(Document doc, OutputStream out) throws Exception {
        
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            //Setup indentazione 
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            //setup 2 spazi di indentazione
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //setup encoding
            serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            
             
            serializer.transform(new DOMSource(doc), new StreamResult(out));
            
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
}