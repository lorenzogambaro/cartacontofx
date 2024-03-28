/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package adminControllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Intestatario;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ViewIntestatarioController implements Initializable 
{
    private Intestatario i;
    
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
    private TextField power_TF;
    @FXML
    private TextField numeroCivico_TF;
    
    public void setIntestatario(final Intestatario i)
    {
        this.i = Objects.requireNonNull(i);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        if (this.i == null)
            return;
        
        this.fiscal_code_TF.setText(i.getFiscal_code());
        this.surname_TF.setText(i.getSurname());
        this.name_TF.setText(i.getName());
        this.username_TF.setText(i.getUsername());
        this.birthDate_TF.setValue(i.getBirthdate());
        this.cap_TF.setText(i.getAddress().getCap());
        this.city_TF.setText(i.getAddress().getComune());
        this.provincia_TF.setText(i.getAddress().getProvincia());
        this.phoneNumber_TF.setText(i.getPhoneNumber());
        this.email_TF.setText(i.getEmailAddress());
        this.address_TF.setText(i.getAddress().getVia());
        this.power_TF.setText(i.getPower().getDescr());
        this.numeroCivico_TF.setText(i.getAddress().getNumero());
    }    
    
}
