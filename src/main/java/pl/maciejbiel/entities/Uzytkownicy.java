package pl.maciejbiel.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "uzytkownicy")
public class Uzytkownicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    @Column(name="imie", length = 64)
    private String imie;

    @Column(name="nazwisko", length = 64)
    private String nazwisko;

    @Column(name="nazwa_uzytkownika", length = 32)
    private String nazwa_uzytkownika;

    @Column(name="email", length = 64)
    private String email;

    @Column(name="telefon", length = 11)
    private String telefon;

    @Column(name="haslo", length = 255)
    private String haslo;

    @Column(name="data_rejestracji")
    private Timestamp data_rejestracji;

    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "adresy_id")
    private Adresy adresy;

    public Uzytkownicy(String imie, String nazwisko, String nazwa_uzytkownika, String email, String telefon, String haslo, Timestamp data_rejestracji, Adresy adresy) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nazwa_uzytkownika = nazwa_uzytkownika;
        this.email = email;
        this.telefon = telefon;
        this.haslo = haslo;
        this.data_rejestracji = data_rejestracji;
        this.adresy = adresy;
    }

    public Uzytkownicy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNazwa_uzytkownika() {
        return nazwa_uzytkownika;
    }

    public void setNazwa_uzytkownika(String nazwa_uzytkownika) {
        this.nazwa_uzytkownika = nazwa_uzytkownika;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public Timestamp getData_rejestracji() {
        return data_rejestracji;
    }

    public void setData_rejestracji(Timestamp data_rejestracji) {
        this.data_rejestracji = data_rejestracji;
    }

    public Adresy getAdresy() {
        return adresy;
    }

    public void setAdresy(Adresy adresy) {
        this.adresy = adresy;
    }

    @Override
    public String toString() {
        return "Uzytkownicy{" +
                "id=" + id +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", nazwa_uzytkownika='" + nazwa_uzytkownika + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", haslo='" + haslo + '\'' +
                ", data_rejestracji=" + data_rejestracji +
                ", adresy=" + adresy +
                '}';
    }
}
