import java.awt.Graphics2D;

public class CheckerAnimation extends Animation {

	private double radius;
	private double dRadius;
	private int maxRadius;
	private int checkerType;
	Lane from;
	Lane to;
	Lane drawMethod;

	public CheckerAnimation(int x, int y, int xDest, int yDest, int radius, int checkerType, Lane from, Lane to, int ms) {
		super(x, y, xDest, yDest, ms);
		this.radius = radius;
		this.from = from;
		this.to = to;
		this.drawMethod = from;
		if(drawMethod == null)
			drawMethod = to;
		this.checkerType = checkerType;
		this.maxRadius = (int)(radius * 1.35);
		this.dRadius = (double)(maxRadius - radius)/(double)(frames / 2); 
	}

	public Lane getTo() {
		return to;
	}
	public Lane getFrom() {
		return from;
	}
	public void setDrawMethod(Lane lane) {
		this.drawMethod = lane;
	}

	public void update() {
		if(Main2.frameCounter - startFrame >= frames/2 && dRadius > 0)
			dRadius = -dRadius;
		radius = radius + dRadius;
		x = x + dx - dRadius/2;
		y = y + dy - dRadius/2;
		if(Main2.frameCounter - startFrame >= frames) 
			finished = true;
	}

	public void draw(Graphics2D g) {
		if(!finished)
			drawMethod.drawChecker((int)x, (int)y, (int)radius, checkerType, g);
	}

}
