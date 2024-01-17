package org.de.jdbc.transaction.commit.success;

import org.de.jdbc.mapper.ResultSetMapper;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");
        con.setAutoCommit(false);

        Statement stmt = con.createStatement();
        stmt.executeUpdate("UPDATE product SET `price` = `price` + 10000 WHERE id = 1");
        stmt.executeUpdate("UPDATE product SET `price` = `price` + 10000 WHERE id = 2");
        stmt.executeUpdate("UPDATE product SET `price` = `price` + 10000 WHERE id = 3");
        stmt.executeUpdate("delete from review where product_id = 1");
        stmt.executeUpdate("delete from product where id = 1");

        con.commit();
        con.close();



        Connection con2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        Statement stmt2 = con2.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 1");
        // con에서 id=1인 product 날렸으므로 조회 x로 기대
        if (rs2.next()){
            ResultSetMapper.printRs(rs2);
        } else {
            System.out.println("no result");
        }
        con2.close();

    }
}
