package edu.bd4.bdp4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;

public class VisualInterface {
    @FXML
    private TextField tableName;
    @FXML
    private TextArea atributesTextArea;
    @FXML
    private Button createTableBtn;
    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;
    @FXML
    private Label errorLabel3;

    public VisualInterface() {
    }


    @FXML
    protected void onCreateTableBtn() {
        String tableNameString = tableName.getText();
        ArrayList <String> Tabla = generateAtributesArrayList();
        checkAtributes(Tabla);
        //JDBC.createTable(tableNameString, Tabla);
    }

    private ArrayList<String> generateAtributesArrayList() {
        ArrayList<String> atributes = new ArrayList<>();
        String textAreaData = atributesTextArea.getText(); // Obtener los datos del TextArea
        String[] lines = textAreaData.split("\n"); // Dividir la cadena por saltos de línea

        // Agregar cada línea al ArrayList de atributos
        for (String line : lines) {
            atributes.add(line.trim()); // trim() para eliminar espacios en blanco adicionales al principio y al final de cada línea
        }
        return atributes;
    }
    private void checkAtributes(ArrayList<String> atributes) {
        String[] validDataTypes = {"INT", "VARCHAR", "TEXT", "DATE", "TIMESTAMP", "FLOAT", "DOUBLE", "DECIMAL", "BOOLEAN"};
        String[] validRestrictions = {"NOT_NULL", "UNIQUE", "PRIMARY_KEY", "FOREIGN_KEY", "CHECK", "DEFAULT", "INDEX", "AUTO_INCREMENT"};
        int contador = 0;

        for (String atribute : atributes) {
            contador++;
            String[] atributeData = atribute.split(" ");

            // Check attribute name
            if (!atributeData[0].matches("[a-zA-Z]+")) {
                errorLabel1.setText("Invalid attribute name: " + atributeData[0] + " from " +contador + "º Atribute" );
                changeOpacityOnAndOff(errorLabel1);
            }

            // Check data type
            if (!Arrays.asList(validDataTypes).contains(atributeData[1].toUpperCase())) {
                errorLabel2.setText("Invalid data type: " + atributeData[1] + "from " +contador + " Atribute" );
                changeOpacityOnAndOff(errorLabel2);
            }

            // Check restrictions
            for (int i = 2; i < atributeData.length; i++) {
                if (!Arrays.asList(validRestrictions).contains(atributeData[i].toUpperCase())) {
                    errorLabel3.setText("Invalid restriction: " + atributeData[i] + "from " +contador + " Atribute" );
                    changeOpacityOnAndOff(errorLabel3);
                    break;
                }
            }
        }
    }

    private void changeOpacityOnAndOff(Label label) {
        label.setOpacity(1);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> label.setOpacity(0)));
        timeline.play();
    }
}