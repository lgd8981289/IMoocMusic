## 慕课网-慕课音乐项目代码
慕课网视频地址：https://www.imooc.com/learn/1104  
个人站地址：http://lgdsunday.club/  
如果您觉得课程对您有所帮助。那么可以在慕课网关注我，也可以在Github上为我点star

---

 android O 的 NotificationChannl 适配和 9.0 的权限适配代码已经更新到了 github。

 主要修改的地方有以下三点：

1、android O 的 NotificationChannel 。 代码位于 MusicService 类 startForeground 方法中。

2、android 9.0 的网络访问问题。代码位于 AndroidManfest.xml 中 application 中 新增了 networkSecurityConfig。

3、android 9.0 前台权限问题。代码位于 AndroidManfest.xml 中 新增了

`<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />`