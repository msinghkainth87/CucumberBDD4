package seleniumutils.methods.helpers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.util.ParseTools;

/**
 * Class is responsible to showcase MVEL template usage for different use case
 */
public class MVELTemplateController {

    public MVELTemplateController() {
    }

    /**
     * Evaluate dynamic property expression inside a string.
     * e.g. "<html>Hello @if{userName!=null && userName!=''}@{userName}@else{}Guest@end{}! Welcome to MVEL tutorial<html>"; where userName is fed from external source
     * @param message  the template message
     * @param inputMap the input map containing the template key and its value e.g. [userName: "Gajendra"], [userName: ""]
     * @return the string (e.g. Output1: "<html>Hello Gajendra! Welcome to MVEL tutorial<html>", Output2: "<html>Hello Guest! Welcome to MVEL tutorial<html>")
     */
    public String evaluatePropertyExpression(String message,Map<String,Object>inputMap){
        return this.applyTemplate(message, inputMap);
    }

    /**
     * Evaluate dynamic boolean expression inside a string.
     * "<variable><BooleanOP><regex>"; where variable is a key from inputMap whose value is being boolean evaluated against a regexp
     * e.g. inputMap[num:5]
     *      MVEL.evalToBoolean("num > 0", inputMap)
     *
     * @param booleanExpression  the template message("num>0")
     * @param inputMap the input map containing the template key and its value e.g. inputMap[num:5]
     * @return the string (e.g. Output1: true")
     */
    public String evaluateBooleanExpression(String booleanExpression,Map<String,Object>inputMap){
        return MVEL.evalToBoolean(booleanExpression, inputMap).toString();
    }

    public String evaluateDynamicMethod(){

        return "";
    }


    /**
     * Evaluate dynamic methods stored in external configuration files.
     * "<variable><BooleanOP><regex>"; where variable is a key from inputMap whose value is being boolean evaluated against a regexp
     * e.g.
     *  concat.mvel
     *    def stringcon(input){
     *      return ($.firstName+$.lastName in input);
     *      }
     *    stringcon(employeeList);
     *   String output = MVEL.eval(new String(ParseTools.loadFromFile(new File("concat.mvel"))), new HashMap()).toString();
     *   employeeList= [firstname: "Gajendra ", lastname:"Mahadevan"]
     * @param filepath the filepath
     * @return the string(e.g. output="Gajendra Mahadevan")
     */
    public String evaluateMethodsFromFiles(String filepath){
        String returnText=null;
        try{
            char[] x= ParseTools.loadFromFile(new File(filepath));
            String evalText=new String(x);
            returnText= evaluateMVEL(evalText, new HashMap<String,Object>());
        }catch(IOException e){
            e.printStackTrace();
        }
        return returnText;
    }

    /**
     * Evaluate mvel string.
     *
     * @param message  the template message containing MVEL dynamic code
     * @param inputMap the input map containing the template key and its value that is to be injected. If empty, the message should provide inputMap
     * @return the string
     */
    public String evaluateMVEL(String message,Map<String,Object> inputMap){
        MVEL.eval(message, inputMap).toString();
        return "";
    }

    /**
     * Evaluate dynamic property expression inside a string.
     * e.g. "<html>Hello validateUser()! Welcome to MVEL tutorial<html>"; where validateUser() is a java method
     *
     * public static String validateUser(){
     *     String returnValue=null;
     *     if(userName!=null && userName!='')
     *      returnValue= userName
     *     else
     *      returnValue= "Guest"
     *     return returnValue;
     * }
     *
     * @param message  the template message
     * @param inputMap the input map containing the template key and its value e.g. [userName: "Gajendra"], [userName: ""]
     * @return the string (e.g. Output1: "<html>Hello Gajendra! Welcome to MVEL tutorial<html>", Output2: "<html>Hello Guest! Welcome to MVEL tutorial<html>")
     */
    public String evaluateTemplate(String message,Map<String,Object>inputMap){
        return this.applyTemplate(message, inputMap);
    }

    /**
     * Dynamic query builder string.
     *
     * @param query    the template query (e.g. "select * from @{schemaName}.@{tableName} where @{condition}")
     * @param queryMap the query map [schema: "orgDB",table: "employee",condition:"salary > 2500 && salary < 3000"]
     * @param commands      the sql command to build the query(e.g. SELECT,UPDATE,CREATE
     * @return the string
     */
    public String dynamicQueryBuilder(String query,Map<String,Object> queryMap,String commands){
        String schema, table, condition,preparedQuery;
        switch(commands.toUpperCase()){
            case "UPDATE":
            case "DELETE":
            case "INSERT INTO":
            case "CREATE DATABASE":
            case "ALTER DATABASE":
            case "CREATE TABLE":
            case "ALTER TABLE":
            case "DROP TABLE" :
            case "CREATE INDEX" :
            case "DROP INDEX" :
            case "SELECT":
            default:
                preparedQuery=evaluateMVEL(query, queryMap);
                break;

        }

        return preparedQuery;
    }

    public void main(String args[]) {
        MVELTemplateController controller = new MVELTemplateController();

        // Usecase1: Injecting the dynamic property to the static HTML content.
        // MVEL supports the decision making tags to place the default values in case of the actual property value is null
        // Input map should contain the key name otherwise it will throw the exception
        System.out.println("***** Usecase1: Injecting the dynamic property Started *****");
        String message = "<html>Hello @if{userName!=null && userName!=''}@{userName}@else{}Guest@end{}! Welcome to MVEL tutorial<html>";
        System.out.println("Input Expression:" + message);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("userName", "Blog Visitor");
        System.out.println("InputMap:" + inputMap);
        String compliedMessage = controller.applyTemplate(message, inputMap);
        System.out.println("compliedMessage:" + compliedMessage);
        System.out.println("***** Usecase1: Injecting the dynamic property Ended  *****\n");

        // Usecase2: Loading the MVEL expression from the configuration or property file
        // MVEL library have build-in utility to load the expression from the file input.
        // Usually the mvel script,template will be save with .mvel extension
        try {
            String templateExpression = new String(ParseTools.loadFromFile(new File("input/declaretemplate.mvel")));
            System.out.println("templateExpression:" + templateExpression + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Usecase3: Accessing the java class methods from the MVEL template to return the output
        System.out.println("***** Usecase3: Accessing static method Started *****");
        String methodExpression = "@if{com.main.MVELTemplateController.validateInput(userName)}Valid Input@else{} Invalid Input@end{}";
        String validateExpression = controller.applyTemplate(methodExpression, inputMap);
        System.out.println("validateExpression:" + validateExpression);
        System.out.println("***** Usecase3: Accessing static method Ended *****\n");

        // Usecase4: Forming dynamic query by binding the dynamic values
        // We can build complex queries by using the decision making tags@if,@else and for loop tags @for
        // We can bind the values from the bean to expression
        System.out.println("***** Usecase4: Forming dynamic Query Started *****");
        String queryExpression = "select * from @{schemaName}.@{tableName} where @{condition}";
        Map<String, Object> queryInput = new HashMap<String, Object>();
        queryInput.put("schemaName", "testDB");
        queryInput.put("tableName", "employee");
        queryInput.put("condition", "age > 25 && age < 30");
        String query = controller.applyTemplate(queryExpression, queryInput);
        System.out.println("Dynamic Query:" + query);
        System.out.println("***** Usecase4: Forming dynamic Query Ended*****\n");

        // Usecase5: Forming dynamic API calls
        System.out.println("***** Usecase5: Forming dynamic API calls Started *****");
        String weatherAPI = "http://api.openweathermap.org/data/2.5/weather?lat=@{latitude}&lon=@{longitude}";
        Map<String, Object> apiInput = new HashMap<String, Object>();
        apiInput.put("latitude", "35");
        apiInput.put("longitude", "139");
        String weatherAPICall = controller.applyTemplate(weatherAPI, apiInput);
        System.out.println("weatherAPICall:" + weatherAPICall);
        System.out.println("***** Usecase5: Forming dynamic API calls Ended *****\n");
    }

    /**
     * Method used to bind the values to the MVEL syntax and return the complete expression to understand by any other engine.
     *
     * @param expression
     * @param parameterMap
     * @return
     *         Jun 19, 2015
     */
    public String applyTemplate(String expression, Map<String, Object> parameterMap) {
        String executeExpression = null;

        if (expression != null && (parameterMap != null && !parameterMap.isEmpty())) {
            // compile the mvel expression
            CompiledTemplate compliedTemplate = TemplateCompiler.compileTemplate(expression);
            // bind the values in the Map input to the expression string.
            executeExpression = (String) TemplateRuntime.execute(compliedTemplate, parameterMap);
        }

        return executeExpression;
    }

    /**
     * Method used to validate the input
     *
     * @param input
     * @return
     *         Jun 19, 2015
     */
    public boolean validateInput(String input) {
        boolean isValid = false;

        if (input != null && input.equals("example")) {
            isValid = true;
        }

        return isValid;
    }
}
