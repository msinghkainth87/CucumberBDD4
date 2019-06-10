package seleniumutils.methods;
import java.util.List;
import java.util.Map;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class DbUtil {
    private final JdbcTemplate  jdbc;

    public DbUtil(Map<String, Object> config) throws SQLException {
         String url = (String) config.get("url");
         String driver = (String) config.get("driverClassName");
         String db = (String) config.get("db");
         DriverManagerDataSource dataSource = new DriverManagerDataSource();
         switch (db.toLowerCase()){
             case "access":
                 try {
                     //Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                     Class.forName(driver);
                 }
                 catch(ClassNotFoundException cnfex) {
                     System.out.println("Problem in loading"
                             + " MS Access JDBC driver");
                     cnfex.printStackTrace();
                 }
                 //String msAccDB =  "C:\\Users\\vs197hg\\Desktop\\Database1.accdb";
                 String msAccDB = url;
                 url = "jdbc:ucanaccess://"
                         + msAccDB;
                 Connection connection = null;
                 connection = DriverManager.getConnection(url);
                 Statement statement = connection.createStatement();
             case "sql":
                 String username = (String) config.get("username");
                 String password = (String) config.get("password");
                 Connection conn = null;
                 try
                 {
                     Class.forName(driver);//.newInstance();
                     conn = DriverManager.getConnection(url,username,password);
                     Statement stmt = conn.createStatement();
                     conn.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
         }
         dataSource.setDriverClassName(driver);
         dataSource.setUrl(url);
        // dataSource.setUsername(username);
         //dataSource.setPassword(password);
         jdbc = new JdbcTemplate(dataSource);

    }

    public Object readValue(String query) {
        return jdbc.queryForObject(query, Object.class);
    }

    public Map<String, Object> readRow(String query) {
        return jdbc.queryForMap(query);
    }

    public List<Map<String, Object>> readRows(String query) {

        return jdbc.queryForList(query);
    }
}