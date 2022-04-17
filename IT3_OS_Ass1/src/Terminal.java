import java.nio.file.*;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// Terminal Class
public class Terminal {
    //commands are implemented here
    //"ls -r", "ls","pwd","cd","rmdir","rm","echo","touch","cp -r","cp","cat","mkdir"
    Parser parser;
    public String cd() {
        return System.getProperty("user.home");
    }

    public void cd(String path)
    {
        File file = new File(path).getAbsoluteFile();
        //File file = new File(path).getAbsoluteFile();
        if (file.exists())
        {
            System.setProperty("user.dir", file.getAbsolutePath());
        }else {
            System.out.println("command not found or invalid parameter");
        }
    }

    public void rmdir(String x) {

        if (!x.contains("*")) {
            boolean delete = new File(x).delete();
            if (delete == false) {
                System.out.println("command not found or invalid parameter");
            }
        } else {

            File fil = new File(System.getProperty("user.dir"));
            for (File file : fil.listFiles()) {
                if (file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    public void Cat(String path1, String path2) throws IOException {
        File file1 = new File(path1);
        String txt = "";

        try {
            // file.ext must be written
            Scanner search = new Scanner(file1);
            while (search.hasNextLine()) {
                txt += search.nextLine().toString() + "\n";
            }
            search.close();
        } catch (Exception e) {
            System.out.println("command not found or invalid parameter" + e.getMessage());
        }

        if (path2 != null) {
            txt += "\n";
            File file2 = new File(path2);
            if (!file2.exists()) {
                System.err.println("command not found or invalid parameter");
            } else {
                try {
                    // file.ext must be written
                    Scanner search = new Scanner(file2);
                    while (search.hasNextLine()) {
                        txt += search.nextLine().toString() + "\n";
                    }
                    search.close();
                } catch (Exception e) {
                    System.out.println("command not found or invalid parameter");
                }
            }
        }
        System.out.println(txt);
    }

    public void rm(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("Deleted the file: " + file.getName());
        } else {
            System.out.println("command not found or invalid parameter");
        }
    }

    public void cp(String filePath, String copyName) throws IOException {
        File file1 = new File(filePath);
        File file2 = new File(copyName);
        FileInputStream in = new FileInputStream(file1);
        FileOutputStream out = new FileOutputStream(file2);
        try {
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } catch (Exception e) {
            System.out.println("command not found or invalid parameter" + e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        System.out.println("File Copied");
    }

    public void cp_r(String dirPath, String copyDir) throws Exception {
        File file1 = new File(dirPath);
        File file2 = new File(copyDir);
        if (file1.isDirectory()) {
            if (!file2.isDirectory()) {
                Files.createDirectories(Paths.get(copyDir));
            }
            copyFolder(Paths.get(dirPath), Paths.get(copyDir));
        } else {
            System.err.printf("command not found or invalid parameter");
        }
    }

    public void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }

    private void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void echo(String x) {
        System.out.println(x);
    }

    public String pwd() {
        return System.getProperty("user.dir");
    }


    public void ls() {
        String current = Paths.get("").toAbsolutePath().toString();
        File file = new File(current);
        // returns an array of all files
        String[] fileList = file.list();
        for (String str : fileList) {
            System.out.println(str);
        }
    }

    //reverse order alphabetically: ls -r
    public void ls_r() {
        String current = pwd();
        File file = new File(current);
        String[] fileList = file.list();
        for (int i = fileList.length-1; i >= 0; i--) {			
        	System.out.println(fileList[i]);
		}
    }

    public void mkdir(String[] args) throws Exception {
        int errcount = 0;
        for (int i = 0; i < args.length; i++) {
            File file = new File(args[i]);
            if (!file.isDirectory()) {
                Files.createDirectories(Paths.get(args[i]));
            } else {
                errcount++;
                System.out.println("Can't over write " + args[i]);
            }
        }
        System.out.println("Process finished with " + errcount + " Errors");
    }

    public void touch(String filePath) throws Exception {
        String result = "";
        Terminal ter = new Terminal();
        if (filePath.contains(".")) {
            result = ter.pwd() + filePath.substring(1);
        } else {
            result += filePath;
        }
        File file1 = new File(result);
        try {
            Files.createFile(Paths.get(filePath));
        } catch (FileAlreadyExistsException e) {
            System.out.println("command not found or invalid parameter");
        }
        System.out.println(file1.getName() + " File Created");
    }

    public static void main(String[] args) throws IOException, Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("******************Command Line Interpreter******************");
        while (true) {
            Parser pp = new Parser();
            System.out.print("> ");
            String a1 = in.nextLine();
            if (a1.equals("exit") || a1.equals("Exit")) {
                break;
            }
            boolean state = pp.parse(a1);
            Terminal ter = new Terminal();
            if (state == true) {
                switch (pp.commandName) {
                    //"ls -r", "ls","pwd","cd","rmdir","rm","echo","touch","cp -r","cp","cat","mkdir"
                    case "ls -r":
                        ter.ls_r();
                        break;
                    case "ls":
                        ter.ls();
                        break;
                    case "pwd":
                        System.out.println(ter.pwd());
                        break;
                    case "cd":
                        if (pp.args[0] == null) {
                            System.out.println(ter.cd());
                        } else {
                            ter.cd(pp.args[0]);
                        }
                        break;
                    case "rmdir":
                        ter.rmdir(pp.args[0]);
                        break;
                    case "rm":
                        ter.rm(pp.args[0]);
                        break;
                    case "echo":
                        ter.echo(pp.args[0]);
                        break;
                    case "touch":
                        ter.touch(pp.args[0]);
                        break;
                    case "cp -r":
                        ter.cp_r(pp.args[0], pp.args[1]);
                        break;
                    case "cp":
                        ter.cp(pp.args[0], pp.args[1]);
                        break;
                    case "cat":
                        ter.Cat(pp.args[0], pp.args[1]);
                        break;
                    case "mkdir":
                        int tt = 0;
                        for (int i = 0; i < 10; i++) {
                            if (pp.args[i] != null) {
                                tt++;
                            }
                        }
                        String[] arr = new String[tt];
                        int rr = 0;
                        for (int i = 0; i < 10; i++) {
                            if (pp.args[i] != null) {
                                arr[rr] = pp.args[i];
                                rr++;
                            } else {
                                break;
                            }
                        }
                        ter.mkdir(arr);
                        break;
                }
            } else {
                System.out.println("command not found or invalid parameter");
            }

        }
    }

}