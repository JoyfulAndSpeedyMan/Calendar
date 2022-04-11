package top.pin90;

import java.time.LocalDate;

/**
 * 日期范围
 */
public class DayRange {
    private int startMonth;
    private int startDay;

    private int endMonth;
    private int endDay;

    private String desc;
    /**
     * MMdd格式解析日期，例如4-11日，格式为：0411
     * @return
     */
    public static DayRange of(String start, String end){
        int startMonth = Integer.parseInt(start.substring(0, 2));
        int startDay = Integer.parseInt(start.substring(2));

        int endMonth = Integer.parseInt(end.substring(0, 2));
        int endDay = Integer.parseInt(end.substring(2));

        return new DayRange(startMonth, startDay, endMonth, endDay);
    }

    public static DayRange of(String start, String end, String desc){
        int startMonth = Integer.parseInt(start.substring(0, 2));
        int startDay = Integer.parseInt(start.substring(2));

        int endMonth = Integer.parseInt(end.substring(0, 2));
        int endDay = Integer.parseInt(end.substring(2));
        DayRange dayRange = new DayRange(startMonth, startDay, endMonth, endDay);
        dayRange.desc = desc;
        return dayRange;
    }


    public DayRange(int startMonth, int startDay, int endMonth, int endDay) {
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endMonth = endMonth;
        this.endDay = endDay;
    }


    public LocalDate startDate(int year){
        return LocalDate.of(year, startMonth, startDay);
    }

    public LocalDate endDate(int year){
        return LocalDate.of(year, endMonth, endDay);
    }


    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public String getDesc() {
        return desc;
    }
}
