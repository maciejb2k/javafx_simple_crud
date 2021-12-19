package pl.maciejbiel.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.entities.Zamowienia;
import pl.maciejbiel.service.ProduktyService;
import pl.maciejbiel.service.UzytkownicyService;
import pl.maciejbiel.service.ZamowieniaService;
import pl.maciejbiel.utils.ProductsCart;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class OrdersAdd implements Initializable {
    private final UzytkownicyService usersService = new UzytkownicyService();
    private final ProduktyService productsService = new ProduktyService();
    private final ZamowieniaService ordersService = new ZamowieniaService();

    private ObservableList<Uzytkownicy> usersList = FXCollections.observableArrayList();
    private ObservableList<Produkty> productsList = FXCollections.observableArrayList();
    private final ObservableList<String> cartItems = FXCollections.observableArrayList();
    private final ArrayList<ProductsCart> cartObjects = new ArrayList<>();
    private BigDecimal overallPrice = new BigDecimal("0.00");
    private Uzytkownicy user;

    @FXML
    public TableView<Produkty> OrdersAdd_table_products;
    public TextField OrdersAdd_input_search;
    public Button OrdersAdd_button_search;
    public ListView<String> OrdersAdd_cart;
    public Label OrdersAdd_text_price;
    public ChoiceBox<String> OrdersAdd_select_payment;
    public Button OrdersAdd_button_submit;
    public Button OrdersAdd_button_cartDelete;
    public TableColumn<Produkty, String> OrdersAdd_col_name;
    public TableColumn<Produkty, String> OrdersAdd_col_code;
    public TableColumn<Produkty, String> OrdersAdd_col_price;
    public Button OrdersAdd_button_addProduct;
    public TextField OrdersAdd_input_quantity;
    public Label OrdersAdd_label_selectedUser;
    public Button OrdersAdd_button_clear;
    public TextField OrdersAdd_input_searchUser;
    public Button OrdersAdd_button_searchUser;
    public Button OrdersAdd_button_clearUser;
    public TableView<Uzytkownicy> OrdersAdd_table_users;
    public TableColumn<Uzytkownicy, String> OrdersAdd_col_email;
    public TableColumn<Uzytkownicy, String> OrdersAdd_col_username;
    public Text OrdersAdd_text_message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupProductsTableView(getProductsList());
        setupUsersTableView(getUsersList());

        OrdersAdd_text_price.setText("0.00 zł");
        OrdersAdd_cart.setItems(cartItems);
        OrdersAdd_input_quantity.setText("1");
        setSelectedUser("");

        OrdersAdd_button_clear.setVisible(false);
        OrdersAdd_button_clearUser.setVisible(false);

        OrdersAdd_table_users.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Uzytkownicy selectedUser = OrdersAdd_table_users.getSelectionModel().getSelectedItem();
                setSelectedUser(selectedUser.getEmail());
                user = selectedUser;
            }
        });
    }

    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == OrdersAdd_button_addProduct) {
            addProductToCart();
        }
        if (mouseEvent.getSource() == OrdersAdd_button_cartDelete) {
            deleteProductFromCart();
        }
        if (mouseEvent.getSource() == OrdersAdd_button_submit) {
            placeOrder();
        }
        if (mouseEvent.getSource() == OrdersAdd_button_search) {
            searchProductsTable();
        }
        if (mouseEvent.getSource() == OrdersAdd_button_clear) {
            clearProductsSearchResults();
        }
        if (mouseEvent.getSource() == OrdersAdd_button_searchUser) {
            searchUsersTable();
        }
        if (mouseEvent.getSource() == OrdersAdd_button_clearUser) {
            clearUsersSearchResults();
        }
    }

    public ObservableList<Produkty> getProductsList() {
        if (!productsList.isEmpty()) {
            productsList.clear();
        }
        productsList = FXCollections.observableList(productsService.listProducts());
        return productsList;
    }

    public ObservableList<Uzytkownicy> getUsersList() {
        if (!usersList.isEmpty()) {
            usersList.clear();
        }
        usersList = FXCollections.observableList(usersService.listUser());
        return usersList;
    }

    public void setupProductsTableView(ObservableList<Produkty> products) {
        OrdersAdd_table_products.getItems().clear();
        OrdersAdd_table_products.setItems(products);
        OrdersAdd_col_name.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        OrdersAdd_col_code.setCellValueFactory(new PropertyValueFactory<>("kod_producenta"));
        OrdersAdd_col_price.setCellValueFactory(new PropertyValueFactory<>("cena"));
    }

    public void setupUsersTableView(ObservableList<Uzytkownicy> users) {
        OrdersAdd_table_users.getItems().clear();
        OrdersAdd_table_users.setItems(users);
        OrdersAdd_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        OrdersAdd_col_username.setCellValueFactory(new PropertyValueFactory<>("nazwa_uzytkownika"));
    }

    public void addProductToCart() {
        Produkty selectedProduct = OrdersAdd_table_products.getSelectionModel().getSelectedItem();
        String quantityValue = OrdersAdd_input_quantity.getText();
        if (OrdersAdd_table_products.getSelectionModel().isEmpty()) {
            setMessageText("error", "Wybierz produkt");
            return;
        }

        if (quantityValue.equals("") || quantityValue.isEmpty()) {
            setMessageText("error", "Podaj ilość");
            return;
        }

        if (checkIfAlreadyInCart(selectedProduct.getKod_producenta())) {
            setMessageText("error", "Taki produkt jest już w koszyku");
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(quantityValue);
        } catch (NumberFormatException e) {
            setMessageText("error", "Podaj poprawną ilość");
            return;
        }

        BigDecimal bigDecimalPrice = BigDecimal.valueOf(selectedProduct.getCena());
        BigDecimal bigDecimalQuantity = BigDecimal.valueOf(quantity);
        BigDecimal bigDecimalOverallPrice = bigDecimalPrice.multiply(bigDecimalQuantity);
        cartItems.add(quantity + "x - " + bigDecimalOverallPrice.toString() + " zł - " + selectedProduct.getNazwa());
        cartObjects.add(new ProductsCart(selectedProduct, quantity));
        setOverallPrice(bigDecimalOverallPrice);
    }

    public void deleteProductFromCart() {
        if (!cartItems.isEmpty() && !OrdersAdd_cart.getSelectionModel().isEmpty()) {
            int index = OrdersAdd_cart.getSelectionModel().getSelectedIndex();
            BigDecimal priceToSubstract = BigDecimal.valueOf(cartObjects.get(index).getQuantity()).multiply(BigDecimal.valueOf(cartObjects.get(index).getProduct().getCena()));
            setOverallPriceAfterCartRemove(priceToSubstract);
            cartItems.remove(index);
            cartObjects.remove(index);
        }
    }

    public void setOverallPrice(BigDecimal newPrice) {
        Stream<BigDecimal> bigDecimalOverallPrice =
                Stream.of(overallPrice, newPrice);
        overallPrice = bigDecimalOverallPrice.reduce(BigDecimal.ZERO, BigDecimal::add);
        OrdersAdd_text_price.setText(overallPrice + " zł");
        System.out.println(overallPrice);
    }

    public void setOverallPriceAfterCartRemove(BigDecimal newPrice) {
        overallPrice = overallPrice.subtract(newPrice);
        OrdersAdd_text_price.setText(overallPrice + " zł");
        System.out.println(overallPrice);
    }

    public boolean checkIfAlreadyInCart(String code) {
        for (ProductsCart p : cartObjects) {
            if (p.getProduct().getKod_producenta().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public void setSelectedUser(String user) {
        OrdersAdd_label_selectedUser.setText("Wybrano: " + user);
    }

    public void placeOrder() {
        String payment = OrdersAdd_select_payment.getValue();

        if (cartObjects.isEmpty()) {
            setMessageText("error", "Zamówienie nie może być puste");
            return;
        }

        if (user == null) {
            setMessageText("error", "Wybierz użytkownika");
            return;
        }

        Zamowienia order = new Zamowienia(
            user,
            payment,
            new Timestamp(System.currentTimeMillis()),
            "złożone",
            false,
            overallPrice.doubleValue(),
            false
        );

        ordersService.addOrder(order, cartObjects);
        setMessageText("success", "Poprawnie złożono zamówienie. Złóż kolejne");
        clearForm();
    }

    private void searchProductsTable() {
        String value = OrdersAdd_input_search.getText().toLowerCase();

        if (value.equals("") || value.isEmpty()) {
            setMessageText("error", "Podaj nazwę produktu do wyszukania");
            return;
        }

        ObservableList<Produkty> searchProductsList = FXCollections.observableArrayList();
        for (Produkty p : productsList) {
            if (p.getNazwa().toLowerCase().contains(value)
                    || p.getKategoria().toLowerCase().contains(value)
                    || p.getKod_producenta().toLowerCase().contains(value)
                    || p.getOpis().toLowerCase().contains(value)) {
                searchProductsList.add(p);
            }
        }

        productsList = searchProductsList;
        OrdersAdd_table_products.setItems(productsList);
        OrdersAdd_button_clear.setVisible(true);
        OrdersAdd_button_search.setVisible(false);
    }

    private void clearProductsSearchResults() {
        OrdersAdd_button_clear.setVisible(false);
        OrdersAdd_button_search.setVisible(true);
        OrdersAdd_input_search.setText("");
        getProductsList();
        OrdersAdd_table_products.setItems(FXCollections.observableArrayList(productsList));
    }


    private void searchUsersTable() {
        String value = OrdersAdd_input_searchUser.getText().toLowerCase();

        if (value.equals("") || value.isEmpty()) {
            setMessageText("error", "Podaj uzytkownika do wyszukania");
            return;
        }

        ObservableList<Uzytkownicy> searchUsersList = FXCollections.observableArrayList();
        for (Uzytkownicy u : usersList) {
            if (u.getEmail().toLowerCase().contains(value)
                    || u.getNazwa_uzytkownika().toLowerCase().contains(value)) {
                searchUsersList.add(u);
            }
        }

        usersList = searchUsersList;
        OrdersAdd_table_users.setItems(usersList);
        OrdersAdd_button_clearUser.setVisible(true);
        OrdersAdd_button_searchUser.setVisible(false);
    }

    private void clearUsersSearchResults() {
        OrdersAdd_button_clearUser.setVisible(false);
        OrdersAdd_button_searchUser.setVisible(true);
        OrdersAdd_input_searchUser.setText("");
        getUsersList();
        OrdersAdd_table_users.setItems(FXCollections.observableArrayList(usersList));
    }

    private void clearForm() {
        OrdersAdd_input_search.setText("");
        OrdersAdd_input_searchUser.setText("");
        OrdersAdd_input_quantity.setText("1");
        cartItems.clear();
        cartObjects.clear();
        user = null;
        overallPrice = new BigDecimal("0.00");
        OrdersAdd_text_price.setText("0.00 zł");
    }

    public void setMessageText(String type, String text) {
        if (type.equals("error")) {
            OrdersAdd_text_message.setStyle("-fx-text-fill: #bc0404; -fx-font-weight: bold;");
            OrdersAdd_text_message.setText("Błąd - " + text);
        } else if (type.equals("success")) {
            OrdersAdd_text_message.setStyle("-fx-text-fill: #38830a; -fx-font-weight: bold;");
            OrdersAdd_text_message.setText("Sukces - " + text);
        } else {
            OrdersAdd_text_message.setStyle("-fx-font-weight: bold;");
            OrdersAdd_text_message.setText(text);
        }
    }
}
