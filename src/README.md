# Java数字时钟应用

这是一个使用Java Swing开发的简单数字时钟应用程序，能够实时显示当前日期和时间。

## 项目概述

该项目实现了一个基本的数字时钟，具有以下特点：
- 实时显示当前日期和时间
- 友好的用户界面（黑色背景，白色文字）
- 自动更新时间（每秒刷新一次）
- 居中显示在屏幕上

## 技术栈

- Java SE
- Swing GUI 工具包

## 项目结构

## 核心功能实现

### 1. 主类结构

`DigitalClock`类继承自`JFrame`，实现了时钟的主要功能：

- 窗口初始化
- 时间显示
- 定时器更新机制

### 2. 时间更新机制

使用Swing的`Timer`类实现每秒自动更新时间：

```java
private void startTimer() {
    Timer timer = new Timer(1000, e -> ltime.setText(getCurrTime()));
    timer.start();
}
```

### 3. 时间格式化

使用`SimpleDateFormat`格式化日期和时间显示：

```java
public String getCurrTime() {
    SimpleDateFormat formatl = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
    String fortime = formatl.format(new Date());
    return "当前日期时间：" + fortime;
}
```

## 运行方法

### 方法一：使用IDE运行

1. 使用您喜欢的Java IDE（如IntelliJ IDEA、Eclipse等）打开项目
2. 找到`DigitalClock`类中的`main`方法
3. 右键点击并选择"Run DigitalClock.main()"

### 方法二：命令行运行

1. 确保已经安装Java开发环境（JDK）
2. 编译源代码：
   ```bash
   javac -d out src/DigitalClock.java
   ```
3. 运行程序：
   ```bash
   java -cp out DigitalClock
   ```

## 代码亮点

1. **使用Lambda表达式**：简化了事件监听器的编写
2. **Swing单线程模型**：通过`SwingUtilities.invokeLater()`确保UI组件在事件调度线程中创建
3. **模块化设计**：功能分离为不同的方法，提高代码可读性和维护性

## 扩展建议

如果您想进一步增强这个时钟应用，可以考虑：
- 添加时区切换功能
- 实现多种时间格式显示选项
- 添加闹钟功能
- 自定义主题和字体样式
- 添加秒钟动画效果

## 许可证

此项目为学习目的创建，可自由使用和修改。

## 开发环境

- JDK 8或更高版本
- 任何支持Java的IDE

