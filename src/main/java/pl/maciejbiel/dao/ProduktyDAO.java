package pl.maciejbiel.dao;

import org.hibernate.Session;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class ProduktyDAO {
    public void addProduct(Produkty product) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.save(product);
        s.getTransaction().commit();
        s.close();
    }

    public void updateProduct(Produkty product) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.update(product);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteProduct(Long id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        Produkty u = s.load(Produkty.class , id);
        s.delete(u);
        s.getTransaction().commit();
        s.close();
    }

    public List<Produkty> listProducts() {
        List<Produkty> list = new ArrayList<>();
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Produkty ").list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Produkty> listProductsByName(String name) {
        List<Produkty> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Produkty U WHERE U.nazwa LIKE :nazwa")
                .setParameter("nazwa", name + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Produkty> listProductsByCode(String code) {
        List<Produkty> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Produkty U WHERE U.kod_producenta LIKE :kod_producenta")
                .setParameter("kod_producenta", code + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Produkty> listProductsByDesc(String desc) {
        List<Produkty> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Produkty U WHERE U.opis LIKE :opis")
                .setParameter("opis", desc + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Produkty> listProductsByCategory(String category) {
        List<Produkty> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Produkty U WHERE U.kategoria LIKE :kategoria")
                .setParameter("kategoria", category + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }
}
