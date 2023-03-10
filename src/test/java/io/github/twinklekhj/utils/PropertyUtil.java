package io.github.twinklekhj.utils;

import io.github.twinklekhj.ros.RosBridgeTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    public static final String fileName = "config/config.properties";

    /**
     * [PropertyUtil] Property 값 가져오기
     * @param key key
     * @return value
     */
    public static String getProperty(String key){
        Properties properties = readProperties(fileName);
        if(properties != null){
            return properties.getProperty(key);
        }
        return "";
    }

    public static int getPropertyInt(String key){
        return parseInt(getProperty(key));
    }

    public static int parseInt(Object val){
        if(val == null) return -1;
        try {
            return Integer.parseInt(val.toString());
        } catch (NumberFormatException e){
            return -1;
        }
    }

    /**
     * [PropertyUtil] 파일을 가져와 Properties 객체 생성
     *
     * @param fileName 파일명
     * @return Properties 객체 반환
     */
    public static Properties readProperties(String fileName) {
        Properties prop = new Properties();
        InputStream inputStream = RosBridgeTest.class.getClassLoader().getResourceAsStream(fileName);

        try {
            if (inputStream != null) {
                prop.load(inputStream);
                return prop;
            } else {
                throw new FileNotFoundException(String.format("Can not Found Properties File - %s", fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
