package ngo.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import ngo.backend.Model.ONG;
import ngo.backend.Service.ONGService;

@RestController
@RequestMapping("/api/ong")
@CrossOrigin(origins = "http://localhost:3000")
public class ONGController {
    @Autowired
    private ONGService service;

    @GetMapping("")
    public String get(@RequestParam(value = "nr_inreg", defaultValue = "4590/A/2000") String nr_inreg) {
        try {
            ONG ong = service.get(nr_inreg);
            return ong.toString();
        } catch (Exception e) {
            return "ONG not found! Err: " + e.getMessage();
        }
    }

    @GetMapping("/everything")
    public String everything() {
        List<ONG> list = service.listAll();
        String result = "[\n";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i).toString();
            if (i != list.size() - 1)
                result += ",\n";
        }
        result += "\n]";
        return result;
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "description", defaultValue = "") String descriere,
                       @RequestParam(value = "city", defaultValue = "") String localitate,
                       @RequestParam(value = "county", defaultValue = "") String judet,
                       @RequestParam(value = "size", defaultValue = "0") int size,
                       @RequestParam(value = "skip", defaultValue = "0") int skip) {
        List<ONG> list = service.listAll();
        String result = "[\n";
        if (size == 0)
            size = list.size();
        if (!descriere.isBlank() || !localitate.isBlank() || !judet.isBlank()) {
            for (int i = skip; i < size && i < list.size(); i++) {
                //if(list.get(i).getDescriere() == null && !descriere.isBlank())
                //    continue;
                //if(list.get(i).getLocalitate() == null && !localitate.isBlank())
                //    continue;
                //if(list.get(i).getJudet() == null && !judet.isBlank())
                //    continue;
                if (list.get(i).getDescriere().contains(descriere) || list.get(i).getLocalitate().contains(localitate)
                        || list.get(i).getJudet().contains(judet)) {
                    result += list.get(i).toString();
                    if (i != list.size() - 1 && i != size - 1)
                        result += ",\n";
                }
            }
        } else {
            for (int i = skip; i < size && i < list.size(); i++) {
                result += list.get(i).toString();
                if (i != list.size() - 1 && i != size - 1)
                    result += ",\n";
            }
        }
        result += "\n]";
        return result;
    }

    
}
