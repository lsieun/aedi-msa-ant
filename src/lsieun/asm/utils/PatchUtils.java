package lsieun.asm.utils;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.utils.RegexUtils;
import lsieun.utils.archive.JarUtils;
import lsieun.utils.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.List;

public class PatchUtils {
    public static void patch(String[] path_regex_array, RegexAdapter cv, ClassWriter cw) {
        String jar_path = PathUtils.getJarPath();
        List<String> list = JarUtils.getClassEntries(jar_path);

        RegexUtils.filter(list, path_regex_array);

        for(String str : list) {
            byte[] origin_bytes = JarUtils.readClass(jar_path, str);
            String fqcn = JarUtils.getFQCN(str);

            ClassReader cr = new ClassReader(origin_bytes);

            cr.accept(cv, ClassReader.EXPAND_FRAMES);

            if (cv.gotcha) {
                byte[] bytes = cw.toByteArray();

                String filepath = FileUtils.getFilePath(PatchUtils.class, fqcn);
                System.out.println("file://" + filepath);
                FileUtils.writeBytes(filepath, bytes);
            }

        }
    }

}
