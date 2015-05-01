package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	BufferedImage image;
	BufferedImage countchocco;
	BufferedImage countcandy;
	BufferedImage bomb;
	
	public GamePanel() {
		bi = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		//big.setBackground(Color.PINK);
		try{
			image = ImageIO.read(new File("f2/image/bg.jpg"));
		}
		catch(IOException e){

		}

		try{
			countchocco = ImageIO.read(new File("f2/image/Chocco.png"));
		}
		catch(IOException e){
		
		}

		try{
			countcandy = ImageIO.read(new File("f2/image/Candy.png"));
		}
		catch(IOException e){
		
		}

		try{
			bomb = ImageIO.read(new File("f2/image/Bomb.png"));
		}
		catch(IOException e){
		
		}
	}

	public void updateGameUI(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		
		big.setColor(Color.BLACK);	
		big.drawImage(image, 0, 0, 400, 600, null);
		big.drawString(String.format("%04d", reporter.getScore()), 20, 20);
		
		big.setColor(Color.BLACK);
		big.drawImage(countchocco, 320, 50, 25, 25, null);
		big.drawString(String.format("%02d", reporter.getCountChocco()), 350, 60);

		big.setColor(Color.BLACK);
		big.drawImage(countcandy, 250, 15, 30, 20, null);
		big.drawString(String.format("%02d", reporter.getCountCandy()), 290, 30);

		big.setColor(Color.BLACK);
		big.drawImage(bomb, 310, 5, 35, 35, null);
		big.drawString(String.format("%02d", reporter.getBomb()), 350, 30);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
