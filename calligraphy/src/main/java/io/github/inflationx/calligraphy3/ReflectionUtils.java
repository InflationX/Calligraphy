package io.github.inflationx.calligraphy3;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectionUtils {

    private static final String TAG = ReflectionUtils.class.getSimpleName();

    static Method getMethod(Class clazz, String methodName) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    static void invokeMethod(Object object, Method method, Object... args) {
        try {
            if (method == null) return;
            method.invoke(object, args);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "Can't access method using reflection", e);
        } catch (InvocationTargetException e) {
            Log.d(TAG, "Can't invoke method using reflection", e);
        }
    }
}
