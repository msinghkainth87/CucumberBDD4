package seleniumutils.methods.helpers;
import java.io.*;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Gajendra Mahadevan
 */

public class JSONFileWriter {

    @SuppressWarnings("unchecked")
    public static void JSONWrite(String jsonObject,String path, String filename){

        // try-with-resources statement based on post comment below :)
        try (FileWriter file = new FileWriter(new File(path,filename))) {
            file.write(jsonObject);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static JSONObject JSONRead(String path,String filename) {
        // try-with-resources statement based on post comment below :)
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader((new File(path,filename))));
            jsonObject = (JSONObject) obj;

        } catch (FileNotFoundException e) {
            jsonObject = new JSONObject(new HashMap());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

