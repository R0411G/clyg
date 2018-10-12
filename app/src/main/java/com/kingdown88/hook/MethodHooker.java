package com.kingdown88.hook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.kingdown88.wb.BuildConfig;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodHooker {

  private static final String FILTER_PKGNAME = "" + BuildConfig.APPLICATION_ID;
  public static Set<String> methodSignSet = Collections.synchronizedSet(new HashSet<String>());
  public static Set<String> callMethodSignSet = Collections.synchronizedSet(new HashSet<String>());

  public static void hookMethod(LoadPackageParam loadPackageParam) {
    String pkgname = loadPackageParam.packageName;
    if (FILTER_PKGNAME.equals(pkgname)) {
      //这里是为了解决app多dex进行hook的问题，Xposed默认是hook主dex
      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              ClassLoader cl = ((Context) param.args[0]).getClassLoader();
              Class<?> hookclass = null;
              try {
                hookclass = cl.loadClass("dalvik.system.DexFile");
              } catch (Exception e) {
                return;
              }

              XposedHelpers.findAndHookMethod(hookclass, "loadClass", String.class,
                  ClassLoader.class, new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      hookClassInfo((String) param.args[0], (ClassLoader) param.args[1]);
                      super.beforeHookedMethod(param);
                    }
                  });

              XposedHelpers.findAndHookMethod(hookclass, "loadClassBinaryName", String.class,
                  ClassLoader.class, List.class, new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      hookClassInfo((String) param.args[0], (ClassLoader) param.args[1]);
                      super.beforeHookedMethod(param);
                    }
                  });

              XposedHelpers.findAndHookMethod(hookclass, "defineClass", String.class,
                  ClassLoader.class, long.class, List.class, new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      hookClassInfo((String) param.args[0], (ClassLoader) param.args[1]);
                      super.beforeHookedMethod(param);
                    }
                  });
            }
          });
    }
  }

  /**
   * 获取dex路径
   */
  public static String getDexPath(ClassLoader classLoader) {
    try {
      Field field = classLoader.getClass().getSuperclass().getDeclaredField("pathList");
      field.setAccessible(true);
      Object objPathList = field.get(classLoader);
      Field elementsField = objPathList.getClass().getDeclaredField("dexElements");
      elementsField.setAccessible(true);
      Object[] elements = (Object[]) elementsField.get(objPathList);
      for (Object obj : elements) {
        Field fileF = obj.getClass().getDeclaredField("file");
        fileF.setAccessible(true);
        File file = (File) fileF.get(obj);
        return file.getAbsolutePath();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void hookClassInfo(String className, ClassLoader classLoader) {
    //过滤系统类名前缀
    if (TextUtils.isEmpty(className)) {
      return;
    }
    if (className.startsWith("android.")) {
      return;
    }
    if (className.startsWith("java.")) {
      return;
    }

    //利用反射获取一个类的所有方法
    try {
      Class<?> clazz = classLoader.loadClass(className);
      //这里获取类的所有方法，但是无法获取父类的方法，不过这里没必要关系父类的方法
      //如果要关心，那么需要调用getMethods方法即可
      Method[] allMethods = clazz.getDeclaredMethods();
      for (Method method : allMethods) {
        Class<?>[] paramTypes = method.getParameterTypes();
        String methodName = method.getName();
        Object[] param = new Object[paramTypes.length + 1];
        for (int i = 0; i < paramTypes.length; i++) {
          param[i] = paramTypes[i];
        }
        String signStr = getMethodSign(method);
        if (TextUtils.isEmpty(signStr) || isFilterMethod(signStr)) {
          continue;
        }

        //开始构造Hook的方法信息
        param[paramTypes.length] = new XC_MethodHook() {
          @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            String methodSign = getMethodSign(param);
            if (!TextUtils.isEmpty(methodSign) && !callMethodSignSet.contains(methodSign)) {
              //这里因为会打印日志，所以会出现app的ANR情况
              XposedBridge.log("Loaded app call-->" + methodSign);
              //这里还可以把方法的参数值打印出来，不过如果应用过大，这里会出现ANR
              for (int i = 0; i < param.args.length; i++) {
                XposedBridge.log("Loaded app ==>arg" + i + ":" + param.args[i]);
              }
              callMethodSignSet.add(methodSign);
            }
            super.afterHookedMethod(param);
          }
        };

        //开始进行Hook操作，注意这里有一个问题，如果一个Hook的方法数过多，会出现OOM的错误，这个是Xposed工具的问题
        if (!TextUtils.isEmpty(signStr) && !methodSignSet.contains(signStr)) {
          //这里因为会打印日志，所以会出现app的ANR情况
          XposedBridge.log("Loaded app all-->" + signStr);
          methodSignSet.add(signStr);
          XposedHelpers.findAndHookMethod(className, classLoader, methodName, param);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取方法的签名信息
   */
  private static String getMethodSign(MethodHookParam param) {
    try {
      StringBuilder methodSign = new StringBuilder();
      methodSign.append(Modifier.toString(param.method.getModifiers()) + " ");
      Object result = param.getResult();
      if (result == null) {
        methodSign.append("void ");
      } else {
        methodSign.append(result.getClass().getCanonicalName() + " ");
      }
      methodSign.append(
          param.method.getDeclaringClass().getCanonicalName() + "." + param.method.getName() + "(");
      for (int i = 0; i < param.args.length; i++) {
        //这里有一个问题：如果方法的参数值为null,那么这里就会报错! 得想个办法如何获取到参数类型？
        if (param.args[i] == null) {
          methodSign.append("?");
        } else {
          methodSign.append(param.args[i].getClass().getCanonicalName());
        }
        if (i < param.args.length - 1) {
          methodSign.append(",");
        }
      }
      methodSign.append(")");
      return methodSign.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取方法的签名信息
   * public final native String xxx(java.lang.String,int) 类似于这种类型
   */
  private static String getMethodSign(Method method) {
    try {
      //如果这个方法是继承父类的方法，也需要做过滤
      String methodClass = method.getDeclaringClass().getCanonicalName();
      if (methodClass.startsWith("android.") || methodClass.startsWith("java.")) {
        return null;
      }
      StringBuilder methodSign = new StringBuilder();
      Class<?>[] paramTypes = method.getParameterTypes();
      Class<?> returnTypes = method.getReturnType();
      methodSign.append(Modifier.toString(method.getModifiers()) + " ");
      methodSign.append(returnTypes.getCanonicalName() + " ");
      methodSign.append(methodClass + "." + method.getName() + "(");
      for (int i = 0; i < paramTypes.length; i++) {
        methodSign.append(paramTypes[i].getCanonicalName());
        if (i < paramTypes.length - 1) {
          methodSign.append(",");
        }
      }
      methodSign.append(")");
      return methodSign.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 过滤Object对象自带的几个方法，这些方法可以不用做处理
   */
  private static boolean isFilterMethod(String methodSign) {
    if ("public final void java.lang.Object.wait()".equals(methodSign)) {
      return true;
    }
    if ("public final void java.lang.Object.wait(long,int)".equals(methodSign)) {
      return true;
    }
    if ("public final native java.lang.Object.wait(long)".equals(methodSign)) {
      return true;
    }
    if ("public boolean java.lang.Object.equals(java.lang.Object)".equals(methodSign)) {
      return true;
    }
    if ("public java.lang.String java.lang.Object.toString()".equals(methodSign)) {
      return true;
    }
    if ("public native int java.lang.Object.hashCode()".equals(methodSign)) {
      return true;
    }
    if ("public final native java.lang.Class java.lang.Object.getClass()".equals(methodSign)) {
      return true;
    }
    if ("public final native void java.lang.Object.notify()".equals(methodSign)) {
      return true;
    }
    if ("public final native void java.lang.Object.notifyAll()".equals(methodSign)) {
      return true;
    }
    return false;
  }

  /**
   * 跨进程读取数据，会显示失败的，这个方法是无效的，因为methodSignSet数据可能跨进程读取失败
   */
  @SuppressLint("SdCardPath") public static boolean dumpAllMethodInfo() {
    XposedBridge.log("Loaded app all method size:" + methodSignSet.size());
    if (methodSignSet.size() == 0) {
      return false;
    }
    FileWriter fw = null;
    BufferedWriter bw = null;
    try {
      fw = new FileWriter("/sdcard/" + FILTER_PKGNAME + "_allmethod.txt");
      bw = new BufferedWriter(fw);
      for (String methodStr : methodSignSet) {
        bw.write(methodStr);
        bw.newLine();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (fw != null) {
          fw.close();
        }
        if (bw != null) {
          bw.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 跨进程读取数据失败
   */
  @SuppressLint("SdCardPath") public static boolean dumpCallMethodInfo() {
    XposedBridge.log("Loaded app call method size:" + callMethodSignSet.size());
    if (callMethodSignSet.size() == 0) {
      return false;
    }
    FileWriter fw = null;
    BufferedWriter bw = null;
    try {
      fw = new FileWriter("/sdcard/" + FILTER_PKGNAME + "_callmethod.txt");
      bw = new BufferedWriter(fw);
      for (String methodStr : callMethodSignSet) {
        bw.write(methodStr);
        bw.newLine();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (fw != null) {
          fw.close();
        }
        if (bw != null) {
          bw.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
