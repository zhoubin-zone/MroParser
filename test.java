import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;

import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) throws Exception {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time1 = time.minusMinutes(20);
        System.out.println(time1);
        String tis = df.format(time);

//        String inFilePathName="/home/ubantu/Desktop/2018-08-21/";
//        File ztfs = new File(inFilePathName);
//        File[] ztfiles = ztfs.listFiles();
//        for (File tfile : ztfiles) {
//            File[] reFiles = tfile.listFiles(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String name) {
//                    String ftime ="20180821210000";
//                    String pattern = "TD-LTE_MRO_ZTE_OMC.*_" + ftime + ".zip";
//                    Pattern r = Pattern.compile(pattern);
//                    Matcher m = r.matcher(name);
//                    return m.find();
//                }
//            });
//
//            for (File reFile : reFiles) {
//                System.out.println("filename:"+reFile.getName());
//            }
//        }



//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy|MM|dd|HH|mm");
//        LocalDateTime time = LocalDateTime.now();
//        String localTime = df.format(time);
//        System.out.println("LocalDateTime转成String类型的时间："+localTime);
//        File file = new File("/home/ubantu/Desktop/mr_xml_process/data/376832");
//        File[] files = file.listFiles(new FilenameFilter() {
//
//            @Override
//            public boolean accept(File dir, String name) {
//                String pattern = ".*MRO.*";
//                Pattern r = Pattern.compile(pattern);
//                Matcher m = r.matcher(name);
//
//                return m.find();
//            }
//        });
//
//        for (File file1 : files) {
//            System.out.println(file1.getAbsolutePath());
////            System.out.println(files.length);
//        }
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
//        LocalDateTime time = LocalDateTime.now();
//        String tis = df.format(time);
//        System.out.println("tis:"+tis);
//        String[] tims = tis.split("-");
//        for (String tim : tims) {
//            System.out.print(tim);
//            System.out.print(" ");
//        }
//        int mm =Integer.valueOf(tims[4]);
//        System.out.println(mm);



    }

}
