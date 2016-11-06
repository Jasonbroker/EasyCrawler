package com.company;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.tools.tree.ThisExpression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by jason on 04/11/2016.
 */

interface MainWindowListener {

    boolean buttonClicked(boolean on);

}

public class MainWindow extends JFrame {

    private JButton startButton;
    private JTextArea text1;
    private JComboBox box;
    private JMenuBar menuBar;
    private JSlider slider;
    private JSpinner spinner;
    private JToolBar toolBar;

    JSpinner indexingSpinner1;
    JSpinner indexingSpinner2;
    JRadioButton precending;
    ButtonGroup btnGroup;
    JCheckBox strictModeBox;
    JCheckBox debugModeBox;
    private JTextArea sepText;

    private JTextArea _logText;

    public MainWindow() {
        this.setSize(300, 600);
        this.getContentPane().setLayout(null);//设置布局控制器

        this.setTitle("网站信息提取系统");//设置窗口标题

        this.setJMenuBar(this.getMenu());//添加菜单

        JLabel modeLabel = new JLabel();
        modeLabel.setBounds(30, 10, 70, 27);
        modeLabel.setText("请选择模式");
        modeLabel.setToolTipText("JLabel");
        this.add(modeLabel, null);

        box = new JComboBox();
        box.setBounds(100, modeLabel.getY(), 150, 27);

        box.addItem("超级邮箱检索器");
        box.addItem("网站关键词杀手");

        box.addActionListener(new comboxListener());//为下拉列表框添加监听器类
        this.add(box, null);

        JLabel enterLabel = new JLabel();
        enterLabel.setBounds(30, modeLabel.getY() + 50, this.getWidth(), 27);
        enterLabel.setText("请输入需要抓取的起始地址以http开头:");
        enterLabel.setToolTipText("JLabel");
        this.add(enterLabel, null);

        text1 = new JTextArea();
        text1.setBounds(30, enterLabel.getY() + 30, this.getWidth() - 60, 50);
        text1.setLineWrap(true);
        JScrollPane pan = new JScrollPane(text1);
        pan.setBounds(text1.getBounds());
        this.add(pan);

        JLabel workingModel = new JLabel();
        workingModel.setBounds(30, pan.getHeight() + pan.getY() + 20, this.getWidth(), 27);
        workingModel.setText("请选择工作模式:");
        workingModel.setToolTipText("JLabel");
        this.add(workingModel, null);


        precending = new JRadioButton("precendent", true);
//        b1.addActionListener(h1);
        this.add(precending);
        JRadioButton indexing = new JRadioButton("indexing", false);
//        b2.addActionListener(h1);
        this.add(indexing);

        precending.setBounds(30, workingModel.getHeight() + workingModel.getY(), 150, 25);
        indexing.setBounds(30, precending.getHeight() + precending.getY(), 100, 25);

        btnGroup = new ButtonGroup();
        btnGroup.add(precending);
        btnGroup.add(indexing);


        indexingSpinner1 = new JSpinner();
        indexingSpinner1.setBounds(150, indexing.getY() + 5, 50, 20);
        indexingSpinner1.setValue(0);
        this.add(indexingSpinner1, null);

        indexingSpinner2 = new JSpinner();
        indexingSpinner2.setBounds(indexingSpinner1.getX() + 50, indexingSpinner1.getY(), 50, 20);
        indexingSpinner2.setValue(100);
        this.add(indexingSpinner2, null);

        String disText = "<html><HTML><body style=color:red>请选择抓取速度,量力而行, 数值越大越影响本机速度:</body></html>";
        JLabel speedLabel = new JLabel(disText);
        speedLabel.setBounds(30, indexing.getHeight() + indexing.getY() + 10, this.getWidth() - 60, 50);
//        speedLabel.setText("");
        speedLabel.setToolTipText("JLabel");
        this.add(speedLabel, null);

        spinner = new JSpinner();
        spinner.setBounds(30, speedLabel.getHeight() + speedLabel.getY(), this.getWidth() - 60, 20);
        spinner.setValue(10);
        this.add(spinner, null);

        strictModeBox = new JCheckBox("开启严肃模式");
        strictModeBox.setSelected(true);
        strictModeBox.setBounds(30, spinner.getHeight() + spinner.getY(), 150, 20);
        this.add(strictModeBox);

        JLabel addSepLabel = new JLabel("添加@替换,逗号分隔");
        addSepLabel.setBounds(30, strictModeBox.getHeight() + strictModeBox.getY(), 220, 27);
        add(addSepLabel);

        sepText = new JTextArea();
        sepText.setBounds(30, addSepLabel.getY() + 30, this.getWidth() - 60, 50);
        sepText.setLineWrap(true);
        JScrollPane sepTextpan = new JScrollPane(sepText);
        sepTextpan.setBounds(sepText.getBounds());
        this.add(sepTextpan);

        debugModeBox = new JCheckBox("开启debug模式");
        debugModeBox.setSelected(false);
        debugModeBox.setBounds(30, sepText.getHeight() + sepText.getY(), 150, 20);
        this.add(debugModeBox);
        debugModeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (debugModeBox.isSelected()) {
                    MainWindow.this.setSize(700, 600);
                }else {
                    MainWindow.this.setSize(300, 600);
                }
            }
        });

        startButton = new JButton();
        startButton.setBounds(30, this.getHeight() - 60 - 60, this.getWidth() - 60, 60);
        startButton.setText("<html><HTML><body style=color:red>开始</body></html>");
        startButton.setToolTipText("开始");
        this.add(startButton);


        _logText = new JTextArea();
        _logText.setBounds(300, 0, 400, this.getHeight());
        _logText.setLineWrap(true);
        JScrollPane _logTextpan = new JScrollPane(_logText);
        _logTextpan.setBounds(_logText.getBounds());
        _logText.enableInputMethods(false);
        this.add(_logTextpan);

        MyPrintStream myPrintStream = new MyPrintStream(System.out, _logText);
        System.setOut(myPrintStream);
        System.setErr(myPrintStream);

//    this.add(this.getSlider(),null);
//    this.add(this.getSpinner(),null);
    }

    public void addCLickLisener(MainWindowListener listener) {
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               boolean suc = listener.buttonClicked(!startButton.isSelected());
                if (suc)
                start(!startButton.isSelected());
            }
        });
    }

    public void start (boolean start) {
        startButton.setSelected(start);
        if (startButton.isSelected()) {
            startButton.setText("结束");
        } else  {
            startButton.setText("开始");
        }
    }

    public int getFunctionType() {
        return box.getSelectedIndex();
    }

    public String getUrl() {
        return text1.getText().trim();
    }

    public boolean getWorkingMode() {
        return precending.isSelected();
    }

    public int getStartIndex() {
        return Integer.valueOf(indexingSpinner1.getValue().toString());
    }

    public int getEndIndex() {
        return Integer.valueOf(indexingSpinner2.getValue().toString());
    }

    public int getThreadNum() {
        return Integer.valueOf(spinner.getValue().toString());
    }

    public boolean enableStrictMode() {
        return strictModeBox.isSelected();
    }

    public void setSeperator(String string) {
        sepText.setText(string);
    }

    public String getSeperator() {
        return sepText.getText();
    }

    public boolean isEnableDebug() {
        return debugModeBox.isSelected();
    }

    public void  setDebugEnabled(boolean enabled) {
        debugModeBox.setSelected(enabled);
    }

    private JToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.setBounds(103, 260, 71, 20);
            toolBar.setFloatable(true);
        }
        return toolBar;
    }

    private JSpinner getSpinner() {
        if (spinner == null) {
            spinner = new JSpinner();
            spinner.setBounds(103, 220, 80, 20);
            spinner.setValue(100);
        }
        return spinner;
    }

    private JSlider getSlider() {
        if (slider == null) {
            slider = new JSlider();
            slider.setBounds(103, 200, 100, 20);
            slider.setMaximum(100);
            slider.setMinimum(0);
            slider.setOrientation(0);
            slider.setValue(0);
        }
        return slider;
    }

    /**
     * 菜单的级别JMenuBar->JMenu->JMenuItem
     * 三级都是1：n的关系
     * 最后添加菜单用的SetJMenuBar方法
     *
     * @return 建立好的菜单
     */
    private JMenuBar getMenu() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            JMenu m1 = new JMenu();
            m1.setText("文件");
            JMenu m2 = new JMenu();
            m2.setText("编辑");
            JMenu m3 = new JMenu();
            m3.setText("帮助");

            JMenuItem item11 = new JMenuItem();
            item11.setText("打开");
            JMenuItem item12 = new JMenuItem();
            item12.setText("保存");
            JMenuItem item13 = new JMenuItem();
            item13.setText("退出");

            JMenuItem item21 = new JMenuItem();
            item21.setText("复制");
            JMenuItem item22 = new JMenuItem();
            item22.setText("拷贝");
            JMenuItem item23 = new JMenuItem();
            item23.setText("剪切");

            JMenuItem item31 = new JMenuItem();
            item31.setText("欢迎");
            JMenuItem item32 = new JMenuItem();
            item32.setText("搜索");
            JMenuItem item33 = new JMenuItem();
            item33.setText("版本信息");

            m1.add(item11);
            m1.add(item12);
            m1.add(item13);

            m2.add(item21);
            m2.add(item22);
            m2.add(item23);

            m3.add(item31);
            m3.add(item32);
            m3.add(item33);

            menuBar.add(m1);
            menuBar.add(m2);
            menuBar.add(m3);
        }
        return menuBar;
    }

    /**
     * 设置下拉列表框
     *
     * @return
     */
    private JComboBox getBox() {
        if (box == null) {
            box = new JComboBox();
            box.setBounds(103, 140, 150, 27);
            box.addItem("网站关键词杀手");
            box.addItem("超级邮箱检索器");
            box.addActionListener(new comboxListener());//为下拉列表框添加监听器类

        }
        return box;
    }

    private class comboxListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object o = e.getSource();
            System.out.println(o.toString());
        }


        /**
         * 监听器类实现ActionListener接口，主要实现actionPerformed方法
         *
         * @author HZ20232
         */
        private class HelloButton implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello world!");
            }
        }
        /**
         * 设定文本域
         * @return
         */
//    private JTextField getTextField(){
//        if(text1==null){
//            text1 = new JTextField();
//            text1.setBounds(96,49,160,20);
//        }
//        return text1;
//    }
    }
}

