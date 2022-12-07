package ngo.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Entity
@Table (name = "ONG")
public class ONG {
    @Column(name = "denumire")
    private String denumire;
    @Id
    @Column(name = "nr_inreg")
    private String regNumber;
    @Column(name = "judet")
    private String county;
    @Column(name = "localitate")
    private String city;
    @Column(name = "adresa")
    private String address;
    @Column(name = "descriere")
    private String description;
    @Column(name = "email")
    private String email;

    public ONG() {
    }

    public ONG(String denumire, String nr_inreg, String judet, String localitate, String adresa, String descriere, String email) {
        this.denumire = denumire;
        this.regNumber = nr_inreg;
        this.county = judet;
        this.city = localitate;
        this.address = adresa;
        this.description = descriere;
        this.email = email;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getNr_inreg() {
        return regNumber;
    }

    public void setNr_inreg(String nr_inreg) {
        this.regNumber = nr_inreg;
    }

    public String getJudet() {
        return county;
    }

    public void setJudet(String judet) {
        this.county = judet;
    }

    public String getLocalitate() {
        return city;
    }

    public void setLocalitate(String localitate) {
        this.city = localitate;
    }

    public String getAdresa() {
        return address;
    }

    public void setAdresa(String adresa) {
        this.address = adresa;
    }

    public String getDescriere() {
        return description;
    }

    public void setDescriere(String descriere) {
        this.description = descriere;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
