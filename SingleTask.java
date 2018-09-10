import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

public class SingleTask implements Runnable{

    String inputFloder=null;
    String outputFloder=null;
    String factory=null;
    String tis=null;
    int poolSize=10;
    String ip="";

    Thread t = Thread.currentThread();

    public SingleTask(String inputFloder, String outputFloder, String factory, String tis, int poolSize,String ip) {
        this.inputFloder = inputFloder;
        this.outputFloder = outputFloder;
        this.factory = factory;
        this.tis = tis;
        this.poolSize = poolSize;
        this.ip = ip;
    }

    @Override
    public void run() {
        boolean nullFloder = true;
        String[] tims = tis.split("-");
        String time = tims[0] + tims[1] + tims[2] + tims[3] + tims[4];
        if(inputFloder==null||outputFloder==null){
            System.out.println("please input inputFloder and outputFloder");
            nullFloder=false;
        }


        if(nullFloder) {


            FileOutputStream os = null;
            GZIPOutputStream gos = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            String outFilePathName = null;


            FileOutputStream ripos = null;
            GZIPOutputStream ripgos = null;
            BufferedInputStream ripbis = null;
            BufferedOutputStream ripbos = null;
            String ripoutFilePathName = null;
            try {
//

                //多线程


                List<Future<Map<String, String>>> results = new ArrayList<Future<Map<String, String>>>();

                ArrayList<String> fileList = getFileList(inputFloder, factory,tis);

                if (fileList!=null) {
                    Executor executor = Executors.newFixedThreadPool(poolSize);
                    CountDownLatch countDownLatch = new CountDownLatch(poolSize);




                    for (String file : fileList) {
//                        System.out.println("while processing:" + file);
                        Future<Map<String, String>> fs = ((ExecutorService) executor).submit(new MroCall(file, countDownLatch));
                        if(fs!=null) {
                            results.add(fs);
                        }

                    }
//                        MRO_RSRP_merge_201608021900_127.0.0.1_HW.csv
                    outFilePathName = outputFloder + "/MRO_RSRP_merge_"+time+"_"+ip+"_"+factory+".csv.gz";
//                       MRO_RIP_merge_201608021900_127.0.0.1_HW.csv
                    ripoutFilePathName = outputFloder + "/MRO_RIP_merge_"+time+"_"+ip+"_"+factory+".csv.gz";
                    os = new FileOutputStream(new File(outFilePathName), true);
                    gos = new GZIPOutputStream(os);
                    bos = new BufferedOutputStream(gos);


                    ripos = new FileOutputStream(new File(ripoutFilePathName), true);
                    ripgos = new GZIPOutputStream(ripos);
                    ripbos = new BufferedOutputStream(ripgos);
                    for (Future<Map<String, String>> fs : results) {

                        Map<String, String> reback = fs.get();
                        if (reback==null){
                            continue;
                        }
                        String rsrp = reback.get("rsrp");
                        String rip = reback.get("rip");


//

                        bos.write(rsrp.getBytes());

                        if (rip != null) {

                            ripbos.write(rip.getBytes());
                        }
                    }
                    countDownLatch.await();
                    ((ExecutorService) executor).shutdown();
                }



            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (ExecutionException e) {
                System.out.println(e.getMessage());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (bos != null) {

                    try {
                        bos.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }
    }

    public  ArrayList<String> getFileList(String inputFloder, String factory, String tis) {

        String[] tims = tis.split("-");

        //指定时间的字符串
        String ftime = tims[0] + tims[1] + tims[2] + tims[3] + tims[4]+"00";
        System.out.println("the combine time:"+ftime);
        //传入的文件路径
        String inFilePathName=null;
        //20180806
        String timeF=null;

        switch (factory) {
            case "NOKIA":
                ArrayList<String> narrayList = new ArrayList<>();
                //   /home/richuser/l3fw_mr/kpi_import/
                //找到当天 ：20180816  小区号 TD-LTE_MRO_NSN_OMC_848210_20180816043000.xml.gz
                //TD-LTE_MRO_NSN_OMC_848210_20180816044500.xml.gz
                //TD-LTE_MRO_NSN_OMC_848210_20180816050000.xml.gz
                //TD-LTE_MRO_NSN_OMC_848210_20180816051500.xml.gz
                //TD-LTE_MRO_NSN_OMC_848210_20180816053000.xml.gz
                //TD-LTE_MRO_NSN_OMC_848210_20180816054500.xml.gz

                timeF = tims[0]+tims[1]+tims[2];
                inFilePathName=inputFloder+timeF;
                if (inFilePathName==null){
                    System.out.println("in singleTask inFilePath is null");
                    t.stop();
                }
                File ntfs = new File(inFilePathName);
                File[] ntfiles = ntfs.listFiles();
                for (File tfile : ntfiles) {
                    File[] reFiles = tfile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {

//                            String pattern = ".*TD-LTE_MRO_NSN_OMC_.*_20180816054500.xml.gz";
                            String pattern = "TD-LTE_MRO_NSN_OMC.*_" + ftime + ".xml.gz";
                            Pattern r = Pattern.compile(pattern);
                            Matcher m = r.matcher(name);
                            return m.find();
                        }
                    });

                    for (File reFile : reFiles) {
//                        System.out.println("filename:"+reFile.getName());
                        narrayList.add(reFile.getAbsolutePath());
                    }
                }
                return narrayList;
//                break;
            case "HW":
                ArrayList<String> harrayList = new ArrayList<>();
                //   /export/home/omc/var/fileint/TSNBI/LTESpecial/
                // 日期 20180816 校区号TD-LTE_MRO_HUAWEI_100092252097_84311_20180816144500.xml.gz
                //TD-LTE_MRO_HUAWEI_100092252097_84311_20180816150000.xml.gz
                //TD-LTE_MRO_HUAWEI_100092252097_84311_20180816151500.xml.gz


                timeF = tims[0]+tims[1]+tims[2];
                inFilePathName=inputFloder+timeF;
                if (inFilePathName==null){
                    System.out.println("in singleTask inFilePath is null");
                    t.stop();
                }
                File htfs = new File(inFilePathName);
                File[] htfiles = htfs.listFiles();
                for (File tfile : htfiles) {
                    File[] reFiles = tfile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {

                            String pattern = "TD-LTE_MRO_HUAWEI.*_" + ftime + ".xml.gz";
                            Pattern r = Pattern.compile(pattern);
                            Matcher m = r.matcher(name);
                            return m.find();
                        }
                    });

                    for (File reFile : reFiles) {
//                        System.out.println("filename:"+reFile.getName());
                        harrayList.add(reFile.getAbsolutePath());
                    }
                }
                return harrayList;
//                break;
            case "ZTE":
                ArrayList<String> zarrayList = new ArrayList<>();
                //   /oracledata/NDS-LTE/output/north_output/NORTHFILE/MR/   /NDS-L/NDS-LTE/output/north_output/NORTHFILE/MR/
                //2018-08-16 校区号 TD-LTE_MRO_ZTE_OMC1_850559_20180816114500.zip
                //TD-LTE_MRO_ZTE_OMC1_850559_20180816120000.zip
                //TD-LTE_MRO_ZTE_OMC1_850559_20180816121500.zip


                timeF = tims[0]+"-"+tims[1]+"-"+tims[2];
                inFilePathName=inputFloder+timeF;
                if (inFilePathName==null){
                    System.out.println("in singleTask inFilePath is null");
                    t.stop();
                }
                File ztfs = new File(inFilePathName);
                File[] ztfiles = ztfs.listFiles();
                for (File tfile : ztfiles) {
                    File[] reFiles = tfile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {

                            String pattern = "TD-LTE_MRO_ZTE_OMC.*_" + ftime + ".zip";
                            Pattern r = Pattern.compile(pattern);
                            Matcher m = r.matcher(name);
                            return m.find();
                        }
                    });

                    for (File reFile : reFiles) {
//                        System.out.println("filename:"+reFile.getName());
                        zarrayList.add(reFile.getAbsolutePath());
                    }
                }
                return zarrayList;
//                break;
            case "ERIC":
                ArrayList<String> earrayList = new ArrayList<>();
                //   /opt/MR/data/northbound/files/
                //  201808130645 TDD-LTE_MRO_ERICSSON_OMC1_958355_20180813064500.xml.zip
                //TDD-LTE_MRO_ERICSSON_OMC1_958357_20180813064500.xml.zip
                //TDD-LTE_MRO_ERICSSON_OMC1_958376_20180813064500.xml.zip
                //TDD-LTE_MRO_ERICSSON_OMC1_958382_20180813064500.xml.zip
                //TDD-LTE_MRO_ERICSSON_OMC1_958386_20180813064500.xml.zip
                //TDD-LTE_MRO_ERICSSON_OMC1_958389_20180813064500.xml.zip


                timeF = tims[0]+tims[1]+tims[2]+tims[3]+tims[4];
                inFilePathName=inputFloder+timeF;
                if (inFilePathName==null){
                    System.out.println("in singleTask inFilePath is null");
                    t.stop();
                }
                File etfs = new File(inFilePathName);
//                File[] etfiles = etfs.listFiles();

                File[] etfiles = etfs.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {

                        String pattern = "TDD-LTE_MRO_ERICSSON_OMC.*_" + ftime + ".xml.zip";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(name);
                        return m.find();
                    }
                });

                for (File reFile : etfiles) {
//                    System.out.println("filename:"+reFile.getName());
                    earrayList.add(reFile.getAbsolutePath());
                }
                return earrayList;
//                break;
        }
        System.out.println("最后返回为空");
        return null;




    }
}
