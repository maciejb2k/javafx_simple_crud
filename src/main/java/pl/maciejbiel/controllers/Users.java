package pl.maciejbiel.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import pl.maciejbiel.App;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.service.UzytkownicyService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Users implements Initializable {
    private final UzytkownicyService usersService = new UzytkownicyService();
    private ObservableList<Uzytkownicy> usersList = FXCollections.observableArrayList();
    private final static int rowsPerPage = 12;

    @FXML
    public Button Users_button_back;
    public Button Users_button_addNew;
    public TextField Users_input_search;
    public Button Users_button_search;
    public Label Users_label_searchResults;
    public Label Users_label_pageNumber;
    public Pagination Users_pagination;
//    public ChoiceBox<String> Users_select_search;
    public Button Users_button_clear;

    @FXML
    public TableView<Uzytkownicy> Users_table = new TableView<>();
    public TableColumn<Uzytkownicy, Long> Users_table_col_id;
    public TableColumn<Uzytkownicy, String> Users_table_col_fname;
    public TableColumn<Uzytkownicy, String> Users_table_col_lname;
    public TableColumn<Uzytkownicy, String> Users_table_col_username;
    public TableColumn<Uzytkownicy, String> Users_table_col_email;
    public TableColumn<Uzytkownicy, String> Users_table_col_phone;
    public TableColumn<Uzytkownicy, String> Users_table_col_password;
    public TableColumn<Uzytkownicy, String> Users_table_colEdit;
    public TableColumn<Uzytkownicy, String> Users_table_colDelete;

    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == Users_button_back) {
            App.setRoot("Home");
        }
        if (mouseEvent.getSource() == Users_button_addNew) {
            openAddUsers();
        }
        if (mouseEvent.getSource() == Users_button_search) {
            searchTable();
        }
        if (mouseEvent.getSource() == Users_button_clear) {
            clearSearchResults();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tabela zaczytanie danych
        setupTableView(getUsersList());

        // Paginacja
        Users_pagination.setPageCount(getTablePagesCount());
        Users_pagination.setPageFactory(this::createPage);

        //Labele na dole
        setLabelPageNumber(Users_pagination.getCurrentPageIndex());
        setLabelResultsNumber();

        Users_button_clear.setVisible(false);
    }

    public void setupTableView(ObservableList<Uzytkownicy> users) {
        Users_table.getItems().clear();
        Users_table.setItems(users);
        Users_table_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Users_table_col_fname.setCellValueFactory(new PropertyValueFactory<>("imie"));
        Users_table_col_lname.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        Users_table_col_username.setCellValueFactory(new PropertyValueFactory<>("nazwa_uzytkownika"));
        Users_table_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Users_table_col_phone.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        Users_table_col_password.setCellValueFactory(new PropertyValueFactory<>("haslo"));

        Callback<TableColumn<Uzytkownicy, String>, TableCell<Uzytkownicy, String>> colEditCellFactory = new Callback<>() {
            @Override
            public TableCell<Uzytkownicy, String> call(final TableColumn<Uzytkownicy, String> param) {
                final TableCell<Uzytkownicy, String> cell = new TableCell<>() {

                    private final Button btn = new Button("Edytuj");

                    {
                        btn.getStyleClass().add("Entity_table_column_editButton");
                        btn.setOnAction((ActionEvent event) -> {
                            Uzytkownicy user = getTableView().getItems().get(getIndex());
                            System.out.println("Edit: " + user);
                            openEditUsers(user);
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
        Users_table_colEdit.setCellFactory(colEditCellFactory);

        Callback<TableColumn<Uzytkownicy, String>, TableCell<Uzytkownicy, String>> colDelCellFactory = new Callback<>() {
            @Override
            public TableCell<Uzytkownicy, String> call(final TableColumn<Uzytkownicy, String> param) {
                final TableCell<Uzytkownicy, String> cell = new TableCell<>() {

                    private final Button btn = new Button("Usuń");

                    {
                        btn.getStyleClass().add("Entity_table_column_deleteButton");
                        btn.setOnAction((ActionEvent event) -> {
                            Uzytkownicy user = getTableView().getItems().get(getIndex());
                            System.out.println("Delete: " + user);
                            usersService.deleteUser(user.getId());
                            getUsersList();
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
        Users_table_colDelete.setCellFactory(colDelCellFactory);
    }

    public ObservableList<Uzytkownicy> getUsersList() {
        if (!usersList.isEmpty()) {
            usersList.clear();
        }
        usersList = FXCollections.observableList(usersService.listUser());
        return usersList;
    }

    private void openAddUsers() {
        try {
            Stage newWindow = new Stage();

            FXMLLoader loader = App.loadFXML("UsersAdd");
            Parent root = loader.load();

            pl.maciejbiel.controllers.UsersAdd usersAddController = loader.getController();
            usersAddController.setActionType("ADD");
            usersAddController.initController();

            App.setTitle(newWindow, "Dodaj Użytkownika");
            App.setIcon(newWindow);
            Scene scene = new Scene(root);
            App.setCss(scene, "main");

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();

            newWindow.setOnCloseRequest(we -> {
                getUsersList();
                fetchTable();
                Users_pagination.setCurrentPageIndex(Users_pagination.getPageCount() - 1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEditUsers(Uzytkownicy user) {
        try {
            Stage newWindow = new Stage();

            FXMLLoader loader = App.loadFXML("UsersAdd");
            Parent root = loader.load();

            pl.maciejbiel.controllers.UsersAdd usersAddController = loader.getController();
            usersAddController.setActionType("UPDATE");
            usersAddController.setUser(user);
            usersAddController.initController();

            App.setTitle(newWindow, "Edytuj Użytkownika");
            App.setIcon(newWindow);
            Scene scene = new Scene(root);
            App.setCss(scene, "main");

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();

            newWindow.setOnCloseRequest(we -> {
                getUsersList();
                fetchTable();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchTable() {
        String value = Users_input_search.getText().toLowerCase();

        if (value.equals("") || value.isEmpty()) {
            return;
        }

        ObservableList<Uzytkownicy> searchUsersList = FXCollections.observableArrayList();
        for (Uzytkownicy u : usersList) {
            if (u.getImie().toLowerCase().contains(value)
            || u.getNazwisko().toLowerCase().contains(value)
            || u.getEmail().toLowerCase().contains(value)
            || u.getTelefon().toLowerCase().contains(value)
            || u.getNazwa_uzytkownika().toLowerCase().contains(value)
            || u.getHaslo().toLowerCase().contains(value)) {
                searchUsersList.add(u);
            }
        }
        usersList = searchUsersList;
        displaySearchToTable();
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, usersList.size());
        Users_table.setItems(FXCollections.observableArrayList(usersList.subList(fromIndex, toIndex)));
        // Refresh tabeli bo przy zmianie strony sie buttony nie refreshowaly
        Users_table.getColumns().get(0).setVisible(false);
        Users_table.getColumns().get(0).setVisible(true);
        // Ustawienie labelu ze stronami
        setLabelPageNumber(pageIndex);
        return new StackPane(Users_table);
    }

    public void fetchTable() {
        int pageIndex = Users_pagination.getCurrentPageIndex();
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, usersList.size());
        Users_table.setItems(FXCollections.observableArrayList(usersList.subList(fromIndex, toIndex)));
        Users_pagination.setPageCount(getTablePagesCount());
        setLabelResultsNumber();
    }

    private void displaySearchToTable() {
        int toIndex = Math.min(rowsPerPage, usersList.size());
        Users_table.setItems(FXCollections.observableArrayList(usersList.subList(0, toIndex)));
        Users_pagination.setPageCount(getTablePagesCount());
        setLabelResultsNumber();
        Users_button_clear.setVisible(true);
        Users_button_search.setVisible(false);
        Users_pagination.setCurrentPageIndex(0);
        Users_table.getColumns().get(0).setVisible(false);
        Users_table.getColumns().get(0).setVisible(true);
    }

    private void clearSearchResults() {
        Users_button_clear.setVisible(false);
        Users_button_search.setVisible(true);
        Users_input_search.setText("");
        getUsersList();
        fetchTable();
    }

    private int getTablePagesCount() {
        return usersList.size() / rowsPerPage + 1;
    }

    private void setLabelPageNumber(int page) {
        Users_label_pageNumber.setText("Strona: " + (page + 1));
    }

    private void setLabelResultsNumber() {
        Users_label_searchResults.setText("Wyniki: " + usersList.size());
    }
}
