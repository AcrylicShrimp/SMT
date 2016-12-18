package game.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import game.Game;
import game.GameScene;
import game.GameSprite;
import game.GameText;
import game.ui.KORInput;
import smt.Word;

public class ENGScene extends GameScene {
	
	public static final float SPAWN_DELAY = 2f;
	public static final int SPAWNED_SCORE = 50;
	public static final float ITEM_DELAY = 2f;
	
	private Random random;
	private Font global_font;
	private KORInput test_input;
	private GameSprite hp_ui;
	private GameText score_ui;
	
	private int hp;
	private int score;
	private float last_spawn_time;
	private float last_item_time;
	private int spawn_count;
	private int item_count;
	private ArrayList<Word> spawn_list;
	private ArrayList<Word> item_list;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		try {
			this.global_font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/SeoulHangangB.ttf"));
		} catch (Exception e) { }
		
		this.random = new Random();
		
		this.last_spawn_time = 0f;
		this.spawn_count = 1;
		this.spawn_list = new ArrayList<>();
		
		this.last_item_time = 0f;
		this.item_count = 2;
		this.item_list = new ArrayList<>();
		
		this.hp_ui = new GameSprite("res/hp_5.png");
		this.hp = 5;
		this.hp_ui.width *= 0.5f;
		this.hp_ui.height *= 0.5f;
		this.hp_ui.x = 250f;
		this.hp_ui.y = -260f;
		
		this.score = 0;
		this.score_ui = new GameText(this.global_font,
				29f,
				Game.GAME_FRAME_WIDTH,
				Game.GAME_FRAME_HEIGHT,
				GameText.ALIGN_LEFT,
				GameText.ALIGN_UP,
				Color.WHITE);
		this.update_score_ui();
		this.score_ui.x += 25f;
		this.score_ui.y += 25f;
		
		this.test_input = new KORInput(this.global_font,
				27f,
				Game.GAME_FRAME_WIDTH,
				27,
				GameText.ALIGN_CENTER,
				GameText.ALIGN_CENTER,
				Color.WHITE);
		
		this.test_input.y = (Game.GAME_FRAME_HEIGHT - 27f) * .5f;
		this.test_input.is_kor = false;
		this.test_input.max_char = 32;
		this.test_input.bind_key();
		
		this.spawn();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;
		
		if(Game.game_input.key_state[KeyEvent.VK_ENTER]) {
			this.test_input.submit_text();
			
			if(!this.test_input.text.isEmpty()) {
				
				for(Iterator<Word> iterator = this.spawn_list.iterator() ; iterator.hasNext() ; ) {
					Word spawned = iterator.next();
					
					if(spawned.text.text.equals(this.test_input.text)) {
						this.score += ENGScene.SPAWNED_SCORE;
						this.update_score_ui();
						
						spawned.dispose();
						iterator.remove();
					}
				}
				
				for(Iterator<Word> iterator = this.item_list.iterator() ; iterator.hasNext() ; ) {
					Word spawned = iterator.next();
					
					if(spawned.text.text.equals(this.test_input.text)) {
						if(this.hp < 5)
							++this.hp;
						
						this.update_hp_ui();
						
						spawned.dispose();
						iterator.remove();
					}
				}
				
				this.test_input.clear_text();
			}
		}
		
		this.spawn();
		this.move_spawned();
		this.clip_spawned();
		
		this.item();
		this.move_item();
		this.clip_item();
	}
	
	private void spawn() {
		if(Game.game_time.elapsed_time >= this.last_spawn_time + ENGScene.SPAWN_DELAY) {
			this.last_spawn_time = Game.game_time.elapsed_time;
			
			for(int count = 0 ; count < this.spawn_count ; ++count)
				this.spawn_word();
			
			++this.spawn_count;
		}
	}
	
	private void spawn_word() {
		Word spawned = new Word(this.global_font, 31f, 1, 1, 0, 0, Color.CYAN);
		spawned.text.text = "a";
		spawned.x = this.roll_random(-.5f * Game.GAME_FRAME_WIDTH, .5f * Game.GAME_FRAME_WIDTH);
		spawned.y = this.roll_random(Game.GAME_FRAME_HEIGHT * -.5f - 100f, Game.GAME_FRAME_HEIGHT * -.5f + 100f);
		
		this.spawn_list.add(spawned);
	}
	
	private void move_spawned() {
		for(Word spawned : this.spawn_list) {
			spawned.y += spawned.speed * Game.game_time.delta_time;
			spawned.sync_position();
		}
	}
	
	private void clip_spawned() {
		for(Iterator<Word> iterator = this.spawn_list.iterator() ; iterator.hasNext() ; ) {
			Word spawned = iterator.next();
			
			if(spawned.y >= Game.GAME_FRAME_HEIGHT * .5f) {
				--this.hp;
				this.update_hp_ui();
				
				if(this.hp == 0) {
					Scenes.over_scene.last_score = this.score;					
					Game.set_game_scene(Scenes.over_scene);
				}
				
				spawned.dispose();
				iterator.remove();
			}
		}
	}
	
	private void item() {
		if(Game.game_time.elapsed_time >= this.last_item_time + ENGScene.ITEM_DELAY) {
			this.last_item_time = Game.game_time.elapsed_time;
			
			for(int count = 0 ; count < this.item_count ; ++count)
				this.spawn_item();
		}
	}
	
	private void spawn_item() {
		Word spawned = new Word(this.global_font, 27f, 1, 1, 0, 0, Color.MAGENTA);
		spawned.text.text = "iTeM";
		spawned.x = this.roll_random(-.5f * Game.GAME_FRAME_WIDTH, .5f * Game.GAME_FRAME_WIDTH);
		spawned.y = this.roll_random(Game.GAME_FRAME_HEIGHT * -.5f - 100f, Game.GAME_FRAME_HEIGHT * -.5f + 100f);
		
		this.item_list.add(spawned);
	}
	
	private void move_item() {
		for(Word spawned : this.item_list) {
			spawned.y += spawned.speed * Game.game_time.delta_time;
			spawned.sync_position();
		}
	}
	
	private void clip_item() {
		for(Iterator<Word> iterator = this.item_list.iterator() ; iterator.hasNext() ; ) {
			Word spawned = iterator.next();
			
			if(spawned.y >= Game.GAME_FRAME_HEIGHT * .5f) {
				spawned.dispose();
				iterator.remove();
			}
		}
	}
	
	private void update_hp_ui() {
		this.hp_ui.reset_sprite("res/hp_" + this.hp + ".png");
		this.hp_ui.width *= 0.5f;
		this.hp_ui.height *= 0.5f;
	}
	
	private void update_score_ui() {
		this.score_ui.text = String.valueOf("SCORE : " + this.score);
	}
	
	private float roll_random(float min, float max) {
		return this.random.nextFloat() * (max - min) + min;
	}
}