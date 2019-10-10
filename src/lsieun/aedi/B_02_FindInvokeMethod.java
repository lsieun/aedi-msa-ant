package lsieun.aedi;

import lsieun.aedi.template.BaseClass;
import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.adapter.find.opcode.invoke.FindInvokeMethod;
import lsieun.asm.utils.NameUtils;
import lsieun.utils.RegexUtils;

import java.util.List;

public class B_02_FindInvokeMethod extends BaseClass {
    private static final String[] path_regex_array = new String[] {
            String.format("^%s.class$", NameUtils.getInternalName("com.intellij.ide.a.m.a7"))
    };
    private static final String[] includes = new String[] {
            "^b:\\(ISLjava/lang/String;I\\)Lcom/intellij/ide/a/m/aI;$",
            "^\\w+:$",
    };
    private static final String[] excludes = null;

    public static void main(String[] args) {
        B_02_FindInvokeMethod instance = new B_02_FindInvokeMethod();
        List<Result> list = instance.findInvoke(path_regex_array);

        String[] class_remove_regex = new String[] {
                "^java/.+$"
        };

        String[] method_name_remove_regex = new String[] {
                "^<init>$"
        };

        String[] method_desc_remove_regex = new String[] {
                "^.+\\)Ljava/.+$",
                "^.+\\)\\[Ljava/.+$",
        };

        int size = list.size();
        for(int i=size-1;i>=0;i--) {
            Result result = list.get(i);
            if (RegexUtils.matches(result.className, class_remove_regex)) {
                list.remove(i);
                continue;
            }
            if (RegexUtils.matches(result.list.get(0).name, method_name_remove_regex)) {
                list.remove(i);
                continue;
            }
            if (RegexUtils.matches(result.list.get(0).desc, method_desc_remove_regex)) {
                list.remove(i);
                continue;
            }
        }
        displayResult(list);
    }

    @Override
    public RegexAdapter getClassVisitor() {
        FindInvokeMethod cv = new FindInvokeMethod(null, includes, excludes);
        return cv;
    }
}
