package karate;

import java.util.ArrayList;

public class JsonCompare {
    public void CompareJSON(ArrayList jsonString1, ArrayList jsonString2,String column1, String column2) throws Exception {
        System.out.println("This is JSON compare");
        int len1 = jsonString1.size();
        int len2 = jsonString1.size();
        String exception = "";

        if(len1<=len2)
        {
            String actualColumnValuePair="";
            String expectedColumnValuePair="";
            String actualColumnValue="";
            String expectedColumnValue="";
            for(int i = 0; i < len1; i++)
            {
                String actual = jsonString1.get(i).toString().replaceAll("\\{","").replaceAll("}","");
                String[] actualCOlumnList = actual.split(",");
                Boolean found=false;
                for (int k=0;k<actualCOlumnList.length;k++)
                {
//                    System.out.println(actualCOlumnList[k]);
                    if(actualCOlumnList[k].contains(column1))
                    {   found=true;
                         actualColumnValuePair=actualCOlumnList[k];
                        actualColumnValue=actualColumnValuePair.split("=")[1];
                        break;

                    }

                }
                if(!found)
                {
                    throw new Exception("No Column found with name "+ column1);
                }

                String expected = jsonString2.get(i).toString().replaceAll("\\{","").replaceAll("}","");
                String[] expectedColumnList = expected.split(",");
                Boolean found1=false;
                for (int k=0;k<expectedColumnList.length;k++)
                {
                    if(expectedColumnList[k].contains(column2))
                    {   found1=true;
                         expectedColumnValuePair=expectedColumnList[k];
                        expectedColumnValue=expectedColumnValuePair.split("=")[1];
                        break;

                    }
                }
                if(!found1)
                {
                    throw new Exception("No Column found with name "+ column2);
                }

                System.out.println(actualColumnValuePair);
                System.out.println(expectedColumnValuePair);
                if(!actualColumnValue.equals(expectedColumnValue))
                {
                    if(exception.isEmpty())
                    {
                        exception="\n Difference at row: " + (i+1) +" : Expected : " +expectedColumnValuePair + " Actual : " + actualColumnValuePair;
                    }
                    else
                    {
                        exception= exception + "\n Difference at row: " + (i+1) +" : Expected : " +expectedColumnValuePair + " Actual : " + actualColumnValuePair;
                    }
                }
            }
        }
        else {

        }
        if(!exception.isEmpty())
        {
            throw new Exception(exception);
        }
    }
//        throw new Exception("json comparison failed");
}
