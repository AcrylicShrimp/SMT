package game.scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.sound.sampled.Clip;

import game.Game;
import game.GameScene;
import game.GameSprite;

public class FlightScene extends GameScene {

	public static final float BACK_SPEED = 1200f;
	public static final float PLAYER_SPEED = 400f;
	public static final float ENEMY_SPEED = 350f;
	public static final float BULLET_SPEED = 600f;
	public static final float ITEM_SPEED = 900f;
	public static final float EBULLET_SPEED = 500f;
	public static final float BULLET_DELAY = 0.3f;
	public static final float EBULLET_DELAY = 0.5f;
	public static final float PLAYER_HIT_DELAY = 2f;
	public static final float ENEMY_SPAWN_DELAY = 2f;
	public static final float ITEM_SPAWN_DELAY = 7f;
	public static final int ENEMY_SCORE = 25;
	public static final int ITEM_SCORE = 100;

	private Random random;
	private GameSprite back_front;
	private GameSprite back_back;
	private GameSprite player;
	private GameSprite player_hp_ui;
	private ArrayList<GameSprite> enemy_list;
	private ArrayList<GameSprite> ebullet_list;
	private ArrayList<GameSprite> bullet_list;
	private ArrayList<GameSprite> item_list;
	private ArrayList<GameSprite> ui_list;

	private boolean bullet_fired;
	private boolean player_hit;

	private float bullet_delay;

	private float bullet_last_fired_time;
	private float ebullet_last_fired_time;
	private float player_last_hit_time;
	private float enemy_last_spawn_time;
	private float item_last_spawn_time;

	private int player_hp;
	private int player_score;

	private Font score_font;
	private Clip fire_sound;

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();

		this.random = new Random();

		this.back_front = new GameSprite("res/back3.jpg");
		this.back_back = new GameSprite("res/back3.jpg");
		this.back_front.width = this.back_back.width = Game.GAME_FRAME_WIDTH;
		this.back_front.height = this.back_back.height = Game.GAME_FRAME_HEIGHT;
		this.back_back.x = Game.GAME_FRAME_WIDTH;

		this.player = new GameSprite("res/f15k_1.png");
		this.player_hp_ui = new GameSprite("res/hp_5.png");

		this.enemy_list = new ArrayList<>();
		this.ebullet_list = new ArrayList<>();
		this.bullet_list = new ArrayList<>();
		this.item_list = new ArrayList<>();
		this.ui_list = new ArrayList<>();

		this.ui_list.add(this.player_hp_ui);

		this.bullet_fired = false;
		this.player_hit = false;

		this.bullet_delay = FlightScene.BULLET_DELAY;

		this.bullet_last_fired_time = 0f;
		this.ebullet_last_fired_time = 0f;
		this.player_last_hit_time = 0f;
		this.enemy_last_spawn_time = 0f;
		this.item_last_spawn_time = 0f;

		this.player_hp = 5;
		this.player_score = 0;

		this.player.x = -400;
		this.player_hp_ui.x = 300;
		this.player_hp_ui.y = 200;
		this.player_hp_ui.opaque = .3f;

		try {
			this.score_font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/SeoulHangangB.ttf"));
			this.score_font = this.score_font.deriveFont(64f);
		} catch (Exception e) {
		}

		this.fire_sound = Game.game_sound.make_sound("res/fire.wav", -30f);

		Game.game_time.elapsed_time = 0f;
		Game.game_time.elapsed_time_unscaled = 0f;

		Game.game_sound.play_sound("res/flight_bgm.wav", -20f, true);

		this.spawn_enemy(true);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		if (Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;

		this.move_background();

		this.move_player();
		this.clip_player();

		this.fire_bullet();
		this.move_bullet();
		this.clip_bullet();

		this.spawn_enemy(false);
		this.fire_enemy();
		this.move_enemy();
		this.clip_enemy();

		this.spawn_item();
		this.move_item();
		this.clip_item();

		this.check_player_hit();
		this.check_enemy_hit();
		this.check_player_item();
	}

	@Override
	public void render(Graphics2D graphics) {
		// TODO Auto-generated method stub
		super.render(graphics);

		for (GameSprite game_sprite : this.ui_list)
			game_sprite.render(graphics);

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

		graphics.setColor(Color.WHITE);
		graphics.setFont(this.score_font);
		graphics.drawString("SCORE : " + this.player_score, 50, 530);
	}

	public void move_background() {
		float speed = FlightScene.BACK_SPEED * Game.game_time.delta_time;

		this.back_front.x -= speed;
		this.back_back.x -= speed;

		if (this.back_back.x < 0) {
			this.back_front.x += Game.GAME_FRAME_WIDTH;

			GameSprite temp = this.back_front;
			this.back_front = this.back_back;
			this.back_back = temp;
		}
	}

	public void move_player() {
		float speed = FlightScene.PLAYER_SPEED * Game.game_time.delta_time;

		if (Game.game_input.key_state[KeyEvent.VK_LEFT])
			player.x -= speed;
		if (Game.game_input.key_state[KeyEvent.VK_RIGHT])
			player.x += speed;
		if (Game.game_input.key_state[KeyEvent.VK_UP])
			player.y -= speed;
		if (Game.game_input.key_state[KeyEvent.VK_DOWN])
			player.y += speed;
	}

	public void clip_player() {
		if (this.player.x < Game.GAME_FRAME_WIDTH * -.5f)
			this.player.x = Game.GAME_FRAME_WIDTH * -.5f;
		else if (this.player.x > Game.GAME_FRAME_WIDTH * .5f)
			this.player.x = Game.GAME_FRAME_WIDTH * .5f;

		if (this.player.y < Game.GAME_FRAME_HEIGHT * -.5f)
			this.player.y = Game.GAME_FRAME_HEIGHT * -.5f;
		else if (this.player.y > Game.GAME_FRAME_HEIGHT * .5f)
			this.player.y = Game.GAME_FRAME_HEIGHT * .5f;
	}

	public void fire_bullet() {
		if (Game.game_input.key_state[KeyEvent.VK_SPACE] && !this.bullet_fired) {
			this.make_bullet();
			this.bullet_fired = true;
			this.bullet_last_fired_time = Game.game_time.elapsed_time;

			this.fire_sound.setMicrosecondPosition(0);
			this.fire_sound.start();
		}

		if (Game.game_time.elapsed_time >= this.bullet_last_fired_time + this.bullet_delay)
			this.bullet_fired = false;
	}

	public void make_bullet() {
		GameSprite bullet = new GameSprite("res/bullet.png");

		bullet.x = this.player.x + this.player.width * .5f;
		bullet.y = this.player.y;

		this.bullet_list.add(bullet);
	}

	public void move_bullet() {
		float bullet_speed = FlightScene.BULLET_SPEED * Game.game_time.delta_time;

		for (GameSprite bullet_sprite : this.bullet_list)
			bullet_sprite.x += bullet_speed;
	}

	public void clip_bullet() {
		for (Iterator<GameSprite> bullet_iterator = this.bullet_list.iterator(); bullet_iterator.hasNext();) {
			GameSprite bullet = bullet_iterator.next();

			if (bullet.x - bullet.width * .5f >= Game.GAME_FRAME_WIDTH * .5f) {
				bullet.dispose();
				bullet_iterator.remove();
			}
		}
	}

	public void spawn_enemy(boolean force) {
		if (force || Game.game_time.elapsed_time >= this.enemy_last_spawn_time + FlightScene.ENEMY_SPAWN_DELAY) {
			for (int i = 0, num = this.random.nextInt(5 + (int) (Game.game_time.elapsed_time * 0.2f)); i < num; ++i)
				this.make_enemy(this.roll_random(Game.GAME_FRAME_WIDTH * .5f, Game.GAME_FRAME_WIDTH * .5f + 1000f),
						this.roll_random(Game.GAME_FRAME_HEIGHT * -.5f + 100f, Game.GAME_FRAME_HEIGHT * .5f - 100f));

			this.enemy_last_spawn_time = Game.game_time.elapsed_time;
		}
	}

	public void make_enemy(float x, float y) {
		GameSprite enemy = new GameSprite("res/enemy.png");

		enemy.x = x;
		enemy.y = y;

		this.enemy_list.add(enemy);
	}

	public void fire_enemy() {
		if (Game.game_time.elapsed_time >= this.ebullet_last_fired_time + FlightScene.EBULLET_DELAY) {
			if (this.enemy_list.size() >= 1)
				for (int i = 0, num = this.random.nextInt(this.enemy_list.size()); i < num; ++i)
					this.make_ebullet(this.enemy_list.get(i));
			
			this.ebullet_last_fired_time = Game.game_time.elapsed_time;
		}
	}

	public void make_ebullet(GameSprite enemy) {
		GameSprite ebullet = new GameSprite("res/bullet.png");

		ebullet.x = enemy.x + enemy.width * -.5f;
		ebullet.y = enemy.y;

		this.ebullet_list.add(ebullet);
	}

	public void move_enemy() {
		float enemy_speed = FlightScene.ENEMY_SPEED * Game.game_time.delta_time;
		float ebullet_speed = FlightScene.EBULLET_SPEED * Game.game_time.delta_time;

		for (GameSprite enemy_sprite : this.enemy_list)
			enemy_sprite.x -= enemy_speed;

		for (GameSprite enemy_sprite : this.ebullet_list)
			enemy_sprite.x -= ebullet_speed;
	}

	public void clip_enemy() {
		for (Iterator<GameSprite> enemy_iterator = this.enemy_list.iterator(); enemy_iterator.hasNext();) {
			GameSprite enemy = enemy_iterator.next();

			if (enemy.x + enemy.width * .5f <= Game.GAME_FRAME_WIDTH * -.5f) {
				enemy.dispose();
				enemy_iterator.remove();
			}
		}

		for (Iterator<GameSprite> enemy_iterator = this.ebullet_list.iterator(); enemy_iterator.hasNext();) {
			GameSprite enemy = enemy_iterator.next();

			if (enemy.x + enemy.width * .5f <= Game.GAME_FRAME_WIDTH * -.5f) {
				enemy.dispose();
				enemy_iterator.remove();
			}
		}
	}

	public void spawn_item() {
		if (Game.game_time.elapsed_time >= this.item_last_spawn_time + FlightScene.ITEM_SPAWN_DELAY) {
			for (int i = 0, num = this.random.nextInt(3); i < num; ++i)
				this.make_item(this.roll_random(Game.GAME_FRAME_WIDTH * .5f, Game.GAME_FRAME_WIDTH * .5f + 1000f),
						this.roll_random(Game.GAME_FRAME_HEIGHT * -.5f + 100f, Game.GAME_FRAME_HEIGHT * .5f - 100f));

			this.item_last_spawn_time = Game.game_time.elapsed_time;
		}
	}

	public void make_item(float x, float y) {
		GameSprite item = new GameSprite("res/item.png");

		item.x = x;
		item.y = y;

		this.item_list.add(item);
	}

	public void move_item() {
		float item_speed = FlightScene.ITEM_SPEED * Game.game_time.delta_time;

		for (GameSprite item_sprite : this.item_list)
			item_sprite.x -= item_speed;
	}

	public void clip_item() {
		for (Iterator<GameSprite> item_iterator = this.item_list.iterator(); item_iterator.hasNext();) {
			GameSprite item = item_iterator.next();

			if (item.x + item.width * .5f <= Game.GAME_FRAME_WIDTH * -.5f) {
				item.dispose();
				item_iterator.remove();
			}
		}
	}

	public void check_player_hit() {
		if (this.player_hit) {
			if (Game.game_time.elapsed_time >= this.player_last_hit_time + FlightScene.PLAYER_HIT_DELAY) {
				this.player_hit = false;
				this.player.opaque = 1f;
			}
		} else {
			for (GameSprite enemy_sprite : this.enemy_list)
				if (this.player.is_collide(enemy_sprite)) {
					this.player_hit = true;
					this.player.opaque = 0.5f;
					this.player_last_hit_time = Game.game_time.elapsed_time;

					this.set_player_hp_ui(--this.player_hp);

					if (this.player_hp == 0) {
						try {
							//RankingManager.add_rank(this.player_score);
							Thread.sleep(2000);
						} catch (InterruptedException e) {
						}

						Game.set_game_scene(Scenes.over_scene);
					}

					return;
				}

			for (GameSprite enemy_sprite : this.ebullet_list)
				if (this.player.is_collide(enemy_sprite)) {
					this.player_hit = true;
					this.player.opaque = 0.5f;
					this.player_last_hit_time = Game.game_time.elapsed_time;

					this.set_player_hp_ui(--this.player_hp);

					if (this.player_hp == 0) {
						try {
							//RankingManager.add_rank(this.player_score);
							Thread.sleep(2000);
						} catch (InterruptedException e) {
						}

						Game.set_game_scene(Scenes.over_scene);
					}

					return;
				}
		}
	}

	public void check_enemy_hit() {
		for (Iterator<GameSprite> bullet_iterator = this.bullet_list.iterator(); bullet_iterator.hasNext();) {
			GameSprite bullet = bullet_iterator.next();

			for (Iterator<GameSprite> enemy_iterator = this.enemy_list.iterator(); enemy_iterator.hasNext();) {
				GameSprite enemy = enemy_iterator.next();

				if (enemy.is_collide(bullet)) {
					// Add boooom effect here!
					this.player_score += FlightScene.ENEMY_SCORE;

					enemy.dispose();
					enemy_iterator.remove();

					bullet.dispose();
					bullet_iterator.remove();

					if (!bullet_iterator.hasNext())
						return;

					bullet = bullet_iterator.next();
				}
			}
		}
	}

	public void check_player_item() {
		for (Iterator<GameSprite> item_iterator = this.item_list.iterator(); item_iterator.hasNext();) {
			GameSprite item_sprite = item_iterator.next();

			if (this.player.is_collide(item_sprite)) {

				this.player_score += FlightScene.ITEM_SCORE;

				if (this.bullet_delay >= .05f)
					this.bullet_delay *= .8f;

				item_sprite.dispose();
				item_iterator.remove();
			}
		}
	}

	public float roll_random(float min, float max) {
		return this.random.nextFloat() * (max - min) + min;
	}

	public void set_player_hp_ui(int hp) {
		this.player_hp_ui.reset_sprite("res/hp_" + hp + ".png");
	}
}