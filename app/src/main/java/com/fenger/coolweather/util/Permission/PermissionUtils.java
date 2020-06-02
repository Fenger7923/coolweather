package com.fenger.coolweather.util.Permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * com.fenger.permission
 * Created by fenger
 * in 2019-12-12
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";

    //这个类里面都是静态方法，不允许被实例化
    private PermissionUtils(){
        throw new UnsupportedOperationException("Can't be instantified");
    }

    //判断安卓版本大于6.0
    public static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //获取到权限
    public static void executeSucceedMethod(Object reflectObject, int requestCode){

        Method[] methods = reflectObject.getClass().getDeclaredMethods();// 获取class中所有的方法

        // 遍历找到给了标记的方法
        for (Method method:methods){
            Log.d(TAG, "打印所有方法: "+method);
            // 获取该方法上面有没有打这个标记
            PermissionSuccess succeedMethod =  method.getAnnotation(PermissionSuccess.class);
            Log.d(TAG, "executeSucceedMethod: " + succeedMethod);
            if(succeedMethod != null){
                // 代表该方法打了标记
                // 并且我们的请求码必须 requestCode 一样
                int methodCode = succeedMethod.requestCode();
                if(methodCode == requestCode){
                    // 这个就是我们要找的成功方法
                    // 反射执行该方法
                    Log.d(TAG, "找到了该方法："+method);
                    executeMethod(reflectObject,method);
                }
            }
        }
    }

    //获取权限失败
    public static void executeFailedMethod(Object reflectObject, int requestCode){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Method[] methods = reflectObject.getClass().getDeclaredMethods();// 获取class中所有的方法

        // 遍历找到给了标记的方法
        for (Method method:methods){
            Log.d(TAG, "打印所有方法: "+method);
            // 获取该方法上面有没有打这个标记
            PermissionFail failMethod =  method.getAnnotation(PermissionFail.class);
//            Log.d(TAG, "executeFailMethod: "+failMethod);
            if(failMethod != null){
                // 代表该方法打了标记
                // 并且我们的请求码必须 requestCode 一样
                int methodCode = failMethod.requestCode();
                if(methodCode == requestCode){
                    // 这个就是我们要找的成功方法
                    // 反射执行该方法
                    Log.d(TAG, "找到了该方法："+method);
                    executeMethod(reflectObject,method);
                }
            }
        }
    }

    //反射执行找到的那个方法。传入：找到的类里面的方法
    public static void executeMethod(Object reflectObject, Method method){
        try {
            method.setAccessible(true);//允许执行私有方法//避免需要被执行的方法是private的
            method.invoke(reflectObject,new Object[]{});//执行指定的方法
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static List<String> getDeniedPermissions(Object object, String... mPermissions){
        List<String> deniedPermissions = new ArrayList<>();
        for(String permission:mPermissions){
            //未授予的权限加入集合
            if(ContextCompat.checkSelfPermission(getActivity(object),permission) == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    //获取类的context
    public static Activity getActivity(Object object){
        if(object instanceof Activity){
            return (Activity)object;
        }else if(object instanceof Fragment){
            return ((Fragment)object).getActivity();
        }
        return null;
    }
}
