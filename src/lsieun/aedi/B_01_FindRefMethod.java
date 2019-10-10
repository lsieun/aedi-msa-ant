package lsieun.aedi;

import lsieun.aedi.template.BaseClass;
import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.adapter.find.opcode.ref.FindRefMethod;
import lsieun.asm.utils.NameUtils;

import java.util.List;

public class B_01_FindRefMethod extends BaseClass {
    private static final String[] path_regex_array = new String[]{"^com/.+$"};
    private static final String targetInternalName = NameUtils.getInternalName("com.jetbrains.a.a.R");
    private static final String[] includes = new String[]{
            "^a:\\(ILjava/lang/String;CLjava/lang/String;IIZS\\)Lcom/jetbrains/ls/responses/ObtainTicketResponse;$",
            "^\\w+:$",
    };
    private static final String[] excludes = null;

    public static void main(String[] args) {
        B_01_FindRefMethod instance = new B_01_FindRefMethod();
        List<Result> list = instance.findOpcode(path_regex_array);
        displayResult(list);
    }

    @Override
    public RegexAdapter getClassVisitor() {
        FindRefMethod cv = new FindRefMethod(null, targetInternalName, includes, excludes);
        return cv;
    }
}
