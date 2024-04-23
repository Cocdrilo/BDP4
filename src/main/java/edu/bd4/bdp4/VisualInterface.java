package edu.bd4.bdp4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import mysqlconnection.JDBC;

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
            if (!Arrays.asList(validDataTypes).contains(atributeData[1].toUpperCase().replaceAll(",", ""))) {
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
        ArrayList <String> Atributes = new ArrayList<>();
        ArrayList <String> Data = new ArrayList<>();
        //Funcion que me de las tablas con sus atributos
        //Atributes = JDBC.getTablesAtributes();
        for (String atribute : Atributes) {
            Data.add(checkAndFillAtribute(atribute));
        }
        //Funcion que devuelva los datos de la tabla
        //JDBC.insertData(Data);
    }

    private String checkAndFillAtribute(String atribute){
        if (atribute.equals("INT")){
            return "1";
        }
        if (atribute.equals("VARCHAR")){
            return "DefaultVchar";
        }
        if (atribute.equals("TEXT")){
            return "DefaultText";
        }
        if (atribute.equals("DATE")){
            return "2021-05-05";
        }
        if (atribute.equals("TIMESTAMP")){
            return "2021-05-05 12:00:00";
        }
        if (atribute.equals("FLOAT")){
            return "1.0";
        }
        if (atribute.equals("DOUBLE")){
            return "1.00";
        }
        if (atribute.equals("DECIMAL")){
            return "1.0";
        }
        if (atribute.equals("BOOLEAN")){
            return "true";
        }
        return "0";
    }



    private void changeOpacityOnAndOff(Label label) {
        label.setOpacity(1);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> label.setOpacity(0)));
        timeline.play();
    }
}