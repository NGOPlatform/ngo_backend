package ngo.backend.Controller;

import java.sql.*;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ngo.backend.Cryptography.deEncrypt;
import ngo.backend.Model.Config;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    @GetMapping("")
    public String get() {
        return "Hello World!";
    }

    public static Config readCFG() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Resource resource = new ClassPathResource("config.json");
            File file = resource.getFile();
            Config cfg = mapper.readValue(file, Config.class);
            return cfg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection() throws SQLException {
        Config cfg = readCFG();
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", cfg.getHostName(), cfg.getDbName(), cfg.getUser(), deEncrypt.decrypt(cfg.getPassword(), cfg.getKey()));
        return java.sql.DriverManager.getConnection(url);
    }

    @GetMapping("/connTest")
    public String connTest() {
        Connection conn = null;
        try {
            conn = getConnection();
            return "Connection Successful!";
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    return e.getMessage();
                }
            }
        }
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
                String mySelect = "SELECT * FROM emp";
                String myWhere = " WHERE job LIKE '%MAN%'";
                String myQuerry = mySelect + myWhere;
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
        return result.substring(0, result.length()-2) + "\n]";
    }
}
