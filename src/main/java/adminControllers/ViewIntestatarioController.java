/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package adminControllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import model.Intestatario;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ViewIntestatarioController implements Initializable 
{
    private Intestatario i;
    public void setIntestatario(final Intestatario i)
    {
        this.i = Objects.requireNonNull(i);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        if (this.i == null)
            return;
    }    
    
}
