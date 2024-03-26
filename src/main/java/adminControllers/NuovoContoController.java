/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package adminControllers;

import API.ApiConnection;
import com.conto.cartacontogui.App;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Intestatario;
/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class NuovoContoController implements Initializable {


    @FXML
    private TextField iban_TF;
    
    @FXML
    private ListView<CheckBox> intestatari_LW;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            final JsonElement ibanObject = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/getfirstavailableiban").asMap().get("result");
            final JsonElement intestatariObject = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/intestatari").asMap().get("result");
            
            if (ibanObject.isJsonNull() || intestatariObject.isJsonNull())
            {
                return;
            }
            
            final String iban = ibanObject.getAsString();
            final List<JsonElement> intestatariElement = intestatariObject.getAsJsonArray().asList();
            
            
            for (final JsonElement e : intestatariElement)
            {
                final Map<String, JsonElement> intestatarioElement = e.getAsJsonObject().asMap();
                
                final Intestatario intestatario = Intestatario.parse(intestatarioElement, intestatarioElement.get("power"));
                
                final CheckBox cb = new CheckBox(intestatario.getFiscal_code());
                
                cb.setOnMouseClicked(eh -> 
                {
                    if (eh.getButton() != MouseButton.SECONDARY)
                        return;
                    
                    try 
                    {
                        final Stage s = new Stage();
                        final FXMLLoader loader = new FXMLLoader(App.class.getResource("adminFXML/viewIntestatario.fxml"));
                        s.setScene(new Scene(loader.load()));
                        ((ViewIntestatarioController) loader.getController()).setIntestatario(intestatario);
                        ((ViewIntestatarioController) loader.getController()).initialize(url, rb);
                        
                        s.initModality(Modality.APPLICATION_MODAL);
                        s.showAndWait();
                    } 
                    catch (final IOException ex) 
                    {
                        final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile visualizzare l'intestatario!");
                        alert.setHeaderText("Errore visualizzazione intestatario!");
                        alert.setTitle("Errore!");
                        alert.showAndWait();
                        Platform.runLater(this.iban_TF.getScene().getWindow()::hide);
                    }
                            
                });
                this.intestatari_LW.getItems().add(cb);
                
            }

            this.iban_TF.setText(iban);
            
        }
        catch (final IOException | IllegalStateException ex)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nel caricamento dei dati!");
            alert.setHeaderText("Errore richiesta dati!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            Platform.runLater(this.iban_TF.getScene().getWindow()::hide);
        }
    }    
    
    @FXML
    private void onAddRequest(final ActionEvent event) throws IOException 
    {
        final JsonObject data = new JsonObject();
        final JsonArray intes = new JsonArray();
        
        for (final CheckBox cb : this.intestatari_LW.getItems())
        {
            if (cb.isSelected())
                intes.add(cb.getText());
        }
        if (intes.isEmpty())
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Inserire almento un intestatario per il conto!");
            alert.setHeaderText("Errore creazione conto!");
            alert.setTitle("Errore!");
            alert.showAndWait();
            return;
        }
        
        data.addProperty("iban", this.iban_TF.getText());
        data.add("intestatari", intes);
        
                    
        final JsonObject response = ApiConnection.sendData("http://server632.ddns.net:8211/cartaconto/conti", data);

        if (response.asMap().get("status").getAsInt() != 200)
            new Alert(Alert.AlertType.ERROR, "Impossibile eseguire la cerazione del nuovo conto!").showAndWait();
        else
        {
            new Alert(Alert.AlertType.INFORMATION, "Conto Creato!").showAndWait();
            this.iban_TF.getScene().getWindow().hide();
        }
    }

}
