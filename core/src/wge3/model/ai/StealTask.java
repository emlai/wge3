/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import wge3.model.Creature;
import wge3.model.Tile;
import wge3.model.actors.Thief;
import wge3.model.objects.Item;

/**
 *
 * @author chang
 */

public class StealTask extends AITask{
    
    private Thief executor;
    private Creature target;
    private MoveTask subTask;
    
    public StealTask(Thief executor, Creature target) {
        this.executor = executor;
        this.target = target;
        
        subTask = new MoveTask(executor, target.getTileUnder());
    }
    
    @Override
    public void execute() {
        Tile targetTile = target.getTileUnder();
        if (subTask.getDestination() != targetTile && target.canBeSeenBy(executor) && targetTile.isAnOKMoveDestinationFor(executor)) {
            subTask.setDestination(targetTile);
        }
        if (!subTask.isFinished()) subTask.execute();
        
        // Steals a random item
        else if (executor.isInSameTileAs(target)) {
            Item item = target.getInventory().getRandomItem();
            executor.getInventory().addItem(item);
            target.getInventory().removeAllOfAKind(item);
        }
    }
    
    @Override
    public boolean isFinished() {
        return !executor.getInventory().getItems().isEmpty() || (subTask.isFinished() && !target.canBeSeenBy(executor));
    }
}