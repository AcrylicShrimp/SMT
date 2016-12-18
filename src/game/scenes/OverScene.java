package game.scenes;

import java.awt.event.KeyEvent;

import game.Game;
import game.GameInput;
import game.GameScene;
import game.GameSprite;

public class OverScene extends GameScene {

	private GameSprite back_over;
	private GameSprite goto_main_button;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		this.back_over = new GameSprite("res/back_over.png");
		this.back_over.width *= 1.3f;
		this.back_over.height *= 1.3f;
		
		this.goto_main_button = new GameSprite("res/start_btn.png");
		this.goto_main_button.y = 200;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		if(Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;
		
		if(this.goto_main_button.is_collide(Game.game_input.mouse_x, Game.game_input.mouse_y)) {
			if(Game.game_input.mouse_state[GameInput.MOUSE_LEFT])
				Game.set_game_scene(Scenes.title_scene);
			else
				this.goto_main_button.opaque = 1f;
		}
		else
			this.goto_main_button.opaque = .5f;
	}
}