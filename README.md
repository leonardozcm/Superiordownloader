# Superiordownloader
简单好用的下载管理器

## 十月一日更新
* 完成主页面UI。
* 学习了多线程下载的基本原理与实现思路。
* 按照思路写成了主要实现类：DownloadTask和DownloadService。
### 遇到的坑：
* LayoutManager遇到空指针报错，没有发现是recyclerview没有绑定的问题。
* LitePal是可以重复插入的（自定义主键）。
PS：这个版本根本不能使用，明天一天debug。

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
