import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class NewGameButton extends Button{

	Main2 main;
	String str;
	
	public NewGameButton(int x, int y, int width, int height, Game2 game, Main2 main) {
		super(x, y, width, height, game);
		this.main = main;
		str = "Rematch";
		if(main.getInMenu())
			str = "Start Game";
	}

	public void action() {
		if(active) {
			if(main.getInMenu())
				main.setInMenu(false);
			main.newGame();
		}
	}

	public void draw(Graphics2D g) {
		if(active) {
			Color color = g.getColor();
			g.setColor(Color.green);
			if(isPressed)
				g.setColor(Color.yellow);
			g.fillRect(x, y, width, height);
			g.setColor(Color.black);
			Font font = new Font("Ariel", (int)(height*0.5), (int)(height*0.5));
			g.setFont(font);
			g.drawString(str, (int)(x + width/2 - str.length()*0.5*height*0.26), (int)(y + 0.5*height + 0.17*height));
			g.setColor(color);
		}
	}
	
}
