package edu.bd4.bdp4;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import mysqlconnection.JDBC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VisualInterface {
    @FXML
    private AnchorPane parentNode;
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
    FadeUtilityClass fader;

    public VisualInterface() {
        this.fader = new FadeUtilityClass();
    }


    @FXML
    protected void onCreateTableBtn() {
        String tableNameString = tableName.getText();
        ArrayList <String> Tabla = generateAtributesArrayList();
        checkAtributes(Tabla);
        JDBC.createTable(tableNameString, Tabla);
    }

    private ArrayList<String> generateAtributesArrayList() {
        ArrayList<String> atributes = new ArrayList<>();
        String textAreaData = atributesTextArea.getText(); // Obtener los datos del TextArea

        // Dividir el texto del TextArea por saltos de línea
        String[] lines = textAreaData.split("\n");

        // Iterar sobre cada línea y agregar su contenido seguido de una coma y un espacio al ArrayList
        for (String line : lines) {
            atributes.add(line.trim() + ", ");
        }

        // Si prefieres que la última línea no termine con una coma y un espacio, puedes eliminarlos así:
        if (!atributes.isEmpty()) {
            String lastElement = atributes.get(atributes.size() - 1);
            atributes.set(atributes.size() - 1, lastElement.substring(0, lastElement.length() - 2)); // Eliminar la coma y el espacio del último elemento
        }

        System.out.println(atributes);
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
            if (!atributeData[0].matches("[a-zA-Z]+([0-9]+)?")) {
                errorLabel1.setText("Invalid attribute name: " + atributeData[0] + " from " +contador + "º Atribute" );
                changeOpacityOnAndOff(errorLabel1);
            }

            // Check data type
            String dataType = atributeData[1].toUpperCase().replaceAll(",", "");
            if (dataType.startsWith("VARCHAR")) {
                if (!dataType.matches("VARCHAR\\(\\d+\\)")) {
                    errorLabel2.setText("Invalid data type: " + atributeData[1] + " from " +contador + " Atribute" );
                    changeOpacityOnAndOff(errorLabel2);
                }
            } else if (!Arrays.asList(validDataTypes).contains(dataType)) {
                errorLabel2.setText("Invalid data type: " + atributeData[1] + " from " +contador + " Atribute" );
                changeOpacityOnAndOff(errorLabel2);
            }

            // Check restrictions
            for (int i = 2; i < atributeData.length; i++) {
                if (!Arrays.asList(validRestrictions).contains(atributeData[i].toUpperCase().replaceAll(",", ""))) {
                    errorLabel3.setText("Invalid restriction: " + atributeData[i] + " from " +contador + " Atribute" );
                    changeOpacityOnAndOff(errorLabel3);
                    break;
                }
            }
        }
    }
    @FXML
    private void fillTableDefaultValues() {
        ArrayList<String> tablesAndAttributes = JDBC.getTablesAndAttributes();
        for (String tableAndAttributes : tablesAndAttributes) {
            String[] split = tableAndAttributes.split(": ");
            String tableName = split[0];
            String[] attributes = split[1].split(", ");
            ArrayList<String> data = new ArrayList<>();
            for (String attribute : attributes) {
                String[] attributeSplit = attribute.split(" ");
                String attributeType = attributeSplit[1];
                data.add(checkAndFillAttribute(attributeType));
            }
            JDBC.insertData(tableName, data);
        }
    }

    private String checkAndFillAttribute(String attributeType){
        return switch (attributeType) {
            case "INT" -> "1";
            case "VARCHAR" -> "Def";
            case "TEXT" -> "DefaultText";
            case "DATE" -> "2021-05-05";
            case "TIMESTAMP" -> "2021-05-05 12:00:00";
            case "FLOAT" -> "1.0";
            case "DOUBLE" -> "1.00";
            case "DECIMAL" -> "1.0";
            case "BOOLEAN" -> "true";
            default -> "0";
        };
    }



    private void changeOpacityOnAndOff(Label label) {
        label.setOpacity(1);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> label.setOpacity(0)));
        timeline.play();
    }

    @FXML
    private void goNextScene() throws IOException {
        fader.fadeNextScene(parentNode, 2, "Borrado.fxml");
    }
}