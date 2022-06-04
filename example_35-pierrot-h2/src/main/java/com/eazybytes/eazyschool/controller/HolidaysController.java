package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Holiday;
import com.eazybytes.eazyschool.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Controller
public class HolidaysController {

    public static final String FESTIVAL_ATTR = "festival";
    public static final String FEDERAL_ATTR = "federal";

    private final HolidayService holidaySrv;

    public HolidaysController(HolidayService holidaySrv) {
        this.holidaySrv = holidaySrv;
    }

    @GetMapping("/holidays/{display}")
    public String displayHolidays(@PathVariable String display,Model model) {
        if(null != display && display.equals("all")){
            model.addAttribute(FESTIVAL_ATTR,true);
            model.addAttribute(FEDERAL_ATTR,true);
        }else if(null != display && display.equals(FEDERAL_ATTR)){
            model.addAttribute(FEDERAL_ATTR,true);
        }else if(null != display && display.equals(FESTIVAL_ATTR)){
            model.addAttribute(FESTIVAL_ATTR,true);
        }
        List<Holiday> holidays = holidaySrv.listHolidays();

        Map<Holiday.Type, List<Holiday>> holidaysMap = holidays.stream().collect(groupingBy(Holiday::getType));
        holidaysMap.forEach((type, holidays1) -> {
            log.info("Holiday Type: {}, Holidays: {}",type,holidays1);
            model.addAttribute(type.toString(),holidays1);
        });
        return "holidays.html";
    }

}
