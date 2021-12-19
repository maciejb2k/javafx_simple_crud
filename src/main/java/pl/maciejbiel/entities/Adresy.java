package pl.maciejbiel.entities;

import javax.persistence.*;

@   Entity
@Table(name = "adresy")
public class Adresy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    @Column(name="ulica", length = 64)
    private String ulica;

    @Column(name="nr_mieszkania", length = 32)
    private String nr_mieszkania;

    @Column(name="miasto", length = 64)
    private String miasto;

    @Column(name="kod_pocztowy", length = 6)
    private String kod_pocztowy;

    @Column(name="kraj", length = 64)
    private String kraj;

    public Adresy(String ulica, String nr_mieszkania, String miasto, String kod_pocztowy ,String kraj) {
        this.ulica = ulica;
        this.nr_mieszkania = nr_mieszkania;
        this.miasto = miasto;
        this.kod_pocztowy = kod_pocztowy;
        this.kraj = kraj;
    }

    public Adresy() {
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getNr_mieszkania() {
        return nr_mieszkania;
    }

    public void setNr_mieszkania(String nr_mieszkania) {
        this.nr_mieszkania = nr_mieszkania;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getKod_pocztowy() {
        return kod_pocztowy;
    }

    public void setKod_pocztowy(String kod_pocztowy) {
        this.kod_pocztowy = kod_pocztowy;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    @Override
    public String toString() {
        return "Adresy{" +
                "id=" + id +
                ", ulica='" + ulica + '\'' +
                ", nr_mieszkania='" + nr_mieszkania + '\'' +
                ", miasto='" + miasto + '\'' +
                ", kod_pocztowy='" + kod_pocztowy + '\'' +
                ", kraj='" + kraj + '\'' +
                '}';
    }
}
