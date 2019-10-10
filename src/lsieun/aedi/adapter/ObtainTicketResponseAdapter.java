package lsieun.aedi.adapter;

import lsieun.asm.adapter.MethodReplaceAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ObtainTicketResponseAdapter extends MethodReplaceAdapter {
    public ObtainTicketResponseAdapter(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        super(classVisitor, includes, excludes);
    }

    @Override
    protected void generateNewBody(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
        if (mv != null) {
            mv.visitTypeInsn(NEW, "com/jetbrains/ls/responses/ObtainTicketResponse");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "<init>", "()V", false);


            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, "com/jetbrains/ls/responses/ResponseCode", "OK", "Lcom/jetbrains/ls/responses/ResponseCode;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setResponseCode", "(Lcom/jetbrains/ls/responses/ResponseCode;)V", false);

            mv.visitInsn(DUP);
            mv.visitLdcInsn("1");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setTicketId", "(Ljava/lang/String;)V", false);

            mv.visitInsn(DUP);
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("licensee=");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn("user.name");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn("\u0009licenseType=0");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setTicketProperties", "(Ljava/lang/String;)V", false);

            mv.visitInsn(DUP);
            mv.visitLdcInsn("message");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setMessage", "(Ljava/lang/String;)V", false);

            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setSalt", "(J)V", false);

            mv.visitInsn(DUP);
            mv.visitLdcInsn("signature");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setSignature", "(Ljava/lang/String;)V", false);

            mv.visitInsn(DUP);
            mv.visitLdcInsn(new Long(1471228928L));
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/ls/responses/ObtainTicketResponse", "setProlongationPeriod", "(J)V", false);

            mv.visitInsn(ARETURN);
        }
    }
}
