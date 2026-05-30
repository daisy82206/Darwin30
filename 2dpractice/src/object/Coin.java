package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Coin {

    GamePanel gp;
    public int worldX, worldY;
    public int width, height;
    public Rectangle solidArea;
    
    // States ng barya
    public boolean shouldRemove = false; 
    private int state = 0;             // 0: Static sa lapag, 1: Nakuha na (Pop sequence)
    private int currentFrame = 0;      
    private int frameCounter = 0;
    private final int FRAME_SPEED = 5; // Bilis ng ikot

    // ✨ ORGANISADONG ARRAY (Kaparehas ng structure ng TileManager mo!)
    // Naka-static para isang beses lang i-load ng memory kahit 100 barya pa ilagay mo.
    public static BufferedImage[] image;

    public Coin(GamePanel gp, int worldX, int worldY) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
     // Mas pinalaking barya (Halimbawa: 85% ng isang tile)
        this.width = (int)(gp.tileSize * 1.8);
        this.height = (int)(gp.tileSize * 1.8);
        this.solidArea = new Rectangle(0, 0, width, height);

        // I-load ang mga pictures kapag wala pang laman ang array
        if (image == null) {
            image = new BufferedImage[4];
            getCoinImage();
        }
    }

    // ✨ PARANG TILE MANAGER: Dito mo madaling makikita at ma-e-edit ang 4 frames!
    public void getCoinImage() {
        try {
            image[0] = ImageIO.read(getClass().getResourceAsStream("/object/stage1/coin3.png"));
            image[1] = ImageIO.read(getClass().getResourceAsStream("/object/stage1/sparkleAnimation1.png"));
            image[2] = ImageIO.read(getClass().getResourceAsStream("/object/stage1/sparkleAnimation2.png"));
            image[3] = ImageIO.read(getClass().getResourceAsStream("/object/stage1/sparkleAnimation3.png"));
        } catch (Exception e) {
            System.out.println("⚠️ ERROR COIN: Siyasatin ang mga pangalan ng coin png files mo!");
            e.printStackTrace();
        }
    }

    // Tinatawag kapag nasalo ni Darwin
    public void collect() {
        if (state == 0) {
            state = 1;         
            currentFrame = 1;  
            frameCounter = 0;
        }
    }

    public void update() {
        if (shouldRemove) return;

        if (state == 0) {
            currentFrame = 0; // Naka-lock sa static frame
            return;
        }

        if (state == 1) { // Nag-a-animate ng pop frames
            frameCounter++;
            if (frameCounter >= FRAME_SPEED) {
                currentFrame++;
                frameCounter = 0;
                
                // Kapag tapos na ang frame 3, burahin na sa screen
                if (currentFrame >= 4) {
                    shouldRemove = true; 
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (shouldRemove || image == null || image[currentFrame] == null) return;

        int screenX = worldX - gp.cameraX;
        int screenY = worldY - gp.cameraY;
        
        g2.drawImage(image[currentFrame], screenX, screenY, width, height, null);
    }
}