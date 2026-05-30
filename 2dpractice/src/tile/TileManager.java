package tile;

import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp; 
        this.tile = new Tile[10]; 
        this.mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        
        getTileImage();
        loadMap(1); 
    }

    public void getTileImage() {
        try {
            // Tile 0: Air / Void
            tile[0] = new Tile();
            tile[0].collision = false;

            // Tile 1: Buhangin
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tile/sandTiles.png"));
            tile[1].collision = true; 
            
        } catch (Exception e) {
            System.out.println("⚠️ ERROR TILE: Hindi ma-load ang tileset!");
            e.printStackTrace();
        }
    }

    public void loadMap(int stageLevel) {
        this.mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                mapTileNum[col][row] = 0;
            }
        }

        if (stageLevel == 1) {
            // Iyong atomic obstacle blueprint
            mapTileNum[20][6] = 1; 
            mapTileNum[21][6] = 1; 
            mapTileNum[22][6] = 1; 
            mapTileNum[23][6] = 1; 
            mapTileNum[24][6] = 1; 
            mapTileNum[25][6] = 1; 

            mapTileNum[59][6] = 1; 
            mapTileNum[60][6] = 1; 
            mapTileNum[61][6] = 1; 
            mapTileNum[62][6] = 1; 
            mapTileNum[63][6] = 1;
            mapTileNum[64][6] = 1;
            mapTileNum[65][6] = 1;
            mapTileNum[66][6] = 1;
            mapTileNum[67][6] = 1;
        } 
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.cameraX;
            int screenY = worldY - gp.cameraY;

            if (worldX + gp.tileSize > gp.cameraX && 
                worldX - gp.tileSize < gp.cameraX + gp.getWidth()) {
                
                if (tileNum != 0 && tile[tileNum] != null && tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
            
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}