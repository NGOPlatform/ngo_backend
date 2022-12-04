package ngoplatform.ngo.model;

public class NGOData {
    private final String name, regNumber, county, city, address, description;

    public NGOData(String name, String regNumber, String county, String city, String address, String description) {
        this.name = name;
        this.regNumber = regNumber;
        this.county = county;
        this.city = city;
        this.address = address;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegNum() {
        return regNumber;
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
}
