package game.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;

import game.Game;
import game.GameScene;
import game.GameText;
import game.ui.KORInput;

public class TitleScene extends GameScene {
	
	private Font global_font;
	private KORInput test_input;
	private GameText test_display;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		try {
			this.global_font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/SeoulHangangB.ttf"));
		} catch (Exception e) { }
		
		this.test_input = new KORInput(this.global_font,
				27f,
				Game.GAME_FRAME_WIDTH,
				27,
				GameText.ALIGN_LEFT,
				GameText.ALIGN_UP,
				Color.WHITE);
		
		this.test_display = new GameText(this.global_font,
				27f,
				Game.GAME_FRAME_WIDTH,
				27,
				GameText.ALIGN_LEFT,
				GameText.ALIGN_UP,
				Color.WHITE);
		
		this.test_input.y = (Game.GAME_FRAME_HEIGHT - 27f) * .5f;
		this.test_input.is_kor = false;
		this.test_input.max_char = 32;
		this.test_input.bind_key();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;
		
		if(Game.game_input.key_state[KeyEvent.VK_ENTER]) {
			this.test_input.submit_text();
			
			if(!this.test_input.text.isEmpty()) {
				//Do something
				this.test_display.text = this.test_input.text;
				this.test_input.clear_text();
			}
		}
	}
}