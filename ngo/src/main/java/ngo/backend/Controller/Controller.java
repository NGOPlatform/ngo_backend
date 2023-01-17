package ngo.backend.Controller;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.io.File;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ngo.backend.Cryptography.deEncrypt;
import ngo.backend.Model.Config;
import ngo.backend.Model.ngo;

@RestController
@RequestMapping("ongAPI")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    @GetMapping("")
    public String get() {
        return "API is running!";
    }

    public static Config readCFG() { // read config.json to get database connection info
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

    public static Connection getConnection() throws SQLException { // get connection to database
        Config cfg = readCFG();
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", cfg.getHostName(), cfg.getDbName(), cfg.getUser(), deEncrypt.decrypt(cfg.getPassword(), cfg.getKey()));
        return java.sql.DriverManager.getConnection(url);
    }

    @GetMapping("/1987")
    public String easterEgg1(){
        return "Is that the bite of '87?!";
    }

    @GetMapping("/listONG")
    public String listONG(@RequestParam(value = "size", defaultValue = "0") int size,
                            @RequestParam(value = "skip", defaultValue = "0") int skip,
                            @RequestParam(value = "city", defaultValue = "") String city,
                            @RequestParam(value = "county", defaultValue = "") String county,
                            @RequestParam(value = "tag", defaultValue = "") String tags) {
        Connection conn = null;
        String result = "";
        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {

                String mySelect = "SELECT * FROM dbo.ONG";

                String myWhere;

                //Check if all search parameters are empty
                if (city.equals("") && county.equals("") && tags.equals(""))
                    myWhere = "";
                else
                    myWhere = " WHERE ";

                //Add search parameters to where clause
                if (!city.equals("")) //If city is not empty
                    myWhere += "localitate LIKE '%" + city + "%'";
                if (!county.equals("")) { //If county is not empty
                    if (!city.equals("")) //If city is not empty add AND
                        myWhere += " AND ";
                    myWhere += "judet LIKE '%" + county + "%'";
                }
                if (!tags.equals("")) { //If tags is not empty
                    if (!city.equals("") || !county.equals("")) //If city or county is not empty add AND
                        myWhere += " AND ";

                    //Split tags by comma
                    List<String> tagsList = Arrays.asList(tags.split(","));

                    //Trim whitespace in tagsList
                    for (int i = 0; i < tagsList.size(); i++) {
                        tagsList.set(i, tagsList.get(i).trim());
                    }

                    if(tagsList.size() == 1) //If there is only one tag
                        myWhere += "descriere LIKE '%" + tagsList.get(0) + "%'";
                    else {  //If there are multiple tags add OR to list everything that matches
                        myWhere += "(descriere LIKE '%" + tagsList.get(0) + "%'";
                        for (int i = 1; i < tagsList.size(); i++) {
                            myWhere += " OR descriere LIKE '%" + tagsList.get(i) + "%'";
                        }
                        myWhere += ")";
                    }
                }

                String myOrder = " ORDER BY id ASC"; // Order by internal ID

                String mySkip; // Skip first x rows
                if (skip == 0)
                    mySkip = " OFFSET 0 ROWS";
                else
                    mySkip = " OFFSET " + skip + " ROWS";

                String mySize;  // Fetch x rows or all rows; Works in conjunction with mySkip
                if (size == 0)  // If size is 0 fetch all rows
                    mySize = ";";
                else            // Otherwise fetch next x rows
                    mySize = " FETCH NEXT " + size + " ROWS ONLY;";
                String myQuerry = mySelect + myWhere + myOrder + mySkip + mySize;
                System.out.println(myQuerry);   //Debug
                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery(myQuerry);
                    result += "[\n";    //Iterate through all rows and build a JSON set response
                    while (rs.next()) {
                        ngo ong = new ngo(rs.getInt("ID"),
                                            rs.getString("denumire"),
                                            rs.getString("nr_inreg"),
                                            rs.getString("judet"),
                                            rs.getString("localitate"),
                                            rs.getString("adresa"),
                                            rs.getString("descriere"),
                                            rs.getString("email"));
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                            String json = mapper.writeValueAsString(ong);
                            result += json + ",\n";
                        } catch (JsonProcessingException e) {
                            return e.getMessage();
                        }
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
        if (result.equals("[\n") || result.equals("")) //If there are no results return empty set
            return "[]";
        else //Otherwise return the set
        return result.substring(0, result.length()-2) + "\n]";
    }

    @GetMapping("/listONGNOU")
    public String listONGNOU(@RequestParam(value = "size", defaultValue = "0") int size,
                            @RequestParam(value = "skip", defaultValue = "0") int skip,
                            @RequestParam(value = "city", defaultValue = "") String city,
                            @RequestParam(value = "county", defaultValue = "") String county,
                            @RequestParam(value = "tag", defaultValue = "") String tags) {
        Connection conn = null;
        String result = "";
        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {

                String mySelect = "SELECT * FROM dbo.ONG";

                String myWhere;

                //Check if all search parameters are empty
                if (city.equals("") && county.equals("") && tags.equals(""))
                    myWhere = "";
                else
                    myWhere = " WHERE ";

                //Add search parameters to where clause
                if (!city.equals("")) //If city is not empty
                    myWhere += "localitate LIKE '%" + city + "%'";
                if (!county.equals("")) { //If county is not empty
                    if (!city.equals("")) //If city is not empty add AND
                        myWhere += " AND ";
                    myWhere += "judet LIKE '%" + county + "%'";
                }
                if (!tags.equals("")) { //If tags is not empty
                    if (!city.equals("") || !county.equals("")) //If city or county is not empty add AND
                        myWhere += " AND ";

                    //Split tags by comma
                    List<String> tagsList = Arrays.asList(tags.split(","));

                    //Trim whitespace in tagsList
                    for (int i = 0; i < tagsList.size(); i++) {
                        tagsList.set(i, tagsList.get(i).trim());
                    }

                    if(tagsList.size() == 1) //If there is only one tag
                        myWhere += "descriere LIKE '%" + tagsList.get(0) + "%'";
                    else {  //If there are multiple tags add OR to list everything that matches
                        myWhere += "(descriere LIKE '%" + tagsList.get(0) + "%'";
                        for (int i = 1; i < tagsList.size(); i++) {
                            myWhere += " OR descriere LIKE '%" + tagsList.get(i) + "%'";
                        }
                        myWhere += ")";
                    }
                }

                String myOrder = " ORDER BY id ASC"; // Order by internal ID

                String mySkip; // Skip first x rows
                if (skip == 0)
                    mySkip = " OFFSET 0 ROWS";
                else
                    mySkip = " OFFSET " + skip + " ROWS";

                String mySize;  // Fetch x rows or all rows; Works in conjunction with mySkip
                if (size == 0)  // If size is 0 fetch all rows
                    mySize = ";";
                else            // Otherwise fetch next x rows
                    mySize = " FETCH NEXT " + size + " ROWS ONLY;";
                String myQuerry = mySelect + myWhere + myOrder + mySkip + mySize;
                System.out.println(myQuerry);   //Debug
                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery(myQuerry);
                    result += "[\n";    //Iterate through all rows and build a JSON set response
                    while (rs.next()) {
                        ngo ong = new ngo(rs.getInt("ID"),
                                            rs.getString("denumire"),
                                            rs.getString("nr_inreg"),
                                            rs.getString("judet"),
                                            rs.getString("localitate"),
                                            rs.getString("adresa"),
                                            rs.getString("descriere"),
                                            rs.getString("email"));
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                            String json = mapper.writeValueAsString(ong);
                            result += json + ",\n";
                        } catch (JsonProcessingException e) {
                            return e.getMessage();
                        }
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
                    return e.getMessage();
                }
            }
        }
        if (result.equals("[\n") || result.equals("")) //If there are no results return empty set
            return "[]";
        else //Otherwise return the set
        return result.substring(0, result.length()-2) + "\n]";
    }

    @PutMapping("/addUser")
    public String addUser(@RequestParam(value = "username", defaultValue = "") String username,
                            @RequestParam(value = "password", defaultValue = "") String password,
                            @RequestParam(value = "email", defaultValue = "") String email) {
        Connection conn = null;

        //Check if any of the parameters are empty
        if (username.equals("") && password.equals("") && email.equals("")) {
            return "{\"error\": \"Username, password and email are empty\"}";
        }
        
        if (username.equals("")) {
            if (password.equals(""))                        
                return "{\"error\": \"Username and password are empty\"}";
            else if (email.equals(""))
                return "{\"error\": \"Username and email are empty\"}";
            else
                return "{\"error\": \"Username is empty\"}";
        }

        if (password.equals("")) {
            if (email.equals(""))
                return "{\"error\": \"Password and email are empty\"}";
            else
                return "{\"error\": \"Password is empty\"}";
        }

        if (email.equals("")) {
            return "{\"error\": \"Email is empty\"}";
        }

        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                
                String myInsert = "INSERT INTO dbo.USERS";

                String myData = "(username, password, email, isAdmin)";

                String myValues = " VALUES ('" + username + "', '" + password + "', '" + email + "', 0)";

                String myQuerry = myInsert + myData + myValues;
                System.out.println(myQuerry);   //Debug

                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(myQuerry);
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
                    return e.getMessage();
                }
            }
        }
        return "{\"success\": \"User added\"}";
    }

    @GetMapping("/getUser")
    public String getUser(@RequestParam(value = "username", defaultValue = "") String username,
                            @RequestParam(value = "password", defaultValue = "") String password) {
        Connection conn = null;
        String result = "";
        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {

                String mySelect = "SELECT * FROM dbo.USERS";

                String myWhere;

                if (username.equals("") || password.equals(""))
                    return "{\"error\": \"Username or password is empty\"}";
                else
                    myWhere = " WHERE username = '" + username + "' AND password = '" + password + "'";

                String myOrder = " ORDER BY id ASC;"; // Order by internal ID

                String myQuerry = mySelect + myWhere + myOrder;
                System.out.println(myQuerry);   //Debug
                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery(myQuerry);
                    result += "[\n";    //Iterate through all rows and build a JSON set response
                    while (rs.next()) {
                        result += "\t{\n" +
                                "\t\t\"username\": \"" + rs.getString("USERNAME") + "\",\n" +
                                "\t\t\"password\": \"" + rs.getString("PASSWORD") + "\",\n" +
                                "\t\t\"email\": \"" + rs.getString("EMAIL") + "\",\n" +
                                "\t\t\"isAdmin\": \"" + rs.getBoolean("ISADMIN") + "\",\n" +
                                "\t\t\"subscriptions\": \"" + rs.getString("SUBSCRIPTIONS") + "\",\n" +
                                "\t\t\"favorites\": \"" + rs.getString("FAVORITES") + "\",\n" +
                                "\t\t\"notifications\": \"" + rs.getString("NOTIFICATIONS") + "\"\n" +
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
        if (result.equals("[\n") || result.equals("")) //If there are no results return empty set
            return "[]";
        else //Otherwise return the set
        return result.substring(0, result.length()-2) + "\n]";
    }

    @PutMapping("/updateUserSub")
    public String updUsrSub(@RequestParam(value = "subscriptions", defaultValue = "") String subscriptions,
                            @RequestParam(value = "username", defaultValue = "") String username,
                            @RequestParam(value = "password", defaultValue = "") String password) {
        
        if (subscriptions.equals(""))
            return "{\"error\": \"Subscriptions is empty\"}";
        else if (username.equals("") || password.equals(""))
                return "{\"error\": \"Username or password is empty\"}";

        Connection conn = null;

        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                String myUpdate = "UPDATE dbo.USERS";

                String mySet = " SET subscriptions = '" + subscriptions + "'";
                
                String myWhere = " WHERE username = '" + username + "' AND password = '" + password + "'";

                String myQuerry = myUpdate + mySet + myWhere;

                System.out.println(myQuerry);   //Debug

                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(myQuerry);
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
                    return e.getMessage();
                }
            }
        }
        return "{\"success\": \"Subscriptions updated\"}";
    }

    @PutMapping("/updateUserFav")
    public String updUsrFav(@RequestParam(value = "favorites", defaultValue = "") String favorites,
                            @RequestParam(value = "username", defaultValue = "") String username,
                            @RequestParam(value = "password", defaultValue = "") String password) {
        
        if (favorites.equals(""))
            return "{\"error\": \"Favorites is empty\"}";
        else if (username.equals("") || password.equals(""))
                return "{\"error\": \"Username or password is empty\"}";
            
        Connection conn = null;

        try {
            conn = getConnection();
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                String myUpdate = "UPDATE dbo.USERS";

                String mySet = " SET favorites = '" + favorites + "'";
                
                String myWhere = " WHERE username = '" + username + "' AND password = '" + password + "'";

                String myQuerry = myUpdate + mySet + myWhere;

                System.out.println(myQuerry);   //Debug

                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(myQuerry);
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
                    return e.getMessage();
                }
            }
        }
        return "{\"success\": \"Favorites updated\"}";
    }
}
    