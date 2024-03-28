/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminControllers;

import API.ApiConnection;
import com.conto.cartacontogui.App;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import funcitons.Functions;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import model.TipoAmministratore;
import model.TipoUtente;

/**
 *
 * @author Administrator
 */
public class AdminDashboardController implements Initializable
{
    private Administrator admin;
    
    @FXML
    private Button addIntestatario;
    
    public void setAdmin(final Administrator a)
    {
        this.admin = Objects.requireNonNull(a);
    }
    
    private List<TipoUtente> powers;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        if (this.admin == null)
            return;
        try 
        {
            final List<JsonElement> powersElement = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/potenzeamministratori").asMap().get("result").getAsJsonArray().asList();
            if (powersElement.isEmpty()) 
            {
                Functions.spawnAlert(Alert.AlertType.ERROR, "Nessuna operazione eseguibile!", "Errore operazioni", "Error");
                Platform.runLater(addIntestatario.getScene().getWindow()::hide);
                return;
            }
            
            this.powers = new ArrayList<>();
            for (final JsonElement j : powersElement) 
            {
                final JsonObject obj = j.getAsJsonObject();
                final TipoUtente t = new TipoAmministratore(obj.get("id_power").getAsInt(), obj.get("powerDesc").getAsString());
                
                this.powers.add(t);
            }
        } 
        catch (final IOException ex) 
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Impossibile contattare il server, riprova più tardi!", "Errore connessione al server!", "Error");
            Platform.runLater(addIntestatario.getScene().getWindow()::hide);
            return;
        }
        
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
        if (this.admin.getPowerLevel() != 0 && this.admin.getPowerLevel() < this.powers.get(3).getPowerCode())
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
        if (this.admin.getPowerLevel() != 0 && this.admin.getPowerLevel() < this.powers.get(3).getPowerCode())
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
