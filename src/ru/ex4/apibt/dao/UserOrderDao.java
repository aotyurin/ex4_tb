package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.model.OrderCreate;
import ru.ex4.apibt.util.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserOrderDao {
    private JdbcTemplate jdbcTemplate;

    public UserOrderDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // currPrice - цена, на момент создания ордера
    public void save(String orderId, OrderCreate orderCreate, float lastPrice) {
        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("orderId", orderId);
        prs.setValues("pair", orderCreate.getPair());
        prs.setValues("quantity", orderCreate.getQuantity());
        prs.setValues("price", orderCreate.getPrice());
        prs.setValues("type", orderCreate.getType().name());
        prs.setValues("lastPrice", lastPrice);
        prs.setValues("date", DateUtil.format(new Date()));

        jdbcTemplate.executeUpdate("INSERT OR REPLACE INTO User_Order VALUES (:orderId, :pair, :quantity, :price, :type, :lastPrice, :date);", prs);
    }

    public float getLastPriceByOrder(String orderId) {
        try {
            String sql = "SELECT\n" +
                    "  orderId,\n" +
                    "  pair,\n" +
                    "  quantity,\n" +
                    "  price,\n" +
                    "  type,\n" +
                    "  lastPrice, \n" +
                    "  date \n" +
                    "FROM User_Order\n" +
                    "WHERE orderId = :orderId ;";

            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("orderId", orderId);

            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            while (resultSet.next()) {
                return resultSet.getFloat("lastPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteByOrderId(String orderId) {
        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("orderId", orderId);
        jdbcTemplate.executeUpdate("DELETE FROM User_Order WHERE orderId = :orderId;", prs);
    }

    public void setStackOrder(String orderId) {
        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("orderId", orderId);
        jdbcTemplate.executeUpdate("INSERT INTO Stack_Order VALUES (:orderId);", prs);
    }

    public String getStackOrder() {
        try {
            List<String> list = new ArrayList<>();
            ResultSet resultSet = jdbcTemplate.executeQuery("SELECT orderId FROM Stack_Order");
            while (resultSet.next()) {
                list.add(resultSet.getString("orderId"));
            }
            if (list.size() > 0) {
                return list.get(list.size() - 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
