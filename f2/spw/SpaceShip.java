package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpaceShip extends Sprite{

	int step = 50;
	BufferedImage image;

	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		try{
			image = ImageIO.read(new File("f2/image/SpaceShip.png"));
		}
		catch(IOException e){

		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, width, height, null);
	}

	public void moveLR(int direction){
		x += (step * direction);
		if(x < 0)
			x = 0;
		if(x > 380 - width)
			x = 380 - width;
	}

	public void moveUD(int direction){
		y += (step * direction);
		if(y < 0)
			y = 0;
		if(y > 555 - width)
			y = 555 - width;
	}
}
