package ngoplatform.ngo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
                         @RequestParam(value = "size", defaultValue = "0") int size) {
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
                if(size == 0)
                {
                    for (NGOData test : testList) {
                        data += ow.writeValueAsString(test);
                        if (testList.indexOf(test) != testList.size() - 1) {
                            data += ",";
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < size; i++) {
                        data += ow.writeValueAsString(testList.get(i));
                        if (i != size - 1) {
                            data += ",";
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                if(size == 0)
                {
                    for (NGOData test : testList) {
                        if (test.getDescription().contains(description) && test.getCity().contains(city) && test.getCounty().contains(county)) {
                            data += ow.writeValueAsString(test);
                            if (testList.indexOf(test) != testList.size() - 1) {
                                data += ",";
                            }
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < size; i++) {
                        if (testList.get(i).getDescription().contains(description) && testList.get(i).getCity().contains(city) && testList.get(i).getCounty().contains(county)) {
                            data += ow.writeValueAsString(testList.get(i));
                            if (i != size - 1) {
                                data += ",\n";
                            }
                        }
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



