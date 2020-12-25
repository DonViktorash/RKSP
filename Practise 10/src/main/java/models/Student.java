package models;

import java.util.Map;

public class Student {
    public static final String FULL_NAME    = "Аляутдинов Равиль Раисович";
    public static final String GROUP        = "ИКБО-01-18";
    public static final String TASK_VARIANT = "1";
    public static final String TASK_TEXT    = "Найти максимальное значение среди множества чисел (без использования класса Math).";

    public static void addToMap(Map<String, Object> map) {
        map.put("fullName",    FULL_NAME);
        map.put("group",       GROUP);
        map.put("taskVariant", TASK_VARIANT);
        map.put("taskText",    TASK_TEXT);
    }
}
