package org.de.jdbc.statement.batch;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");
        Statement stmt = con.createStatement();


        stmt.addBatch("select `id`, `name`, `updated at`, `contents`, `price` from product where id between 1 and 5");
        stmt.addBatch("UPDATE product SET `price` = `price` + 10000 WHERE id = 1");

        try {
            int[] results = stmt.executeBatch();
        } catch (BatchUpdateException batchUpdateException){
            System.out.println(batchUpdateException.getMessage());
        }

        stmt.addBatch("UPDATE product SET `price` = `price` + 10000 WHERE id = 1");
        stmt.addBatch("UPDATE product SET `price` = `price` + 10000 WHERE id = 2");
        stmt.addBatch("UPDATE product SET `price` = `price` + 10000 WHERE id between 3 and 5");
        int[] results = stmt.executeBatch();

        for (int result: results) {
            if (result >= 0) {
                System.out.println("result of update:" + result);
            }
        }

        con.close();

    }
}
