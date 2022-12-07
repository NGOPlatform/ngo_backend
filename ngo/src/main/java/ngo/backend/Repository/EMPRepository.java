package ngo.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ngo.backend.Model.Employee;

public interface EMPRepository extends JpaRepository<Employee, Integer>{
}
