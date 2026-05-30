package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class MainMenu extends JFrame {

    private boolean isLoading = false; 
    private int loadingTimerCount = 0;
    private JPanel bgPanel;
    private JButton btnStart, btnExit;
    private JLabel lblTitle, lblLoading;

    public MainMenu() {
        // 1. SETUP NG JFRAME (FULL SCREEN & BORDERLESS)
        setTitle("Darwin's Adventure - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        getContentPane().setLayout(null); 
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // 2. SETUP NG BACKGROUND PANEL
        bgPanel = new JPanel() {
            Image bgImage;
            {
                try {
                    bgImage = ImageIO.read(getClass().getResourceAsStream("/background/menu_bg.png"));
                } catch(Exception e) {
                    System.out.println("⚠️ Babala: Gagamit ng fallback color dahil wala pang image.");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isLoading) {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(25, 30, 45)); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bgPanel.setBounds(0, 0, screenWidth, screenHeight);
        bgPanel.setLayout(null);
        getContentPane().add(bgPanel);

        int leftPadding = 100; 

        // 3. TITLE TEXT
        lblTitle = new JLabel("DARWIN'S ADVENTURE");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 60)); 
        lblTitle.setBounds(leftPadding, screenHeight / 4, 800, 100); 
        bgPanel.add(lblTitle);

        // 4. START GAME BUTTON
        btnStart = createAnimatedMenuButton("START GAME", leftPadding, (screenHeight / 2));
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerLoadingTransition(); 
            }
        });
        bgPanel.add(btnStart);

        // 5. EXIT BUTTON
        btnExit = createAnimatedMenuButton("EXIT", leftPadding, (screenHeight / 2) + 80);
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });
        bgPanel.add(btnExit);

        // 6. LOADING TEXT
        lblLoading = new JLabel("DARWIN IS WAKING UP...");
        lblLoading.setForeground(Color.YELLOW);
        lblLoading.setFont(new Font("SansSerif", Font.ITALIC, 30));
        lblLoading.setBounds(leftPadding, (screenHeight / 2), 600, 50);
        lblLoading.setVisible(false);
        bgPanel.add(lblLoading);
    }

    private void triggerLoadingTransition() {
        isLoading = true;
        lblTitle.setVisible(false);
        btnStart.setVisible(false);
        btnExit.setVisible(false);
        lblLoading.setVisible(true);
        bgPanel.repaint();

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadingTimerCount++;
                if (loadingTimerCount % 3 == 0) {
                    lblLoading.setVisible(!lblLoading.isVisible());
                }
                if (loadingTimerCount >= 10) {
                    ((Timer)e.getSource()).stop();
                    startGame(); 
                }
            }
        });
        timer.start();
    }

    private JButton createAnimatedMenuButton(String text, int xPos, int yPos) {
        JButton button = new JButton(text);
        Font normalFont = new Font("SansSerif", Font.PLAIN, 35);
        Font hoverFont = new Font("SansSerif", Font.BOLD, 40); 

        button.setFont(normalFont);
        button.setForeground(new Color(200, 200, 200)); 
        button.setBounds(xPos, yPos, 400, 60); 
        button.setHorizontalAlignment(SwingConstants.LEFT); 
        
        button.setOpaque(false);
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false);     
        button.setFocusPainted(false);      
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isLoading) {
                    button.setFont(hoverFont); 
                    button.setForeground(Color.WHITE); 
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isLoading) {
                    button.setFont(normalFont); 
                    button.setForeground(new Color(200, 200, 200)); 
                }
            }
        });

        return button;
    }

    private void startGame() {
        this.dispose(); 
        JFrame gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(true); 
        gameWindow.setUndecorated(true); 
        gameWindow.setTitle("Darwin's Adventure - Playing");

        GamePanel gamePanel = new GamePanel();
        gameWindow.add(gamePanel);

        gameWindow.pack();
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        gameWindow.setVisible(true);

        gamePanel.startGameThread(); 
    }

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
    }
}