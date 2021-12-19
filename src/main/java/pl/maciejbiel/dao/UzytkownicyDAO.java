package pl.maciejbiel.dao;

import org.hibernate.Session;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;

public class UzytkownicyDAO{
    public void addUser(Uzytkownicy user) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.save(user);
        s.getTransaction().commit();
        s.close();
    }

    public void updateUser(Uzytkownicy user) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.update(user);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteUser(Long id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        Uzytkownicy u = s.load(Uzytkownicy.class , id);
        s.delete(u);
        s.getTransaction().commit();
        s.close();
    }

    public List<Uzytkownicy> listUser() {
        List<Uzytkownicy> list = new ArrayList<>();
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy").list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> listUserByName(String name) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.imie LIKE :imie")
                .setParameter("imie", name + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> getUserNameById(Long id) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.id = :id")
                .setParameter("id", id)
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> getUserByEmail(String email) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.email = :email")
                .setParameter("email", email)
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> getUserByUsername(String username) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.nazwa_uzytkownika = :username")
                .setParameter("username", username)
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> listUserBySurname(String surname) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.nazwisko LIKE :nazwisko")
                .setParameter("nazwisko", surname + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> listUserByEmail(String email) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.email LIKE :email")
                .setParameter("email", email + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> listUserByUsername(String username) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.nazwa_uzytkownika LIKE :nazwa_uzytkownika")
                .setParameter("nazwa_uzytkownika", username + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> listUserByPhone(String phone) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.telefon LIKE :telefon")
                .setParameter("telefon", phone + "%")
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    public List<Uzytkownicy> listUserById(Long id) {
        List<Uzytkownicy> list;
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        list = s.createQuery("FROM Uzytkownicy U WHERE U.id = :id")
                .setParameter("id", id)
                .list();
        s.getTransaction().commit();
        s.close();
        return list;
    }
}
