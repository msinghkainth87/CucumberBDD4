package seleniumutils.methods.helpers;

import org.openqa.selenium.Keys;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperUtils {
    public static Keys identifyKey(String key){
        Keys k=Keys.NULL;
        switch(key.toUpperCase()){
            case "NULL":
                k = Keys.NULL;
            	break;
            case "CANCEL":
                k = Keys.CANCEL;
            	break;
            case "HELP":
                k = Keys.HELP;
            	break;
            case "BACK_SPACE":
                k = Keys.BACK_SPACE;
            	break;
            case "TAB":
                k = Keys.TAB;
            	break;
            case "CLEAR":
                k = Keys.CLEAR;
            	break;
            case "RETURN":
                k = Keys.RETURN;
            	break;
            case "ENTER":
                k = Keys.ENTER;
            	break;
            case "SHIFT":
                k = Keys.SHIFT;
            	break;
            case "LEFT_SHIFT":
                k = Keys.LEFT_SHIFT;
            	break;
            case "CONTROL":
                k = Keys.CONTROL;
            	break;
            case "LEFT_CONTROL":
                k = Keys.LEFT_CONTROL;
            	break;
            case "ALT":
                k = Keys.ALT;
            	break;
            case "LEFT_ALT":
                k = Keys.LEFT_ALT;
            	break;
            case "PAUSE":
                k = Keys.PAUSE;
            	break;
            case "ESCAPE":
                k = Keys.ESCAPE;
            	break;
            case "SPACE":
                k = Keys.SPACE;
            	break;
            case "PAGE_UP":
                k = Keys.PAGE_UP;
            	break;
            case "PAGE_DOWN":
                k = Keys.PAGE_DOWN;
            	break;
            case "END":
                k = Keys.END;
            	break;
            case "HOME":
                k = Keys.HOME;
            	break;
            case "LEFT":
                k = Keys.LEFT;
            	break;
            case "ARROW_LEFT":
                k = Keys.ARROW_LEFT;
            	break;
            case "UP":
                k = Keys.UP;
            	break;
            case "ARROW_UP":
                k = Keys.ARROW_UP;
            	break;
            case "RIGHT":
                k = Keys.RIGHT;
            	break;
            case "ARROW_RIGHT":
                k = Keys.ARROW_RIGHT;
            	break;
            case "DOWN":
                k = Keys.DOWN;
            	break;
            case "ARROW_DOWN":
                k = Keys.ARROW_DOWN;
            	break;
            case "INSERT":
                k = Keys.INSERT;
            	break;
            case "DELETE":
                k = Keys.DELETE;
            	break;
            case "SEMICOLON":
                k = Keys.SEMICOLON;
            	break;
            case "EQUALS":
                k = Keys.EQUALS;
            	break;
            case "NUMPAD0":
                k = Keys.NUMPAD0;
                break;
            case "NUMPAD1":
                k = Keys.NUMPAD1;
                break;
            case "NUMPAD2":
                k = Keys.NUMPAD2;
                break;
            case "NUMPAD3":
                k = Keys.NUMPAD3;
                break;
            case "NUMPAD4":
                k = Keys.NUMPAD4;
                break;
            case "NUMPAD5":
                k = Keys.NUMPAD5;
                break;
            case "NUMPAD6":
                k = Keys.NUMPAD6;
                break;
            case "NUMPAD7":
                k = Keys.NUMPAD7;
                break;
            case "NUMPAD8":
                k = Keys.NUMPAD8;
                break;
            case "NUMPAD9":
                k = Keys.NUMPAD9;
                break;
            case "MULTIPLY":
                k = Keys.MULTIPLY;
                break;
            case "ADD":
                k = Keys.ADD;
                break;
            case "SEPARATOR":
                k = Keys.SEPARATOR;
                break;
            case "SUBTRACT":
                k = Keys.SUBTRACT;
                break;
            case "DECIMAL":
                k = Keys.DECIMAL;
                break;
            case "DIVIDE":
                k = Keys.DIVIDE;
                break;
            case "F1":
                k = Keys.F1;
                break;
            case "F2":
                k = Keys.F2;
                break;
            case "F3":
                k = Keys.F3;
                break;
            case "F4":
                k = Keys.F4;
                break;
            case "F5":
                k = Keys.F5;
                break;
            case "F6":
                k = Keys.F6;
                break;
            case "F7":
                k = Keys.F7;
                break;
            case "F8":
                k = Keys.F8;
                break;
            case "F9":
                k = Keys.F9;
                break;
            case "F10":
                k = Keys.F10;
                break;
            case "F11":
                k = Keys.F11;
                break;
            case "F12":
                k = Keys.F12;
                break;
        }
        return k;
    }

    public static StringBuffer removeUTFCharacters(String data){
        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf;
    }
    public static String stringFetch(String string,String regex){
        String mydata = string;
        String result=null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mydata);
        if (matcher.find())
        {
            result =(matcher.group(1));
        }
        return result;
    }

}
