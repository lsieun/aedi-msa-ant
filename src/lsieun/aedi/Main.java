package lsieun.aedi;

import lsieun.aedi.adapter.ObtainTicketResponseAdapter;
import lsieun.aedi.adapter.PingResponseAdaptor;
import lsieun.asm.adapter.MethodReplaceAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.adapter.find.clazz.FindMethodAdaptor;
import lsieun.asm.adapter.find.opcode.invoke.FindInvokeMethod;
import lsieun.asm.adapter.find.opcode.ref.FindRefMethod;
import lsieun.asm.utils.NameUtils;
import lsieun.asm.utils.PathUtils;
import lsieun.utils.RegexUtils;
import lsieun.utils.archive.JarUtils;
import lsieun.utils.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    private static String default_jar_path;

    public static Result findReturnObtainTicketResponse() {
        String[] path_regex_array = null;
        String[] includes = new String[]{
                "^.+:\\(.+\\)Lcom/jetbrains/ls/responses/ObtainTicketResponse;$",
                "^\\w+:$",
        };
        String[] excludes = new String[]{"^error:.+$"};

        List<String> list = JarUtils.getClassEntries(default_jar_path);

        RegexUtils.filter(list, path_regex_array);

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String item = it.next();
            byte[] bytes = JarUtils.readClass(default_jar_path, item);
            ClassReader cr = new ClassReader(bytes);
            FindMethodAdaptor cv = new FindMethodAdaptor((ClassVisitor) null, includes, excludes);
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if (cv.gotcha) {
                return cv.result;
            }
        }
        return null;
    }

    public static Result findRefObtainTicketResponse(
            String target_class_name,
            String target_method_name,
            String target_method_desc) {
        String[] path_regex_array = new String[]{"^com/.+$"};
        String target_name_desc = regex_str(String.format("%s:%s", target_method_name, target_method_desc));

        String[] includes = new String[]{
                String.format("^%s$", target_name_desc),
                "^\\w+:$",
        };
        String[] excludes = null;

        List<String> list = JarUtils.getClassEntries(default_jar_path);

        RegexUtils.filter(list, path_regex_array);

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String item = it.next();
            byte[] bytes = JarUtils.readClass(default_jar_path, item);
            ClassReader cr = new ClassReader(bytes);
            FindRefMethod cv = new FindRefMethod(null, target_class_name, includes, excludes);
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if (cv.gotcha) {
                return cv.result;
            }
        }

        return null;
    }

    public static List<Result> findInvokeObtainTicketResponse(
            String target_class_name,
            String target_method_name,
            String target_method_desc) {
        String[] path_regex_array = new String[]{
                String.format("^%s.class$", target_class_name)
        };
        String target_name_desc = regex_str(String.format("%s:%s", target_method_name, target_method_desc));
        String[] includes = new String[]{
                String.format("^%s$", target_name_desc),
                "^\\w+:$",
        };
        String[] excludes = null;

        List<String> list = JarUtils.getClassEntries(default_jar_path);

        RegexUtils.filter(list, path_regex_array);

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String item = it.next();
            byte[] bytes = JarUtils.readClass(default_jar_path, item);
            ClassReader cr = new ClassReader(bytes);
            FindInvokeMethod cv = new FindInvokeMethod(null, includes, excludes);
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if (cv.gotcha) {
                return cv.resultList;
            }
        }
        return null;
    }

    public static String regex_str(String str) {
        if (str == null) return str;
        return str.replaceAll("\\(", "\\\\\\(")
                .replaceAll("\\)", "\\\\\\)")
                .replaceAll("\\[", "\\\\\\[");
    }

    public static List<Result> filter(List<Result> list) {
        String[] class_remove_regex = new String[]{
                "^java/.+$"
        };

        String[] method_name_remove_regex = new String[]{
                "^<init>$"
        };

        String[] method_desc_remove_regex = new String[]{
                "^.+\\)Ljava/.+$",
                "^.+\\)\\[Ljava/.+$",
        };

        List<Result> newList = new ArrayList<>();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            Result result = list.get(i);
            if (RegexUtils.matches(result.className, class_remove_regex)) {
                continue;
            }
            if (RegexUtils.matches(result.list.get(0).name, method_name_remove_regex)) {
                continue;
            }
            if (RegexUtils.matches(result.list.get(0).desc, method_desc_remove_regex)) {
                continue;
            }
            newList.add(result);
        }

        return newList;
    }

    public static Result getOneResult(List<Result> list,
                                      String target_class_name,
                                      String target_method_name,
                                      String target_method_desc) {
        boolean flag = false;
        Result r = null;
        for (int i = 0; i < list.size(); i++) {
            Result result = list.get(i);
            String className = result.className;
            String methodName = result.list.get(0).name;
            String methodDesc = result.list.get(0).desc;
            if (!flag &&
                    target_class_name.equals(className) &&
                    target_method_name.equals(methodName) &&
                    target_method_desc.equals(methodDesc)) {
                flag = true;
            }

            if (flag) {
                boolean matches = RegexUtils.matches(methodDesc, "\\(.+\\)V");
                if (matches) {
                    r = result;
                    break;
                }
            }
        }
        return r;
    }

    public static void patchResponse(String target_class_name) {
        byte[] origin_bytes = JarUtils.readClass(default_jar_path, String.format("%s.class", target_class_name));
        ClassReader cr = new ClassReader(origin_bytes);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        PingResponseAdaptor cv1 = new PingResponseAdaptor(cw, new String[]{
                "^.+:\\(.+\\)Lcom/jetbrains/ls/responses/PingResponse;$",
                "^\\w+:$",
        }, null);
        ObtainTicketResponseAdapter cv2 = new ObtainTicketResponseAdapter(cv1, new String[]{
                "^.+:\\(.+\\)Lcom/jetbrains/ls/responses/ObtainTicketResponse;$",
                "^\\w+:$",
        }, null);


        cr.accept(cv2, 0);

        byte[] bytes = cw.toByteArray();

        String filepath = FileUtils.getFilePath(Main.class, NameUtils.getFQCN(target_class_name));
        System.out.println("file://" + filepath);
        FileUtils.writeBytes(filepath, bytes);
    }

    public static void patchVerify(String target_class_name,
                                   String target_method_name,
                                   String target_method_desc) {
        byte[] origin_bytes = JarUtils.readClass(default_jar_path, String.format("%s.class", target_class_name));
        ClassReader cr = new ClassReader(origin_bytes);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        String target_name_desc = regex_str(String.format("%s:%s", target_method_name, target_method_desc));
        String[] includes = new String[]{
                String.format("^%s$", target_name_desc),
                "^\\w+:$",
        };
        String[] excludes = null;
        MethodReplaceAdapter cv = new MethodReplaceAdapter(cw, includes, excludes);


        cr.accept(cv, 0);

        byte[] bytes = cw.toByteArray();

        String filepath = FileUtils.getFilePath(Main.class, NameUtils.getFQCN(target_class_name));
        System.out.println("file://" + filepath);
        FileUtils.writeBytes(filepath, bytes);
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            default_jar_path = PathUtils.getJarPath();
        }
        else {
            System.out.println(String.format("args = %s", Arrays.toString(args)));
            default_jar_path  = PathUtils.getJarPath(args[0]);
        }

        // (1)
        Result first_result = findReturnObtainTicketResponse();
        if (first_result == null) {
            System.out.println("first_result is null");
            return;
        }

        String first_target_class_name = first_result.className;
        String first_target_method_name = first_result.list.get(0).name;
        String first_target_method_desc = first_result.list.get(0).desc;

        // (2)
        Result second_result = findRefObtainTicketResponse(first_target_class_name, first_target_method_name, first_target_method_desc);
        if (second_result == null) {
            System.out.println("second_result is null");
            return;
        }

        String second_target_class_name = second_result.className;
        String second_target_method_name = second_result.list.get(0).name;
        String second_target_method_desc = second_result.list.get(0).desc;

        // (3)
        List<Result> third_result_list = findInvokeObtainTicketResponse(second_target_class_name, second_target_method_name, second_target_method_desc);

        // (4)
        List<Result> fourth_result_list = filter(third_result_list);

        // (5)
        Result fifth_result = getOneResult(
                fourth_result_list,
                first_target_class_name,
                first_target_method_name,
                first_target_method_desc);
        if (fifth_result == null) {
            System.out.println("fifth_result is null");
            return;
        }
        String fifth_target_class_name = fifth_result.className;
        String fifth_target_method_name = fifth_result.list.get(0).name;
        String fifth_target_method_desc = fifth_result.list.get(0).desc;

        // (6)
        patchResponse(first_target_class_name);

        // (7)
        patchVerify(fifth_target_class_name, fifth_target_method_name, fifth_target_method_desc);

//        System.out.println(fifth_result.className);
//        System.out.println(fifth_result.list.get(0).name);
//        System.out.println(fifth_result.list.get(0).desc);

//        FindUtils.displayResult(fourth_result_list);
    }
}
