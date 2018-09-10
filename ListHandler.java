import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListHandler implements ContentHandler {

    String tag;
    String rsrp;
    String qci;
    String rip;
    String record_startTime;
    String eNbid;
    String key;
    int ripNum=0;
    int num=0;

    Map<String,Object> oMap = new HashMap<String,Object>();

    List<Object> ripList = new ArrayList<Object>();
    List<String> keyList = new ArrayList<String>();

    public ListHandler() {

    }


    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {


    }




    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        tag = qName;

            if ("fileHeader".equals(qName)) {
                record_startTime = atts.getValue("reportTime").replace("T"," ");
            } else if ("eNB".equals(qName)) {
                eNbid = atts.getValue("id");
            }else if ("object".equals(qName)){
                String MmeCode = atts.getValue("MmeCode");
                String MmeGroupId = atts.getValue("MmeGroupId");
                String MmeUeS1apId = atts.getValue("MmeUeS1apId");
                String TimeStamp = atts.getValue("TimeStamp").replace("T"," ");
                String id = atts.getValue("id");
                key = MmeUeS1apId+TimeStamp+id;
                    if (num == 0) {
                        Object o = new Object();
                        o.setRSRP(rsrp);
                        o.setId(id);
                        o.setMmeCode(MmeCode);
                        o.setMmeGroupId(MmeGroupId);
                        o.setMmeUeS1apId(MmeUeS1apId);
                        o.setTimeStamp(TimeStamp);
                        oMap.put(key,o);
                        keyList.add(key);

                    } else if (num==1) {
                        Object o = oMap.get(key);
                        o.setSmrQci(qci);

                    } else if (num == 2) {
                        Object o = new Object();
                        o.setId(id);
                        o.setMmeCode(MmeCode);
                        o.setMmeGroupId(MmeGroupId);
                        o.setMmeUeS1apId(MmeUeS1apId);
                        o.setTimeStamp(TimeStamp);
                        ripList.add(ripNum, o);
                    }


            }


//        for(int i=0;atts!=null&&i<atts.getLength();i++){
//            String attName=atts.getQName(i);
//            String attValueString=atts.getValue(i);
//            System.out.print(" "+attName+"="+attValueString);
//            System.out.print(">");
//
//        }
    }



    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {


        String s = new String(ch, start, length);

        if ("smr".equals(tag)) {
            if (s.contains("RSRP")) {
                rsrp = s;
            } else if (s.contains("Qci")) {
                qci = s;
                num = 1;
            } else if (s.contains("RIP")) {
                rip = s;
                num = 2;
            }
        }

        if ("v".equals(tag)) {
            if (!"\n".equals(s)) {



                    if (num == 0) {

                        Object o = oMap.get(key);
                        if (o != null) {

                            o.addRsrpV(s);
                        }
                    } else if (num == 1) {

                        Object o = oMap.get(key);
                        if (o != null) {
//                            o.setSmrQci(qci);
                            o.setQci(s);
                        }
                    } else if (num == 2) {
                        Object o = ripList.get(ripNum);
                        o.setRip(s);
                        ripNum++;

                    }
            }
        }
        tag = "";
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {


    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    public String getRsrp() {
        StringBuffer sb = new StringBuffer();
        for (String key : keyList) {
            Object ob = oMap.get(key);
            sb.append(
                    record_startTime + "|"+eNbid +"|"+ ob.getMmeCode()+"|"+ob.getMmeGroupId()+"|"+ob.getMmeUeS1apId()+"|"+ob.getTimeStamp()+"|"+ob.getId()
                            +"|"+
                                    ob.getRsrpV()
                            +"|"+
                                    ob.getQci()
            );
            sb.append("\n");
        }





        return sb.toString();
    }


    public String getRip(){
        StringBuffer sb = new StringBuffer();
        for (Object ob : ripList) {
            sb.append(record_startTime + "|"+eNbid +"|"+ ob.getMmeCode()+"|"+ob.getMmeGroupId()+"|"+ob.getMmeUeS1apId()+"|"+ob.getTimeStamp()+"|"+ob.getId()+ob.getRip());
            sb.append("\n");
        }
        return sb.toString();
    }
}
