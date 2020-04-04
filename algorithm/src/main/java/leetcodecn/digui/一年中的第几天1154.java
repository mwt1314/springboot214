package leetcodecn.digui;

public class 一年中的第几天1154 {

    public static void main(String[] args) {

    }

    public int dayOfYear(String date) {
        String[] split = date.split("-");
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        if (month == 1) return day;
        if (month == 2) return 31 + day;
        int year = Integer.parseInt(split[0]);
        int days = 0;
        for (int i = 1; i < month; i++) {
            days += getMonthDay(year, i);
        }
        return days + day;
    }

    //判断是否是闰年，闰年有366天，平年有365天
    //置闰法则 四年一闰；百年不闰，四百年再闰
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    private int getMonthDay(int year, int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException();
        if (month == 2) return isLeapYear(year) ? 29 : 28;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
            return 31;
        return 30;
    }

}
