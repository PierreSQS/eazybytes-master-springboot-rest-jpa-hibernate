package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Holiday;
import com.eazybytes.eazyschool.repository.HolidaysRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
public class HolidaysController {

    public static final String FESTIVAL_ATTR = "festival";
    public static final String FEDERAL_ATTR = "federal";

    private final HolidaysRepository holidaysRepository;

    public HolidaysController(HolidaysRepository holidaysRepository) {
        this.holidaysRepository = holidaysRepository;
    }

    @GetMapping("/holidays/{display}")
    public String displayHolidays(@PathVariable String display,Model model) {
        if (null != display && display.equals("all")) {
            model.addAttribute(FESTIVAL_ATTR, true);
            model.addAttribute(FEDERAL_ATTR, true);
        } else if (null != display && display.equals(FEDERAL_ATTR)) {
            model.addAttribute(FEDERAL_ATTR, true);
        } else if (null != display && display.equals(FESTIVAL_ATTR)) {
            model.addAttribute(FESTIVAL_ATTR, true);
        }

        List<Holiday> holidayList = holidaysRepository.findAll();

        Map<Holiday.Type, List<Holiday>> holydaysMap = holidayList.stream().collect(Collectors.groupingBy(Holiday::getType));

        holydaysMap.forEach((type, holidays) -> {
            log.info("Holyday-Type: {}, Holidays: {}", type, holidays);
            model.addAttribute(type.toString(), holidays);
        });

        return "holidays.html";
    }

}
