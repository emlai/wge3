package wge3.entity.ground;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelements.Ground;
import wge3.world.Tile;

public final class Water extends Ground {

    public Water() {
        sprite = new Sprite(texture, 2*Tile.size, 0, Tile.size, Tile.size);
        
        movementModifier = 0.5f;
    }
}
