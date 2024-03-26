/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package userControllers;

import model.Intestatario;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import API.ApiConnection;
import com.conto.cartacontogui.App;
import java.io.IOException;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class LoginController
{
    @FXML
    private TextField username_TF;
    
    @FXML
    private PasswordField password_TF;
    
    @FXML
    public void onLogin(ActionEvent e) throws IOException
    {
        final String username = this.username_TF.getText();
        final String password = this.password_TF.getText();
        
        if (username.trim().isBlank() || password.trim().isBlank())
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Riempire i campi per eseguire l'accesso!");
            alert.setHeaderText("Errore di login!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            
            this.username_TF.setText("");
            this.password_TF.setText("");  
            return;
        }
        
        try
        {
            final JsonObject datas = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/intestatari/" + username);    
            
            System.out.println(datas);
            
            if (datas.asMap().get("result") == null)
            {
                final Alert alert = new Alert(Alert.AlertType.ERROR, "Utente non presente nel database!");
                alert.setHeaderText("Errore di login!");
                alert.setTitle("Errore!");
                alert.showAndWait();

                this.username_TF.setText("");
                this.password_TF.setText("");  
                return;
            }

            final Map<String, JsonElement> user = datas.asMap().get("result").getAsJsonObject().asMap();
            
            if (!Intestatario.calcolaHash(password).equals(user.get("hashed_pw").getAsString()))
            {
                final Alert alert = new Alert(Alert.AlertType.ERROR, "Password Non Corretta!");
                alert.setHeaderText("Errore di login!");
                alert.setTitle("Errore!");
                alert.showAndWait();

                this.password_TF.setText("");
                return;
            }
            
            final Intestatario intestatario = Intestatario.parse(user, user.get("power"));
            
            final Stage s = new Stage();

            final FXMLLoader loader = new FXMLLoader(App.class.getResource("userFXML/dashboard.fxml"));
            final Scene sc = new Scene(loader.load());
            ((MainPageController) loader.getController()).setIntestatario(intestatario);
            ((MainPageController) loader.getController()).initialize(null, null);
                    
            s.setScene(sc);
            s.setTitle("Internet Banking di " + user.get("username").getAsString());
            s.setOnCloseRequest(eh -> ((Stage) App.scene.getWindow()).show());
            s.show();
            username_TF.getScene().getWindow().hide();
        }
        catch (final IOException | IllegalStateException ex)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile raggiungere il server, riprova più tardi!");
            alert.setHeaderText("Errore di login!");
            alert.setTitle("Errore!");
            alert.showAndWait();
        }
        
    }      

    @FXML
    private void onBack(final ActionEvent event) 
    {
        this.password_TF.getScene().getWindow().hide();
        ((Stage) App.scene.getWindow()).show();
    }
}