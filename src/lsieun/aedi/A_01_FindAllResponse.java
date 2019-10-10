package lsieun.aedi;

import lsieun.aedi.template.BaseClass;
import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.adapter.find.clazz.FindMethodAdaptor;

import java.util.List;

public class A_01_FindAllResponse extends BaseClass {
    private static final String[] path_regex_array = null;
    private static final String[] includes = new String[] {
            "^.+:\\(.+\\)Lcom/jetbrains/ls/responses/\\w+Response;$",
            "^\\w+:$",
    };
    private static final String[] excludes = new String[] {"^error:.+$"};

    public static void main(String[] args) {
        A_01_FindAllResponse instance = new A_01_FindAllResponse();
        List<Result> resultList = instance.find(path_regex_array);
        displayResult(resultList);
    }

    @Override
    public RegexAdapter getClassVisitor() {
        FindMethodAdaptor cv = new FindMethodAdaptor(null, includes, excludes);
        return cv;
    }
}
