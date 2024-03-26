/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package adminControllers;

import API.ApiConnection;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.Indirizzo;
import model.Intestatario;
import model.TipoIntestatario;
import model.User;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class NewIntestatarioController implements Initializable {

    @FXML
    private TextField fiscal_code_TF;
    @FXML
    private TextField surname_TF;
    @FXML
    private TextField name_TF;
    @FXML
    private TextField username_TF;
    @FXML
    private DatePicker birthDate_TF;
    @FXML
    private TextField cap_TF;
    @FXML
    private TextField city_TF;
    @FXML
    private TextField provincia_TF;
    @FXML
    private TextField phoneNumber_TF;
    @FXML
    private TextField email_TF;
    @FXML
    private TextField address_TF;
    @FXML
    private PasswordField password_TF;
    @FXML
    private ChoiceBox<TipoIntestatario> powers_CB;
    @FXML
    private TextField numeroCivico_TF;


    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try
        {
            final List<JsonElement> powers = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/potenzeintestatario").asMap().get("result").getAsJsonArray().asList();
       
            for (final JsonElement power : powers)
            {
                final TipoIntestatario tipo = new TipoIntestatario(power.getAsJsonObject().asMap().get("id_potenza").getAsInt(), power.getAsJsonObject().asMap().get("descPerms").getAsString());
                this.powers_CB.getItems().add(tipo);
            }
        }
        catch (final IOException | IllegalStateException ex)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile eseguire la richiesta di inserimento intestatatio!");
            alert.setHeaderText("Errore di inserimento intestatario!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            Platform.runLater(this.address_TF.getScene().getWindow()::hide);
            return;
        }
        
        if (this.powers_CB.getItems().isEmpty())
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Nessun ruolo per gli intestatari impostato nella banca, aggiungerli prima per creare intestatari!");
            alert.setHeaderText("Errore di inserimento intestatario!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            Platform.runLater(this.address_TF.getScene().getWindow()::hide);
            return;
        }
        
        this.powers_CB.setValue(this.powers_CB.getItems().get(0));
        
        
        this.powers_CB.setConverter(new StringConverter<TipoIntestatario>() 
        {
            @Override
            public String toString(final TipoIntestatario object)
            {
                if (object == null)
                    return "";
                return object.getDescr();
            }

            @Override
            public TipoIntestatario fromString(final String string) 
            {
                return null;
            }
        });
    }   
    
    @FXML
    public void onAddIntestatarioRequest(final ActionEvent event) throws IllegalArgumentException, IllegalAccessException 
    {
        for (final Field f : getClass().getDeclaredFields())
        {
            if (f.get(this) instanceof TextField tf)
            {
                f.setAccessible(true);
                if (tf.getText().trim().isEmpty())
                {
                    final Alert alert = new Alert(Alert.AlertType.ERROR, "Riempire tutti i campi per aggiungere un intestatario!");
                    alert.setHeaderText("Errore di inserimento intestatario!");
                    alert.setTitle("Errore!");
                    alert.showAndWait();
                    return;
                }
            }
            else if (f.get(this) instanceof DatePicker dp)
            {
                if (dp.getValue() == null)
                {
                    final Alert alert = new Alert(Alert.AlertType.ERROR, "Riempire tutti i campi per aggiungere un intestatario!");
                    alert.setHeaderText("Errore di inserimento intestatario!");
                    alert.setTitle("Errore!");
                    alert.showAndWait();
                    return;
                }
            }
        }
        try
        {
            final Intestatario i = new Intestatario
            (
                    this.username_TF.getText(), 
                    User.calcolaHash(this.password_TF.getText()), 
                    this.powers_CB.getValue(), 
                    this.fiscal_code_TF.getText(), 
                    this.surname_TF.getText(), 
                    this.name_TF.getText(),
                    this.birthDate_TF.getValue(), 
                    new Indirizzo(this.address_TF.getText(), this.numeroCivico_TF.getText(), this.cap_TF.getText(), this.city_TF.getText(), this.provincia_TF.getText()), 
                    this.phoneNumber_TF.getText(), 
                    this.email_TF.getText()
            );
            
            final JsonObject data = new JsonObject();

            data.addProperty("username", i.getUsername());
            data.addProperty("hashed_pw", i.getPassword());
            data.addProperty("fiscal_code", i.getFiscal_code());
            data.addProperty("surname_intestatario", i.getSurname());
            data.addProperty("name_intestatario", i.getName());
            data.addProperty("birth_date", i.getBirthdate().toString());
            data.addProperty("via", i.getAddress().getVia());
            data.addProperty("numero", i.getAddress().getNumero());
            data.addProperty("cap", i.getAddress().getCap());
            data.addProperty("comune", i.getAddress().getComune());
            data.addProperty("provincia", i.getAddress().getProvincia());
            data.addProperty("phoneNumber", i.getPhoneNumber());
            data.addProperty("email_address", i.getEmailAddress());
            data.addProperty("email_address", i.getEmailAddress());
            data.addProperty("power", i.getPower().getPowerCode());
            
            
            final JsonObject response = ApiConnection.sendData("http://server632.ddns.net:8211/cartaconto/intestatari", data);

            if (response.asMap().get("status").getAsInt() != 200)
            {
                final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile eseguire la richiesta di inserimento intestatario!");
                alert.setHeaderText("Impossibile aggiungere l'intestatario, dati errati!");
                alert.setTitle("Errore inserimento intestatario");
                alert.showAndWait();
            }
            else
            {
                new Alert(Alert.AlertType.INFORMATION, "Intestatario aggiunto alla banca!").showAndWait();
                this.fiscal_code_TF.getScene().getWindow().hide();
            }
            
        }
        catch (final IllegalArgumentException ex)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.setHeaderText("Dati non conformi per la creazione del nuovo intestatario!");
            alert.setTitle("Errore!");
            alert.showAndWait();
        } 
        catch (final IOException ex) 
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante la comunicazione dei dati al server!");
            alert.setHeaderText("Errore inserimento intestatario!");
            alert.setTitle("Errore!");
            alert.showAndWait();
        }
    }
    
}
