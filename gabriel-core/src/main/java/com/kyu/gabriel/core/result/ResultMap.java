package com.kyu.gabriel.core.result;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class ResultMap<E> {

    @NonNull
    private int statusCode;
    @NonNull
    private boolean successful;
    @Setter
    private String message;
    @Setter
    private E data;

    public static <E> ResultMap<E> success(){
        return new ResultMap<>(0, true);
    }

    public static <E> ResultMap<E> success(int statusCode){
        return new ResultMap<>(statusCode, true);
    }

    public static <E> ResultMap<E> success(String message){
        return new ResultMap<>(0, true, message, null);
    }

    public static <E> ResultMap<E> success(int statusCode, String message){
        return new ResultMap<>(statusCode, true, message, null);
    }

    public static <E> ResultMap<E> success(E data){
        return new ResultMap<>(0, true, null, data);
    }

    public static <E> ResultMap<E> success(int statusCode, E data){
        return new ResultMap<>(statusCode, true, null, data);
    }

    public static <E> ResultMap<E> success(int statusCode, String message, E data){
        return new ResultMap<>(statusCode, true, message, data);
    }

    public static <E> ResultMap<E> failed(int statusCode){
        return new ResultMap<>(statusCode, false);
    }

    public static <E> ResultMap<E> failed(int statusCode, String message){
        return new ResultMap<>(statusCode, false, message, null);
    }

    public static <E> ResultMap<E> failed(int statusCode, E data){
        return new ResultMap<>(statusCode, false, null, data);
    }

    public static <E> ResultMap<E> failed(int statusCode, String message, E data){
        return new ResultMap<>(statusCode, false, message, data);
    }

    public static <E> ResultMap<E> cast(Object resultMap, Class<E> clazz){
        if (resultMap instanceof ResultMap){
            ResultMap<?> notCastResult = (ResultMap<?>)resultMap;
            ResultMap<E> res = new ResultMap<>();
            res.statusCode = notCastResult.getStatusCode();
            res.successful = notCastResult.isSuccessful();
            res.setMessage(notCastResult.getMessage());
            res.setData(clazz.cast(notCastResult.getData()));
            return res;
        }
        throw new ClassCastException("参数resultMap不是ResultMap类的实例");
    }
}
