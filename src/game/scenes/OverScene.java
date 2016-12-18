package game.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import game.Game;
import game.GameInput;
import game.GameScene;
import game.GameSprite;
import game.rank.RankData;
import game.rank.RankingManager;
import game.ui.KORInput;

public class OverScene extends GameScene {
	
	public int last_score;
	
	private Font global_font;
	private GameSprite back_over;
	private GameSprite goto_main_button;
	private KORInput name_input;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		try {
			this.global_font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/SeoulHangangB.ttf"));
		} catch (Exception e) { }
		
		this.back_over = new GameSprite("res/horangee.jpg");
		this.back_over.width *= 1.5f;
		this.back_over.height *= 1.5f;
		
		this.goto_main_button = new GameSprite("res/start_btn.png");
		this.goto_main_button.y = 200f;
		
		this.name_input = new KORInput(
				this.global_font,
				49f,
				Game.GAME_FRAME_WIDTH,
				Game.GAME_FRAME_HEIGHT,
				0,
				0,
				Color.MAGENTA);
		
		this.name_input.max_char = 16;
		this.name_input.bind_key();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;
		
		if(this.name_input.text.isEmpty())
			return;
		
		if(this.goto_main_button.is_collide(Game.game_input.mouse_x, Game.game_input.mouse_y)) {
			if(Game.game_input.mouse_state[GameInput.MOUSE_LEFT]) {
				RankData rank_data = new RankData();
				rank_data.play_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				rank_data.player_name = this.name_input.text;
				rank_data.game_score = this.last_score;
				
				RankingManager.add_rank(rank_data);
				
				Game.set_game_scene(Scenes.title_scene);
			} else
				this.goto_main_button.opaque = 1f;
		}
		else
			this.goto_main_button.opaque = .5f;
	}
}