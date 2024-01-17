package org.de.jdbc.preparedstatement;

import com.mysql.cj.protocol.Resultset;
import org.de.jdbc.mapper.Product;
import org.de.jdbc.mapper.ResultSetMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");
        PreparedStatement psUpdate = con.prepareStatement("update product set `price` = `price` + ? where id = ?");
        // ?를 parameter 개념으로 작성 가능

        // 아래는 실제 ?에 할당하는 개념
        psUpdate.setInt(1, 1150); // 첫번쨰 ?에게 1150 할당
        psUpdate.setInt(2, 9);    // 두번쨰 ?에게 9 할당
        // 실행부
        int updateResult = psUpdate.executeUpdate();
        System.out.println("result of update: " + updateResult);
        // DB 가보면 반영돼 있는것 확인 가능


        // select 문에도 할당 가능
        PreparedStatement psSelect = con.prepareStatement("select `id`, `name`, `updated_at`, `contents`, `price` from product where id between ? and ?");
        psSelect.setInt(1,1);
        psSelect.setInt(2,5);
        ResultSet rs = psSelect.executeQuery();

        while(rs.next()) {
            ResultSetMapper.printRs(rs);
        }


        // select 문 예시 2
        psSelect.setInt(1,6);
        psSelect.setInt(2,10);
        ResultSet rs2 = psSelect.executeQuery();
        List<Product> productList = new ArrayList<>();
        while(rs2.next()) {
            productList.add(ResultSetMapper.create(rs2));
        }


        PreparedStatement psUpdateProduct = con.prepareStatement(
                "update product set `id` = ?, `name` = ?, `updated_at` = ?, `contents` = ?, `price` = ? where id = ?");

        for(Product product: productList) {
            product.setPrice(product.getPrice() - 1000);
            product.setUpdatedAt(LocalDateTime.now());

            psUpdateProduct.setInt(1, product.getId());
            psUpdateProduct.setString(2, product.getName());
            psUpdateProduct.setTimestamp(3, Timestamp.valueOf(product.getUpdatedAt()));
            psUpdateProduct.setString(4, product.getContents());
            psUpdateProduct.setInt(5, product.getPrice());
            psUpdateProduct.setInt(6, product.getId());

            // 이대로 돌리면 반복물 돌리는 꼴인데 이거 말고 아래의 batch 작업 ㄱㄱ
//            int result = psUpdateProduct.executeUpdate();
//            System.out.println("result of query: " + result);

            // 배치로 돌리게 되면
            psUpdateProduct.addBatch();
            psUpdateProduct.clearParameters();
            // 변수 초기화


        }

        int[] results = psUpdateProduct.executeBatch();
        for (int result : results) {
            System.out.println("result of update: " + result);
        }

    }

}
