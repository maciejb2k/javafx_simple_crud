package pl.maciejbiel.service;

import pl.maciejbiel.dao.UzytkownicyDAO;
import pl.maciejbiel.dao.ZamowieniaDAO;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.entities.Zamowienia;
import pl.maciejbiel.entities.ZamowieniaProdukty;
import pl.maciejbiel.utils.ProductsCart;

import java.util.ArrayList;
import java.util.List;

public class ZamowieniaService {
    private final ZamowieniaDAO ordersDAO = new ZamowieniaDAO();

    public void addOrder(Zamowienia order, ArrayList<ProductsCart> cart) {
        ordersDAO.addOrder(order, cart);
    }

    public void deleteOrder(Zamowienia order) {
        ordersDAO.deleteOrder(order);
    }

    public void updateOrder(Zamowienia order) {
        ordersDAO.updateOrder(order);
    }

    public List<Zamowienia> listOrders() {
        return ordersDAO.listOrders();
    }

    public List<Zamowienia> listLatestOrders() {
        return ordersDAO.listLatestOrders();
    }

    public List<Zamowienia> listOrderByPayment(String payment) {
        return ordersDAO.listOrderByPayment(payment);
    }

    public List<Zamowienia> listOrderByEmail(String email) {
        return ordersDAO.listOrderByEmail(email);
    }

    public List<ZamowieniaProdukty> listProductsFromOrder(Zamowienia order) {
        return ordersDAO.listProductsFromOrder(order);
    }
}
