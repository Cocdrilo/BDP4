package edu.bd4.bdp4;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;

public class VisualInterface {
    @FXML
    private TextField tableName;
    @FXML
    private TextArea atributesTextArea;
    @FXML
    private Button createTableBtn;

    public VisualInterface() {
    }


    @FXML
    protected void onCreateTableBtn() {
        String tableNameString = tableName.getText();
        ArrayList <String> Tabla = generateAtributesArrayList();
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
}