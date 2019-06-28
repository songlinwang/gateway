package com.wsl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wsl
 * @date 2019/6/27
 */
@Slf4j
@Component
@Configuration
public class Manager implements ApplicationListener<ApplicationContextEvent> {


    @Autowired
    private List<CalcService> calcServiceList;


    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        for (CalcService calcService : calcServiceList) {
            log.error(calcService.calcType());
            typeCalcMap().put(calcService.calcType(), calcService);
        }
    }

    @Bean(name = "typeCalcMap")
    public Map<String, CalcService> typeCalcMap(){
        return new LinkedHashMap<>();
    }
}
