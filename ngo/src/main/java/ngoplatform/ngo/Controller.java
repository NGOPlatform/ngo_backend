package ngoplatform.ngo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class Controller {

    @GetMapping("/")
    public String index() {
        return "API is live!";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(value = "description", defaultValue = "") String description,
                         @RequestParam(value = "city", defaultValue = "") String city,
                         @RequestParam(value = "county", defaultValue = "") String county,
                         @RequestParam(value = "size", defaultValue = "0") int size,
                         @RequestParam(value = "skip", defaultValue = "0") int skip) {
        String data;
        List<NGOData> testList = new ArrayList<NGOData>() {{
            add(new NGOData("FEDERATIA CARITAS A DIACEZIEI TIMISOARA", "13/C/1993", "TIMIS", "Timisoara", "Str. Matei Corvin, nr. 2","Caritate crestina de ajutorarea tuturor..."));
            add(new NGOData("FILIALA FUNDATIEI UMANITARE 'PARADISUL COPIILOR' TIMISOARA", "175/B/2003", "TIMIS", "Timisoara", "STR. LIVIU REBREANU NR 8","De a sustine activitatile umanitare ale Fundatiei Umanitare..."));
            add(new NGOData("FUNDATIA ACADEMICA CULTURALA TIMISOARA", "19/B/2021", "TIMIS", "Timisoara", "B-DUL LIVIU REBREANU, NR 81A","Sa stimuleze si sa promoveze valorile culturale si..."));
        }};
        data = "[\n";
        if (description.isEmpty() && city.isEmpty() && county.isEmpty()) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                if(size == 0 || size > testList.size())
                {
                    while(skip < testList.size())
                    {
                        data += ow.writeValueAsString(testList.get(skip));
                        if (skip != testList.size() - 1) {
                            data += ",\n";
                        }
                        skip++;
                    }
                }
                else
                {
                    while(size > 0)
                    {
                        data += ow.writeValueAsString(testList.get(skip));
                        if (size != 1) {
                            data += ",";
                        }
                        size--;
                        skip++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                if(size == 0 || size > testList.size())
                {
                    while(skip < testList.size())
                    {
                        if (testList.get(skip).getDescription().contains(description) && testList.get(skip).getCity().contains(city) && testList.get(skip).getCounty().contains(county)) {
                            data += ow.writeValueAsString(testList.get(skip));
                            if (skip != testList.size() - 1) {
                                data += ",";
                            }
                        }
                        skip++;
                    }
                }
                else
                {
                    while(size > 0)
                    {
                        if (testList.get(skip).getDescription().contains(description) && testList.get(skip).getCity().contains(city) && testList.get(skip).getCounty().contains(county)) {
                            data += ow.writeValueAsString(testList.get(skip));
                            if (size != 1) {
                                data += ",";
                            }
                            size--;
                        }
                        skip++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data += "\n]";
        return data;
    }
}



