package mysqlconnection;

import javafx.scene.input.Dragboard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JDBC {

    public static String url_db = "jdbc:mysql://localhost:3306/agenda";
    public static String user_db = "agenda";
    public static String password_db= "agenda";

    public static boolean register(String username, String lastname, String email, String userpassword){
        try{

            if(!checkUser(username)){
                Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

                PreparedStatement insertUser = connection.prepareStatement(
                        "INSERT INTO USERS(username, lastname, email, password) VALUES(?,?,?,?)"
                );

                insertUser.setString(1, username);
                insertUser.setString(2, lastname);
                insertUser.setString(3, email);
                insertUser.setString(4, userpassword);

                insertUser.executeUpdate();
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static boolean checkUser(String email){
        try {
            Connection connection = DriverManager.getConnection(url_db,user_db,password_db);

            PreparedStatement checkUserExists = connection.prepareStatement(
                    "SELECT * FROM USERS WHERE EMAIL = ?"
            );
            checkUserExists.setString(1,email);

            ResultSet resultSet = checkUserExists.executeQuery();

            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace(System.err);
        }

        return true;
    }

    public static boolean validateLogin(String email, String userpassword){
        try {
            Connection connection = DriverManager.getConnection(url_db,user_db,password_db);

            PreparedStatement validateUser = connection.prepareStatement(
                    "SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD = ?"
            );

            validateUser.setString(1,email);
            validateUser.setString(2,userpassword);

            ResultSet resultSet = validateUser.executeQuery();

            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace(System.err);
        }
        return true;
    }

    public static String getUser(String email) {
        try {
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            PreparedStatement getUser = connection.prepareStatement(
                    "SELECT * FROM USERS WHERE EMAIL = ?"
            );
            getUser.setString(1, email);

            ResultSet resultSet = getUser.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                return username;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }



    public static void main(String[] args) {
        String tableName = "test2";
        String[] data = {"1", "2"}; //en este caso la tabla tiene dos valores int sino da error
        JDBC.insertData(tableName, data);
    }
        public static void createTable(String nombre, ArrayList<String> atributos){
        try {
            // Establecer conexión con la base de datos
            Connection conexion = DriverManager.getConnection(url_db, user_db, password_db);

            // Unir los atributos en una cadena
            String atributosString = String.join(" ", atributos);

            // Eliminar las comas adicionales y los espacios antes de ejecutar la consulta
            atributosString = atributosString.replaceAll(",\\s+", ",");

            // Crear una sentencia SQL para crear la tabla
            Statement sentencia = conexion.createStatement();


            // Definir la sentencia SQL para crear la tabla
            String sql = "CREATE TABLE IF NOT EXISTS " + nombre + " ("
                    + atributosString
                    + ")";

            for(int i = 0;i<atributos.size();i++){
                System.out.println(atributos.get(i));
            }
            // Ejecutar la sentencia SQL
            sentencia.executeUpdate(sql);

            // Cerrar la conexión y la sentencia
            sentencia.close();
            conexion.close();

            System.out.println("Tabla creada exitosamente.");

        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    //funcion para devolver las tablas y sus atributos
    //funcion para insertar un dato concreto en una tabla concreta
    public static boolean insertData(String tableName, String... data) {
        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            // Construir la consulta SQL para la inserción de datos
            StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" VALUES (");
            for (int i = 0; i < data.length; i++) {
                sql.append("?");
                if (i < data.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");

            // Crear la declaración preparada
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            // Establecer los valores de los parámetros
            for (int i = 0; i < data.length; i++) {
                preparedStatement.setString(i + 1, data[i]);
            }

            // Ejecutar la consulta
            preparedStatement.executeUpdate();

            // Cerrar la conexión y la declaración preparada
            preparedStatement.close();
            connection.close();

            System.out.println("Datos insertados en la tabla '" + tableName + "' exitosamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar datos en la tabla '" + tableName + "': " + e.getMessage());
            return false;
        }
    }
}