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
        if(!compiler.getTask(null, fileManager,null,null,null, fileObjects).call()) {
            throw new Exception("Kompilasi gagal. Pastikan versi Java anda memenuhi standar minimum penggunaan SundaLang.");
        }

        URL[] urls = new URL[]{
                new File("").toURI().toURL()
        };

        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Object sundaLang = urlClassLoader.loadClass("SundaLang").newInstance();
        sundaLang.getClass().getMethod("main").invoke(sundaLang);
    }

    public static void read(String location) throws Exception {
        String code = null;
        File file = new File(location);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext())
            code = scanner.nextLine();

        scanner.close();

        if (code == null)
            throw new Exception("Pembaca file SundaLang tidak menemukan file yang anda maksud.");

        dictionary(code);
    }

    public static void dictionary(String code) throws Exception {
        Map<String, String> dictionary = new LinkedHashMap<>();
        dictionary.put("int", "nomer");
        dictionary.put("long", "nomerBadag");
        dictionary.put("lamun", "if");
        dictionary.put("cetakEuy", "System.out.println");

        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            code = code.replace(entry.getKey(), entry.getValue());
        }

        run(code);
    }

    public static void main(String[] args) {
        try {
            read("./test.sunda");
        } catch (Exception ignored) {}
    }
}
