package org.de.jdbc.callablestatement.procedurecall;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
//here de-jdbc is database name, root is username and password is null. Fix them for your database settings.
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");
        CallableStatement stmtProcedureCall = con.prepareCall("call discount_price(?, ?, ?);"); // prepareCall(); SQL문 자체를 파라미터로 받아옴!, 그리고 변수 할당 가능
        stmtProcedureCall.setInt(1,1); // 1번째 ?를 1로 할당하라
        stmtProcedureCall.setInt(2,20); // 2번째 ?를 20으로 할당하라
        stmtProcedureCall.registerOutParameter(3, Types.INTEGER); // 프로시저의 OUT 파라미터, 3번째 변수의 타입을 INTEGER로 할당

        boolean result =  stmtProcedureCall.execute(); // 위의 코드 실행, rs 여부를 TF로 반환

        System.out.println("result: " + result);
        System.out.println("param: " + stmtProcedureCall.getInt(3)); // rs로 받지 않아도 결과값을 바로 가져올 수 있음!!

        if (!result) { // 업데이트 된 행의 개수 반환하는 결과 셋
            System.out.println("update count: " + stmtProcedureCall.getUpdateCount());
        }

    }
}