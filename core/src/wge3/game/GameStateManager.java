package wge3.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class GameStateManager {
    
    private GameState currentState;
    
    private SpriteBatch batch;

    public GameStateManager() {
        batch = new SpriteBatch();
    }
    
    public void setState(int newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        
        switch (newState) {
            case 0:
                currentState = new MenuState(this);
                break;
                
            case 1:
                currentState = new PlayState(this);
                break;
                
            case 2:
                // currentState = new PreferencesState(this);
                break;
        }
    }
    
    public void update(float delta) {
        currentState.update(delta);
    }
    
    public void draw() {
        currentState.draw(batch);
    }
}
