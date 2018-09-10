import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {



    public static void main(String[] args) {
        String inputFloder=null;
        String outputFloder=null;
        String factory=null;
        String ip=null;
        int poolSize=10;
        int timeLazy=20;
        int c;
        GetOpt getOpt = new GetOpt(args, "f:i:o:s:t:p:");
        try {
            while ((c = getOpt.getNextOption()) != -1) {
//                System.out.println((char) c);
                switch (c) {
                    case 'f':
                        factory = getOpt.getOptionArg();
                        if (factory == null||factory == "") {
                            System.out.println("factory is null");
                            System.exit(0);
                        }
                        System.out.println("factory: "+factory);
                        break;
                    case 'i':
                        inputFloder = getOpt.getOptionArg();
                        if (inputFloder == null||inputFloder == "") {
                            System.out.println("inputFloder is null");
                            System.exit(0);
                        }
                        System.out.println("inputfilepath:"+inputFloder);
                        break;
                    case 'o':
                        outputFloder = getOpt.getOptionArg();
                        if (outputFloder == null||outputFloder == "") {
                            System.out.println("outputFloder is null");
                            System.exit(0);
                        }
                        System.out.println("outputfilepath:"+outputFloder);
                        break;
                    case 's':
                        String Size = getOpt.getOptionArg();
                        if (Size!=null && Size!="") {
                            poolSize=Integer.valueOf(Size);
                        }
                        break;
                    case 't':
                        String lazy = getOpt.getOptionArg();
                        if (lazy!=null && lazy!="") {
                            timeLazy = Integer.valueOf(lazy);
                        }
                        break;
                    case 'p':
                        String opip = getOpt.getOptionArg();
                        if (opip!=null && opip!="") {
                            ip = opip;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        while (true) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
            LocalDateTime time = LocalDateTime.now();
            LocalDateTime lasyTime = time.minusMinutes(timeLazy);
            String tis = df.format(lasyTime);
//            System.out.println("searching time is :"+tis);
//            String tis = "2018-08-23-14-45";
            String[] tims = tis.split("-");
//            System.out.println("");
            int mm =Integer.valueOf(tims[4]);
//            boolean b00 = false;
//            boolean b15 = false;
//            boolean b30 = false;
//            boolean b45 = false;
            if(mm==00||mm==15||mm==30||mm==45){
//                System.out.println(mm+":run");
                SingleTask singleTask = new SingleTask(inputFloder, outputFloder, factory, tis, poolSize,ip);
                singleTask.run();
            }

//            if(mm==00 && !b00){
//                System.out.println("00 run");
//                SingleTask singleTask = new SingleTask(inputFloder, outputFloder, factory, tis, poolSize);
//                singleTask.run();
//                b00=true;
//                b45=false;
//            }else if (mm==15 && !b15){
//                System.out.println("15 run");
//                SingleTask singleTask = new SingleTask(inputFloder, outputFloder, factory, tis, poolSize);
//                singleTask.run();
//                b15=true;
//            }else if (mm==30 && !b30){
//                System.out.println("30 run");
//                SingleTask singleTask = new SingleTask(inputFloder, outputFloder, factory, tis, poolSize);
//                singleTask.run();
//                b30=true;
//            }else if (mm==45 && !b45){
//                System.out.println("45 run");
//                SingleTask singleTask = new SingleTask(inputFloder, outputFloder, factory, tis, poolSize);
//                singleTask.run();
//                b45=true;
//                b00=b15=b30=false;
//            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
            }


        }



    }








}

