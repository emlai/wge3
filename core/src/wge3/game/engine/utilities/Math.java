package wge3.game.engine.utilities;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.floor;
import wge3.game.entity.Tile;

public final class Math {
    
    public static float angle(float x1, float y1, float x2, float y2) {
        float angle = atan2(y2 - y1, x2 - x1);
        if (angle < 0) angle += PI2;
        return angle;
    }

    public static float getDiff(float src, float dst) {
        float diff = dst - src;
        if (diff > PI) diff -= PI2;
        else if (diff < -PI) diff += PI2;
        return diff;
    }
    
    public static int floatPosToTilePos(float pos) {
        return floor(pos) / Tile.size;
    }
}