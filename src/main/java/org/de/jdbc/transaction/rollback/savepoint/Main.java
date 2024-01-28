package org.de.jdbc.transaction.rollback.savepoint;

import org.de.jdbc.mapper.ResultSetMapper;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        try {
            con.setAutoCommit(false);
            // rollback 여부 보기 위해 autocommit off

            PreparedStatement updateStmt = con.prepareStatement("update product set price = price + 10000 where id = ?");
            PreparedStatement selectStmt = con.prepareStatement("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = ?");

            System.out.println(" ============== before start update ============== ");
            // update 하기 이전의 데이터 조회
            selectAndPrintRow(selectStmt, 2);
            selectAndPrintRow(selectStmt, 3);
            selectAndPrintRow(selectStmt, 4);


            // update 실행
            updateStmt.setInt(1, 2);
            updateStmt.executeUpdate();
            Savepoint sp1 = con.setSavepoint(); // session 단위로 savepoint 지정
            updateStmt.setInt(1, 3);
            updateStmt.executeUpdate();
            Savepoint sp2 = con.setSavepoint(); // session 단위로 savepoint 지정
            updateStmt.setInt(1, 4);
            updateStmt.executeUpdate();



            System.out.println();
            System.out.println(" ============= after update in transaction ============");

            // update 된 값들이 조회될 것으로 기대
            selectAndPrintRow(selectStmt, 2);
            System.out.println("savepoint sp1 is here");
            selectAndPrintRow(selectStmt, 3);
            System.out.println("savepoint sp2 is here");
            selectAndPrintRow(selectStmt, 4);


            // rollback 실행
            con.rollback(sp2); // 특정 savepoint 시점까지로 rollback 가능

            System.out.println();
            System.out.println(" ============= after rollback to sp2 in transaction ============");

            // update 되기 전 값들이 조회될 것으로 기대
            selectAndPrintRow(selectStmt, 2);
            selectAndPrintRow(selectStmt, 3);
            selectAndPrintRow(selectStmt, 4);


            con.rollback(sp1); // 특정 savepoint 시점까지로 rollback 가능

            System.out.println();
            System.out.println(" ============= after rollback to sp1 in transaction ============");

            // update 되기 전 값들이 조회될 것으로 기대
            selectAndPrintRow(selectStmt, 2);
            selectAndPrintRow(selectStmt, 3);
            selectAndPrintRow(selectStmt, 4);


        } catch(SQLException sqlException) {
            System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
        } finally {
            con.close();
        }
    }


    private static void selectAndPrintRow(PreparedStatement stmt, int id) throws SQLException {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ResultSetMapper.printRs(rs);
        }
    }
}
