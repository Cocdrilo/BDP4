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
        Scanner scanner = new Scanner(System.in);

        // Pedir al usuario que ingrese el nombre de la tabla
        System.out.print("Ingrese el nombre de la tabla: ");
        String nombreTabla = scanner.nextLine();

        // Pedir al usuario que ingrese los atributos de la tabla con restricciones
        ArrayList<String> atributos = new ArrayList<>();
        boolean continuar = true;
        while (continuar) {
            System.out.print("Ingrese un atributo (nombre tipo_dato restricciones) o escriba 'fin' para terminar: ");
            String entrada = scanner.nextLine();
            if (entrada.equalsIgnoreCase("fin")) {
                continuar = false;
            } else {
                atributos.add(entrada+", ");
            }
        }

        // Eliminar la coma del último elemento del ArrayList
        if (!atributos.isEmpty()) {
            String ultimoAtributo = atributos.get(atributos.size() - 1);
            ultimoAtributo = ultimoAtributo.substring(0, ultimoAtributo.length() - 2); // Eliminar coma y espacio
            atributos.set(atributos.size() - 1, ultimoAtributo);
        }

        System.out.println(atributos);
        createTable(nombreTabla, atributos);
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
}