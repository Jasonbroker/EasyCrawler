package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("网站信息提取系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane.
        this.addComponentsToPane(frame.getContentPane());
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void addComponentsToPane(Container pane) {
        //Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        //Dimension size = defaultToolkit.getScreenSize();
//        int height = (int)(size.height *0.7);
//        this.setSize(height>>1, height);

        pane.setLayout(new GridBagLayout());//设置布局控制器
        GridBagConstraints c = new GridBagConstraints();

        this.setTitle("网站信息提取系统");//设置窗口标题

        menuBar = this.getMenu();
        this.setJMenuBar(menuBar);//添加菜单

        JLabel modeLabel = new JLabel();
        modeLabel.setText("请选择模式");
        Font font=new Font("宋体",Font.PLAIN,14);
        modeLabel.setFont(font);
        modeLabel.setToolTipText("JLabel");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(modeLabel, c);

        box = new JComboBox();
        box.addItem("超级邮箱检索器");
        box.addItem("网站关键词杀手");
        box.addActionListener(new comboxListener());//为下拉列表框添加监听器
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(10,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(box, c);

        JLabel enterLabel = new JLabel();
        enterLabel.setText("请输入需要抓取的起始地址以http开头:");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.insets = new Insets(10,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(enterLabel, c);

        text1 = new JTextArea();
       // text1.setBounds(30, enterLabel.getY() + 30, this.getWidth() - 60, 50);
        text1.setLineWrap(true);
        JScrollPane span = new JScrollPane(text1);
        //pan.setBounds(text1.getBounds());
        //c.ipadx = (int) (height*0.4);
        c.ipady = 70; // make this component tall
        c.weightx = 0.0;
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(span, c);

        JLabel workingModel = new JLabel();
        //workingModel.setBounds(30, span.getHeight() + span.getY() + 20, this.getWidth(), 27);
        workingModel.setText("请选择工作模式:");
        workingModel.setToolTipText("JLabel");
        c.ipady = 0;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(workingModel, c);

        precending = new JRadioButton("precendent", true);
        c.weightx = 0.0;
        //c.insets = new Insets(10,0,0,0);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(precending, c);

        JRadioButton indexing = new JRadioButton("indexing", false);
        c.weightx = 0.0;
        //c.insets = new Insets(10,0,0,0);
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(indexing, c);

        btnGroup = new ButtonGroup();
        btnGroup.add(precending);
        btnGroup.add(indexing);

        indexingSpinner1 = new JSpinner();
        indexingSpinner1.setValue(0);
        indexingSpinner1.setPreferredSize(new Dimension(50,30));
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(indexingSpinner1, c);

        indexingSpinner2 = new JSpinner();
        indexingSpinner2.setPreferredSize(new Dimension(50,30));
        indexingSpinner2.setValue(100);
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(indexingSpinner2, c);

        String disText = "<html><HTML><body style=color:red>请选择抓取速度,量力而行, 数值越大越影响本机速度:</body></html>";
        JLabel speedLabel = new JLabel(disText);
        speedLabel.setToolTipText("JLabel");
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 6;
        pane.add(speedLabel, c);

        spinner = new JSpinner();
        spinner.setBounds(30, speedLabel.getHeight() + speedLabel.getY(), this.getWidth() - 60, 20);
        spinner.setValue(10);
        c.gridx = 2;
        c.gridwidth = 1;
        c.gridy = 6;
        pane.add(spinner, c);

        strictModeBox = new JCheckBox("开启严肃模式");
        strictModeBox.setSelected(true);
        c.gridwidth =  1;
        c.gridx = 2;
        c.gridy = 7;
        pane.add(strictModeBox, c);

        JLabel addSepLabel = new JLabel("添加@替换,逗号分隔");
        c.ipady = 0; // make this component tall
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 8;
        pane.add(addSepLabel, c);

        sepText = new JTextArea();
        sepText.setLineWrap(true);
        JScrollPane sepTextpan = new JScrollPane(sepText);
        c.ipady = 40; // make this component tall
        c.gridwidth = 3;
        c.gridy = 9;
        c.insets = new Insets(10,10,10,10);
        pane.add(sepTextpan, c);

        debugModeBox = new JCheckBox("开启debug模式");
        debugModeBox.setSelected(false);
        c.gridwidth = 1;
        c.ipady = 0;
        c.gridy = 10;
        c.gridx = 2;
        pane.add(debugModeBox, c);

//        debugModeBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (debugModeBox.isSelected()) {
//                    MainWindow.this.setSize(700, 600);
//                }else {
//                    MainWindow.this.setSize(300, 600);
//                }
//            }
//        });

        startButton = new JButton();
        startButton.setText("<html><HTML><body style=color:red>开始</body></html>");
        startButton.setToolTipText("开始");
        c.gridwidth = 3;
        c.ipady = 40;
        c.gridy = 11;
        c.gridx = 0;
        pane.add(startButton, c);
        /*
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
        */

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

