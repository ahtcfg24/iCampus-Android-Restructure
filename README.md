# iCampus-Android-Restructure
Restructure by XuDing, HYL
iCampus-Android-Restructure
==========================

工程结构
-------
> - model 模型包
> - utils 工具包
> - http 网络访问(已经封装完毕，不需要再添加)
> - oauth 用户认证(已经封装完毕，不需要再添加)
> - ui 自定义ui
> - fragment 存放fragment
> - 如还有需要，再添加


命名规则
-------

- Java文件

> - 驼峰命名法 例：MainActivity
> - Activity以 ‘名词+Activity’ 例：OauthActivity
> - Fragment以 ‘名词+Fragment’ 例：OauthFragment
> - 其余java文件必须做到见名之义

- 资源文件

> - Layout文件
> - activity的layout文件以‘activity+名词+名词+...’ 例：actvity_oauth
> - fragment的layout文件以‘fragment+名词+名词+...’ 例：fragment_oauth
> - listview,gridview等控件的item布局文件以'item+名词+名词+...'例：item_form_list.xml
> - 其余布局文件以‘名词+名词+...’ 例：custom_marker_view.xml
> - 注意：名词与名词之间以下划线连接，所有名词为小写
