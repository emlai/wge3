package wge3.game.entity.items.potions;

import com.badlogic.gdx.utils.Timer;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class LevitationPotion extends Item {
    
    private int duration; // seconds

    public LevitationPotion() {
        setSprite(1, 5);
        name = "levitation potion";
        duration = 10;
    }

    @Override
    public void use(final Creature user) {
        mStream.addMessage("*glug*");
        user.removeItem(this);
        user.setFlying(true);
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                    user.setFlying(false);
            }
        }, duration);
    }
}
