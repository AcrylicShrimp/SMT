package game;

public class GameInput {
	
	public static final int MOUSE_LEFT = 0;
	public static final int MOUSE_RIGHT = 1;
	public static final int MOUSE_MIDDLE = 2;
	
	public boolean mouse_inbound = false;
	public boolean[] key_state;
	public boolean[] mouse_state;
	public int mouse_x = 0;
	public int mouse_y = 0;
	
	public GameInput() {
		this.key_state = new boolean[256];
		this.mouse_state = new boolean[3];
	}
}