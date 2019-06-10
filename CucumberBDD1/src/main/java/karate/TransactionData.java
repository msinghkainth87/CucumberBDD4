package karate;

import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionData {
    public String compareData(ArrayList arrSource, String arrTarget, ArrayList arrMapingData,String id) throws Exception {
        String result = "";
        int passCounter = 0;
        int failCounter = 0;
        int sourceLen = arrSource.size();
        int targetLen = arrTarget.length();
        arrTarget = arrTarget.replace("[", "");
        arrTarget = arrTarget.replace("]", "");
        arrTarget = arrTarget.replace("}", "}@");
        arrTarget = arrTarget.replaceAll("}", "");
        arrTarget = arrTarget.replaceAll("\\{", "");
        arrTarget = arrTarget.replaceAll("\",", "\", ");
        arrTarget = arrTarget.replaceAll("\"", "");
        arrTarget = arrTarget.substring(0, arrTarget.length() - 1) + "";
        String[] arrTargetFinal = arrTarget.split("@,");
        if (sourceLen > 0) {
            for (int i = 0; i < sourceLen; i++) {
                String actual = arrSource.get(i).toString();
                actual = actual.replaceAll("\\{", "");
                actual = actual.replaceAll("}", "");
                String[] actualCOlumnList = actual.split(", ");
//                System.out.println(actual);
                HashMap<String, String> map = createData(actualCOlumnList, "=");
                String resTemp = validateTarget(arrTargetFinal, map, arrMapingData);
                if(resTemp.contains("M")) {
                    result = result + "\n" + resTemp;
                }
                if (resTemp.contains("No Mismatches for")) {
                    passCounter = passCounter + 1;
                } else {
                    failCounter = failCounter + 1;
                }
                if (i == sourceLen - 1) {
                    result = result + "\n" + "\n" + "TOTAL " + id + "s Validated: " + sourceLen + " | TOTAL ROWS PASSED: " + passCounter + " | TOTAL ROWS WITH MISMATCHES: " + failCounter + "|";
                }
            }
        }
        return (String)result;
    }

    public String validateTarget(String[] arrTarget, HashMap<String, String> mapSource, ArrayList arrMapingData) {
        String exception = "";
        int keyFound=0;
        String id="";
        int targetLen = arrTarget.length;
        String transactionType = "";
        String mismatchList = "";
        int counterPass = 0;
        int counterFail = 0;
        for (int i = 0; i < targetLen; i++) {
            String[] arrTargetRow = arrTarget[i].split(", ");
            HashMap<String, String> mapTarget = createData(arrTargetRow, ":");

            //String[] arrTargetRownext = arrTarget[i+1].split(", ");
            //HashMap<String, String> mapTargetnext = createData(arrTargetRownext, ":");

            HashMap<String, String> mapResult = new HashMap<String, String>();
//            System.out.println("Source :"+mapSource.get("AcnoMinor").toString()+" | Target :"+mapTarget.get("AcnoMinor").toString());
//            System.out.println(mapSource.get("AcnoMinor").toString().trim().equals(mapTarget.get("AcnoMinor").toString().trim()));
//            System.out.println(transactionType.equals("SWAPS") && (((mapSource.get("CRDSecid").toString().equals(mapTarget.get("CRDSecid").toString()) || (mapSource.get("CRDSecid").toString().equals(mapTarget.get("ID3")))) || (mapSource.get("AcnoMinor").toString().equals(mapTarget.get("AcnoMinor").toString())))));
//            System.out.println("RAKTIM"+mapSource.get("CRDSecid").toString().equals(mapTarget.get("ID3"))+" "+mapSource.get("CRDSecid")+"|"+mapSource.get("CRDSecid").toString().equals(mapTarget.get("CRDSecid").toString())+"|"+mapTarget.get("ID3"));

            //System.out.println("Maninder"+mapTarget.get("TRADE_ID"));
            id = "Trade ID" + " :" + mapSource.get("TRADE_ID").toString().trim() +  " | SEC_TYP_CD :" + mapSource.get("SEC_TYP_CD").toString().trim() + "  "+ "\n";

            //int versionNum=Integer.parseInt(mapTarget.get("Version").toString().trim());


            if (mapSource.get("TRADE_ID").toString().trim().equals(mapTarget.get("TRADE_ID").toString().trim()) ) {

                int maxVer = getmaxversion(mapSource.get("TRADE_ID").toString().trim(),arrTarget);
                System.out.println("My max version is:" + maxVer);
                if(Integer.parseInt(mapTarget.get("Version").toString().trim())==maxVer)
                {
                    keyFound = 1;
                id = "Trade ID" + " :" + mapSource.get("TRADE_ID").toString().trim() +  " | SEC_TYP_CD :" + mapSource.get("SEC_TYP_CD").toString().trim() + " | Sub Type  : "+ mapTarget.get("SubType").toString().trim()+ "\n";
                    mapResult.put("Row", String.valueOf(i + 1));
                    Set keys = mapTarget.keySet();
                    }

            }

            if (keyFound == 1) {
                Set keys = mapTarget.keySet();
                Iterator itr = keys.iterator();
                String key1 = "";
                String value = "";
                System.out.println("Key Found");



                while (itr.hasNext()) {
                    key1 = (String) itr.next();
                    value = (String) mapTarget.get(key1);
                   // System.out.println(key1 + ":" + value + "\n");
                    //System.out.println(mapSource.values());

                      //System.out.println("ManinderValue"+key1);
                    switch (key1.toString().trim().toLowerCase()) {


                        case "trade_date":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            try {
                                if(mapSource.get(key1).toString().trim().equals("NAN")||mapTarget.get(key1).toString().trim().equals("NAN"))
                                {
                                    if (mapSource.get(key1).toString().trim().equals(mapTarget.get(key1).toString().trim()))
                                    {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name = " + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + mapTarget.get(key1) + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }
                                }
                                else {


                                    Date inpdate1 = new SimpleDateFormat("MM/dd/yyyy hh:mm").parse(mapSource.get(key1).toString().trim());
                                    System.out.println("\n" + inpdate1 + "  ");
                                    String inpdate11 = new SimpleDateFormat("dd.MM.yy").format(inpdate1);

                                    Date outdate1 = new SimpleDateFormat("MM/dd/yyyy").parse(mapTarget.get(key1).toString().trim());
                                    String outdate11 = new SimpleDateFormat("dd.MM.yy").format(outdate1);
                                    System.out.println("\n" + inpdate11 + "  ");
                                    System.out.println("\n" + outdate11 + "  ");


                                    //if (mapSource.get("MaturityDate").toString().trim().contains(mapTarget.get("Maturity Date").toString().trim())) {

                                    if (inpdate11.contains(outdate11)) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name = " + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + mapTarget.get(key1) + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }


                                }
                            }

                            catch(ParseException e){
                                        e.printStackTrace();
                                    }



                            break;

                        case "prin_local_crrncy":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );

                            if(mapTarget.get("Trade Type").toString().trim().contains("FX Option"))
                            {
                                if (mapTarget.get("Ccy Pair").toString().trim().contains(mapSource.get("LOC_CRRNCY_CD").toString().trim()) &&
                                        mapTarget.get("Ccy Pair").toString().trim().contains(mapSource.get("PRIN_LOCAL_CRRNCY").toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                    //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" +"Ccy Pair", " |Expected: " + mapSource.get("LOC_CRRNCY_CD")+"/"+mapSource.get("PRIN_LOCAL_CRRNCY") + " | Actual: " + value + " |"+"\n");
                                    System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }

                            }
                            else{
                                if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                    //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                    System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }





                        case "mature_date":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            try {
                                if(mapSource.get(key1).toString().trim().equals("NAN")||mapTarget.get(key1).toString().trim().equals("NAN"))
                                {
                                    if (mapSource.get(key1).toString().trim().equals(mapTarget.get(key1).toString().trim()))
                                    {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name = " + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + mapTarget.get(key1) + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }
                                }
                                else {


                                    Date inpdate1 = new SimpleDateFormat("MM/dd/yyyy hh:mm").parse(mapSource.get(key1).toString().trim());
                                    System.out.println("\n" + inpdate1 + "  ");
                                    String inpdate11 = new SimpleDateFormat("dd.MM.yy").format(inpdate1);

                                    Date outdate1 = new SimpleDateFormat("MM/dd/yyyy").parse(mapTarget.get(key1).toString().trim());
                                    String outdate11 = new SimpleDateFormat("dd.MM.yy").format(outdate1);
                                    System.out.println("\n" + inpdate11 + "  ");
                                    System.out.println("\n" + outdate11 + "  ");


                                    //if (mapSource.get("MaturityDate").toString().trim().contains(mapTarget.get("Maturity Date").toString().trim())) {

                                    if (inpdate11.contains(outdate11)) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name = " + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + mapTarget.get(key1) + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }


                                }
                            }

                            catch(ParseException e){
                                e.printStackTrace();
                            }
                            break;




                        case "underlyingsecurityname":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().contains("IRS"))
                            {
                                //System.out.println("I Am IRS Trade"+ mapSource.get("SEC_TYP_CD")+ "  "+ mapSource.get("TRADE_ID")+"\n");
                                break;
                            }
                            else if(mapSource.get("SEC_TYP_CD").toString().trim().equals("CCO"))
                            {
                             if(value.toString().trim().contains("OFX") && mapSource.get("SEC_NAME").toString().trim().contains("FXO"))
                             {
                                 counterPass = counterPass + 1;
                                 break;
                             }
                             else
                             {
                                 counterFail = counterFail + 1;
                                 mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_NAME") + " | Actual: " + value + " |" + "\n");
                                 //System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                 break;
                             }
                            }
                            else {

                                if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                    //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |" + "\n");
                                    System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }


                        case "trans_type":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if(mapSource.get(key1).toString().trim().toLowerCase().contains(value.toString().trim().toLowerCase()))
                            {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }


                        case "acct_cd":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }



                        case "trans_sub_type":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if(value.toString().trim().equals("Contract Create/New") && mapSource.get(key1).toString().trim().equals("OPEN") ||
                                    value.toString().trim().equals("Assignment") && mapSource.get(key1).toString().trim().equals("FULL_ASSGN") ||
                                    value.toString().trim().equals("Terminated") && mapSource.get(key1).toString().trim().equals("FULL_UNWD") ||
                                    value.toString().trim().equals("Part Assignment") && mapSource.get(key1).toString().trim().equals("PART_ASSGN") ||
                                    value.toString().trim().equals("Part Terminated") && mapSource.get(key1).toString().trim().equals("PART_UNWD") ||
                                    value.toString().trim().equals(mapSource.get(key1).toString().trim()) )

                            {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }


                        case "deal_spread":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "fixed_rate":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if(value.toString().trim().contains("0") && mapSource.get(key1).toString().trim().contains("NAN"))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else if(value.toString().trim().contains("NAN") && mapSource.get(key1).toString().trim().contains("0"))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }

                            else if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "float_rate":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "put/call":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if(mapSource.get(key1).toString().trim().toLowerCase().contains(value.toString().trim().toLowerCase()))
                            {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "allocation_target_qty":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().contains("IRS"))
                            {
                                //System.out.println("I Am IRS Trade"+ mapSource.get("SEC_TYP_CD")+ "  "+ mapSource.get("TRADE_ID")+"\n");


                                   System.out.println("I am in IRS Alloc   "+Math.abs((int)Integer.parseInt(mapSource.get("ALLOCATION_TARGET_QTY").toString().trim())));
                                System.out.println("I am in IRS  Notional  "+Math.abs((int)Integer.parseInt(mapTarget.get("TARGET_NOTNL_AMT").toString().trim())));

                                break;
                            }
                            if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "exec_broker":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );

                            if(mapTarget.get("TRANS_SUB_TYPE").toString().trim().contains("Terminated"))
                            {
                                if(mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("CITI") ||
                                        mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SBOP") ||
                                        mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SBPT") ||
                                        mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SBSH") ||
                                        mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SSAE") ||
                                        mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SSAL") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("AST1") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPM") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPHQ") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPOP") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPPT") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPM-BB") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPM-MA") ||
                                        mapTarget.get("Broker").toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("FXCH") ||
                                        mapTarget.get("Broker").toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("BXS1") ||
                                        mapTarget.get("Broker").toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSCO") ||
                                        mapTarget.get("Broker").toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSCO-BB") ||
                                        mapTarget.get("Broker").toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MCSO-MA") ||
                                        mapTarget.get("Broker").toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSOP") ||
                                        mapTarget.get("Broker").toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSPT") ||
                                        mapTarget.get("Broker").toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("FXML") ||
                                        mapTarget.get("Broker").toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("MLCO") ||
                                        mapTarget.get("Broker").toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("MLOP") ||
                                        mapTarget.get("Broker").toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("MLPT") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSAT") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSCO") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSCO-BB") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSCO-MA") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSDX") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSOP") ||
                                        mapTarget.get("Broker").toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSPT") ||
                                        mapTarget.get("Broker").toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBPT") ||
                                        mapTarget.get("Broker").toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBS") ||
                                        mapTarget.get("Broker").toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBSA") ||
                                        mapTarget.get("Broker").toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBST") ||
                                        mapTarget.get("Broker").toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBSW") ||
                                        mapTarget.get("Broker").toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("CSFB") ||
                                        mapTarget.get("Broker").toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("CSNY") ||
                                        mapTarget.get("Broker").toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("FBOP") ||
                                        mapTarget.get("Broker").toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("FXCS") ||
                                        mapTarget.get("Broker").toString().trim().equals("ICUSUS33XXX") && mapSource.get(key1).toString().trim().equals("ICE") ||
                                        mapTarget.get("Broker").toString().trim().equals("BARCUS33XXX") && mapSource.get(key1).toString().trim().equals("BRCP") ||
                                        mapTarget.get("Broker").toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("FXCI") ||
                                        mapTarget.get("Broker").toString().trim().equals("MIDLUS22XXX") && mapSource.get(key1).toString().trim().equals("FXHS") ||
                                        mapTarget.get("Broker").toString().trim().equals("ROYCCAT2XXX") && mapSource.get(key1).toString().trim().equals("FXRB") ||
                                        mapTarget.get("Broker").toString().trim().equals("BNPAFRPPXXX") && mapSource.get(key1).toString().trim().equals("FXBP") ||

                                        mapTarget.get("Broker").toString().trim().contains(mapSource.get(key1).toString().trim())

                                )
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + mapTarget.get("Broker") + " |"+"\n");
                                    System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + mapTarget.get("Broker"));
                                    break;
                                }
                            }
                            else {


                                if (value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("CITI") ||
                                        value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SBOP") ||
                                        value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SBPT") ||
                                        value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SBSH") ||
                                        value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SSAE") ||
                                        value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("SSAL") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("AST1") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPM") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPHQ") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPOP") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPPT") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPM-BB") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("JPM-MA") ||
                                        value.toString().trim().equals("CHASGB2LDPC") && mapSource.get(key1).toString().trim().equals("FXCH") ||
                                        value.toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("BXS1") ||
                                        value.toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSCO") ||
                                        value.toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSCO-BB") ||
                                        value.toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MCSO-MA") ||
                                        value.toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSOP") ||
                                        value.toString().trim().equals("MSCSUS33XXX") && mapSource.get(key1).toString().trim().equals("MSPT") ||
                                        value.toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("FXML") ||
                                        value.toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("MLCO") ||
                                        value.toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("MLOP") ||
                                        value.toString().trim().equals("BOFAUS3DEQN") && mapSource.get(key1).toString().trim().equals("MLPT") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSAT") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSCO") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSCO-BB") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSCO-MA") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSDX") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSOP") ||
                                        value.toString().trim().equals("GSILGB2XXXX") && mapSource.get(key1).toString().trim().equals("GSPT") ||
                                        value.toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBPT") ||
                                        value.toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBS") ||
                                        value.toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBSA") ||
                                        value.toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBST") ||
                                        value.toString().trim().equals("UBSWUS33XXX") && mapSource.get(key1).toString().trim().equals("UBSW") ||
                                        value.toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("CSFB") ||
                                        value.toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("CSNY") ||
                                        value.toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("FBOP") ||
                                        value.toString().trim().equals("CSFPGB2LXXX") && mapSource.get(key1).toString().trim().equals("FXCS") ||
                                        value.toString().trim().equals("ICUSUS33XXX") && mapSource.get(key1).toString().trim().equals("ICE") ||
                                        value.toString().trim().equals("BARCUS33XXX") && mapSource.get(key1).toString().trim().equals("BRCP") ||
                                        value.toString().trim().equals("CITIUS33XXX") && mapSource.get(key1).toString().trim().equals("FXCI") ||
                                        value.toString().trim().equals("MIDLUS22XXX") && mapSource.get(key1).toString().trim().equals("FXHS") ||
                                        value.toString().trim().equals("ROYCCAT2XXX") && mapSource.get(key1).toString().trim().equals("FXRB") ||
                                        value.toString().trim().equals("BNPAFRPPXXX") && mapSource.get(key1).toString().trim().equals("FXBP") ||

                                        value.toString().trim().contains(mapSource.get(key1).toString().trim())

                                ) {
                                    // if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                    //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |" + "\n");
                                    System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }

                        case "last_reset_price":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );
                            if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }


                        case "target_notnl_amt":
                            System.out.println(" Value of  "+key1+" is "+value+" source value is "+mapSource.get(key1) );

                            if(mapTarget.get(key1).toString().trim().equals("NAN")||mapSource.get(key1).toString().trim().equals("NAN"))
                            {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }
                            else if (Math.abs((int)Double.parseDouble(value.toString().trim()))==Math.abs((int)Double.parseDouble(mapSource.get(key1).toString().trim()))) {
                                counterPass = counterPass + 1;
                                break;
                                //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |"+"\n");
                                System.out.println("Data Mismatched for " + id + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        default: {
                            System.out.println("");
                        }

                    }
                }
                Set resKeys = mapResult.keySet();
                Iterator resItr = resKeys.iterator();
                String resKey;
                String resValue;
                while (resItr.hasNext()) {
                    resKey = (String) resItr.next();
                    resValue = (String) mapResult.get(resKey);
                    mismatchList = mismatchList + " " + resKey + " " + resValue;
                }

                if ((counterFail == 0) && (keyFound ==1) ) {
                    exception = "\n" +"No Mismatches for  "+id+" RESULT: PASSED";
                    return exception;

                }else{
                    if((mapResult.size()>1) && (keyFound ==1)) {
                        exception = exception + "\n" + "Mismatch at Row :" + mapResult.get("Row") + " " + "for "+id+" "+ mismatchList;
                        return exception;
                    }
                }

            }

        }
        if (keyFound==0)
        {
            //exception = "\n" +"No Match Found for  "+id+" RESULT: Failed";
            //return exception;
        }
        return exception;

    }
















    public HashMap<String, String> createTransformationData(LinkedHashMap json, String column){
        HashMap<String, String> mapTrans = new HashMap<String, String>();
        String Key;
        String Value;
        Set entrySet = json.entrySet();
        Iterator it = entrySet.iterator();
        int flagFound = 0;

        while(it.hasNext()) {
            //System.out.println(it.next().toString());
            String rowData = it.next().toString();
            if(rowData.toString().trim().contains(column.toString().trim())) {
                flagFound=1;
                rowData = rowData.replaceAll(column+"=","").replaceAll("\\[","").replaceAll("]","");
                rowData = rowData.replaceAll("\\\\","").replaceAll("\\{","").replaceAll("}","");
                String[] data = rowData.split(",");
                for(int i = 0; i<data.length;i++){
                    String[] res = data[i].toString().split(":");
                    mapTrans.put(res[0],res[1]);
                }
            }
        }
        return mapTrans;
    }











    public  HashMap<String,String> createData(String[] arrItems,String dimiliter) {
        HashMap<String, String> mapData = new HashMap<String, String>();
        for (int i = 0; i < arrItems.length; i++) {
            String[] res= null;
            if(dimiliter.equals("=")) {
                res = arrItems[i].split("=", -1);
            }else
            {
                res = arrItems[i].split(":", -1);
            }
            if(res[1].equals("")||res[1].equals("(null)"))
            {
                mapData.put(res[0], "NAN");
                //System.out.println("Data "+res[0]+" : "+ res[1]);
            }
            else {
                mapData.put(res[0], res[1]);
//                System.out.println("Data "+res[0]+" : "+ res[1]);
            }
        }

        return mapData;
    }


    public int getmaxversion(String srcTradeID,String[] arrTarget){

        int len = arrTarget.length;
        HashMap<String, Integer> mapTrdVeresion= new HashMap<String, Integer>();
        mapTrdVeresion.put(srcTradeID,0);
        for (int i=0;i<len;i++){
            String[] rowData = arrTarget[i].split(", ");
            HashMap<String, String> mapData = createData(rowData, ":");

            if ( srcTradeID.equalsIgnoreCase(mapData.get("TRADE_ID"))){
               int version= Integer.parseInt(mapData.get("Version").toString().trim());
               if(mapTrdVeresion.get(srcTradeID) < version) {
                   mapTrdVeresion.put(srcTradeID, version);
               }
            }


        }
        return mapTrdVeresion.get(srcTradeID);

    }










    public String getTargetColumnName(ArrayList arrMapingData,String sourceColumnName ){

        String targetColumnName="";
        for (int arrItr = 0; arrItr < arrMapingData.size(); arrItr++) {
            String actual = arrMapingData.get(arrItr).toString();
            actual = actual.replaceAll("\\{", "");
            actual = actual.replaceAll("}", "");
            String[] actualCOlumnList = actual.split(", ");
            HashMap<String, String> mapMappingData = createData(actualCOlumnList, "=");
            if (mapMappingData.get("Input_File_Field").toString().trim().equals(sourceColumnName)){
                targetColumnName = mapMappingData.get("Output_File_Field").toString().trim();
                break;
            }

        }

        return targetColumnName;

    }







}
