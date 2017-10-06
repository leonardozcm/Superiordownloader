# Superiordownloader
简单好用的下载管理器

## 十月六日更新
* 基本完成了对剪切板的监听。
* 完成了对浏览器下载任务的监听。
至此，基本要求全部完成。
## 收获
* 学会了阅读Android源码来找要用的方法。
* 了解了Android文件匹配应用的机制，更加理解AndroidManifest的作用。
* 第一次使用剪切板方法。
## 遇到的坑
* 在做最后的监听浏览器的需求时，我的思维被跨进程获取监听权限所限制，苦恼了一个下午，而google后也发现这方面的资料远远没有前面丰富。
在查资料的过程中我发现了网页打开APP的这个机制，于是打开了思路，发现这也可以变相地看作一种监听。从这方面入手，终于查到了intent-filter的另一种用法，顺利完成了需求。
## 待实现的模块
* 检测URL合法性。
* 选择储存目录。
* 防误删机制。

## 十月五日更新
* 设置了通知栏点击事件。
* 实现了“已完成”Fragment，点击已完成任务可以自动打开文件。
* 设置了防重复下载功能。
* 修复了多任务显示问题。
### 收获
* 了解到Android打开文件机制。
* 通过阅读文档了解adapter的notify机制。
### 遇到的坑
* 因为把下载的item的layout_height设置成了match_parent,导致我一直以为第二个新建的task显示不出来，其实是显示在了屏幕外。
### 待实现的模块
* 监听浏览器的下载请求。
* 监听剪切板。
* 检测URL合法性。
* 选择储存目录。
* 防误删机制。

## 十月四日更新
* 完成了通知栏显示进度。
* 实现后台下载。
* 微调了布局。
### 收获
* 学会了使用Android官方文档。
* Collector全局调用想用的context对象。
* 了解了安卓系统的内存回收机制。
* 学会了通知的高级用法。
* 学会了使用debug工具。
### 遇到的坑
* 不知道安卓系统回收活动时默认回收服务，因此很难实现后台下载。
### 待实现的模块
* 下载完毕提醒功能
* 已完成列表
* 监听功能

## 十月三日更新
* 修复了多线程重复下载的bug。
* 修复了及时更新数据的频率问题。
* 修复了暂停功能。
* 暂时修复Service退出异常。
* 美化了布局。
### 收获：
了解了服务器返回数据的原理。
### 遇到的坑
* Range请求码出错导致多线程重复下载。
* 在储存“是否暂停”标志的时候，发现false无法存进去，花费了一下午时间，最后只能换成递加对2取模的替代方法。
### 待实现的模块
* 通知栏
* 监听功能
* 后台下载功能
* 去除第三方库litepal依赖。

## 十月二日更新
* 实现了多线程断点下载。
* 不稳定的暂停功能。
* 添加URL创建下载项目功能。
* UI跟随更新。
### 收获：
 今天收获很大，在理解原理的基础上成功修复了昨天的bug，明白了序列化的含义，序列化的对象和Intent连用简直不能更方便。debug时加深了对logcat的应用理解。
### 遇到的坑：
* 虚拟机找不到下载文件——解决方法：换实体机。
* 文件长度的读取，多线程同时读取。
### 待实现的模块
* 解决退出活动Service抛出异常（绑定活动解决）。
* 通知栏。
* 监听功能（可能使用内容提供器）。
* 去除第三方库litepal依赖。
* 美化界面。

## 十月一日更新
* 完成主页面UI。
* 学习了多线程下载的基本原理与实现思路。
* 按照思路写成了主要实现类：DownloadTask和DownloadService。
### 遇到的坑：
* LayoutManager遇到空指针报错，没有发现是recyclerview没有绑定的问题。
* LitePal是可以重复插入的（自定义主键）。
PS：这个版本根本不能使用，明天一天debug。
