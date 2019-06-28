package com.wsl.service.serviceImpl;

import com.wsl.service.CalcService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author wsl
 * @date 2019/6/27
 */
@Component
public class AddCalcServiceImpl implements CalcService {
    @Override
    public String calcType() {
        return "add";
    }

    @Override
    public String calc(String string) {
        return calcType().concat(string);
    }
}
