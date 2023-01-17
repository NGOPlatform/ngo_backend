package ngo.backend.Model;

public class ngo {
    private int ID;
    private String name;
    private String regNo;
    private String county;
    private String city;
    private String address;
    private String description;
    private String email;

    public ngo() {
    }

    public ngo(int ID, String name, String regNo, String county, String city, String address, String description, String email) {
        this.ID = ID;
        this.name = name;
        this.regNo = regNo;
        this.county = county;
        this.city = city;
        this.address = address;
        this.description = description;
        this.email = email;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getCounty() {
        return county;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
