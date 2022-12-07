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
    private String nr_inreg;
    @Column(name = "judet")
    private String judet;
    @Column(name = "localitate")
    private String localitate;
    @Column(name = "adresa")
    private String adresa;
    @Column(name = "descriere")
    private String descriere;
    @Column(name = "email")
    private String email;

    public ONG() {
    }

    public ONG(String denumire, String nr_inreg, String judet, String localitate, String adresa, String descriere, String email) {
        this.denumire = denumire;
        this.nr_inreg = nr_inreg;
        this.judet = judet;
        this.localitate = localitate;
        this.adresa = adresa;
        this.descriere = descriere;
        this.email = email;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getNr_inreg() {
        return nr_inreg;
    }

    public void setNr_inreg(String nr_inreg) {
        this.nr_inreg = nr_inreg;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
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
