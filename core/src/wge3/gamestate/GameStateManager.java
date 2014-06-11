package wge3.gamestate;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameStateManager {
    
    private GameState currentState;
    
    private ShapeRenderer sr;

    public GameStateManager() {
        sr = new ShapeRenderer();
    }
    
    public void setState(int newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        
        switch (newState) {
            case 0:
                // menu
                break;
                
            case 1:
                currentState = new PlayState(this);
                break;
                
            case 2:
                // preferences
                break;
        }
    }
    
    public void update(float delta) {
        currentState.update(delta);
    }
    
    public void draw() {
        currentState.draw(sr);
    }
}
