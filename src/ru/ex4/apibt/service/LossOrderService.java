package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.LossOrderDao;
import ru.ex4.apibt.dto.LossOrderDto;
import ru.ex4.apibt.model.custom.LossOrder;

import java.util.ArrayList;
import java.util.List;

public class LossOrderService {
    private LossOrderDao lossOrderDao;

    public LossOrderService() {
        this.lossOrderDao = new LossOrderDao();
    }


    public List<LossOrderDto> getTrailingDtoList() {
        List<LossOrderDto> lossOrderDtoList = new ArrayList<>();

        List<LossOrder> lossOrderList = lossOrderDao.getLossOrderList();
        lossOrderList.forEach(lossOrder -> lossOrderDtoList.add(new LossOrderDto(lossOrder.getPair(), lossOrder.getQuantity(), lossOrder.getPrice(),
                lossOrder.getType(), lossOrder.getPriceLoss())));

        return lossOrderDtoList;
    }

    public void save(LossOrderDto lossOrderDto) {
        lossOrderDao.save(convertDtoToModel(lossOrderDto));
    }

    public void delete(LossOrderDto trailingDto) {
        lossOrderDao.delete(convertDtoToModel(trailingDto));
    }

    private LossOrder convertDtoToModel(LossOrderDto lossOrderDto) {
        return new LossOrder(lossOrderDto.getPair(), lossOrderDto.getPrice(), lossOrderDto.getQuantity(), lossOrderDto.getType(),
                lossOrderDto.getCreated(), lossOrderDto.getPriceLoss());
    }

}
