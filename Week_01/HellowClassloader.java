import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            HelloClassLoader helloClassLoader = new HelloClassLoader();
            Class<?> hello = helloClassLoader.findClass("Hello");
            Object o = hello.newInstance();
            Method method = hello.getMethod("hello");
            method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] classBytes = new byte[0];
        try {
            classBytes = getClassBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < classBytes.length; i++) {
            classBytes[i] = (byte) (255 - classBytes[i]);
        }
        return defineClass(name, classBytes, 0,classBytes.length);
    }

    private byte[] getClassBytes() throws Exception {
        File file = new File("E:/Hello.xlass");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream);
        ByteBuffer by = ByteBuffer.allocate(1024);
        while (true) {
            int i = fc.read(by);
            if (i == 0 || i == -1)
                break;
            by.flip();
            writableByteChannel.write(by);
            by.clear();
        }
        fis.close();
        return byteArrayOutputStream.toByteArray();
    }
}
