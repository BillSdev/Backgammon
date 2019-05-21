import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class Button {

	protected int x;
	protected int y;
	protected int xFixed;
	protected int yFixed;
	protected int width;
	protected int height;
	protected boolean active;
	protected boolean isPressed;     //mouse on button
	protected Game2 game;

	public Button(int x, int y, int width, int height, Game2 game) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.game = game;
		this.active = false;
		xFixed = x;
		yFixed = y;
	}

	public void action() {

	}
	
	public void activate() {
		active = true;
	}
	public void deactivate() {
		active = false;
	}
	public boolean isActive() {
		return active;
	}
	public boolean getIsPressed() {
		return isPressed;
	}
	public void setIsPressed(boolean b) {
		this.isPressed = b;
	}
	
	public boolean isInBounds(MouseEvent e) {
		if(((e.getX() >= x && e.getX() <= x + width) && 
				e.getY() >= y && e.getY() <= y + height) && active) 
			return true;
		return false;
	}
	
	public void draw(Graphics2D g) {
		
	}
}
