package top.pin90;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class GenerateCalendar {
    private int year;
    private int restMode;
    private HolidayConfig holidayConfig;

    public GenerateCalendar(int year, int restMode, HolidayConfig holidayConfig) {
        this.year = year;
        this.restMode = restMode;
        this.holidayConfig = holidayConfig;
    }

    public static void main(String[] args) throws IOException {
        int year = 2022;
        HolidayConfig holidayConfig = new HolidayConfig();
        GenerateCalendar generateCalendar = new GenerateCalendar(year, 2, holidayConfig);

        String json = generateCalendar.generate();
        System.out.println(json);
        Files.write(Paths.get("data/" + year + "-two-rest.json"), json.getBytes(StandardCharsets.UTF_8));
    }

    public String generate() {
        return generateJSONObject().toString(SerializerFeature.PrettyFormat);
    }

    public JSONObject generateJSONObject() {
        JSONObject node = new JSONObject();

        initNode(node);

        normalDay(year, node);

        handBusyAndRestDay(year, node);

        return node;
    }

    public void initNode(JSONObject node) {
        JSONObject index = new JSONObject();
        JSONArray dates = new JSONArray(366);
        JSONObject property = new JSONObject();
        node.put("index", index);
        node.put("dates", dates);
        node.put("property", property);
    }

    public void normalDay(int year, JSONObject node) {
        JSONObject property = node.getJSONObject("property");
        JSONObject index = node.getJSONObject("index");
        JSONArray dates = node.getJSONArray("dates");

        property.put("year", year);

        int i = 0;
        for (int month = 1; month <= 12; month++) {
            LocalDate md = LocalDate.of(year, month, 1);
            LocalDate lastDayOfMonth = md.with(TemporalAdjusters.lastDayOfMonth());
            int days = lastDayOfMonth.getDayOfMonth();
            for (int day = 1; day <= days; day++) {
                LocalDate dayDate = LocalDate.of(year, month, day);
                index.put(indexKey(month, day), i);
                JSONObject date = new JSONObject();
                date.put("index", i++);
                date.put("month", month);
                date.put("day", day);
                int dayOfWeek = dayDate.getDayOfWeek().getValue();
                date.put("dayOfWeek", dayOfWeek);

                int rest = 0;
                if (dayOfWeek == 6 || dayOfWeek == 7) {
                    rest = 1;
                }

                date.put("rest", rest);
                dates.add(date);
            }
        }
    }

    public void handBusyAndRestDay(int year, JSONObject node) {
        JSONObject index = node.getJSONObject("index");
        JSONArray dates = node.getJSONArray("dates");

        List<DayRange> busyDay = holidayConfig.getBusyDay();
        for (DayRange dayRange : busyDay) {
            setRestRangeValue(index, dates, dayRange, 0);
        }

        List<DayRange> restDay = holidayConfig.getRestDay();
        for (DayRange dayRange : restDay) {
            setRestRangeValue(index, dates, dayRange, 1);
        }
    }

    public void setRestRangeValue(JSONObject index, JSONArray dates, DayRange dayRange, Object value) {
        LocalDate start = dayRange.startDate(year);
        LocalDate end = dayRange.endDate(year);

        while (start.compareTo(end) <= 0) {
            String indexKey = indexKey(start.getMonthValue(), start.getDayOfMonth());
            Integer i = index.getInteger(indexKey);
            JSONObject date = dates.getJSONObject(i);
            date.put("rest", value);
            date.put("restDesc", dayRange.getDesc());
            start = start.plusDays(1);
        }
    }

    public LocalDate parseDate(int year, String date) {
        String month = date.substring(0, 2);
        String day = date.substring(2);
        return LocalDate.of(year, Integer.parseInt(month), Integer.parseInt(day));
    }

    public String indexKey(int month, int day) {
        return String.format("%02d", month) + String.format("%02d", day);
    }

    public HolidayConfig getHolidayConfig() {
        return holidayConfig;
    }

    public void setHolidayConfig(HolidayConfig holidayConfig) {
        this.holidayConfig = holidayConfig;
    }
}
