import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class passAdmin {

    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3305/tc-db-main?serverTimezone=Europe/Moscow";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            System.out.println("Выполнено соединение с MariaDB.");

            ResultSet resultSet = statement.executeQuery("select USER_PASSWORD from personal where id = 5;");

            String passwordAdm = null;

            while (resultSet.next()) {
                passwordAdm = resultSet.getString(1);
            }
            
            ProcessBuilder builder = new ProcessBuilder("sphinxd.exe", "dc", passwordAdm).inheritIO();
            builder.redirectInput(ProcessBuilder.Redirect.PIPE);
            Process process = builder.start();

            String output;
            InputStream inputStream = process.getInputStream();
            output = new String(inputStream.readAllBytes());

            System.out.println("Пароль администратора: " + output);

            // ждем завершения процесса
            process.waitFor();
            connection.close();

            System.out.println("Соедиение с MariaDB - разорвано.");

            Scanner scan = new Scanner(System.in);

            System.out.print("Нажмите любую клавишу для продолжения . . . ");
            scan.nextLine();

        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");

        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");

        }
    }


}
