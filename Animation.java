import java.awt.Graphics2D;

public class Animation {

	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	protected int xDest;
	protected int yDest;
	protected int startFrame;
	protected int frames;
	protected boolean finished;

	public Animation(int x, int y, int xDest, int yDest, int ms) {
		this.x = x;
		this.y = y;
		this.xDest = xDest;
		this.yDest = yDest;
		this.startFrame = Main2.frameCounter;
		frames = (int)(0.001*ms*Main2.FPS);
		dx = (xDest - x)/frames;
		dy = (yDest - y)/frames;
		finished = false;
	}

	public boolean isFinished() {
		return finished;
	}

	public void update() {
		x += dx;
		y += dy;
		if(Main2.frameCounter - startFrame >= frames) 
			finished = true;
	}

	public void draw(Graphics2D g) {
		
	}

}
