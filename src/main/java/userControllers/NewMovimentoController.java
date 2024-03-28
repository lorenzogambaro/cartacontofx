/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userControllers;

import model.Conto;
import model.Iban;
import model.Intestatario;
import model.Movimento;
import model.TipoMovimento;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import API.ApiConnection;
import funcitons.Functions;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.InvalidIbanException;

/**
 *
 * @author Administrator
 */
public class NewMovimentoController implements Initializable
{
    @FXML
    private ChoiceBox<TipoMovimento> typeCB;
    
    @FXML
    private DatePicker execDate;
    
    @FXML
    private DatePicker emitDate;
    
    @FXML
    private TextField causal;
    
    @FXML
    private TextField iban;
    
    @FXML
    private TextField amount;
    
    
    @FXML
    private TextField operationCostTF;
    
    
    @FXML
    private Label iban_LBL;
    
    private Intestatario user;
    private Conto conto;
    
    
    public void setUser(final Intestatario i)
    {
        this.user = Objects.requireNonNull(i);
    }
    public void setConto(final Conto c)
    {
        this.conto = Objects.requireNonNull(c);
    }

    private List<TipoMovimento> tipiMovimento;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        if (this.user == null)
            return;
        try 
        {
            final List<JsonElement> types = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/tipimovimento").asMap().get("result").getAsJsonArray().asList(); 
            
            this.tipiMovimento = new ArrayList<>();
            
            for (final JsonElement e : types)
            {
                final Map<String, JsonElement> type = e.getAsJsonObject().asMap();
                
                final TipoMovimento t = new TipoMovimento(type.get("id_type").getAsLong(), type.get("descript").getAsString(), type.get("cost").getAsDouble(), type.get("direction").getAsBoolean() ? 1 : -1, type.get("days").getAsInt(), type.get("min_power_required_id").getAsInt());
                
                if (this.user.getPower().getPowerCode() == 0 || this.user.getPower().getPowerCode() >= t.getMin_power_requried())
                    this.tipiMovimento.add(t);
            } 
        } 
        catch (final IOException | IllegalStateException ex) 
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Errore durante la richiesta dei dati!", "Errore richiesta dati", "Errore!");
            Platform.runLater(this.amount.getScene().getWindow()::hide);
            return;
        }
        
        if (this.tipiMovimento.isEmpty())
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Nessun movimento presente per le tue autorizzazioni!", "Errore creazione Movimento", "Errore!");
            Platform.runLater(this.amount.getScene().getWindow()::hide);
            return;
        }
        
        this.typeCB.getItems().addAll(this.tipiMovimento);
        
        this.typeCB.setConverter(new StringConverter<TipoMovimento>() 
        {
            @Override
            public String toString(TipoMovimento object)
            {
                if (object == null)
                    return null;
                return object.getDesc();
            }

            @Override
            public TipoMovimento fromString(String string) 
            {
                return null;
            }
        }
        );
        
        this.typeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> 
        {
            final TipoMovimento tipo =  this.typeCB.getValue();
            this.operationCostTF.setText("€" + String.valueOf(tipo.getCost()));
            
            this.iban.setVisible(tipo.getAmount() != 1);
            this.iban_LBL.setVisible(tipo.getAmount() != 1);
            
        });
        
        this.execDate.valueProperty().addListener(eh -> 
        {
            this.emitDate.setValue(this.execDate.getValue().plusDays(this.typeCB.getValue().getGiorniValuta()));
        });
        
        this.typeCB.setValue(this.typeCB.getItems().get(0));
    }
    @FXML
    public void onRequest(final ActionEvent eh)
    {
        try
        {
            final TipoMovimento tipo = this.typeCB.getValue();
            if (!this.execDate.getValue().isAfter(LocalDate.now().minusDays(1)))
            {
                Functions.spawnAlert(Alert.AlertType.ERROR, "Impossibile mettere una data precedente a quella odierna", "Errore data", "Errore!");
                return;
            }
            final Movimento m = new Movimento(this.conto.getIban(), this.execDate.getValue(), this.emitDate.getValue(), this.causal.getText(), tipo.getAmount() == 1 ? this.conto.getIban() : new Iban(this.iban.getText()), Double.parseDouble(this.amount.getText()), tipo);
            final JsonObject data = new JsonObject();
            
            for (final Field f : m.getClass().getDeclaredFields())
            {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;
                f.setAccessible(true);
                
                data.addProperty(f.getName(), f.get(m).toString());
            }
            data.addProperty("type", m.getType().getCode());
            
            final JsonObject response = ApiConnection.sendData("http://server632.ddns.net:8211/cartaconto/movimenti", data);

            if (response.asMap().get("status").getAsInt() != 200)
                new Alert(Alert.AlertType.ERROR, "Impossibile richiedere il movimento!").showAndWait();
            else
            {
                new Alert(Alert.AlertType.INFORMATION, "Movimento pervenuto alla banca!").showAndWait();
                this.typeCB.getScene().getWindow().hide();
            }
            
        }
        catch (final IOException | IllegalStateException ex)
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Errore nella richiesta del movimento!", "Errore richiesta", "Errore!");
        }
        catch (final NumberFormatException ex)
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Inserire solo valori numerici nell'importo!", "Errore valori", "Errore!");
            this.amount.setText("");
        }
        catch (final InvalidIbanException ex)
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Iban inserito non valido!", "Errore iban", "Errore!");
            this.iban.setText("");
        } 
        catch (IllegalArgumentException | IllegalAccessException ex) 
        {
            Functions.spawnAlert(Alert.AlertType.ERROR, "Errore nel caricamento dei dati!", "Errore caricamento dati", "Errore!");
            Platform.runLater(this.amount.getScene().getWindow()::hide);
        }
        
    }
  
}
