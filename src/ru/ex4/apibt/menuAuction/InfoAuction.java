package ru.ex4.apibt.menuAuction;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.model.PairSetting;
import ru.ex4.apibt.model.Ticker;
import ru.ex4.apibt.model.UserInfo;
import ru.ex4.apibt.service.PairSettingsService;
import ru.ex4.apibt.service.TickerOldService;
import ru.ex4.apibt.service.UserInfoService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class InfoAuction {
    public void start() {
        try {
//            System.out.println("|pair\t|buyPrice\t|sellPrice\t|lastTrade\t|high\t|low\t|avg\t|vol\t|volCurr\t|volat\t| ");
            List<UserInfo.BalanceValue> balanceList = UserInfoService.getBalance();

            balanceList.forEach(balance -> {
                if (balance.getAmountBalance().floatValue() > 0 || balance.getAmountReserved().floatValue()>0) {
                    System.out.println(balance.toString());
                }
            });

            List<Ticker> tickerList = TickerOldService.getTickerDtos();
            for (Ticker ticker : tickerList) {
                final String[] s = {"", ""};
                PairSetting pairSettingByPair = PairSettingsService.getPairSettingByPair(ticker.getPair());
                float minBalances = ticker.getBuyPrice().floatValue() * pairSettingByPair.getMinQuantity() + (ticker.getBuyPrice().floatValue() * pairSettingByPair.getMinQuantity()) * IExConst.STOCK_FEE;

                for (UserInfo.BalanceValue balance : balanceList) {
//                    if (balance.getAmountBalance().floatValue() > minBalances) {
                    if ((ticker.getPair().endsWith(IExConst.PAIR_PREFIX + balance.getCurrency())) && ticker.getSellPrice().floatValue() < ticker.getAvg()) {
                        s[0] = "- BUY ";
                    } else if (ticker.getPair().startsWith(balance.getCurrency() + IExConst.PAIR_PREFIX) && ticker.getBuyPrice().floatValue() > ticker.getAvg()) {
                        s[1] = "- SELL ";
                    }
//                    }

                }
                System.out.println(s[0] + ticker.toString() + ", \t\t волатильность: " + ((ticker.getHigh() - ticker.getLow()) / ticker.getLow() * 100));
            }


        } catch (IOException | SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

}
