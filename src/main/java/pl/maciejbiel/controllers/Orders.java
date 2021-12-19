package pl.maciejbiel.controllers;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.maciejbiel.App;
import pl.maciejbiel.entities.Produkty;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.entities.Zamowienia;
import pl.maciejbiel.service.ZamowieniaService;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Orders implements Initializable {
    private final ZamowieniaService ordersService = new ZamowieniaService();
    private ObservableList<Zamowienia> ordersList = FXCollections.observableArrayList();
    private final static int rowsPerPage = 12;

    @FXML
    public Button Orders_button_back;
    public TextField Orders_input_search;
    public Button Orders_button_search;
    public Button Orders_button_addNew;
    public Button Orders_button_clear;
    public Pagination Orders_pagination;
    public TableView<Zamowienia> Orders_table;
    public TableColumn<Zamowienia, String> Orders_table_col_id;
    public TableColumn<Zamowienia, String> Orders_table_col_email;
    public TableColumn<Zamowienia, String> Orders_table_col_payment;
    public TableColumn<Zamowienia, String> Orders_table_col_price;
    public TableColumn<Zamowienia, String> Orders_table_col_status;
    public TableColumn<Zamowienia, String> Orders_table_col_isFinished;
    public TableColumn<Zamowienia, String> Orders_table_colDetails;
    public TableColumn<Zamowienia, String> Orders_table_colDelete;
    public Label Orders_label_searchResults;
    public Label Orders_label_pageNumber;

    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == Orders_button_back) {
            App.setRoot("Home");
        }
        if (mouseEvent.getSource() == Orders_button_addNew) {
            openAddOrders();
        }
        if (mouseEvent.getSource() == Orders_button_search) {
            searchTable();
        }
        if (mouseEvent.getSource() == Orders_button_clear) {
            clearSearchResults();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tabela zaczytanie danych
        setupTableView(getOrdersList());

        // Paginacja
        Orders_pagination.setPageCount(getTablePagesCount());
        Orders_pagination.setPageFactory(this::createPage);

        //Labele na dole
        setLabelPageNumber(Orders_pagination.getCurrentPageIndex());
        setLabelResultsNumber();

        Orders_button_clear.setVisible(false);
    }

    public void setupTableView(ObservableList<Zamowienia> orders){
        Orders_table.getItems().clear();
        Orders_table.setItems(orders);
        Orders_table_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Orders_table_col_email.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getUzytkownicy().getEmail())));
        Orders_table_col_payment.setCellValueFactory(new PropertyValueFactory<>("forma_platnosci"));
        Orders_table_col_price.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKwota() + " zł"));
        Orders_table_col_status.setCellValueFactory(new PropertyValueFactory<>("status_zamowienia"));
        Orders_table_col_status.setCellFactory(new Callback<>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Zamowienia, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.contains("zakończono")) {
                                this.setStyle("-fx-alignment: CENTER; -fx-background-color: #d9f2d1; -fx-text-fill: #38830a; -fx-font-weight: bold;");
                            }
                            else  {
                                this.setStyle("-fx-alignment: CENTER; -fx-background-color: #e1edf3; -fx-text-fill: #347ab0;" );
                            }
                            setText(item);
                        }
                        else {
                            setStyle("");
                            setText("");
                        }
                    }
                };
            }
        });

        Orders_table_col_isFinished.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isCzy_zrealizowano() ? "Tak" : "Nie"));
        Orders_table_col_isFinished.setCellFactory(new Callback<>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Zamowienia, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        setStyle(null);
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.contains("Nie")) {
                                setStyle("-fx-alignment: CENTER; -fx-background-color: #f3e1e2; -fx-text-fill: #bc0404; -fx-font-weight: bold;" );

                            }
                            if (item.contains("Tak")) {
                                setStyle("-fx-alignment: CENTER; -fx-background-color: #d9f2d1; -fx-text-fill: #38830a; -fx-font-weight: bold;");
                            }

                            setText(item);
                        }
                        else {
                            setStyle("");
                            setText("");
                        }
                    }
                };
            }
        });

        Callback<TableColumn<Zamowienia, String>, TableCell<Zamowienia, String>> colEditCellFactory = new Callback<>() {
            @Override
            public TableCell<Zamowienia, String> call(final TableColumn<Zamowienia, String> param) {
                final TableCell<Zamowienia, String> cell = new TableCell<>() {

                    private final Button btn = new Button("Wyświetl");

                    {
                        btn.getStyleClass().add("Entity_table_column_editButton");
                        btn.setOnAction((ActionEvent event) -> {
                            Zamowienia order = getTableView().getItems().get(getIndex());
                            System.out.println("Details: " + order);
                            openOrderDetails(order);
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
        Orders_table_colDetails.setCellFactory(colEditCellFactory);

        Callback<TableColumn<Zamowienia, String>, TableCell<Zamowienia, String>> colDelCellFactory = new Callback<>() {
            @Override
            public TableCell<Zamowienia, String> call(final TableColumn<Zamowienia, String> param) {
                final TableCell<Zamowienia, String> cell = new TableCell<>() {

                    private final Button btn = new Button("Usuń");

                    {
                        btn.getStyleClass().add("Entity_table_column_deleteButton");
                        btn.setOnAction((ActionEvent event) -> {
                            Zamowienia order = getTableView().getItems().get(getIndex());
                            order.setUsuniete(true);
                            System.out.println("Delete: " + order);
                            ordersService.deleteOrder(order);
                            getOrdersList();
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
        Orders_table_colDelete.setCellFactory(colDelCellFactory);
    }

    public ObservableList<Zamowienia> getOrdersList(){
        if(!ordersList.isEmpty()) {
            ordersList.clear();
        }
        ordersList = FXCollections.observableList(ordersService.listOrders());
        return ordersList;
    }

    private void openAddOrders() {
        try {
            Stage newWindow = new Stage();

            FXMLLoader loader = App.loadFXML("OrdersAdd");
            Parent root = loader.load();

            App.setTitle(newWindow, "Złóż Zamówienie");
            App.setIcon(newWindow);
            Scene scene = new Scene(root);
            App.setCss(scene, "main");

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();

            newWindow.setOnCloseRequest(we -> {
                getOrdersList();
                fetchTable();
                Orders_pagination.setCurrentPageIndex(Orders_pagination.getPageCount() - 1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openOrderDetails(Zamowienia order) {
        try {
            Stage newWindow = new Stage();

            FXMLLoader loader = App.loadFXML("OrdersDetails");
            Parent root = loader.load();

            pl.maciejbiel.controllers.OrdersDetails ordersDetailsController = loader.getController();
            ordersDetailsController.setOrder(order);
            ordersDetailsController.initController();

            App.setTitle(newWindow, "Szczegóły Zamówienia");
            App.setIcon(newWindow);
            Scene scene = new Scene(root);
            App.setCss(scene, "main");

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();

            newWindow.setOnCloseRequest(we -> {
                getOrdersList();
                fetchTable();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void searchTable() {
        String value = Orders_input_search.getText().toLowerCase();

        if(value.equals("") || value.isEmpty()) {
            return;
        }

        ObservableList<Zamowienia> ordersUsersList = FXCollections.observableArrayList();
        for (Zamowienia z : ordersList) {
            if (z.getForma_platnosci().toLowerCase().contains(value)
            || z.getStatus_zamowienia().toLowerCase().contains(value)
            || z.getUzytkownicy().getEmail().toLowerCase().contains(value)
            || String.valueOf(z.getKwota()).toLowerCase().contains(value)) {
                ordersUsersList.add(z);
            }
        }
        ordersList = ordersUsersList;
        displaySearchToTable();
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, ordersList.size());
        Orders_table.setItems(FXCollections.observableArrayList(ordersList.subList(fromIndex, toIndex)));
        // Refresh tabeli bo przy zmianie strony sie buttony nie refreshowaly
        Orders_table.getColumns().get(0).setVisible(false);
        Orders_table.getColumns().get(0).setVisible(true);
        // Ustawienie labelu ze stronami
        setLabelPageNumber(pageIndex);
        return new StackPane(Orders_table);
    }

    public void fetchTable() {
        int pageIndex = Orders_pagination.getCurrentPageIndex();
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, ordersList.size());
        Orders_table.setItems(FXCollections.observableArrayList(ordersList.subList(fromIndex, toIndex)));
        Orders_pagination.setPageCount(getTablePagesCount());
        setLabelResultsNumber();
    }

    private void displaySearchToTable() {
        int toIndex = Math.min(rowsPerPage, ordersList.size());
        Orders_table.setItems(FXCollections.observableArrayList(ordersList.subList(0, toIndex)));
        Orders_pagination.setPageCount(getTablePagesCount());
        setLabelResultsNumber();
        Orders_button_clear.setVisible(true);
        Orders_button_search.setVisible(false);
        Orders_pagination.setCurrentPageIndex(0);
        Orders_table.getColumns().get(0).setVisible(false);
        Orders_table.getColumns().get(0).setVisible(true);
    }

    private void clearSearchResults() {
        Orders_button_clear.setVisible(false);
        Orders_button_search.setVisible(true);
        Orders_input_search.setText("");
        getOrdersList();
        fetchTable();
    }

    private int getTablePagesCount() {
        return ordersList.size() / rowsPerPage + 1;
    }

    private void setLabelPageNumber(int page) {
        Orders_label_pageNumber.setText("Strona: " + (page + 1));
    }

    private void setLabelResultsNumber() {
        Orders_label_searchResults.setText("Wyniki: " + ordersList.size());
    }
}
