import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DigitalClock extends JFrame {
    private JLabel dateLabel; // 日期标签
    private JLabel timeLabel; // 时间标签
    private JComboBox<String> localeComboBox; // 地区选择下拉框
    private JCheckBox hour12CheckBox; // 12/24 小时制复选框
    private javax.swing.Timer timer; // 定时器

    // 支持的 Locale 列表
    private final Locale[] locales = {Locale.CHINA, Locale.US, Locale.JAPAN, Locale.GERMANY};

    // 日期与时间格式(24小时制)
    private final String[] dateFormats24 = {
            "EEEE, yyyy年MM月dd日", // 中文
            "EEEE, MMMM dd, yyyy",  // 英文（美国）
            "EEEE, yyyy年MM月dd日", // 日文（用中文格式显示）
            "EEEE, dd. MMMM yyyy"   // 德文
    };
    private final String[] timeFormats24 = {
            "HH:mm:ss", "HH:mm:ss", "HH:mm:ss", "HH:mm:ss"
    };

    // 日期与时间格式(12小时制)
    private final String[] dateFormats12 = {
            "EEEE, yyyy年MM月dd日",
            "EEEE, MMMM dd, yyyy",
            "EEEE, yyyy年MM月dd日",
            "EEEE, dd. MMMM yyyy"
    };
    private final String[] timeFormats12 = {
            "hh:mm:ss a", "hh:mm:ss a", "hh:mm:ss a", "hh:mm:ss a"
    };

    // 当前选择的格式与地区（初始为中文、24 小时）
    private Locale currentLocale = Locale.CHINA;
    private String currentDateFormat = dateFormats24[0];
    private String currentTimeFormat = timeFormats24[0];
    private boolean use12Hour = false;
    /**
     * 构造方法: 创建一个 DigitalClock 实例
     * */
    public DigitalClock() {
        InitFrame();
    }
    /**
     * 初始化方法: 配置窗口属性和组件
     * */
    public void InitFrame() {
        // 初始化传口
        setTitle("数字时钟");
        setSize(600, 350);
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭时退出程序
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(15, 15)); // 设置边界布局

        // 初始化中央面板：上面日期，下面时间，采用盒式布局
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // 设置垂直布局

        // 初始化日期标签
        dateLabel = new JLabel(getCurrentDate(), SwingConstants.CENTER); // 日期标签文本居中显示
        dateLabel.setFont(new Font("宋体", Font.PLAIN, 24));
        dateLabel.setForeground(Color.YELLOW);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 日期标签文本居中显示

        // 初始化时间标签
        timeLabel = new JLabel(getCurrentTime(), SwingConstants.CENTER); // 时间标签文本居中显示
        timeLabel.setFont(new Font("宋体", Font.BOLD, 48));
        timeLabel.setForeground(new Color(0, 255, 255));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 时间标签文本居中显示

        // 添加组件到中央面板
        centerPanel.add(Box.createVerticalGlue()); // 添加垂直弹性空间
        centerPanel.add(dateLabel); // 添加日期标签
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8))); // 添加垂直刚性空间
        centerPanel.add(timeLabel); // 添加时间标签
        centerPanel.add(Box.createVerticalGlue()); // 添加垂直弹性空间

        // 添加中央面板到窗口中心
        add(centerPanel, BorderLayout.CENTER);

        // 初始化底部面板：地区下拉框，12小时制复选框，采用流式布局
        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(Color.BLACK); // 设置背景色为黑色
        comboPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // 设置流式布局，居中对齐

        // 初始化地区下拉框
        String[] localeNames = {"中文（中国）", "英文（美国）", "日文（日本）", "德文（德国）"}; // 地区名称数组
        localeComboBox = new JComboBox<>(localeNames); // 创建下拉框，绑定地区名称数组
        localeComboBox.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体
        localeComboBox.setBackground(Color.WHITE);
        localeComboBox.setForeground(Color.BLACK);

        // 选择地区时切换 Locale 与对应格式，并立即刷新显示
        localeComboBox.addActionListener(e -> { // 创建地区下拉框的事件监听器
            int index = localeComboBox.getSelectedIndex(); // 获取选中项索引
            currentLocale = locales[index]; // 更新当前地区为选中项的 Locale
            updateFormatsByIndex(index); // 更新日期和时间格式
            updateLabels(); // 立即刷新显示
        });

        // 初始化12/24小时制复选框
        // 12小时制复选框
        hour12CheckBox = new JCheckBox("12小时制"); // 创建复选框，标签为“12小时制”
        hour12CheckBox.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体
        hour12CheckBox.setBackground(Color.BLACK); // 设置背景色为黑色
        hour12CheckBox.setForeground(Color.WHITE); // 设置前景色为白色
        hour12CheckBox.setFocusPainted(false); // 取消获取焦点时的虚线框

        // 切换 12/24 小时制
        hour12CheckBox.addActionListener(e -> { // 创建12小时制复选框的事件监听器
            use12Hour = hour12CheckBox.isSelected(); // 更新是否使用12小时制的标志
            int index = localeComboBox.getSelectedIndex(); // 获取当前选中地区索引
            updateFormatsByIndex(index); // 更新日期和时间格式
            updateLabels(); // 立即刷新显示
        });

        // 添加组件到底部面板
        comboPanel.add(localeComboBox);
        comboPanel.add(hour12CheckBox);

        // 添加底部面板到窗口底部
        add(comboPanel, BorderLayout.SOUTH);

        // 使用 Timer 每秒更新标签
        timer = new javax.swing.Timer(1000, e -> updateLabels());
        timer.setInitialDelay(0); // 立即触发一次
        timer.start();

        // 窗口关闭时停止 Timer
        addWindowListener(new WindowAdapter() { // 添加窗口监听器，监听窗口关闭事件
            @Override
            public void windowClosing(WindowEvent e) { // 窗口关闭时触发
                if (timer != null && timer.isRunning()) {
                    timer.stop(); // 停止 Timer 任务
                }
            }
        });

        setVisible(true); // 显示窗口
    }

    // 获取当前日期字符串
    private String getCurrentDate() {
        // 使用当前选择的日期格式和地区创建 SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat(currentDateFormat, currentLocale);
        return sdf.format(new Date());
    }
    // 获取当前时间字符串
    private String getCurrentTime() {
        // 使用当前选择的时间格式和地区创建 SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat(currentTimeFormat, currentLocale);
        return sdf.format(new Date());
    }

    // 根据下拉框索引与 use12Hour 标志选择正确的日期/时间格式
    private void updateFormatsByIndex(int index) {
        if (use12Hour) {
            currentDateFormat = dateFormats12[index];
            currentTimeFormat = timeFormats12[index];
        } else {
            currentDateFormat = dateFormats24[index];
            currentTimeFormat = timeFormats24[index];
        }
    }

    // 刷新两个标签的显示内容
    private void updateLabels() {
        dateLabel.setText(getCurrentDate());
        timeLabel.setText(getCurrentTime());
    }
    public static void main(String[] args) {
        // 确保 Swing 组件在事件分发线程（EDT）中创建和更新
        SwingUtilities.invokeLater(DigitalClock::new);
    }
}
