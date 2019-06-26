package karate;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DataCompareConversion {
    public String compareData(ArrayList arrSource, String arrTarget, ArrayList arrMapingData,ArrayList arrTransformation,String id) throws Exception {
        String result = "";
        int passCounter = 0;
        int failCounter = 0;
        int noMatchCounter=0;
        String noMatchCounterString="";
        System.out.println("MANINDER SINGH KAINTH");
        int sourceLen = arrSource.size();
        int targetLen = arrTarget.length();
        int transformationLen=arrTransformation.size();

        MultiMap<String,String> transformationMap = new MultiMap<>();

        arrTarget = arrTarget.replace("[", "");
        arrTarget = arrTarget.replace("]", "");
        arrTarget = arrTarget.replace("}", "}@");
        arrTarget = arrTarget.replaceAll("}", "");
        arrTarget = arrTarget.replaceAll("\\{", "");
        arrTarget = arrTarget.replaceAll("\",", "\", ");
        arrTarget = arrTarget.replaceAll("\"", "");
        arrTarget = arrTarget.substring(0, arrTarget.length() - 1) + "";
        String[] arrTargetFinal = arrTarget.split("@,");
        if(transformationLen>0)
        {
            for (int i = 0; i < transformationLen; i++) {
                String actual = arrTransformation.get(i).toString();
                actual = actual.replaceAll("\\{", "");
                actual = actual.replaceAll("}", "");
                String[] actualCOlumnList = actual.split(", ");
                String[] keyList = actualCOlumnList[0].split("=");
                String[] valueList = actualCOlumnList[1].split("=");
                String key = keyList[1];
                String val=valueList[1];

                System.out.println("Transformation KEY:" +key);
                System.out.println("Transformation VALUE :" +val);

                transformationMap.put(key,val);
                }
            System.out.println("----- Printing Multimap using keySet -----\n");
            for (String str : transformationMap.keySet()) {
                System.out.println(str + ": " + transformationMap.get(str));
            }


        }





        if (sourceLen > 0) {
            for (int i = 0; i < sourceLen; i++) {
                String actual = arrSource.get(i).toString();
                actual = actual.replaceAll("\\{", "");
                actual = actual.replaceAll("}", "");
                String[] actualCOlumnList = actual.split(", ");
//                System.out.println(actual);
               // System.out.println("MANINDER SINGH KAINTH");
                HashMap<String, String> map = createData(actualCOlumnList, "=");
                String resTemp = validateTarget(arrTargetFinal, map, arrMapingData,transformationMap);
                if(resTemp.contains("M")) {
                    result = result + "\n" + resTemp;
                }
                if (resTemp.contains("Mismatch") || resTemp.contains("No Match Found for")) {

                    failCounter = failCounter + 1;
                } else {
                    passCounter = passCounter + 1;
                }
                if(resTemp.contains("No Match Found for"))
                {
                    noMatchCounter=noMatchCounter+1;
                    noMatchCounterString=noMatchCounterString+map.get("ALC_TRADE_ID")+",";
                }
                if (i == sourceLen - 1) {
                    result = result + "\n" + "\n" + "TOTAL " + id + "s Validated: " + sourceLen + " | TOTAL ROWS PASSED: " + passCounter + " | TOTAL ROWS WITH MISMATCHES: " + failCounter + "|"  + "| TOTAL ROWS NOT FOUND:"+noMatchCounter+ "|"+"\n"+"\n"+noMatchCounterString;

                }
            }
        }
        return (String)result;
    }

    public String validateTarget(String[] arrTarget, HashMap<String, String> mapSource, ArrayList arrMapingData,MultiMap<String,String> transformationMap) throws Exception {
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

            //System.out.println("Testing ACCID for"+mapSource.get("Acid"));
            id = "Trade ID" + " :" + mapSource.get("ALC_TRADE_ID") +  " | SEC_TYP_CD :" + mapSource.get("SEC_TYP_CD") + "  "+ "\n";

            //int versionNum=Integer.parseInt(mapTarget.get("Version").toString().trim());


            if (mapSource.get("ALC_TRADE_ID").toString().trim().equals(mapTarget.get("ALC_TRADE_ID").toString().trim()) ) {

                /*int maxSwap = getmaxSwap(mapSource.get("ALC_TRADE_ID").toString().trim(),arrTarget);
                System.out.println("My max SWAP is:" + maxSwap);
                if(Integer.parseInt(mapTarget.get("Swap Id").toString().trim())==maxSwap) {*/

                    System.out.println("MANINDER SINGH KAINTH KEY FOUND");
                    keyFound = 1;
                    id = "Trade ID" + " :" + mapSource.get("ALC_TRADE_ID") + " | SEC_TYP_CD :" + mapSource.get("SEC_TYP_CD") + " | Product Type  : " + mapTarget.get("Product Type") + "\n";
                    mapResult.put("Row", String.valueOf(i + 1));
                    Set keys = mapTarget.keySet();





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

                        case "trade date" :
                            System.out.println(" Value of  "+key1+" is "+value );


                            if(getDate(mapSource.get("BLK_TRADE_DATE").toString().trim(),"MM/dd/yyyy hh:mm").contains(getDate(mapTarget.get("Trade Date").toString().trim(),"MM/dd/yyyy")))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else
                            {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_TRADE_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }


                            case "settle date":
                                System.out.println(" Value of  "+key1+" is "+value );

                                if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDSI") || mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDS") )
                                {
                                    if(getDate(mapSource.get("BLK_PAY_DATE").toString().trim(),"MM/dd/yyyy hh:mm" ).contains(getDate(mapTarget.get("Settle Date").toString().trim(),"MM/dd/yyyy")))
                                    {
                                        counterPass = counterPass + 1;
                                        break;
                                    }
                                    else
                                    {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_PAY_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }
                                }

                                else
                                {
                                    break;
                                }


                        case "effective date":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN"))
                            {
                                break;
                            }

                            if(getDate(mapSource.get("LEG1_EFFEC_DATE").toString().trim(),"MM/dd/yyyy hh:mm").contains(getDate(mapTarget.get("Effective Date").toString().trim(),"MM/dd/yyyy")))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else
                            {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG1_EFFEC_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }


                        case "maturity date":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(getDate(mapSource.get("SEC_MATURE_DATE").toString().trim(),"MM/dd/yyyy hh:mm").contains(getDate(mapTarget.get("Maturity Date").toString().trim(),"MM/dd/yyyy")))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else
                            {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_MATURE_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "account abbreviation":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(mapSource.get("ALC_ACCT_CD").toString().trim().contains(mapTarget.get("Account Abbreviation").toString().trim()))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else
                            {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("ALC_ACCT_CD") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "notional":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN") && mapTarget.get("Product Type").toString().trim().equals("EQO"))
                          {
                              break;
                          }
                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDSI"))
                            {
                                if((Math.abs(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim()))*Math.abs(getInteger(mapSource.get("SEC_FACTOR"))))==Math.abs(getInteger(mapTarget.get("Notional").toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: ALC_EXEC_QTY * SEC_FACTOR " + (Math.abs(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim()))*Math.abs(getInteger(mapSource.get("SEC_FACTOR")))) + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }

                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("CCO"))
                            {
                                if((Math.ceil(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim())/(getInteger(mapSource.get("SEC_STRIKE_PRICE").toString().trim()))))==Math.ceil(getInteger(mapTarget.get("Notional").toString().trim())) ||
                                        Math.abs(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim()))==Math.abs(getInteger(mapTarget.get("Notional").toString().trim())) )
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: ALC_EXEC_QTY / Strike Price " + (Math.abs(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim()))/Math.abs(getInteger(mapSource.get("SEC_STRIKE_PRICE")))) + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }


                           if(Math.abs(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim()))==Math.abs(getInteger(mapTarget.get("Notional").toString().trim())))
                           {
                               counterPass = counterPass + 1;
                               break;
                           }
                           else{
                               counterFail = counterFail + 1;
                               mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("ALC_EXEC_QTY") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                               break;
                           }




                        case "isin":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDS"))
                        {
                            if(mapSource.get("UND_ISIN_NO").toString().trim().contains(mapTarget.get("ISIN").toString().trim()))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else{
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("UND_ISIN_NO") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }
                        }
                            else
                            {
                                break;
                            }


                        case "currency code":
                            System.out.println(" Value of  "+key1+" is "+value );


                            if(mapSource.get("BLK_TDE_CRRNCY").toString().trim().contains(mapTarget.get("Currency Code").toString().trim()) ||
                                    mapSource.get("SEC_LOC_CRRNCY_CD").toString().trim().contains(mapTarget.get("Currency Code").toString().trim()))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else{
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_TDE_CRRNCY") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "id5":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().contains("SWAPINF"))
                            {
                                break;
                            }

                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN") && mapTarget.get("Product Type").toString().contains("FXO"))
                            {
                                if(mapSource.get("UND_EXCH_CD").toString().trim().contains(value.toString().trim()))
                                {

                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("UND_EXCH_CD") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }

                            }

                          else if(mapSource.get("BLK_EXCH_CD").toString().trim().equals("OTC"))
                          {
                              if(mapTarget.get("ID5").toString().trim().contains("OTC"))
                              {
                                  counterPass = counterPass + 1;
                                  break;
                              }
                              else
                              {
                                  counterFail = counterFail + 1;
                                  mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_EXCH_CD") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                  break;
                              }
                          }
                          else
                          {
                              break;
                          }

                        case "payment frequency description":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN"))
                            {
                                break;
                            }

                            if(mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("Q") && value.toString().trim().equals("Quarterly")||
                                        mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("A") && value.toString().trim().equals("Annual") ||
                            mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("S") && value.toString().trim().equals("Semi Annual") ||
                                    mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("1Y") && value.toString().trim().equals("Annual") ||
                                    mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("6M") && value.toString().trim().equals("Semi Annual") ||
                                    mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("3M") && value.toString().trim().equals("Quarterly") ||
                                            mapSource.get("LEG1_PAY_FREQ").toString().trim().equals("1T") && value.toString().trim().equals("Zero Coupon"))

                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else{
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG1_PAY_FREQ") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "payment frequency description 2":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPIRS"))


                            {
                                if(mapSource.get("LEG2_PAY_FREQ").toString().trim().equals("Q") && value.toString().trim().equals("Quarterly")||
                                        mapSource.get("LEG2_PAY_FREQ").toString().trim().equals("A") && value.toString().trim().equals("Annual") ||
                                        mapSource.get("LEG2_PAY_FREQ").toString().trim().equals("S") && value.toString().trim().equals("Semi Annual") ||
                                        mapSource.get("LEG2_PAY_FREQ").toString().trim().equals("1Y") && value.toString().trim().equals("Annual") ||
                                        mapSource.get("LEG2_PAY_FREQ").toString().trim().equals("6M") && value.toString().trim().equals("Semi Annual") ||
                                        mapSource.get("LEG2_PAY_FREQ").toString().trim().equals("3M") && value.toString().trim().equals("Quarterly")) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG2_PAY_FREQ") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else {
                                break;
                            }

                        case "protection":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDSI") || mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDS") )
                            {
                                if(mapSource.get("ORD_PROT_TYPE").toString().trim().equals("P") && mapTarget.get("Protection").toString().trim().equals("Buy") ||
                                        mapSource.get("ORD_PROT_TYPE").toString().trim().equals("R") && mapTarget.get("Protection").toString().trim().equals("Sell"))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("ORD_PROT_TYPE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }

                            else
                            {
                                break;
                            }

                        case "pay/receive":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPIRS"))
                            {
                                if (mapSource.get("LEG1_SWAP_LEG_IND").toString().trim().equals("P") && (value.toString().trim().equals("Pay")) ||
                                        mapSource.get("LEG1_SWAP_LEG_IND").toString().trim().equals("R") && (value.toString().trim().equals("Receive"))) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG1_SWAP_LEG_IND") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else {
                                break;
                            }

                        case "rate":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapTarget.get("Product Type").toString().trim().contains("SWO"))
                            {
                                break;
                            }

                            if(mapTarget.get("Product Type").toString().trim().contains("IFS"))
                            {
                                if(mapSource.get("BLK_IRR_RATE").toString().trim().equals(value.toString().trim()))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_IRR_RATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }

                            else if( Math.abs(getInteger(mapSource.get("LEG1_COUPON_RATE").toString().trim()))==Math.abs(getInteger(value.toString().trim())))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }
                            else{
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG1_COUPON_RATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "first payment date":
                            System.out.println(" Value of  "+key1+" is "+value );

                            LocalDate startDate = LocalDate.parse(getDate(mapSource.get("LEG1_FST_PAY_DATE").toString().trim(),"MM/dd/yyyy hh:mm" ));
                            LocalDate endDate = LocalDate.parse(getDate(value.toString().trim(),"MM/dd/yyyy"));
                            if(getDate(mapSource.get("LEG1_FST_PAY_DATE").toString().trim(),"MM/dd/yyyy hh:mm" ).contains(getDate(value.toString().trim(),"MM/dd/yyyy")))
                            {
                                counterPass = counterPass + 1;
                                break;
                            }


                            else if(Math.abs(ChronoUnit.DAYS.between(startDate, endDate))<=2)
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }


                            else{


                                /*SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
                                Date dt1=format1.parse(input_date);
                                DateFormat format2=new SimpleDateFormat("EEEE");
                                String finalDay=format2.format(dt1);*/

                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG1_FST_PAY_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }



                        case "fixing index name 2":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPIRS"))
                            {
                                Boolean bool=false;
                                if(transformationMap.containsKey(mapSource.get("LEG2_FLT_INDX").toString().trim())) {

                                    bool = validateTransformation(mapSource.get("LEG2_FLT_INDX").toString().trim(), mapTarget.get("Fixing Index Name 2").toString().trim(),  transformationMap);
                                    if (bool) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name=" + key1 + " Trans", " |Expected: " + mapSource.get("LEG2_FLT_INDX").toString().trim() + " | Actual: " + mapTarget.get("Fixing Index Name 2").toString().trim() + " |" + "\n");
//
                                        break;
                                    }
                                }
                                else
                                {
                                    if(mapSource.get("LEG2_FLT_INDX").toString().trim().contains(mapTarget.get("Fixing Index Name 2").toString().trim())) {
                                        counterPass = counterPass + 1;
                                        break;
                                    }
                                    else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG2_FLT_INDX").toString().trim() + " | Actual: " + mapTarget.get("Fixing Index Name 2").toString().trim() + " |" + "\n");
                                        break;
                                    }
                                }
                            }
                            else {
                                break;
                            }


                        case "fixing term 2":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPIRS"))
                            {
                                if(mapSource.get("LEG2_IDX_PAY_FREQ").toString().trim().equals("Q") && value.toString().trim().equals("Quarterly")||
                                        mapSource.get("LEG2_IDX_PAY_FREQ").toString().trim().equals("A") && value.toString().trim().equals("Annual") ||
                                        mapSource.get("LEG2_IDX_PAY_FREQ").toString().trim().equals("S") && value.toString().trim().equals("Semi Annual")||
                                        mapSource.get("LEG2_IDX_PAY_FREQ").toString().trim().equals("1Y") && value.toString().trim().equals("Annual") ||
                                        mapSource.get("LEG2_IDX_PAY_FREQ").toString().trim().equals("6M") && value.toString().trim().equals("Semi Annual") ||
                                        mapSource.get("LEG2_IDX_PAY_FREQ").toString().trim().equals("3M") && value.toString().trim().equals("Quarterly")) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("LEG2_IDX_PAY_FREQ") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else {
                                break;
                            }


                        case "broker id":
                            System.out.println(" Value of  "+key1+" is "+value+"  "+mapSource.get("ALC_TRADE_ID"));
                            if(!mapSource.get("SEC_CCP_IND").toString().trim().contains("CCP")) {
                                String sourceBroker = mapSource.get("BRK_CD").toString().trim();
                                String targetBroker = mapTarget.get("Broker Id").toString().trim();
                                Boolean bool = false;
                                if (transformationMap.containsKey(sourceBroker)) {

                                    bool = validateTransformation(sourceBroker, targetBroker, transformationMap);
                                    if (bool) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name=" + key1 + " Trans", " |Expected: " + sourceBroker + " | Actual: " + targetBroker + " |" + "\n");
//
                                        break;
                                    }
                                } else {
                                    if (sourceBroker.toString().trim().contains(targetBroker.toString().trim())) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + sourceBroker + " | Actual: " + targetBroker + " |" + "\n");
                                        break;
                                    }
                                }
                            }

                            else{
                                break;
                            }

                        case "broker":
                            System.out.println(" Value of  "+key1+" is "+value+" "+mapSource.get("ALC_TRADE_ID") );

                            if(mapSource.get("SEC_CCP_IND").toString().trim().equals("CCP")) {

                                String sourceBroker = mapSource.get("ALC_CLR_BRK_CD").toString().trim();
                                String targetBroker = mapTarget.get("Broker").toString().trim();


                                Boolean bool = false;
                                if (transformationMap.containsKey(sourceBroker)) {

                                    bool = validateTransformation(sourceBroker, targetBroker, transformationMap);
                                    if (bool) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name=" + key1 + " Trans", " |Expected: " + sourceBroker + " | Actual: " + targetBroker + " |" + "\n");
//
                                        break;
                                    }
                                } else {
                                    if (sourceBroker.toString().trim().contains(targetBroker.toString().trim())) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + sourceBroker + " | Actual: " + targetBroker + " |" + "\n");
                                        break;
                                    }
                                }
                            }
                            else{
                                break;
                            }









                        case "issuer name":
                            System.out.println(" Value of  "+key1+" is "+value );




                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDSI") || mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPCDS") )
                            {
                                Boolean bool=false;
                                if(transformationMap.containsKey(value.toString().trim())) {

                                    bool = validateTransformation(value.toString().trim(),mapSource.get("SEC_DESCRIPTION").toString().trim(),transformationMap);
                                    if (bool) {
                                        counterPass = counterPass + 1;
                                        break;
                                    } else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name=" + key1 + " Trans", " |Expected: " + mapSource.get("SEC_DESCRIPTION").toString().trim() + " | Actual: " + value.toString().trim() + " |" + "\n");
//
                                        break;
                                    }
                                }
                                else
                                {
                                    if(mapSource.get("SEC_DESCRIPTION").toString().trim().contains(value.toString().trim())) {
                                        counterPass = counterPass + 1;
                                        break;
                                    }
                                    else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_DESCRIPTION").toString().trim() + " | Actual: " + value + " |" + "\n");
                                        break;
                                    }
                                }
                            }
                            else {
                                break;
                            }






                        case "lasteqtyresetprice":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPIRS"))
                            {
                                if(getInteger(mapSource.get("ORD_EXEC_PRICE").toString().trim()).equals(getInteger(mapTarget.get("LastEqtyResetPrice").toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("ORD_EXEC_PRICE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }

                            }
                            else
                            {
                                break;
                            }



                        case "spread":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(mapSource.get("SEC_TYP_CD").toString().trim().equals("SWAPIRS"))
                            {
                                if( getInteger(mapSource.get("BLK_DEAL_SPREAD").toString().trim()).equals(getInteger(mapTarget.get("Spread").toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_DEAL_SPREAD") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else
                            {
                                break;
                            }
                        case "# of shares/options":
                            System.out.println(" Value of  "+key1+" is "+value );

                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN") && mapTarget.get("Product Type").toString().trim().equals("EQO")){
                                if(Math.abs(getInteger(mapSource.get("ALC_EXEC_QTY").toString().trim()))==Math.abs(getInteger(mapTarget.get("# of Shares/Options").toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("ALC_EXEC_QTY") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }



                        case "optionbuysellindicator":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(mapSource.get("BLK_TRANS_TYPE").toString().trim().equals("BUYL") && value.toString().trim().equals("Buyer") ||
                                        mapSource.get("BLK_TRANS_TYPE").toString().trim().equals("SELLS") && value.toString().trim().equals("Seller") ||
                                        mapSource.get("BLK_TRANS_TYPE").toString().trim().equals("SELLS") && value.toString().trim().equals("Sell") ||
                                        mapSource.get("BLK_TRANS_TYPE").toString().trim().equals("BUYL") && value.toString().trim().equals("Buy"))

                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_TRANS_TYPE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "settlement amount":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(Math.abs(getInteger(mapSource.get("NORM_ALC_EXEC_AMT").toString().trim()))==Math.abs(getInteger(mapTarget.get("Settlement Amount").toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("NORM_ALC_EXEC_AMT") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }


                        case "option exercise end date":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(getDate(mapSource.get("SEC_EXPIRY_DATE").toString().trim(),"MM/dd/yyyy hh:mm").contains(getDate(value.toString().trim(),"MM/dd/yyyy")))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_EXPIRY_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "option exercise start date":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                //if(mapSource.get("SEC_EXPIRY_DATE").toString().trim().contains(value.toString().trim()) || value.toString().trim().contains(mapSource.get("SEC_EXPIRY_DATE").toString().trim()))
                                  if(getDate(mapSource.get("SEC_EXPIRY_DATE").toString().trim(),"MM/dd/yyyy hh:mm").contains(getDate(value.toString().trim(),"MM/dd/yyyy")))

                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_EXPIRY_DATE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "option type":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(mapSource.get("SEC_OPTION_STYLE").toString().trim().equals("E") &&  value.toString().trim().equals("European") ||
                                        mapSource.get("SEC_OPTION_STYLE").toString().trim().equals("E") &&  value.toString().trim().equals("EURO") ||
                                        mapSource.get("SEC_OPTION_STYLE").toString().trim().equals("A") &&  value.toString().trim().equals("American") )
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_OPTION_STYLE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "putcall":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN") && !mapTarget.get("Product Type").toString().contains("SWO")){
                                if(mapSource.get("SEC_OPTION_TYPE").toString().trim().contains(value.toString().trim()))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_OPTION_TYPE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "strike price":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(Math.abs(getInteger(mapSource.get("SEC_STRIKE_PRICE").toString().trim()))==Math.abs(getInteger(value.toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_STRIKE_PRICE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "product type":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(mapSource.get("SEC_TYP_CD").toString().trim().equals("CPO") && value.toString().trim().toString().trim().equals("FXO") ||
                                        mapSource.get("SEC_TYP_CD").toString().trim().equals("SWPTNCDS") && value.toString().trim().toString().trim().equals("SWOCDX") ||
                                        mapSource.get("SEC_TYP_CD").toString().trim().equals("CCO") && value.toString().trim().toString().trim().equals("FXO") ||
                                        mapSource.get("SEC_TYP_CD").toString().trim().equals("OTCECO") && value.toString().trim().toString().trim().equals("EQO") ||
                                        mapSource.get("SEC_TYP_CD").toString().trim().equals("OTCEPO") && value.toString().trim().toString().trim().equals("EQO") )
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("SEC_TYP_CD") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "2nd leg currency":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN")){
                                if(mapTarget.get("Product Type").toString().trim().contains("SWO") || mapTarget.get("Product Type").toString().trim().contains("EQO"))
                                {
                                    break;
                                }
                                else if(mapSource.get("BLK_TDE_CRRNCY").toString().trim().contains(value.toString().trim()) ||
                                        mapSource.get("SEC_LOC_CRRNCY_CD").toString().trim().contains(value.toString().trim()))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("BLK_TDE_CRRNCY") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }

                        case "red":
                            System.out.println(" Value of  "+key1+" is "+value );
                            if(!mapSource.get("SEC_OPTION_STYLE").toString().trim().contains("NAN") && mapSource.get("SEC_TYP_CD").toString().trim().contains("SWPTNCDS")){
                                if(mapSource.get("UND_RED_CODE").toString().trim().contains(value.toString().trim()))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + key1, " |Expected: " + mapSource.get("UND_RED_CODE") + " | Actual: " + value + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
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
                    //exception = "\n" +"No Mismatches for  "+id+" RESULT: PASSED";
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
            exception = "\n" +"No Match Found for  "+id+" RESULT: Failed";
            return exception;
        }
        return exception;

    }

    public boolean validateTransformation(String srcValue,String tgtValue,MultiMap<String,String> transformationMap )
    {
        Boolean bool=false;

        Collection<String> keyVal = transformationMap.get(srcValue);
        for (String val : keyVal) {

            if (tgtValue.contains(val.toString().trim())) {
                bool = true;
                break;
            }

        }

        return bool;

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


    public String getDate(String srcDate, String pattern) throws Exception
    {
            if(srcDate.equals("NAN"))
            {
                return "1999-01-01";
            }
            else if(srcDate.contains("/")||srcDate.contains(":")||srcDate.contains("-"))
             {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date convertedCurrentDate = sdf.parse(srcDate);
                String convertedDate = new SimpleDateFormat("yyyy-MM-dd").format(convertedCurrentDate);
                return convertedDate;
            }
            else
            {
                return "1999-01-01";
            }

    }

    public Double getInteger(String src) throws Exception
    {
        Double a;
        if(src.equals("NAN"))
        {
           a=0.0;
        }
        else {
            a = (double) Double.parseDouble(src);
        }
        return a;
    }



    public int getmaxSwap(String srcTradeID,String[] arrTarget){

        int len = arrTarget.length;
        HashMap<String, Integer> mapTrdVeresion= new HashMap<String, Integer>();
        mapTrdVeresion.put(srcTradeID,0);
        for (int i=0;i<len;i++){
            String[] rowData = arrTarget[i].split(", ");
            HashMap<String, String> mapData = createData(rowData, ":");

            if ( srcTradeID.toString().trim().equalsIgnoreCase(mapData.get("Trade ID").toString().trim())){
                int swapid= Integer.parseInt(mapData.get("Swap Id").toString().trim());
                if(mapTrdVeresion.get(srcTradeID) < swapid) {
                    mapTrdVeresion.put(srcTradeID, swapid);
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
