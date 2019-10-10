package lsieun.aedi.adapter;

import lsieun.asm.adapter.MethodReplaceAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class PingResponseAdaptor extends MethodReplaceAdapter {
    public PingResponseAdaptor(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        super(classVisitor, includes, excludes);
    }

    @Override
    protected void generateNewBody(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
        if (mv != null) {
            mv.visitTypeInsn(NEW, "com/jetbrains/ls/responses/PingResponse");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/jetbrains/ls/responses/PingResponse", "<init>", "()V", false);

            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, "com/jetbrains/ls/responses/ResponseCode", "OK", "Lcom/jetbrains/ls/responses/ResponseCode;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/PingResponse", "setResponseCode", "(Lcom/jetbrains/ls/responses/ResponseCode;)V", false);

            mv.visitInsn(ARETURN);
        }
    }
}
