package pl.maciejbiel.entities;

import javax.persistence.*;

@Entity
@Table(name = "produkty")
public class Produkty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    @Column(name="nazwa", length = 128)
    private String nazwa;

    @Column(name="kod_producenta", length = 32)
    private String kod_producenta;

    @Column(name="cena")
    private Double cena;

    @Column(name="opis", length = 255)
    private String opis;

    @Column(name="kategoria", length = 255)
    private String kategoria;

    public Produkty(String nazwa, String kod_producenta, double cena, String opis, String kategoria) {
        this.nazwa = nazwa;
        this.kod_producenta = kod_producenta;
        this.cena = cena;
        this.opis = opis;
        this.kategoria = kategoria;
    }

    public Produkty() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKod_producenta() {
        return kod_producenta;
    }

    public void setKod_producenta(String kod_producenta) {
        this.kod_producenta = kod_producenta;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }
}
