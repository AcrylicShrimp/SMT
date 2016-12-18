package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import game.rank.RankingManager;

public class Game {
	
	public static final int GAME_FRAME_WIDTH = 800;
	public static final int GAME_FRAME_HEIGHT = 600;
	public static final String GAME_FRAME_TITLE = "SMT";
	
	public static GameFrame game_frame;
	public static GameTime game_time;
	public static GameInput game_input;
	public static GameSound game_sound;
	public static GameScene game_scene;
	public static boolean game_looping;
	
	private static Image back_buffer;
	private static Image front_buffer;
	
	public static void init_game() {
		RankingManager.initRanking();
		
		Game.game_time = new GameTime();
		Game.game_input = new GameInput();
		Game.game_sound = new GameSound();
		
		Game.game_frame = new GameFrame(Game.GAME_FRAME_WIDTH, Game.GAME_FRAME_HEIGHT, Game.GAME_FRAME_TITLE);
		
		//버퍼 초기화
		Game.back_buffer = new BufferedImage(Game.GAME_FRAME_WIDTH, Game.GAME_FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		Game.front_buffer = new BufferedImage(Game.GAME_FRAME_WIDTH, Game.GAME_FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	}
	
	public static void set_game_scene(GameScene new_game_scene) {
		Game.game_sound.stop_sound_all();
		Game.game_time.elapsed_time = 0f;
		Game.game_time.elapsed_time_unscaled = 0f;
		(Game.game_scene = new_game_scene).initialize();
	}
	
	public static void loop_game_logic() {
		Game.game_looping = true;
		
		while(Game.game_looping) {
			Game.game_time.update();
			Game.game_sound.update();
			Game.game_scene.update();
			
			//화면 검은색으로 지우고 씬 렌더
			Graphics2D graphics = (Graphics2D)Game.back_buffer.getGraphics();
			graphics.clearRect(0, 0, Game.GAME_FRAME_WIDTH, Game.GAME_FRAME_HEIGHT);
			Game.game_scene.render(graphics);
			
			//백 버퍼를 프론트 버퍼로 설정하고 렌더
			Game.game_frame.game_panel.front_buffer = Game.back_buffer;
			Game.game_frame.game_panel.repaint();
			
			//백 프론트 스왑
			Image temp_back_buffer = Game.back_buffer;
			Game.back_buffer = Game.front_buffer;
			Game.front_buffer = temp_back_buffer;
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
}