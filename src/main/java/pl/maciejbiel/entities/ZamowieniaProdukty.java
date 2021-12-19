package pl.maciejbiel.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "zamowienia_produkty")
public class ZamowieniaProdukty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkty_id")
    private Produkty produkty;

    @ManyToOne(cascade=CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "zamowienia_id")
    private Zamowienia zamowienia;

    @Column(name="ilosc")
    private int ilosc;

    public ZamowieniaProdukty(Produkty produkty, Zamowienia zamowienia, int ilosc) {
        this.produkty = produkty;
        this.zamowienia = zamowienia;
        this.ilosc = ilosc;
    }

    public ZamowieniaProdukty() {
    }

    public Produkty getProdukty() {
        return produkty;
    }

    public void setProdukty(Produkty produkty) {
        this.produkty = produkty;
    }

    public Zamowienia getZamowienia() {
        return zamowienia;
    }

    public void setZamowienia(Zamowienia zamowienia) {
        this.zamowienia = zamowienia;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    @Override
    public String toString() {
        return "ZamowieniaProdukty{" +
                "id=" + id +
                ", produkty=" + produkty +
                ", zamowienia=" + zamowienia +
                ", ilosc=" + ilosc +
                '}';
    }
}
