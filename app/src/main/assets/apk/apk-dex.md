
### 模拟器或是root手机
====================

### 脱壳工具drizzleDumper
        https://github.com/DrizzleRisk/drizzleDumper
        或
        链接：https://pan.baidu.com/s/1el92IpYMO0WXKbOAUKioNQ 提取码：k09g

### 步骤
        adb shell dumpsys activity top  查看当前包名com.xxx.xxx
        adb push drizzleDumper /data/local/tmp/drizzleDumper
        adb shell
        cd /data/local/tmp
        chmod 777 drizzleDumper
        ./drizzleDumper com.xxx.xxx
        exit
        adb pull /data/local/tmp/xxx.dex c:/xxx.dex

        使用dex2jar工具导出jar再来分析
        使用jadx查看并导出gradle项目

### 使用工具AndroidKiller
        链接：https://pan.baidu.com/s/1EF5YhDLH2IOzzX9ktaTrew 提取码：m3qh


1.Zjdroid
github:https://github.com/halfkiss/ZjDroid
原理分析:http://blog.csdn.net/jiangwei0910410003/article/details/52840602

2. DexExtractor
github:https://github.com/bunnyblue/DexExtractor
原理分析:http://blog.csdn.net/qq1084283172/article/details/53557894

3. dexdump
github:https://github.com/smartdone/dexdump
基于Xposed插件

4.drizzleDumper
github:https://github.com/DrizzleRisk/drizzleDumper
原理分析:http://blog.csdn.net/qq1084283172/article/details/53563.1622

5.TUnpacker(Python)
github:https://github.com/DrizzleRisk/TUnpacker

6.BUnpacker (Python)
github:https://github.com/DrizzleRisk/BUnpacker