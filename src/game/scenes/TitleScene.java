package game.scenes;

import java.awt.event.KeyEvent;

import game.Game;
import game.GameScene;
import game.GameSprite;

public class TitleScene extends GameScene {
	
	private GameSprite korButton;
	private GameSprite engButton;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		this.korButton = new GameSprite("res/kor.jpg");
		this.engButton = new GameSprite("res/eng.png");
		
		this.korButton.width = this.engButton.width = 230;
		this.korButton.height = this.engButton.height = 150;
		
		this.korButton.y = -100f;
		this.engButton.y = 100f;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;
		
		if(Game.game_input.mouse_state[0]) {
			if(this.korButton.is_collide(Game.game_input.mouse_x, Game.game_input.mouse_y))
				Game.set_game_scene(Scenes.kor_scene);
			else if(this.engButton.is_collide(Game.game_input.mouse_x, Game.game_input.mouse_y))
				Game.set_game_scene(Scenes.eng_scene);
		}
	}
}