/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import static wge3.engine.PlayState.mStream;
import wge3.model.Creature;
import wge3.model.objects.Item;

public final class InvisibilityPotion extends Item {

    private int duration; // seconds
    
    public InvisibilityPotion() {
        setSprite(3, 5);
        name = "invisibility potion";
        duration = 10;
    }
    
    @Override
    public void use(final Creature user) {
        mStream.addMessage("*glug*");
        user.removeItem(this);
        user.setInvisibility(true);
        new Timer().scheduleTask(new Task() {
            @Override
            public void run() {
                user.setInvisibility(false);
            }
        }, duration);
    }
}