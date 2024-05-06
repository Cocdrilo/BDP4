package edu.bd4.bdp4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mysqlconnection.JDBC;

import java.io.IOException;
import java.util.List;

public class Select {
    @FXML
    private AnchorPane parentNode;
    @FXML
    private TextField tableNameField;
    @FXML
    private TextArea resultsArea;
    @FXML
    private Button getUsersButton;

    @FXML
    private TextField tableInputField;
    @FXML
    private TextField deptNameInputField;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button fetchUsersButton;
    @FXML
    private Button backButton;

    FadeUtilityClass fader;

    public Select() {
        this.fader = new FadeUtilityClass();
    }

    @FXML
    protected void onFetchUsersButton() {
        String tableName = tableInputField.getText();
        String departmentName = deptNameInputField.getText();
        List<String> users = JDBC.getUsersWithNamesStartingWithCAndDepartment(tableName, departmentName);
        outputArea.clear();
        for (String user : users) {
            outputArea.appendText(user + "\n");
        }
        if(users.isEmpty()){
            outputArea.setText("No users found.");
        }
    }

    @FXML
    protected void onGetUsersButton() {
        String tableName = tableNameField.getText();
        List<String> users = JDBC.getUsersWithNamesStartingWithA(tableName);
        resultsArea.clear();
        for (String user : users) {
            resultsArea.appendText(user + "\n");
        }
        if (users.isEmpty()) {
            resultsArea.setText("No users found.");
        }
    }
    @FXML
    protected void backToMain() throws IOException {
        fader.fadeNextScene(parentNode,1, "VisualInterface.fxml");
    }

}
