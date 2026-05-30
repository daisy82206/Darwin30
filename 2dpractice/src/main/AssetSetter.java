package main;

import object.Box;
import darwin1.player; // Import para makilala ang player class

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObjects(int stageLevel) {
        // Linisin ang listahan
        for(int i = 0; i < gp.objBox.length; i++) {
            gp.objBox[i] = null;
        }

        if (stageLevel == 1) {
            // Eto ang pinaka-consistent na alignment
            // Gagamitin natin ang static player.groundY
            int boxHeight = gp.tileSize * 2; 
            double surfaceY = player.groundY + gp.tileSize; 

            // BOX 0: Pushable sa X=500
            gp.objBox[0] = new Box(500, (int)(surfaceY - boxHeight), gp.tileSize, true);
            
            // BOX 1 & 2: Stacking sa X=1002
            // Ilalim
            gp.objBox[1] = new Box(1002, (int)(surfaceY - boxHeight), gp.tileSize, false);
            // Ibabaw
            gp.objBox[2] = new Box(1002, (int)(surfaceY - (boxHeight * 2)), gp.tileSize, false);
            gp.objBox[3] = new Box(2000, (int)(surfaceY - boxHeight), gp.tileSize, false);
            gp.objBox[4] = new Box(3400, (int)(surfaceY - boxHeight), gp.tileSize, false);
            gp.objBox[5] = new Box(3470, (int)(surfaceY - boxHeight * 2), gp.tileSize, false);
            gp.objBox[6] = new Box(3550, (int)(surfaceY - boxHeight), gp.tileSize, false);
            //2900
            gp.objBox[7] = new Box(2850, (int)(surfaceY - boxHeight), gp.tileSize, false);
            
            // ==========================================================
            // ☔ HARM OBJECT: UMBRELLA (🔥 NO LOCATION CHANGES)
            // ==========================================================
            // Pinasa natin ang 'gp' sa dulo para mag-sync sa bagong overloaded constructor natin!
            gp.objUmb[0] = new object.Umbrella(3000, 580, (int)(gp.tileSize * 1.2));
            gp.objUmb[1] = new object.Umbrella(4270, 270, gp.tileSize);
            
         // ==========================================================
            // 🪙 MGA BARYA PARA SA STAGE 1 (Coin Distribution)
            // ==========================================================
            // Nakalutang sa ere para tatalunin ni Darwin!
            
            // 70 yung sunod 
            gp.objCoin[0] = new object.Coin(gp, 1000, (int)(surfaceY - gp.tileSize * 5.5));
            gp.objCoin[1] = new object.Coin(gp, 1320, (int)(surfaceY - gp.tileSize * 6.7));
            gp.objCoin[2] = new object.Coin(gp, 1390, (int)(surfaceY - gp.tileSize * 6.7));
            gp.objCoin[3] = new object.Coin(gp, 1460, (int)(surfaceY - gp.tileSize * 6.7));
            gp.objCoin[4] = new object.Coin(gp, 1280, (int)(surfaceY - gp.tileSize * 1.5));
            gp.objCoin[5] = new object.Coin(gp, 1350, (int)(surfaceY - gp.tileSize * 1.5));
            gp.objCoin[6] = new object.Coin(gp, 1420, (int)(surfaceY - gp.tileSize * 1.5));
            
            gp.objCoin[7] = new object.Coin(gp, 1890, (int)(surfaceY - gp.tileSize * 1.5));
            gp.objCoin[8] = new object.Coin(gp, 2000, (int)(surfaceY - gp.tileSize * 3.5));

            
            gp.objCoin[9] = new object.Coin(gp, 2900, (int)(surfaceY - gp.tileSize * 4.9));
            gp.objCoin[10] = new object.Coin(gp, 2970, (int)(surfaceY - gp.tileSize * 5.8));
            gp.objCoin[11] = new object.Coin(gp, 3040, (int)(surfaceY - gp.tileSize * 5.3));
            
            gp.objCoin[12] = new object.Coin(gp, 3480, (int)(surfaceY - gp.tileSize * 5.5));
            
            gp.objCoin[13] = new object.Coin(gp, 3900, (int)(surfaceY - gp.tileSize * 6.7));
            gp.objCoin[14] = new object.Coin(gp, 3970, (int)(surfaceY - gp.tileSize * 6.7));
            
            
            gp.objCoin[15] = new object.Coin(gp, 3970, (int)(surfaceY - gp.tileSize * 1.5));
            gp.objCoin[16] = new object.Coin(gp, 4040, (int)(surfaceY - gp.tileSize * 1.5));
            gp.objCoin[17] = new object.Coin(gp, 4110, (int)(surfaceY - gp.tileSize * 1.5));
            
            gp.objCoin[18] = new object.Coin(gp, 4200, (int)(surfaceY - gp.tileSize * 9.7));
            gp.objCoin[19] = new object.Coin(gp, 4270, (int)(surfaceY - gp.tileSize * 9.4));
            gp.objCoin[20] = new object.Coin(gp, 4345, (int)(surfaceY - gp.tileSize * 8.7));
            
            gp.objCoin[21] = new object.Coin(gp, 4040, (int)(surfaceY - gp.tileSize * 6.7));
            
            
         // Sa loob ng stageLevel == 1
            for(int i = 0; i < gp.objFlag.length; i++) gp.objFlag[i] = null; // Linisin din ito
            
            // Ilagay ang flag sa pinakadulo ng mapa (Halimbawa sa X = 4000)
            gp.objFlag[0] = new object.Flag(gp, 5700, (int)(surfaceY - (gp.tileSize * 2)));
            
            
            
        }
    }
}