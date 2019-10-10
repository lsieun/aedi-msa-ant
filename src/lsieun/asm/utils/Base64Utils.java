package lsieun.asm.utils;

public class Base64Utils {
    public static String decode(String str) {
        byte[] decoded_bytes = java.util.Base64.getDecoder().decode(str);
        String decoded_str = new String(decoded_bytes);
        return decoded_str;
    }

    public static String encode(String str) {
        byte[] origin_bytes = str.getBytes();
        String encoded_str = java.util.Base64.getEncoder().encodeToString(origin_bytes);
        return encoded_str;
    }

    public static void main(String[] args) {
        String str = "";
        String value = decode(str);
        System.out.println(value);
    }
}
