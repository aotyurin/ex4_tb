package ru.ex4.apibt.dao;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.model.UserTrade;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.util.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryTradeDao {
    private JdbcTemplate jdbcTemplate;

    public HistoryTradeDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<UserTrade.UserTradeValue> UserTradeDto(String orderId) {
        try {
            String sql = "SELECT \n" +
                    "  orderId, \n" +
                    "  pair, \n" +
                    "  tradeId, \n" +
                    "  type, \n" +
                    "  price, \n" +
                    "  quantity, \n" +
                    "  amount, \n" +
                    "  date \n" +
                    "FROM History_Trades \n" +
                    "WHERE orderId=:orderId";

            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("orderId", orderId);

            List<UserTrade.UserTradeValue> trades = new ArrayList<>();

            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            while (resultSet.next()) {
                trades.add(new UserTrade.UserTradeValue(resultSet.getString("orderId"),
                        resultSet.getString("pair"),
                        resultSet.getFloat("amount"),
                        DateUtil.parse(resultSet.getString("date")),
                        resultSet.getFloat("price"),
                        resultSet.getFloat("quantity"),
                        resultSet.getLong("tradeId"),
                        TypeOrder.valueOf(resultSet.getString("type"))));
            }
            return trades;
        } catch (SQLException | ParseException e) {
            Logs.error(e.getMessage());
        }

        return Collections.emptyList();
    }

    public void update(List<UserTrade> userTrades) {
        String sql = "INSERT OR REPLACE INTO History_Trades VALUES (:orderId, :pair, :tradeId, :type, :price, :quantity, :amount, :date);";
        for (UserTrade userTrade : userTrades) {
            List<UserTrade.UserTradeValue> trades = userTrade.getTrades();
            for (UserTrade.UserTradeValue trade : trades) {
                PreparedParamsSetter prs = new PreparedParamsSetter();
                prs.setValues("orderId", trade.getOrderId());
                prs.setValues("pair", trade.getPair());
                prs.setValues("tradeId", trade.getTradeId());
                prs.setValues("type", trade.getType().name());
                prs.setValues("price", trade.getPrice());
                prs.setValues("quantity", trade.getQuantity());
                prs.setValues("amount", trade.getAmount());
                prs.setValues("date", DateUtil.format(trade.getDate()));


                jdbcTemplate.executeUpdate(sql, prs);
            }

        }

    }
}
