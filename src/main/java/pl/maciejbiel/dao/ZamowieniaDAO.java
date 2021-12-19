package pl.maciejbiel.dao;

import org.hibernate.Session;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.entities.Zamowienia;
import pl.maciejbiel.entities.ZamowieniaProdukty;
import pl.maciejbiel.utils.HibernateUtil;
import pl.maciejbiel.utils.ProductsCart;

import java.util.ArrayList;
import java.util.List;

public class ZamowieniaDAO {
    public void addOrder(Zamowienia order, ArrayList<ProductsCart> cart) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.save(order);
        for(ProductsCart p : cart) {
            s.save(new ZamowieniaProdukty(p.getProduct(), order, p.getQuantity()));
        }
        s.getTransaction().commit();
        s.close();
    }

    public void deleteOrder(Zamowienia order) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.update(order);
        s.getTransaction().commit();
        s.close();
    }

    public void updateOrder(Zamowienia order) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.update(order);
        s.getTransaction().commit();
        s.close();
    }

    public List<Zamowienia> listOrders() {
        List<Zamowienia> list = new ArrayList<>();
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Zamowienia Z WHERE Z.usuniete = false").list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Zamowienia> listLatestOrders() {
        List<Zamowienia> list = new ArrayList<>();
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Zamowienia Z WHERE Z.usuniete = false ORDER BY Z.data_zamowienia DESC").setMaxResults(6).list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Zamowienia> listOrderByPayment(String payment) {
        List<Zamowienia> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Zamowienia Z WHERE Z.forma_platnosci LIKE :forma_platnosci")
                .setParameter("forma_platnosci", payment + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Zamowienia> listOrderByEmail(String email) {
        List<Zamowienia> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Zamowienia Z WHERE Z.uzytkownicy.email LIKE :email")
                .setParameter("email", email + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<ZamowieniaProdukty> listProductsFromOrder(Zamowienia order) {
        List<ZamowieniaProdukty> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM ZamowieniaProdukty Z WHERE Z.zamowienia = :order")
                .setParameter("order", order)
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }
}
