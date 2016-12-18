package game.scenes;

import java.awt.event.KeyEvent;

import game.Game;
import game.GameScene;
import game.GameSprite;

public class TestScene extends GameScene {
	
	private GameSprite back_sprite;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		this.back_sprite = new GameSprite("res/back.png");
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(Game.game_input.key_state[KeyEvent.VK_ENTER])
			Game.set_game_scene(Scenes.title_scene);
	}

}