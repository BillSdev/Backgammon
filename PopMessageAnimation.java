import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PopMessageAnimation extends Animation {
	//did not make the size of the message dynamic. specifically tailored to  1080x660 (+-~).
	String str;
	int startWidth;
	double width;
	int startHeight;
	double height;
	int endWidth;
	int endHeight;
	Color color;

	public PopMessageAnimation(String str, int x, int y, int xDest, int yDest, int ms, Color color) {
		super(x, y, xDest, yDest, ms);
		this.str = str;
		endWidth = 200;
		endHeight = 50;
		dx = (double)((startWidth - endWidth) / 2) / (frames / 8);
		dy = (double)((startHeight - endHeight) / 2) / (frames / 8);
		this.color = color;
	}

	public void update() {
		super.update();
		width -= 2*dx;
		if(width >= endWidth)
			dx = 0;
		height -= 2*dy;
		if(height >= endHeight)
			dy = 0;

	}

	public void draw(Graphics2D g) {
		System.out.println("x = " + x + " y = " + y);
		g.setColor(Color.black);
		g.fillOval((int)(x-height/2)-2, (int)y-2, (int)height+4, (int)height+4);
		g.fillOval((int)(x + width - height/2)-2, (int)y-2, (int)height+4, (int)height+4);
		g.fillRect((int)x-2, (int)y-2, (int)width+4, (int)height+4);
		g.setColor(color);
		g.fillOval((int)(x-height/2), (int)y, (int)height, (int)height);
		g.fillOval((int)(x + width - height/2), (int)y, (int)height, (int)height);
		g.fillRect((int)x, (int)y, (int)width, (int)height);
		g.setColor(Color.black);
		Font font = new Font("Ariel", (int)(height*0.5), (int)(height*0.5));
		g.setFont(font);
		g.drawString(str, (int)(x + width/2 - str.length()*0.5*height*0.23), (int)(y + 0.5*height + 0.17*height));

	}

}
