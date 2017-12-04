package ru.ex4.apibt.logic;

import ru.ex4.apibt.dto.LossOrderDto;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.service.LossOrderService;
import ru.ex4.apibt.service.TickerService;

import java.util.List;

public class LossOrderProcess extends Thread {
    private LossOrderService lossOrderService;
    private TickerService tickerService;

    public LossOrderProcess() {
        this.lossOrderService = new LossOrderService();
        this.tickerService = new TickerService();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Logs.info("start 'LossOrderProcessThread', while ...");

            List<LossOrderDto> lossOrderDtoList = lossOrderService.getTrailingDtoList();
            if (lossOrderDtoList.size() == 0) {
                Logs.info("stop 'LossOrderProcessThread', lossOrderDtoList list is empty...");
                currentThread().interrupt();
            }

            List<TickerDto> tickerDtoList = tickerService.getTickerList();
            lossOrderDtoList.forEach(lossOrderDto -> tickerDtoList.forEach(tickerDto -> {
                if (lossOrderDto.getPair().equals(tickerDto.getPair())) {
                    if (lossOrderDto.getType() == TypeOrder.buy_loss) {
                        //todo ...


                        // if:      sellPrice > price               --->>> error
                        // if:      price - priceLoss < sellPrice
                        //     edit:    price = price + priceLoss
                    }
                    if (lossOrderDto.getType() == TypeOrder.buy_loss) {

                    }
                }
            }));
            Wait.sleep(5, "ждем 5 мин и запускаем повторно");
        }
    }


}
