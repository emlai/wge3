package wge3.entity.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.character.Bomb;
import static wge3.game.gamestates.PlayState.mStream;
import wge3.world.Tile;

public final class FusedBomb extends Bomb {
    
    private int damage;
    
    public FusedBomb() {
        sprite = new Sprite(texture, 0, 2*Tile.size, Tile.size, Tile.size);
        
        range = 2;
        damage = 100;
        time = 2;
    }

    @Override
    public void explode() {
        mStream.addMessage("*EXPLOSION*");
        
        float x = this.getX();
        float y = this.getY();
        int range = this.getRange();
        for (Tile currentTile : area.getTiles()) {
            if (currentTile.canBeSeenFrom(x, y, range)) {
                float distance = currentTile.getDistanceTo(x, y) / Tile.size;
                float intensity = 1f - Math.max(distance-1f, 0f) * (1f / range);
                // intensity = 1, when distance = [0,1].
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
        area.removeBullet(this);
    }
}
