package kr.coder.ba.bacoderdevproject.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtil {
    private volatile static FileUtil instance;
    public static FileUtil getInstance(){
        if(instance == null){
            synchronized (FileUtil.class){
                if(instance == null){
                    instance = new FileUtil();
                }
            }
        }
        return instance;
    }
    private File getFileDir(Context context){
        String state = Environment.getExternalStorageState();
        File filesDir;
        if(Environment.MEDIA_MOUNTED.equals(state)){
            filesDir = context.getExternalFilesDir(null);
        }else{
            filesDir = context.getFilesDir();
        }

        return filesDir;
    }

    public File getImageFile(Context context, String name){
        return new File(FileUtil.getInstance().getFileDir(context), name + ".png");
    }
}
