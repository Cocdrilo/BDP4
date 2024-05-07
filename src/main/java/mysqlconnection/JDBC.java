package mysqlconnection;

import javafx.scene.input.Dragboard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class JDBC {

    // URL de la base de datos
    public static String url_db = "jdbc:mysql://localhost:3306/agenda";
    // Usuario de la base de datos
    public static String user_db = "agenda";
    // Contraseña de la base de datos
    public static String password_db = "agenda";

    // Método principal
    public static void main(String[] args) {

    }

    // Función para crear una tabla en la base de datos
    public static void createTable(String nombre, ArrayList<String> atributos) {
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

            // Imprimir los atributos para depuración
            for (int i = 0; i < atributos.size(); i++) {
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

    // Función para obtener información sobre las tablas y sus atributos
    public static ArrayList<String> getTablesAndAttributes() {
        ArrayList<String> tablesInfo = new ArrayList<>();

        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);
            DatabaseMetaData metaData = connection.getMetaData();

            // Obtener los nombres de las tablas
            ResultSet tablesResultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                List<String> attributes = new ArrayList<>();

                // Obtener los atributos de cada tabla
                ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);
                while (columnsResultSet.next()) {
                    String columnName = columnsResultSet.getString("COLUMN_NAME");
                    String columnType = columnsResultSet.getString("TYPE_NAME");
                    attributes.add(columnName + " " + columnType);
                }
                columnsResultSet.close();

                // Unir los atributos en una cadena y agregarlos a la lista de información de la tabla
                String attributesString = String.join(", ", attributes);
                tablesInfo.add(tableName + ": " + attributesString);
            }
            tablesResultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tablesInfo;
    }

    // Función para insertar datos en una tabla
    public static boolean insertData(String tableName, ArrayList<String> data) {
        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            // Construir la consulta SQL para la inserción de datos
            StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" VALUES (");
            for (int i = 0; i < data.size(); i++) {
                sql.append("?");
                if (i < data.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");

            // Crear la declaración preparada
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            // Establecer los valores de los parámetros
            for (int i = 0; i < data.size(); i++) {
                preparedStatement.setObject(i + 1, data.get(i));
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

    // Función para eliminar datos de una tabla
    public static boolean deleteData(String tableName, String columnName, Object value) {
        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            // Construir la consulta SQL para eliminar datos
            String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";

            // Crear la declaración preparada
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Establecer el valor del parámetro
            preparedStatement.setObject(1, value);

            // Ejecutar la consulta
            int rowsAffected = preparedStatement.executeUpdate();

            // Cerrar la conexión y la declaración preparada
            preparedStatement.close();
            connection.close();

            // Verificar si se eliminó alguna fila
            if (rowsAffected > 0) {
                System.out.println("Datos eliminados de la tabla '" + tableName + "' exitosamente.");
                return true;
            } else {
                System.out.println("No se encontraron datos para eliminar en la tabla '" + tableName + "'.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar datos de la tabla '" + tableName + "': " + e.getMessage());
            return false;
        }
    }

    // Función para actualizar el teléfono de un dato en la tabla persona
    public static boolean updatePhoneNumber(Object newValue, String idValue) {
        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            // Construir la consulta SQL para actualizar el teléfono
            String sql = "UPDATE persona SET telefono = ? WHERE idPersona = ?";

            // Crear la declaración preparada
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Establecer los valores de los parámetros
            preparedStatement.setObject(1, newValue);
            preparedStatement.setObject(2, idValue);

            // Ejecutar la consulta
            int rowsAffected = preparedStatement.executeUpdate();

            // Cerrar la conexión y la declaración preparada
            preparedStatement.close();
            connection.close();

            // Verificar si se actualizaron filas
            if (rowsAffected > 0) {
                System.out.println("Teléfono actualizado en la tabla 'persona' exitosamente.");
                return true;
            } else {
                System.out.println("No se encontraron datos para actualizar en la tabla 'persona'.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el teléfono en la tabla 'persona': " + e.getMessage());
            return false;
        }
    }

    // Función para obtener los nombres de usuario que empiezan por "A"
    public static List<String> getUsersWithNamesStartingWithA(String tableName) {
        List<String> users = new ArrayList<>();
        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            // Construir la consulta SQL para obtener los usuarios
            String sql = "SELECT * FROM " + tableName + " WHERE nombre LIKE 'A%'";

            // Crear la declaración
            Statement statement = connection.createStatement();

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery(sql);

            // Iterar sobre los resultados y añadir los nombres de usuario a la lista
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                users.add(nombre);
            }

            // Cerrar la conexión y el statement
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }
        return users;
    }

    // Función para obtener los nombres de usuario que empiezan por "C" y pertenecen a un departamento dado
    public static List<String> getUsersWithNamesStartingWithCAndDepartment(String tableName, String departmentName) {
        List<String> users = new ArrayList<>();
        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(url_db, user_db, password_db);

            // Construir la consulta SQL para obtener los usuarios
            String sql = "SELECT p.nombre " +
                    "FROM " + tableName + " p " +
                    "INNER JOIN DEPARTAMENTO d ON p.idDepartamento = d.idDepartamento " +
                    "WHERE p.nombre LIKE 'C%' AND d.Nombre_departamento = ?";

            // Crear la declaración preparada
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, departmentName);

            // Ejecutar la consulta
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterar sobre los resultados y añadir los nombres de usuario a la lista
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                users.add(nombre);
            }

            // Cerrar la conexión y la declaración preparada
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }
        return users;
    }
}