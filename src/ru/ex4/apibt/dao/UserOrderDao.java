package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.log.Logs;

import java.sql.SQLException;

public class UserOrderDao {
    private JdbcTemplate jdbcTemplate;

    public UserOrderDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            Logs.error(e.getMessage());
        }
    }

    // currPrice - цена, на момент создания ордера
    public void save(String orderId, OrderCreateDto orderCreateDto, float currPrice) {
        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("orderId", orderId);
        prs.setValues("pair", orderCreateDto.getPair());
        prs.setValues("quantity", orderCreateDto.getQuantity());
        prs.setValues("price", orderCreateDto.getPrice());
        prs.setValues("type", orderCreateDto.getType().name());
        prs.setValues("currPrice", currPrice);

        jdbcTemplate.executeUpdate("INSERT INTO User_Order VALUES (:orderId, :pair, :quantity, :price, :type, :currPrice);", prs);
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
