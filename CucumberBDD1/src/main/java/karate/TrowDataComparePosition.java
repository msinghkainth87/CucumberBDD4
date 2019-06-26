package karate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TrowDataComparePosition {
    public String compareData(ArrayList arrSource, String arrTarget, ArrayList arrMapingData,String id) throws Exception {
        String result = "";
        int passCounter = 0;
        int failCounter = 0;
        int mismatchCounter=0;
        int noMatchCounter=0;
        String noMatchCounterString="";
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
                //System.out.println("RAKTIM"+resTemp);

                if (resTemp.contains("Mismatch")) {
                    result = result + "\n" + resTemp;
                    failCounter = failCounter + 1;
                } else if(resTemp.contains("No Match Found for")){
                    noMatchCounter=noMatchCounter+1;
                    if(noMatchCounterString.contains(map.get("CRDOrderID").toString().trim()) || noMatchCounterString.contains(map.get("CRDSecid").toString().trim()) )
                    {
                        System.out.println("Already Added to Queue");
                    }
                    else{
                        if(map.get("TransactionType").equals("OPTIONS"))
                        {
                            noMatchCounterString=noMatchCounterString+"Options trade "+map.get("CRDOrderID").toString().trim()+"\n";
                        }
                        else{
                            noMatchCounterString=noMatchCounterString+map.get("CRDSecid").toString().trim()+"\n";
                        }
                    }

                }
                else
                {
                    passCounter = passCounter + 1;
                }


                if (i == sourceLen - 1) {
                    result = result + "\n" + "\n" + "TOTAL " + id + "s Validated: " + sourceLen + " | TOTAL ROWS PASSED: " + passCounter + " | TOTAL ROWS WITH MISMATCHES: " + failCounter + "| TOTAL ROWS NOT FOUND:"+noMatchCounter+ "|"+"\n"+"\n"+noMatchCounterString;
                }
            }
        }
        return (String)result;
    }

    public String validateTarget(String[] arrTarget, HashMap<String, String> mapSource, ArrayList arrMapingData) throws Exception {
        String exception = "";
        int keyFound=0;
        String id="";
        int targetLen = arrTarget.length;
        String transactionType = "";
        String mismatchList = "";
        int counterPass = 0;
        int counterFail = 0;
        String sourceTradeid="";
        switch (mapSource.get("TransactionType")) {
            case "SWAPS":
                transactionType = "SWAPS";
                break;

            case "OPTIONS":
                transactionType = "OPTIONS";
                break;
        }

        if(transactionType.equals("OPTIONS"))
        {
            System.out.println(mapSource.get("CRDOrderID").toString().trim().length());
            if(mapSource.get("CRDOrderID").toString().trim().length()>11)
            {
                sourceTradeid=mapSource.get("CRDOrderID").toString().trim().substring(4);
            }
            else
            {
                sourceTradeid=mapSource.get("CRDOrderID").toString().trim();
            }


            System.out.println("CRDORDER ID iS "+sourceTradeid);
        }
        for (int i = 0; i < targetLen; i++) {
            String[] arrTargetRow = arrTarget[i].split(", ");
            HashMap<String, String> mapTarget = createData(arrTargetRow, ":");

            HashMap<String, String> mapResult = new HashMap<String, String>();
//            System.out.println("Source :"+mapSource.get("AcnoMinor").toString()+" | Target :"+mapTarget.get("AcnoMinor").toString());
//            System.out.println(mapSource.get("AcnoMinor").toString().trim().equals(mapTarget.get("AcnoMinor").toString().trim()));
//            System.out.println(transactionType.equals("SWAPS") && (((mapSource.get("CRDSecid").toString().equals(mapTarget.get("CRDSecid").toString()) || (mapSource.get("CRDSecid").toString().equals(mapTarget.get("ID3")))) || (mapSource.get("AcnoMinor").toString().equals(mapTarget.get("AcnoMinor").toString())))));
//            System.out.println("RAKTIM"+mapSource.get("CRDSecid").toString().equals(mapTarget.get("ID3"))+" "+mapSource.get("CRDSecid")+"|"+mapSource.get("CRDSecid").toString().equals(mapTarget.get("CRDSecid").toString())+"|"+mapTarget.get("ID3"));

            //System.out.println("Testing ACCID for"+mapSource.get("Acid"));



            if (transactionType.equals("OPTIONS")) {
                id = "Options Tradeid" + " : " + sourceTradeid + "  CRDSecid / ID2" + " :" + mapSource.get("CRDSecid").toString().trim()+ "  |  Product Type " + mapSource.get("InstrCode") + "   |  Accno Minor   "+mapSource.get("AcnoMinor");
            } else {
                id = "CRDSecid / ID2" + " :" + mapSource.get("CRDSecid").toString().trim() + " | ID3 :" + "  |  Product Type " + mapSource.get("InstrCode") +"   |  Accno Minor   "+mapSource.get("AcnoMinor")+ "\n";
            }

            if (transactionType.equals("SWAPS") && ((mapSource.get("CRDSecid").toString().trim().equals(mapTarget.get("CRDSecid").toString().trim()) || (mapSource.get("CRDSecid").toString().trim().equals(mapTarget.get("ID3").toString().trim()))))) {
                if (mapSource.get("AcnoMinor").toString().trim().equals(mapTarget.get("AcnoMinor").toString().trim())) {
                    keyFound = 1;
                    mapResult.put("Row", String.valueOf(i + 1));
                    Set keys = mapTarget.keySet();

                }
            } else if (transactionType.equals("OPTIONS") && sourceTradeid.toString().trim().equals(mapTarget.get("Trade ID").toString().trim())) {
                if (mapSource.get("AcnoMinor").toString().trim().equals(mapTarget.get("AcnoMinor").toString().trim())) {
                    keyFound = 1;
                    //Set keys = mapTarget.keySet();
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
                    //System.out.println(key1 + ":" + value + "\n");
                    //System.out.println(mapSource.values());

                    //System.out.println("ManinderValue"+key1);
                    switch (key1.toString().trim().toLowerCase()) {




                            case "# of shares/options":

                            System.out.println("# of shares/options" + key1 + "\n");
                           if (mapSource.get("InstrCode").toString().trim().equals("EQCLOPT") && mapSource.get("TransactionType").toString().trim().equals("OPTIONS") || mapSource.get("InstrCode").toString().trim().equals("EQPTOPT") && mapSource.get("TransactionType").toString().trim().equals("OPTIONS"))

                                //if (mapSource.get("TransactionType").toString().trim().equals("OPTIONS"))
                            {
                                if (Math.abs(getInteger(mapSource.get("Quantity").toString().trim()))==Math.abs(getInteger(mapTarget.get("# of Shares/Options").toString().trim()))) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "# of Shares/Options", " |Expected: " + mapSource.get("Quantity") + " | Actual: " + mapTarget.get("# of Shares/Options") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }

                            }
                            else{
                                break;
                            }


                        case "pay/receive":

                            System.out.println("Pay/Receive" + " " + value.toString().trim() + " " + key1 + "\n");
                            if (mapSource.get("InstrCode").toString().trim().equals("EQTRSWP") ||
                                    mapSource.get("InstrCode").toString().trim().equals("FITRSWP") ||
                                    mapSource.get("InstrCode").toString().trim().equals("ZCINSWP") ||
                                    mapSource.get("InstrCode").toString().trim().equals("INTRTSWP")) {
                                if (mapSource.get("LongShort").toString().trim().equals("L")&& mapTarget.get("Pay/Receive").toString().trim().equals("Receive")||
                                        mapSource.get("LongShort").toString().trim().equals("S")&& mapTarget.get("Pay/Receive").toString().trim().equals("Pay"))
                                         {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Pay/Receive", " |Expected: " + mapSource.get("LongShort") + " | Actual: " + mapTarget.get("Pay/Receive") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }

                            } else {
                                break;
                            }

                        case "optionbuysellindicator":

                            System.out.println("optionbuysellindicator" + " " + value.toString().trim() + " " + key1 + "\n");

                            if(mapSource.get("InstrCode").toString().trim().equals("PASWPCDX") ||
                                    mapSource.get("InstrCode").toString().trim().equals("RESWPCDX"))
                            {
                                if(mapTarget.get("OptionBuySellIndicator").toString().trim().equals("Buy") && mapSource.get("LongShort").toString().trim().equals("L") ||
                                        mapTarget.get("OptionBuySellIndicator").toString().trim().equals("Sell") && mapSource.get("LongShort").toString().trim().equals("S"))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else{
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "OptionBuySellIndicator", " |Expected:  "+mapSource.get("LongShort").toString().trim()  + " | Actual: " + mapTarget.get("OptionBuySellIndicator") + " |" + "\n");
                                 break;
                                }
                            }

                            else{
                                break;
                            }


                        case "protection":

                            System.out.println("Protection" + " " + value.toString().trim() + " " + key1 + "\n");
                            if (mapSource.get("InstrCode").toString().trim().equals("CRDEFSWP") ||
                                    mapSource.get("InstrCode").toString().trim().equals("INDTRSWP") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CMBXSWP") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CMBXSWP")) {
                                if (mapSource.get("LongShort").toString().trim().contains("L") && mapTarget.get("Protection").toString().trim().contains("Sell") ||
                                        mapSource.get("LongShort").toString().trim().contains("S") && mapTarget.get("Protection").toString().trim().contains("Buy"))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Protection", " |Expected: " + mapSource.get("LongShort") + " | Actual: " + mapTarget.get("Protection") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }

                            } else if(mapSource.get("InstrCode").toString().trim().equals("RESWPCDX")) {
                                if (mapTarget.get("Protection").toString().trim().contains("Sell"))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Protection", " |Expected: " + mapSource.get("LongShort") + " | Actual: " + mapTarget.get("Protection") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }
                            }
                            else if(mapSource.get("InstrCode").toString().trim().equals("PASWPCDX")) {
                                if (mapTarget.get("Protection").toString().trim().contains("Buy"))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Protection", " |Expected: " + mapSource.get("LongShort") + " | Actual: " + mapTarget.get("Protection") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }
                            }

                            else{
                                break;
                            }
                            // Added new code, not being used yet


                        case "executing broker":

                            System.out.println("Executing Broker" + " " + value.toString().trim() + " " + key1 + "\n");
                            break;

                        case "strike price":
                            System.out.println("Strike Price" + " " + value.toString().trim() + " " + key1 + "\n");
                            if(transactionType.equals("OPTIONS"))
                            {
                                if (mapSource.get("InstrCode").toString().trim().equals("RECSWPOP") || mapSource.get("InstrCode").toString().trim().equals("PAYSWPOP"))
                                {
                                     DecimalFormat df2 = new DecimalFormat("#.##");
                                    //if(String.format("%.2f",getInteger(mapSource.get("StrikePrice").toString().trim())).contains(String.format("%.2f",getInteger(mapTarget.get("Strike Price").toString().trim())*100)))
                                    if(df2.format(getInteger(mapSource.get("StrikePrice").toString().trim())/100).contains(df2.format(getInteger(mapTarget.get("Strike Price").toString().trim()))))
                                    {
                                        counterPass = counterPass + 1;
                                        break;
                                    }
                                    else{
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + "StrikePrice", " |Expected: " +getInteger(mapSource.get("StrikePrice").toString().trim())/100 + " | Actual: " + mapTarget.get("Strike Price") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }

                                }

                                else if (mapSource.get("InstrCode").toString().trim().equals("PASWPCDX") || mapSource.get("InstrCode").toString().trim().equals("RESWPCDX"))
                                {
                                    break;
                                }
                               else if (Math.abs(getInteger(mapSource.get("StrikePrice").toString().trim()))==Math.abs(getInteger(mapTarget.get("Strike Price").toString().trim())))
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "StrikePrice", " |Expected: " + mapSource.get("StrikePrice") + " | Actual: " + mapTarget.get("Strike Price") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }

                            }
                            else{
                                break;
                            }


                        case "2nd leg notional"  :
                            System.out.println("2nd Leg Notional" + " " + value.toString().trim() + " " + key1 + "\n");



                            if(mapSource.get("InstrCode").toString().trim().equals("FITRSWP")&& mapSource.get("IssueCurrency").toString().trim().equals("EUR")&&mapTarget.get("Pay/Receive").toString().equals("Receive"))
                            {

                                if(mapTarget.get("2nd Leg Notional").toString().trim().equals("NAN"))
                                {
                                    break;
                                }
                               else if (Math.abs((int)Double.parseDouble(mapSource.get("Notional").toString().trim()))==Math.abs((int)Double.parseDouble(mapTarget.get("2nd Leg Notional").toString().trim()))) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "2nd Leg Notional", " |Expected: " + mapSource.get("Notional") + " | Actual: " + mapTarget.get("2nd Leg Notional") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }
                            }

                            else if (mapSource.get("InstrCode").toString().trim().equals("FITRSWP")&& mapSource.get("IssueCurrency").toString().trim().equals("USD")&&mapTarget.get("Pay/Receive").toString().equals("Pay"))
                            {

                                //if (mapSource.get("Notional").toString().trim().equals(mapTarget.get("2nd Leg Notional").toString().trim())) {
                                if(mapTarget.get("2nd Leg Notional").toString().trim().equals("NAN"))
                                {
                                    break;
                                }
                                if (Math.abs((int)Double.parseDouble(mapSource.get("Notional").toString().trim()))==Math.abs((int)Double.parseDouble(mapTarget.get("2nd Leg Notional").toString().trim()))) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "2nd Leg Notional", " |Expected: " + mapSource.get("Notional") + " | Actual: " + mapTarget.get("2nd Leg Notional") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }
                            }

                            else if (mapSource.get("InstrCode").toString().trim().equals("CURCLOPT") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CURPTOPT"))
                            {
                                Double strikePrice=getInteger(mapSource.get("StrikePrice").toString().trim());
                                Double quantity=getInteger(mapSource.get("Quantity").toString().trim());
                                Double secNotional=getInteger(mapTarget.get("2nd Leg Notional").toString().trim());
                                if(mapSource.get("LongShort").toString().trim().contains("L"))
                                    if(strikePrice*quantity==secNotional)
                                    {

                                        counterPass = counterPass + 1;
                                        break;
                                    }
                                else
                                    {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + "2nd Leg Notional", " |Expected: " + strikePrice*quantity + " | Actual: " + mapTarget.get("2nd Leg Notional") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                        break;
                                    }

                            }



                            else {
                                break;
                            }








                        case "notional 2":

                            System.out.println("Notional 2" + " " + value.toString().trim() + " " +mapTarget.get("CRDSecid")+  "\n");

                            if(mapSource.get("InstrCode").toString().trim().equals("FITRSWP")&& mapTarget.get("Pay/Receive").toString().trim().contains("Receive") )

                                if(mapTarget.get("IssueCurrency").toString().trim().equals("EUR") || mapTarget.get("IssueCurrency").toString().trim().equals("USD"))
                                {
                                    double notional=getInteger(mapTarget.get("Notional").toString().trim());
                                    double notional2=getInteger(mapTarget.get("Notional 2").toString().trim());
                                    double lastEquityPrice=getInteger(mapTarget.get("LastEqtyResetPrice").toString().trim());

                                    if(mapTarget.get("InstrCode").toString().trim().contains("EQS") || mapTarget.get("InstrCode").toString().trim().contains("TRS"))
                                    {
                                        if(Math.ceil(notional/lastEquityPrice)==Math.ceil(notional2))
                                        {
                                            counterPass = counterPass + 1;
                                            break;
                                        }
                                        else
                                        {
                                            counterFail = counterFail + 1;
                                            mapResult.put("Mismatch Column Name =" + "Notional2 for FITRSWP "+mapSource.get("IssueCurrency")+ " " , " |Expected: " + Math.ceil(notional/lastEquityPrice) + " | Actual: " + value + " |" + "\n");
                                            break;

                                        }
                                    }
                                }

                            else if (mapSource.get("InstrCode").toString().trim().equals("FITRSWP"))
                            {
                                if (mapSource.get("Notional").toString().trim().contains(mapTarget.get("Notional 2").toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Notional 2", " |Expected: " + mapSource.get("Notional") + " | Actual: " + mapTarget.get("Notional 2") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }
                            }

                            else if (mapSource.get("InstrCode").toString().trim().equals("EQTRSWP")) {
                                if (mapSource.get("Quantity").toString().trim().contains(mapTarget.get("Notional 2").toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Notional 2", " |Expected: " + mapSource.get("Quantity") + " | Actual: " + mapTarget.get("Notional 2") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }

                            } else {
                                break;
                            }


                        case "option exercise start date":

                            System.out.println("Option Exercise Start Date" + " " + value.toString().trim() + " " + key1 + "\n");
                            if (mapSource.get("InstrCode").toString().trim().equals("EQCLOPT") && mapSource.get("TransactionType").toString().trim().equals("OPTIONS") || mapSource.get("InstrCode").toString().trim().equals("EQPTOPT") && mapSource.get("TransactionType").toString().trim().equals("OPTIONS")) {
                                if (getDate(mapSource.get("MaturityDate").toString().trim(),"yyyy-MM-dd hh:mm:ss" ).contains(getDate(mapTarget.get("Option Exercise Start Date").toString().trim(),"MM/dd/yyyy"))) {
                                //    if (getDate(mapSource.get("MaturityDate").toString().trim(),"MM/dd/yyyy hh:mm" ).contains(getDate(mapTarget.get("Option Exercise Start Date").toString().trim(),"MM/dd/yyyy"))) {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Option Exercise Start Date", " |Expected: " + mapSource.get("MaturityDate") + " | Actual: " + mapTarget.get("Option Exercise Start Date") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            } else {
                                break;
                            }


                        case "instrcode":


                            System.out.println("Product Type" + " " + value.toString().trim() + " " + key1 + "\n");
                            if (mapSource.get("InstrCode").toString().trim().equals("EQCLOPT") && mapTarget.get("InstrCode").toString().trim().equals("EQO") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CRDEFSWP") && mapTarget.get("InstrCode").toString().trim().equals("CDS") ||
                                    mapSource.get("InstrCode").toString().trim().equals("INDTRSWP") && mapTarget.get("InstrCode").toString().trim().equals("CDX") ||
                                    mapSource.get("InstrCode").toString().trim().equals("EQPTOPT") && mapTarget.get("InstrCode").toString().trim().equals("EQO") ||
                                    mapSource.get("InstrCode").toString().trim().equals("EQTRSWP") && mapTarget.get("InstrCode").toString().trim().equals("EQS") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CURCLOPT") && mapTarget.get("InstrCode").toString().trim().equals("FXO") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CURPTOPT") && mapTarget.get("InstrCode").toString().trim().equals("FXO") ||
                                    mapSource.get("InstrCode").toString().trim().equals("ZCINSWP") && mapTarget.get("InstrCode").toString().trim().equals("IFS") ||
                                    mapSource.get("InstrCode").toString().trim().equals("INTRTSWP") && mapTarget.get("InstrCode").toString().trim().equals("IRS") ||
                                    mapSource.get("InstrCode").toString().trim().equals("PASWPCDX") && mapTarget.get("InstrCode").toString().trim().equals("SWOCDX") ||
                                    mapSource.get("InstrCode").toString().trim().equals("RESWPCDX") && mapTarget.get("InstrCode").toString().trim().equals("SWOCDX") ||
                                    mapSource.get("InstrCode").toString().trim().equals("FITRSWP") && mapTarget.get("InstrCode").toString().trim().equals("TRS") ||
                                    mapSource.get("InstrCode").toString().trim().equals("FITRSWP") && mapTarget.get("InstrCode").toString().trim().equals("EQS") ||
                                    mapSource.get("InstrCode").toString().trim().equals("CMBXSWP") && mapTarget.get("InstrCode").toString().trim().equals("ABX") ||
                                    mapSource.get("InstrCode").toString().trim().equals("PAYSWPOP") && mapTarget.get("InstrCode").toString().trim().equals("SWO") ||
                                    mapSource.get("InstrCode").toString().trim().equals("RECSWPOP") && mapTarget.get("InstrCode").toString().trim().equals("SWO"))  {
                                counterPass = counterPass + 1;
                                break;
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + "Product Type", " |Expected: DERMA sheet has  " + mapSource.get("InstrCode") + " | Actual: BNY sent  " + mapTarget.get("InstrCode") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;

                            }


                        case "trade date":


                            if(mapSource.get("InstrCode").toString().trim().contains("ZCINSWP")|| mapSource.get("InstrCode").toString().trim().contains("CRDEFSWP")|| mapSource.get("InstrCode").toString().trim().contains("INTRTSWP"))
                            {
                                if(mapSource.get("SecurityName").toString().trim().contains("Non Cleared Swap Sell") || mapSource.get("SecurityName").toString().trim().contains("Cleared Swap Sell") )
                                {
                                    break;
                                }
                            }
                            if(mapSource.get("InstrCode").toString().trim().contains("FITRSWP") && mapSource.get("SecurityName").toString().trim().contains("Non Cleared Swap"))

                            {
                                break;
                            }

                            if(mapSource.get("InstrCode").toString().trim().contains("EQTRSWP") && mapSource.get("TradeDate").toString().trim().equals("6/17/2019"))

                            {

                                break;
                            }

                            //if (getDate(mapSource.get("TradeDate").toString().trim(),"yyyy-MM-dd hh:mm:ss" ).contains(getDate(mapTarget.get("Trade Date").toString().trim(),"MM/dd/yyyy"))) {
                                if (getDate(mapSource.get("TradeDate").toString().trim(),"MM/dd/yyyy" ).contains(getDate(mapTarget.get("Trade Date").toString().trim(),"MM/dd/yyyy"))) {
                                counterPass = counterPass + 1;
                                break;
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + "TradeDate", " |Expected: " + mapSource.get("TradeDate") + " | Actual: " + value + " |" + "\n");
                                break;
//                                System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                            }


                        case "price":

                            System.out.println("Price" + " " + value.toString().trim() + " " + key1 + "\n");
                            if (mapSource.get("InstrCode").toString().trim().equals("EQTRSWP") || mapSource.get("InstrCode").toString().trim().equals("EQTRSWP")) {

                                if(Math.ceil(getInteger(mapSource.get("TradePrice").toString().trim())*getInteger(mapSource.get("ExchgBuyRate").toString().trim()))==Math.ceil(getInteger(mapTarget.get("LastEqtyResetPrice").toString().trim())))
                                {
                                //if (mapSource.get("TradePrice").toString().trim().contains(mapTarget.get("LastEqtyResetPrice").toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Price", " |Expected: " + (getInteger(mapSource.get("TradePrice").toString().trim())*getInteger(mapSource.get("ExchgBuyRate").toString().trim())) + " | Actual: " + mapTarget.get("LastEqtyResetPrice") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;

                                }

                            } else {
                                break;
                            }


                        case "notional":

                            // Different cases from Team.

                            if(mapSource.get("InstrCode").toString().trim().contains("ZCINSWP")|| mapSource.get("InstrCode").toString().trim().contains("CRDEFSWP")|| mapSource.get("InstrCode").toString().trim().contains("INTRTSWP"))
                            {
                                if(mapSource.get("SecurityName").toString().trim().contains("Non Cleared Swap Sell") || mapSource.get("SecurityName").toString().trim().contains("Cleared Swap Sell") )
                                {
                                    break;
                                }
                            }


                            if(mapSource.get("InstrCode").toString().trim().equals("FITRSWP")&& mapTarget.get("Pay/Receive").toString().trim().contains("Receive") )

                                if(mapTarget.get("IssueCurrency").toString().trim().equals("EUR") || mapTarget.get("IssueCurrency").toString().trim().equals("USD"))
                            {
                                double notional=getInteger(mapTarget.get("Notional").toString().trim());
                                double notional2=getInteger(mapTarget.get("Notional 2").toString().trim());
                                double lastEquityPrice=getInteger(mapTarget.get("LastEqtyResetPrice").toString().trim());

                                if(mapTarget.get("InstrCode").toString().trim().contains("EQS") || mapTarget.get("InstrCode").toString().trim().contains("TRS"))
                                    {
                                    if(Math.ceil(notional2*lastEquityPrice)==Math.ceil(notional))
                                        {
                                            counterPass = counterPass + 1;
                                            break;
                                        }
                                    else
                                        {
                                            counterFail = counterFail + 1;
                                            mapResult.put("Mismatch Column Name =" + "Notional for FITRSWP "+mapSource.get("IssueCurrency")+ " " , " |Expected: " + Math.ceil(notional2*lastEquityPrice) + " | Actual: " + value + " |" + "\n");
                                            break;

                                        }
                                    }
                            }


                            else if (mapSource.get("InstrCode").toString().trim().equals("EQTRSWP") && !mapSource.get("IssueCurrency").toString().trim().equals("USD"))
                            {
                                double exchgbuyrate=getInteger(mapSource.get("ExchgBuyRate").toString().trim());

                                double notional=getInteger(mapSource.get("Notional").toString().trim());

                                //System.out.println("TEST VALUE OF EQTRSWP"+mapTarget.get("IssueCurrency").toString().trim()+"  "+ Math.ceil((exchgbuyrate*notional))+ "   "+Math.ceil(Double.parseDouble(mapTarget.get("Notional").toString().trim()))+ "  Done"  );
                                if (Math.ceil((exchgbuyrate*notional))==Math.ceil(getInteger(mapTarget.get("Notional").toString().trim()))) {
                                    //System.out.println("Perfect Match");

                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Notional for EQTRSWP "+mapSource.get("IssueCurrency")+ " " , " |Expected: " + Math.ceil(exchgbuyrate*notional) + " | Actual: " + value + " |" + "\n");
//                                System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }

                            else if (mapSource.get("InstrCode").toString().trim().contains("CRDEFSWP")&& !mapSource.get("IssueCurrency").toString().trim().equals("USD"))
                            {
                                if (Math.abs(getInteger(mapSource.get("Quantity").toString().trim()))==Math.abs(getInteger(value.toString().trim()))) {
                                    //System.out.println("Perfect Match");

                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Notional Non USD", " |Expected: " + mapSource.get("Quantity") + " | Actual: " + value + " |" + "\n");
//                                System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }

                            else if (mapSource.get("InstrCode").toString().trim().contains("INTRTSWP")&& !mapSource.get("IssueCurrency").toString().trim().equals("USD"))
                            {  if (Math.abs(getInteger(mapSource.get("Quantity").toString().trim()))==Math.abs(getInteger(value.toString().trim()))) {
                                //System.out.println("Perfect Match");

                                counterPass = counterPass + 1;
                                break;
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + "Notional Non USD", " |Expected: " + mapSource.get("Quantity") + " | Actual: " + value + " |" + "\n");
//                                System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                            }

                            else if (mapSource.get("InstrCode").toString().trim().contains("INDTRSWP")&& !mapSource.get("IssueCurrency").toString().trim().equals("USD"))
                            {
                                if (Math.abs(getInteger(mapSource.get("Quantity").toString().trim()))==Math.abs(getInteger(value.toString().trim()))) {
                                    //System.out.println("Perfect Match");

                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Notional Non USD", " |Expected: " + mapSource.get("Quantity") + " | Actual: " + value + " |" + "\n");
//                                System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                                else if (mapSource.get("InstrCode").toString().trim().equals("CURCLOPT") ||
                                        mapSource.get("InstrCode").toString().trim().equals("CURPTOPT"))
                                {
                                    if(mapSource.get("LongShort").toString().trim().contains("S"))
                                        if(Math.abs(getInteger(mapSource.get("Quantity").toString().trim()))==Math.abs(getInteger(mapTarget.get("Notional").toString().trim())))
                                        {
                                            counterPass = counterPass + 1;
                                            break;
                                        }
                                        else
                                        {
                                            counterFail = counterFail + 1;
                                            mapResult.put("Mismatch Column Name =" + "Notional", " |Expected: " + mapSource.get("Quantity") + " | Actual: " + mapTarget.get("Notional") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                            break;
                                        }

                                }




                            else if (Math.abs(getInteger(mapSource.get("Notional").toString().trim()).intValue())==Math.abs(getInteger(value.toString().trim()).intValue())) {
                                //System.out.println("Perfect Match");
                                //Math.abs(Math.ceil(getInteger(mapSource.get("Notional").toString().trim())))==Math.abs(Math.ceil(getInteger(value.toString().trim())))

                                counterPass = counterPass + 1;
                                break;
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + "Notional", " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |" + "\n");
//                                System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;

                            }

                            // Currency Matching
                        case "issuecurrency":

                            //System.out.println("Key Used"+key1);
                            if(mapSource.get("InstrCode").toString().trim().contains("CURCLOPT"))
                            {
                                break;
                            }

                            if (mapTarget.get("IssueCurrency").toString().trim().contains(mapSource.get("IssueCurrency").toString().trim())) {
                                counterPass = counterPass + 1;
                                break;
                            } else {
                                counterFail = counterFail + 1;
                                mapResult.put("Mismatch Column Name =" + "IssueCurrency", " |Expected: " + mapSource.get("IssueCurrency") + " | Actual: " + mapTarget.get("Currency Code") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                break;
                            }

                        case "settlement currency":
                            System.out.println("Key Used"+key1);

                            if(mapSource.get("InstrCode").toString().trim().contains("CURCLOPT"))
                            {
                                if (mapTarget.get("Settlement Currency").toString().trim().contains(mapSource.get("IssueCurrency").toString().trim())) {
                                    counterPass = counterPass + 1;
                                    break;
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "IssueCurrency/Settlement Currency", " |Expected: " + mapSource.get("IssueCurrency") + " | Actual: " + mapTarget.get(key1) + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }
                            }
                            else{
                                break;
                            }



                        case "option expiration date":
                            System.out.println("Key Used"+key1);

                            if(mapSource.get("InstrCode").toString().trim().contains("PAYSWPOP") || mapSource.get("InstrCode").toString().trim().contains("RECSWPOP") )
                            {
                                if (getDate(mapSource.get("MaturityDate").toString().trim(),"yyyy-MM-dd hh:mm:ss" ).contains(getDate(mapTarget.get("Option Expiration Date").toString().trim(),"MM/dd/yyyy"))) {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "Maturity Date/Option Expiration Date", " |Expected: " + mapSource.get("MaturityDate") + " | Actual: " + mapTarget.get("Option Expiration Date") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    break;
                                }

                            }
                            else
                            {
                                break;
                            }


                        case "maturity date":

                            System.out.println("\n" + "I am in Maturity date");
                            System.out.println("\n"+mapSource.get("MaturityDate") +" "+mapSource.get("CRDSecid"));
                            System.out.println("\n"+mapTarget.get("Maturity Date")+" "+mapTarget.get("CRDSecid"));

                            if(mapSource.get("InstrCode").toString().trim().contains("PAYSWPOP") || mapSource.get("InstrCode").toString().trim().contains("RECSWPOP") )
                            {
                                break;
                            }
                             if(mapSource.get("InstrCode").toString().trim().contains("FITRSWP") && mapSource.get("SecurityName").toString().trim().contains("Non Cleared Swap"))

                            {
                                break;
                            }
                             if(mapSource.get("InstrCode").toString().trim().contains("EQTRSWP"))
                            {

                                LocalDate startDate = LocalDate.parse(getDate2(mapSource.get("MaturityDate").toString().trim(),"yyyy-MM-dd hh:mm:ss" ));
                                //LocalDate startDate = LocalDate.parse(getDate2(mapSource.get("MaturityDate").toString().trim(),"MM/dd/yyyy hh:mm" ));
                                LocalDate endDate = LocalDate.parse(getDate2(mapTarget.get("Maturity Date").toString().trim(),"MM/dd/yyyy"));

                                if(Math.abs(ChronoUnit.DAYS.between(startDate, endDate))==2)
                                {
                                    counterPass = counterPass + 1;
                                    break;
                                }
                                else
                                {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + "MaturityDate/EQTRSWP", " |Expected: " + mapSource.get("MaturityDate") + " | Actual: " + mapTarget.get("Maturity Date") + " |" + "\n");
                                    break;
                                }

                            }


                               if (getDate(mapSource.get("MaturityDate").toString().trim(),"yyyy-MM-dd hh:mm:ss" ).contains(getDate(mapTarget.get("Maturity Date").toString().trim(),"MM/dd/yyyy"))) {
                               //   if (getDate(mapSource.get("MaturityDate").toString().trim(),"MM/dd/yyyy hh:mm" ).contains(getDate(mapTarget.get("Maturity Date").toString().trim(),"MM/dd/yyyy"))) {
                                        counterPass = counterPass + 1;
                                        break;
                                    }


                               else {
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + "MaturityDate", " |Expected: " + mapSource.get("MaturityDate") + " | Actual: " + mapTarget.get("Maturity Date") + " |" + "\n");
//                               System.out.println("Data Mismatched for " + id +  "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
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

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.DAYS);
    }


    public String getDate(String srcDate, String pattern) throws Exception
    {
        if(srcDate.equals("NAN") || srcDate.equals("null"))
        {
            return "1/1/1999";
        }
        else if(srcDate.contains("/")||srcDate.contains(":")||srcDate.contains("-"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date convertedCurrentDate = sdf.parse(srcDate);
            String convertedDate = new SimpleDateFormat("dd.MM.yy").format(convertedCurrentDate);
            return convertedDate;
        }
        else
        {
            return "1/1/1999";
        }
    }


    public String getDate2(String srcDate, String pattern) throws Exception
    {
        if(srcDate.equals("NAN") || srcDate.equals("null"))
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


    public Date getDateFormat(String srcDate, String pattern) throws Exception
    {
        Date date1 = new Date();
        if(srcDate.equals("NAN") || srcDate.equals("null"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(sdf.format(date1));
        }
        else if(srcDate.contains("/")||srcDate.contains(":")||srcDate.contains("-"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date convertedCurrentDate = sdf.parse(srcDate);
            //String convertedDate = new SimpleDateFormat("dd.MM.yy").format(convertedCurrentDate);
            return convertedCurrentDate;
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(sdf.format(date1));
        }
    }




    public String newDate(String srcDate, String pattern, int numdays) throws Exception
    {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(srcDate));
        c.add(Calendar.DATE,numdays );  // number of days to add
        srcDate = sdf.format(c.getTime());
        return srcDate;
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
