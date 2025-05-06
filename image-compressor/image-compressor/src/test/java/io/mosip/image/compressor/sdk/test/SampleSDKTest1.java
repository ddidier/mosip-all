package io.mosip.image.compressor.sdk.test;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.mosip.image.compressor.sdk.impl.ImageCompressorSDKV2;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.ProcessedLevelType;
import io.mosip.kernel.biometrics.entities.BDBInfo;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.entities.VersionType;
import io.mosip.kernel.biometrics.model.Response;

public class SampleSDKTest1 {
    Logger LOGGER = LoggerFactory.getLogger(SampleSDKTest.class);

    private static String sampleFace = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SampleSDKTest1 test = new SampleSDKTest1();
		test.test_face();
	}

	 public void Setup() {
		 sampleFace = SampleSDKTest.class.getResource("/sample_files/sample_face.xml").getPath();
    }
	 
    public void test_face() {
        try {
            List<BiometricType> modalitiesToMatch = new ArrayList<BiometricType>(){{
                add(BiometricType.FACE);
                add(BiometricType.FINGER);
                add(BiometricType.IRIS);
            }};
            BiometricRecord sample_record = xmlFileToBiometricRecord(sampleFace);

            ImageCompressorSDKV2 sampleSDK = new ImageCompressorSDKV2();
            Response<BiometricRecord> response = sampleSDK.extractTemplate(sample_record, modalitiesToMatch, new HashMap<>());
            if (response != null && response.getResponse() != null)
            {
            	BiometricRecord compressed_record = response.getResponse();
                Assert.assertEquals("Should be Intermediate", compressed_record.getSegments().get(0).getBdbInfo().getLevel().toString(), ProcessedLevelType.RAW.toString());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private BiometricRecord xmlFileToBiometricRecord(String path) throws ParserConfigurationException, IOException, SAXException {
        BiometricRecord biometricRecord = new BiometricRecord();
        List bir_segments = new ArrayList();
        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        LOGGER.debug("Root element :" + doc.getDocumentElement().getNodeName());
        Node rootBIRElement = doc.getDocumentElement();
        NodeList childNodes = rootBIRElement.getChildNodes();
        for (int temp = 0; temp < childNodes.getLength(); temp++) {
            Node childNode = childNodes.item(temp);
            if(childNode.getNodeName().equalsIgnoreCase("bir")){
                BIR.BIRBuilder bd = new BIR.BIRBuilder();

                /* Version */
                Node nVersion = ((Element) childNode).getElementsByTagName("Version").item(0);
                String major_version = ((Element) nVersion).getElementsByTagName("Major").item(0).getTextContent();
                String minor_version = ((Element) nVersion).getElementsByTagName("Minor").item(0).getTextContent();
                VersionType bir_version = new VersionType(parseInt(major_version), parseInt(minor_version));
                bd.withVersion(bir_version);

                /* CBEFF Version */
                Node nCBEFFVersion = ((Element) childNode).getElementsByTagName("Version").item(0);
                String cbeff_major_version = ((Element) nCBEFFVersion).getElementsByTagName("Major").item(0).getTextContent();
                String cbeff_minor_version = ((Element) nCBEFFVersion).getElementsByTagName("Minor").item(0).getTextContent();
                VersionType cbeff_bir_version = new VersionType(parseInt(cbeff_major_version), parseInt(cbeff_minor_version));
                bd.withCbeffversion(cbeff_bir_version);

                /* BDB Info */
                Node nBDBInfo = ((Element) childNode).getElementsByTagName("BDBInfo").item(0);
                String bdb_info_type = "";
                String bdb_info_subtype = "";
                NodeList nBDBInfoChilds = nBDBInfo.getChildNodes();
                for (int z=0; z < nBDBInfoChilds.getLength(); z++){
                    Node nBDBInfoChild = nBDBInfoChilds.item(z);
                    if(nBDBInfoChild.getNodeName().equalsIgnoreCase("Type")){
                        bdb_info_type = nBDBInfoChild.getTextContent();
                    }
                    if(nBDBInfoChild.getNodeName().equalsIgnoreCase("Subtype")){
                        bdb_info_subtype = nBDBInfoChild.getTextContent();
                    }
                }

                BDBInfo.BDBInfoBuilder bdbInfoBuilder = new BDBInfo.BDBInfoBuilder();
                bdbInfoBuilder.withType(Arrays.asList(BiometricType.fromValue(bdb_info_type)));
                bdbInfoBuilder.withSubtype(Arrays.asList(bdb_info_subtype));
                BDBInfo bdbInfo = new BDBInfo(bdbInfoBuilder);
                bd.withBdbInfo(bdbInfo);

                /* BDB */
                //String nBDB = ((Element) childNode).getElementsByTagName("BDB").item(0).getTextContent();
                //bd.withBdb(nBDB.getBytes("UTF-8"));
                
                byte[] nBDB = Base64.getDecoder().decode(((Element) childNode).getElementsByTagName("BDB").item(0).getTextContent());
                bd.withBdb(nBDB);

                /* Prepare BIR */
                BIR bir = new BIR(bd);

                /* Add BIR to list of segments */
                bir_segments.add(bir);
            }
        }
        biometricRecord.setSegments(bir_segments);
        return biometricRecord;
    }

}
