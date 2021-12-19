package pl.maciejbiel.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import pl.maciejbiel.entities.*;
import pl.maciejbiel.service.ZamowieniaService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersDetails implements Initializable {
    private final ZamowieniaService ordersService = new ZamowieniaService();
    private final ObservableList<String> productsList = FXCollections.observableArrayList();
    private Zamowienia order;

    @FXML
    public Text OrdersDetails_info_street;
    public Text OrdersDetails_info_zipcodeCity;
    public Text OrdersDetails_info_country;
    public Text OrdersDetails_info_name;
    public Text OrdersDetails_info_email;
    public Text OrdersDetails_info_username;
    public Text OrdersDetails_info_phone;
    public ListView<String> OrdersDetails_list_products;
    public Text OrdersDetails_info_date;
    public Text OrdersDetails_info_price;
    public Text OrdersDetails_info_payment;
    public CheckBox OrdersDetails_checkbox;
    public ChoiceBox<String> OrdersAdd_select_progress;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initController() {
        Adresy address = order.getUzytkownicy().getAdresy();
        Uzytkownicy user = order.getUzytkownicy();

        OrdersDetails_info_name.setText(user.getImie() + " " + user.getNazwisko());
        OrdersDetails_info_email.setText(user.getEmail());
        OrdersDetails_info_username.setText(user.getNazwa_uzytkownika());
        OrdersDetails_info_phone.setText("+48 " + user.getTelefon());

        OrdersDetails_info_street.setText(address.getUlica() + " " + address.getNr_mieszkania());
        OrdersDetails_info_zipcodeCity.setText(address.getKod_pocztowy() + " " + address.getMiasto());
        OrdersDetails_info_country.setText(address.getKraj());

        OrdersDetails_info_date.setText(String.valueOf(order.getData_zamowienia()));
        OrdersDetails_info_price.setText(order.getKwota() + " zł");
        OrdersDetails_info_payment.setText(String.valueOf(order.getForma_platnosci()));

        List<ZamowieniaProdukty> orderProducts = ordersService.listProductsFromOrder(order);
        for (ZamowieniaProdukty z : orderProducts) {
            productsList.add(z.getIlosc() + "x - " + z.getProdukty().getCena() + "zł - " + z.getProdukty().getNazwa());
        }

        OrdersDetails_list_products.setItems(productsList);
        OrdersDetails_checkbox.setSelected(order.isCzy_zrealizowano());

        OrdersDetails_checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            order.setCzy_zrealizowano(newValue);
            ordersService.updateOrder(order);
            System.out.println(order.isCzy_zrealizowano());
        });

        OrdersAdd_select_progress.setItems(FXCollections.observableArrayList(
            "złożone",
            "przyjęte do realizacji",
            "w trakcie kompletowania",
            "oczekiwanie na płatność",
            "przesyłka wysłana",
            "zakończono"
        ));
        OrdersAdd_select_progress.setValue(order.getStatus_zamowienia());

        OrdersAdd_select_progress.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            order.setStatus_zamowienia(newValue);
            if(newValue.equals("zakończono") && !OrdersDetails_checkbox.isSelected()) {
                order.setCzy_zrealizowano(true);
                OrdersDetails_checkbox.setSelected(order.isCzy_zrealizowano());
            }
            ordersService.updateOrder(order);
        });
    }

    public Zamowienia getOrder() {
        return order;
    }

    public void setOrder(Zamowienia order) {
        this.order = order;
    }
}
