package edu.bd4.bdp4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import mysqlconnection.JDBC;

import java.io.IOException;

public class Borrado {
    @FXML
    AnchorPane parentNode;
    @FXML
    private TextField tableNameField;
    @FXML
    private TextField columnNameField;
    @FXML
    private TextField valueField;
    @FXML
    private Button deleteButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label succesLabel;
    @FXML
    private Label errorLabel1;
    @FXML
    private Label succesLabel1;
    @FXML
    private TextField tableNameField2;
    @FXML
    private TextField columnNameToUpdateField;
    @FXML
    private TextField newValueField;
    @FXML
    private TextField columnNameToIdentifyField;
    @FXML
    private TextField identifierValueField;
    @FXML
    private Button goShowScreen;

    FadeUtilityClass fader;

    public Borrado() {
        this.fader = new FadeUtilityClass();
    }

    @FXML
    protected void onDeleteButton() {
        String tableName = tableNameField.getText();
        String columnName = columnNameField.getText();
        String value = valueField.getText();
        boolean result = JDBC.deleteData(tableName, columnName, value);
        if (result) {
            succesLabel.setText("Data deleted successfully.");
            changeOpacityOnAndOff(succesLabel);
        } else {
            errorLabel.setText("Error deleting data.");
            changeOpacityOnAndOff(errorLabel);
        }
    };

    @FXML
    protected void onUpdateButton() {
        String newValue = newValueField.getText();
        String idValue = identifierValueField.getText();
        boolean result = JDBC.updatePhoneNumber(newValue, idValue);
        if (result) {
            changeOpacityOnAndOff(succesLabel);
        } else {
            changeOpacityOnAndOff(errorLabel);
        }
    };


    private void changeOpacityOnAndOff(Label label) {
        label.setOpacity(1);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> label.setOpacity(0)));
        timeline.play();
    }

    @FXML
    private void goShowScreen() throws IOException {
        fader.fadeNextScene(parentNode, 2, "Select.fxml");
    }

}
