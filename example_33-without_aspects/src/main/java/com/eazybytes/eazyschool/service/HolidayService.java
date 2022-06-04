package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.model.Holiday;
import com.eazybytes.eazyschool.repository.HolidaysRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    private final HolidaysRepository holidaysRepo;

    public HolidayService(HolidaysRepository holidaysRepo) {
        this.holidaysRepo = holidaysRepo;
    }

    public List<Holiday> listHolidays() {
        return holidaysRepo.listHolidays();
    }
}
