package com.fenger.coolweather.util.Permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * com.fenger.permission
 * Created by fenger
 * in 2019-12-12
 */
public class PermissionHelper {
    //这个类用于权限处理
    //功能包括:1.检查权限。2.申请权限。3.版本低于23时适配。4.重复请求权限。


    // 传递的参数定义
    private Object mObject;
    private int mRequestCode;//请求码
    private String[] mRequestPermission;//所请求或者检查的权限

    private PermissionHelper(Object object){
        this.mObject = object;
    }

    //直接传入参数//只用于activity和fragment//直接调用的方式
    public static void requestPermission(Activity activity, int requestCode, String[] permissions){
        PermissionHelper.with(activity).requestCode(requestCode).requestPermission(permissions).request();
    }

    public static void requestPermission(Fragment fragment, int requestCode, String[] permissions){
        PermissionHelper.with(fragment).requestCode(requestCode).requestPermission(permissions).request();
    }

    //链式传递
    public static PermissionHelper with(Activity activity){
        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment){
        return new PermissionHelper(fragment);
    }

    //请求码
    public PermissionHelper requestCode(int requestCode){
        this.mRequestCode = requestCode;
        return this;
    }

    //权限的数组
        //Stirng…的写法主要是不限制后面一个String参数的个数，
        // 可以理解为传递了一个String的List 过来是一样的 ，
        // 这么写的优点就是在调用的时候不用重新构造一个List。
        //在List<String> 中如果String值只有两三个的时候，且调用比较频繁的时候可以用String…代替List<String>
    public PermissionHelper requestPermission(String... permissions){
        this.mRequestPermission = permissions;
        return this;
    }

    public void request(){
        //判断是否是6.0以上
        if(!PermissionUtils.isOverMarshmallow()){
            // 如果不是6.0以上  那么进行判断
            for(String permission:mRequestPermission){
                //未授予的权限加入集合
                if(ContextCompat.checkSelfPermission(PermissionUtils.getActivity(mObject),permission) == PackageManager.PERMISSION_DENIED){
                    //如果存在未打开的权限就弹窗
                    Toast.makeText(PermissionUtils.getActivity(mObject),"有权限未打开哦", Toast.LENGTH_LONG);
                    return;
                }
            }
            //全部权限都打开了就直接运行了
            PermissionUtils.executeSucceedMethod(mObject,mRequestCode);
            return;
        }

        // 如果是6.0以上
        // 获取需要申请的权限中 获取还没有获得的权限
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject,mRequestPermission);

        if(deniedPermissions.size() == 0){
            //所有权限都已获得
            PermissionUtils.executeSucceedMethod(mObject,mRequestCode);
        }else {
            // 存在权限未获得
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),deniedPermissions.toArray(new String[deniedPermissions.size()]), mRequestCode);
        }
    }

    //处理权限请求之后的回调
    public static void onRequestPermissionResult(Object object, int requestCode, String[] permissions){
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object,permissions);

        if(deniedPermissions.size() == 0){
            //所有权限都已获得
            PermissionUtils.executeSucceedMethod(object,requestCode);
        }else {
            // 存在权限未获得
            PermissionUtils.executeFailedMethod(object,requestCode);
        }
    }
}
