/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.actors;

import wge3.model.NonPlayer;
import static com.badlogic.gdx.math.MathUtils.random;

public final class Zombie extends NonPlayer {
    
    public Zombie() {
        setSprite(random(9), 6);
        
        HP.setMax(random(30, 60));
        strength = random(5, 12);
        defense = random(0, 7);
        defaultSpeed = random(20, 35);
        currentSpeed = defaultSpeed;
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
    }
}
