package com.gc.baggoid;

/**
 * Created by Marios Sifalakis on 8/23/17.
 * Class is used to Store variables of GetWeather Class
 */

public class Config {
    public static String WEATHER_API_KEY = "8855d8df419b9e97c03b5c7fc9297d06";
    public static Double FIXED_LATITUDE = 40.714628;
    public static Double FIXED_LONGITUDE = -74.007315;

    //WEATHER CONDITION CODES
    public static Integer[] cloudyWeatherConditionCodes = new Integer[]{801,802,803,804,701, 711, 721, 731, 741, 751, 761, 762, 771, 781};
    public static Integer[] sunnyWeatherConditionCodes = new Integer[]{951,952,953,954,955,956,957,958,959,960,961,962,800};
    public static Integer[] rainyWeatherConditionCodes = new Integer[]{200,201,202,210,211,212,221,230,231,232,300,301,302,310,311,312,313,314,321,500,501,502,503,504,511,520,521,522,531,600,601,602,611,612,615,616,620,621,622,900,901,902,903,904,905,906};


    //Game types
    public static int GAME_TYPE_1 = 1;      //Game Rules: First to reach 21
    public static int GAME_TYPE_2 = 2;      //Game Rules: First to reach exactly 21
}
