package lsieun.asm.utils;

import java.io.File;

public class PathUtils {
    public static String getJarPath() {
        return getJarPath("idea.jar");
    }

    public static String getJarPath(String jarName) {
        String user_dir = System.getProperty("user.dir");
        String jar_path = user_dir + File.separator + String.format("lib/%s", jarName);
        return jar_path;
    }

}
