package environment;

import main.GamePanel;

public class Stage1Seaside extends Stage {

    public Stage1Seaside(GamePanel gp) {
        super(gp); 
        
        // Saktong 69 tiles para sa texture line nung sand at ocean
        gp.maxWorldCol = 69; 
        
        setStaticBackground("/background/stage1/bg_static.png"); 
        setMovingBackground("/background/stage1/sand_and_ocean.png"); 
    }
}