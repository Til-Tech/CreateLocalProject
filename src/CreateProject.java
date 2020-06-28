import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.awt.Desktop;

class CreateProject {

    static String projectName;
    static String projectPath;

    public void createMainDirectory() {
        File mainDirectory = new File("****" + projectName);
        if (mainDirectory.exists()) {
            System.out.println("Project exists already!");
        } else {
            mainDirectory.mkdir();
            projectPath = mainDirectory.getPath();
        }
    }

    public File createDirectories() {
        File directories;
        directories = new File(projectPath + "\\bin");
        directories.mkdir();
        directories = new File(projectPath + "\\lib");
        directories.mkdir();
        directories = new File(projectPath + "\\src");
        directories.mkdir();
        return directories;
    }

    public void createJavaClass(File srcDirectory) throws IOException {
        File javaFile = new File(srcDirectory.getPath() + "\\" + projectName + ".java");
        javaFile.createNewFile();
        RandomAccessFile javaClass = new RandomAccessFile(javaFile, "rw");
        javaClass.seek(0);
        javaClass.writeBytes("class " + projectName + " {\n\n\n   public static void main(String[] args) {\n\n   }\n}");
        javaClass.close();

    }

    public void createManifest() throws IOException {
        File manifest = new File(projectPath + "\\launch.txt");
        manifest.createNewFile();
        RandomAccessFile manifestFile = new RandomAccessFile(manifest, "rw");
        manifestFile.seek(0);
        manifestFile.writeBytes("Main-Class: " + projectName + "\nClass-Path: \n");
        manifestFile.close();
    }

    public void createXMLClasspath() throws IOException {
        File classpath = new File(projectPath + "\\.classpath");
        classpath.createNewFile();
        RandomAccessFile classpathRA = new RandomAccessFile(classpath, "rw");
        classpathRA.seek(0);
        classpathRA.writeBytes(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<classpath>\n    <classpathentry kind=\"src\" path=\"src\"/>\n    <classpathentry kind=\"lib\" path=\"\"/>\n    <classpathentry kind=\"output\" path=\"bin\"/>\n</classpath>");
        classpathRA.close();
    }

    public static boolean forbiddenCharcaters(String name) {

        boolean wrongCharacter = false;

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '?' || name.charAt(i) == '\\' || name.charAt(i) == '/' || name.charAt(i) == ':'
                    || name.charAt(i) == '*' || name.charAt(i) == '"' || name.charAt(i) == '<' || name.charAt(i) == '>'
                    || name.charAt(i) == '|') {
                wrongCharacter = true;
            }
        }
        return wrongCharacter;
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("You have to declare a project name -->@Example: create project [your project name]");
        } else {
            StringBuffer sb = new StringBuffer();
            if (args.length > 1) {
                for (int i = 0; i < args.length; i++) {

                    if (i == (args.length - 1)) {
                        sb.append(args[i]);
                    } else {

                        sb.append(args[i] + " ");
                    }
                }
                if (forbiddenCharcaters(sb.toString())) {
                    System.out.println("Your project name cannot contain following characters: [\\/:*?\"<>|]");
                    System.exit(1);
                } else {
                    projectName = sb.toString();
                }
            } else {
                if (forbiddenCharcaters(args[0].toString())) {
                    System.out.println("Your project name cannot contain following characters: [\\/:*?\"<>|]");
                    System.exit(1);
                } else {
                    projectName = args[0].toString();
                }
            }

            CreateProject newProject = new CreateProject();

            try {
                newProject.createMainDirectory();
                newProject.createJavaClass(newProject.createDirectories());
                newProject.createManifest();
                newProject.createXMLClasspath();
                File projectDirectory = new File(projectPath);
                Desktop.getDesktop().open(projectDirectory);
                Runtime.getRuntime()
                        .exec("****\\Microsoft VS Code\\Code.exe " + projectPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}