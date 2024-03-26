/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userControllers;

import properties.ContoProperty;
import properties.IntestatarioProperty;
import model.Conto;
import model.Iban;
import model.Intestatario;
import model.Movimento;
import model.TipoMovimento;
import com.google.gson.JsonElement;
import API.ApiConnection;
import com.conto.cartacontogui.App;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class MainPageController implements Initializable
{
    @FXML
    private Label saldoLBL;
    
    @FXML
    private TableView<Movimento> movimentiTABLE;
    
    @FXML
    private ChoiceBox contiSelector;
    
    private final IntestatarioProperty intestatario = new IntestatarioProperty();
    
    final ObservableMap<Iban, ContoProperty> conti = FXCollections.observableHashMap();
    
    @FXML
    private Label user_LBL;
    @FXML
    private TableColumn<Movimento, String> movimentiEntrata;
    @FXML
    private TableColumn<Movimento, String> movimentiUscita;
    
    public void setIntestatario(final Intestatario i)
    {
        this.intestatario.set(Objects.requireNonNull(i));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        if (this.intestatario.get() == null)
            return;
                
        this.conti.addListener((final Observable change) ->
        {
            this.contiSelector.getItems().clear();
            this.contiSelector.getItems().addAll(this.conti.keySet());

            this.contiSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> 
            {
                if (newValue == null)
                    return;
                final Conto c = this.conti.get((Iban)newValue).get();
                this.saldoLBL.setText("Saldo del Conto: €" + String.valueOf(c.saldo()));
                
                this.movimentiTABLE.setItems(FXCollections.observableArrayList(c.getOperazioni()));
            });
            if (!this.contiSelector.getItems().isEmpty())
                this.contiSelector.setValue(this.contiSelector.getItems().get(0));
            
        });
        
        try 
        {
            this.conti.clear();
            this.conti.putAll(retriveDatas(this.intestatario.get()));
            this.user_LBL.setText("Dashboard banca di: " + this.intestatario.get().getSurname() + " " + this.intestatario.get().getName());
        }
        catch (final IOException | IllegalStateException ex) 
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile richiedere i dati al server!");
            alert.setHeaderText("Errore richiesta dati");
            alert.setTitle("Errore");
            alert.showAndWait();
            Platform.runLater(((Stage) this.saldoLBL.getScene().getWindow())::close);
        }
        
        this.movimentiEntrata.setCellValueFactory(param -> 
        {
            if (param.getValue().getType().getAmount() == -1)
                return null;
            
            final StringBuilder sb = new StringBuilder();
            sb.append("Ricevuto ").append(param.getValue().getType().getDesc()).append(" di ").append(param.getValue().getImporto()).append(" da ").append(param.getValue().getIbanRichiedente());
            
            return new SimpleStringProperty(sb.toString());
        });
        this.movimentiUscita.setCellValueFactory(param -> 
        {
            if (param.getValue().getType().getAmount() == 1)
                return null;
            
            final StringBuilder sb = new StringBuilder();
            sb.append("Inviato ").append(param.getValue().getType().getDesc()).append(" di ").append(param.getValue().getImporto()).append(" a ").append(param.getValue().getIbanDestinatario());
            
            return new SimpleStringProperty(sb.toString());
        });

    }
    public static Map<Iban, ContoProperty> retriveDatas(final Intestatario intestatario) throws IOException
    {
        final Map<Iban, ContoProperty> conti = new HashMap<>();

        for (final Iban i : intestatario.getContiAssociati())
        {
            final Map<String, JsonElement> conto = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/conti/" + i).asMap().get("result").getAsJsonObject().asMap();

            final List<JsonElement> reqs = conto.get("movimenti_cui_richiedente").getAsJsonArray().asList();
            final List<JsonElement> dest = conto.get("movimenti_cui_destinatario").getAsJsonArray().asList();

            final Conto c = new Conto(new Iban(conto.get("iban_conto").getAsString()), OffsetDateTime.parse(conto.get("opening_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate(), intestatario);

            for (final JsonElement e : reqs)
            {
                final Map<String, JsonElement> mov = e.getAsJsonObject().asMap();
                final Map<String, JsonElement> type = mov.get("movement_type").getAsJsonObject().asMap();

                final TipoMovimento t = new TipoMovimento(type.get("id_type").getAsLong(), type.get("descript").getAsString(), type.get("cost").getAsDouble(), -1, type.get("min_power_required_id").getAsInt());

                c.newOperazioneOut
                (       t,
                        new Iban(mov.get("iban_destinatario").getAsString()), 
                        mov.get("amount").getAsDouble(), 
                        OffsetDateTime.parse(mov.get("operation_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate(), 
                        OffsetDateTime.parse(mov.get("valute_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate(), 
                        mov.get("causal").getAsString()
                );
            }
            for (final JsonElement e : dest)
            {
                final Map<String, JsonElement> mov = e.getAsJsonObject().asMap();
                final Map<String, JsonElement> type = mov.get("movement_type").getAsJsonObject().asMap();

                final TipoMovimento t = new TipoMovimento(type.get("id_type").getAsLong(), type.get("descript").getAsString(), type.get("cost").getAsDouble(), 1, type.get("min_power_required_id").getAsInt());

                c.newOperazioneIn
                (       t,
                        new Iban(mov.get("iban_richiedente").getAsString()), 
                        mov.get("amount").getAsDouble(), 
                        OffsetDateTime.parse(mov.get("operation_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate(), 
                        OffsetDateTime.parse(mov.get("valute_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate(), 
                        mov.get("causal").getAsString()
                );
            }

            conti.put(i, new ContoProperty(c));
        }
        return conti;   
    }
    @FXML
    public void onNewMovimentoClick(ActionEvent eh) throws IOException
    {
        final Stage s = new Stage();

        final FXMLLoader loader = new FXMLLoader(App.class.getResource("userFXML/newmovimento.fxml"));
        final Scene sc = new Scene(loader.load());
        ((NewMovimentoController) loader.getController()).setUser(intestatario.get());
        ((NewMovimentoController) loader.getController()).setConto(this.conti.get((Iban)this.contiSelector.getValue()).get());
        s.setScene(sc);
        ((NewMovimentoController) loader.getController()).initialize(null, null);

        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Nuovo Movimento");
        s.showAndWait();
        
        this.conti.clear();
        this.conti.putAll(retriveDatas(intestatario.get()));
        this.contiSelector.getItems().clear();
        this.contiSelector.getItems().addAll(this.conti.keySet());
        this.contiSelector.setValue(this.contiSelector.getItems().get(0));    
    }

    @FXML
    private void onModifyDatas(final ActionEvent event) throws IOException 
    {
        final Stage s = new Stage();
        final FXMLLoader loader = new FXMLLoader(App.class.getResource("userFXML/modificaIntestatario.fxml"));
        s.setScene(new Scene(loader.load()));
        s.initModality(Modality.APPLICATION_MODAL);
        ((ModificaIntestatarioController) loader.getController()).setIntestatario(this.intestatario.get());
        ((ModificaIntestatarioController) loader.getController()).initialize(null, null);
        s.showAndWait();

        try
        {
            final JsonObject datas = ApiConnection.getDatas("http://server632.ddns.net:8211/cartaconto/intestatari/" + this.intestatario.get().getUsername());    
            final Map<String, JsonElement> user = datas.asMap().get("result").getAsJsonObject().asMap();

            
            this.intestatario.set(Intestatario.parse(user, user.get("power")));
        }
        catch (final IOException ex)
        {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile richiedere i dati aggiornati dell'intestatario!");
            alert.setHeaderText("Errore richiesta dati intestatario!");
            alert.setTitle("Errore!");
            alert.showAndWait();
        }
    }
}