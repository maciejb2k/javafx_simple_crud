package pl.maciejbiel.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import pl.maciejbiel.App;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.entities.Zamowienia;
import pl.maciejbiel.service.ProduktyService;
import pl.maciejbiel.service.UzytkownicyService;
import pl.maciejbiel.service.ZamowieniaService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {
    private ObservableList<Zamowienia> ordersList = FXCollections.observableArrayList();

    private final ProduktyService productsService = new ProduktyService();
    private final UzytkownicyService usersService = new UzytkownicyService();
    private final ZamowieniaService ordersService = new ZamowieniaService();

    @FXML
    public Button Home_button_orders;
    public Button Home_button_products;
    public Button Home_button_users;
    public Button Home_button_logout;
    public Text Home_text_usersCount;
    public Text Home_text_productsCount;
    public Text Home_text_ordersCount;
    public TableView<Zamowienia> Home_table_orders;
    public TableColumn<Zamowienia, String> Home_col_id;
    public TableColumn<Zamowienia, String> Home_col_email;
    public TableColumn<Zamowienia, String> Home_col_date;
    public TableColumn<Zamowienia, String> Home_col_done;

    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == Home_button_orders) {
            App.setRoot("Orders");
        }
        else if (mouseEvent.getSource() == Home_button_products) {
            App.setRoot("Products");
        }
        else if (mouseEvent.getSource() == Home_button_users) {
            App.setRoot("Users");
        }
        else if (mouseEvent.getSource() == Home_button_logout) {
            Platform.exit();
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int usersCount = usersService.listUser().size();
        int productsCount = productsService.listProducts().size();
        int ordersCount = ordersService.listOrders().size();

        Home_text_usersCount.setText(String.valueOf(usersCount));
        Home_text_productsCount.setText(String.valueOf(productsCount));
        Home_text_ordersCount.setText(String.valueOf(ordersCount));

        setupTableView(getOrdersList());
    }

    public ObservableList<Zamowienia> getOrdersList(){
        if(!ordersList.isEmpty()) {
            ordersList.clear();
        }
        ordersList = FXCollections.observableList(ordersService.listLatestOrders());
        return ordersList;
    }

    public void setupTableView(ObservableList<Zamowienia> orders){
        Home_table_orders.getItems().clear();
        Home_table_orders.setItems(orders);
        Home_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Home_col_email.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getUzytkownicy().getEmail())));
        Home_col_date.setCellValueFactory(new PropertyValueFactory<>("data_zamowienia"));
        Home_col_done.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isCzy_zrealizowano() ? "Tak" : "Nie"));
        Home_col_done.setCellFactory(new Callback<>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Zamowienia, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.contains("Nie")) {
                                this.setStyle("-fx-alignment: CENTER; -fx-background-color: #f3e1e2; -fx-text-fill: #bc0404; -fx-font-weight: bold;" );

                            }
                            if (item.contains("Tak")) {
                                this.setStyle("-fx-alignment: CENTER; -fx-background-color: #d9f2d1; -fx-text-fill: #38830a; -fx-font-weight: bold;");
                            }

                            setText(item);
                        }
                    }
                };
            }
        });
    }
}
