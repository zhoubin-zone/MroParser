import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MyErrorHandler implements org.xml.sax.ErrorHandler {
    boolean w=false;
    Thread t = Thread.currentThread();
    String name="";
    public MyErrorHandler(boolean w, Thread t, String name) {
        this.w = w;
        this.t = t;
        this.name=name;
    }



    @Override
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("*******WARNING******");
        System.out.println("/tLine:/t" + exception.getLineNumber());
        System.out.println("/tColumn:/t" + exception.getColumnNumber());
        System.out.println("/tinfo:/t" + exception.getMessage());
        System.out.println("/tFilename:/t" +name);
        System.out.println("********************");
        w=false;
//        t.stop();
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.out.println("*******WARNING******");
        System.out.println("/tLine:/t" + exception.getLineNumber());
        System.out.println("/tColumn:/t" + exception.getColumnNumber());
        System.out.println("/tinfo:/t" + exception.getMessage());
        System.out.println("/tFilename:/t" +name);
        System.out.println("********************");
        w=false;
//        t.stop();
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("*******WARNING******");
        System.out.println("/tLine:/t" + exception.getLineNumber());
        System.out.println("/tColumn:/t" + exception.getColumnNumber());
        System.out.println("/tinfo:/t" + exception.getMessage());
        System.out.println("/tFilename:/t" +name);
        System.out.println("********************");
        w=false;
//        t.stop();
    }
}
