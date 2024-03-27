/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funcitons;

import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Administrator
 */
public final class Functions {
    private Functions() {}
    
    public static Optional<ButtonType> spawnAlert(final Alert.AlertType type, final String text, final String header, final String title)
    {
        final Alert alert = new Alert(Objects.requireNonNull(Alert.AlertType.ERROR), Objects.requireNonNull(text));
        alert.setHeaderText(Objects.requireNonNull(header));
        alert.setTitle(Objects.requireNonNull(title));
        return alert.showAndWait();
    }
    
}
