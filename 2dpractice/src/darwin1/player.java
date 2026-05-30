package darwin1;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import object.Box; 

public class player {

    // ==========================================
    // ⚙️ SYSTEM & DEPENDENCY VARIABLES
    // ==========================================
    GamePanel gp;       
    KeyHandler keyH;    

    // ==========================================
    // 🏃‍♂️ COORD PHYSICAL GRAPHICS VARIATION
    // ==========================================
    public double worldX;          
    public double worldY;          
    public double speed;           
    public double yVelocity = 0;   
    
    // ==========================================
    // 🌍 CONSTANT WORLD ENTITIES
    // ==========================================
    public final double GRAVITY = 0.9;         
    public final double JUMP_STRENGTH = -20.0; 
    public static double groundY;              
    public boolean onGround = false;           
    
    // ==========================================
    // 📦 COLLISION & HITBOX COMPONENT
    // ==========================================
    public Rectangle solidArea;    

    // ==========================================
    // 🎨 SPRITE ANIMATION MANAGEMENT
    // ==========================================
    public BufferedImage imgStraight;
    public BufferedImage imgRight1, imgRight2, imgRight3; 
    public String direction;        
    public boolean isMoving;        
    public int spriteCounter = 0;   
    public int spriteNum = 1;       
    
    // ==========================================
    // 👑 LIFE & STATUS MANAGEMENT SYSTEM
    // ==========================================
    public int lifeCount = 1;       
    public int maxLife = 3;         
    public int life = 3;            

    // ==========================================
    // 🛡️ INVINCIBILITY FRAMES (i-FRAMES) CONFIG
    // ==========================================
    public boolean invincible = false;     
    public int invincibleCounter = 0;       
    public boolean drawing = true;
    public boolean isKnockedBack = false;   
    public String knockbackDirection = "";  
    public int knockbackTimer = 0;          
    
    private boolean isResetting = false;

    // ==========================================
    // 🛠️ CLASS CONSTRUCTOR
    // ==========================================
    public player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        
        setDefaultValues(); 
        getPlayerImage();   
    }

    public void takeDamage() {
        life--;
        if(life < 0) life = 0;
    }

    public void setDefaultValues() {
        isResetting = true; 
        
        worldX = gp.tileSize * 2; 
        
        int baseFloorY = gp.screenHeight - (int)(gp.tileSize * 0.7);
        groundY = baseFloorY - gp.tileSize; 
        worldY = groundY; 
        
        speed = 4.9; 
        
        direction = "right";
        isMoving = false;
        onGround = true;
        yVelocity = 0; 

        if (keyH != null) {
            keyH.upPressed = false;
            keyH.downPressed = false;
            keyH.leftPressed = false;
            keyH.rightPressed = false;
        }

        life = maxLife; 
        isResetting = false; 
    }
    
    public void getPlayerImage() {
        try {
            imgStraight = ImageIO.read(getClass().getResourceAsStream("/player/darwin-straight.png"));
            imgRight1   = ImageIO.read(getClass().getResourceAsStream("/player/darwin-right1.png"));
            imgRight2   = ImageIO.read(getClass().getResourceAsStream("/player/darwin-right2.png"));
            imgRight3   = ImageIO.read(getClass().getResourceAsStream("/player/darwin-right3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 🔄 CORE PHYSICS GAME LOOP TICK
    // ==========================================
    public void update() {
        if (isResetting || gp == null) {
            return;
        }

        isMoving = false;
        double oldX = worldX; 

        // ==========================================================
        // 🚀 THE MOMENTUM OVERRIDE ENGINE (REDUCED KNOCKBACK TIMPLA)
        // ==========================================================
        if (isKnockedBack == true) {
            knockbackTimer++;
            
            if (knockbackTimer == 1) {
                worldY -= 15;       
                yVelocity = -16.0;  
                onGround = false;
            }
            
            if (knockbackDirection.equals("left")) {
                worldX -= 4.0; 
            } else if (knockbackDirection.equals("right")) {
                worldX += 4.0; 
            }
            
            if (worldX < 0) worldX = 0;
            
            int spriteRenderWidth = gp.tileSize + 26; 
            int absoluteMaxX = (gp.maxWorldCol * gp.tileSize) - spriteRenderWidth;
            
            if (gp.stageSeaside != null && gp.stageSeaside.worldWidth > 0) {
                absoluteMaxX = gp.stageSeaside.worldWidth - spriteRenderWidth;
            }
            
            if (worldX > absoluteMaxX) {
                worldX = absoluteMaxX;
            }
            worldY += yVelocity;
            yVelocity += 1.0; 
            
            if (worldY >= groundY) {
                worldY = groundY;
                yVelocity = 0;
                onGround = true;
            }

            if (knockbackTimer > 18) { 
                isKnockedBack = false;
                knockbackTimer = 0;
            }
            
            return; 
        }

        // ------------------------------------------
        // 1. HORIZONTAL MOVEMENTS & BOUNDARIES
        // ------------------------------------------
        if (keyH.leftPressed == true) {
            worldX -= speed;
            direction = "left";
            isMoving = true;
        }
        else if (keyH.rightPressed == true) {
            worldX += speed;
            direction = "right";
            isMoving = true;
        }

        if (worldX < 0) worldX = 0;
        
        int spriteRenderWidth = gp.tileSize + 26; 
        int absoluteMaxX = (gp.maxWorldCol * gp.tileSize) - spriteRenderWidth;
        
        if (gp.stageSeaside != null && gp.stageSeaside.worldWidth > 0) {
            absoluteMaxX = gp.stageSeaside.worldWidth - spriteRenderWidth;
        }
        
        if (worldX > absoluteMaxX) {
            worldX = absoluteMaxX;
        }

        // ------------------------------------------
        // 2. HORIZONTAL BOX INTERSECTION BLOCK
        // ------------------------------------------
        for (int i = 0; i < gp.objBox.length; i++) {
            if (gp.objBox[i] != null) {
                Box box = gp.objBox[i];
                Rectangle playerBoundsX = new Rectangle((int)worldX + solidArea.x, (int)worldY + solidArea.y, solidArea.width, solidArea.height);
                Rectangle boxBounds = new Rectangle(box.worldX + box.solidArea.x, box.worldY + box.solidArea.y, box.width, box.height);

                if (playerBoundsX.intersects(boxBounds)) {
                    if (box.isPushable) {
                        int nextBoxX = box.worldX;
                        if ("right".equals(direction)) nextBoxX += (int)speed;
                        else if ("left".equals(direction)) nextBoxX -= (int)speed;
                        
                        Rectangle nextBoxBounds = new Rectangle(nextBoxX + box.solidArea.x, box.worldY + box.solidArea.y, box.width, box.height);
                        boolean canPush = true;
                        
                        for (int j = 0; j < gp.objBox.length; j++) {
                            if (gp.objBox[j] != null && i != j) { 
                                Box otherBox = gp.objBox[j];
                                Rectangle otherBoxBounds = new Rectangle(otherBox.worldX + otherBox.solidArea.x, otherBox.worldY + otherBox.solidArea.y, otherBox.width, otherBox.height);
                                if (nextBoxBounds.intersects(otherBoxBounds)) {
                                    canPush = false; break;
                                }
                            }
                        }
                        
                        if (canPush) {
                            box.worldX = nextBoxX;
                            if ("right".equals(direction)) worldX = oldX + (int)speed;
                            else if ("left".equals(direction)) worldX = oldX - (int)speed;
                        } else {
                            worldX = oldX; 
                        }
                    } else {
                        worldX = oldX; 
                    }
                }
            }
        }
        
        // ------------------------------------------
        // 3. HORIZONTAL SOLID TILES COLLISION
        // ------------------------------------------
        int pLeftCol   = ((int)worldX + solidArea.x) / gp.tileSize;
        int pRightCol  = ((int)worldX + solidArea.x + solidArea.width - 1) / gp.tileSize;
        int pTopRow    = ((int)worldY + solidArea.y) / gp.tileSize;
        int pBottomRow = ((int)worldY + solidArea.y + solidArea.height - 1) / gp.tileSize; 

        pLeftCol = Math.max(0, Math.min(pLeftCol, gp.maxWorldCol - 1));
        pRightCol = Math.max(0, Math.min(pRightCol, gp.maxWorldCol - 1));
        pTopRow = Math.max(0, Math.min(pTopRow, gp.maxWorldRow - 1));
        pBottomRow = Math.max(0, Math.min(pBottomRow, gp.maxWorldRow - 1));

        if (gp.tileM != null && gp.tileM.mapTileNum != null) {
            if (direction.equals("left")) {
                int tile1 = gp.tileM.mapTileNum[pLeftCol][pTopRow];    
                int tile2 = gp.tileM.mapTileNum[pLeftCol][pBottomRow]; 
                if ((gp.tileM.tile[tile1] != null && gp.tileM.tile[tile1].collision) || 
                    (gp.tileM.tile[tile2] != null && gp.tileM.tile[tile2].collision)) {
                    worldX = oldX; 
                }
            }
            if (direction.equals("right")) {
                int tile1 = gp.tileM.mapTileNum[pRightCol][pTopRow];    
                int tile2 = gp.tileM.mapTileNum[pRightCol][pBottomRow]; 
                if ((gp.tileM.tile[tile1] != null && gp.tileM.tile[tile1].collision) || 
                    (gp.tileM.tile[tile2] != null && gp.tileM.tile[tile2].collision)) {
                    worldX = oldX; 
                }
            }
        }

        // ------------------------------------------
        // 4. VERTICAL JUMP & GRAVITY PHYSICS ENGINE
        // ------------------------------------------
        if (keyH.upPressed == true && onGround == true) {
            yVelocity = JUMP_STRENGTH; 
            onGround = false; 
        }

        worldY += yVelocity; 
        yVelocity += GRAVITY; 
        onGround = false;     

        // ------------------------------------------
        // 5. VERTICAL BOX HITBOX PROJECTIONS
        // ------------------------------------------
        for (int i = 0; i < gp.objBox.length; i++) {
            if (gp.objBox[i] != null) {
                Box box = gp.objBox[i];
                Rectangle playerBoundsY = new Rectangle((int)worldX + solidArea.x, (int)worldY + solidArea.y, solidArea.width, solidArea.height);
                Rectangle boxBounds = new Rectangle(box.worldX + box.solidArea.x, box.worldY + box.solidArea.y, box.width, box.height);

                if (playerBoundsY.intersects(boxBounds)) {
                    if (yVelocity > 0) { 
                        worldY = box.worldY - solidArea.height; 
                        yVelocity = 0; 
                        onGround = true; 
                    } else if (yVelocity < 0) { 
                        worldY = box.worldY + box.height; 
                        yVelocity = 0; 
                    }
                }
            }
        }

        // ------------------------------------------
        // 6. VERTICAL SOLID TILE MATRIX DETECTOR 
        // ------------------------------------------
        int calcTopY = (int)worldY + solidArea.y;
        int calcBottomY = (int)worldY + solidArea.y + solidArea.height - 1;
        int calcLeftX = (int)worldX + solidArea.x;
        int calcRightX = (int)worldX + solidArea.x + solidArea.width - 1;

        if (calcTopY < 0) calcTopY = 0;
        if (calcBottomY < 0) calcBottomY = 0;
        if (calcLeftX < 0) calcLeftX = 0;
        if (calcRightX < 0) calcRightX = 0;

        pLeftCol   = calcLeftX / gp.tileSize;
        pRightCol  = calcRightX / gp.tileSize;
        pTopRow    = calcTopY / gp.tileSize;
        pBottomRow = calcBottomY / gp.tileSize;

        pTopRow    = Math.max(0, Math.min(pTopRow, gp.maxWorldRow - 1));
        pBottomRow = Math.max(0, Math.min(pBottomRow, gp.maxWorldRow - 1));
        pLeftCol   = Math.max(0, Math.min(pLeftCol, gp.maxWorldCol - 1));
        pRightCol  = Math.max(0, Math.min(pRightCol, gp.maxWorldCol - 1));

        if (gp.tileM != null && gp.tileM.mapTileNum != null) {
            if (yVelocity > 0) { 
                int tile1 = gp.tileM.mapTileNum[pLeftCol][pBottomRow];
                int tile2 = gp.tileM.mapTileNum[pRightCol][pBottomRow];
                
                if ((gp.tileM.tile[tile1] != null && gp.tileM.tile[tile1].collision) || 
                    (gp.tileM.tile[tile2] != null && gp.tileM.tile[tile2].collision)) {
                    worldY = (pBottomRow * gp.tileSize) - (solidArea.y + solidArea.height);
                    yVelocity = 0;
                    onGround = true; 
                }
            }
            else if (yVelocity < 0) { 
                int tile1 = gp.tileM.mapTileNum[pLeftCol][pTopRow];
                int tile2 = gp.tileM.mapTileNum[pRightCol][pTopRow];
                
                if ((gp.tileM.tile[tile1] != null && gp.tileM.tile[tile1].collision) || 
                    (gp.tileM.tile[tile2] != null && gp.tileM.tile[tile2].collision)) {
                    worldY = (pTopRow * gp.tileSize) + gp.tileSize - solidArea.y;
                    yVelocity = 0;
                }
            }
        }
	
        if (worldY >= groundY) {
            worldY = groundY;
            yVelocity = 0;
            onGround = true; 
        }

        // ------------------------------------------
        // 7. ANIMATION TICK COUNTER
        // ------------------------------------------
        if (isMoving == true) {
            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1) spriteNum = 2;
                else if (spriteNum == 2) spriteNum = 3;
                else if (spriteNum == 3) spriteNum = 1;
                spriteCounter = 0;
            }
        }
        
        // ------------------------------------------
        // 8. INVINCIBILITY FRAMES TICK FLICKER
        // ------------------------------------------
        if (invincible == true) {
            invincibleCounter++;
            
            if (invincibleCounter % 5 == 0) {
                drawing = !drawing; 
            }
            
            if (invincibleCounter > 150) {
                invincible = false;
                drawing = true; 
                invincibleCounter = 0;
            }
        }

        // ==========================================
        // 9. ☔ SONIC-STYLE SOLID HARD UMBRELLA MATRIX 
        // ==========================================
        for (int i = 0; i < gp.objUmb.length; i++) {
            if (gp.objUmb[i] != null) {
                object.Umbrella umb = gp.objUmb[i];
                
                Rectangle playerBounds = new Rectangle(
                    (int)worldX + solidArea.x, 
                    (int)worldY + solidArea.y, 
                    solidArea.width, 
                    solidArea.height
                );
                
                Rectangle umbBounds = new Rectangle(
                    umb.worldX + umb.solidArea.x, 
                    umb.worldY + umb.solidArea.y, 
                    umb.solidArea.width, 
                    umb.solidArea.height
                );

                if (playerBounds.intersects(umbBounds)) {
                    
                    if (yVelocity > 0 && worldY + solidArea.y + solidArea.height - yVelocity <= umb.worldY + umb.solidArea.y + 15) {
                        worldY = umb.worldY + umb.solidArea.y - (solidArea.y + solidArea.height); 
                        yVelocity = 0;
                        onGround = true; 
                    } else if (yVelocity < 0) {
                        worldY = umb.worldY + umb.solidArea.y + umb.solidArea.height;
                        yVelocity = 0;
                    } else {
                        worldX = oldX; 
                    }
                    
                    if (invincible == false) {
                        life--; 
                        
                        this.isKnockedBack = true;
                        if ((worldX + (solidArea.width / 2)) < (umb.worldX + (umb.solidArea.width / 2))) {
                            this.knockbackDirection = "left"; 
                        } else {
                            this.knockbackDirection = "right"; 
                        }

                        if (life <= 0) {
                            lifeCount--; 
                            if (lifeCount > 0) {
                                isResetting = true;
                                setDefaultValues(); 
                                this.invincible = false;
                                this.invincibleCounter = 0;
                                this.drawing = true; 
                                this.isKnockedBack = false;
                                return;
                            } else {
                                isResetting = true;
                                System.out.println("🚨 GAME OVER: Bumabalik sa WindowBuilder...");
                                gp.setVisible(false); 
                                resetAll(); 
                                return;
                            }
                        } else {
                            invincible = true; 
                        }
                    }
                }
            }
        } // DITO NAGTATAPOS ANG UMBRELLA LOOP!

        // ==========================================
        // 10. 🪙 THE LIGHTWEIGHT COIN COLLECTION MATRIX
        // ==========================================
        // ✨ LIGTAS AT NASA LABAS NA NG UMBRELLA LOOP!
        for (int i = 0; i < gp.objCoin.length; i++) {
            if (gp.objCoin[i] != null && !gp.objCoin[i].shouldRemove) {
                object.Coin coin = gp.objCoin[i];
                
                Rectangle playerBounds = new Rectangle(
                    (int)worldX + solidArea.x, 
                    (int)worldY + solidArea.y, 
                    solidArea.width, 
                    solidArea.height
                );
                
                Rectangle coinBounds = new Rectangle(
                    coin.worldX + coin.solidArea.x, 
                    coin.worldY + coin.solidArea.y, 
                    coin.solidArea.width, 
                    coin.solidArea.height
                );

                if (playerBounds.intersects(coinBounds)) {
                    if (coin.shouldRemove == false && coinBounds.width > 0) {
                        gp.currentStageScore += 10; 
                        coin.collect(); 
                    }
                    
                }
            }
        }

     // ==========================================
        // 11. 🏁 FINISH LINE COLLISION DETECTOR
        // ==========================================
        for (int i = 0; i < gp.objFlag.length; i++) {
            if (gp.objFlag[i] != null) {
                object.Flag flag = gp.objFlag[i];
                
                Rectangle playerBounds = new Rectangle((int)worldX + solidArea.x, (int)worldY + solidArea.y, solidArea.width, solidArea.height);
                Rectangle flagBounds = new Rectangle(flag.worldX + flag.solidArea.x, flag.worldY + flag.solidArea.y, flag.solidArea.width, flag.solidArea.height);

                if (playerBounds.intersects(flagBounds)) {
                    // ✨ KAPAG NAKUHA ANG FLAG, IPAPAKITA ANG MENU!
                    gp.gameState = gp.finishState; 
                }
            }
        }
    } // DITO NAGTATAPOS ANG BUONG UPDATE() METHOD!

    // ==========================================
    // 🎨 RENDER DRAW COMPONENT GRAPHICS
    // ==========================================
    public void draw(Graphics2D g2) {
        if (drawing == false || isResetting) {
            return;
        }
        
        BufferedImage image = null;
        
        if (isMoving == false) {
            image = imgStraight;
        } else {
            if (spriteNum == 1) image = imgRight1;
            if (spriteNum == 2) image = imgRight2;
            if (spriteNum == 3) image = imgRight3;
        }

        int screenX = (int)worldX - gp.cameraX;
        int screenY = (int)worldY - gp.cameraY;

        if (image != null) {
            int width = gp.tileSize + 26; 
            int height = gp.tileSize;     

            if ("left".equals(direction)) {
                g2.drawImage(image, screenX + width, screenY, -width, height, null);
            } else {
                g2.drawImage(image, screenX, screenY, width, height, null);
            }
        }
    }

    public void damagePlayer() {
        if (invincible == false) {
            life--; 
            invincible = true;
            
            if ("right".equals(direction)) {
                worldX -= (gp.tileSize * 1.5); 
            } else if ("left".equals(direction)) {
                worldX += (gp.tileSize * 1.5); 
            }
            
            yVelocity = -8.0; 
            onGround = false;
            
            if (life < 0) life = 0; 
        }
    }

    public void resetAll() {
        isResetting = true;
        this.lifeCount = 3;
        setDefaultValues(); 
        this.invincible = false;
        this.invincibleCounter = 0;
        this.drawing = true;
        this.isKnockedBack = false;
        this.knockbackTimer = 0;

        if (gp != null && gp.keyH != null) {
            gp.keyH.upPressed = false;
            gp.keyH.downPressed = false;
            gp.keyH.leftPressed = false;
            gp.keyH.rightPressed = false;
        }
        isResetting = false;
    }
}