package com.texas.poker.util;



import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 *
 * ��˵��
 *
 * @author RinfonChen:
 * @Day 2014��8��21�� 
 * @Time ����9:53:32
 * @Declaration :
 *
 */
public class FileUtil {
    private Context mContext;
    private final String SDPATH;
    /**
     * �����ļ���ģʽ���Ѿ����ڵ��ļ�Ҫ����
     */
    public final static int MODE_COVER = 1;

    /**
     * �����ļ���ģʽ���ļ��Ѿ���������������
     */
    public final static int MODE_UNCOVER = 0;
    
    public FileUtil(Context context){
        mContext = context;
        SDPATH = Environment.getExternalStorageDirectory().getPath();
    }
    
    public static boolean createFile(String path, int mode) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                if (mode == FileUtil.MODE_COVER) {
                    file.delete();
                    file.createNewFile();
                }
            } else {
                // ���·�������ڣ��ȴ���·��
                File mFile = file.getParentFile();
                if (!mFile.exists()) {
                    mFile.mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
 // ���ļ�д��Ӧ�õ�data/data��filesĿ¼��  
    public void writeDateFile(String fileName, String buffer) throws Exception {  
        byte[] buf = fileName.getBytes("iso8859-1");  
        fileName = new String(buf, "utf-8");  
        // Context.MODE_PRIVATE��ΪĬ�ϲ���ģʽ�������ļ���˽����ݣ�ֻ�ܱ�Ӧ�ñ�����ʣ��ڸ�ģʽ�£�д������ݻḲ��ԭ�ļ������ݣ���������д�������׷�ӵ�ԭ�ļ��С�����ʹ��Context.MODE_APPEND  
        // Context.MODE_APPEND��ģʽ�����ļ��Ƿ���ڣ����ھ����ļ�׷�����ݣ�����ʹ������ļ���  
        // Context.MODE_WORLD_READABLE��Context.MODE_WORLD_WRITEABLE������������Ӧ���Ƿ���Ȩ�޶�д���ļ���  
        // MODE_WORLD_READABLE����ʾ��ǰ�ļ����Ա�����Ӧ�ö�ȡ��MODE_WORLD_WRITEABLE����ʾ��ǰ�ļ����Ա�����Ӧ��д�롣  
        // ���ϣ���ļ�������Ӧ�ö���д�����Դ��룺  
        // openFileOutput("output.txt", Context.MODE_WORLD_READABLE +  
        // Context.MODE_WORLD_WRITEABLE);  
//        File file = new File(SDPATH + "//" + fileName); 
//        if (!file.exists()) { 
//            file.createNewFile(); 
//        }
//        
//        FileWriter fw = new FileWriter(SDPATH + "//" + fileName); 
//        Log.i("Rinfon",SDPATH + "//" + fileName);
//        FileOutputStream fos = mContext.openFileOutput(SDPATH + "//" + fileName,  
//                Context.MODE_APPEND);// ������ļ�����  
//        fos.write(buffer);  
//        fos.close();  
        
      //ͨ��getExternalStorageDirectory������ȡSDCard���ļ�·��  
        File file = new File(SDPATH, fileName);  
        if (!file.exists()) { 
          file.createNewFile(); 
        }
        //��ȡ�����  
//        FileOutputStream outStream = new FileOutputStream(file);
//        outStream.write(buffer.getBytes());  
//        outStream.close();
        
        FileWriter writer = new FileWriter(SDPATH + "//" +fileName, true);  
        writer.write(buffer);  
        writer.close();  
    }  
    
    /**
     * ���ļ���ĩβ������
     * 
     * @param path
     * @param data
     */
    public static boolean appendData(String path, byte[] data) {
        try {
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
