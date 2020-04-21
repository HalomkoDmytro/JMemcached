package net.devstudy;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public class TestUtils {

    public static void setLoggerMockViaReflection(Class<?> clazz, Logger logger) throws IllegalAccessException {
        Field loggerFiled = FieldUtils.getField(clazz, "LOGGER", true);
        FieldUtils.removeFinalModifier(loggerFiled);
        loggerFiled.set(clazz, logger);
    }
}
