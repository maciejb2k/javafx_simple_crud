package pl.maciejbiel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.service.ProduktyService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductsAdd implements Initializable {
    private final ProduktyService productsService = new ProduktyService();

    private String actionType;
    private Produkty product;

    @FXML
    public Label ProductsAdd_label_title;
    public TextField ProductsAdd_input_name;
    public TextField ProductsAdd_input_code;
    public TextField ProductsAdd_input_price;
    public TextField ProductsAdd_input_category;
    public TextArea ProductsAdd_input_desc;
    public Button ProductsAdd_button_add;
    public Text ProductsAdd_text_form;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initController() throws Exception {
        if (actionType.equals("ADD") && product == null) {
            ProductsAdd_label_title.setText("Dodaj Produkt");
            ProductsAdd_button_add.setText("Dodaj");
        } else if (actionType.equals("UPDATE") && product != null) {
            ProductsAdd_label_title.setText("Edytuj Produkty");
            ProductsAdd_button_add.setText("Edytuj");
            fillForm();
        } else {
            throw new Exception("Controller:ProductAdd - initController() failed");
        }
    }

    public void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == ProductsAdd_button_add) {
            handleFormSubmit();
        }
    }

    public void fillForm() {
        ProductsAdd_input_name.setText(product.getNazwa());
        ProductsAdd_input_code.setText(product.getKod_producenta());
        ProductsAdd_input_price.setText(String.valueOf(product.getCena()));
        ProductsAdd_input_category.setText(product.getKategoria());
        ProductsAdd_input_desc.setText(product.getOpis());
    }

    public void clearForm() {
        ProductsAdd_input_name.clear();
        ProductsAdd_input_code.clear();
        ProductsAdd_input_price.clear();
        ProductsAdd_input_category.clear();
        ProductsAdd_input_desc.clear();
    }

    public void handleFormSubmit() throws IOException {
        String name = ProductsAdd_input_name.getText();
        String code = ProductsAdd_input_code.getText();
        String price = ProductsAdd_input_price.getText();
        String category = ProductsAdd_input_category.getText();
        String desc = ProductsAdd_input_desc.getText();

        if (name == null || name.trim().isEmpty()) {
            setMessageText("error", "Podaj nazwę produktu");
            return;
        }
        if (code == null || code.trim().isEmpty()) {
            setMessageText("error", "Podaj kod producenta");
            return;
        }
        if (price == null || price.trim().isEmpty()) {
            setMessageText("error", "Podaj cenę produktu");
            return;
        }
        if (category == null || category.trim().isEmpty()) {
            setMessageText("error", "Podaj kategorię produktu");
            return;
        }
        if (desc == null || desc.trim().isEmpty()) {
            setMessageText("error", "Podaj opis produktu");
            return;
        }


        if(name.length() > 128) {
            setMessageText("error", "Nazwa produktu jest za długa");
            return;
        }

        if(code.length() > 32) {
            setMessageText("error", "Kod produktu jest za długi");
            return;
        }

        if(desc.length() > 255) {
            setMessageText("error", "Opis jest za długi");
            return;
        }

        if(category.length() > 255) {
            setMessageText("error", "Kategoria jest za długa");
            return;
        }

        double parsedPrice;

        try {
            parsedPrice = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            setMessageText("error", "Podaj poprawną cenę");
            return;
        }

        desc = desc.replaceAll("[\\t\\n\\r]+"," ");

        if (actionType.equals("ADD")) {
            Produkty product = new Produkty(name, code, parsedPrice, desc, category);
            addProduct(product);
            setMessageText("success", "Poprawnie dodano produkt. Dodaj kolejne");
        } else if (actionType.equals("UPDATE")) {
            product.setNazwa(name);
            product.setKod_producenta(code);
            product.setCena(parsedPrice);
            product.setKategoria(category);
            product.setOpis(desc);
            updateProduct(product);
            setMessageText("success", "Poprawnie edytowano produkt.");
        } else {
            // TODO
        }
    }

    public void addProduct(Produkty product) {
        productsService.addProduct(product);
        ProductsAdd_text_form.setText("Poprawnie dodano produkt.");
        clearForm();
    }

    public void updateProduct(Produkty product) {
        System.out.println(product);
        productsService.updateProduct(product);
        ProductsAdd_text_form.setText("Poprawnie zaktualizowano produkt.");
    }

    public void setMessageText(String type, String text) {
        if (type.equals("error")) {
            ProductsAdd_text_form.setStyle("-fx-text-fill: #bc0404; -fx-font-weight: bold;");
            ProductsAdd_text_form.setText("Błąd - " + text);
        } else if (type.equals("success")) {
            ProductsAdd_text_form.setStyle("-fx-text-fill: #38830a; -fx-font-weight: bold;");
            ProductsAdd_text_form.setText("Sukces - " + text);
        } else {
            ProductsAdd_text_form.setStyle("-fx-font-weight: bold;");
            ProductsAdd_text_form.setText(text);
        }
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Produkty getProduct() {
        return product;
    }

    public void setProduct(Produkty product) {
        this.product = product;
    }
}
