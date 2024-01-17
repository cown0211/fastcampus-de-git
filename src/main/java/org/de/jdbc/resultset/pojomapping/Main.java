package org.de.jdbc.resultset.pojomapping;

import org.de.jdbc.mapper.Product;
import org.de.jdbc.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.de.jdbc.mapper.ResultSetMapper.*;

public class Main {
    public static void main(String[] args) {
        try {
//here de-jdbc is database name, root is username and password is null. Fix them for your database settings.
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/de-jdbc", "root", "1234");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from product");

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = ResultSetMapper.create(rs);
                System.out.println(create(rs));
                products.add(product);
            }

            products.stream().forEach(it -> it.setPrice(it.getPrice() - 1000));
            System.out.println(create(rs));
            con.close();
        } catch (Exception e) {System.out.println(e);}
    }


}

