package ngo.backend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ngo.backend.Model.Employee;
import ngo.backend.Repository.EMPRepository;

@Service
@Transactional
public class EMPService {
    @Autowired
    private EMPRepository repo;
    public List<Employee> listAll() {
        return repo.findAll();
    }

    public void save(Employee ong) {
        repo.save(ong);
    }

    public Employee get(Integer empno) {
        return repo.findById(empno).get();
    }

    public void delete(Integer empno) {
        repo.deleteById(empno);
    }

    public long count() {
        return repo.count();
    }
}
