/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package applicationControllers;

import com.conto.cartacontogui.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class MainController
{

    @FXML
    private Button userBTN;
    @FXML
    public void asAdmin(final ActionEvent eh) throws IOException
    {
        final Stage s = new Stage();
        s.setScene(new Scene(App.loadFXML("adminFXML/login")));
        App.scene.getWindow().hide();
        s.setOnCloseRequest(event -> ((Stage) App.scene.getWindow()).show());

        s.show();
    }
    
    @FXML
    public void asUser(final ActionEvent eh) throws IOException
    {
        final Stage s = new Stage();
        s.setScene(new Scene(App.loadFXML("userFXML/login")));
        App.scene.getWindow().hide();
        s.setOnCloseRequest(event -> ((Stage) App.scene.getWindow()).show());

        s.show();
    }
}
