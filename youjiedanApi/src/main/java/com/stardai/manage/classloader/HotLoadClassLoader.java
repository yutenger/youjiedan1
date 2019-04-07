package com.stardai.manage.classloader;

import java.io.*;

public class HotLoadClassLoader extends ClassLoader{
    private String classpath;
    private static HotLoadClassLoader classLoader=new HotLoadClassLoader();
    private HotLoadClassLoader(){
        super(HotLoadClassLoader.class.getClassLoader());
        if(classpath==null) {
            this.classpath=this.getClass().getResource("/").getPath();
        }
    }
    public static void updateClassLoader(){
        classLoader=new HotLoadClassLoader();
    }
    public static ClassLoader getSingleClassLoader(){
        return classLoader;
    }
    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            if (name.contains("com.stardai.manage.hotLoadClass")) {
                String path = classpath + File.separatorChar +
                        name.replace('.', File.separatorChar) + ".class";
                File f = new File(path);
                Class<?> c = findLoadedClass(name);
                if (c != null) {
                    return c;
                }
                return findClass(name);
            }
            return super.loadClass(name, resolve);
        }
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte [] classDate=getDate(name);
            if(classDate==null){
                throw new ClassNotFoundException(name);
            } else{
                return defineClass(name,classDate,0,classDate.length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }
    private byte[] getDate(String className) throws IOException {
        String path=classpath + File.separatorChar +
                className.replace('.', File.separatorChar)+".class";
        File f=new File(path);
        try(InputStream in=new FileInputStream(path);
            ByteArrayOutputStream out=new ByteArrayOutputStream();
                ){
            byte[] buffer=new byte[2048];
            int len=0;
            while((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            return out.toByteArray();
        }
    }
}
