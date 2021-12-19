package pl.maciejbiel.service;

import pl.maciejbiel.dao.UzytkownicyDAO;
import pl.maciejbiel.entities.Uzytkownicy;

import java.util.List;

public class UzytkownicyService {
    private final UzytkownicyDAO usersDAO = new UzytkownicyDAO();

    public void addUser(Uzytkownicy user) {
        usersDAO.addUser(user);
    }

    public void updateUser(Uzytkownicy user) {
        usersDAO.updateUser(user);
    }

    public void deleteUser(Long id) {
        usersDAO.deleteUser(id);
    }

    public List<Uzytkownicy> listUser() {
        return usersDAO.listUser();
    }

    public List<Uzytkownicy>  getUserNameById(Long id) {
        return usersDAO.getUserNameById(id);
    }

    public List<Uzytkownicy> getUserByEmail(String email) {
        return usersDAO.getUserByEmail(email);
    }

    public List<Uzytkownicy> getUserByUsername(String username) {
        return usersDAO.getUserByUsername(username);
    }

    public List<Uzytkownicy> listUserById(Long id) {
        return usersDAO.listUserById(id);
    }

    public List<Uzytkownicy> listUserByName(String name) {
        return usersDAO.listUserByName(name);
    }

    public List<Uzytkownicy> listUserBySurname(String surname) {
        return usersDAO.listUserBySurname(surname);
    }

    public List<Uzytkownicy> listUserByEmail(String email) {
        return usersDAO.listUserByEmail(email);
    }

    public List<Uzytkownicy> listUserByUsername(String username) {
        return usersDAO.listUserByUsername(username);
    }

    public List<Uzytkownicy> listUserByPhone(String phone) {
        return usersDAO.listUserByPhone(phone);
    }
}
