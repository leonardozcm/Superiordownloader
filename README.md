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
