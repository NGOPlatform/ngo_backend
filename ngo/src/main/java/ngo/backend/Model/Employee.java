package ngo.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Entity
@Table (name = "emp")
public class Employee {
    @Id
    @Column(name = "empno")
    private Integer empno;
    @Column(name = "mgr")
    private Integer mgr;
    @Column(name = "sal")
    private Integer sal;
    @Column(name = "comm")
    private Integer comm;
    @Column(name = "deptno")
    private Integer deptno;
    @Column(name = "ename")
    private String ename;
    @Column(name = "job")
    private String job;
    @Column(name = "hiredate")
    private Date hiredate;

    public Employee() {
    }

    public Employee(Integer empno, Integer mgr, Integer sal, Integer comm, Integer deptno, String ename, String job, Date hiredate) {
        this.empno = empno;
        this.mgr = mgr;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
        this.ename = ename;
        this.job = job;
        this.hiredate = hiredate;
    }

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public Integer getSal() {
        return sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    public Integer getComm() {
        return comm;
    }

    public void setComm(Integer comm) {
        this.comm = comm;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getHiredate() {
        return hiredate.toString();
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this);
        } catch (Exception e) {
           return e.getMessage();
        }
    }

}
