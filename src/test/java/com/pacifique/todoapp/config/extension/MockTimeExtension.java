package com.pacifique.todoapp.config.extension;

import com.pacifique.todoapp.config.utils.time.Time;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MockTimeExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        Time.useSystemDefaultZoneClock();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.of(2023, Month.AUGUST, 8, 3, 56);
        ZoneId zoneId = ZoneId.of("Asia/Manila");
        Time.useMockTime(currentDateTime, zoneId);
    }
}
