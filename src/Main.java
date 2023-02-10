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
    private static final ExceptionHandler exceptionHandler = new ExceptionHandler();

    public static void run(String code) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File file = new File("SundaLangOutput.java");

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println("public class SundaLangOutput { public static void main(String[] args) { " + code + " } }");
        printWriter.close();

        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file);
        if(!compiler.getTask(null, fileManager, null, null, null, fileObjects).call())
            throw new Exception("Aya kasalahan nalika kompilasi, urang teu ngarti mun ieu mah. Kodena geus di cek deui mang?");

        URL[] urls = new URL[]{
                new File("").toURI().toURL()
        };

        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Object sundaLang = urlClassLoader.loadClass("SundaLangOutput").getDeclaredConstructor().newInstance();
        sundaLang.getClass().getMethod("main").invoke(sundaLang);
    }

    public static void read(String location) throws Exception {
        if (!location.endsWith(".westjava"))
            throw new Exception("SundaFile na teu valid, nu bener atuh kang.");

        StringBuilder code = new StringBuilder();
        File file = new File(location);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            code.append(scanner.nextLine());
        }

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
        dictionary.put("jeungsorangan", "private");
        dictionary.put("jeungurang", "protected");
        dictionary.put("jeungkabehan", "public");

        // loops
        dictionary.put("pikeun", "for");

        // others
        dictionary.put("nyoba", "try");
        dictionary.put("tangkep", "catch");
        dictionary.put("tungtungna", "finally");
        dictionary.put("angger", "final");
        dictionary.put("baledog", "throw");
        dictionary.put("baledogkeun", "throws");

        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            code = code.replaceAll("(?i)\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        code = code.trim().replaceAll(" +", " ");
        code = code.replaceAll("\\R", " ");
        code = code.replace(";", ";\n");

        run(code);
    }

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Lebetkeun alamat file SundaLang-na (*.westjava):");
        String path = scanner.nextLine();

        read(path);
    }
}
