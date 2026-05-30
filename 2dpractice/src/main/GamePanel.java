package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import environment.Stage1Seaside;
import darwin1.player;
import object.Box;
import tile.TileManager; 

public class GamePanel extends JPanel implements Runnable {

    // ==========================================
    // ⚙️ VIRTUAL RESOLUTION LOGIC (Saktong 1024x768)
    // ==========================================
    public final int originalTileSize = 16; 
    public final int scale = 4;
    public final int tileSize = originalTileSize * scale; 
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12; 
    
    // Ito ang saktong sukat ng laro natin (Walang gagalawin dito)
    public final int screenWidth = tileSize * maxScreenCol; // 1024
    public final int screenHeight = tileSize * maxScreenRow; // 768

    public int maxWorldCol = 69; 
    public final int maxWorldRow = 12;  

    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);
    
    public Stage1Seaside stageSeaside;
    public TileManager tileM; 
    public player player1; 
    public UI ui = new UI(this);
    
    public Box objBox[] = new Box[10]; 
    public object.Umbrella objUmb[] = new object.Umbrella[10];
    public object.Coin objCoin[] = new object.Coin[50]; 
    public object.Flag objFlag[] = new object.Flag[5]; 
    
    public int currentStageScore = 0;     
    public int accumulatedTotalScore = 0; 
    
    public AssetSetter aSetter = new AssetSetter(this);

    public int cameraX = 0;
    public int cameraY = 0; 
    
    public int gameState;
    public final int titleState = 0; 
    public final int playState = 1;  
    public final int finishState = 2; 
    
    public GamePanel() {
        // Hahayaan natin ang JFrame na mag-decide ng totoong physical size (Full Screen)
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // Ito ang pipigil sa pagkurap (flashing)!
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        gameState = playState; 
        
        this.stageSeaside = new Stage1Seaside(this);
        this.tileM = new TileManager(this); 
        this.player1 = new player(this, keyH); 
        
        aSetter.setObjects(1); 
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint(); // Diretsong i-drawing sa screen
                delta--;
            }
        }
    }
    
    public void update() {
        if (gameState == finishState) {
            return; 
        }

        if (player1 != null) {
            player1.update();
        }
        
        for (int i = 0; i < objCoin.length; i++) {
            if (objCoin[i] != null) {
                objCoin[i].update();
            }
        }
        
        if (player1 != null && player1.worldX > (screenWidth / 3)) {
            cameraX = (int)player1.worldX - (screenWidth / 3);
        } else {
            cameraX = 0;
        }
        
        if (cameraX < 0) cameraX = 0;
        
        if (stageSeaside != null && stageSeaside.worldWidth > 0) {
            if (cameraX > stageSeaside.worldWidth - screenWidth) {
                cameraX = stageSeaside.worldWidth - screenWidth;
            }
        }
        cameraY = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        // ==========================================
        // ✨ THE ULTIMATE ANTI-GLITCH SCALER
        // ==========================================
        // Kinakalkula nito kung gaano kalaki ang monitor mo kumpara sa 1024x768.
        // Tapos, gagamitin ang "g2.scale" para i-stretch nang perpekto ang buong laro!
        double scaleX = (double) getWidth() / screenWidth;
        double scaleY = (double) getHeight() / screenHeight;
        g2.scale(scaleX, scaleY);
        
        // Dahil naka-scale na sa itaas, normal na lang nating iguguhit ang lahat.
        // Wala nang black bars, at lapat na lapat sa screen!
        if (stageSeaside != null) stageSeaside.draw(g2); 
        if (tileM != null) tileM.draw(g2);

        for (int i = 0; i < objFlag.length; i++) { if (objFlag[i] != null) objFlag[i].draw(g2); }
        for (int i = 0; i < objBox.length; i++) { if (objBox[i] != null) objBox[i].draw(g2, this); }
        for (int i = 0; i < objCoin.length; i++) { if (objCoin[i] != null) objCoin[i].draw(g2); }
        for (int i = 0; i < objUmb.length; i++) { if (objUmb[i] != null) objUmb[i].draw(g2, this); }

        if (player1 != null) player1.draw(g2);
        if (ui != null) ui.draw(g2);

        g2.dispose();
    }
}