package cn.matio.api.test;

/**
 * @author mawt
 * @description
 * @date 2020/1/2
 */
public class EndWith {

    public static void main(String[] args) {
        String link = "http://192.168.1.145:8088/upload/images/20200102/e53c868ee9e8e7b28c424b56afe2066d.jpg?width=680&height=453";
        boolean wh = link.matches(".*(\\?width=[0-9]{1,}&height=[0-9]{1,})$");
        System.out.println(wh);

        System.out.println(link.substring(0, link.lastIndexOf("?width=")));
        System.out.println(link.substring(link.lastIndexOf("?width=") + 7, link.lastIndexOf("&height=")));
        System.out.println(link.substring(link.lastIndexOf("&height=") + 8));
    }

}
