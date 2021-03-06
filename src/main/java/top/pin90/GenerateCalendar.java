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
import java.util.List;

public class GenerateCalendar {
    private int year;
    private int restMode;
    private int version;
    private String desc;
    private HolidayConfig holidayConfig;
    private JSONObject node;

    public GenerateCalendar(int year, int restMode, int version, String desc, HolidayConfig holidayConfig) {
        this.year = year;
        this.restMode = restMode;
        this.holidayConfig = holidayConfig;
        this.version = version;
        this.desc = desc;
    }

    public static void main(String[] args) throws IOException {
        int year = 2022;
        int restMode = 2;
        int version = 2;
        String desc = "2022年日历数据";

        HolidayConfig holidayConfig = new HolidayConfig();
        GenerateCalendar generateCalendar = new GenerateCalendar(year, restMode, version, desc, holidayConfig);

        String json = generateCalendar.generate();
        System.out.println(json);

        String fileNameFormat = "%s-%s-rest.json";
        String versionFileNameFormat = "%s-%s-version";
        Files.write(Paths.get("data/" + String.format(fileNameFormat, year, restMode)), json.getBytes(StandardCharsets.UTF_8));
        Files.write(Paths.get("data/" + String.format(versionFileNameFormat, year, restMode)), (version + "").getBytes(StandardCharsets.UTF_8));

    }

    public String generate() {
        return generateJSONObject().toString(SerializerFeature.PrettyFormat);
    }

    public JSONObject generateJSONObject() {

        initNode();

        commonProperty();

        normalDay();

        handBusyAndRestDay();

        return node;
    }

    public void initNode() {
        JSONObject node = new JSONObject();

        JSONObject index = new JSONObject();
        JSONArray dates = new JSONArray(366);
        JSONObject property = new JSONObject();
        node.put("index", index);
        node.put("dates", dates);
        node.put("property", property);

        this.node = node;
    }

    public void commonProperty() {
        JSONObject property = node.getJSONObject("property");
        property.put("version", version);
        property.put("year", year);
        property.put("desc", desc);
    }

    public void normalDay() {
        JSONObject index = node.getJSONObject("index");
        JSONArray dates = node.getJSONArray("dates");


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

    public void handBusyAndRestDay() {
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

    public String indexKey(int month, int day) {
        return String.format("%02d", month) + String.format("%02d", day);
    }

    public HolidayConfig getHolidayConfig() {
        return holidayConfig;
    }

    public void setHolidayConfig(HolidayConfig holidayConfig) {
        this.holidayConfig = holidayConfig;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
