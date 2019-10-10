package lsieun.aedi;

import lsieun.aedi.template.BaseClass;
import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.adapter.find.clazz.FindMethodAdaptor;
import lsieun.asm.utils.NameUtils;

import java.util.List;

public class A_02_FindLicense extends BaseClass {
    private static final String[] path_regex_array = null;
    private static final String[] includes = new String[]{
            String.format("^\\w+:\\(.*\\)L%s;$", NameUtils.getInternalName("com.jetbrains.ls.responses.License")),
            "^\\w+:$",
    };
    private static final String[] excludes = new String[]{"^error:.+$"};

    public static void main(String[] args) {
        A_02_FindLicense instance = new A_02_FindLicense();
        List<Result> resultList = instance.find(path_regex_array);
        displayResult(resultList);
    }

    @Override
    public RegexAdapter getClassVisitor() {
        FindMethodAdaptor cv = new FindMethodAdaptor(null, includes, excludes);
        return cv;
    }
}
