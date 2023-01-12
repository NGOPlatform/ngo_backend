package ngo.backend.Controller;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ngo.backend.Cryptography.deEncrypt;
import ngo.backend.Model.Config;

@RestController
@RequestMapping("ongAPI")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    @GetMapping("")
    public String get() {
        return "API is running!";
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
                    mySkip = "";
                else
                    mySkip = " OFFSET " + skip + " ROWS";

                String mySize;  // Fetch x rows or all rows; Works in conjunction with mySkip
                if (size == 0)  // If size is 0 fetch all rows
                    mySize = ";";
                else            // Otherwise fetch next x rows
                    mySize = " FETCH NEXT " + size + " ROWS ONLY;";
                String myQuerry = mySelect + myWhere + myOrder + mySkip + mySize;
                System.out.println(myQuerry);
                java.sql.Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery(myQuerry);
                    result += "[\n";    //Iterate through all rows and build a JSON set response
                    while (rs.next()) {
                        result += "\t{\n" +
                                "\t\t\"id\": " + rs.getInt("id") + ",\n" +
                                "\t\t\"name\": \"" + rs.getString("denumire") + "\",\n" +
                                "\t\t\"regNo\": \"" + rs.getString("nr_inreg") + "\",\n" +
                                "\t\t\"county\": \"" + rs.getString("judet") + "\",\n" +
                                "\t\t\"city\": \"" + rs.getString("localitate") + "\",\n" +
                                "\t\t\"address\": \"" + rs.getString("adresa") + "\",\n" +
                                "\t\t\"description\": \"" + rs.getString("descriere") + "\",\n" +
                                "\t\t\"email\": \"" + rs.getString("email") + "\",\n" +
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
}
