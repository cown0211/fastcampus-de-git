package org.de.jdbc.transaction.isolation;

import org.de.jdbc.mapper.ResultSetMapper;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");
        con.setAutoCommit(false); // autocommit 하지 않도록 명시

        Statement stmt = con.createStatement();
        stmt.executeUpdate("update product set `id` = 109 where `id` = 9;");

        ResultSet rs = stmt.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 109");

        while(rs.next()){
            ResultSetMapper.printRs(rs);
        }

        con.close();
        // auto commit == false이기 때문에 commit 되지 않았을 것임




        Connection con2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        Statement stmt2 = con2.createStatement();

        ResultSet rs2 = stmt2.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 9");
        // 위 con에서 commit이 안됐을 것이므로 id = 109로 해봐야 조회 안될것 == id = 9로 조회
        while(rs2.next()){
            ResultSetMapper.printRs(rs2);
        }

        con2.close();
    }
}
