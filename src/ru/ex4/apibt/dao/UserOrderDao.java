package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.log.Logs;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void save(String orderId, OrderCreateDto orderCreateDto, float lastPrice) {
        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("orderId", orderId);
        prs.setValues("pair", orderCreateDto.getPair());
        prs.setValues("quantity", orderCreateDto.getQuantity());
        prs.setValues("price", orderCreateDto.getPrice());
        prs.setValues("type", orderCreateDto.getType().name());
        prs.setValues("lastPrice", lastPrice);

        jdbcTemplate.executeUpdate("INSERT OR REPLACE INTO User_Order VALUES (:orderId, :pair, :quantity, :price, :type, :lastPrice);", prs);
    }

    public float getLastPriceByOrder(String orderId) {
        try {
            String sql = "SELECT\n" +
                    "  orderId,\n" +
                    "  pair,\n" +
                    "  quantity,\n" +
                    "  price,\n" +
                    "  type,\n" +
                    "  lastPrice\n" +
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

    public void deleteAll() {
        jdbcTemplate.executeUpdate("DELETE FROM User_Order;");
    }
}
