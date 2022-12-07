package ngo.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ngo.backend.Model.Employee;
import ngo.backend.Service.EMPService;

@RestController
@RequestMapping("/api/emp")
public class EMPController {
    @Autowired
    private EMPService service;

    @GetMapping("/list")
    public String list() {
        List<Employee> list = service.listAll();
        String result = "[\n";
        for (Employee emp : list) {
            result += emp.toString();
            if (emp != list.get(list.size() - 1))
                result += ",\n";
        }
        result += "\n]";
        return result;
    }
    

    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    @GetMapping("/")
    public String get() {
        return "API Works~!";
    }

    @GetMapping("/{empno}")
    public String get(@PathVariable("empno") Integer empno) {
        try {
            Employee emp = service.get(empno);
            return emp.toString();
        } catch (Exception e) {
            return "Employee not found! Err: " + e.getMessage();
        }
    }

    
}
