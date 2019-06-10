package karate;
import org.apache.commons.lang.ObjectUtils;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataCompare {
    public String compareData(ArrayList arrSource, String arrTarget,ArrayList arrMapingData,String key,LinkedHashMap transformationData,String id)throws Exception{
        String result ="";
        int passCounter=0;
        int failCounter=0;
//        HashMap<String,String> mapTran = createTransformationData(transformationData,"Customer");
//        Set keys = mapTran.keySet();
//        Iterator itr = keys.iterator();
//        String key12;
//        String value12;
//        while(itr.hasNext())
//        {
//            key12 = (String)itr.next();
//            value12 = (String)mapTran.get(key12);
//            System.out.println(key12 + " - "+ value12);
//        }
        int sourceLen = arrSource.size();
        int targetLen = arrTarget.length();
        arrTarget = arrTarget.replace("[","");
        arrTarget = arrTarget.replace("]","");
        arrTarget = arrTarget.replace("}","}@");
        arrTarget = arrTarget.replaceAll("}","");
        arrTarget = arrTarget.replaceAll("\\{","");
        arrTarget = arrTarget.replaceAll("\",","\", ");
        arrTarget = arrTarget.replaceAll("\"","");
        arrTarget = arrTarget.substring(0, arrTarget.length() - 1) + "";
        String[] arrTargetFinal =  arrTarget.split("@,");
    /*    for(String s : arrTargetFinal){
            System.out.println(">"+s+"<");
        }*/
 System.out.print("Target Data Count: "+arrTarget.length()+"mannn");
        System.out.print("Source Data Count: "+arrSource.size()+"/n");
        System.out.print("Maninde"+sourceLen+"mani");

        if(sourceLen <= arrTargetFinal.length) {
            System.out.println("Inside If statement");
            for (int i = 0; i < sourceLen; i++) {
                String actual = arrSource.get(i).toString();
                actual = actual.replaceAll("\\{","");
                actual = actual.replaceAll("}","");
                String[] actualCOlumnList = actual.split(", ");
                System.out.println("Inside If statement");
                HashMap<String,String> map = createData(actualCOlumnList,"=");
                String resTemp=  validateTarget(arrTargetFinal,map,arrMapingData,key,transformationData,id);
                result =result+"\n" +resTemp;
                if(resTemp.contains("No Mismatches for ") ){
                    passCounter= passCounter +1;
                }else{
                    failCounter = failCounter+1;
                }
                if (i==sourceLen-1){

                    result=result+"\n"+"\n"+"TOTAL "+ id +"s Validated: "+sourceLen+" | TOTAL ROWS PASSED: "+passCounter+" | TOTAL ROWS WITH MISMATCHES: "+failCounter+"|";
                }
            }
        }

        if(!result.isEmpty())
        {
            if(result.equals("PASSED")){
                return result;
            }else {
                return result;
                //throw new Exception(result);
            }
        }
        return result;
    }
    public String validateTarget(String[] arrTarget,HashMap<String,String> mapSource,ArrayList arrMapingData,String primaryKey,LinkedHashMap transformationData,String id) {
        String exception = "";
        int targetLen = arrTarget.length;
        String TradeId;
        String mismatchList = "";
        int counterPass = 0;
        int counterFail = 0;
        int keyFound = 0;
        for (int i = 0; i < targetLen; i++) {
            String[] arrTargetRow = arrTarget[i].split(", ");
            HashMap<String, String> mapTarget = createData(arrTargetRow, ":");
//            System.out.println("Validate Target Function"+ mapTarget.get(primaryKey));
            if (mapTarget.get(primaryKey).equals(mapSource.get(primaryKey))) {
                keyFound = 1;
                HashMap<String, String> mapResult = new HashMap<String, String>();
                TradeId = mapSource.get(primaryKey);
                //mapResult.put("TradeId",TradeId);
                mapResult.put("Row", String.valueOf(i + 1));
//                System.out.println("Validate Target Function");
                Set keys = mapTarget.keySet();
//                System.out.println("Array Data : "+arrMapingData.get(0));
                for (int arrItr = 0; arrItr < arrMapingData.size(); arrItr++) {
                    String actual = arrMapingData.get(arrItr).toString();
                    actual = actual.replaceAll("\\{", "");
                    actual = actual.replaceAll("}", "");
                    String[] actualCOlumnList = actual.split(", ");
                    HashMap<String, String> mapMappingData = createData(actualCOlumnList, "=");
                    Iterator itr = keys.iterator();
                    String key1;
                    String value;
                    while (itr.hasNext()) {
                        key1 = (String) itr.next();
                        value = (String) mapTarget.get(key1);
//                        System.out.println("Target : " + key1 + " Value" + value);
                        if (key1.contains(mapMappingData.get("Input_File_Field"))) {
                            HashMap<String,String> mapTran = createTransformationData(transformationData, mapMappingData.get("Output_File_Field"));



                            if (mapTran.size() <= 0) {

                                //if(mapSource.get(key1).toString().trim().contains(value.toString().trim())){

                                if (value.toString().trim().contains(mapSource.get(key1).toString().trim())) {
                                    counterPass = counterPass + 1;
                                    //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                                } else {
                                    counterFail = counterFail + 1;
                                    mapResult.put("Mismatch Column Name =" + mapMappingData.get("Output_File_Field"), " |Expected: " + mapSource.get(key1) + " | Actual: " + value + " |");
                                    System.out.println("Data Mismatched for "+id+ ": " + TradeId + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                }
                            }else {
                                Set keys2 = mapTran.keySet();
                                Iterator itr2 = keys2.iterator();
                                String key12="";
                                String value12;
                                while(itr2.hasNext())
                                {
                                    key12 = (String)itr2.next();
                                    value12 = (String)mapTran.get(key12);
                                    System.out.println(key12 + ":"+ value12);
                                    System.out.println("Key"+key12);
                                }

                                //System.out.println(value.toString().trim().equalsIgnoreCase(mapTran.get(mapSource.get(key1).toString()).toString().trim()));
//                                System.out.println("Source Key ");
                                String srcKey = mapSource.get(key1).toString();
//                                System.out.println("Source Value "+value);
//                                System.out.println("ELse " + mapTran.get('"'+srcKey+'"').replaceAll("\"",""));



                                if(srcKey.equals("NAN"))
                                {
                                    System.out.println("Maninder "+value);
                                    if(value.equals("NAN"))
                                        counterPass = counterPass + 1;
                                    else{
                                        counterFail = counterFail + 1;
                                        mapResult.put("Mismatch Column Name =" + mapMappingData.get("Output_File_Field"), " |Expected: " + "(null)" + " | Actual: " + value + " |");
                                       // System.out.println("Data Mismatched for "+id+": " + TradeId + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    }
                                }
                                else{
                                    if (value.toString().trim().contains(mapTran.get('"'+srcKey+'"').replaceAll("\"","").trim())) {
                                        counterPass = counterPass + 1;
                                        //System.out.println("Data Matched for TradeID : "+TradId+"  "+key1+" = " + mapSource.get(key1) + " Target : " + value);
                                    } else {
                                        counterFail = counterFail + 1;
                                        if(value.equals("NAN"))
                                        mapResult.put("Mismatch Column Name =" + mapMappingData.get("Output_File_Field"), " |Expected: " + mapTran.get('"'+srcKey+'"').replaceAll("\"","") + " | Actual: Blank |");
                                        else {
                                            mapResult.put("Mismatch Column Name =" + mapMappingData.get("Output_File_Field"), " |Expected: " + mapTran.get('"' + srcKey + '"').replaceAll("\"", "") + " | Actual: " + value + " |");
                                        }
                                        System.out.println("Data Mismatched for "+id+": " + TradeId + "  " + key1 + " = " + mapSource.get(key1) + " Target : " + value);
                                    }
                                }


                            }
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
                if(mapResult.size()>1) {
                    exception = exception + "\n"+ "\n" + "Mismatch at Row :" + mapResult.get("Row") + " " + "for "+id+" : "+ TradeId + " " + mismatchList;
                }


            }
        }
        if (keyFound == 0) {
            System.out.println("Key Not found in Target Sheet");
            String expRes = "\n" +id+" :" + mapSource.get(primaryKey) + " not found in Target sheet. RESULT: FAILED";
            exception = expRes;
            return exception;
        }
        if (counterPass != arrMapingData.size()) {
            return exception;
        }else{
            exception = "\n" +"No Mismatches for  "+id+":" + mapSource.get(primaryKey)+" RESULT: PASSED";
            return exception;
        }
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

}


