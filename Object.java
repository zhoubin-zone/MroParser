import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Object {
    String MmeCode;
    String MmeGroupId;
    String MmeUeS1apId;
    String TimeStamp;
    String id;
    List<String> vList = new ArrayList<>();

    //read from text
    String smrRsrp;
    String Qci;
    String smrQci;
    String Rip;

    //read from cfg
    String[] cfgRSRP;
    String[] cfgQCI;
    String[] cfgRIP;
    ArrayList<Integer> rsrpOrder = new ArrayList<Integer>();
    ArrayList<Integer> qciOrder = new ArrayList<Integer>();

    {
        Properties prop = new Properties();

        InputStream in = Main.class.getClassLoader().getResourceAsStream(
                "cfg.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rsrp = prop.getProperty("RSRP");
        String qci = prop.getProperty("QCI");
        String rip = prop.getProperty("RIP");
        cfgRSRP = rsrp.split(",");
        cfgQCI = qci.split(",");
        cfgRIP = rip.split(",");

    }


    public String getSmrQci() {
        return smrQci;
    }

    public void setSmrQci(String smrQci) {
        this.smrQci = smrQci;
    }

    public String getRSRP() {
        return smrRsrp;
    }

    public void setRSRP(String smrRsrp) {
        this.smrRsrp = smrRsrp;
    }

    public String getMmeCode() {
        return MmeCode;
    }

    public void setMmeCode(String mmeCode) {
        MmeCode = mmeCode;
    }

    public String getMmeGroupId() {
        return MmeGroupId;
    }

    public void setMmeGroupId(String mmeGroupId) {
        MmeGroupId = mmeGroupId;
    }

    public String getMmeUeS1apId() {
        return MmeUeS1apId;
    }

    public void setMmeUeS1apId(String mmeUeS1apId) {
        MmeUeS1apId = mmeUeS1apId;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getvList() {
        return vList;
    }

    public void addRsrpV(String v) {
        vList.add(v);
    }

    public void setQci(String qci) {
        Qci = qci;
    }

    public void setRip(String rip) {
        Rip = rip;
    }

    public String getRsrpV() {


        String[] rs = smrRsrp.split(" ");
        List<String> rsrp = Arrays.asList(rs);
        for (int i = 0; i < cfgRSRP.length; i++) {
            String s = cfgRSRP[i];
            int si = rsrp.indexOf(s);
            if (si != -1) {

                rsrpOrder.add(i, si);

            } else {
                rsrpOrder.add(i, -2);
            }

        }

        StringBuffer sb = new StringBuffer();
        for (int m = 0; m < vList.size(); m++) {
            String s = vList.get(m);
            String[] split = s.split(" ");


            for (int i = 0; i < rsrpOrder.size(); i++) {
                Integer j = rsrpOrder.get(i);
                if (i == rsrpOrder.size() - 1) {
                    if (j != -2) {
                        if(j>split.length-1) {
                            sb.append("NIL");
                        }else {

                            sb.append(split[j]);
                        }
                    } else {
                        sb.append("NIL");
                    }

                } else {
                    if (j != -2) {
                        if(j>split.length-1) {
                            sb.append("NIL");
                            sb.append(" ");
                        }else {
                            sb.append(split[j]);
                            sb.append(" ");
                        }
                    } else {
                        sb.append("NIL");
                        sb.append(" ");
                    }

                }
            }


            if (m != vList.size() - 1) {

                sb.append(",");
            }
        }

        return sb.toString();
//        Stream<String>Vstream= vList.stream();
//        String sr = Vstream.collect(Collectors.joining(","));
    }


    public String getQci() {
        if (smrQci != null) {
            String[] qs = smrQci.split(" ");
            List<String> qciList = Arrays.asList(qs);
            for (int i = 0; i < cfgQCI.length; i++) {
                String s = cfgQCI[i];

                int si = qciList.indexOf(s);
                if (si != -1) {

                    qciOrder.add(i, si);

                }else {
                    qciOrder.add(i, -2);
                }

            }

            StringBuffer sb = new StringBuffer();
            String[] qcil = Qci.split(" ");
            for (int i = 0; i < qciOrder.size(); i++) {
                Integer j = qciOrder.get(i);
                if (i == qciOrder.size() - 1) {
                    if (j != -2) {
                            if(j>qcil.length-1) {
                                sb.append("NIL");
                            }else {
                                sb.append(qcil[j]);
                            }
                    }else {
                        sb.append("NIL");
                    }
                } else {
                    if (j != -2) {
                            if(j>qcil.length-1) {
                                sb.append("NIL");
                                sb.append("|");
                            }else {
                                sb.append(qcil[j]);
                                sb.append("|");
                            }
                    }else {

                        sb.append("NIL");
                        sb.append("|");
                    }
                }
            }
            return sb.toString();
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < cfgQCI.length; i++) {
                if (i != cfgQCI.length - 1) {
                    sb.append("NIL|");
                } else {
                    sb.append("NIL");
                }
            }
            return sb.toString();
        }


    }

    public String getRip() {
        return Rip;
    }
}
