package ngo.backend.Controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import ngo.backend.Cryptography.deEncrypt;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    @GetMapping("")
    public String get() {
        return "Hello World!";
    }

    public static String readConfig() {
        try {
            String config = new String(Files.readAllBytes(Paths.get("config.json")));
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection() throws SQLException {

        //Parse the json string to get the details
        String data = readConfig();
        String[] split = data.split(":");
        String user = split[1].substring(1, split[1].length() - 2);
        String encPass = split[2].substring(1, split[2].length() - 2);
        String key = split[3].substring(1, split[3].length() - 2);
        String hostName = split[4].substring(1, split[4].length() - 2);
        String dbName = split[5].substring(1, split[5].length() - 2);

        //String hostName = "ongfinder.database.windows.net";
        //String dbName = "ONGFinder";
        //String user = "cbt";
        String password = deEncrypt.decrypt(encPass, key);
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        return java.sql.DriverManager.getConnection(url);
    }

    @GetMapping("/encryptTest")
    public String encTest(){
        String input = "Hello World!";
        String password = "1987";
        String encrypted = deEncrypt.encrypt(input, password);
        String decrypted = deEncrypt.decrypt(encrypted, password);
        return "Input: " + input + "\n" +
                "Password: " + password + "\n" +
                "Encrypted: " + encrypted + "\n" +
                "Decrypted: " + decrypted;
    }

    @GetMapping("/list")
    public String list() {
        Connection conn = null;
        String result = "";
        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                String myQuerry = "SELECT * FROM emp";
                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery(myQuerry);
                    result += "[\n";
                    while (rs.next()) {
                        result += "\t{\n" +
                                "\t\t\"empno\": " + rs.getInt("empno") + ",\n" +
                                "\t\t\"ename\": \"" + rs.getString("ename") + "\",\n" +
                                "\t\t\"job\": \"" + rs.getString("job") + "\",\n" +
                                "\t\t\"mgr\": " + rs.getInt("mgr") + ",\n" +
                                "\t\t\"hiredate\": \"" + rs.getDate("hiredate") + "\",\n" +
                                "\t\t\"sal\": " + rs.getInt("sal") + ",\n" +
                                "\t\t\"comm\": " + rs.getInt("comm") + ",\n" +
                                "\t\t\"deptno\": " + rs.getInt("deptno") + "\n" +
                                "\t}" + ",\n";
                    }
                } catch (SQLException e) {
                    return e.getMessage();
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            return e.getMessage();
                        }
                    }
                } 
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.getMessage();
                }
            }
        }
        return result;
    }
}
