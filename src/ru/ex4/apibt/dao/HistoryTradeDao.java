package ru.ex4.apibt.dao;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.dto.TypeOrder;
import ru.ex4.apibt.dto.UserTradeDto;
import ru.ex4.apibt.log.Logs;

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


    public List<UserTradeDto.UserTrade> UserTradeDto(String orderId) {
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

            List<UserTradeDto.UserTrade> trades = new ArrayList<>();

            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            while (resultSet.next()) {
                trades.add(new UserTradeDto.UserTrade(resultSet.getString("orderId"),
                        resultSet.getString("pair"),
                        resultSet.getFloat("amount"),
                        new SimpleDateFormat(IExConst.DATE_FORMAT).parse(resultSet.getString("date")),
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

    public void update(List<UserTradeDto> userTrades) {
        String sql = "INSERT OR REPLACE INTO History_Trades VALUES (:orderId, :pair, :tradeId, :type, :price, :quantity, :amount, :date);";
        for (UserTradeDto userTrade : userTrades) {
            List<UserTradeDto.UserTrade> trades = userTrade.getTrades();
            for (UserTradeDto.UserTrade trade : trades) {
                PreparedParamsSetter prs = new PreparedParamsSetter();
                prs.setValues("orderId", trade.getOrderId());
                prs.setValues("pair", trade.getPair());
                prs.setValues("tradeId", trade.getTradeId());
                prs.setValues("type", trade.getType().name());
                prs.setValues("price", trade.getPrice());
                prs.setValues("quantity", trade.getQuantity());
                prs.setValues("amount", trade.getAmount());
                prs.setValues("date", new SimpleDateFormat(IExConst.DATE_FORMAT).format(trade.getDate()));


                jdbcTemplate.executeUpdate(sql, prs);
            }

        }

    }
}
