package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.TrailingDao;
import ru.ex4.apibt.dto.TrailingDto;
import ru.ex4.apibt.model.custom.Trailing;

import java.util.ArrayList;
import java.util.List;

public class TrailingService {
    private TrailingDao trailingDao;

    public TrailingService() {
        this.trailingDao = new TrailingDao();
    }

    public List<TrailingDto> getTrailingDtoList() {
        List<TrailingDto> trailingDtoList = new ArrayList<>();

        List<Trailing> trailingList = trailingDao.getTrailingList();
        trailingList.forEach(trailing -> trailingDtoList.add(new TrailingDto(trailing.getPair(), trailing.getTrendType(), trailing.getPrice(),
                trailing.getDateCreated(), trailing.getDateNotify())));

        return trailingDtoList;
    }
}
