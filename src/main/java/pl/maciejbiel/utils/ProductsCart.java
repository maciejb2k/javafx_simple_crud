package pl.maciejbiel.utils;

import pl.maciejbiel.entities.Produkty;

public class ProductsCart {
    Produkty product;
    private int quantity;

    public ProductsCart(Produkty product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Produkty getProduct() {
        return product;
    }

    public void setProduct(Produkty product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

