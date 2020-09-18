package com.ke.consultant;


import java.sql.*;

/**
 * Generate database and add or edit table
 */
public class JDBC {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Student";
    private static Connection con;
    private static Statement statement;


    public static void main(String[] args) throws Exception {
        try {
            createDB();
            editDB(111111, "John", "Doe", "john.doe@gmail.com");
            editDB(222222, "Janee", "Doe", "jane.doe@gmail.com");
            editDB(222222, "Jane", "Doe", "jane.doe@gmail.com");
            queryDB();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void createDB() {
        try {
            con = DriverManager.getConnection(DATABASE_URL, "root", "root");
            statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE Student (\n" +
                    "id INT(6)  PRIMARY KEY,\n" +
                    "firstname VARCHAR(30) NOT NULL,\n" +
                    "lastname VARCHAR(30) NOT NULL,\n" +
                    "email VARCHAR(50))");
            statement.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("SQl Exception");
        }
    }

    public static void addDB(Integer id, String first, String last, String mail) {
        try {
            con = DriverManager.getConnection(DATABASE_URL, "root", "root");
            String query = "insert into Student values(?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, first);
            statement.setString(3, last);
            statement.setString(4, mail);
            int count = statement.executeUpdate();
            System.out.println(count + " rows affected");
            statement.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("Driver Exception");
        }
    }

    public static void queryDB() throws Exception {
        try {
            con = DriverManager.getConnection(DATABASE_URL, "root", "root");
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Student");
            printDB(rs);
            statement.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("Driver Exception");
        }
    }

    static void printDB(ResultSet resultSet) throws SQLException {
        StudentBean bean = new StudentBean();
        while (resultSet.next()) {
            bean.setId(resultSet.getInt("id"));
            bean.setFirst(resultSet.getString("firstname"));
            bean.setLast(resultSet.getString("lastname"));
            bean.setMail(resultSet.getString("email"));
            System.out.println(bean.toString());
        }
    }

    public static void editDB(Integer id, String first, String last, String mail) {
        try {
            con = DriverManager.getConnection(DATABASE_URL, "root", "root");
            PreparedStatement statement = con.prepareStatement("select * from Student where id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String query = "update Student set firstname = ?, lastname = ?, email = ? where id = ?";
                statement = con.prepareStatement(query);
                statement.setString(1, first);
                statement.setString(2, last);
                statement.setString(3, mail);
                statement.setInt(4, id);
                int count = statement.executeUpdate();
                System.out.println(count + " rows affected");
            } else {
                addDB(id, first, last, mail);
            }
            statement.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }
    }

}

