package top.pin90;

import java.util.ArrayList;
import java.util.List;

/**
 * 2022假期配置（数据来自国务院www.gov.cn）
 * <p>
 * 一、元旦：2022年1月1日至3日放假，共3天。
 * <p>
 * 二、春节：1月31日至2月6日放假调休，共7天。1月29日（星期六）、1月30日（星期日）上班。
 * <p>
 * 三、清明节：4月3日至5日放假调休，共3天。4月2日（星期六）上班。
 * <p>
 * 四、劳动节：4月30日至5月4日放假调休，共5天。4月24日（星期日）、5月7日（星期六）上班。
 * <p>
 * 五、端午节：6月3日至5日放假，共3天。
 * <p>
 * 六、中秋节：9月10日至12日放假，共3天。
 * <p>
 * 七、国庆节：10月1日至7日放假调休，共7天。10月8日（星期六）、10月9日（星期日）上班。
 */
public class HolidayConfig {
    public List<DayRange> getBusyDay() {
        List<DayRange> busyDay = new ArrayList<>();
        busyDay.add(DayRange.of("0129", "0130", "春节调休"));
        busyDay.add(DayRange.of("0402", "0402", "清明节调休"));
        busyDay.add(DayRange.of("0424", "0424", "劳动节调休"));
        busyDay.add(DayRange.of("0507", "0507", "劳动节调休"));
        busyDay.add(DayRange.of("1008", "1009", "国庆节调休"));

        return busyDay;
    }

    public List<DayRange> getRestDay() {
        List<DayRange> restDay = new ArrayList<>();
        restDay.add(DayRange.of("0101", "0103", "元旦"));
        restDay.add(DayRange.of("0131", "0206", "春节"));
        restDay.add(DayRange.of("0403", "0405", "清明节"));
        restDay.add(DayRange.of("0430", "0504", "劳动节"));
        restDay.add(DayRange.of("0603", "0605", "端午节"));
        restDay.add(DayRange.of("0910", "0912", "中秋节"));
        restDay.add(DayRange.of("1001", "1007", "国庆节"));

        return restDay;
    }
}
