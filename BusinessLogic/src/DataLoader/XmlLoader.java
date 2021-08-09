package DataLoader;

import Exceptions.ExtensionFileException;
import generated.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class XmlLoader {
    private final static String JAXB_XML_PACKAGE_NAME = "generated";
    private String xmlPath;
    private ETTDescriptor descriptor;

    public String getXmlPath() {
        return xmlPath;
    }

    public ETTDescriptor getDescriptor() {return descriptor;}

    public boolean setXmlPath(String xmlPath) throws JAXBException, FileNotFoundException {
        this.xmlPath = xmlPath;
        if (isValidXmlFile() && isXMLFile()) {
            createTimeTable();
            return true;
        }
        return false;
    }

    private void createTimeTable() throws JAXBException, FileNotFoundException {
        InputStream inputXml = new FileInputStream(new File(this.xmlPath));
        this.descriptor = deserializeFrom(inputXml);
    }

    private ETTDescriptor deserializeFrom(InputStream input) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (ETTDescriptor) u.unmarshal(input);
    }

    public boolean isValidXmlFile() {
        if (!hasDuplicateData()) {
        }
        return true;
    }

    private boolean hasDuplicateData() {
        return false;
    }

    public boolean isXMLFile() throws ExtensionFileException {
       if (!this.xmlPath.endsWith(".xml"))
       {
           throw new ExtensionFileException ("xml");
       }
       return true;
    }
}
