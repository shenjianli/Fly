http://www.jianshu.com/p/d9e4ddd1c530



为了避免合作开发写的代码风格迥异。或做出了多套开发模式。下面是个例子。毕竟是为了高效开发而制定的。适合自己项目的才是最好。
所有Activity继承BaseActivity
所有Fragment继承BaseFragment
所有Presenter继承BasePresenter
这样利于生命周期管理。也可以方便的全局修改。
命名，例
AccountFragment
UserDetailActivity

layout命名，例
activity_collection
fragment_account
item_person
include_toolbar
view_progress
不过对于庞大项目的开发。近百个activity开头的layout列表还是会眼瞎。所以那种情况会在前面加上模块名。

id命名，例
btn_send
tv_name
list_persons
et_password
然后用butterknife的插件生成变量会自动将下划线变成驼峰命名

变量命名:以m开头。例mAdapter使用时按一个m全都出来了
方法命名:与其写好名字不如写好注释。= =。

TextView使用官方标准字体


TextView.png
style="@style/TextAppearance.AppCompat.Display4"
style="@style/TextAppearance.AppCompat.Display3"
style="@style/TextAppearance.AppCompat.Display2"
style="@style/TextAppearance.AppCompat.Display1"
style="@style/TextAppearance.AppCompat.Headline"
style="@style/TextAppearance.AppCompat.Title"
style="@style/TextAppearance.AppCompat.Subhead"
style="@style/TextAppearance.AppCompat.Body2"
style="@style/TextAppearance.AppCompat.Body1"
style="@style/TextAppearance.AppCompat.Caption"
style="@style/TextAppearance.AppCompat.Button"
Button使用Material Design标准样式


Button.png
style="@style/Widget.AppCompat.Button"
style="@style/Widget.AppCompat.Button.Borderless"
style="@style/Widget.AppCompat.Button.Borderless.Colored"
style="@style/Widget.AppCompat.Button.Small"
定好网络请求写法。文件存储方式与位置。写好项目所使用的类库框架用法。

文／Jude95（简书作者）
原文链接：http://www.jianshu.com/p/d9e4ddd1c530
著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。