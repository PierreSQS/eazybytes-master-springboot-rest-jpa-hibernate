package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Holiday;
import com.eazybytes.eazyschool.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HolidayService holidaysSrv;

    @GetMapping("/holidays/{display}")
    public String displayHolidays(@PathVariable String display,Model model) {
        if(null != display && display.equals("all")){
            model.addAttribute("festival",true);
            model.addAttribute("federal",true);
        }else if(null != display && display.equals("federal")){
            model.addAttribute("federal",true);
        }else if(null != display && display.equals("festival")){
            model.addAttribute("festival",true);
        }

        List<Holiday> holidayList = holidaysSrv.listHolidays();

        Map<Holiday.Type, List<Holiday>> holidaysByType =
                holidayList.stream().collect(groupingBy(Holiday::getType));
        holidaysByType.forEach((type, holidays) -> model.addAttribute(type.toString(),holidays));
        return "holidays.html";
    }

}
