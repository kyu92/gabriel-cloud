package com.kyu.gabriel.core.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CastUtil {

    public static <T> List<T> castList(Object list, Class<T> clazz){
        if (list == null){
            return null;
        }
        List<T> result = new ArrayList<>();
        if (list instanceof List<?>){
            for (Object o : (List<?>)list){
                result.add(clazz.cast(o));
            }
        }
        return result;
    }

    public static <K,V> List<Map<K, V>> castMapList(Object list, Class<K> kClass, Class<V> vClass){
        if (list == null){
            return null;
        }
        List<Map<K, V>> result = new ArrayList<>();
        if (list instanceof List<?>){
            for (Object o : (List<?>)list){
                result.add(castMap(o, kClass, vClass));
            }
        }
        return result;
    }

    public static <K, V> Map<K, V> castMap(Object map, Class<K> kClass, Class<V> vClass){
        if (map == null){
            return null;
        }
        Map<K, V> result = new HashMap<>();
        if (map instanceof Map<?, ?>){
            Map<?, ?> tmp = (Map<?, ?>) map;
            for (Object o : tmp.keySet()){
                result.put(kClass.cast(o), vClass.cast(tmp.get(o)));
            }
        }
        return result;
    }

    public static <K, T> Map<K, List<T>> castListMap(Object map, Class<K> kClass, Class<T> tClass){
        if (map == null){
            return null;
        }
        Map<K, List<T>> result = new HashMap<>();
        if (map instanceof Map<?, ?>){
            Map<?, ?> tmp = (Map<?, ?>) map;
            for (Object o : tmp.keySet()){
                result.put(kClass.cast(o), castList(tmp.get(o), tClass));
            }
        }
        return result;
    }
}
