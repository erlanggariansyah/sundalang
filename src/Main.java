import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void run(String code) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File file = new File("SundaLang.java");

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println("public class SundaLang { public static void main() { " + code + " } }");
        printWriter.close();

        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file);
        if(!compiler.getTask(null, fileManager,null,null,null, fileObjects).call())
            throw new Exception("Kompilasi gagal. Pastikan versi Java anda memenuhi standar minimum penggunaan SundaLang.");

        URL[] urls = new URL[]{
                new File("").toURI().toURL()
        };

        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Object sundaLang = urlClassLoader.loadClass("SundaLang").newInstance();
        sundaLang.getClass().getMethod("main").invoke(sundaLang);
    }

    public static void read(String location) throws Exception {
        StringBuilder code = new StringBuilder();
        File file = new File(location);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext())
            code.append(scanner.nextLine());

        scanner.close();

        dictionary(code.toString());
    }

    public static void dictionary(String code) throws Exception {
        Map<String, String> dictionary = new LinkedHashMap<>();

        // data types
        dictionary.put("NomerPangleutikna", "byte");
        dictionary.put("NomerLeutik", "short");
        dictionary.put("Nomer", "int");
        dictionary.put("NomerBadag", "long");
        dictionary.put("NomerFraksi", "float");
        dictionary.put("NomerFraksiBadag", "double");
        dictionary.put("Surat", "char");
        dictionary.put("Kecap", "String");

        // logical operator
        dictionary.put("lamun", "if");
        dictionary.put("iraha", "while");
        dictionary.put("lamunhenteu", "else");
        dictionary.put("lamundeui", "else if");

        // function
        dictionary.put("cetakEuy", "System.out.println");

        // access modifiers
        dictionary.put("swasta", "private");
        dictionary.put("ditangtayungan", "protected");
        dictionary.put("umum", "public");

        // others
        dictionary.put("coba", "try");
        dictionary.put("nangkep", "catch");

        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            code = code.replaceAll("(?i)\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        code = code.trim().replaceAll(" +", " ");
        code = code.replaceAll("\\R", " ");
        code = code.replace(";", ";\n");

        run(code);
    }

    public static void main(String[] args) {
        try {
            read("./test.sunda");
        } catch (Exception ignored) {}
    }
}
