import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.de.jdbc.mapper.ResultSetMapper;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");

        System.out.println("===== Direct Function Call =====");
        CallableStatement stmtFunctionCall = con.prepareCall("{?= call add_event_prefix(?)}");
        String originalContent = "original";
        System.out.println("original content: " + originalContent);
        stmtFunctionCall.setString(2, originalContent);
        stmtFunctionCall.registerOutParameter(1, Types.VARCHAR);
        boolean result2 = stmtFunctionCall.execute();
        System.out.println(result2);
        System.out.println("after prefix: " + stmtFunctionCall.getString(1));

        System.out.println("===== Function Call in Query =====");
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT `id`, add_event_prefix(`name`) as `name`, `updated_at`, `contents`, `price` from product where id = 1;");
        while (rs.next()) {
            ResultSetMapper.printRs(rs);
        }
    }
}