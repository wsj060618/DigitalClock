import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DigitalClock extends JFrame {
    JLabel ltime;
    public DigitalClock() {
        InitFrame();
        startTimer();
    }
    // 初始化
    public void InitFrame() {
        this.getContentPane().setBackground(Color.BLACK); // 设置背景为黑色；
        this.setTitle("数字时钟");
        setSize(700,500);
        setLocationRelativeTo(null); // 屏幕居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置退出方式
        // 初始化标签
        ltime = new JLabel(getCurrTime());
        ltime.setHorizontalAlignment(SwingConstants.CENTER);
        ltime.setFont(new Font("宋体", Font.BOLD,30));
        ltime.setForeground(Color.white);
        add(ltime,BorderLayout.CENTER);

        setVisible(true);
    }
    public String getCurrTime() {
        SimpleDateFormat formatl = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        String fortime = formatl.format(new Date());
        return "当前日期时间：" + fortime;
    }

    // 启动定时器
    private void startTimer() {
        Timer timer = new Timer(1000, e -> ltime.setText(getCurrTime()));
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DigitalClock digitalClock = new DigitalClock();
        });
    }
}
