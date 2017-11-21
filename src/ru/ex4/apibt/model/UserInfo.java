package ru.ex4.apibt.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;
import java.util.*;

public class UserInfo {
    //      идентификатор пользователя
    @JsonProperty("uid")
    private String uid;
    //      дата и время сервера
    private Date serverData;
    //      доступный баланс
    private ArrayList<Balance> balances;
    //      баланс в ордерах
    private ArrayList<Balance> reserved;

    private ArrayList<BalanceValue> balanceValues;

    public UserInfo(String uid, Date serverData, ArrayList<BalanceValue> balanceValues) {
        this.uid = uid;
        this.serverData = serverData;
        this.balanceValues = balanceValues;
    }

    public UserInfo() {

    }

    @JsonProperty("server_date")
    private void setServerData(Long serverData) {
        this.serverData = new Date(serverData * 1000L);
    }

    @JsonProperty("balances")
    private void setBalances(LinkedHashMap<Object, Object> balances) {
        this.balances = new ArrayList<>();
        convertObjectToBalance(this.balances, balances);
    }

    @JsonProperty("reserved")
    private void setReserved(LinkedHashMap<Object, Object> reserved) {
        this.reserved = new ArrayList<>();
        convertObjectToBalance(this.reserved, reserved);
    }

    public UserInfo(String uid, Date serverData, ArrayList<Balance> balances, ArrayList<Balance> reserved) {
        this.uid = uid;
        this.serverData = serverData;
        this.balances = balances;
        this.reserved = reserved;
    }

    private void convertObjectToBalance(ArrayList<Balance> thisBalance, LinkedHashMap<Object, Object> hashMap) {
        if (hashMap != null) {
            Set<Object> objects = hashMap.keySet();
            for (Object key : objects) {
                Object o = hashMap.get(key);
                Balance pairSum = new Balance(key.toString(), BigDecimal.valueOf(Double.valueOf(o.toString())));
                thisBalance.add(pairSum);
            }
        }
    }


    public String getUid() {
        return uid;
    }

    public Date getServerData() {
        return serverData;
    }

    public ArrayList<BalanceValue> getBalanceValues() {
        if (this.balanceValues != null) {
            return this.balanceValues;
        }

        ArrayList<BalanceValue> balanceValues = new ArrayList<>();
        balances.forEach(balance -> {
            final double[] amountReserved = {0};
            reserved.forEach(reserved -> {
                if (reserved.getCurrency().equals(balance.getCurrency())) {
                    amountReserved[0] = reserved.getAmount().floatValue();
                }
            });
            balanceValues.add(new BalanceValue(balance.getCurrency(), balance.getAmount(), BigDecimal.valueOf(amountReserved[0]) ));
        });

        return balanceValues;
    }

    class Balance {
        //  валюта
        String currency;
        //  сумма
        BigDecimal amount;

        Balance(String currency, BigDecimal amount) {
            this.amount = amount;
            this.currency = currency;
        }

        BigDecimal getAmount() {
            return amount;
        }

        String getCurrency() {
            return currency;
        }
    }

    public class BalanceValue {
        String currency;
        BigDecimal amountBalance;
        BigDecimal amountReserved;

        public BalanceValue(String currency, BigDecimal amountBalance, BigDecimal amountReserved) {
            this.currency = currency;
            this.amountBalance = amountBalance;
            this.amountReserved = amountReserved;
        }

        public String getCurrency() {
            return currency;
        }

        public BigDecimal getAmountBalance() {
            return amountBalance;
        }

        public BigDecimal getAmountReserved() {
            return amountReserved;
        }


        @Override
        public String toString() {
            return "BalanceValue{" +
                    "currency='" + currency + '\'' +
                    ", amountBalance=" + amountBalance.toPlainString() +
                    ", amountReserved=" + amountReserved.toPlainString() +
                    '}';
        }
    }
}
