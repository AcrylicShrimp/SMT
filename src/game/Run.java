package game;

import game.scenes.Scenes;

public class Run {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Game.init_game();
		Game.set_game_scene(Scenes.rank_scene);
		Game.loop_game_logic();
		
		System.exit(0);
	}
}