package hannoi;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class UI extends JFrame{
    
    static JPanel MainPanel,ButtonPanel,OpPanel;
    static JLabel Steps;
    static JButton Set,Solve,Clear;
    static JComboBox Diff;
    static Font font = new Font("Times New Roman",Font.BOLD,20);
    static int[][] mmp = new int[3][20];
    static int[] Len = new int[3];
    static int nowPage;
    static Graphics2D G;
    static Hanoi hanoi;
    static JButton prev,next;
    public UI(){
        this.setTitle("Tower of Hanoi");
        
        ButtonPanel = new JPanel(null);
        ButtonPanel.setBackground(Color.BLACK);
        ButtonPanel.setPreferredSize(new Dimension(800,50));
        this.add(ButtonPanel,BorderLayout.NORTH);
        
        Set = new JButton("Set");
        Set.setBounds(100,5,150,40);
        Set.setFont(font);
        Set.setEnabled(true);
        Set.setMargin(new Insets(0,0,0,0));
        Set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hanoi = new Hanoi(Diff.getSelectedIndex() + 1);
                G.setColor(Color.WHITE);
                G.fillRect(0, 0, 800, 600);
                Diff.setEnabled(false);
                nowPage = 0;
                Steps.setText("Step  " + String.valueOf(nowPage));
                Draw(0,G);
            }
        });
        ButtonPanel.add(Set);
        
        Diff = new JComboBox();
        for(int i = 1;i <= 6;i ++){Diff.addItem(i);}
        Diff.setBounds(250,5,50,40);
        Diff.setFont(font);
        ButtonPanel.add(Diff);

        Clear = new JButton("Clear");
        Clear.setBounds(550,5,150,40);
        Clear.setFont(font);
        Clear.setEnabled(true);
        Clear.setMargin(new Insets(0,0,0,0));
        Clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Diff.setEnabled(true);
                hanoi = null;
                Set.setEnabled(true);
                Solve.setEnabled(true);
                G.setColor(Color.WHITE);
                G.fillRect(0, 0, 800, 600);
                Steps.setText("Step  Null");
                next.setEnabled(false);
                prev.setEnabled(false);
            }
        });
        ButtonPanel.add(Clear);
        
        Solve = new JButton("Solve");
        Solve.setBounds(350,5,150,40);
        Solve.setFont(font);
        Solve.setEnabled(true);
        Solve.setMargin(new Insets(0,0,0,0));
        Solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(hanoi == null) {return ;}
                hanoi.Solve();
                Set.setEnabled(false);
                Solve.setEnabled(false);
                next.setEnabled(true);
                prev.setEnabled(true);
            }
        });
        ButtonPanel.add(Solve);
        
        OpPanel = new JPanel(null);
        OpPanel.setBackground(Color.BLACK);
        OpPanel.setPreferredSize(new Dimension(800,50));
        this.add(OpPanel,BorderLayout.SOUTH);
        
        prev = new JButton("<<");
        prev.setBounds(100,5,150,40);
        prev.setFont(font);
        prev.setEnabled(false);
        prev.setMargin(new Insets(0,0,0,0));
        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nowPage - 1 < 0){return ;}
                G.setColor(Color.WHITE);
                G.fillRect(0, 0, 800, 600);
                nowPage --;
                Steps.setText("Step  " + String.valueOf(nowPage));
                Draw(nowPage,G);
            }
        });
        OpPanel.add(prev);
        
        next = new JButton(">>");
        next.setBounds(550,5,150,40);
        next.setFont(font);
        next.setEnabled(false);
        next.setMargin(new Insets(0,0,0,0));
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nowPage + 1 > hanoi.maxStep){return ;}
                G.setColor(Color.WHITE);
                G.fillRect(0, 0, 800, 600);
                nowPage ++;
                Steps.setText("Step  " + String.valueOf(nowPage));
                Draw(nowPage,G);
            }
        });
        OpPanel.add(next);
        
        Steps = new JLabel("Step  Null",JLabel.CENTER);
        Steps.setBounds(300,5,150,40);
        Steps.setFont(new Font("Times New Roman",Font.BOLD,25));
        Steps.setForeground(Color.WHITE);
        OpPanel.add(Steps);
        
        MainPanel = new JPanel();
        MainPanel.setBackground(Color.WHITE);
        this.add(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        Dimension winSize = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((winSize.width - this.getWidth()) / 2,(winSize.height - this.getHeight()) / 2);
        this.setResizable(false);
        this.setVisible(true);
        G = (Graphics2D)MainPanel.getGraphics();
    }
    
    public void Draw(int x,Graphics G) {
        G.setColor(Color.DARK_GRAY);
        G.fillRect(50, 350, 200, 30);
        G.fillRect(300, 350, 200, 30);
        G.fillRect(550, 350, 200, 30);
        G.setColor(Color.BLUE);
        for(int i = 0;i < hanoi.ans[x][0].size();i ++) {
            int s = hanoi.ans[x][0].get(i);
            G.drawRect(150 - 15 * s, 320 - 30 * i, 30 * s, 30);
        }
        for(int i = 0;i < hanoi.ans[x][1].size();i ++) {
            int s = hanoi.ans[x][1].get(i);
            G.drawRect(400 - 15 * s, 320 - 30 * i, 30 * s, 30);
        }
        for(int i = 0;i < hanoi.ans[x][2].size();i ++) {
            int s = hanoi.ans[x][2].get(i);
            G.drawRect(650 - 15 * s, 320 - 30 * i, 30 * s, 30);
        }
    }
    
    public static void main(String[] args) {
        new UI();
    }

}