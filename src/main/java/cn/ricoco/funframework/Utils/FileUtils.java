package cn.ricoco.funframework.Utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    public static void CopyFile(File orifile,File tofile) throws IOException {
        FileInputStream infile=new FileInputStream(orifile);
        FileOutputStream outfile=new FileOutputStream(tofile);
        FileChannel inc=infile.getChannel();
        FileChannel outc=outfile.getChannel();
        inc.transferTo(0,inc.size(),outc);
        inc.close();
        outc.close();
        infile.close();
        outfile.close();
    }
    public static void Copydir(String oridir,String todir) throws IOException {
        File[] flist=new File(oridir).listFiles();
        for(int i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                CopyFile(flist[i],new File(new File(todir).getPath()+"/"+flist[i].getName()));
            }else{
                String faps=flist[i].getPath();
                new File(new File(todir).getPath()+"/"+faps.substring(new File(oridir).getPath().length(),faps.length())+"/").mkdirs();
                Copydir(faps,new File(todir).getPath()+"/"+faps.substring(new File(oridir).getPath().length(),faps.length())+"/");
            }
        }
    }
    public static void deldir(String dir) throws IOException {
        File[] flist=new File(dir).listFiles();
        for(int i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                flist[i].delete();
            }else{
                deldir(flist[i].getPath());
            }
        }
    }
    public static void unescapeB64(String b64,String outdir) throws IOException {
        byte[] b=Base64.getDecoder().decode(b64);
        FileOutputStream out=new FileOutputStream(outdir);
        for (int i=0;i<b.length;++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        out.write(b);
        out.flush();
        out.close();
    }
    public static String readFile(String fileName) {
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
            return new String(filecontent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void writeFile(String path,String text) {
        try {
            Writer writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void ReadJar(String fileName,String JarDir,String path){
        try {
            String jarName = JarDir;
            JarFile jarFile = new JarFile(jarName);
            JarEntry entry = jarFile.getJarEntry(fileName);
            InputStream input = jarFile.getInputStream(entry);
            writeIS(input,new File(path));
            input.close();
            jarFile.close();
        }catch (Exception e){
            System.out.println(new String(String.valueOf(e)));
        }
    }
    private static void writeIS(InputStream input,File file) {
        try {
            java.nio.file.Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String loadCFG(String fname,String pluginName,String JarDir){
        if(!new File("./plugins/"+pluginName+"/"+fname).exists()){
            new File(new File("./plugins/"+pluginName+"/"+fname).getPath()).mkdirs();
            ReadJar("resources/"+fname,JarDir,"./plugins/"+pluginName+"/"+fname);
        }
        return readFile("./plugins/"+pluginName+"/"+fname);
    }
}
