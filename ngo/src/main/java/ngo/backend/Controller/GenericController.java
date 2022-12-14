package ngo.backend.Controller;

import java.sql.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class GenericController {
    @GetMapping("")
    public String get() {
        return "Hello World!";
    }

    public static Connection getConnection() throws SQLException {
        String hostName = "ongfinder.database.windows.net";
        String dbName = "ONGFinder";
        String user = "cbt";
        String password = "whatPassword?";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        return java.sql.DriverManager.getConnection(url);
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
