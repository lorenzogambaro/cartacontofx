/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package userControllers;

import properties.IntestatarioProperty;
import API.ApiConnection;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Intestatario;
import model.User;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ModificaIntestatarioController implements Initializable {

    @FXML
    private TextField password_TF;
    @FXML
    private TextField confirm_password_TF;
    @FXML
    private TextField phoneNumber_TF;
    @FXML
    private TextField email_TF;
    @FXML
    private TextField via_TF;
    @FXML
    private TextField number_TF;
    @FXML
    private TextField cap_TF;
    @FXML
    private TextField comune_TF;
    @FXML
    private TextField provincia_TF;
    
    private final IntestatarioProperty intestatario = new IntestatarioProperty();
    
    public void setIntestatario(final Intestatario i)
    {
        this.intestatario.set(Objects.requireNonNull(i));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        if (this.intestatario.get() == null)
            return;
        
        this.phoneNumber_TF.setText(this.intestatario.get().getPhoneNumber());
        this.email_TF.setText(this.intestatario.get().getEmailAddress());
        this.via_TF.setText(this.intestatario.get().getAddress().getVia());
        this.number_TF.setText(this.intestatario.get().getAddress().getNumero());
        this.cap_TF.setText(this.intestatario.get().getAddress().getCap());
        this.comune_TF.setText(this.intestatario.get().getAddress().getComune());
        this.provincia_TF.setText(this.intestatario.get().getAddress().getProvincia());
    }    

    @FXML
    private void onModifiy(final ActionEvent event)
    {
        final JsonObject data = new JsonObject();
        
        try
        {
            if (!password_TF.getText().isBlank() && !password_TF.getText().equals(confirm_password_TF.getText()))
            {
                new Alert(Alert.AlertType.ERROR, "Le due password devono essere uguali!").showAndWait();
                return;
            }

            if (!password_TF.getText().isBlank() && !User.calcolaHash(this.password_TF.getText()).equals(this.intestatario.get().getPassword()))
                data.addProperty("hashed_pw", User.calcolaHash(this.password_TF.getText()));

            if (!this.via_TF.getText().equals(this.intestatario.get().getAddress().getVia()))
                data.addProperty("via", this.via_TF.getText());

            if (!this.number_TF.getText().equals(this.intestatario.get().getAddress().getNumero()))
                data.addProperty("numero", this.number_TF.getText());

            if (!this.cap_TF.getText().equals(this.intestatario.get().getAddress().getCap()))
                data.addProperty("cap", this.cap_TF.getText());

            if (!this.comune_TF.getText().equals(this.intestatario.get().getAddress().getComune()))
                data.addProperty("comune", this.comune_TF.getText());

            if (!this.provincia_TF.getText().equals(this.intestatario.get().getAddress().getProvincia()))
                data.addProperty("provincia", this.provincia_TF.getText());

            if (!this.phoneNumber_TF.getText().equals(this.intestatario.get().getPhoneNumber()))
                data.addProperty("phoneNumber", this.phoneNumber_TF.getText());

            if (!this.email_TF.getText().equals(this.intestatario.get().getEmailAddress()))
                data.addProperty("phoneNumber", this.email_TF.getText());

            System.out.println(data);
            
            if (data.isEmpty())
            {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nessuna modifica, ritorno!");
                alert.setHeaderText("Nessuna modifica!");
                alert.setTitle("Infomrazione");
                alert.showAndWait();
                Platform.runLater(this.cap_TF.getScene().getWindow()::hide);
                return;
            }

            final JsonObject response = ApiConnection.sendData("http://server632.ddns.net:8211/cartaconto/intestatari/" + this.intestatario.get().getFiscal_code(), data);

            if (response.asMap().get("status").getAsInt() != 200)
            {
                final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile eseguire la richiesta di modifica dell' intestatario!");
                alert.setHeaderText("Impossibile modificare l'intestatario, dati errati!");
                alert.setTitle("Errore modifica intestatario");
                alert.showAndWait();
                
                //reload datas
                
                final Map<String, JsonElement> user = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/intestatari/" + this.intestatario.get().getUsername()).asMap().get("result").getAsJsonArray().asList().get(0).getAsJsonObject().asMap();
                final JsonElement tipoIntestatario = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/potenzeintestatario/" + user.get("power").getAsInt()).asMap().get("result");
            
                this.setIntestatario(Intestatario.parse(user, tipoIntestatario));
            }
            else
            {
                new Alert(Alert.AlertType.INFORMATION, "Intestatario modificato correttamente!").showAndWait();
                this.password_TF.getScene().getWindow().hide();
            }
            
        }
        catch (final IOException | IllegalArgumentException ex)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile modificare l'intestatario");
            alert.setHeaderText("Errore modifica!");
            alert.setTitle("Errore!");
            alert.showAndWait();
        }
        
    }
    
}
