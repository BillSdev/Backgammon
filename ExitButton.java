import java.awt.Color;
import java.awt.Graphics2D;

public class ExitButton extends Button {

	public ExitButton(int x, int y, int width, int height, Game2 game) {
		super(x, y, width, height, game);
	}

	public void action() {
		if(active) {
			System.exit(0);
		}
	}

	public void draw(Graphics2D g) {
		if(active) {
			Color color = g.getColor();
			g.setColor(Color.red);
			g.fillRect(x, y, width, height);
			g.setColor(color);
			
		}
	}
	
}
