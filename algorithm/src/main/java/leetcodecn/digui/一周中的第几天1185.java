package leetcodecn.digui;

public class 一周中的第几天1185 {

    public static void main(String[] args) {
        一周中的第几天1185 x = new 一周中的第几天1185();
        System.out.println(x.dayOfTheWeek(6, 3, 1969));
    }

    private String[] weeks = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    //基姆拉尔森计算公式外文名是Kim larsen calculation formula
    private String dayOfTheWeek2(int day, int month, int year) {
        if (month == 1 || month == 2) {
            month += 12;
            year--;
        }
        int iWeek = (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;                              //基姆拉尔森计算公式
        String[] result = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        return result[iWeek];
    }

    //常规算法
    private String dayOfTheWeek(int day, int month, int year) {
        //距离1970.1.1有多少天
        int days = getSubDay(year, month, day);
        //1970.1.1是星期四
        int weekIndex = year >= 1970 ? ((4 + days) % 7) : (Math.abs(4 - days)) % 7;
        return weeks[weekIndex];
    }

    //判断是否是闰年，闰年有366天，平年有365天
    //置闰法则 四年一闰；百年不闰，四百年再闰
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    //计算距离1970.1.1有多少天
    private int getSubDay(int year, int month, int day) {
        if (year <= 0) throw new IllegalArgumentException();
        if (day > getMonthDay(year, month) || day < 1) throw new IllegalArgumentException();
        //1970.1.1是星期四
        int yearDay = 0, fromYear = 1970;
        if (year >= fromYear) {
            for (int i = fromYear; i < year; i++) {
                yearDay += isLeapYear(i) ? 366 : 365;
            }
            for (int i = 1; i < month; i++) {
                yearDay += getMonthDay(year, i);
            }
            yearDay += day - 1;
        } else {
            for (int i = year; i < fromYear; i++) {
                yearDay += isLeapYear(i) ? 366 : 365;
            }
            for (int i = 1; i < month; i++) {
                yearDay -= getMonthDay(year, i);
            }
            yearDay -= day;
        }
        return yearDay;
    }

    private int getMonthDay(int year, int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException();
        if (month == 2) return isLeapYear(year) ? 29 : 28;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
            return 31;
        return 30;
    }

}
