package pl.maciejbiel.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "zamowienia")
public class Zamowienia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "uzytkownicy_id")
    private Uzytkownicy uzytkownicy;

    @Column(name="forma_platnosci", length = 64)
    private String forma_platnosci;

    @Column(name="data_zamowienia")
    private Timestamp data_zamowienia;

    @Column(name="status_zamowienia", length = 32)
    private String status_zamowienia;

    @Column(name="czy_zrealizowano")
    private boolean czy_zrealizowano;

    @Column(name="kwota")
    private double kwota;

    @Column(name="usuniete")
    private boolean usuniete;

    public Zamowienia(Uzytkownicy uzytkownicy, String forma_platnosci, Timestamp data_zamowienia, String status_zamowienia, boolean czy_zrealizowano, double kwota, boolean usuniete) {
        this.uzytkownicy = uzytkownicy;
        this.forma_platnosci = forma_platnosci;
        this.data_zamowienia = data_zamowienia;
        this.status_zamowienia = status_zamowienia;
        this.czy_zrealizowano = czy_zrealizowano;
        this.kwota = kwota;
        this.usuniete = usuniete;
    }

    public Zamowienia() {
    }

    public Uzytkownicy getUzytkownicy() {
        return uzytkownicy;
    }

    public void setUzytkownicy(Uzytkownicy uzytkownicy) {
        this.uzytkownicy = uzytkownicy;
    }

    public String getForma_platnosci() {
        return forma_platnosci;
    }

    public void setForma_platnosci(String forma_platnosci) {
        this.forma_platnosci = forma_platnosci;
    }

    public Timestamp getData_zamowienia() {
        return data_zamowienia;
    }

    public void setData_zamowienia(Timestamp data_zamowienia) {
        this.data_zamowienia = data_zamowienia;
    }

    public String getStatus_zamowienia() {
        return status_zamowienia;
    }

    public void setStatus_zamowienia(String status_zamowienia) {
        this.status_zamowienia = status_zamowienia;
    }

    public boolean isCzy_zrealizowano() {
        return czy_zrealizowano;
    }

    public void setCzy_zrealizowano(boolean czy_zrealizowano) {
        this.czy_zrealizowano = czy_zrealizowano;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getKwota() {
        return kwota;
    }

    public void setKwota(double kwota) {
        this.kwota = kwota;
    }

    public boolean isUsuniete() {
        return usuniete;
    }

    public void setUsuniete(boolean usuniete) {
        this.usuniete = usuniete;
    }
}
