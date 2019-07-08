
## MagicaSakura-x

MagicaSakura-x是一个Android多主题库，支持每日丰富多彩的主题和夜晚主题。

##功能

> 1。支持每日丰富多彩的主题和夜晚主题。

> 2。切换不同的主题而不重新创建活动。

> 3。提供TintXXX小部件，使多主题更加方便快捷。

> 4。只需写入drawable.xml或layout.xml即可自动适应不同的主题样式。

> 5。提供可与4.0.3或更高版本一起使用的Android系统的向后兼容版本。

> 6。使用appcompat-v7支持Vector Drawable。

> 7。易于集成到您的应用程序。

## Demo

![ScreenShot.gif](magicasakura.gif)
 
 You can download the lastest sample apk from Google Play.

<a href="https://play.google.com/store/apps/details?id=com.bilibili.magicasakurademo"><img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" data-canonical-src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" style="max-width:100%;"></a>
 

## Gradle Dependency
```gradle
allprojects {
    repositories {
        maven { url 'http://192.168.1.210/artifactory/mob-release' }
    }
}  
```


其次添加依赖  
```gradle
implementation "cn.lonsun.android:magicasakura-x:$magicasakura_version"

```

$lsrefresh_version具体版本请去192.168.1.210中查看  

## 用法
 
  
 - **STEP1** : 
 
在values / color.xml中定义应用程序全局主题颜色变量，如：
 ```xml
 <color name="theme_color_primary">#fb7299</color>
 <color name="theme_color_primary_dark">#b85671</color>
 <color name="theme_color_primary_trans">#99f0486c</color>
 ```
 当这些xml文件需要自动适应不同的主题样式时，必须在布局xml，color xml或drawable xml中使用这些颜色变量。
 如果使用直接颜色值或其他颜色变量，则调整不同的主题样式将失去工作。
 
 - **STEP2** :
 
 在app Applaction中实现ThemeUtils.switchColor接口;
 您可以定义自己的规则，并结合颜色变量（在步骤1中定义），以便在选择不同的主题时切换不同的颜色。
 ```java
 public class MyApplication extends Application implements ThemeUtils.switchColor {
      @Override
      public void onCreate() {
          super.onCreate();
          //init
          ThemeUtils.setSwitchColor(this);
      }
      
      @Override
      public int replaceColorById(Context context, @ColorRes int colorId) {
        ...
        if(ThemeHelper.getThemeId(context) == "blue"){
            switch (colorId) {
              // define in Step 1
              case R.color.theme_color_primary:
                return R.color.blue;
                ...
            }
        ...
      }

      @Override
      public int replaceColor(Context context, @ColorInt int originColor) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return originColor;
        }
        ...
      }
 }
 ```
 
 - **STEP3** :
 
 该库提供了一系列TintXXX小部件，其中包括最常见的Android小部件。
 
 当您的应用中的某个位置需要适配多主题时，您可以使用这些TintXXX小部件**结合颜色变量（在步骤1中定义）或颜色xml（使用颜色变量）或可绘制的xml（颜色变量）** ，然后他们将自动适应。

  - Drawable Xml
    TintXXX widgets support common drawable xml tag, such as <selector/>, <item/>, <shape/> , <layerlist/>, <color/> and etc in drawable xml.
  
    (Note: when using not supporting drawable xml tag, just can't be adapted to multi theme)
  
    Specially, TintXXX widgets additional support directly tint drawable with app:drawableTint and app:drawableTintMode and set color alpha with android:alpha.
  
    Here is an example:
    
    ``` java
    //drawable xml
    //tint directly
    <selector
      xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">
      <item android:drawable="@drawable/icon " android:state_pressed="true" app:drawableTint="@color/theme_color_primary" />
      <item android:drawable="@drawable/icon" app:drawableTint="@color/gray_dark" />
    </selector>
    
    ```

    ```java
    // set color alpha in color
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item android:state_enabled="true" android:state_pressed="true">
            <shape>
              <corners android:radius="4dp" />
              <solid android:color="@color/theme_color_primary_dark" />
            </shape>
        </item>
        <item android:state_enabled="true">
            <shape>
              <solid android:color="@color/theme_color_primary" />
              <corners android:radius="4dp" />
            </shape>
        </item>
        <item android:state_enabled="false">
            <shape>
              <solid android:alpha="0.3" android:color="@color/theme_color_primary" />
              <corners android:radius="4dp" />
            </shape>
        </item>
    </selector>
    ```
 
  - Layout Xml
    TintXXX小部件可以直接在布局xml中着色，支持最常见的android drawable attrs，例如background，src，drawableLeft，button等。
    （注意：当直接在布局xml中着色时，必须使用颜色变量（在步骤1中定义），否则调整不同的主题样式将失去工作）
    
    Here is an example:
    
    ```java
    // shape_lock.xml
    <shape xmlns:android="http://schemas.android.com/apk/res/android"
        android:shape="rectangle">
        <size android:width="1dp" />
        <solid android:color="@color/theme_color_primary" />
        <stroke android:color="@color/gray_dark" />
    </shape>
    
    // TintTextView
    // The selector_lock and selector_text is a ColorStateList
    // The shape_lock is a shape drawable.
    <com.bilibili.magicasakura.widgets.TintTextView
	     xmlns:app="http://schemas.android.com/apk/res-auto"
	     android:layout_width="wrap_content"
	     android:layout_height="wrap_content"
	     android:drawablePadding="@dimen/padding_half"
	     android:drawableRight="@drawable/selector_lock"
	     android:drwableLeft="@drawable/shape_lock"
	     android:text="@string/textview_title"
	     android:textColor="@color/selector_text"
	     android:textSize="19sp" 
	     app:drawableRightTint="@color/selector_lock" 
	     app:drawableRightTintMode="src_in"/>
    ```
    
    Here is the table that supporting attr of TintXXX widgets:
    
    | attr     | tint | tintMode   |
    | :------- | ----: | :---: |
    | background | backgroundTint |  backgroundTintMode    |
    | src    | imageTint   |  imageTintMode   |
    | button     | compoundButtonTint    |  compoundButtonTintMode  |
    | drawableXxx     | drawableXxxTint    |  drawableXxxTintMode  |
    | progress     | progressTint,progressIndeterminateTint    |    |
    | track     | trackTint    |  trackTintMode  |
    | thumb     | thumbTint    |  thumbTintMode  |

  - Java code
    TintXXX widgets can also be tinted in java code. The way of tinting drawable is the same as android native methods.
    
    Here is an example:

    ```java
    //the background of tintTextView is a shape selector, we can call method setBackgroundResource to tint the shape.
    tintTextView.setBackgroundResource(R.drawable.selector_shape_lock);
    
    //the src of tintImageView is a selector containing the png，we need call method setImageTintList than the android native method call once more.
    tintImageView.setImageResource(R.drawable.selecor_png_lock);
    tintImageView.setImageTintList(R.color.selector_color_lock);
    ```
    
- **STEP4** :
  
    该库提供实用程序类ThemeUtils以满足某些特殊需求或您自己的自定义小部件。
    
  实用程序类ThemeUtils主要提供着色drawable的方法，并将当前主题包括colorStateList和color转换为颜色变量（在步骤1中定义）。
    
    ```java
    // R.color.selector_color.lock is a colorStateList, the method of ThemeUtils.getThemeColorStateList return the colorStateList with the current theme.
    ThemeUtils.getThemeColorStateList(context, R.color.selector_color.lock);
    ThemeUtils.getThemeColorStateList(context, context.getResource().getColorStateList(R.color.selector_color.lock));
    ```
- **STEP5** :  

  - 构建夜间资源目录，它们对应于默认资源目录，并将独立的夜间xml放入相应的目录中，例如values-night / values，color-night / night ...
  
  - 定义一系列颜色变量，包括值相同但值不同的值 - 夜晚/值，然后使用颜色变量编写一次xml以适应夜间主题。  
 
  ```java
  // in value/color.xml
  <color name="theme_color_primary">#2EA200</color>
  <color name="theme_color_primary_dark">#057748</color>
  <color name="theme_color_primary_trans">#992EA200</color>
  <color name="theme_color_secondary">#2EA200</color> // special used for night theme primary color

  // in values-night/color.xml
  <color name="theme_color_primary">#2d2d2d</color>
  <color name="theme_color_primary_dark">#242424</color>
  <color name="theme_color_primary_trans">#992d2d2d</color>
  <color name="theme_color_secondary">#057748</color> // special used for night theme primary color
  ```
   
- **STEP6** :
  
  即将切换每日丰富多彩的主题，您可以直接调用主线程中的Theme.refreshUI方法，此方法还提供可选的回调参数，以满足您在切换主题期间的自定义需求。
  
  即将切换夜间主题，当你的android支持库的版本低于23.2.0时，你可以调用ThemeUtils.updateNightMode的方法来切换夜晚和日常主题，当版本高于23.2.0时，你可以在android支持库中使用android native方法。
