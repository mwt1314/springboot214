package cn.matio.api.test;

import java.io.*;

/**
 * @author mawt
 * @description
 * @date 2020/4/20
 */
public class XXXX {

    public static void main(String[] args) throws IOException {
        System.out.println(System.currentTimeMillis() / 1000);

        File file = new File("C:\\Users\\mawt\\Desktop\\im");
        ll(file);
    }

    private static void ll(File p) throws IOException {
        if (p != null) {
            File[] files = p.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                //    if (file.getName().endsWith(".class")) continue;
                //    if (file.getAbsolutePath().contains("mianshi-parent")) continue;
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int lineIndex = 0;
                    while ((line = br.readLine()) != null) {
                        lineIndex++;
                        /*if (line != null && line.contains("imapi.server.com")) {
                            System.out.println(lineIndex + "" + file.getAbsolutePath());
                        }*/

                        /*if (line != null && line.contains("shiku-tigase")) {
                            System.out.println(lineIndex + "" + file.getAbsolutePath());
                        }*/
                        if (line != null && line.contains("userStatusMessage")) {
                            System.out.println(lineIndex + "\t" + file.getAbsolutePath());
                            System.out.println(line + "\n");
                        }
                        /*if (line != null && line.contains("sendMessage")) {
                            System.out.println(lineIndex + "\t" + file.getAbsolutePath());
                            System.out.println(line + "\n");
                        }*/

                        /*if (line != null && line.contains("jquery.nanoscroller.js.map")) {
                            System.out.println(lineIndex + "" + file.getAbsolutePath());
                        }*/
                    }
                    br.close();
                } else {
                    ll(file);
                }
            }
        }
    }

}
