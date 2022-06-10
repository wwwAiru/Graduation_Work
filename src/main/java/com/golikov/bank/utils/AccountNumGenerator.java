package com.golikov.bank.utils;

import org.springframework.context.annotation.Bean;

// утилитарный класс для генерации номера счёта
public class AccountNumGenerator {
    private static final int SERIAL_TEMPLATE = 408777;
    private static Long randNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

    public static String generate(Long clientID){
        StringBuffer result = new StringBuffer();
        result.append(SERIAL_TEMPLATE);
        result.append(clientID);
        result.append(randNumber);
        result.setLength(16);
        return result.toString();
    }
}
