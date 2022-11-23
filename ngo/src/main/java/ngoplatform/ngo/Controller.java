package ngoplatform.ngo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
            add(new TestClass("ASSOC-1", "Scope-1", 1));
            add(new TestClass("ASSOC-2", "Scope-2", 2));
            add(new TestClass("ASSOC-3", "Scope-3", 3));
            add(new TestClass("ASSOC-4", "Scope-4", 4));
            add(new TestClass("ASSOC-5", "Scope-5", 5));
            add(new TestClass("ASSOC-6", "Scope-6", 6));
            add(new TestClass("ASSOC-7", "Scope-7", 7));
            add(new TestClass("ASSOC-8", "Scope-8", 8));
            add(new TestClass("ASSOC-9", "Scope-9", 9));
            add(new TestClass("ASSOC-10", "Scope-10", 10));
            add(new TestClass("ASSOC-11", "Scope-11", 11));
        }};
        return testList.get(Integer.parseInt(id)).toJSON();
    }

    @GetMapping("/testAll")
    public String test(@RequestParam(value = "size", defaultValue = "0") int size) {
        String data;
        List<TestClass> testList = new ArrayList<TestClass>() {{
            add(new TestClass("ASSOC-1", "Scope-1", 1));
            add(new TestClass("ASSOC-2", "Scope-2", 2));
            add(new TestClass("ASSOC-3", "Scope-3", 3));
            add(new TestClass("ASSOC-4", "Scope-4", 4));
            add(new TestClass("ASSOC-5", "Scope-5", 5));
            add(new TestClass("ASSOC-6", "Scope-6", 6));
            add(new TestClass("ASSOC-7", "Scope-7", 7));
            add(new TestClass("ASSOC-8", "Scope-8", 8));
            add(new TestClass("ASSOC-9", "Scope-9", 9));
            add(new TestClass("ASSOC-10", "Scope-10", 10));
            add(new TestClass("ASSOC-11", "Scope-11", 11));
        }};
        data = "[";
        if (size == 0) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                for (TestClass test : testList) {
                    data += ow.writeValueAsString(test);
                    if (testList.indexOf(test) != testList.size() - 1) {
                        data += ",";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                for (int i = 0; i < size; i++) {
                    data += ow.writeValueAsString(testList.get(i));
                    if (i != size - 1) {
                        data += ",";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data += "]";
        return data;
}}

