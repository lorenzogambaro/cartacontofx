/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminControllers;

import API.ApiConnection;
import com.conto.cartacontogui.App;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Administrator;

/**
 *
 * @author Administrator
 */
public class AdminDashboardController implements Initializable
{
    private Administrator admin;
    public final static int ADD_POWER = 3; //to implement in DB
    
    @FXML
    private Button addIntestatario;
    
    public void setAdmin(final Administrator a)
    {
        this.admin = Objects.requireNonNull(a);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        if (this.admin == null)
            return;
        
        try 
        {
            final Map<String, JsonElement> response = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/conti").asMap();
            
            final List<JsonElement> conti = response.get("result").getAsJsonArray().asList();

        } 
        catch (final IOException | IllegalStateException ex) 
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile richiedere i dati al server, riprova!");
            alert.setHeaderText("Errore richiesta dati");
            alert.setTitle("Errore");
            alert.showAndWait();
            Platform.runLater(this.addIntestatario.getScene().getWindow()::hide);
        }
    }

    @FXML
    private void onAddIntestatario(final ActionEvent event) throws IOException 
    {
        if (this.admin.getPowerLevel() <= ADD_POWER)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Permessi insufficienti per seguire l'azione!");
            alert.setHeaderText("Errore di accesso!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            return;
        }
        final Stage s = new Stage();
        s.setScene(new Scene(App.loadFXML("adminFXML/newintestatario")));
        s.showAndWait();
    }

    @FXML
    private void addNewConto(final ActionEvent event) throws IOException 
    {
        if (this.admin.getPowerLevel() <= ADD_POWER)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Permessi insufficienti per seguire l'azione!");
            alert.setHeaderText("Errore di accesso!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            return;
        }
        final Stage s = new Stage();
        s.setScene(new Scene(App.loadFXML("adminFXML/nuovoConto")));
        s.showAndWait();
    }
}
