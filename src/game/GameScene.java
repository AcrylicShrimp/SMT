package game;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class GameScene implements GameRenderable {
	
	public ArrayList<GameRenderable> renderable_list;
	
	public GameScene() {
		this.renderable_list = new ArrayList<>();
	}
	
	public void add_renderable(GameRenderable game_renderable) {
		this.renderable_list.add(game_renderable);
	}
	
	public void remove_sprite(GameRenderable game_renderable) {
		this.renderable_list.remove(game_renderable);
	}
	
	public void initialize() {
		this.renderable_list.clear();
	}
	
	public abstract void update();
	
	public void render(Graphics2D graphics) {
		for(GameRenderable game_renderable : this.renderable_list)
			game_renderable.render(graphics);
	}
}