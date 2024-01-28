package org.de.jdbc.transaction.commit.fail;

import org.de.jdbc.mapper.ResultSetMapper;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        try {

            con.setAutoCommit(false);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 2");

            System.out.println("========= before start update ========");
            while (rs.next()){
                ResultSetMapper.printRs(rs);
            }


            stmt.executeUpdate("UPDATE product SET `price` = `price` + 10000 WHERE id = 2");
            stmt.executeUpdate("UPDATE product SET `price` = `price` + 10000 WHERE id = 3");

            stmt.executeUpdate("delete from product where id = 2");
            // success에서는 review를 미리 지우고 product를 지워줘서 성공했으나, 여기선 참조되는 review를 살린 채로 삭제
            // fail 될 것으로 기대

            con.commit();

            // 실행 시 실제로 에러 발생, cannot delete or update a parent row~~
            // 또한 커밋 시 아래 삭제 쿼리 때문에 에러가 발생했으므로 상기 2건은 반영될 수도 있지 않나?
            // ㄴ 커밋이 될 거면 전부 되고, 안될거면 전부 안돼야 하기 때문에 상기 update 2건도 반영 x됨을 MySQL에서 확인
        } catch(SQLException sqlException) {
            System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
        } finally {
            con.close();
        }
        System.out.println("========== after commit failed ===========");


        Connection con2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        Statement stmt2 = con2.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 2");
        // 위에서 update, delete 모두 커밋에 실패했으므로 결과는 before start update 그대로 나올 것으로 기대
        if (rs2.next()){
            ResultSetMapper.printRs(rs2);
        } else {
            System.out.println("no result");
        }
        con2.close();

    }
}
