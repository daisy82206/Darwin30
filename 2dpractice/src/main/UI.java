package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class UI {
    GamePanel gp;
    BufferedImage heart_full, heart_blank, darwin_head;
    Font marfont;
    public int commandNum = 0; // 0 para sa Continue, 1 para sa Exit

    public UI(GamePanel gp) {
        this.gp = gp;
        this.marfont = new Font("Arial", Font.BOLD, 30);
        loadImages();
    }

    public void loadImages() {
        try {
            darwin_head = ImageIO.read(getClass().getResourceAsStream("/ui/darwinHead.png"));
            heart_full = ImageIO.read(getClass().getResourceAsStream("/ui/heart.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/ui/heart.png"));
        } catch (Exception e) {
            System.out.println("⚠️ Babala: May nawawalang UI image sa res folder!");
        }
    }
    
    // ✨ MASTER DRAW METHOD
    public void draw(Graphics2D g2) {
        if (gp.gameState == gp.titleState) {
            drawTitleScreen(g2);
        }
        else if (gp.gameState == gp.playState) {
            drawPlayScreenHUD(g2); 
        } 
        else if (gp.gameState == gp.finishState) {
            drawFinishScreen(g2);
        }
    }

    // ==========================================
    // ✨ 1. IN-GAME HUD (Buhay at Score)
    // ==========================================
    public void drawPlayScreenHUD(Graphics2D g2) {
        if (gp.player1 == null) return;
        
        int x = 20; 
        int y = 20; 
        
        // DRAW DARWIN HEAD & LIFE COUNT
        if (darwin_head != null) {
            g2.drawImage(darwin_head, x, y, gp.tileSize, gp.tileSize, null);
        }
        
        g2.setFont(marfont);
        g2.setColor(Color.WHITE);
        g2.drawString("x " + gp.player1.lifeCount, x + gp.tileSize + 15, y + gp.tileSize - 15);

        // DRAW HEARTS
        int heartX = x;
        int heartY = y + gp.tileSize + 10; 
        
        for (int i = 0; i < gp.player1.maxLife; i++) {
            if (i < gp.player1.life) {
                if (heart_full != null) {
                    if (!(gp.player1.invincible == true && gp.player1.drawing == false)) {
                        g2.drawImage(heart_full, heartX, heartY, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
            heartX += gp.tileSize + 5; 
        }

        // 5-DIGIT ARCADE SCORE HUD
        int totalDisplayScore = gp.accumulatedTotalScore + gp.currentStageScore;
        String scoreText = String.format("%05d", totalDisplayScore);
        
        g2.setFont(marfont.deriveFont(Font.BOLD, 45f)); 
        g2.setColor(Color.YELLOW); 
        
        // ✨ LIGTAS NA BUFFER BOUNDS: Ginamit ang gp.screenWidth (1024) imbes na gp.getWidth()
        int textWidth = g2.getFontMetrics().stringWidth(scoreText);
        int scoreX = gp.screenWidth - textWidth - 20; 
        int scoreY = y + gp.tileSize - 10; 
        
        g2.drawString(scoreText, scoreX, scoreY);
    }

    // ==========================================
    // ✨ 2. FINISH LINE MENU PANEL (FIXED)
    // ==========================================
    public void drawFinishScreen(Graphics2D g2) {
        // ✨ FIXED: Nakakulong na sa 1024x768 virtual screen ang panel kaya hindi na mag-gi-glitch pabaligtad!
        g2.setColor(new Color(0, 0, 0, 200)); 
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(marfont.deriveFont(Font.BOLD, 60f));
        g2.setColor(Color.WHITE);

        String text = "STAGE CLEAR!";
        int x = getXforCenteredText(g2, text);
        int y = gp.screenHeight / 3;
        g2.drawString(text, x, y);

        g2.setFont(marfont.deriveFont(Font.BOLD, 40f));
        g2.setColor(Color.YELLOW);
        int totalScore = gp.accumulatedTotalScore + gp.currentStageScore;
        String scoreText = "FINAL SCORE: " + String.format("%05d", totalScore);
        x = getXforCenteredText(g2, scoreText);
        y += gp.tileSize;
        g2.drawString(scoreText, x, y);

        // MENU PAGPIPILIAN
        g2.setFont(marfont.deriveFont(Font.BOLD, 35f));
        g2.setColor(Color.WHITE);
        
        text = "CONTINUE TO NEXT STAGE";
        x = getXforCenteredText(g2, text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if (commandNum == 0) g2.drawString(">", x - gp.tileSize, y);

        text = "EXIT TO MAIN MENU";
        x = getXforCenteredText(g2, text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) g2.drawString(">", x - gp.tileSize, y);
    }

    public void drawTitleScreen(Graphics2D g2) {
        g2.setColor(new Color(30, 40, 60));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(marfont.deriveFont(Font.BOLD, 50f));
        g2.setColor(Color.WHITE);
        
        String titleText = "DARWIN'S ADVENTURE";
        int titleX = getXforCenteredText(g2, titleText);
        int titleY = gp.screenHeight / 3;
        g2.drawString(titleText, titleX, titleY);

        g2.setFont(marfont.deriveFont(Font.PLAIN, 25f));
        g2.setColor(Color.YELLOW);
        String instruction = "PRESS ENTER TO START NEW GAME";
        int instX = getXforCenteredText(g2, instruction);
        g2.drawString(instruction, instX, titleY + (gp.tileSize * 2));
    }

    // Helper Method para laging sentro ang teksto
    public int getXforCenteredText(Graphics2D g2, String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth / 2) - (length / 2);
    }
}