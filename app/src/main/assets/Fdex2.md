
安装apk后hook相关应用导出相应的dex文件利用jd-gui查看源码

https://vxposed.com/

https://www.52pojie.cn/thread-803607-1-1.html

基础源码分析
1.反编译xx.apk，分析smail代码，查看分包等情况；

2.利用jadx导出Java代码进一步分析方法调用流程；

3.解密方法在基础包里，解密关键部分在so里面处理，so可能有签名验证、初始化等操作导致无法直接调用；

解密源码分析
4.利用Xposed框架进行关键步骤方法hook,由此进行解密方法调用操作：

参考步骤地址http://blog.csdn.net/chenhao0428/article/details/51360554

如果某个APP的dex有多个在安卓5，0以上ART会合成一个oat文件。那么5.0以下会存在多个dex。 
  所以在5.0以下hook一个某个方法，而这个方法不在主dex，而存在分包dex。
  此时xposed会在没有加载分包dex的时候进行回调handleLoadPackage().此时类加载器并没有加载分包里面的类 
  会导致 XposedHelpers.findAndHookMethod 抛出异常。

(https://blog.csdn.net/fuchaosz/article/details/53143216)

(http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/1002/8570.html)

(http://www.idoog.cn/?p=2933) apkdb

(https://blog.csdn.net/Fisher_3/article/details/78654450)   jadx 

(https://sec.xiaomi.com/article/12)

(http://www.apkbus.com/thread-589781-1-1.html)

(https://www.aliyun.com/jiaocheng/28880.html)

(http://wps2015.org/drops/drops/Android.Hook%E6%A1%86%E6%9E%B6xposed%E7%AF%87(Http%E6%B5%81%E9%87%8F%E7%9B%91%E6%8E%A7).html)

(https://www.52pojie.cn/thread-676272-1-1.html)

(https://blog.csdn.net/groundhappy/article/details/80236872)