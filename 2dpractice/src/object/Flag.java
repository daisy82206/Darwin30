package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Flag {
    GamePanel gp;
    public int worldX, worldY;
    public int width, height;
    public Rectangle solidArea;
    public static BufferedImage image;

    public Flag(GamePanel gp, int worldX, int worldY) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;

        // Sukat ng bandila (Halimbawa: Kasing-tangkad ng 2 tiles)
        this.width = gp.tileSize;
        this.height = gp.tileSize * 2; 
        this.solidArea = new Rectangle(0, 0, width, height);

        if (image == null) {
            getFlagImage();
        }
    }

    public void getFlagImage() {
        try {
            // Tiyaking nandoon ang flag.png mo sa folder na ito!
            image = ImageIO.read(getClass().getResourceAsStream("/object/stage1/flag.png"));
        } catch (Exception e) {
            System.out.println("⚠️ ERROR: Hindi mahanap ang flag.png!");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (image == null) return;
        int screenX = worldX - gp.cameraX;
        int screenY = worldY - gp.cameraY;
        g2.drawImage(image, screenX, screenY, width, height, null);
    }
}