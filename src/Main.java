import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    private JMenuItem miMaxsize,miCut,miCopy,miPaste,miUndo,miRedo,miSelectAll;
    private JPanel p;
    private JLabel bottomInfoLabel;
    private CardLayout card;
    private JTabbedPane tp1, tp2, tp3, tp4;//主面板
    private ImageIcon aboutPNG, helpPNG, errorPNG, successPNG, clockPNG, questPNG, smallReadyPNG, smallWrongPNG,smallSucceedPNG,smallErrorPNG;
    private JTextArea a1_1,a1_2,a2_1,a2_2,a2_3,a2_4,a2_5,a3_1,a3_2,a3_3,a3_4,a3_5,a3_6,a3_7,a4_1,a4_2,a4_3,a4_4,a4_5,a4_6;
    private JTextField t2_5foot,t2_5nx1,t2_5nx2,t2_5nx3,t2_5nx4;
    private Color successColor,errorColor,wrongColor,readyColor;
    private Queue<BottomInfo> infoQueue;
    private RereadyBottomInfoThread rereadyBottomInfoThread;
    private UndoManager undoManager;
    LagrangeInterpolation lagrangeInterpolation;//拉格朗日插值法处理类
    NewtonInterpolation newtonInterpolation;//牛顿差值法处理类
    Euler euler;
    TwoSteps_Euler twoStepsEuler;
    Improved_Euler improvedEuler;
    Classical_Runge_Kutta classicalRungeKutta;
    Adams_Calibration_System adamsCalibrationSystem;
    NormalIteration normalIteration;
    Aitken aitken;
    Newton newton;
    NewtonDownHill newtonDownHill;
    Square square;
    Secant secant;
    FastSecant fastSecant;
    Jacobi jacobi;
    Gauss_Seidel gaussSeidel;
    SOR sor;
    Jordan jordan;
    Gauss gauss;
    MainGauss mainGauss;


    Main(){
        super("计算机数值分析实验可视化");
        initPNG();
        initMenu();
        initServices();
        initMenu();
        initToolBar();
        initMainPanel();
        initBottomInfoPanel();
        initFormSetting();
    }
    //初始化图片的方法
    private void initPNG() {
        String  aboutPNGPath, helpPNGPath, errorPNGPath, successPNGPath, clockPNGPath, questPNGPath,readyPNGPath,wrongPNGPath;
        int smallWidth = 18, smallHeight = 18;
        aboutPNGPath = "src/image/about.png";
        helpPNGPath = "src/image/help.png";
        errorPNGPath = "src/image/error.png";
        successPNGPath = "src/image/success.png";
        clockPNGPath = "src/image/clock.png";
        questPNGPath = "src/image/quest.png";
        readyPNGPath = "src/image/ready.png";
        wrongPNGPath = "src/image/wrong.png";

        //窗口图标
        aboutPNG = new ImageIcon(new ImageIcon(aboutPNGPath).getImage().getScaledInstance(80, 70, Image.SCALE_SMOOTH));
        helpPNG = new ImageIcon(new ImageIcon(helpPNGPath).getImage().getScaledInstance(80, 70, Image.SCALE_SMOOTH));
        errorPNG = new ImageIcon(new ImageIcon(errorPNGPath).getImage().getScaledInstance(75, 70, Image.SCALE_SMOOTH));
        successPNG = new ImageIcon(new ImageIcon(successPNGPath).getImage().getScaledInstance(75, 70, Image.SCALE_SMOOTH));
        clockPNG = new ImageIcon(new ImageIcon(clockPNGPath).getImage().getScaledInstance(75, 55, Image.SCALE_SMOOTH));
        questPNG = new ImageIcon(new ImageIcon(questPNGPath).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

        //信息栏图标
        smallSucceedPNG = new ImageIcon(new ImageIcon(successPNGPath).getImage().getScaledInstance(smallWidth, smallHeight, Image.SCALE_SMOOTH));
        smallErrorPNG = new ImageIcon(new ImageIcon(errorPNGPath).getImage().getScaledInstance(smallWidth, smallHeight, Image.SCALE_SMOOTH));
        smallReadyPNG = new ImageIcon(new ImageIcon(readyPNGPath).getImage().getScaledInstance(smallWidth, smallHeight, Image.SCALE_SMOOTH));
        smallWrongPNG = new ImageIcon(new ImageIcon(wrongPNGPath).getImage().getScaledInstance(smallWidth, smallHeight, Image.SCALE_SMOOTH));
    }

    //初始化业务对象
    private void initServices() {
        successColor = new Color(40,206,49);
        readyColor = new Color(207, 55, 255, 176);
        errorColor = new Color(225, 187, 0);
        wrongColor = new Color(185, 2, 2);
        infoQueue = new LinkedList<>();
        undoManager = new UndoManager();
        lagrangeInterpolation = new LagrangeInterpolation();
        newtonInterpolation = new NewtonInterpolation();
        euler = new Euler();
        twoStepsEuler = new TwoSteps_Euler();
        improvedEuler = new Improved_Euler();
        classicalRungeKutta = new Classical_Runge_Kutta();
        adamsCalibrationSystem = new Adams_Calibration_System();
        normalIteration = new NormalIteration();
        aitken = new Aitken();
        newton = new Newton();
        newtonDownHill = new NewtonDownHill();
        square = new Square();
        secant = new Secant();
        fastSecant = new FastSecant();
        jacobi = new Jacobi();
        gaussSeidel = new Gauss_Seidel();
        sor = new SOR();
        jordan = new Jordan();
        gauss = new Gauss();
        mainGauss = new MainGauss();

        //底部信息更新
        rereadyBottomInfoThread = new RereadyBottomInfoThread();
    }

    // 初始化菜单的方法
    private void initMenu() {
        // 初始化菜单组件
        JMenuBar menuBar = new JMenuBar();
        Main.this.setJMenuBar(menuBar);

        //"操作"菜单卡
        JMenu menuOperate = new JMenu("操作");
        menuBar.add(menuOperate);
        {
            //退出系统
            JMenuItem miExit = new JMenuItem("退出系统...");
            miExit.addActionListener(new ExitAppListener());
            menuOperate.add(miExit);
        }

        //"编辑"菜单卡
        JMenu menuEdit = new JMenu("编辑");
        menuEdit.addMenuListener(new EditMenuListener());
        menuBar.add(menuEdit);
        {
            miUndo = new JMenuItem("撤销");
            miUndo.addActionListener(new UndoListener());

            miRedo = new JMenuItem("重做");
            miRedo.addActionListener(new RedoListener());

            menuEdit.add(miUndo);
            menuEdit.add(miRedo);

            // 添加分割线
            menuEdit.addSeparator();

            miCut = new JMenuItem("剪切");
            miCut.addActionListener(new DefaultEditorKit.CutAction());

            miCopy = new JMenuItem("拷贝");
            miCopy.addActionListener(new DefaultEditorKit.CopyAction());

            miPaste = new JMenuItem("粘贴");
            miPaste.addActionListener(new DefaultEditorKit.PasteAction());

            menuEdit.add(miCut);
            menuEdit.add(miCopy);
            menuEdit.add(miPaste);

            // 添加分割线
            menuEdit.addSeparator();

            miSelectAll = new JMenuItem("全选");
            miSelectAll.addActionListener(new SelectAllListener());
            menuEdit.add(miSelectAll);
        }

        //"窗口"菜单卡
        JMenu menuForm = new JMenu("窗口");
        menuForm.addMenuListener(new FormMenuListener());
        menuBar.add(menuForm);
        {
            miMaxsize = new JMenuItem("最大化");
            miMaxsize.addActionListener(new MaxSizeListener());
            menuForm.add(miMaxsize);

            JMenuItem miMinimize = new JMenuItem("隐藏");
            miMinimize.addActionListener(new MinSizeListener());
            menuForm.add(miMinimize);

            JMenuItem miSmallSize = new JMenuItem("最小尺寸");
            miSmallSize.addActionListener(new SmallSizeListener());
            menuForm.add(miSmallSize);

            JMenuItem miMaxMin = new JMenuItem("缩放");
            miMaxMin.addActionListener(new MaxMinSizeListener());
            menuForm.add(miMaxMin);
        }

        //"帮助"菜单卡
        JMenu menuHelp = new JMenu("帮助");
        menuBar.add(menuHelp);
        {
            JMenuItem miHelp = new JMenuItem("查看帮助...");
            // 注册监听
            miHelp.addActionListener(new HelpListener());
            menuHelp.add(miHelp);

            JMenuItem miAbout = new JMenuItem("关于系统...");
            // 注册监听
            miAbout.addActionListener(new AboutListener());
            menuHelp.add(miAbout);
        }
    }
    private class ExitAppListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 显示确认对话框，当选择YES_OPTION时退出系统
            if (JOptionPane.showConfirmDialog(null, "您确定要退出系统吗？", "退出系统", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, helpPNG) == JOptionPane.YES_OPTION) {
                // 退出系统
                System.exit(0);
            }
        }
    }
    private class EditMenuListener implements MenuListener {
        @Override
        public void menuSelected(MenuEvent e) {
            miUndo.setEnabled(undoManager.canUndo());
            miRedo.setEnabled(undoManager.canRedo());
            boolean isCom = getFocusOwner() instanceof JTextComponent;
            miSelectAll.setEnabled(isCom);
            miCopy.setEnabled(isCom);
            miCut.setEnabled(isCom);
            miPaste.setEnabled(isCom);
        }

        @Override
        public void menuDeselected(MenuEvent e) {
            // 不做
        }

        @Override
        public void menuCanceled(MenuEvent e) {
            // 不做
        }
    }
    private class UndoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
                tempUpdateBottomInfo("撤销了一次",smallSucceedPNG,successColor,1);
            }
        }
    }
    private class RedoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
                tempUpdateBottomInfo("重做了一次",smallSucceedPNG,successColor,1);
            }
        }
    }
    private class SelectAllListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (getFocusOwner() instanceof JTextComponent) {
                JTextComponent focusedComponent = (JTextComponent) getFocusOwner();
                // 执行全选操作
                focusedComponent.selectAll();
            }
        }
    }
    private class FormMenuListener implements MenuListener {
        @Override
        public void menuSelected(MenuEvent e) {
            miMaxsize.setEnabled((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0);
        }

        @Override
        public void menuDeselected(MenuEvent e) {
            // 不做
        }

        @Override
        public void menuCanceled(MenuEvent e) {
            // 不做
        }
    }
    private class MaxSizeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
    private class MinSizeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setExtendedState(JFrame.ICONIFIED);
        }
    }
    private class SmallSizeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Main.this.setSize(550, 400);
        }
    }
    private class MaxMinSizeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int state = getExtendedState();
            if ((state & JFrame.MAXIMIZED_BOTH) == 0) {
                setExtendedState(state | JFrame.MAXIMIZED_BOTH); // 最大化
            } else {
                setExtendedState(state & ~JFrame.MAXIMIZED_BOTH); // 还原
            }
        }
    }
    private class HelpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 显示消息对话框
            JOptionPane.showMessageDialog(null, "本系统实现'计算机数值分析'课程实验的可视化", "帮助", JOptionPane.QUESTION_MESSAGE, helpPNG);
        }
    }
    private class AboutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 显示消息对话框
            JOptionPane.showMessageDialog(null,
                    "计算机数值分析实验可视化系统 2023.12.17 (Test Edition)\n" +
                            "2023年12月18日构建\n" +
                            "Copyright © 2019-2023 Alex_Grandbell\n"+
                    "GitHub开源地址:https://github.com/AlexGrandbell/Numerical_Analysis_GUI", "关于计算机数值分析实验可视化系统",
                    JOptionPane.WARNING_MESSAGE, aboutPNG);
        }
    }

    //初始化工具栏
    private void initToolBar(){
        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);

        JButton b1 = new JButton("插值方法");
        b1.addActionListener(e -> card.show(p, "tp1"));
        toolBar.add(b1);

        toolBar.addSeparator();

        JButton b2 = new JButton("常微分方程初值问题");
        b2.addActionListener(e -> card.show(p, "tp2"));
        toolBar.add(b2);

        toolBar.addSeparator();

        JButton b3 = new JButton("非线性方程求根问题");
        b3.addActionListener(e -> card.show(p, "tp3"));
        toolBar.add(b3);

        toolBar.addSeparator();

        //主面板切换按钮
        JButton b4 = new JButton("线性方程组求根问题");
        b4.addActionListener(e -> card.show(p, "tp4"));
        toolBar.add(b4);
    }

    //主面板初始化
    private void initMainPanel(){
        card = new CardLayout();
        p = new JPanel();
        p.setLayout(card);

        //第一章多项式插值
        tp1 = new JTabbedPane(JTabbedPane.TOP);
        tp1.setForeground(Color.BLACK);
        initLagrangeInterpolationGUI();
        initNewtonInterpolation();

        //第三章常微分方程初值
        tp2 = new JTabbedPane(JTabbedPane.TOP);
        tp2.setForeground(Color.BLACK);
        initEuler();
        initTwoSteps_Euler();
        initImproved_Euler();
        initClassical_Runge_Kutta();
        initAdams_Calibration_System();

        //第四章非线性方程求根
        tp3 = new JTabbedPane(JTabbedPane.TOP);
        tp3.setForeground(Color.BLACK);
        initNormalIteration();
        initAitkenIteration();
        initNewtonIteration();
        initNewtonDownHillIteration();
        initSquareIteration();
        initSecantIteration();
        initFastSecantIteration();

        //第五、六章线性方程组求根
        tp4 = new JTabbedPane(JTabbedPane.TOP);
        tp4.setForeground(Color.BLACK);
        initJacobi();
        initGauss_Seidel();
        initSOR();
        initJordan();
        initGauss();
        initMainGauss();

        // 将选项卡面板添加到卡片面板中
        p.add(tp1, "tp1");
        p.add(tp2, "tp2");
        p.add(tp3, "tp3");
        p.add(tp4, "tp4");

        // 将主面板添加到窗体中
        getContentPane().add(p, BorderLayout.CENTER);
    }
    //1-拉格朗日插值法GUI初始化
    void initLagrangeInterpolationGUI(){
        JPanel p1_1 = new JPanel();
        p1_1.setLayout(new GridBagLayout());
        tp1.add("拉格朗日插值法",p1_1);
        //输入提示与节点与添加按钮
        JPanel p1_1AddInfo = new JPanel();
        p1_1AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p1_1AddInfo.add(new JLabel("请输入插值节点"));
        JTextField t1_1nx = new JTextField(6);
        t1_1nx.getDocument().addUndoableEditListener(undoManager);
        JTextField t1_1ny = new JTextField(6);
        t1_1ny.getDocument().addUndoableEditListener(undoManager);
        p1_1AddInfo.add(new JLabel("x:"));
        p1_1AddInfo.add(t1_1nx);
        p1_1AddInfo.add(new JLabel("y:"));
        p1_1AddInfo.add(t1_1ny);
        JButton b1_1add = new JButton("添加节点");
        b1_1add.addActionListener(event->{
            try {
                double x = Double.parseDouble(t1_1nx.getText());
                double y = Double.parseDouble(t1_1ny.getText());
                lagrangeInterpolation.addNode(x,y);
                t1_1nx.setText("");
                t1_1ny.setText("");
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效坐标",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b1_1delete = new JButton("删除所有节点");
        b1_1delete.setForeground(Color.red);
        b1_1delete.addActionListener(event-> lagrangeInterpolation.deleteAllNode());
        p1_1AddInfo.add(b1_1add);
        p1_1AddInfo.add(b1_1delete);
        //横坐标与计算
        JPanel p1_1x = new JPanel();
        p1_1x.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JTextField t1_1x = new JTextField(6);
        t1_1x.getDocument().addUndoableEditListener(undoManager);
        p1_1x.add(new JLabel("请输入所求节点横坐标:"));
        JButton b1_1cal = new JButton("计算");
        b1_1cal.setForeground(Color.BLUE);
        b1_1cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t1_1x.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                lagrangeInterpolation.calculate(x);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效所求节点横坐标",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        p1_1x.add(t1_1x);
        p1_1x.add(b1_1cal);
        //按钮群
        JPanel p1_1Buttons = new JPanel();
        p1_1Buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton b1_1reset = new JButton("重置输入");
        b1_1reset.addActionListener(event->{
            t1_1nx.setText("");
            t1_1ny.setText("");
            t1_1x.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b1_1clear = new JButton("清空下方显示");
        b1_1clear.setForeground(Color.red);
        b1_1clear.addActionListener(event->{
            a1_1.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b1_1help = new JButton("帮助");
        b1_1help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n拉格朗日插值法是一种用于在已知数据点之间估算函数值的插值方法。它以18世纪法国数学家约瑟夫·拉格朗日的名字命名。\n该方法的基本思想是通过一个多项式来逼近已知的数据点，从而实现对未知点的估算。\n"
                        +"\n您的操作:\n你需要在该界面中不断输入并添加节点，并输入所求节点的横坐标。\n按下计算按钮后程序会根据你给出的节点计算出所求节点的纵坐标结果。\n你可以在必要时删除所有节点。",
                "拉格朗日插值法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p1_1Buttons.add(b1_1reset);
        p1_1Buttons.add(b1_1clear);
        p1_1Buttons.add(b1_1help);
        //显示区域
        JPanel p1_1show = new JPanel(new BorderLayout());
        a1_1 = new JTextArea();
        a1_1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a1_1);
        p1_1show.add(scrollPane);

        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        p1_1.add(p1_1AddInfo, gbc);
        gbc.gridy = 2;
        p1_1.add(p1_1x, gbc);
        gbc.gridy = 3;
        p1_1.add(p1_1Buttons, gbc);

        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p1_1.add(p1_1show, gbc);
    }
    //1-牛顿插值法GUI初始化
    void initNewtonInterpolation(){
        JPanel p1_2 = new JPanel();
        p1_2.setLayout(new GridBagLayout());
        tp1.add("牛顿插值法",p1_2);
        //输入提示与节点与添加按钮
        JPanel p1_2AddInfo = new JPanel();
        p1_2AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p1_2AddInfo.add(new JLabel("请不断输入插值节点"));
        JTextField t1_2nx = new JTextField(6);
        JTextField t1_2ny = new JTextField(6);
        t1_2ny.getDocument().addUndoableEditListener(undoManager);
        t1_2nx.getDocument().addUndoableEditListener(undoManager);
        p1_2AddInfo.add(new JLabel("x:"));
        p1_2AddInfo.add(t1_2nx);
        p1_2AddInfo.add(new JLabel("y:"));
        p1_2AddInfo.add(t1_2ny);
        p1_2AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton b1_2delete = new JButton("删除所有节点");
        b1_2delete.setForeground(Color.red);
        b1_2delete.addActionListener(event-> newtonInterpolation.deleteAllNode());
        p1_2AddInfo.add(b1_2delete);
        //横坐标
        JPanel p1_2x = new JPanel();
        p1_2x.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JTextField t1_2x = new JTextField(6);
        t1_2x.getDocument().addUndoableEditListener(undoManager);
        JButton b1_2add = new JButton("添加节点并计算");
        b1_2add.setForeground(Color.BLUE);
        b1_2add.addActionListener(event->{
            try {
                double nx = Double.parseDouble(t1_2nx.getText());
                double ny = Double.parseDouble(t1_2ny.getText());
                double x = Double.parseDouble(t1_2x.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                newtonInterpolation.addNodeAndCalculate(nx,ny,x);
                t1_2nx.setText("");
                t1_2ny.setText("");
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效坐标与横坐标",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        p1_2x.add(new JLabel("请输入所求节点横坐标:"));
        p1_2x.add(t1_2x);
        p1_2x.add(b1_2add);
        //按钮群
        JPanel p1_2Buttons = new JPanel();
        p1_2Buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton b1_2reset = new JButton("重置输入");
        b1_2reset.addActionListener(event->{
            t1_2nx.setText("");
            t1_2x.setText("");
            t1_2ny.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b1_2clear = new JButton("清空下方显示");
        b1_2clear.setForeground(Color.red);
        b1_2clear.addActionListener(event->{
            a1_2.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b1_2help = new JButton("帮助");
        b1_2help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n牛顿插值法是一种用于在已知数据点之间进行插值的数值方法，其基本思想是通过一个递推的差商公式来构造插值多项式。\n这个方法以英国数学家艾萨克·牛顿的名字命名。\n牛顿插值法的优点之一是具有承袭性，即在插值时可以逐步添加新的数据点而无需重新计算整个插值多项式，这使得在实时计算中效率较高。\n它也避免了拉格朗日插值法中多次的乘法运算，从而减少了计算的复杂性。\n"
                        +"\n您的操作:\n你只需要在该界面中不断输入并添加节点，并输入所求节点的横坐标。\n每次输入新的节点后程序计算出所求节点的新的纵坐标结果。\n你可以在必要时删除所有节点。",
                "牛顿插值法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p1_2Buttons.add(b1_2reset);
        p1_2Buttons.add(b1_2clear);
        p1_2Buttons.add(b1_2help);
        //显示区域
        JPanel p1_2show = new JPanel(new BorderLayout());
        a1_2 = new JTextArea();
        a1_2.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a1_2);
        p1_2show.add(scrollPane);

        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 1;
        p1_2.add(p1_2AddInfo, gbc);
        gbc.gridy = 2;
        p1_2.add(p1_2x, gbc);
        gbc.gridy = 3;
        p1_2.add(p1_2Buttons, gbc);

        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p1_2.add(p1_2show, gbc);
    }
    //2-欧拉格式GUI初始化
    void initEuler(){
        JPanel p2_1 = new JPanel();
        p2_1.setLayout(new GridBagLayout());
        tp2.add("欧拉格式",p2_1);
        //函数提示
        JPanel p2_1Info  = new JPanel();
        p2_1Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_1Info.add(new JLabel("本环节将对常微分方程y'=y-2x/y进行欧拉求值"));
        //输入坐标
        JPanel p2_1AddInfo = new JPanel();
        p2_1AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_1AddInfo.add(new JLabel("请输入初值坐标:"));
        JTextField t2_1nx = new JTextField(6);
        JTextField t2_1ny = new JTextField(6);
        t2_1nx.getDocument().addUndoableEditListener(undoManager);
        t2_1ny.getDocument().addUndoableEditListener(undoManager);
        p2_1AddInfo.add(new JLabel("x:"));
        p2_1AddInfo.add(t2_1nx);
        p2_1AddInfo.add(new JLabel("y:"));
        p2_1AddInfo.add(t2_1ny);
        //输入步长
        JPanel p2_1FootLength = new JPanel();
        p2_1FootLength.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_1FootLength.add(new JLabel("请输入x的步长:"));
        JTextField t2_1foot = new JTextField(6);
        t2_1foot.getDocument().addUndoableEditListener(undoManager);
        p2_1FootLength.add(t2_1foot);
        //输入个数
        JPanel p2_1Num = new JPanel();
        p2_1Num.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_1Num.add(new JLabel("请输入所求节点个数:"));
        JTextField t2_1num = new JTextField(6);
        t2_1foot.getDocument().addUndoableEditListener(undoManager);
        p2_1Num.add(t2_1num);
        //按钮群
        JPanel p2_1Buttons = new JPanel();
        JButton b2_1cal = new JButton("计算");
        b2_1cal.setForeground(Color.BLUE);
        b2_1cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t2_1nx.getText());
                double y = Double.parseDouble(t2_1ny.getText());
                double h = Double.parseDouble(t2_1foot.getText());
                int num = Integer.parseInt(t2_1num.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                euler.calculate(x,y,h,num);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b2_1reset = new JButton("重置输入");
        b2_1reset.addActionListener(event->{
            t2_1nx.setText("");
            t2_1ny.setText("");
            t2_1foot.setText("");
            t2_1num.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b2_1clear = new JButton("清空下方显示");
        b2_1clear.setForeground(Color.red);
        b2_1clear.addActionListener(event->{
            a2_1.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b2_1help = new JButton("帮助");
        b2_1help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n欧拉格式是一种用于求解常微分方程初值问题的数值方法。是一种一阶精度的方法。\n"
                        +"\n您的操作:\n你需要在该界面中输入初值节点、步长以及所求节点个数。\n按下计算按钮后程序会给出所求节点的结果。\n"
                +"\n提示:\n本程序仅对常微分方程y'=y-2x/y进行欧拉求值。",
                "欧拉格式帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p2_1Buttons.add(b2_1cal);
        p2_1Buttons.add(b2_1reset);
        p2_1Buttons.add(b2_1clear);
        p2_1Buttons.add(b2_1help);
        //显示区域
        JPanel p2_1show = new JPanel(new BorderLayout());
        a2_1 = new JTextArea();
        a2_1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a2_1);
        p2_1show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p2_1.add(p2_1Info, gbc);
        gbc.gridy = 1;
        p2_1.add(p2_1AddInfo, gbc);
        gbc.gridy = 2;
        p2_1.add(p2_1FootLength, gbc);
        gbc.gridy = 3;
        p2_1.add(p2_1Num, gbc);
        gbc.gridy = 4;
        p2_1.add(p2_1Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p2_1.add(p2_1show, gbc);
    }
    //2-两步欧拉格式GUI初始化
    void initTwoSteps_Euler(){
        JPanel p2_2 = new JPanel();
        p2_2.setLayout(new GridBagLayout());
        tp2.add("两步欧拉格式",p2_2);
        //函数提示
        JPanel p2_2Info  = new JPanel();
        p2_2Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_2Info.add(new JLabel("本环节将对常微分方程y'=y-2x/y进行两步欧拉求值"));
        //输入坐标1
        JPanel p2_2AddInfo = new JPanel();
        p2_2AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_2AddInfo.add(new JLabel("请输入初值1坐标:"));
        JTextField t2_2nx = new JTextField(6);
        t2_2nx.getDocument().addUndoableEditListener(undoManager);
        JTextField t2_2ny = new JTextField(6);
        t2_2ny.getDocument().addUndoableEditListener(undoManager);
        p2_2AddInfo.add(new JLabel("x:"));
        p2_2AddInfo.add(t2_2nx);
        p2_2AddInfo.add(new JLabel("y:"));
        p2_2AddInfo.add(t2_2ny);
        //输入步长
        JPanel p2_2AddInfo2 = new JPanel();
        p2_2AddInfo2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_2AddInfo2.add(new JLabel("请输入初值2坐标:"));
        JTextField t2_2nx2 = new JTextField(6);
        t2_2nx2.getDocument().addUndoableEditListener(undoManager);
        JTextField t2_2ny2 = new JTextField(6);
        t2_2ny2.getDocument().addUndoableEditListener(undoManager);
        p2_2AddInfo2.add(new JLabel("x:"));
        p2_2AddInfo2.add(t2_2nx2);
        p2_2AddInfo2.add(new JLabel("y:"));
        p2_2AddInfo2.add(t2_2ny2);
        //输入个数
        JPanel p2_2Num = new JPanel();
        p2_2Num.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_2Num.add(new JLabel("请输入所求节点个数:"));
        JTextField t2_2num = new JTextField(6);
        t2_2num.getDocument().addUndoableEditListener(undoManager);
        p2_2Num.add(t2_2num);
        //按钮群
        JPanel p2_2Buttons = new JPanel();
        JButton b2_2cal = new JButton("计算");
        b2_2cal.setForeground(Color.BLUE);
        b2_2cal.addActionListener(event->{
            try {
                double x1 = Double.parseDouble(t2_2nx.getText());
                double y1 = Double.parseDouble(t2_2ny.getText());
                double x2 = Double.parseDouble(t2_2nx2.getText());
                double y2 = Double.parseDouble(t2_2ny2.getText());
                int num = Integer.parseInt(t2_2num.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                twoStepsEuler.calculate(x1,y1,x2,y2,num);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b2_2reset = new JButton("重置输入");
        b2_2reset.addActionListener(event->{
            t2_2nx.setText("");
            t2_2ny.setText("");
            t2_2nx2.setText("");
            t2_2ny2.setText("");
            t2_2num.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b2_2clear = new JButton("清空下方显示");
        b2_2clear.setForeground(Color.red);
        b2_2clear.addActionListener(event->{
            a2_2.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b2_2help = new JButton("帮助");
        b2_2help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n两步欧拉格式基于欧拉格式进行了改进。是一种二阶精度的方法。\n"
                        +"\n您的操作:\n你需要在该界面中输入两个初值节点以及所求节点个数，步长将自动计算。\n按下计算按钮后程序会给出所求节点的结果。\n"
                        +"\n提示:\n本程序仅对常微分方程y'=y-2x/y进行两步欧拉求值。",
                "两步欧拉格式帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p2_2Buttons.add(b2_2cal);
        p2_2Buttons.add(b2_2reset);
        p2_2Buttons.add(b2_2clear);
        p2_2Buttons.add(b2_2help);
        //显示区域
        JPanel p2_2show = new JPanel(new BorderLayout());
        a2_2 = new JTextArea();
        a2_2.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a2_2);
        p2_2show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p2_2.add(p2_2Info, gbc);
        gbc.gridy = 1;
        p2_2.add(p2_2AddInfo, gbc);
        gbc.gridy = 2;
        p2_2.add(p2_2AddInfo2, gbc);
        gbc.gridy = 3;
        p2_2.add(p2_2Num, gbc);
        gbc.gridy = 4;
        p2_2.add(p2_2Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p2_2.add(p2_2show, gbc);
    }
    //2-改进欧拉格式GUI初始化
    void initImproved_Euler(){
        JPanel p2_3 = new JPanel();
        p2_3.setLayout(new GridBagLayout());
        tp2.add("改进欧拉格式",p2_3);
        //函数提示
        JPanel p2_3Info  = new JPanel();
        p2_3Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_3Info.add(new JLabel("本环节将对常微分方程y'=y-2x/y进行改进欧拉求值"));
        //输入坐标
        JPanel p2_3AddInfo = new JPanel();
        p2_3AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_3AddInfo.add(new JLabel("请输入初值坐标:"));
        JTextField t2_3nx = new JTextField(6);
        t2_3nx.getDocument().addUndoableEditListener(undoManager);
        JTextField t2_3ny = new JTextField(6);
        t2_3ny.getDocument().addUndoableEditListener(undoManager);
        p2_3AddInfo.add(new JLabel("x:"));
        p2_3AddInfo.add(t2_3nx);
        p2_3AddInfo.add(new JLabel("y:"));
        p2_3AddInfo.add(t2_3ny);
        //输入步长
        JPanel p2_3FootLength = new JPanel();
        p2_3FootLength.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_3FootLength.add(new JLabel("请输入x的步长:"));
        JTextField t2_3foot = new JTextField(6);
        t2_3foot.getDocument().addUndoableEditListener(undoManager);
        p2_3FootLength.add(t2_3foot);
        //输入个数
        JPanel p2_3Num = new JPanel();
        p2_3Num.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_3Num.add(new JLabel("请输入所求节点个数:"));
        JTextField t2_3num = new JTextField(6);
        t2_3num.getDocument().addUndoableEditListener(undoManager);
        p2_3Num.add(t2_3num);
        //按钮群
        JPanel p2_3Buttons = new JPanel();
        JButton b2_3cal = new JButton("计算");
        b2_3cal.setForeground(Color.BLUE);
        b2_3cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t2_3nx.getText());
                double y = Double.parseDouble(t2_3ny.getText());
                double h = Double.parseDouble(t2_3foot.getText());
                int num = Integer.parseInt(t2_3num.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                improvedEuler.calculate(x,y,h,num);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b2_3reset = new JButton("重置输入");
        b2_3reset.addActionListener(event->{
            t2_3nx.setText("");
            t2_3ny.setText("");
            t2_3foot.setText("");
            t2_3num.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b2_3clear = new JButton("清空下方显示");
        b2_3clear.setForeground(Color.red);
        b2_3clear.addActionListener(event->{
            a2_3.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b2_3help = new JButton("帮助");
        b2_3help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n改进欧拉格式综合了欧拉格式和梯形公式，是一种一步显式格式，明显改善了精度。\n"
                        +"\n您的操作:\n你需要在该界面中输入初值节点、步长以及所求节点个数。\n按下计算按钮后程序会给出所求节点的结果。\n"
                        +"\n提示:\n本程序仅对常微分方程y'=y-2x/y进行改进欧拉求值。",
                "改进欧拉格式帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p2_3Buttons.add(b2_3cal);
        p2_3Buttons.add(b2_3reset);
        p2_3Buttons.add(b2_3clear);
        p2_3Buttons.add(b2_3help);
        //显示区域
        JPanel p2_3show = new JPanel(new BorderLayout());
        a2_3 = new JTextArea();
        a2_3.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a2_3);
        p2_3show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p2_3.add(p2_3Info, gbc);
        gbc.gridy = 1;
        p2_3.add(p2_3AddInfo, gbc);
        gbc.gridy = 2;
        p2_3.add(p2_3FootLength, gbc);
        gbc.gridy = 3;
        p2_3.add(p2_3Num, gbc);
        gbc.gridy = 4;
        p2_3.add(p2_3Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p2_3.add(p2_3show, gbc);
    }
    //2-四阶龙格-库塔经典格式GUI初始化
    void initClassical_Runge_Kutta(){
        JPanel p2_4 = new JPanel();
        p2_4.setLayout(new GridBagLayout());
        tp2.add("四阶龙格-库塔经典格式",p2_4);
        //函数提示
        JPanel p2_4Info  = new JPanel();
        p2_4Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_4Info.add(new JLabel("本环节将对常微分方程y'=y-2x/y进行四阶龙格-库塔经典格式求值"));
        //输入坐标
        JPanel p2_4AddInfo = new JPanel();
        p2_4AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_4AddInfo.add(new JLabel("请输入初值坐标:"));
        JTextField t2_4nx = new JTextField(6);
        t2_4nx.getDocument().addUndoableEditListener(undoManager);
        JTextField t2_4ny = new JTextField(6);
        t2_4ny.getDocument().addUndoableEditListener(undoManager);
        p2_4AddInfo.add(new JLabel("x:"));
        p2_4AddInfo.add(t2_4nx);
        p2_4AddInfo.add(new JLabel("y:"));
        p2_4AddInfo.add(t2_4ny);
        //输入步长
        JPanel p2_4FootLength = new JPanel();
        p2_4FootLength.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_4FootLength.add(new JLabel("请输入x的步长:"));
        JTextField t2_4foot = new JTextField(6);
        t2_4foot.getDocument().addUndoableEditListener(undoManager);
        p2_4FootLength.add(t2_4foot);
        //输入个数
        JPanel p2_4Num = new JPanel();
        p2_4Num.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_4Num.add(new JLabel("请输入所求节点个数:"));
        JTextField t2_4num = new JTextField(6);
        t2_4num.getDocument().addUndoableEditListener(undoManager);
        p2_4Num.add(t2_4num);
        //按钮群
        JPanel p2_4Buttons = new JPanel();
        JButton b2_4cal = new JButton("计算");
        b2_4cal.setForeground(Color.BLUE);
        b2_4cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t2_4nx.getText());
                double y = Double.parseDouble(t2_4ny.getText());
                double h = Double.parseDouble(t2_4foot.getText());
                int num = Integer.parseInt(t2_4num.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                classicalRungeKutta.calculate(x,y,h,num);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b2_4reset = new JButton("重置输入");
        b2_4reset.addActionListener(event->{
            t2_4nx.setText("");
            t2_4ny.setText("");
            t2_4foot.setText("");
            t2_4num.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b2_4clear = new JButton("清空下方显示");
        b2_4clear.setForeground(Color.red);
        b2_4clear.addActionListener(event->{
            a2_4.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b2_4help = new JButton("帮助");
        b2_4help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n四阶龙格-库塔经典格式是一种四阶精度的方法。\n"
                        +"\n您的操作:\n你需要在该界面中输入初值节点、步长以及所求节点个数。\n按下计算按钮后程序会给出所求节点的结果。\n"
                        +"\n提示:\n本程序仅对常微分方程y'=y-2x/y进行四阶龙格-库塔经典格式求值。",
                "四阶龙格-库塔经典格式帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p2_4Buttons.add(b2_4cal);
        p2_4Buttons.add(b2_4reset);
        p2_4Buttons.add(b2_4clear);
        p2_4Buttons.add(b2_4help);
        //显示区域
        JPanel p2_4show = new JPanel(new BorderLayout());
        a2_4 = new JTextArea();
        a2_4.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a2_4);
        p2_4show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p2_4.add(p2_4Info, gbc);
        gbc.gridy = 1;
        p2_4.add(p2_4AddInfo, gbc);
        gbc.gridy = 2;
        p2_4.add(p2_4FootLength, gbc);
        gbc.gridy = 3;
        p2_4.add(p2_4Num, gbc);
        gbc.gridy = 4;
        p2_4.add(p2_4Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p2_4.add(p2_4show, gbc);
    }
    //2-亚当姆斯预报-校正GUI初始化
    void initAdams_Calibration_System(){
        JPanel p2_5 = new JPanel();
        p2_5.setLayout(new GridBagLayout());
        tp2.add("亚当姆斯预报-校正系统",p2_5);
        //函数提示
        JPanel p2_5Info  = new JPanel();
        p2_5Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5Info.add(new JLabel("本环节将对常微分方程y'=y-2x/y进行亚当姆斯预报-校正系统求值"));
        //输入初值坐标
        JPanel p2_5AddInfo1 = new JPanel();
        p2_5AddInfo1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5AddInfo1.add(new JLabel("请输入第1个初值坐标:"));
        t2_5nx1 = new JTextField(6);
        t2_5nx1.getDocument().addUndoableEditListener(undoManager);
        t2_5nx1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRemainingNodesX();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRemainingNodesX();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRemainingNodesX();
            }
        });
        JTextField t2_5ny1 = new JTextField(6);
        t2_5ny1.getDocument().addUndoableEditListener(undoManager);
        p2_5AddInfo1.add(new JLabel("x:"));
        p2_5AddInfo1.add(t2_5nx1);
        p2_5AddInfo1.add(new JLabel("y:"));
        p2_5AddInfo1.add(t2_5ny1);
        //输入步长
        JPanel p2_5FootLength = new JPanel();
        p2_5FootLength.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5FootLength.add(new JLabel("请输入x的步长:"));
        t2_5foot = new JTextField(6);
        t2_5foot.getDocument().addUndoableEditListener(undoManager);
        t2_5foot.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRemainingNodesX();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRemainingNodesX();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRemainingNodesX();
            }
        });
        p2_5FootLength.add(t2_5foot);
        //剩余三个坐标
        JPanel p2_5AddInfo2 = new JPanel();
        p2_5AddInfo2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5AddInfo2.add(new JLabel("请输入第2个初值坐标:"));
        t2_5nx2 = new JTextField(6);
        t2_5nx2.getDocument().addUndoableEditListener(undoManager);
        JTextField t2_5ny2 = new JTextField(6);
        t2_5ny2.getDocument().addUndoableEditListener(undoManager);
        p2_5AddInfo2.add(new JLabel("x:"));
        p2_5AddInfo2.add(t2_5nx2);
        t2_5nx2.setEditable(false);
        p2_5AddInfo2.add(new JLabel("y:"));
        p2_5AddInfo2.add(t2_5ny2);
        JPanel p2_5AddInfo3 = new JPanel();
        p2_5AddInfo3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5AddInfo3.add(new JLabel("请输入第3个初值坐标:"));
        t2_5nx3 = new JTextField(6);
        t2_5nx3.getDocument().addUndoableEditListener(undoManager);
        t2_5nx3.setEditable(false);
        JTextField t2_5ny3 = new JTextField(6);
        t2_5ny3.getDocument().addUndoableEditListener(undoManager);
        p2_5AddInfo3.add(new JLabel("x:"));
        p2_5AddInfo3.add(t2_5nx3);
        p2_5AddInfo3.add(new JLabel("y:"));
        p2_5AddInfo3.add(t2_5ny3);
        JPanel p2_5AddInfo4 = new JPanel();
        p2_5AddInfo4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5AddInfo4.add(new JLabel("请输入第2个初值坐标:"));
        t2_5nx4 = new JTextField(6);
        t2_5nx4.getDocument().addUndoableEditListener(undoManager);
        t2_5nx4.setEditable(false);
        JTextField t2_5ny4 = new JTextField(6);
        t2_5ny4.getDocument().addUndoableEditListener(undoManager);
        p2_5AddInfo4.add(new JLabel("x:"));
        p2_5AddInfo4.add(t2_5nx4);
        p2_5AddInfo4.add(new JLabel("y:"));
        p2_5AddInfo4.add(t2_5ny4);
        //输入个数
        JPanel p2_5Num = new JPanel();
        p2_5Num.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p2_5Num.add(new JLabel("请输入所求节点个数:"));
        JTextField t2_5num = new JTextField(6);
        t2_5num.getDocument().addUndoableEditListener(undoManager);
        p2_5Num.add(t2_5num);
        //按钮群
        JPanel p2_5Buttons = new JPanel();
        JButton b2_5cal = new JButton("计算");
        b2_5cal.setForeground(Color.BLUE);
        b2_5cal.addActionListener(event->{
            try {
                double x1 = Double.parseDouble(t2_5nx1.getText());
                double x2 = Double.parseDouble(t2_5nx2.getText());
                double x3 = Double.parseDouble(t2_5nx3.getText());
                double x4 = Double.parseDouble(t2_5nx4.getText());
                double y1 = Double.parseDouble(t2_5ny1.getText());
                double y2 = Double.parseDouble(t2_5ny2.getText());
                double y3 = Double.parseDouble(t2_5ny3.getText());
                double y4 = Double.parseDouble(t2_5ny4.getText());
                double h = Double.parseDouble(t2_5foot.getText());
                int num = Integer.parseInt(t2_5num.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                adamsCalibrationSystem.calculate(x1,y1,x2,y2,x3,y3,x4,y4,h,num);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b2_5reset = new JButton("重置输入");
        b2_5reset.addActionListener(event->{
            t2_5nx1.setText("");
            t2_5ny1.setText("");
            t2_5nx2.setText("");
            t2_5ny2.setText("");
            t2_5nx3.setText("");
            t2_5ny3.setText("");
            t2_5nx4.setText("");
            t2_5ny4.setText("");
            t2_5foot.setText("");
            t2_5num.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b2_5clear = new JButton("清空下方显示");
        b2_5clear.setForeground(Color.red);
        b2_5clear.addActionListener(event->{
            a2_5.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b2_5help = new JButton("帮助");
        b2_5help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n亚当姆斯预报-校正系统是对四阶龙格-库塔经典格式的改进，利用已有节点作为计算节点。\n"
                        +"\n您的操作:\n你需要在该界面中输入第一个初值节点、步长、剩余三个初值节点的纵坐标以及所求节点个数即可。\n剩余三个坐标的横坐标会根据步长生成。\n按下计算按钮后程序会给出所求节点的结果。\n"
                        +"\n提示:\n本程序仅对常微分方程y'=y-2x/y进行亚当姆斯预报-校正系统求值。",
                "亚当姆斯预报-校正系统帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p2_5Buttons.add(b2_5cal);
        p2_5Buttons.add(b2_5reset);
        p2_5Buttons.add(b2_5clear);
        p2_5Buttons.add(b2_5help);
        //显示区域
        JPanel p2_5show = new JPanel(new BorderLayout());
        a2_5 = new JTextArea();
        a2_5.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a2_5);
        p2_5show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p2_5.add(p2_5Info, gbc);
        gbc.gridy = 1;
        p2_5.add(p2_5AddInfo1, gbc);
        gbc.gridy = 2;
        p2_5.add(p2_5FootLength, gbc);
        gbc.gridy = 3;
        p2_5.add(p2_5AddInfo2, gbc);
        gbc.gridy = 4;
        p2_5.add(p2_5AddInfo3, gbc);
        gbc.gridy = 5;
        p2_5.add(p2_5AddInfo4, gbc);
        gbc.gridy = 6;
        p2_5.add(p2_5Num, gbc);
        gbc.gridy = 7;
        p2_5.add(p2_5Buttons, gbc);
        gbc.gridy = 8;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p2_5.add(p2_5show, gbc);
    }
    // 更新剩余三个节点的x值的方法
    private void updateRemainingNodesX() {
        if (!t2_5foot.getText().isEmpty() && !t2_5nx1.getText().isEmpty()) {
            double step = 0;
            double x1 = 0;
            try {
                step = Double.parseDouble(t2_5foot.getText());
                x1 = Double.parseDouble(t2_5nx1.getText());
                permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入正确的数字",smallErrorPNG,errorColor,2);
            }
            // 根据步长和第一个初值节点的x值计算剩余三个节点的x值
            double x2 = x1 + step;
            double x3 = x1 + 2 * step;
            double x4 = x1 + 3 * step;

            // 使用String.format自动判断小数位数
            String formattedX2 = String.format("%f", x2);
            String formattedX3 = String.format("%f", x3);
            String formattedX4 = String.format("%f", x4);

            // 利用正则表达式去掉末尾的多余零
            formattedX2 = formattedX2.replaceAll("\\.?0*$", "");
            formattedX3 = formattedX3.replaceAll("\\.?0*$", "");
            formattedX4 = formattedX4.replaceAll("\\.?0*$", "");

            // 更新剩余三个节点的x值
            t2_5nx2.setText(formattedX2);
            t2_5nx3.setText(formattedX3);
            t2_5nx4.setText(formattedX4);
        }else {
            t2_5nx2.setText("");
            t2_5nx3.setText("");
            t2_5nx4.setText("");
        }
    }
    //3-普通迭代算法GUI初始化
    void initNormalIteration(){
        JPanel p3_1 = new JPanel();
        p3_1.setLayout(new GridBagLayout());
        tp3.add("普通迭代算法",p3_1);
        //函数提示
        JPanel p3_1Info  = new JPanel();
        p3_1Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_1Info.add(new JLabel("本环节将对(x=e^-x)的根进行普通迭代求解"));
        //输入坐标
        JPanel p3_1AddInfo = new JPanel();
        p3_1AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_1AddInfo.add(new JLabel("请输入初值x:"));
        JTextField t3_1nx = new JTextField(6);
        t3_1nx.getDocument().addUndoableEditListener(undoManager);
        p3_1AddInfo.add(t3_1nx);
        //输入精度
        JPanel p3_1Epsilon = new JPanel();
        p3_1Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_1Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_1ne  = new JTextField(6);
        t3_1ne.getDocument().addUndoableEditListener(undoManager);
        t3_1ne.setText("0.00001");
        p3_1Epsilon.add(t3_1ne);
        //输入最多次数
        JPanel p3_1N = new JPanel();
        p3_1N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_1N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_1nN  = new JTextField(6);
        t3_1nN.getDocument().addUndoableEditListener(undoManager);
        t3_1nN.setText("100");
        p3_1N.add(t3_1nN);
        //按钮群
        JPanel p3_1Buttons = new JPanel();
        JButton b3_1cal = new JButton("计算");
        b3_1cal.setForeground(Color.BLUE);
        b3_1cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t3_1nx.getText());
                double e = Double.parseDouble(t3_1ne.getText());
                double N = Double.parseDouble(t3_1nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                normalIteration.calculate(x,e,N);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_1reset = new JButton("重置输入");
        b3_1reset.addActionListener(event->{
            t3_1nx.setText("");
            t3_1ne.setText("");
            t3_1nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_1clear = new JButton("清空下方显示");
        b3_1clear.setForeground(Color.red);
        b3_1clear.addActionListener(event->{
            a3_1.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_1help = new JButton("帮助");
        b3_1help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n普通迭代算法是一种迭代求非线性方程解的算法。\n"
                        +"\n您的操作:\n你需要在该界面中输入初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对非线性方程(x=e^-x)的根进行普通迭代求解。",
                "普通迭代算法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_1Buttons.add(b3_1cal);
        p3_1Buttons.add(b3_1reset);
        p3_1Buttons.add(b3_1clear);
        p3_1Buttons.add(b3_1help);
        //显示区域
        JPanel p3_1show = new JPanel(new BorderLayout());
        a3_1 = new JTextArea();
        a3_1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_1);
        p3_1show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_1.add(p3_1Info, gbc);
        gbc.gridy = 1;
        p3_1.add(p3_1AddInfo, gbc);
        gbc.gridy = 2;
        p3_1.add(p3_1Epsilon, gbc);
        gbc.gridy = 3;
        p3_1.add(p3_1N, gbc);
        gbc.gridy = 4;
        p3_1.add(p3_1Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_1.add(p3_1show, gbc);
    }
    //3-埃特金算法GUI初始化
    void initAitkenIteration(){
        JPanel p3_2 = new JPanel();
        p3_2.setLayout(new GridBagLayout());
        tp3.add("埃特金算法",p3_2);
        //函数提示
        JPanel p3_2Info  = new JPanel();
        p3_2Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_2Info.add(new JLabel("本环节将对(x=e^-x)的根进行埃特金算法求解"));
        //输入坐标
        JPanel p3_2AddInfo = new JPanel();
        p3_2AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_2AddInfo.add(new JLabel("请输入初值x:"));
        JTextField t3_2nx = new JTextField(6);
        t3_2nx.getDocument().addUndoableEditListener(undoManager);
        p3_2AddInfo.add(t3_2nx);
        //输入精度
        JPanel p3_2Epsilon = new JPanel();
        p3_2Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_2Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_2ne  = new JTextField(6);
        t3_2ne.getDocument().addUndoableEditListener(undoManager);
        t3_2ne.setText("0.00001");
        p3_2Epsilon.add(t3_2ne);
        //输入最多次数
        JPanel p3_2N = new JPanel();
        p3_2N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_2N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_2nN  = new JTextField(6);
        t3_2nN.getDocument().addUndoableEditListener(undoManager);
        t3_2nN.setText("100");
        p3_2N.add(t3_2nN);
        //按钮群
        JPanel p3_2Buttons = new JPanel();
        JButton b3_2cal = new JButton("计算");
        b3_2cal.setForeground(Color.BLUE);
        b3_2cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t3_2nx.getText());
                double e = Double.parseDouble(t3_2ne.getText());
                int N = Integer.parseInt(t3_2nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                aitken.calculate(x,e,N);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_2reset = new JButton("重置输入");
        b3_2reset.addActionListener(event->{
            t3_2nx.setText("");
            t3_2ne.setText("");
            t3_2nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_2clear = new JButton("清空下方显示");
        b3_2clear.setForeground(Color.red);
        b3_2clear.addActionListener(event->{
            a3_2.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_2help = new JButton("帮助");
        b3_2help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n埃特金算法是一种迭代求非线性方程解算法的加速。\n"
                        +"\n您的操作:\n你需要在该界面中输入初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对非线性方程(x=e^-x)的根进行埃特金算法求解。",
                "埃特金算法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_2Buttons.add(b3_2cal);
        p3_2Buttons.add(b3_2reset);
        p3_2Buttons.add(b3_2clear);
        p3_2Buttons.add(b3_2help);
        //显示区域
        JPanel p3_2show = new JPanel(new BorderLayout());
        a3_2 = new JTextArea();
        a3_2.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_2);
        p3_2show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_2.add(p3_2Info, gbc);
        gbc.gridy = 1;
        p3_2.add(p3_2AddInfo, gbc);
        gbc.gridy = 2;
        p3_2.add(p3_2Epsilon, gbc);
        gbc.gridy = 3;
        p3_2.add(p3_2N, gbc);
        gbc.gridy = 4;
        p3_2.add(p3_2Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_2.add(p3_2show, gbc);
    }
    //3-牛顿法GUI初始化
    void initNewtonIteration(){
        JPanel p3_3 = new JPanel();
        p3_3.setLayout(new GridBagLayout());
        tp3.add("牛顿法",p3_3);
        //函数提示
        JPanel p3_3Info  = new JPanel();
        p3_3Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_3Info.add(new JLabel("本环节可对两个非线性方程进行使用牛顿法迭代求解"));
        //函数选择
        JPanel p3_3Choice  = new JPanel();
        p3_3Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        String[] options = {"e^x-2=0","x^3-x-1=0"};
        JComboBox<String> cmb3_3 = new JComboBox<>(options);
        p3_3Choice.add(new JLabel("请选择进行迭代的非线性方程:"));
        p3_3Choice.add(cmb3_3);
        //输入坐标
        JPanel p3_3AddInfo = new JPanel();
        p3_3AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_3AddInfo.add(new JLabel("请输入初值x:"));
        JTextField t3_3nx = new JTextField(6);
        t3_3nx.getDocument().addUndoableEditListener(undoManager);
        p3_3AddInfo.add(t3_3nx);
        //输入精度
        JPanel p3_3Epsilon = new JPanel();
        p3_3Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_3Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_3ne  = new JTextField(6);
        t3_3ne.getDocument().addUndoableEditListener(undoManager);
        t3_3ne.setText("0.00001");
        p3_3Epsilon.add(t3_3ne);
        //输入最多次数
        JPanel p3_3N = new JPanel();
        p3_3N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_3N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_3nN  = new JTextField(6);
        t3_3nN.getDocument().addUndoableEditListener(undoManager);
        t3_3nN.setText("100");
        p3_3N.add(t3_3nN);
        //按钮群
        JPanel p3_3Buttons = new JPanel();
        JButton b3_3cal = new JButton("计算");
        b3_3cal.setForeground(Color.BLUE);
        b3_3cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t3_3nx.getText());
                double e = Double.parseDouble(t3_3ne.getText());
                int N = Integer.parseInt(t3_3nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                newton.calculate(x,e,N,cmb3_3.getSelectedIndex());
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_3reset = new JButton("重置输入");
        b3_3reset.addActionListener(event->{
            t3_3nx.setText("");
            t3_3ne.setText("");
            t3_3nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_3clear = new JButton("清空下方显示");
        b3_3clear.setForeground(Color.red);
        b3_3clear.addActionListener(event->{
            a3_3.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_3help = new JButton("帮助");
        b3_3help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n牛顿法是一种迭代求非线性方程对迭代方程有着特殊选择的方法。\n"
                        +"\n您的操作:\n你需要先选择迭代的方程。\n你需要在该界面中输入初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n这里提供了两种方程，分别是e^x-2=0和x^3-x-1=0，以便你验证牛顿法的缺陷。",
                "牛顿法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_3Buttons.add(b3_3cal);
        p3_3Buttons.add(b3_3reset);
        p3_3Buttons.add(b3_3clear);
        p3_3Buttons.add(b3_3help);
        //显示区域
        JPanel p3_3show = new JPanel(new BorderLayout());
        a3_3 = new JTextArea();
        a3_3.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_3);
        p3_3show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_3.add(p3_3Info, gbc);
        gbc.gridy = 1;
        p3_3.add(p3_3Choice, gbc);
        gbc.gridy = 2;
        p3_3.add(p3_3AddInfo, gbc);
        gbc.gridy = 3;
        p3_3.add(p3_3Epsilon, gbc);
        gbc.gridy = 4;
        p3_3.add(p3_3N, gbc);
        gbc.gridy = 5;
        p3_3.add(p3_3Buttons, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_3.add(p3_3show, gbc);
    }
    //3-牛顿下山法GUI初始化
    void initNewtonDownHillIteration(){
        JPanel p3_4 = new JPanel();
        p3_4.setLayout(new GridBagLayout());
        tp3.add("牛顿下山法",p3_4);
        //函数提示
        JPanel p3_4Info  = new JPanel();
        p3_4Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_4Info.add(new JLabel("本环节对(x^3-x-1=0)的根进行牛顿下山法迭代求解"));
        //输入坐标
        JPanel p3_4AddInfo = new JPanel();
        p3_4AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_4AddInfo.add(new JLabel("请输入初值x:"));
        JTextField t3_4nx = new JTextField(6);
        t3_4nx.getDocument().addUndoableEditListener(undoManager);
        p3_4AddInfo.add(t3_4nx);
        //输入精度
        JPanel p3_4Epsilon = new JPanel();
        p3_4Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_4Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_4ne  = new JTextField(6);
        t3_4ne.getDocument().addUndoableEditListener(undoManager);
        t3_4ne.setText("0.00001");
        p3_4Epsilon.add(t3_4ne);
        //输入最多次数
        JPanel p3_4N = new JPanel();
        p3_4N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_4N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_4nN  = new JTextField(6);
        t3_4nN.getDocument().addUndoableEditListener(undoManager);
        t3_4nN.setText("100");
        p3_4N.add(t3_4nN);
        //按钮群
        JPanel p3_4Buttons = new JPanel();
        JButton b3_4cal = new JButton("计算");
        b3_4cal.setForeground(Color.BLUE);
        b3_4cal.addActionListener(event->{
            try {
                double x = Double.parseDouble(t3_4nx.getText());
                double e = Double.parseDouble(t3_4ne.getText());
                int N = Integer.parseInt(t3_4nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                newtonDownHill.calculate(x,e,N);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_4reset = new JButton("重置输入");
        b3_4reset.addActionListener(event->{
            t3_4nx.setText("");
            t3_4ne.setText("");
            t3_4nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_4clear = new JButton("清空下方显示");
        b3_4clear.setForeground(Color.red);
        b3_4clear.addActionListener(event->{
            a3_4.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_4help = new JButton("帮助");
        b3_4help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n牛顿下山法对牛顿法进行了优化，让每一次迭代结果都小于之前的值，使迭代过程不发散。\n"
                        +"\n您的操作:\n你需要在该界面中输入初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对非线性方程(x^3-x-1=0)的根进行牛顿下山法迭代求解。",
                "牛顿下山法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_4Buttons.add(b3_4cal);
        p3_4Buttons.add(b3_4reset);
        p3_4Buttons.add(b3_4clear);
        p3_4Buttons.add(b3_4help);
        //显示区域
        JPanel p3_4show = new JPanel(new BorderLayout());
        a3_4 = new JTextArea();
        a3_4.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_4);
        p3_4show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_4.add(p3_4Info, gbc);
        gbc.gridy = 1;
        p3_4.add(p3_4AddInfo, gbc);
        gbc.gridy = 2;
        p3_4.add(p3_4Epsilon, gbc);
        gbc.gridy = 3;
        p3_4.add(p3_4N, gbc);
        gbc.gridy = 4;
        p3_4.add(p3_4Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_4.add(p3_4show, gbc);
    }
    //3-开方公式GUI初始化
    void initSquareIteration(){
        JPanel p3_5 = new JPanel();
        p3_5.setLayout(new GridBagLayout());
        tp3.add("开方公式",p3_5);
        //函数提示
        JPanel p3_5Info  = new JPanel();
        p3_5Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_5Info.add(new JLabel("本环节对(x^2-c=0(x>0,c>0))的根进行开方公式求解"));
        //输入c
        JPanel p3_5Addc = new JPanel();
        p3_5Addc.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_5Addc.add(new JLabel("请输入参数c:"));
        JTextField t3_5c = new JTextField(6);
        t3_5c.getDocument().addUndoableEditListener(undoManager);
        p3_5Addc.add(t3_5c);
        //输入坐标
        JPanel p3_5AddInfo = new JPanel();
        p3_5AddInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_5AddInfo.add(new JLabel("请输入初值x:"));
        JTextField t3_5nx = new JTextField(6);
        t3_5nx.getDocument().addUndoableEditListener(undoManager);
        p3_5AddInfo.add(t3_5nx);
        //输入精度
        JPanel p3_5Epsilon = new JPanel();
        p3_5Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_5Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_5ne  = new JTextField(6);
        t3_5ne.getDocument().addUndoableEditListener(undoManager);
        t3_5ne.setText("0.00001");
        p3_5Epsilon.add(t3_5ne);
        //输入最多次数
        JPanel p3_5N = new JPanel();
        p3_5N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_5N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_5nN  = new JTextField(6);
        t3_5nN.getDocument().addUndoableEditListener(undoManager);
        t3_5nN.setText("100");
        p3_5N.add(t3_5nN);
        //按钮群
        JPanel p3_5Buttons = new JPanel();
        JButton b3_5cal = new JButton("计算");
        b3_5cal.setForeground(Color.BLUE);
        b3_5cal.addActionListener(event->{
            try {
                double c = Double.parseDouble(t3_5c.getText());
                double x = Double.parseDouble(t3_5nx.getText());
                double e = Double.parseDouble(t3_5ne.getText());
                int N = Integer.parseInt(t3_5nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                square.calculate(c,x,e,N);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_5reset = new JButton("重置输入");
        b3_5reset.addActionListener(event->{
            t3_5c.setText("");
            t3_5nx.setText("");
            t3_5ne.setText("");
            t3_5nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_5clear = new JButton("清空下方显示");
        b3_5clear.setForeground(Color.red);
        b3_5clear.addActionListener(event->{
            a3_5.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_5help = new JButton("帮助");
        b3_5help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n开方公式是针对(x^2-c=0(x>0,c>0))进行迭代的公式。\n"
                        +"\n您的操作:\n你需要在该界面中输入参数c，初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对非线性方程(x^2-c=0(x>0,c>0))的根进行开方公式迭代求解。",
                "开方公式帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_5Buttons.add(b3_5cal);
        p3_5Buttons.add(b3_5reset);
        p3_5Buttons.add(b3_5clear);
        p3_5Buttons.add(b3_5help);
        //显示区域
        JPanel p3_5show = new JPanel(new BorderLayout());
        a3_5 = new JTextArea();
        a3_5.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_5);
        p3_5show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_5.add(p3_5Info, gbc);
        gbc.gridy = 1;
        p3_5.add(p3_5Addc, gbc);
        gbc.gridy = 2;
        p3_5.add(p3_5AddInfo, gbc);
        gbc.gridy = 3;
        p3_5.add(p3_5Epsilon, gbc);
        gbc.gridy = 4;
        p3_5.add(p3_5N, gbc);
        gbc.gridy = 5;
        p3_5.add(p3_5Buttons, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_5.add(p3_5show, gbc);
    }
    //3-弦截法GUI初始化
    void initSecantIteration(){
        JPanel p3_6 = new JPanel();
        p3_6.setLayout(new GridBagLayout());
        tp3.add("弦截法",p3_6);
        //函数提示
        JPanel p3_6Info  = new JPanel();
        p3_6Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_6Info.add(new JLabel("本环节将对(e^x-2=0)的根进行弦截法迭代求解"));
        //输入坐标0
        JPanel p3_6AddInfo1 = new JPanel();
        p3_6AddInfo1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_6AddInfo1.add(new JLabel("请输入初值x0:"));
        JTextField t3_6nx1 = new JTextField(6);
        t3_6nx1.getDocument().addUndoableEditListener(undoManager);
        p3_6AddInfo1.add(t3_6nx1);
        //输入坐标1
        JPanel p3_6AddInfo2 = new JPanel();
        p3_6AddInfo2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_6AddInfo2.add(new JLabel("请输入初值x1:"));
        JTextField t3_6nx2 = new JTextField(6);
        t3_6nx2.getDocument().addUndoableEditListener(undoManager);
        p3_6AddInfo2.add(t3_6nx2);
        //输入精度
        JPanel p3_6Epsilon = new JPanel();
        p3_6Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_6Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_6ne  = new JTextField(6);
        t3_6ne.getDocument().addUndoableEditListener(undoManager);
        t3_6ne.setText("0.00001");
        p3_6Epsilon.add(t3_6ne);
        //输入最多次数
        JPanel p3_6N = new JPanel();
        p3_6N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_6N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_6nN  = new JTextField(6);
        t3_6nN.getDocument().addUndoableEditListener(undoManager);
        t3_6nN.setText("100");
        p3_6N.add(t3_6nN);
        //按钮群
        JPanel p3_6Buttons = new JPanel();
        JButton b3_6cal = new JButton("计算");
        b3_6cal.setForeground(Color.BLUE);
        b3_6cal.addActionListener(event->{
            try {
                double x0 = Double.parseDouble(t3_6nx1.getText());
                double x1 = Double.parseDouble(t3_6nx2.getText());
                double e = Double.parseDouble(t3_6ne.getText());
                int N = Integer.parseInt(t3_6nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                secant.calculate(x0,x1,e,N);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_6reset = new JButton("重置输入");
        b3_6reset.addActionListener(event->{
            t3_6nx1.setText("");
            t3_6nx2.setText("");
            t3_6ne.setText("");
            t3_6nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_6clear = new JButton("清空下方显示");
        b3_6clear.setForeground(Color.red);
        b3_6clear.addActionListener(event->{
            a3_6.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_6help = new JButton("帮助");
        b3_6help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n弦截法是利用初值与最新节点的弦与x轴的交点作为新的迭代值，是线性收敛的。\n"
                        +"\n您的操作:\n你需要在该界面中输入两个初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对非线性方程(e^x-2=0)的根进行弦截法迭代求解，请确保初值的可靠性。",
                "弦截法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_6Buttons.add(b3_6cal);
        p3_6Buttons.add(b3_6reset);
        p3_6Buttons.add(b3_6clear);
        p3_6Buttons.add(b3_6help);
        //显示区域
        JPanel p3_6show = new JPanel(new BorderLayout());
        a3_6 = new JTextArea();
        a3_6.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_6);
        p3_6show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_6.add(p3_6Info, gbc);
        gbc.gridy = 1;
        p3_6.add(p3_6AddInfo1, gbc);
        gbc.gridy = 2;
        p3_6.add(p3_6AddInfo2, gbc);
        gbc.gridy = 3;
        p3_6.add(p3_6Epsilon, gbc);
        gbc.gridy = 4;
        p3_6.add(p3_6N, gbc);
        gbc.gridy = 5;
        p3_6.add(p3_6Buttons, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_6.add(p3_6show, gbc);
    }
    //3-快速弦截法GUI初始化
    void initFastSecantIteration(){
        JPanel p3_7 = new JPanel();
        p3_7.setLayout(new GridBagLayout());
        tp3.add("快速弦截法",p3_7);
        //函数提示
        JPanel p3_7Info  = new JPanel();
        p3_7Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_7Info.add(new JLabel("本环节将对(e^x-2=0)的根进行快速弦截法迭代求解"));
        //输入坐标0
        JPanel p3_7AddInfo1 = new JPanel();
        p3_7AddInfo1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_7AddInfo1.add(new JLabel("请输入初值x0:"));
        JTextField t3_7nx1 = new JTextField(6);
        t3_7nx1.getDocument().addUndoableEditListener(undoManager);
        p3_7AddInfo1.add(t3_7nx1);
        //输入坐标1
        JPanel p3_7AddInfo2 = new JPanel();
        p3_7AddInfo2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_7AddInfo2.add(new JLabel("请输入初值x1:"));
        JTextField t3_7nx2 = new JTextField(6);
        t3_7nx2.getDocument().addUndoableEditListener(undoManager);
        p3_7AddInfo2.add(t3_7nx2);
        //输入精度
        JPanel p3_7Epsilon = new JPanel();
        p3_7Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_7Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t3_7ne  = new JTextField(6);
        t3_7ne.getDocument().addUndoableEditListener(undoManager);
        t3_7ne.setText("0.00001");
        p3_7Epsilon.add(t3_7ne);
        //输入最多次数
        JPanel p3_7N = new JPanel();
        p3_7N.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p3_7N.add(new JLabel("请输入迭代停止次数N:"));
        JTextField t3_7nN  = new JTextField(6);
        t3_7nN.getDocument().addUndoableEditListener(undoManager);
        t3_7nN.setText("100");
        p3_7N.add(t3_7nN);
        //按钮群
        JPanel p3_7Buttons = new JPanel();
        JButton b3_7cal = new JButton("计算");
        b3_7cal.setForeground(Color.BLUE);
        b3_7cal.addActionListener(event->{
            try {
                double x0 = Double.parseDouble(t3_7nx1.getText());
                double x1 = Double.parseDouble(t3_7nx2.getText());
                double e = Double.parseDouble(t3_7ne.getText());
                int N = Integer.parseInt(t3_7nN.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                fastSecant.calculate(x0,x1,e,N);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b3_7reset = new JButton("重置输入");
        b3_7reset.addActionListener(event->{
            t3_7nx1.setText("");
            t3_7nx2.setText("");
            t3_7ne.setText("");
            t3_7nN.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b3_7clear = new JButton("清空下方显示");
        b3_7clear.setForeground(Color.red);
        b3_7clear.addActionListener(event->{
            a3_7.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b3_7help = new JButton("帮助");
        b3_7help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n快速弦截法是在弦截法的基础上利用相邻的两个节点来计算新的节点，提高了收敛速度。\n"
                        +"\n您的操作:\n你需要在该界面中输入两个初值的横坐标x，迭代精度ε以及最多迭代次数N。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对非线性方程(e^x-2=0)的根进行快速弦截法迭代求解，请确保初值的可靠性。",
                "快速弦截法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p3_7Buttons.add(b3_7cal);
        p3_7Buttons.add(b3_7reset);
        p3_7Buttons.add(b3_7clear);
        p3_7Buttons.add(b3_7help);
        //显示区域
        JPanel p3_7show = new JPanel(new BorderLayout());
        a3_7 = new JTextArea();
        a3_7.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a3_7);
        p3_7show.add(scrollPane);
        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p3_7.add(p3_7Info, gbc);
        gbc.gridy = 1;
        p3_7.add(p3_7AddInfo1, gbc);
        gbc.gridy = 2;
        p3_7.add(p3_7AddInfo2, gbc);
        gbc.gridy = 3;
        p3_7.add(p3_7Epsilon, gbc);
        gbc.gridy = 4;
        p3_7.add(p3_7N, gbc);
        gbc.gridy = 5;
        p3_7.add(p3_7Buttons, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p3_7.add(p3_7show, gbc);
    }
    //4-雅可比迭代公式GUI初始化
    void initJacobi() {
        JPanel p4_1 = new JPanel();
        p4_1.setLayout(new GridBagLayout());
        tp4.add("雅可比迭代公式", p4_1);
        //函数提示1
        JPanel p4_1Info1 = new JPanel();
        p4_1Info1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_1Info1.add(new JLabel("本环节将对以下线性方程组进行雅可比迭代公式求解"));
        //函数提示2
        JPanel p4_1Info2 = new JPanel();
        p4_1Info2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        // 创建线性方程组的HTML格式文本
        String equationText = "<html>"
                + "| 10x1  -   x2  -  2x3 = 7.2 <br>"
                + "| - x1  + 10x2  -  2x3 = 8.3 <br>"
                + "| - x1  -   x2  +  5x3 = 4.2 <br>"
                + "</html>";
        p4_1Info2.add(new JLabel(equationText));
        //输入精度
        JPanel p4_1Epsilon = new JPanel();
        p4_1Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_1Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t4_1ne  = new JTextField(6);
        t4_1ne.getDocument().addUndoableEditListener(undoManager);
        t4_1ne.setText("0.00001");
        p4_1Epsilon.add(t4_1ne);
        //按钮群
        JPanel p4_1Buttons = new JPanel();
        JButton b4_1cal = new JButton("计算");
        b4_1cal.setForeground(Color.BLUE);
        b4_1cal.addActionListener(event->{
            try {
                double e = Double.parseDouble(t4_1ne.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                jacobi.calculate(e);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b4_1reset = new JButton("重置输入");
        b4_1reset.addActionListener(event->{
            t4_1ne.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b4_1clear = new JButton("清空下方显示");
        b4_1clear.setForeground(Color.red);
        b4_1clear.addActionListener(event->{
            a4_1.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b4_1help = new JButton("帮助");
        b4_1help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n雅可比迭代公式是一种利用迭代法将联立方程组归结为一组彼此独立的线性表达式的解线性方程组的算法。\n"
                        +"\n您的操作:\n你只需要在该界面中输入迭代精度ε即可。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对给出的线性方程组进行雅可比迭代公式求解。",
                "雅可比迭代公式帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p4_1Buttons.add(b4_1cal);
        p4_1Buttons.add(b4_1reset);
        p4_1Buttons.add(b4_1clear);
        p4_1Buttons.add(b4_1help);
        //显示区域
        JPanel p4_1show = new JPanel(new BorderLayout());
        a4_1 = new JTextArea();
        a4_1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a4_1);
        p4_1show.add(scrollPane);


        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p4_1.add(p4_1Info1, gbc);
        gbc.gridy = 1;
        p4_1.add(p4_1Info2, gbc);
        gbc.gridy = 2;
        p4_1.add(p4_1Epsilon, gbc);
        gbc.gridy = 3;
        p4_1.add(p4_1Buttons, gbc);
        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_1.add(p4_1show, gbc);
    }
    //4-高斯-塞德尔迭代GUI初始化
    void initGauss_Seidel(){
        JPanel p4_2 = new JPanel();
        p4_2.setLayout(new GridBagLayout());
        tp4.add("高斯-塞德尔迭代",p4_2);
        //函数提示1
        JPanel p4_2Info1 = new JPanel();
        p4_2Info1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_2Info1.add(new JLabel("本环节将对以下线性方程组进行高斯-塞德尔迭代求解"));
        //函数提示2
        JPanel p4_2Info2 = new JPanel();
        p4_2Info2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        // 创建线性方程组的HTML格式文本
        String equationText = "<html>"
                + "| 10x1  -   x2  -  2x3 = 7.2 <br>"
                + "| - x1  + 10x2  -  2x3 = 8.3 <br>"
                + "| - x1  -   x2  +  5x3 = 4.2 <br>"
                + "</html>";
        p4_2Info2.add(new JLabel(equationText));
        //输入精度
        JPanel p4_2Epsilon = new JPanel();
        p4_2Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_2Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t4_2ne  = new JTextField(6);
        t4_2ne.getDocument().addUndoableEditListener(undoManager);
        t4_2ne.setText("0.00001");
        p4_2Epsilon.add(t4_2ne);
        //按钮群
        JPanel p4_2Buttons = new JPanel();
        JButton b4_2cal = new JButton("计算");
        b4_2cal.setForeground(Color.BLUE);
        b4_2cal.addActionListener(event->{
            try {
                double e = Double.parseDouble(t4_2ne.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                gaussSeidel.calculate(e);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b4_2reset = new JButton("重置输入");
        b4_2reset.addActionListener(event->{
            t4_2ne.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b4_2clear = new JButton("清空下方显示");
        b4_2clear.setForeground(Color.red);
        b4_2clear.addActionListener(event->{
            a4_2.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b4_2help = new JButton("帮助");
        b4_2help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n高斯-塞德尔迭代是对雅可比迭代公式的优化，利用每一次迭代结果实时更新，不仅提高了迭代速度，也优化了储存空间。\n"
                        +"\n您的操作:\n你只需要在该界面中输入迭代精度ε即可。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对给出的线性方程组进行高斯-塞德尔迭代求解。",
                "高斯-塞德尔迭代帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p4_2Buttons.add(b4_2cal);
        p4_2Buttons.add(b4_2reset);
        p4_2Buttons.add(b4_2clear);
        p4_2Buttons.add(b4_2help);
        //显示区域
        JPanel p4_2show = new JPanel(new BorderLayout());
        a4_2 = new JTextArea();
        a4_2.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a4_2);
        p4_2show.add(scrollPane);


        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p4_2.add(p4_2Info1, gbc);
        gbc.gridy = 1;
        p4_2.add(p4_2Info2, gbc);
        gbc.gridy = 2;
        p4_2.add(p4_2Epsilon, gbc);
        gbc.gridy = 3;
        p4_2.add(p4_2Buttons, gbc);
        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_2.add(p4_2show, gbc);
    }
    //4-超松弛法GUI初始化
    void initSOR(){
        JPanel p4_3 = new JPanel();
        p4_3.setLayout(new GridBagLayout());
        tp4.add("超松弛法",p4_3);
        //函数提示1
        JPanel p4_3Info1 = new JPanel();
        p4_3Info1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_3Info1.add(new JLabel("本环节将对以下线性方程组进行超松弛法求解"));
        //函数提示2
        JPanel p4_3Info2 = new JPanel();
        p4_3Info2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        // 创建线性方程组的HTML格式文本
        String equationText = "<html>"
                + "| 10x1  -   x2  -  2x3 = 7.2 <br>"
                + "| - x1  + 10x2  -  2x3 = 8.3 <br>"
                + "| - x1  -   x2  +  5x3 = 4.2 <br>"
                + "</html>";
        p4_3Info2.add(new JLabel(equationText));
        //输入精度
        JPanel p4_3Epsilon = new JPanel();
        p4_3Epsilon.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_3Epsilon.add(new JLabel("请输入精度ε:"));
        JTextField t4_3ne  = new JTextField(6);
        t4_3ne.getDocument().addUndoableEditListener(undoManager);
        t4_3ne.setText("0.00001");
        p4_3Epsilon.add(t4_3ne);
        //输入松弛因子
        JPanel p4_3O = new JPanel();
        p4_3O.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_3O.add(new JLabel("请输入松弛因子ω:"));
        JTextField t4_3no  = new JTextField(6);
        t4_3no.getDocument().addUndoableEditListener(undoManager);
        p4_3O.add(t4_3no);
        //按钮群
        JPanel p4_3Buttons = new JPanel();
        JButton b4_3cal = new JButton("计算");
        b4_3cal.setForeground(Color.BLUE);
        b4_3cal.addActionListener(event->{
            try {
                double e = Double.parseDouble(t4_3ne.getText());
                double o = Double.parseDouble(t4_3no.getText());
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                sor.calculate(e,o);
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b4_3reset = new JButton("重置输入");
        b4_3reset.addActionListener(event->{
            t4_3ne.setText("");
            t4_3no.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b4_3clear = new JButton("清空下方显示");
        b4_3clear.setForeground(Color.red);
        b4_3clear.addActionListener(event->{
            a4_3.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b4_3help = new JButton("帮助");
        b4_3help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n超松弛法是对高斯-塞德尔迭代的优化，通过加入松弛因子ω，来可能的加速迭代。不过ω的最优值需要通过实验测定。\n"
                        +"\n您的操作:\n你需要在该界面中输入迭代精度ε和松弛因子ω。\n按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序仅对给出的线性方程组进行超松弛法求解。\nω的取值范围为0-2，但是这里需要输入1-2的范围确保结果是收敛的。",
                "超松弛法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p4_3Buttons.add(b4_3cal);
        p4_3Buttons.add(b4_3reset);
        p4_3Buttons.add(b4_3clear);
        p4_3Buttons.add(b4_3help);
        //显示区域
        JPanel p4_3show = new JPanel(new BorderLayout());
        a4_3 = new JTextArea();
        a4_3.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(a4_3);
        p4_3show.add(scrollPane);


        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p4_3.add(p4_3Info1, gbc);
        gbc.gridy = 1;
        p4_3.add(p4_3Info2, gbc);
        gbc.gridy = 2;
        p4_3.add(p4_3Epsilon, gbc);
        gbc.gridy = 3;
        p4_3.add(p4_3O, gbc);
        gbc.gridy = 4;
        p4_3.add(p4_3Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_3.add(p4_3show, gbc);
    }
    //4-约当消去法GUI初始化
    void initJordan(){
        JPanel p4_4 = new JPanel();
        p4_4.setLayout(new GridBagLayout());
        tp4.add("约当消去法",p4_4);
        //函数提示
        JPanel p4_4Info = new JPanel();
        p4_4Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_4Info.add(new JLabel("本环节将对自定义输入的线性方程组进行约当消去法求解"));
        //输入未知数数量
        JPanel p4_4n = new JPanel();
        p4_4n.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_4n.add(new JLabel("请输入未知数数量n:"));
        JTextField t4_4n  = new JTextField(6);
        t4_4n.getDocument().addUndoableEditListener(undoManager);
        p4_4n.add(t4_4n);
        //输入增广矩阵提示
        JPanel p4_4mInfo = new JPanel();
        p4_4mInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_4mInfo.add(new JLabel("请输入增广矩阵:"));
        //输入增广矩阵
        JPanel p4_4M = new JPanel(new BorderLayout());
        JTextArea a4_41  = new JTextArea();
        JScrollPane scrollPane1 = new JScrollPane(a4_41);
        a4_41.getDocument().addUndoableEditListener(undoManager);
        p4_4M.add(scrollPane1);
        //按钮群
        JPanel p4_4Buttons = new JPanel();
        JButton b4_4add = new JButton("输入");
        b4_4add.addActionListener(event->{
            try{
                int n = Integer.parseInt(t4_4n.getText());
                String m = a4_41.getText();
                jordan.inputCoefficientMatrix(n,m);
            } catch (NumberFormatException | InputMismatchException e){
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(), smallErrorPNG, errorColor, 3);
            }
        });
        JButton b4_4out = new JButton("输出方程组");
        b4_4out.addActionListener(event-> {
            try {
                jordan.printCoefficientMatrix();
            } catch (IllegalArgumentException e) {
                tempUpdateBottomInfo(e.getMessage(), smallErrorPNG, errorColor, 3);
            }
        });
        JButton b4_4cal = new JButton("计算");
        b4_4cal.setForeground(Color.BLUE);
        b4_4cal.addActionListener(event->{
            try {
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                jordan.calculate();
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b4_4reset = new JButton("重置输入");
        b4_4reset.addActionListener(event->{
            t4_4n.setText("");
            a4_41.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b4_4clear = new JButton("清空下方显示");
        b4_4clear.setForeground(Color.red);
        b4_4clear.addActionListener(event->{
            a4_4.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b4_4help = new JButton("帮助");
        b4_4help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n约当消去法是常用的线性方程组解法。\n"
                        +"\n您的操作:\n你需要在该界面中输入未知数n以及其增广矩阵。\n先按下添加按钮以添加矩阵数据，再按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序可以对你输入的线性方程组进行求解，但是只能解非奇异矩阵。\n增广矩阵是系数矩阵加上常数b矩阵。\n非奇异矩阵为是有唯一解或解为0的线性方程组。",
                "约当消去法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p4_4Buttons.add(b4_4add);
        p4_4Buttons.add(b4_4out);
        p4_4Buttons.add(b4_4cal);
        p4_4Buttons.add(b4_4reset);
        p4_4Buttons.add(b4_4clear);
        p4_4Buttons.add(b4_4help);
        //显示区域
        JPanel p4_4show = new JPanel(new BorderLayout());
        a4_4 = new JTextArea();
        a4_4.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(a4_4);
        p4_4show.add(scrollPane2);

        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p4_4.add(p4_4Info, gbc);
        gbc.gridy = 1;
        p4_4.add(p4_4n, gbc);
        gbc.gridy = 2;
        p4_4.add(p4_4mInfo, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_4.add(p4_4M, gbc);
        gbc.weighty = 0;
        gbc.gridy = 4;
        p4_4.add(p4_4Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_4.add(p4_4show, gbc);
    }
    //4-高斯消去法GUI初始化
    void initGauss(){
        JPanel p4_5 = new JPanel();
        p4_5.setLayout(new GridBagLayout());
        tp4.add("高斯消去法",p4_5);
        //函数提示
        JPanel p4_5Info = new JPanel();
        p4_5Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_5Info.add(new JLabel("本环节将对自定义输入的线性方程组进行高斯消去法求解"));
        //输入未知数数量
        JPanel p4_5n = new JPanel();
        p4_5n.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_5n.add(new JLabel("请输入未知数数量n:"));
        JTextField t4_5n  = new JTextField(6);
        t4_5n.getDocument().addUndoableEditListener(undoManager);
        p4_5n.add(t4_5n);
        //输入增广矩阵提示
        JPanel p4_5mInfo = new JPanel();
        p4_5mInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_5mInfo.add(new JLabel("请输入增广矩阵:"));
        //输入增广矩阵
        JPanel p4_5M = new JPanel(new BorderLayout());
        JTextArea a4_51  = new JTextArea();
        JScrollPane scrollPane1 = new JScrollPane(a4_51);
        a4_51.getDocument().addUndoableEditListener(undoManager);
        p4_5M.add(scrollPane1);
        //按钮群
        JPanel p4_5Buttons = new JPanel();
        JButton b4_5add = new JButton("输入");
        b4_5add.addActionListener(event->{
            try{
                int n = Integer.parseInt(t4_5n.getText());
                String m = a4_51.getText();
                gauss.inputCoefficientMatrix(n,m);
            } catch (NumberFormatException | InputMismatchException e){
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(), smallErrorPNG, errorColor, 3);
            }
        });
        JButton b4_5out = new JButton("输出方程组");
        b4_5out.addActionListener(event-> {
            try {
                gauss.printCoefficientMatrix();
            }catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(), smallErrorPNG, errorColor, 3);
            }
        });
        JButton b4_5cal = new JButton("计算");
        b4_5cal.setForeground(Color.BLUE);
        b4_5cal.addActionListener(event->{
            try {
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                gauss.calculate();
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b4_5reset = new JButton("重置输入");
        b4_5reset.addActionListener(event->{
            t4_5n.setText("");
            a4_51.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b4_5clear = new JButton("清空下方显示");
        b4_5clear.setForeground(Color.red);
        b4_5clear.addActionListener(event->{
            a4_5.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b4_5help = new JButton("帮助");
        b4_5help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n高斯消去法是对约当消去法的优化，可以减少计算次数。\n"
                        +"\n您的操作:\n你需要在该界面中输入未知数n以及其增广矩阵。\n先按下添加按钮以添加矩阵数据，再按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序可以对你输入的线性方程组进行求解，但是只能解非奇异矩阵。\n增广矩阵是系数矩阵加上常数b矩阵。\n非奇异矩阵为是有唯一解或解为0的线性方程组。",
                "高斯消去法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p4_5Buttons.add(b4_5add);
        p4_5Buttons.add(b4_5out);
        p4_5Buttons.add(b4_5cal);
        p4_5Buttons.add(b4_5reset);
        p4_5Buttons.add(b4_5clear);
        p4_5Buttons.add(b4_5help);
        //显示区域
        JPanel p4_5show = new JPanel(new BorderLayout());
        a4_5 = new JTextArea();
        a4_5.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(a4_5);
        p4_5show.add(scrollPane2);

        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p4_5.add(p4_5Info, gbc);
        gbc.gridy = 1;
        p4_5.add(p4_5n, gbc);
        gbc.gridy = 2;
        p4_5.add(p4_5mInfo, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_5.add(p4_5M, gbc);
        gbc.weighty = 0;
        gbc.gridy = 4;
        p4_5.add(p4_5Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_5.add(p4_5show, gbc);
    }
    //列主元高斯消去法GUI初始化
    void initMainGauss(){
        JPanel p4_6 = new JPanel();
        p4_6.setLayout(new GridBagLayout());
        tp4.add("列主元高斯消去法",p4_6);
        //函数提示
        JPanel p4_6Info = new JPanel();
        p4_6Info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_6Info.add(new JLabel("本环节将对自定义输入的线性方程组进行列主元高斯消去法求解"));
        //输入未知数数量
        JPanel p4_6n = new JPanel();
        p4_6n.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_6n.add(new JLabel("请输入未知数数量n:"));
        JTextField t4_6n  = new JTextField(6);
        t4_6n.getDocument().addUndoableEditListener(undoManager);
        p4_6n.add(t4_6n);
        //输入增广矩阵提示
        JPanel p4_6mInfo = new JPanel();
        p4_6mInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        p4_6mInfo.add(new JLabel("请输入增广矩阵:"));
        //输入增广矩阵
        JPanel p4_6M = new JPanel(new BorderLayout());
        JTextArea a4_61  = new JTextArea();
        JScrollPane scrollPane1 = new JScrollPane(a4_61);
        a4_61.getDocument().addUndoableEditListener(undoManager);
        p4_6M.add(scrollPane1);
        //按钮群
        JPanel p4_6Buttons = new JPanel();
        JButton b4_6add = new JButton("输入");
        b4_6add.addActionListener(event->{
            try{
                int n = Integer.parseInt(t4_6n.getText());
                String m = a4_61.getText();
                mainGauss.inputCoefficientMatrix(n,m);
            } catch (NumberFormatException | InputMismatchException e){
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(), smallErrorPNG, errorColor, 3);
            }
        });
        JButton b4_6out = new JButton("输出方程组");
        b4_6out.addActionListener(event-> {
            try {
                mainGauss.printCoefficientMatrix();
            } catch (IllegalArgumentException e) {
                tempUpdateBottomInfo(e.getMessage(), smallErrorPNG, errorColor, 3);
            }
        });
        JButton b4_6cal = new JButton("计算");
        b4_6cal.setForeground(Color.BLUE);
        b4_6cal.addActionListener(event->{
            try {
                permanentUpdateBottomInfo("计算中...",smallReadyPNG,readyColor);
                mainGauss.calculate();
            } catch (NumberFormatException e) {
                tempUpdateBottomInfo("请输入有效数据",smallErrorPNG,errorColor,3);
            } catch (IllegalArgumentException e){
                tempUpdateBottomInfo(e.getMessage(),smallErrorPNG,errorColor,3);
            }
        });
        JButton b4_6reset = new JButton("重置输入");
        b4_6reset.addActionListener(event->{
            t4_6n.setText("");
            a4_61.setText("");
            tempUpdateBottomInfo("已重置",smallSucceedPNG,successColor,2);
        });
        JButton b4_6clear = new JButton("清空下方显示");
        b4_6clear.setForeground(Color.red);
        b4_6clear.addActionListener(event->{
            a4_6.setText("");
            tempUpdateBottomInfo("已清空",smallSucceedPNG,successColor,2);
        });
        JButton b4_6help = new JButton("帮助");
        b4_6help.addActionListener(event-> JOptionPane.showMessageDialog(Main.this,
                "介绍:\n列主元高斯消去法是高斯消去法的优化，每次将最大值行提到顶端进行计算，能避免数值小引起的误差。\n"
                        +"\n您的操作:\n你需要在该界面中输入未知数n以及其增广矩阵。\n先按下添加按钮以添加矩阵数据，再按下计算按钮后程序会给出所求解的结果。\n"
                        +"\n提示:\n本程序可以对你输入的线性方程组进行求解，但是只能解非奇异矩阵。\n增广矩阵是系数矩阵加上常数b矩阵。\n非奇异矩阵为是有唯一解或解为0的线性方程组。",
                "列主元高斯消去法帮助",JOptionPane.INFORMATION_MESSAGE,helpPNG));
        p4_6Buttons.add(b4_6add);
        p4_6Buttons.add(b4_6out);
        p4_6Buttons.add(b4_6cal);
        p4_6Buttons.add(b4_6reset);
        p4_6Buttons.add(b4_6clear);
        p4_6Buttons.add(b4_6help);
        //显示区域
        JPanel p4_6show = new JPanel(new BorderLayout());
        a4_6 = new JTextArea();
        a4_6.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(a4_6);
        p4_6show.add(scrollPane2);

        // 使用GridBagLayout进行布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        p4_6.add(p4_6Info, gbc);
        gbc.gridy = 1;
        p4_6.add(p4_6n, gbc);
        gbc.gridy = 2;
        p4_6.add(p4_6mInfo, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_6.add(p4_6M, gbc);
        gbc.weighty = 0;
        gbc.gridy = 4;
        p4_6.add(p4_6Buttons, gbc);
        gbc.gridy = 5;
        gbc.weighty = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p4_6.add(p4_6show, gbc);
    }

    //初始化底部信息栏目
    private void initBottomInfoPanel(){
        //主
        JPanel bottomInfoPanel = new JPanel();

        Border topBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK);
        bottomInfoPanel.setBorder(topBorder);
        bottomInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        bottomInfoLabel = new JLabel("就绪");
        bottomInfoLabel.setForeground(readyColor);
        bottomInfoLabel.setIcon(smallReadyPNG);

        bottomInfoPanel.add(bottomInfoLabel);

        getContentPane().add(bottomInfoPanel, BorderLayout.SOUTH);
    }

    //初始化窗体设置
    private void initFormSetting() {
        this.setSize(700, 600);
        this.setIconImage(smallReadyPNG.getImage());
        //设置最小大小
        this.setMinimumSize(new Dimension(550, 400));
        // 设置窗口初始化居中
        this.setLocationRelativeTo(null);
        // 设置默认的关闭按钮操作为退出程序
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    //Node结构体
    private static class Node{
        public double x;
        double y;
        Node(){
            x = 0;
            y = 0;
        }
        Node(double x1, double y1){
            x=x1;
            y=y1;
        }
    }
    //拉格朗日插值处理类
    class LagrangeInterpolation{
        private List<Node> n;
        LagrangeInterpolation(){
            n = new ArrayList<>();
        }
        public void calculate(double x){
            if (n.isEmpty()){
                throw new IllegalArgumentException("你没有输入任何节点");
            }
            double numerator = 1,L = 0;
            for (Node node : n) {
                if (Double.compare(x,node.x) == 0){
                    a1_1.append("该节点就是已知节点之一，其纵坐标为"+node.y+"\n");
                    a1_1.append("节点数据仍然保留\n");
                    permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
                    return;
                }
            }
            for (Node node : n) {
                numerator *= (x - node.x);
            }
            for(int i = 0; i < n.size(); ++i){
                double denominator = 1;
                for(int j = 0; j < n.size(); ++j){
                    if (i != j) {
                        denominator *= (n.get(i).x - n.get(j).x);
                    }
                }
                L += ((numerator/(x-n.get(i).x))/(denominator))*(n.get(i).y);
            }
            a1_1.append("横坐标为"+x+"的点的近似值由拉格朗日插值法得到为:"+L+"\n");
            a1_1.append("节点数据仍然保留\n");
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
        public void deleteAllNode(){
            n = new ArrayList<>();
            a1_1.append("所有节点删除成功\n");
            tempUpdateBottomInfo("所有节点删除成功",smallSucceedPNG,successColor,1);
        }
        public void addNode(double x, double y) {
            for (Node node : n) {
                if (node.x == x) {
                    throw new IllegalArgumentException("已存在相同的x值的节点");
                }
            }
            n.add(new Node(x, y));
            a1_1.append("已添加节点:("+x+","+y+")\n");
            tempUpdateBottomInfo("所有节点添加成功",smallSucceedPNG,successColor,1);
        }
    }
    //牛顿差值法处理类
    class NewtonInterpolation{
        private List<Node> n;
        private ArrayList<ArrayList<Double>> dv;
        private double N,w;
        NewtonInterpolation(){
            n = new ArrayList<>();
            dv = new ArrayList<>();
            N = 0;
            w=1;
        }
        void addNodeAndCalculate(double nx, double ny,double x){
            for (Node node : n) {
                if (node.x == nx) {
                    throw new IllegalArgumentException("已存在相同的x值的节点");
                }
            }
            n.add(new Node(nx, ny));
            for (Node node : n) {
                if (Double.compare(x,node.x) == 0){
                    a1_2.append("该节点就是已知节点之一，其纵坐标为"+node.y+"\n");
                    permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
                    return;
                }
            }
            dv.add(new ArrayList<>());
            a1_2.append("已添加节点:("+nx+","+ny+")\n");
            int i = dv.size()-1;
            dv.get(i).add(n.get(i).y);
            for (int j = 1; j <= i; ++j) {
                dv.get(i).add((dv.get(i).get(j-1)- dv.get(i-1).get(j-1))/(n.get(i).x-n.get(i-j).x));
            }
            if (i>0) {
                w *= (x - n.get(i-1).x);
            }
            N += (w * dv.get(i).get(i));
            a1_2.append("横坐标为"+x+"的点的近似值在"+(i+1)+"个节点时由牛顿插值法得到为:"+N+"\n");
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
        public void deleteAllNode(){
            n = new ArrayList<>();
            dv = new ArrayList<>();
            N= 0;
            w=1;
            a1_2.append("所有节点删除成功\n");
            tempUpdateBottomInfo("所有节点删除成功",smallSucceedPNG,successColor,1);
        }
    }
    //欧拉方法处理类
    class Euler{
        double x,y,h;
        int num;
        private CalculateThread calculateThread;
        Euler() {
            x = 0;
            y = 1;
            h = 0.1;
            num = 11;
            calculateThread = new CalculateThread();
        }
        //常微分方程
        private double func(double x,double y) {
            return y - 2 * x / y;
        }
        private class CalculateThread extends Thread{
            @Override
            public void run() {
                boolean isInfinity = false;
                StringBuilder result = new StringBuilder("初值节点为("+x+","+y+"),步长为"+h+"的求值结果为:\n");
                ArrayList<Node> n = new ArrayList<>();
                n.add(new Node(x, y));
                for (int i = 1; i < num; ++i) {
                    x += h;
                    y = n.get(i-1).y + h * func(n.get(i-1).x, n.get(i-1).y);
                    if (Double.isInfinite(y)){
                        isInfinity = true;
                        num = i;
                        break;
                    }
                    n.add(new Node(x, y));
                }
                for (int i = 0; i <= num; ++i) {
                    result.append(String.format("x%-3d= %-10.4f", i, n.get(i).x));
                    result.append(String.format("y%-3d= %-10.4f\n", i, n.get(i).y));
                }
                a2_1.append(result.toString());
                if (isInfinity){
                    a2_1.append("过大的数据已截断");
                }
                permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
            }
        }
        public void calculate(double x1,double y1,double h1,int num1){
            if (!calculateThread.isAlive()){
                calculateThread = new CalculateThread();
            }else {
                throw new IllegalArgumentException("上一个计算正在运行！");
            }
            if (h1<=0){
                throw new IllegalArgumentException("步长必须大于0");
            }
            if (num1<=0){
                throw new IllegalArgumentException("请输入有效个数");
            }
            if (num1>100000){
                throw new IllegalArgumentException("数据过大");
            }
            x = x1;
            y = y1;
            h  =h1;
            num = num1;
            calculateThread.start();
        }
    }
    //两步欧拉方法处理类
    class TwoSteps_Euler{
        private double x1,x2,y1,y2;
        private double h;
        private int num;
        private CalculateThread calculateThread;
        TwoSteps_Euler() {
            x1 = 0;
            y1 = 1;
            x2 = 0.1;
            y2 = 1.0954;
            h = 0.1;
            num = 11;
            calculateThread = new CalculateThread();
        }
        //常微分方程
        private double func(double x,double y) {
            return y - 2 * x / y;
        }
        private class CalculateThread extends Thread{
            @Override
            public void run() {
                boolean isInfinity = false;
                StringBuilder result = new StringBuilder("初值节点为("+x1+","+y1+")和("+x2+","+y2+"),步长为"+h+"的求值结果为:\n");
                ArrayList<Node> n = new ArrayList<>();
                n.add(new Node(x1, y1));
                n.add(new Node(x2, y2));
                double x = x2,y;
                for (int i = 2; i <= num+1; ++i) {
                    x += h;
                    y = n.get(i-2).y + 2 * h * func(n.get(i-1).x, n.get(i-1).y);
                    if (Double.isInfinite(y)){
                        isInfinity = true;
                        num = i;
                        break;
                    }
                    n.add(new Node(x, y));
                }
                for (int i = 0; i < num; ++i) {
                    result.append(String.format("x%-3d= %-10.4f", i, n.get(i).x));
                    result.append(String.format("y%-3d= %-10.4f\n", i, n.get(i).y));
                }
                a2_2.append(result.toString());
                if (isInfinity){
                    a2_2.append("过大的数据已截断");
                }
                permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
            }
        }
        public void calculate(double x11,double y11,double x21,double y21,int num1){
            if (!calculateThread.isAlive()){
                calculateThread = new CalculateThread();
            }else {
                throw new IllegalArgumentException("上一个计算正在运行！");
            }
            double h1 = x21-x11;
            if (h1<=0){
                throw new IllegalArgumentException("x2必须大于x1");
            }
            if (num1<=0){
                throw new IllegalArgumentException("请输入有效个数");
            }
            if (num1>100000){
                throw new IllegalArgumentException("数据过大");
            }
            x1 = x11;
            y1 = y11;
            x2 = x21;
            y2 = y21;
            h  = x21-x11;
            num = num1;
            calculateThread.start();
        }
    }
    //改进欧拉处理类
    class Improved_Euler{
        private double x,y,h;
        private int num;
        private CalculateThread calculateThread;
        Improved_Euler() {
            x = 0;
            y = 1;
            h = 0.1;
            num = 11;
            calculateThread = new CalculateThread();
        }
        //常微分方程
        private double func(double x,double y) {
            return y - 2 * x / y;
        }
        private class CalculateThread extends Thread{
            @Override
            public void run() {
                boolean isInfinity = false;
                StringBuilder result = new StringBuilder("初值节点为("+x+","+y+"),步长为"+h+"的求值结果为:\n");
                ArrayList<Node> n = new ArrayList<>();
                n.add(new Node(x, y));
                for (int i = 1; i <= num; ++i) {
                    x += h;
                    y = n.get(i-1).y + h * func(n.get(i-1).x, n.get(i-1).y);
                    y = n.get(i-1).y +(h/2)*(func(n.get(i-1).x,n.get(i-1).y)+func(x,y));
                    if (Double.isInfinite(y)){
                        isInfinity = true;
                        num = i;
                        break;
                    }
                    n.add(new Node(x, y));
                }
                for (int i = 0; i < num; ++i) {
                    result.append(String.format("x%-3d= %-10.4f", i, n.get(i).x));
                    result.append(String.format("y%-3d= %-10.4f\n", i, n.get(i).y));
                }
                a2_3.append(result.toString());
                if (isInfinity){
                    a2_3.append("过大的数据已截断");
                }
                permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
            }
        }
        public void calculate(double x1,double y1,double h1,int num1){
            if (!calculateThread.isAlive()){
                calculateThread = new CalculateThread();
            }else {
                throw new IllegalArgumentException("上一个计算正在运行！");
            }
            if (h1<=0){
                throw new IllegalArgumentException("步长必须大于0");
            }
            if (num1<=0){
                throw new IllegalArgumentException("请输入有效个数");
            }
            if (num1>100000){
                throw new IllegalArgumentException("数据过大");
            }
            x = x1;
            y = y1;
            h  =h1;
            num = num1;
            calculateThread.start();
        }
    }
    //四阶龙格-库塔处理类
    class Classical_Runge_Kutta{
        private double x,y,h;
        private int num;
        private CalculateThread calculateThread;
        Classical_Runge_Kutta() {
            x = 0;
            y = 1;
            h = 0.1;
            num = 11;
            calculateThread = new CalculateThread();
        }
        //常微分方程
        private double func(double x,double y) {
            return y - 2 * x / y;
        }
        private class CalculateThread extends Thread{
            @Override
            public void run() {
                boolean isInfinity = false;
                StringBuilder result = new StringBuilder("初值节点为("+x+","+y+"),步长为"+h+"的求值结果为:\n");
                ArrayList<Node> n = new ArrayList<>();
                double k1,k2,k3,k4;
                n.add(new Node(x, y));
                for (int i = 1; i <= num; ++i) {
                    k1 = func(x,y);
                    k2 = func(x+0.5*h,y+h/2*k1);
                    k3 = func(x+0.5*h,y+h/2*k2);
                    k4 = func(x+h,y+h*k3);
                    y = y + h/6*(k1+2*k2+2*k3+k4);
                    x+=h;
                    if (Double.isInfinite(y)){
                        isInfinity = true;
                        num = i;
                        break;
                    }
                    n.add(new Node(x, y));
                }
                for (int i = 0; i < num; ++i) {
                    result.append(String.format("x%-3d= %-10.4f", i, n.get(i).x));
                    result.append(String.format("y%-3d= %-10.4f\n", i, n.get(i).y));
                }
                a2_4.append(result.toString());
                if (isInfinity){
                    a2_4.append("过大的数据已截断");
                }
                permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
            }
        }
        public void calculate(double x1,double y1,double h1,int num1){
            if (!calculateThread.isAlive()){
                calculateThread = new CalculateThread();
            }else {
                throw new IllegalArgumentException("上一个计算正在运行！");
            }
            if (h1<=0){
                throw new IllegalArgumentException("步长必须大于0");
            }
            if (num1<=0){
                throw new IllegalArgumentException("请输入有效个数");
            }
            if (num1>100000){
                throw new IllegalArgumentException("数据过大");
            }
            x = x1;
            y = y1;
            h = h1;
            num = num1;
            calculateThread.start();
        }
    }
    //亚当姆斯预报-校正系统处理类
    class Adams_Calibration_System{
        private double[] x , y;
        private double h;
        private int num;
        private CalculateThread calculateThread;
        Adams_Calibration_System(){
            x = new double[]{0.0,0.0,0.0,0.0};
            y = new double[]{0.0,0.0,0.0,0.0};
            h = 0.1;
            num = 1;
            calculateThread = new CalculateThread();
        }
        //常微分方程
        private double func(double x,double y) {
            return y - 2 * x / y;
        }
        private class CalculateThread extends Thread{
            @Override
            public void run() {
                boolean isInfinity = false;
                StringBuilder result = new StringBuilder("初值节点为("+x[0]+","+y[0]+")、("+x[1]+","+y[1]+")、("+x[2]+","+y[2]+")、("+x[3]+","+y[3]+")、(,步长为"+h+"的求值结果为:\n");
                ArrayList<Node> n = new ArrayList<>();
                double tmpx,tmpy,tmpyfy;

                for (int i = 0;i<4;i++) {
                    n.add(new Node(x[i], y[i]));
                }

                for (int i = 4; i < num; ++i) {
                    tmpx = n.get(i-1).x + h;
                    tmpy = (n.get(i-1).y) + h * ((55 * func(n.get(i-1).x, n.get(i-1).y)) - (59 * func(n.get(i-2).x, n.get(i-2).y)) + (37 * func(n.get(i-3).x, n.get(i-3).y)) - (9 * func(n.get(i-4).x, n.get(i-4).y))) / 24.0;
                    tmpyfy = func(tmpx, tmpy);
                    tmpy = n.get(i-1).y + (h / 24.0) * (9 * tmpyfy + 19 * func(n.get(i-1).x, n.get(i-1).y) - 5 * func(n.get(i-2).x, n.get(i-2).y) + func(n.get(i-3).x, n.get(i-3).y));
                    if (Double.isInfinite(tmpy)){
                        isInfinity = true;
                        num = i;
                        break;
                    }
                    n.add(new Node(tmpx, tmpy));
                }
                for (int i = 0; i < num; ++i) {
                    result.append(String.format("x%-3d= %-10.4f", i, n.get(i).x));
                    result.append(String.format("y%-3d= %-10.4f\n", i, n.get(i).y));
                }
                a2_5.append(result.toString());
                if (isInfinity){
                    a2_5.append("过大的数据已截断");
                }
                permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
            }
        }
        public void calculate(double x11,double y11,double x21,double y21,double x31,double y31,double x41,double y41,double h1,int num1){
            if (!calculateThread.isAlive()){
                calculateThread = new CalculateThread();
            }else {
                throw new IllegalArgumentException("上一个计算正在运行！");
            }
            if (h1<=0){
                throw new IllegalArgumentException("步长必须大于0");
            }
            if (num1<=0){
                throw new IllegalArgumentException("请输入有效个数");
            }
            if (num1>100000){
                throw new IllegalArgumentException("数据过大");
            }
            x[0] = x11;
            x[1] = x21;
            x[2] = x31;
            x[3] = x41;
            y[0] = y11;
            y[1] = y21;
            y[2] = y31;
            y[3] = y41;
            h = h1;
            num = num1;
            calculateThread.start();
        }
    }
    //普通迭代算法
    class NormalIteration{
        double realX;
        NormalIteration(){
            realX = 0.567143;
        }
        //y=e^-x
        private double fun_e_x(double x) {
            return Math.exp(-x);
        }
        public void calculate(double x0,double epsilon,double N){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            ArrayList<Double> x = new ArrayList<>();
            x.add(x0);
            int i = 1;
            do {
                x.add(fun_e_x(x.get(i-1)));
                i++;
            }while (i<N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_1.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_1.append(String.format("初值为%f,精度为%f的迭代结果为:\n",x0,epsilon));
                for (int j = 0;j<x.size();j++){
                    a3_1.append(String.format("第%-3d次迭代: x=%.6f\n",j,x.get(j)));
                }
                a3_1.append("准确值为"+realX+"\n\n");
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //埃特金算法
    class Aitken{
        double realX;
        Aitken(){
            realX = 0.567143;
        }
        //y=e^-x
        private double fun_e_x(double x) {
            return Math.exp(-x);
        }
        public void calculate(double x0,double epsilon,int N){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            ArrayList<Double> x = new ArrayList<>();
            x.add(x0);
            double tmpX1 = x0;
            double tmpX2 = x0;
            a3_2.append(String.format("初值为%f,精度为%f的迭代结果为:\n",x0,epsilon));
            int i = 1;
            do {
                a3_2.append(String.format("第%-3d次迭代: ",i));
                tmpX1 = fun_e_x(x.get(i-1));
                a3_2.append(String.format("x~:%.6f   ",tmpX1));
                tmpX2 = fun_e_x(tmpX1);
                a3_2.append(String.format("x-:%.6f   ",tmpX2));
                x.add(tmpX2 - (Math.pow((tmpX2-tmpX1),2))/(tmpX2-2*tmpX1+x.get(i-1)));
                a3_2.append(String.format("x:%.6f\n",x.get(i)));
                i++;
            }while (i<N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_2.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_2.append("准确值为"+realX+"\n\n");
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //牛顿法
    class Newton{
        double realX1;
        //y=e^x-2
        double fun_ex_2(double x) {
            return Math.exp(x)-2;
        }
        //y':y=e^x-2
        double fun_ex_2_derivative(double x) {
            return Math.exp(x);
        }
        double realX2;
        //y=x^3-x-1
        double fun_x3x1(double x){
            return Math.pow(x,3)-x-1;
        }
        //y':y=x^3-x-1
        double fun_x3x1_derivative(double x){
            return 3 * Math.pow(x,2)-1;
        }
        //牛顿法
        double fun_Newton1(double x){
            return x-fun_ex_2(x)/fun_ex_2_derivative(x);
        }
        double fun_Newton2(double x){
            return x-fun_x3x1(x)/fun_x3x1_derivative(x);
        }
        Newton(){
            realX1 = 0.693148;
            realX2 = 1.324717;
        }
        public void calculate(double x0,double epsilon,int N,int type){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            ArrayList<Double> x = new ArrayList<>();
            x.add(x0);
            int i = 1;
            do {
                if (type == 0) {
                    x.add(fun_Newton1(x.get(i-1)));
                }else {
                    x.add(fun_Newton2(x.get(i-1)));
                }
                i++;
            } while (i < N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_3.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_3.append(String.format("对"+(type == 0 ? "e^x-2=0" : "x^3-x-1=0")+"进行初值为%f,精度为%f的迭代结果为:\n",x0,epsilon));
                for (int j = 0;j<x.size();j++){
                    a3_3.append(String.format("第%-3d次迭代: x=%.6f\n",j,x.get(j)));
                }
                a3_3.append("准确值为"+ (type == 0 ? realX1 : realX2) +"\n\n");
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //牛顿下山法
    class NewtonDownHill{
        double realX2;
        //y=x^3-x-1
        double fun_x3x1(double x){
            return Math.pow(x,3)-x-1;
        }
        //y':y=x^3-x-1
        double fun_x3x1_derivative(double x){
            return 3 * Math.pow(x,2)-1;
        }
        double fun_Newton2(double x){
            return x-fun_x3x1(x)/fun_x3x1_derivative(x);
        }
        NewtonDownHill(){
            realX2 = 1.324717;
        }
        public void calculate(double x0,double epsilon,int N){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            ArrayList<Double> x = new ArrayList<>();
            x.add(x0);
            int i = 1;
            do {
                double x2 = fun_Newton2(x.get(i-1));
                double lamda = 1.0;
                double x3 = x2;
                while (Math.abs(fun_x3x1(x3))>= Math.abs(fun_x3x1(x.get(i-1)))){
                    lamda/=2.0;
                    x3 = lamda*x2+(1 - lamda)*x.get(i-1);
                }
                x.add(x3);
                i++;
            } while (i < N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_4.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_4.append(String.format("初值为%f,精度为%f的迭代结果为:\n",x0,epsilon));
                for (int j = 0;j<x.size();j++){
                    a3_4.append(String.format("第%-3d次迭代: x=%.6f\n",j,x.get(j)));
                }
                a3_4.append("准确值为"+realX2 +"\n\n");
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //开方公式
    class Square{
        //开方公式
        double fun_sqrt(double x,double c){
            return 0.5*(x+c/x);
        }
        public void calculate(double c,double x0,double epsilon,int N){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (c<0){
                throw new IllegalArgumentException("c<0导致方程无解");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            if (x0<=0){
                throw new IllegalArgumentException("初值x必须大于0");
            }
            ArrayList<Double> x = new ArrayList<>();
            double realX = Math.sqrt(c);
            x.add(x0);
            int i = 1;
            do {
                x.add(fun_sqrt(x.get(i-1),c));
                i++;
            } while (i < N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_5.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_5.append(String.format("参数为%f,初值为%f,精度为%f的迭代结果为:\n",c,x0,epsilon));
                for (int j = 0;j<x.size();j++){
                    a3_5.append(String.format("第%-3d次迭代: x=%.6f\n",j,x.get(j)));
                }
                a3_5.append(String.format("准确值为%.6f\n\n",realX));
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //弦截法
    class Secant{
        private double realX;
        Secant(){
            realX = 0.693148;
        }
        //y=e^x-2
        double fun_ex_2(double x) {
            return Math.exp(x)-2;
        }
        //弦截法
        double fun_secant(double x0,double x){
            return x-(fun_ex_2(x)/(fun_ex_2(x)-fun_ex_2(x0)))*(x-x0);
        }
        public void calculate(double x0,double x1,double epsilon,int N){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            if (Double.compare(x0,x1)==0){
                throw new IllegalArgumentException("两个初值不能相同");
            }
            ArrayList<Double> x = new ArrayList<>();
            x.add(x0);
            x.add(x1);
            int i = 2;
            do {
                x.add(fun_secant(x.get(0), x.get(i-1)));
                i++;
            } while (i < N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_6.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_6.append(String.format("初值为%f和%f,精度为%f的迭代结果为:\n",x0,x1,epsilon));
                for (int j = 0;j<x.size();j++){
                    if (Double.isInfinite(x.get(j)) || Double.isNaN(x.get(j))){
                        a3_6.append(String.format("第%-3d次迭代: x超出范围，请重新选择合适的初值\n",j));
                        break;
                    }
                    a3_6.append(String.format("第%-3d次迭代: x=%.6f\n",j,x.get(j)));
                }
                a3_6.append("准确值为"+ realX +"\n\n");
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //快速弦截法
    class FastSecant{
        private double realX;
        FastSecant(){
            realX = 0.693148;
        }
        //y=e^x-2
        double fun_ex_2(double x) {
            return Math.exp(x)-2;
        }
        //弦截法
        double fun_secant(double x0,double x){
            return x-(fun_ex_2(x)/(fun_ex_2(x)-fun_ex_2(x0)))*(x-x0);
        }
        public void calculate(double x0,double x1,double epsilon,int N){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (N <= 0){
                throw new IllegalArgumentException("迭代停止次数必须大于0");
            }
            if (Double.compare(x0,x1)==0){
                throw new IllegalArgumentException("两个初值不能相同");
            }
            ArrayList<Double> x = new ArrayList<>();
            x.add(x0);
            x.add(x1);
            int i = 2;
            do {
                x.add(fun_secant(x.get(i-2), x.get(i-1)));
                i++;
            } while (i < N && Math.abs(x.get(i-1) - x.get(i-2)) > epsilon);
            if (i>=N){
                a3_7.append("数据迭代次数过多，请尝试换一个合适的初值\n");
            } else{
                a3_7.append(String.format("初值为%f和%f,精度为%f的迭代结果为:\n",x0,x1,epsilon));
                for (int j = 0;j<x.size();j++){
                    if (Double.isInfinite(x.get(j)) || Double.isNaN(x.get(j))){
                        a3_7.append(String.format("第%-3d次迭代: x超出范围，请重新选择合适的初值\n",j));
                        break;
                    }
                    a3_7.append(String.format("第%-3d次迭代: x=%.6f\n",j,x.get(j)));
                }
                a3_7.append("准确值为"+ realX +"\n\n");
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //雅可比迭代公式
    class Jacobi{
        double norm(double[] k,double num){
            double sum = 0.0;
            for (int i = 0; i < num; ++i) {
                sum += k[i] * k[i];
            }
            return Math.sqrt(sum);
        }
        public void calculate(double epsilon){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            a4_1.append("精度为"+epsilon+"的迭代结果为:\n");
            double x[] = new double[]{0,0,0};//解
            double tmpx[] = new double[]{0,0,0};//临时
            for (int i = 0; i < 100; ++i) {
                tmpx[0] = 0.1*x[1] + 0.2*x[2] + 0.72;
                tmpx[1] = 0.1*x[0] + 0.2*x[2] + 0.83;
                tmpx[2] = 0.2*x[0] + 0.2*x[1] + 0.84;
                a4_1.append(String.format("第%3d次迭代结果:",i+1));
                for(int j=0;j<3;j++){
                    a4_1.append(String.format("x%d:%-10.6f",j+1,x[j]));
                }
                a4_1.append("\n");
                if (Math.abs(norm(x,3)- norm(tmpx,3))<epsilon){
                    a4_1.append("达到精度要求，迭代结束\n\n");
                    permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
                    return;
                }
                for (int j = 0; j < 3; ++j) {
                    x[j] = tmpx[j];
                }
            }
            a4_1.append("迭代次数过多，仍未达到要求精度，迭代失败。\n\n");
            tempUpdateBottomInfo("迭代失败",smallErrorPNG,errorColor,3);
        }
    }
    //高斯-塞德尔迭代
    class Gauss_Seidel{
        double norm(double[] k,double num){
            double sum = 0.0;
            for (int i = 0; i < num; ++i) {
                sum += k[i] * k[i];
            }
            return Math.sqrt(sum);
        }
        public void calculate(double epsilon){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            a4_2.append("精度为"+epsilon+"的迭代结果为:\n");
            double x[] = new double[]{0,0,0};//解
            double length = 0;
            double length2 = 0;
            a4_2.append("第0次迭代结果:");
            a4_2.append(String.format("x0:%-10.6f",x[0]));
            for (int i = 0; i < 100; ++i) {
                x[0] = 0.1*x[1] + 0.2*x[2] + 0.72;
                x[1] = 0.1*x[0] + 0.2*x[2] + 0.83;
                x[2] = 0.2*x[0] + 0.2*x[1] + 0.84;
                a4_2.append(String.format("第%3d次迭代结果:",i+1));
                for(int j=0;j<3;j++){
                    a4_2.append(String.format("x%d:%-10.6f",j+1,x[j]));
                }
                length2 = norm(x,3);
                a4_2.append("\n");
                if (Math.abs(length2- length)<epsilon){
                    a4_2.append("达到精度要求，迭代结束\n\n");
                    permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
                    return;
                }
                length = length2;
            }
            a4_2.append("迭代次数过多，仍未达到要求精度，迭代失败。\n\n");
            tempUpdateBottomInfo("迭代失败",smallErrorPNG,errorColor,3);
        }
    }
    //超松弛法
    class SOR{
        double norm(double[] k,double num){
            double sum = 0.0;
            for (int i = 0; i < num; ++i) {
                sum += k[i] * k[i];
            }
            return Math.sqrt(sum);
        }
        public void calculate(double epsilon,double omega){
            if (epsilon<=0){
                throw new IllegalArgumentException("精度必须大于0");
            }
            if (omega<1 || omega>2){
                throw new IllegalArgumentException("请输入范围为1-2的松弛因子");
            }
            a4_3.append("精度为"+epsilon+",松弛因子为"+omega+"的迭代结果为:\n");
            double x[] = new double[]{0,0,0};//解
            double length = 0;
            double length2 = 0;
            a4_3.append("第0次迭代结果:");
            a4_3.append(String.format("x0:%-10.6f",x[0]));
            for (int i = 0; i < 100; ++i) {
                x[0] = (1-omega)*x[0]+omega*(0.1*x[1] + 0.2*x[2] + 0.72);
                x[1] = (1-omega)*x[1]+omega*(0.1*x[0] + 0.2*x[2] + 0.83);
                x[2] = (1-omega)*x[2]+omega*(0.2*x[0] + 0.2*x[1] + 0.84);
                a4_3.append(String.format("第%3d次迭代结果:",i+1));
                for(int j=0;j<3;j++){
                    a4_3.append(String.format("x%d:%-10.6f",j+1,x[j]));
                }
                length2 = norm(x,3);
                a4_3.append("\n");
                if (Math.abs(length2- length)<epsilon){
                    a4_3.append("达到精度要求，迭代结束\n\n");
                    permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
                    return;
                }
                length = length2;
            }
            a4_3.append("迭代次数过多，仍未达到要求精度，迭代失败。\n\n");
            tempUpdateBottomInfo("迭代失败",smallErrorPNG,errorColor,3);
        }
    }
    //约当消去法
    class Jordan {
//        4 -1 0 -1 0 0 0
//        -1 4 -1 0 -1 0 5
//        0 -1 4 0 0 -1 0
//        -1 0 0 4 -1 0 6
//        0 -1 0 -1 4 -1 -2
//        0 0 -1 0 -1 4 6
        private List<List<Double>> coefficientMatrix;//增广矩阵
        private int n;//未知数个数
        public Jordan() {
            coefficientMatrix = new ArrayList<>();
        }
        public void inputCoefficientMatrix(int n,String matrix){
            List<List<Double>> tempCoefficientMatrix = new ArrayList<>();
            Scanner scanner = new Scanner(matrix);
            // 逐行读取输入的系数
            for (int i = 0; i < n; i++) {
                List<Double> row = new ArrayList<>();
                // 逐列读取系数
                for (int j = 0; j <= n; j++) {
                    if (scanner.hasNextDouble()) {
                        row.add(scanner.nextDouble());
                    } else {
                        throw new IllegalArgumentException("输入数据不足或过多，构建失败，请重新输入");
                    }
                }
                tempCoefficientMatrix.add(row);
            }

            // 检查是否还有多余的输入数据
            if (scanner.hasNext()) {
                throw new IllegalArgumentException("输入数据过多，请重新输入");
            }

            this.n = n;
            coefficientMatrix = new ArrayList<>(tempCoefficientMatrix);
            a4_4.append("增广矩阵输入成功,内容如下:\n");
            printCoefficientMatrix();
        }

        private void printCoefficientMatrix() {
            if (coefficientMatrix.isEmpty()){
                throw new IllegalArgumentException("请先输入增广矩阵");
            }
            for (List<Double> row : coefficientMatrix) {
                for (Double coefficient : row) {
                    a4_4.append(String.format("%-10f ",coefficient));
                }
                a4_4.append("\n");
            }
        }

        public void calculate(){
            if (coefficientMatrix.isEmpty()){
                throw new IllegalArgumentException("请先输入增广矩阵");
            }
            List<List<Double>> tempCoefficientMatrix = new ArrayList<>();
            for (List<Double> innerList : coefficientMatrix) {
                List<Double> innerCopy = new ArrayList<>(innerList);
                tempCoefficientMatrix.add(innerCopy);
            }
            a4_4.append("开始计算...\n");
            //有n-1行,n列
            for (int i = 0; i < n; i++)
            {
                //判断是否非奇异矩阵
                if (tempCoefficientMatrix.get(i).get(i) == 0) {
                    a4_4.append("该矩阵为奇异矩阵，暂时无法求解\n");
                    throw new IllegalArgumentException("求解失败");
                }
                //处理第i行一行
                for (int j = i+1; j <= n; ++j) {
                    tempCoefficientMatrix.get(i).set(j,(tempCoefficientMatrix.get(i).get(j)/tempCoefficientMatrix.get(i).get(i)));
                }
                tempCoefficientMatrix.get(i).set(i,1.0);
                //处理其他
                for (int i2 = 0; i2 < n; ++i2) {
                    if (i2 == i){
                        continue;
                    }
                    double kl = tempCoefficientMatrix.get(i2).get(i);
                    for (int j2 = i; j2 <= n; ++j2) {
                        tempCoefficientMatrix.get(i2).set(j2,(tempCoefficientMatrix.get(i2).get(j2)-tempCoefficientMatrix.get(i).get(j2)*kl));
                    }
                }
            }
            //判断是否为有唯一解，否则返回错误
            if (tempCoefficientMatrix.get(n-1).get(n-1) == 0){
                a4_4.append("该矩阵有无穷解，暂时无法求解\n");
                throw new IllegalArgumentException("求解失败");
            }
            a4_4.append("计算结束，方程组的解为:\n");
            for (int i = 0; i < n; i++) {
                a4_4.append(String.format("x%d=%f\n",i+1,tempCoefficientMatrix.get(i).get(n)));
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //高斯消去法
    class Gauss{
        //4 -1 0 -1 0 0 0
        //-1 4 -1 0 -1 0 5
        //0 -1 4 0 0 -1 0
        //-1 0 0 4 -1 0 6
        //0 -1 0 -1 4 -1 -2
        //0 0 -1 0 -1 4 6
        private List<List<Double>> coefficientMatrix;//增广矩阵
        private int n;//未知数个数
        public Gauss() {
            coefficientMatrix = new ArrayList<>();
        }
        public void inputCoefficientMatrix(int n,String matrix){
            List<List<Double>> tempCoefficientMatrix = new ArrayList<>();
            Scanner scanner = new Scanner(matrix);
            // 逐行读取输入的系数
            for (int i = 0; i < n; i++) {
                List<Double> row = new ArrayList<>();
                // 逐列读取系数
                for (int j = 0; j <= n; j++) {
                    if (scanner.hasNextDouble()) {
                        row.add(scanner.nextDouble());
                    } else {
                        throw new IllegalArgumentException("输入数据不足或过多，构建失败，请重新输入");
                    }
                }
                tempCoefficientMatrix.add(row);
            }

            // 检查是否还有多余的输入数据
            if (scanner.hasNext()) {
                throw new IllegalArgumentException("输入数据过多，请重新输入");
            }

            this.n = n;
            coefficientMatrix = new ArrayList<>(tempCoefficientMatrix);
            a4_5.append("增广矩阵输入成功,内容如下:\n");
            printCoefficientMatrix();
        }

        private void printCoefficientMatrix() {
            if (coefficientMatrix.isEmpty()){
                throw new IllegalArgumentException("请先输入增广矩阵");
            }
            for (List<Double> row : coefficientMatrix) {
                for (Double coefficient : row) {
                    a4_5.append(String.format("%-10f ",coefficient));
                }
                a4_5.append("\n");
            }
        }

        public void calculate(){
            if (coefficientMatrix.isEmpty()){
                throw new IllegalArgumentException("请先输入增广矩阵");
            }

            List<List<Double>> tempCoefficientMatrix = new ArrayList<>();
            for (List<Double> innerList : coefficientMatrix) {
                List<Double> innerCopy = new ArrayList<>(innerList);
                tempCoefficientMatrix.add(innerCopy);
            }
            a4_5.append("开始计算...\n");
            //有n-1行,n列
            for (int i = 0; i < n; i++)
            {
                //判断是否非奇异矩阵
                if (tempCoefficientMatrix.get(i).get(i) == 0) {
                    a4_5.append("该矩阵为奇异矩阵，暂时无法求解\n");
                    throw new IllegalArgumentException("求解失败");
                }
                //处理第i一行
                for (int j = i+1; j <= n; ++j) {
                    tempCoefficientMatrix.get(i).set(j,(tempCoefficientMatrix.get(i).get(j)/tempCoefficientMatrix.get(i).get(i)));
                }
                tempCoefficientMatrix.get(i).set(i,1.0);
                //处理下面每一行
                for (int i2 = i+1; i2 < n; ++i2) {
                    double kl = tempCoefficientMatrix.get(i2).get(i);
                    for (int j2 = i; j2 <= n; ++j2) {
                        tempCoefficientMatrix.get(i2).set(j2,(tempCoefficientMatrix.get(i2).get(j2)-tempCoefficientMatrix.get(i).get(j2)*kl));
                    }
                }
            }
            //判断是否为有唯一解，否则返回错误
            if (tempCoefficientMatrix.get(n-1).get(n-1) == 0){
                a4_5.append("该矩阵有无穷解，暂时无法求解\n");
                throw new IllegalArgumentException("求解失败");
            }
            //回代求解
            for (int j = n-1; j >= 0; --j) {
                for (int i = j-1; i>=0 ; --i) {
                    //处理b矩阵
                    tempCoefficientMatrix.get(i).set(n,(tempCoefficientMatrix.get(i).get(n)-(tempCoefficientMatrix.get(j).get(n)*tempCoefficientMatrix.get(i).get(j)/tempCoefficientMatrix.get(j).get(j))));
                    tempCoefficientMatrix.get(i).set(j,0.0);
                }
            }
            a4_5.append("计算结束，方程组的解为:\n");
            for (int i = 0; i < n; i++) {
                a4_5.append(String.format("x%d=%f\n",i+1,tempCoefficientMatrix.get(i).get(n)));
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }
    //列主元高斯消去
    class MainGauss{
        //        4 -1 0 -1 0 0 0
//        -1 4 -1 0 -1 0 5
//        0 -1 4 0 0 -1 0
//        -1 0 0 4 -1 0 6
//        0 -1 0 -1 4 -1 -2
//        0 0 -1 0 -1 4 6
        private List<List<Double>> coefficientMatrix;//增广矩阵
        private int n;//未知数个数
        public MainGauss() {
            coefficientMatrix = new ArrayList<>();
        }
        public void inputCoefficientMatrix(int n,String matrix){
            List<List<Double>> tempCoefficientMatrix = new ArrayList<>();
            Scanner scanner = new Scanner(matrix);
            // 逐行读取输入的系数
            for (int i = 0; i < n; i++) {
                List<Double> row = new ArrayList<>();
                // 逐列读取系数
                for (int j = 0; j <= n; j++) {
                    if (scanner.hasNextDouble()) {
                        row.add(scanner.nextDouble());
                    } else {
                        throw new IllegalArgumentException("输入数据不足或过多，构建失败，请重新输入");
                    }
                }
                tempCoefficientMatrix.add(row);
            }

            // 检查是否还有多余的输入数据
            if (scanner.hasNext()) {
                throw new IllegalArgumentException("输入数据过多，请重新输入");
            }

            this.n = n;
            coefficientMatrix = new ArrayList<>(tempCoefficientMatrix);
            a4_6.append("增广矩阵输入成功,内容如下:\n");
            printCoefficientMatrix();
        }

        private void printCoefficientMatrix() {
            if (coefficientMatrix.isEmpty()){
                throw new IllegalArgumentException("请先输入增广矩阵");
            }
            for (List<Double> row : coefficientMatrix) {
                for (Double coefficient : row) {
                    a4_6.append(String.format("%-10f ",coefficient));
                }
                a4_6.append("\n");
            }
        }

        public void calculate() {
            if (coefficientMatrix.isEmpty()){
                throw new IllegalArgumentException("请先输入增广矩阵");
            }
            List<List<Double>> tempCoefficientMatrix = new ArrayList<>();
            for (List<Double> innerList : coefficientMatrix) {
                List<Double> innerCopy = new ArrayList<>(innerList);
                tempCoefficientMatrix.add(innerCopy);
            }
            a4_6.append("开始计算...\n");
            //有n-1行,n列
            for (int i = 0; i < n; i++)
            {
                //判断是否非奇异矩阵
                if (tempCoefficientMatrix.get(i).get(i) == 0) {
                    a4_6.append("该矩阵为奇异矩阵，暂时无法求解\n");
                    throw new IllegalArgumentException("求解失败");
                }
                //处理第i行一行
                for (int j = i+1; j <= n; ++j) {
                    tempCoefficientMatrix.get(i).set(j,(tempCoefficientMatrix.get(i).get(j)/tempCoefficientMatrix.get(i).get(i)));
                }
                tempCoefficientMatrix.get(i).set(i,1.0);
                //交换
                int maxI = i+1;
                for (int i2 = maxI+1; i2 < n; ++i2) {
                    if (tempCoefficientMatrix.get(i2).get(i)>tempCoefficientMatrix.get(maxI).get(i)){
                        maxI = i2;
                    }
                }
                if (maxI!=(i+1)){
                    List<Double> tempList = tempCoefficientMatrix.get(maxI);
                    tempCoefficientMatrix.set(maxI,tempCoefficientMatrix.get(i+1));
                    tempCoefficientMatrix.set(i+1,tempList);
                }
                //处理下面每一行
                for (int i2 = i+1; i2 < n; ++i2) {
                    double kl = tempCoefficientMatrix.get(i2).get(i);
                    for (int j2 = i; j2 <= n; ++j2) {
                        tempCoefficientMatrix.get(i2).set(j2,(tempCoefficientMatrix.get(i2).get(j2)-tempCoefficientMatrix.get(i).get(j2)*kl));
                    }
                }
            }
            //判断是否为有唯一解，否则返回错误
            if (tempCoefficientMatrix.get(n-1).get(n-1) == 0){
                a4_6.append("该矩阵有无穷解，暂时无法求解\n");
                throw new IllegalArgumentException("求解失败");
            }
            //回代求解
            for (int j = n-1; j >= 0; --j) {
                for (int i = j-1; i>=0 ; --i) {
                    //处理b矩阵
                    tempCoefficientMatrix.get(i).set(n,(tempCoefficientMatrix.get(i).get(n)-(tempCoefficientMatrix.get(j).get(n)*tempCoefficientMatrix.get(i).get(j)/tempCoefficientMatrix.get(j).get(j))));
                    tempCoefficientMatrix.get(i).set(j,0.0);
                }
            }
            a4_6.append("计算结束，方程组的解为:\n");
            for (int i = 0; i < n; i++) {
                a4_6.append(String.format("x%d=%f\n",i+1,tempCoefficientMatrix.get(i).get(n)));
            }
            permanentUpdateBottomInfo("就绪",smallReadyPNG,readyColor);
        }
    }

    //////////
    //信息结构体
    class BottomInfo{
        public Icon icon;
        public String string;
        public Color color;
        public long millisecond;
        BottomInfo(Icon icon1,String s1,Color c){
            icon = icon1;
            string = s1;
            color = c;
            millisecond = 3000;
        }
        BottomInfo(Icon icon1,String s1,Color c,long second1){
            icon = icon1;
            string = s1;
            color = c;
            millisecond = second1;
        }
        BottomInfo(){
            icon = smallReadyPNG;
            string = "就绪";
            color = successColor;
            millisecond = 3000;
        }
    }
    //底部信息更新线程类
    private class RereadyBottomInfoThread extends Thread{
        @Override
        public void run() {
            while (!infoQueue.isEmpty()){
                try {
                    BottomInfo bottomInfo = infoQueue.poll();
                    if (bottomInfo.string.equals(bottomInfoLabel.getText())){
                        bottomInfoLabel.setText("  ");
                        Thread.sleep(100);
                    }
                    bottomInfoLabel.setIcon(bottomInfo.icon);
                    bottomInfoLabel.setText(bottomInfo.string);
                    bottomInfoLabel.setForeground(bottomInfo.color);
                    Thread.sleep(bottomInfo.millisecond);
                    bottomInfoLabel.setIcon(smallReadyPNG);
                    bottomInfoLabel.setText("就绪");
                    bottomInfoLabel.setForeground(readyColor);
                }catch (InterruptedException e){
                    System.out.println("底部信息睡眠中断，信息更新");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    //暂时底部更新方法
    private void tempUpdateBottomInfo(String s, Icon icon, Color color,long second){
        if (!rereadyBottomInfoThread.isAlive()){
            rereadyBottomInfoThread = new RereadyBottomInfoThread();
            infoQueue.add(new BottomInfo(icon, s,color,second*1000));
            rereadyBottomInfoThread.start();
        }else {
            infoQueue.add(new BottomInfo(icon, s,color,second*1000));
            rereadyBottomInfoThread.interrupt(); // 中断线程，让它提前醒来
        }
    }
    //永久底部更新方法
    private void permanentUpdateBottomInfo(String s, Icon icon, Color color){
        if (rereadyBottomInfoThread.isAlive()){
            rereadyBottomInfoThread.interrupt(); // 中断线程，让它提前醒来
        }
        bottomInfoLabel.setIcon(icon);
        bottomInfoLabel.setText(s);
        bottomInfoLabel.setForeground(color);
    }
    //////////

    public static void main(String[] args) {
        new Main();
    }
}