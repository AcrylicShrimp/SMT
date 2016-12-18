package game.scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.File;

import game.Game;
import game.GameInput;
import game.GameScene;
import game.GameSprite;
import game.rank.RankData;
import game.rank.RankingManager;

public class RankScene extends GameScene {

	public static final int MAX_RANK_NUM = 7;
	public static final float RENDER_X_FIRST = 0f;
	public static final float RENDER_X_SECOND = 80f;
	public static final float RENDER_Y_STEP = 70f;

	private GameSprite title_button;
	private Font score_font;
	private int max_num;

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();

		this.title_button = new GameSprite("res/start_btn.png");
		this.title_button.y = 220;

		this.max_num = Math.min(RankingManager.rank_list.size(), RankScene.MAX_RANK_NUM);

		try {
			this.score_font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/SeoulHangangB.ttf"));
			this.score_font = this.score_font.deriveFont(48f);
		} catch (Exception e) {
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (Game.game_input.key_state[KeyEvent.VK_ESCAPE])
			Game.game_looping = false;

		if (this.title_button.is_collide(Game.game_input.mouse_x, Game.game_input.mouse_y)) {
			if (Game.game_input.mouse_state[GameInput.MOUSE_LEFT])
				Game.set_game_scene(Scenes.title_scene);
			else
				this.title_button.opaque = 1f;
		} else
			this.title_button.opaque = .5f;
	}

	@Override
	public void render(Graphics2D graphics) {
		// TODO Auto-generated method stub
		super.render(graphics);

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		graphics.setColor(Color.WHITE);
		graphics.setFont(this.score_font);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		float render_y = RankScene.RENDER_Y_STEP;

		for (int count = 0; count < this.max_num; ++count) {
			RankData rank_data = RankingManager.rank_list.get(count);
			
			graphics.drawString((count + 1) + this.generate_num(count + 1), RankScene.RENDER_X_FIRST, render_y);
			graphics.drawString(
					": " + rank_data.play_date + " / " + rank_data.player_name + " / " + rank_data.game_score,
					RankScene.RENDER_X_SECOND, render_y);

			render_y += RankScene.RENDER_Y_STEP;
		}
	}

	public String generate_num(int number) {
		if (number == 1)
			return "st";
		else if (number == 2)
			return "nd";
		else if (number == 3)
			return "rd";
		else
			return "th";
	}
}