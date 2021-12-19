package pl.maciejbiel.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.maciejbiel.App;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.service.ProduktyService;
import pl.maciejbiel.utils.ProductsCart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Products implements Initializable {
    private final ProduktyService productsService = new ProduktyService();
    private ObservableList<Produkty> productsList = FXCollections.observableArrayList();
    private final static int rowsPerPage = 12;

    @FXML
    public Pagination Products_pagination;
    public Button Products_button_back;
    public TextField Products_input_search;
    public Button Products_button_search;
    public Button Products_button_addNew;
    public Button Products_button_clear;
//    public ChoiceBox<String> Products_select_search;
    public Label Products_label_searchResults;
    public Label Products_label_pageNumber;

    @FXML
    public TableView<Produkty> Products_table = new TableView<>();
    public TableColumn<Produkty, Long> Products_table_col_id;
    public TableColumn<Produkty, String> Products_table_col_name;
    public TableColumn<Produkty, String> Products_table_col_code;
    public TableColumn<Produkty, String> Products_table_col_price;
    public TableColumn<Produkty, String> Products_table_col_desc;
    public TableColumn<Produkty, String> Products_table_col_category;
    public TableColumn<Produkty, String> Products_table_colEdit;
    public TableColumn<Produkty, String> Products_table_colDelete;

    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == Products_button_back) {
            App.setRoot("Home");
        }
        if (mouseEvent.getSource() == Products_button_addNew) {
            openAddProducts();
        }
        if (mouseEvent.getSource() == Products_button_search) {
            searchTable();
        }
        if (mouseEvent.getSource() == Products_button_clear) {
            clearSearchResults();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tabela zaczytanie danych
        setupTableView(getProductsList());

        // Paginacja
        Products_pagination.setPageCount(getTablePagesCount());
        Products_pagination.setPageFactory(this::createPage);

        //Labele na dole
        setLabelPageNumber(Products_pagination.getCurrentPageIndex());
        setLabelResultsNumber();

        Products_button_clear.setVisible(false);
    }

    public void setupTableView(ObservableList<Produkty> products){
        Products_table.getItems().clear();
        Products_table.setItems(products);
        Products_table_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Products_table_col_name.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        Products_table_col_code.setCellValueFactory(new PropertyValueFactory<>("kod_producenta"));
        Products_table_col_price.setCellValueFactory(new PropertyValueFactory<>("cena"));
        Products_table_col_desc.setCellValueFactory(new PropertyValueFactory<>("opis"));
        Products_table_col_category.setCellValueFactory(new PropertyValueFactory<>("kategoria"));

        Callback<TableColumn<Produkty, String>, TableCell<Produkty, String>> colEditCellFactory = new Callback<>() {
            @Override
            public TableCell<Produkty, String> call(final TableColumn<Produkty, String> param) {
                final TableCell<Produkty, String> cell = new TableCell<>() {

                    private final Button btn = new Button("Edytuj");

                    {
                        btn.getStyleClass().add("Entity_table_column_editButton");
                        btn.setOnAction((ActionEvent event) -> {
                            Produkty product = getTableView().getItems().get(getIndex());
                            System.out.println("Edit: " + product);
                            openEditProducts(product);
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        Products_table_colEdit.setCellFactory(colEditCellFactory);

        Callback<TableColumn<Produkty, String>, TableCell<Produkty, String>> colDelCellFactory = new Callback<>() {
            @Override
            public TableCell<Produkty, String> call(final TableColumn<Produkty, String> param) {
                final TableCell<Produkty, String> cell = new TableCell<>() {

                    private final Button btn = new Button("Usuń");

                    {
                        btn.getStyleClass().add("Entity_table_column_deleteButton");
                        btn.setOnAction((ActionEvent event) -> {
                            Produkty product = getTableView().getItems().get(getIndex());
                            System.out.println("Delete: " + product);
                            productsService.deleteProduct(product.getId());
                            getProductsList();
                            fetchTable();
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };

                return cell;
            }
        };
        Products_table_colDelete.setCellFactory(colDelCellFactory);
    }

    public ObservableList<Produkty> getProductsList(){
        if(!productsList.isEmpty()) {
            productsList.clear();
        }
        productsList = FXCollections.observableList(productsService.listProducts());
        return productsList;
    }

    private void openAddProducts() {
        try {
            Stage newWindow = new Stage();

            FXMLLoader loader = App.loadFXML("ProductsAdd");
            Parent root = loader.load();

            pl.maciejbiel.controllers.ProductsAdd productsAddController = loader.getController();
            productsAddController.setActionType("ADD");
            productsAddController.initController();

            App.setTitle(newWindow, "Dodaj Użytkownika");
            App.setIcon(newWindow);
            Scene scene = new Scene(root);
            App.setCss(scene, "main");

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();

            newWindow.setOnCloseRequest(we -> {
                getProductsList();
                fetchTable();
                Products_pagination.setCurrentPageIndex(Products_pagination.getPageCount() - 1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEditProducts(Produkty product) {
        try {
            Stage newWindow = new Stage();

            FXMLLoader loader = App.loadFXML("ProductsAdd");
            Parent root = loader.load();

            pl.maciejbiel.controllers.ProductsAdd productsAddController = loader.getController();
            productsAddController.setActionType("UPDATE");
            productsAddController.setProduct(product);
            productsAddController.initController();

            App.setTitle(newWindow, "Edytuj Użytkownika");
            App.setIcon(newWindow);
            Scene scene = new Scene(root);
            App.setCss(scene, "main");

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();

            newWindow.setOnCloseRequest(we -> {
                getProductsList();
                fetchTable();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchTable() {
        String value = Products_input_search.getText().toLowerCase();

        if(value.equals("") || value.isEmpty()) {
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
        displaySearchToTable();
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, productsList.size());
        Products_table.setItems(FXCollections.observableArrayList(productsList.subList(fromIndex, toIndex)));
        // Refresh tabeli bo przy zmianie strony sie buttony nie refreshowaly
        Products_table.getColumns().get(0).setVisible(false);
        Products_table.getColumns().get(0).setVisible(true);
        // Ustawienie labelu ze stronami
        setLabelPageNumber(pageIndex);
        return new StackPane(Products_table);
    }

    public void fetchTable() {
        int pageIndex = Products_pagination.getCurrentPageIndex();
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, productsList.size());
        Products_table.setItems(FXCollections.observableArrayList(productsList.subList(fromIndex, toIndex)));
        Products_pagination.setPageCount(getTablePagesCount());
        setLabelResultsNumber();
    }

    private void displaySearchToTable() {
        int toIndex = Math.min(rowsPerPage, productsList.size());
        Products_table.setItems(FXCollections.observableArrayList(productsList.subList(0, toIndex)));
        Products_pagination.setPageCount(getTablePagesCount());
        setLabelResultsNumber();
        Products_button_clear.setVisible(true);
        Products_button_search.setVisible(false);
        Products_pagination.setCurrentPageIndex(0);
        Products_table.getColumns().get(0).setVisible(false);
        Products_table.getColumns().get(0).setVisible(true);
    }

    private void clearSearchResults() {
        Products_button_clear.setVisible(false);
        Products_button_search.setVisible(true);
        Products_input_search.setText("");
        getProductsList();
        fetchTable();
    }

    private int getTablePagesCount() {
        return productsList.size() / rowsPerPage + 1;
    }

    private void setLabelPageNumber(int page) {
        Products_label_pageNumber.setText("Strona: " + (page + 1));
    }

    private void setLabelResultsNumber() {
        Products_label_searchResults.setText("Wyniki: " + productsList.size());
    }

}