package lsieun.aedi.template;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.utils.PathUtils;
import lsieun.utils.RegexUtils;
import lsieun.utils.archive.JarUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseClass {

    public List<Result> find(String[] path_regex_array) {
        String default_jar_path = PathUtils.getJarPath();
        return find(default_jar_path, path_regex_array);
    }

    public List<Result> find(String jar_path, String[] path_regex_array) {
        int parsingOptions = ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        return find(jar_path, path_regex_array, parsingOptions, 1);
    }

    public List<Result> findOpcode(String[] path_regex_array) {
        String default_jar_path = PathUtils.getJarPath();
        return findOpcode(default_jar_path, path_regex_array);
    }

    public List<Result> findOpcode(String jar_path, String[] path_regex_array) {
        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        return find(jar_path, path_regex_array, parsingOptions, 1);
    }

    public List<Result> findInvoke(String[] path_regex_array) {
        String default_jar_path = PathUtils.getJarPath();
        return findInvoke(default_jar_path, path_regex_array);
    }

    public List<Result> findInvoke(String jar_path, String[] path_regex_array) {
        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        return find(jar_path, path_regex_array, parsingOptions, 0);
    }

    public List<Result> find(String jar_path, String[] path_regex_array, int parsingOptions, int option) {

        List<String> list = JarUtils.getClassEntries(jar_path);

        RegexUtils.filter(list, path_regex_array);

        List<Result> resultList = new ArrayList<>();

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String item = it.next();
            byte[] bytes = JarUtils.readClass(jar_path, item);
            ClassReader cr = new ClassReader(bytes);
            ClassVisitor cv = getClassVisitor();
            cr.accept(cv, parsingOptions);
            if (cv instanceof RegexAdapter) {
                RegexAdapter ra = (RegexAdapter) cv;
                if (ra.gotcha) {
                    if (option == 1) {
                        resultList.add(ra.result);
                    }
                    else {
                        resultList.addAll(ra.resultList);
                    }
                }
            }
        }

        return resultList;
    }

    public abstract RegexAdapter getClassVisitor();

    public static void displayResult(List<Result> resultList) {
        if (resultList == null || resultList.size() < 1) return;

        for (int i = 0; i < resultList.size(); i++) {
            Result result = resultList.get(i);
            System.out.println(String.format("(%s)ClassName: %s", (i + 1), result.className));
            for (Result.NameAndDesc item : result.list) {
                System.out.println(String.format("%s: %s", item.name, item.desc));
            }
            System.out.println();
        }
    }
}
