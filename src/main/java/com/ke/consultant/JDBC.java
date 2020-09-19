package com.ke.consultant;


import java.sql.*;
import java.util.ArrayList;

/**
 * Generate database and add or edit table
 */
public class JDBC {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Student";

    public void test(String[] args) throws Exception {
        try {
//            createDB();
//            editDB(111111, "John", "Doe", "john.doe@gmail.com", "Student");
//            editDB(222222, "Janee", "Doe", "jane.doe@gmail.com", "Student");
//            editDB(222222, "Jane", "Doe", "jane.doe@gmail.com", "Student");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void createDB() {
        try (Connection con = DriverManager.getConnection(DATABASE_URL, "root", "root");
             Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE TABLE Student (\n" +
                    "id INT(6)  PRIMARY KEY,\n" +
                    "firstname VARCHAR(30) NOT NULL,\n" +
                    "lastname VARCHAR(30) NOT NULL,\n" +
                    "email VARCHAR(50))");
        } catch (SQLException e) {
            throw new RuntimeException("SQl Exception");
        }
    }

    public void addDB(Integer id, String first, String last, String mail, String table) {
        String query = "insert into " + table + " values(?,?,?,?)";
        try (Connection con = DriverManager.getConnection(DATABASE_URL, "root", "root");
             PreparedStatement statement = con.prepareStatement(query);) {
            statement.setInt(1, id);
            statement.setString(2, first);
            statement.setString(3, last);
            statement.setString(4, mail);
            int count = statement.executeUpdate();
            System.out.println(count + " rows affected");
        } catch (SQLException e) {
            throw new RuntimeException("Driver Exception");
        }
    }

    public StudentBean[] printDB(ResultSet resultSet) throws SQLException {
        ArrayList<StudentBean> studentBeans = new ArrayList<>();
        StudentBean bean = new StudentBean();
        while (resultSet.next()) {
            bean.setId(resultSet.getInt("id"));
            bean.setFirst(resultSet.getString("firstname"));
            bean.setLast(resultSet.getString("lastname"));
            bean.setMail(resultSet.getString("email"));
            studentBeans.add(bean);
        }
        StudentBean[] beans = new StudentBean[0];
        studentBeans.toArray(beans);
        return beans;
    }

    public void editDB(Integer id, String first, String last, String mail, String table) {
        String query = "select * from " + table + " where id = ?";
        try (Connection con = DriverManager.getConnection(DATABASE_URL, "root", "root");
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                query = "update " + table + " set firstname = ?, lastname = ?, email = ? where id = ?";
                try (PreparedStatement statement2 = con.prepareStatement(query);
                ) {
                    statement2.setString(1, first);
                    statement2.setString(2, last);
                    statement2.setString(3, mail);
                    statement2.setInt(4, id);
                    int count = statement2.executeUpdate();
                    System.out.println(count + " rows affected");
                }
            } else {
                addDB(id, first, last, mail, table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SQL Exception");
        }
    }

    public StudentBean[] queryDB(String table) throws Exception {
        try (Connection con = DriverManager.getConnection(DATABASE_URL, "root", "root");
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery("select * from " + table);) {
            return printDB(rs);
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }
    }

}

