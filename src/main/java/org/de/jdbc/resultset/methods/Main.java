package org.de.jdbc.resultset.methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id between 1 and 5");


        // cursor init 이 시점에서는 최초의 값이므로 init 값으로 존재
        if(rs.next()) { // .next() 적용했으므로
            printRs(rs);// 1번 행을 가져올 것으로 기대!!
        }
        if(rs.last()) { // .last() 적용했으므로
            printRs(rs);// 5번행 출력 기대!!
        }
//        if(!rs.next()) { // last에다가 다시 next하면 에러 발생
//            printRs(rs); // After end of result set
//        }
        if (!rs.next()) { // 예외처리
            try {
                printRs(rs);
            } catch (SQLException sqlException) {
                System.out.println("Error code: " + sqlException.getErrorCode() + ", Error Message: " + sqlException.getMessage());
            }
        }
        rs.last(); // cursor를 다시 last로 옮겨 둠

        if (rs.previous()) { // 4번행 나올 것 기대
            printRs(rs);
        }






        int n = 2;
        if (rs.absolute(n)) { // 2번행!!
            System.out.print("절대경로 row: " + n + ", ");
            printRs(rs);
        }
        int r = 3;
        if (rs.relative(r)) { // 위의 2번행에서 + r 해준 5번 행이 나올 것!!
            System.out.print("상대경로 row: " + n + "+" + r + ", ");
            printRs(rs);
        }
        int r2 = -2;
        if (rs.relative(r2)) { // 위의 5번행에서 - r2 해준 3번 행이 나올 것!!
            System.out.print("상대경로 row: " + n + "+" + r + "+" + r2 + ", ");
            printRs(rs);
        }
        con.close();
    }

    private static void printRs(ResultSet rs) throws SQLException {
        System.out.println(rs.getInt(1) + " " + rs.getString(2) + " "
                + rs.getDate(3) + " " + rs.getString(4)
                + " " + rs.getInt(5));

    }


}
