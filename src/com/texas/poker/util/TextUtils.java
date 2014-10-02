package com.texas.poker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import android.content.Context;
import android.widget.EditText;


/**
 * @description 文本工具类
 */
public class TextUtils {

    /**
     * 判断文本框的内容是否为空
     * 
     * @param editText
     *            需要判断是否为空的EditText对象
     * @return boolean 返回是否为空,空(true),非空(false)
     */
    public static boolean isNull(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text != null && text.length() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 返回指定长度的一串数字
     * 
     * @param NumLen 数字串位数
     * @return
     */
    public static String getRandomNumStr(int NumLen) {
        Random random = new Random(System.currentTimeMillis());
        StringBuffer str = new StringBuffer();
        int i, num;
        for (i = 0; i < NumLen; i++) {
            num = random.nextInt(10); // 0-10的随机数
            str.append(num);
        }
        return str.toString();
    }

    /**
     * 获取Assets中的json文本
     * 
     * @param context
     *            上下文
     * @param name
     *            文本名称
     * @return
     */
    public static String getJson(Context context, String name) {
        // TODO 待清除
        if (name != null) {
            String path = "json/" + name;
            InputStream is = null;
            try {
                is = context.getAssets().open(path);
                return readTextFile(is);
            }
            catch (IOException e) {
                return null;
            }
            finally {
                try {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                }
                catch (IOException e) {

                }
            }
        }
        return null;
    }

    /**
     * 从输入流中获取文本
     * 
     * @param inputStream
     *            文本输入流
     * @return
     */
    public static String readTextFile(InputStream inputStream) {
        // TODO 待清除
        String readedStr = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                readedStr += tmp;
            }
            br.close();
            inputStream.close();
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
        catch (IOException e) {
            return null;
        }

        return readedStr;
    }

}
