import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MroCall implements Callable<Map<String, String>> {
    private String name;
    private CountDownLatch countDownLatch;
    boolean w = true;
    public MroCall(String name,CountDownLatch countDownLatch) {
        this.name = name;
        this.countDownLatch=countDownLatch;
    }

    @Override
    public Map<String, String> call() throws Exception {

        Thread t = Thread.currentThread();
//        String name ="/home/ubantu/Desktop/mro.xml.zip";
        ListHandler listHandler = null;
        GZIPInputStream gis = null;
        ZipInputStream zipStream = null;
        StringBuffer sb = null;
        try {
            FileInputStream is = new FileInputStream(name);
            InputSource inputSource = null;

            if (name.endsWith("gz")) {

                gis = new GZIPInputStream(is);

                inputSource = new InputSource(gis);


            } else if (name.endsWith("zip")) {
                zipStream = new ZipInputStream(is);
                ZipEntry entry = null;

                if ((entry = zipStream.getNextEntry()) != null) {
                    inputSource = new InputSource(zipStream);
                }


            } else {
                System.out.println("FileName Wrong");
//                System.exit(0);
                t.stop();
            }


//        BufferedReader br = new BufferedReader(new InputStreamReader(gis));

            //1.创建解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //2.得到解析器
            SAXParser sp = factory.newSAXParser();
            //3得到解读器
            XMLReader reader = sp.getXMLReader();
            //设置内容处理器
            sb = new StringBuffer();
            listHandler = new ListHandler(sb);

            reader.setContentHandler(listHandler);

            MyErrorHandler myErrorHandler = new MyErrorHandler(w,t,name);

            reader.setErrorHandler(myErrorHandler);

            //读取xml的文档内容
//        reader.parse("/home/ubantu/Desktop/mro1.xml.gz");
            if (inputSource != null) {

                reader.parse(inputSource);
            } else {
                t.stop();
            }

        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally {
            if (gis!=null){
                try {
                    gis.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (zipStream != null) {
                try {
                    zipStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if(w) {
            Map<String, String> reback = new HashMap<>();
            String rsrp = listHandler.getRsrp();
            String rip = listHandler.getRip();
            reback.put("rsrp", rsrp);
            reback.put("rip", rip);
            countDownLatch.countDown();
            return reback;
        }else {
            if (gis!=null){
                try {
                    gis.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (zipStream != null) {
                try {
                    zipStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (sb != null) {
                sb=null;
            }
        }

        countDownLatch.countDown();
        return null;
    }
}
