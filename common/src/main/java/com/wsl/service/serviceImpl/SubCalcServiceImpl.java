package com.wsl.service.serviceImpl;

import com.wsl.service.CalcService;
import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/6/27
 */
@Component
public class SubCalcServiceImpl implements CalcService {
    @Override
    public String calcType() {
        return "sub";
    }

    @Override
    public String calc(String string) {
        return calcType().concat(string);
    }
}
