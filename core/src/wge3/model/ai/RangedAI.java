/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import wge3.model.NonPlayer;
import wge3.model.items.Gun;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static wge3.engine.util.Math.getDistance2;

/**
 *
 * @author chang
 */
public class RangedAI extends AI {
    
    public RangedAI(NonPlayer creature) {
        super(creature);
    }
    
    @Override
    public void update() {
        if (!isAttacking()) {
            checkForEnemies();
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            currentTask = new MoveTask(NPC, getNewMovementDestination(NPC));
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }
    
    @Override
    public void checkForEnemies() {
        if (NPC.getSelectedItem().isGun()) {
            Gun gun = (Gun) NPC.getSelectedItem();
            int gunRange2 = gun.getRange() * gun.getRange();
            
            NPC.getEnemiesWithinFOV()
                    .parallelStream()
                    .filter(x -> getDistance2(NPC, x) <= gunRange2)
                    .findAny()
                    .ifPresent(x -> currentTask = new RangedAttackTask(NPC, x));
            
//            for (Creature dude : NPC.getEnemiesWithinFOV()) {
//                if (NPC.getDistance2To(dude) <= gunRange2) {
//                    currentTask = new RangedAttackTask(NPC, dude);
//                    return;
//                }
//            }
        } else {
            
            NPC.getEnemiesWithinFOV()
                    .parallelStream()
                    .findAny()
                    .ifPresent(x -> currentTask = new MeleeAttackTask(NPC, x));
            
//            for (Creature dude : NPC.getEnemiesWithinFOV()) {
//                currentTask = new MeleeAttackTask(NPC, dude);
//                return;
//            }
        }
    }
}
