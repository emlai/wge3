package wge3.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.LinkedList;
import java.util.List;
import wge3.game.entity.bombs.Bomb;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.grounds.OneWayFloor;
import wge3.game.entity.tilelayers.mapobjects.Tree;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.tilelayers.MapObject;
import wge3.game.engine.gui.Drawable;
import static wge3.game.entity.Area.floatPosToTilePos;

public class Tile implements Drawable {
    
    public static final int size = 24;
    
    private Area area;
    private int x, y; // in tiles
    private Rectangle bounds;
    
    private Ground ground;
    private MapObject object;

    public Tile() {
        bounds = new Rectangle();
        bounds.width = Tile.size;
        bounds.height = Tile.size;
    }

    public Ground getGround() {
        return ground;
    }

    public MapObject getObject() {
        return object;
    }
    
    public void setGround(Ground g) {
        g.setTile(this);
        g.setPosition(x * Tile.size, y * Tile.size);
        ground = g;
    }

    public void setObject(MapObject o) {
        o.setTile(this);
        o.setPosition(x * Tile.size, y * Tile.size);
        object = o;
    }

    public void removeObject() {
        object = null;
    }

    public boolean isPassable() {
        if (object == null) {
            return true;
        } else {
            return object.isPassable();
        }
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        bounds.x = x * Tile.size;
        bounds.y = y * Tile.size;
        ground.setPosition(x * Tile.size, y * Tile.size);
        if (hasObject()) {
            object.setPosition(x * Tile.size, y * Tile.size);
        }
    }

    public void setLighting(Color color) {
        ground.setLighting(color);
        if (hasObject()) object.setLighting(color);
        for (Creature creature : getCreatures()) {
            creature.setLighting(color);
        }
        for (Bomb bomb : getBombs()) {
            bomb.setLighting(color);
        }
    }
    
    public float getMovementModifier() {
        if (!hasObject()) {
            return ground.getMovementModifier();
        } else {
            return ground.getMovementModifier() * object.getMovementModifier();
        }
    }
    
    public boolean blocksVision() {
        if (object != null) {
            return object.blocksVision();
        } else {
            return false;
        }
    }
    
    @Override
    public void draw(Batch batch) {
        if (!hasObject() || object.isTree()) {
            ground.draw(batch);
        } else if (object.coversWholeTile()) {
            object.draw(batch);
        } else {
            ground.draw(batch);
            batch.enableBlending();
            object.draw(batch);
            batch.disableBlending();
        }
    }
    
    public boolean canBeSeenBy(Creature creature) {
        return canBeSeenFrom(creature.getX(), creature.getY(), creature.getSight()) || creature.canSeeEverything();
    }
    
    public boolean canBeSeenFrom(float x, float y, int sight) {
        if (!getArea().hasLocation(x, y)) throw new IllegalArgumentException();
        
        if (getDistanceTo(x, y) > sight * Tile.size) return false;
        
        for (Tile tile : area.getTilesOnLine(x, y, getMiddleX(), getMiddleY())) {
            if (tile.blocksVision()) return false;
        }
        
        return true;
    }
    
    // Calculates distance to middlepoint of tile
    public float getDistanceTo(float x, float y) {
        float dx = x - this.getMiddleX();
        float dy = y - this.getMiddleY();
        return (float) Math.sqrt(dx*dx + dy*dy);
    }

    public boolean hasObject() {
        return object != null;
    }

    public boolean hasItem() {
        if (object == null) return false;
        return object.isItem();
    }

    public boolean hasCreature() {
        return !getCreatures().isEmpty();
    }

    public List<Creature> getCreatures() {
        List<Creature> creatures = new LinkedList<Creature>();
        for (Creature creature : area.getCreatures()) {
            if (floatPosToTilePos(creature.getX()) == this.getX()
                    && floatPosToTilePos(creature.getY()) == this.getY()) {
                creatures.add(creature);
            }
        }
        return creatures;
    }
    
    public List<Bomb> getBombs() {
        List<Bomb> bombs = new LinkedList<Bomb>();
        for (Bomb bomb : area.getBombs()) {
            if (area.getTileAt(bomb.getX(), bomb.getY()).equals(this)) {
                bombs.add(bomb);
            }
        }
        return bombs;
    }

    public void dealDamage(int amount) {
        for (Creature creature : getCreatures()) {
            creature.dealDamage(amount);
            if (creature.isDead()) {
                creature = null;
            }
        }
        if (hasItem()) {
            object = null;
        } else if (hasObject()) {
            object.dealDamage(amount);
            if (object.isDestroyed() & !object.hasDestroyedSprite()) {
                object = null;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    public boolean drainsHP() {
        if (object == null) {
            return ground.drainsHP();
        } else {
            return object.drainsHP();
        }
    }
    
    public int getHPDrainAmount() {
        if (object == null) {
            return ground.getHPDrainAmount();
        } else {
            return object.getHPDrainAmount();
        }
    }
    
    public void requestDraw() {
        area.addTileToDraw(this);
        if (hasTree())
            area.addTreeToDraw((Tree) object);
    }
    
    public boolean isAnOKMoveDestinationFor(Creature creature) {
        return isAnOKMoveDestinationFrom(creature.getX(), creature.getY());
    }
    
    public boolean isAnOKMoveDestinationFrom(float startX, float startY) {
        if (!isPassable() || drainsHP()) return false;
        
        for (Tile tile : getArea().getTilesOnLine(startX, startY, getMiddleX(), getMiddleY())) {
            // Don't call tile.blocksVision() here. It's called
            // in Creature.getPossibleMovementDestinations()
            // (which calls this method.
            if (tile.drainsHP()) return false;
        }
        
        return true;
    }
    
    public int getLeftX() {
        return getX() * Tile.size;
    }
    
    public int getRightX() {
        return (getX() + 1) * Tile.size;
    }
    
    public int getBottomY() {
        return getY() * Tile.size;
    }
    
    public int getTopY() {
        return (getY() + 1) * Tile.size;
    }
    
    public int getMiddleX() {
        return getX()*Tile.size + Tile.size/2;
    }
    
    public int getMiddleY() {
        return getY()*Tile.size + Tile.size/2;
    }
    
    public boolean isOneWay() {
        return getGround().getClass() == OneWayFloor.class;
    }
    
    public boolean hasSlime() {
        if (!hasObject()) return false;
        return object.isSlime();
    }
    
    public boolean hasTree() {
        if (!hasObject()) return false;
        return object.isTree();
    }
    
    public List<Tile> getNearbyTiles() {
        List<Tile> tiles = new LinkedList<Tile>();
        if (area.hasLocation(x-1, y)) tiles.add(area.getTileAt(x-1, y));
        if (area.hasLocation(x+1, y)) tiles.add(area.getTileAt(x+1, y));
        if (area.hasLocation(x, y-1)) tiles.add(area.getTileAt(x, y-1));
        if (area.hasLocation(x, y+1)) tiles.add(area.getTileAt(x, y+1));
        return tiles;
    }
    
    public boolean castsShadows() {
        if (!hasObject()) return false;
        return object.castsShadows();
    }
}