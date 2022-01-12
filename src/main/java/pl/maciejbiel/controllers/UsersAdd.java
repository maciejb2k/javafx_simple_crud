package pl.maciejbiel.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToUrl;
import pl.maciejbiel.App;
import pl.maciejbiel.entities.Adresy;
import pl.maciejbiel.entities.Uzytkownicy;
import pl.maciejbiel.service.UzytkownicyService;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UsersAdd implements Initializable{
    private final UzytkownicyService usersService = new UzytkownicyService();

    private String actionType;
    private Uzytkownicy user;

    @FXML
    public Label UsersAdd_label_title;
    public TextField UsersAdd_input_fname;
    public TextField UsersAdd_input_lname;
    public TextField UsersAdd_input_username;
    public TextField UsersAdd_input_email;
    public TextField UsersAdd_input_phone;
    public TextField UsersAdd_input_password;
    public TextField UsersAdd_input_street;
    public TextField UsersAdd_input_flatnum;
    public TextField UsersAdd_input_city;
    public TextField UsersAdd_input_zipcode;
    public TextField UsersAdd_input_country;
    public Button UsersAdd_button_add;
    public Text UsersAdd_text_form;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void initController() throws Exception {
        if(actionType.equals("ADD") && user == null) {
            UsersAdd_label_title.setText("Dodaj Użytkownika");
            UsersAdd_button_add.setText("Dodaj");
        }
        else if(actionType.equals("UPDATE") && user != null){
            UsersAdd_label_title.setText("Edytuj Użytkownika");
            UsersAdd_button_add.setText("Edytuj");
            fillForm();
        }
        else {
            throw new Exception("Controller:UserAdd - initController() failed");
        }
    }

    public void handleButtonClicks(javafx.event.ActionEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == UsersAdd_button_add) {
            handleFormSubmit();
        }
    }

    public void fillForm() {
        UsersAdd_input_fname.setText(user.getImie());
        UsersAdd_input_lname.setText(user.getNazwisko());
        UsersAdd_input_username.setText(user.getNazwa_uzytkownika());
        UsersAdd_input_email.setText(user.getEmail());
        UsersAdd_input_phone.setText(user.getTelefon());
        UsersAdd_input_password.setText(user.getHaslo());
        UsersAdd_input_street.setText(user.getAdresy().getUlica());
        UsersAdd_input_flatnum.setText(user.getAdresy().getNr_mieszkania());
        UsersAdd_input_city.setText(user.getAdresy().getMiasto());
        UsersAdd_input_zipcode.setText(user.getAdresy().getKod_pocztowy());
        UsersAdd_input_country.setText(user.getAdresy().getKraj());
    }

    public void clearForm() {
        UsersAdd_input_fname.clear();
        UsersAdd_input_lname.clear();
        UsersAdd_input_username.clear();
        UsersAdd_input_email.clear();
        UsersAdd_input_phone.clear();
        UsersAdd_input_password.clear();
        UsersAdd_input_street.clear();
        UsersAdd_input_flatnum.clear();
        UsersAdd_input_city.clear();
        UsersAdd_input_zipcode.clear();
        UsersAdd_input_country.clear();
    }

    public void handleFormSubmit() throws IOException {
        String fname = UsersAdd_input_fname.getText();
        String lname = UsersAdd_input_lname.getText();
        String username = UsersAdd_input_username.getText();
        String email = UsersAdd_input_email.getText().toLowerCase();
        String phone = UsersAdd_input_phone.getText();
        String password = UsersAdd_input_password.getText();
        String street = UsersAdd_input_street.getText();
        String flatnum = UsersAdd_input_flatnum.getText();
        String city = UsersAdd_input_city.getText();
        String zipcode = UsersAdd_input_zipcode.getText();
        String country = UsersAdd_input_country.getText();

        if (fname == null || fname.trim().isEmpty()) {
            setMessageText("error", "Podaj imię");
            return;
        }
        if (lname == null || lname.trim().isEmpty()) {
            setMessageText("error", "Podaj nazwisko");
            return;
        }
        if (username == null || username.trim().isEmpty()) {
            setMessageText("error", "Podaj nazwę użytkownika");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            setMessageText("error", "Podaj adres e-mail");
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            setMessageText("error", "Podaj numer telefonu");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            setMessageText("error", "Podaj hasło");
            return;
        }
        if (street == null || street.trim().isEmpty()) {
            setMessageText("error", "Podaj nazwę ulicy");
            return;
        }
        if (flatnum == null || flatnum.trim().isEmpty()) {
            setMessageText("error", "Podaj numer mieszkania");
            return;
        }
        if (city == null || city.trim().isEmpty()) {
            setMessageText("error", "Podaj miasto");
            return;
        }
        if (zipcode == null || zipcode.trim().isEmpty()) {
            setMessageText("error", "Podaj kod pocztowy");
            return;
        }
        if (country == null || country.trim().isEmpty()) {
            setMessageText("error", "Podaj kraj");
            return;
        }


        if(fname.length() > 64) {
            setMessageText("error", "Imie jest za dlugie");
            return;
        }

        if(lname.length() > 64) {
            setMessageText("error", "Nazwisko jest za dlugie");
            return;
        }

        if(username.length() > 32) {
            setMessageText("error", "Nazwa uzytkownika jest za dluga");
            return;
        }

        if(email.length() > 64) {
            setMessageText("error", "E-Mail jest za dlugi");
            return;
        }

        if(password.length() > 255) {
            setMessageText("error", "Haslo jest za dlugie");
            return;
        }

        if(street.length() > 64) {
            setMessageText("error", "Nazwa ulicy jest za dluga");
            return;
        }

        if(flatnum.length() > 32) {
            setMessageText("error", "Numer mieszkania jest za długi");
            return;
        }

        if(city.length() > 64) {
            setMessageText("error", "Nazwa miasta jest za dluga");
            return;
        }

        if(country.length() > 64) {
            setMessageText("error", "Nazwa kraju jest za dluga");
            return;
        }

        if(zipcode.length() != 6) {
            setMessageText("error", "Podaj poprawny kod pocztowy");
            return;
        }

        if(phone.length() != 11) {
            setMessageText("error", "Podaj poprawny numer telefonu");
            return;
        }

        if(password.length() < 8) {
            setMessageText("error", "Podaj poprawne hasło");
            return;
        }

        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if(!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            setMessageText("error", "Podaj poprawny email");
            return;
        }

        if(!phone.matches("[0-9]+")) {
            setMessageText("error", "Podaj poprawny telefon");
            return;
        }

        if(!zipcode.matches("[0-9]{2}-[0-9]{3}")) {
            setMessageText("error", "Podaj poprawny kod pocztowy");
            return;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(actionType.equals("ADD")) {
            List<Uzytkownicy> checkUsername = usersService.getUserByUsername(username);
            List<Uzytkownicy> checkEmail = usersService.getUserByEmail(email);

            if(!checkUsername.isEmpty()) {
                setMessageText("error", "Użytkownik o takiej nazwie już istnieje");
                return;
            }

            if(!checkEmail.isEmpty()) {
                setMessageText("error", "Użytkownik o takim adresie email już istnieje");
                return;
            }

            Uzytkownicy user = new Uzytkownicy(fname, lname, username, email, phone, password, timestamp,
                    new Adresy(street, flatnum, city, zipcode, country)
            );
            addUser(user);
        }
        else if(actionType.equals("UPDATE")){
            user.setImie(fname);
            user.setNazwisko(lname);
            user.setNazwa_uzytkownika(username);
            user.setEmail(email);
            user.setTelefon(phone);
            user.setHaslo(password);
            user.getAdresy().setUlica(street);
            user.getAdresy().setNr_mieszkania(flatnum);
            user.getAdresy().setMiasto(city);
            user.getAdresy().setKod_pocztowy(zipcode);
            user.getAdresy().setKraj(country);
            updateUser(user);
        }
        else {
            // TODO
        }
    }

    public void addUser(Uzytkownicy user) {
        usersService.addUser(user);
        setMessageText("success", "Poprawnie dodano użytkownika. Dodaj kolejnego");
        clearForm();
    }

    public void updateUser(Uzytkownicy user) {
        usersService.updateUser(user);
        setMessageText("success", "Poprawnie zaktualizowano użytkownika");
    }

    public void setMessageText(String type, String text) {
        if(type.equals("error")) {
            UsersAdd_text_form.setStyle("-fx-text-fill: #bc0404; -fx-font-weight: bold;");
            UsersAdd_text_form.setText("Błąd - " + text);
        }
        else if(type.equals("success")) {
            UsersAdd_text_form.setStyle("-fx-text-fill: #38830a; -fx-font-weight: bold;");
            UsersAdd_text_form.setText("Sukces - " + text);
        }
        else {
            UsersAdd_text_form.setStyle("-fx-font-weight: bold;");
            UsersAdd_text_form.setText(text);
        }
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Uzytkownicy getUser() {
        return user;
    }

    public void setUser(Uzytkownicy user) {
        this.user = user;
    }
}
