package game.scenes;

import java.awt.event.KeyEvent;

import game.Game;
import game.GameScene;

public class TitleScene extends GameScene {
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;
		
	}
}