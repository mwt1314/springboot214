package queen;

import java.util.ArrayList;
import java.util.List;

public class QueenProblem {

    private static final int QUEEN_NUMBER = 8;

    public static void main(String[] args) {
        CorrectLocation correctLocation = new CorrectLocation();
//        getCorrect(1, QUEEN_NUMBER, new ArrayList<>(QUEEN_NUMBER), correctLocation);
//        int i = 1;
//        for (List<Location> list : correctLocation.locations) {
//            System.out.println("第" + i++ + "种解法:");
//            for (Location location : list) {
//                System.out.println("x: " + location.x + ", y: " + location.y);
//            }
//        }

        getCorrect(0, QUEEN_NUMBER, correctLocation, new int[QUEEN_NUMBER][QUEEN_NUMBER]);

        int i = 1;
        for (List<Location> list : correctLocation.locations) {
            System.out.println("第" + i++ + "种解法:");
            for (Location location : list) {
                System.out.println("x: " + location.x + ", y: " + location.y);
            }
        }

    }

    /**
     * 递归求解可用位置
     * 比较浪费空间,每次有解都要生成一个位置数组
     *
     * @param level           y坐标的位置
     * @param col             x方向的元素数量
     * @param prev            记录前面的位置信息
     * @param correctLocation 正确答案
     */
    static void getCorrect(int level, int col, List<Location> prev, CorrectLocation correctLocation) {
        if (level == col + 1) {
            correctLocation.addCorrectLocation(prev);
            return;
        }
        for (int i = 1; i <= col; i++) {
            if (isCorrect(prev, i, level)) {
                //此处可以使用克隆替换
                List<Location> newList = new ArrayList<>(QUEEN_NUMBER);
                newList.addAll(prev);

                newList.add(new Location(i, level));
                getCorrect(level + 1, col, newList, correctLocation);
            }
        }
    }

    static boolean isCorrect(List<Location> prev, int i, int level) {
        for (Location location : prev) {
            //限制条件
            if (i == location.x - (level - location.y) || i == location.x + (level - location.y) || i == location.x) {
                return false;
            }
        }
        return true;
    }

    /**
     * 回溯求解
     * 使用状态量解决递归求解占用空间巨大的问题
     */
    static void getCorrect(int level, int col, CorrectLocation correctLocation, int[][] states) {
        if (level == col) {
            List<Location> locations = new ArrayList<>();
            for (int j = 0; j < col; j++) {
                for (int i = 0; i < col; i++) {
                    if (states[j][i] == 1) {
                        locations.add(new Location(i, j));
                        break;
                    }
                }
            }
            correctLocation.addCorrectLocation(locations);
            return;
        }

        for (int i = 0; i < col; i++) {
            if (isCorrect(i, level, states)) {
                states[level][i] = 1;
                getCorrect(level + 1, col, correctLocation, states);
                //回溯
                states[level][i] = 0;
            }
        }
    }

    static boolean isCorrect(int i, int j, int[][] states) {
        int leftLocation;
        int rightLocation;
        for (int y = j - 1; y >= 0; y--) {
            //垂直方向重复
            if (states[y][i] == 1) {
                return false;
            }
            //45度 j-y是偏移量
            leftLocation = i - (j - y);
            if (leftLocation >= 0 && states[y][leftLocation] == 1) {
                return false;
            }
            rightLocation = i + (j - y);
            if (rightLocation < states[0].length && states[y][rightLocation] == 1) {
                return false;
            }
        }

        return true;
    }

    private static class Location {
        private int x;
        private int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class CorrectLocation {
        private List<List<Location>> locations;

        CorrectLocation() {
            locations = new ArrayList<>();
        }

        void addCorrectLocation(List<Location> locations) {
            this.locations.add(locations);
        }
    }


}
