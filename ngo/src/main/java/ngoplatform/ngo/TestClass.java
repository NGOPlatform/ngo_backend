package ngoplatform.ngo;

public class TestClass {
    private final String name;
    private final String content;
    private final long id;

    public TestClass(String name, String content, long id) {
        this.name = name;
        this.content = content;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public long getId() {
        return id;
    }

    //method to convert the object to JSON
    public String toJSON() {
        return "{\"name\":\"" + name + "\",\"content\":\"" + content + "\",\"id\":\"" + id + "\"}";
    }

    //method to convert the object to XML
    public String toXML() {
        return "<TestClass><name>" + name + "</name><content>" + content + "</content><id>" + id + "</id></TestClass>";
    }

}
