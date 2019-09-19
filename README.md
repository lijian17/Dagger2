# 前言
Dagger2是现在非常火的一个依赖注入框架，目前由Google维护，在Github上面已经有12K star了。Dagger2的入门门槛其实是比较高的，据了解，目前有很多Android工程师对Dagger2还不甚了解，没有用上Dagger2或者是用法有问题，本文的主旨就是让Android工程师快速掌握Dagger2并且优雅简洁地使用Dagger2。这里为大家奉上一份Dagger2 在Android上的最佳实践教程。

> 注意： Dagger2框架的上手难度是比一般的框架更难一些的，所以在练习的时候应该尽量减少干扰因素，尽量少引入其它复杂的第三方库，最佳做法是只依赖Android基础库和Dagger2 For Android需要的库。

# 依赖注入
## 什么是依赖注入？
维基百科上面的介绍是：在软件工程中，依赖注入是种实现控制反转用于解决依赖性设计模式。一个依赖关系指的是可被利用的一种对象（即服务提供端） 。依赖注入是将所依赖的传递给将使用的从属对象（即客户端）。该服务是将会变成客户端的状态的一部分。 传递服务给客户端，而非允许客户端来建立或寻找服务，是本设计模式的基本要求。

简单来说依赖注入就是将实例对象传入到另一个对象中去。

## 依赖注入的实现
维基百科的说法非常抽象，其实在平常编码中，我们一直都在使用以来注入。依赖注入主要有以下几种方式。

- 构造函数注入
```java
public class Chef{
    Menu menu;
    public Man(Menu menu){
        this.menu = menu;
    }
}
```

- setter方法注入
```java
public class Chef{
    Menu menu;
    public setMenu(Menu menu){
        this.menu = menu;
    }
}
```


- 接口注入
```java
public interface MenuInject{
    void injectMenu(Menu menu);
}

public class Chef implements MenuInject{
    Menu menu;
    
    @Override
    public injectMenu(Menu menu){
        this.menu = menu;
    }
}
```

- 依赖注入框架
```java
public @Inject class Menu{
    ...
}

public class Chef{
    @Inject
    Menu menu;
}
```
从上面的例子可以看出，依赖注入其实就是我们天天都在用的东西。

# Dagger2实现依赖注入
## 为什么要使用Dagger2？
从上面这个简单的例子来看，为了实现依赖注入，好像没必要引入第三方的框架。在只有一个人开发，并且业务像上面这么简单的时候，确实是没必要引入Dagger2。但是如果多人同时开发，并且业务非常复杂呢？例如，我们这里的Menu需要初始化，而菜单也要依赖具体的菜式的呢？如果只是一个地方用到的话，还是能接受的。如果项目中有很多地方同时用到呢？如果这个菜单要修改呢？有经验的开发者可能会想到使用单例模式。但是如果项目中有很多类型的结构的话，那么我们就需要管理非常多的单例，并且单例可能也需要依赖其它对象。在这种情况下如果有变更需求或者是更换维护人员，都会使简单的改动变得非常繁琐，并且容易导致各种各样的奇怪BUG。所以这里我们就需要引入第三方的依赖注入工具，让这个工具来帮助我们实现依赖注入。

Dagger2就是我们需要的第三方依赖注入工具。Dagger2较其它依赖注入工具有一个优势，就是它是采用静态编译的方式编译代码的，会在编译期生成好辅助代码，不会影响运行时性能，这一点非常适合用于移动端。

## Dagger2的使用方式
Dagger是通过Component来确认需求与依赖对象的，可以说Component是他们之间的纽带。如果各位用过Dagger2或者了解过Dagger2的教程的话，那么一定知道，Dagger2的使用方式是十分繁琐的，每个需要用到依赖注入的地方都需要通过编写DaggerxxxComponent的模版代码来实现依赖注入。要写非常多的模版代码，大大增加了系统的复杂度。笔者在使用Dagger 2.17的时候，发现Google对Dagger 2进行了优化，现在使用Dagger实现依赖注入要写的代码其实非常少，并且复杂度已经有了很大程度的降低了。在这里，笔者就不介绍旧的使用方式了，使用过Dagger2的同学可以对比这两种方式的差异，没有使用过的直接学习新的使用方式就可以了。

Dagger2最简单的使用方式就是下面这种：

```java
public class A{
    @Inject
    public A(){
        
    }
}

public class B{
    @Inject A a;
    ...
}
```
这种方法是最简单的，没什么难度。但是在实际的项目中我们会遇到各种各样的复杂情况，例如，A还需要依赖其它的类，并且这个类是第三方类库中提供的。又或者A实现了C接口，我们在编码的时候需要使用依赖导致原则来加强我们的代码的可维护性等等。这个时候，用上面这种方法是没办法实现这些需求的，我们使用Dagger2的主要难点也是因为上面这些原因导致的。

还是用上面的例子来解释，假设需要做一个餐饮系统，需要把点好的菜单发给厨师，让厨师负责做菜。现在我们来尝试下用Dagger2来实现这个需求。

首先，我们需要引入Dagger For Android的一些列依赖库：

```
implementation 'com.google.dagger:dagger-android:2.17'
implementation 'com.google.dagger:dagger-android-support:2.17' // if you use the support libraries
implementation 'com.google.dagger:dagger:2.17'
annotationProcessor 'com.google.dagger:dagger-compiler:2.17'
annotationProcessor 'com.google.dagger:dagger-android-processor:2.17'
```

然后我们实现Chef类和Menu类

**Cooking接口**
```java
public interface Cooking{
    String cook();
}
```

**Chef**
```java
public class Chef implements Cooking{

    Menu menu;

    @Inject
    public Chef(Menu menu){
        this.menu = menu;
    }

    @Override
    public String cook(){
        //key菜名， value是否烹饪
        Map<String,Boolean> menuList = menu.getMenus();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Boolean> entry : menuList.entrySet()){
            if (entry.getValue()){
                sb.append(entry.getKey()).append(",");
            }
        }

        return sb.toString();
    }
}
```

Menu
```java
public class Menu {

    public Map<String,Boolean> menus;

    @Inject
    public Menu( Map<String,Boolean> menus){
         this.menus = menus;
    }
    
    Map<String,Boolean> getMenus(){
        return menus;
    }

}
```

现在我们写一个Activity，作用是在onCreate方法中使用Chef对象实现cooking操作。我们先来看看不使用Dagger2和使用Dagger2的代码区别。

**MainActivity**
```java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Map<String, Boolean> menus = new LinkedHashMap<>();
        menus.put("酸菜鱼", true);
        menus.put("土豆丝", true);
        menus.put("铁板牛肉", true);
        Menu menu = new Menu(menus);
        Chef chef = new Chef(menu);
        System.out.println(chef.cook());
    }
}
```

**DaggerMainActivity**
```java
public class DaggerMainActivity extends DaggerActivity {
    @Inject
    Chef chef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,chef.cook());
    }
}
```

可以看到，在使用Dagger2的时候，使用者的代码会变得非常简洁。但是，Dagger 2还需要一些列的辅助代码来实现依赖注入的。如果用过Dagger2就知道要实现依赖注入的话，需要写十分多模版代码。那么我们可不可以用更简单的方式使用Dagger2呢？今天笔者就来介绍一下在Android上使用Dagger2的更简洁的方案。

我们先来看看在DaggerMainActivity上实现依赖注入还需要哪些代码。

**CookModules**
```java
@Module
public class CookModules {

    @Singleton
    @Provides
    public Map<String, Boolean> providerMenus(){
        Map<String, Boolean> menus = new LinkedHashMap<>();
        menus.put("酸菜鱼", true);
        menus.put("土豆丝", true);
        menus.put("铁板牛肉", true);
        return menus;
    }
}
```

**ActivityModules**
```java
@Module
abstract class ActivityModules {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}
```

**CookAppComponent**
```java
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityModules.class,
        CookModules.class})
public interface CookAppComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication>{}

}
```

**MyApplication**
```java
public class MyApplication extends DaggerApplication{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerCookAppComponent.builder().create(this);
    }
}
```

## Dagger2 For Android 使用要点分析
1. CookModules
CookModule很简单，它的目的就是通过@Providers注解提供Menu对象需要的数据。因为Menu是需要依赖一个Map对象的，所以我们通过CookModules给它构造一个Map对象，并自动把它注入到Menu实例里面。
2. ActivityModules
ActivityModules的主要作用就是通过@ContributesAndroidInjector来标记哪个类需要使用依赖注入功能，这里标记的是ManActivity，所以MainActivity能通过@Inject注解来注入Chef对象。
3. CookAppComponent
CookAppComponent相当于一个注射器，我们前面定义的Modules就是被注射的类，使用@Inject注入对象的地方就是接收者类。
4. MyApplication
MyAppliction的特点是继承了DaggerAppliction类，并且在applicationInjector方法中构建了一个DaggerCookAppComponent注射器。
这就是Dagger 2在Android中的使用方案了，在这里我们可以看到，接收这类（MainActivity）中的代码非常简单，实现依赖注入只使用了：

```
@Inject
Chef chef;
```

在接收类里面完全没有多余的代码，如果我们要拓展可以SecondsActivity的话，在SecondsActivity我们要用到Menu类。

那么我们只需要在ActivityModules中增加：

```
@ContributesAndroidInjector
abstract SecondsActivity contributeSecondsActivity();
```

然后在SecondsActivity注入Menu：

```
@Inject
Menu menu;
```

可以看到，对于整个工程来说，实现使用Dagger2 For Android实现依赖注入要写的模版代码其实非常少，非常简洁。只需要进行一次配置就可以，不需要频繁写一堆模版代码。总的来说，Dagger2造成模版代码增加这个问题已经解决了。

## Dagger2的优势
在这里我们总结下使用Dagger2带来的优点。

1. 减少代码量，提高工作效率
例如上面的例子中，我们构建一个Chef对象的话，不使用Dagger2的情况下，需要在初始化Chef对象之前进行一堆前置对象（Menu、Map）的初始化，并且需要手工注入到对应的实例中。你想像下，如果我们再加一个Restaurant( 餐馆 )对象，并且需要把Chef注入到Restaurant中的话，那么初始化Restaurant对象时，需要的前置步骤就更繁琐了。
可能有人会觉得，这也没什么啊，我不介意手工初始化。但是如果你的系统中有N处需要初始化Restaurant对象的地方呢？使用Dagger2 的话，只需要用注解注入就可以了。
2. 自动处理依赖关系
使用Dagger2的时候，我们不需要指定对象的依赖关系，Dagger2会自动帮我们处理依赖关系（例如Chef需要依赖Menu，Menu需要依赖Map，Dagger自动处理了这个依赖关系）。
3. 采用静态编译，不影响运行效率
因为Dagger2是在编译期处理依赖注入的，所以不会影响运行效率在一定的程度上还能提高系统的运行效率（例如采用Dagger2实现单例，不用加锁效率更高）。
4. 提高多人编程效率
在多人协作的时候，一个人用Dagger2边写完代码后，其它所有组员都能通过@Inject注解直接注入常用的对象。加快编程效率，并且能大大增加代码的复用性。
上面我们介绍完了Dagger2 For Android的基本用法了。可能有些读者意犹未尽，觉得这个例子太简单了。那么我们来尝试下构建一个更加复杂的系统，深度体验下Dagger2 For Android的优势。现在我们在上面这个例子的基础上拓展下，尝试开发一个简单的点餐Demo来深度体验下。

