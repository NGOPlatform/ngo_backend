package ngoplatform.ngo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/test")
    public String test(@RequestParam(value = "id", defaultValue = "0") String id) {
        List<TestClass> testList = new ArrayList<TestClass>() {{
            add(new TestClass("ASSOC-1", "What", 1));
            add(new TestClass("ASSOC-2", "Scope", 2));
            add(new TestClass("ASSOC-3", "Why", 3));
            add(new TestClass("ASSOC-4", "How", 4));
        }};
        return testList.get(Integer.parseInt(id)).toJSON();
    }

    @GetMapping("/testAll")
    public String test(@RequestParam(value = "size", defaultValue = "0") int size) {
        String data;
        List<TestClass> testList = new ArrayList<TestClass>() {{
            add(new TestClass("ASSOC-1", "What", 1));
            add(new TestClass("ASSOC-2", "Scope", 2));
            add(new TestClass("ASSOC-3", "Why", 3));
            add(new TestClass("ASSOC-4", "How", 4));
        }};
        data = "[";
        if (size == 0) {
            for (int i = 0; i < testList.size(); i++) {
                data += testList.get(i).toJSON();
                if (i != testList.size() - 1) {
                    data += ",";
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                data += testList.get(i).toJSON();
                if (i != size - 1) {
                    data += ",";
                }
            }
        }
        data += "]";
        return data;
    }
}

