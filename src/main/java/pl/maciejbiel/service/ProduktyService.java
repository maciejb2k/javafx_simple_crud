package pl.maciejbiel.service;

import pl.maciejbiel.dao.ProduktyDAO;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.entities.Uzytkownicy;

import java.util.List;

public class ProduktyService {
    private final ProduktyDAO productsDAO = new ProduktyDAO();

    public void addProduct(Produkty product) {
        productsDAO.addProduct(product);
    }

    public void updateProduct(Produkty product) {
        productsDAO.updateProduct(product);
    }

    public void deleteProduct(Long id) {
        productsDAO.deleteProduct(id);
    }

    public List<Produkty> listProducts() {
        return productsDAO.listProducts();
    }

    public List<Produkty> listProductsByName(String name) {
        return productsDAO.listProductsByName(name);
    }

    public List<Produkty> listProductsByCode(String code) {
        return productsDAO.listProductsByCode(code);
    }

    public List<Produkty> listProductsByDesc(String desc) {
        return productsDAO.listProductsByDesc(desc);
    }

    public List<Produkty> listProductsByCategory(String category) {
        return productsDAO.listProductsByCategory(category);
    }
}
