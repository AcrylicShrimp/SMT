package game;

public class GameTime {
	
	private long last_time;
	public float time_scale;
	public float delta_time;
	public float delta_time_unscaled;
	public float elapsed_time;
	public float elapsed_time_unscaled;
	
	public GameTime() {
		this.last_time = 0l;
		this.time_scale = 1f;
		this.elapsed_time = 0f;
		this.elapsed_time_unscaled = 0f;
	}
	
	public void update() {
		if(this.last_time == 0l) {
			this.last_time = System.currentTimeMillis();
			return;
		}
		
		long current_time = System.currentTimeMillis();
		
		this.delta_time_unscaled = (current_time - this.last_time) * .001f;
		this.delta_time = this.delta_time_unscaled * this.time_scale;
		
		this.elapsed_time_unscaled += this.delta_time_unscaled;
		this.elapsed_time += this.delta_time;
		
		this.last_time = current_time;
	}
}