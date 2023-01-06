package ngo.backend.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
    @JsonProperty("user")
    private String user;
    @JsonProperty("password")
    private String password;
    @JsonProperty("key")
    private String key;
    @JsonProperty("hostName")
    private String hostName;
    @JsonProperty("DBName")
    private String DBName;

    public Config() {
    }

    public Config(String user, String password, String hostName, String dbName, String key) {
        this.user = user;
        this.password = password;
        this.hostName = hostName;
        this.DBName = dbName;
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHostName() {
        return hostName;
    }

    public String getDbName() {
        return DBName;
    }

    public String getKey() {
        return key;
    }
}
