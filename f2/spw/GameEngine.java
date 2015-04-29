package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<GhostEnemy> ghost = new ArrayList<GhostEnemy>();
	private ArrayList<Candy> candy = new ArrayList<Candy>();
	private ArrayList<Chocco> chocco = new ArrayList<Chocco>();
	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.05;
	private int countchocco = 0;

	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*360), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	private void generateCandy(){
		Candy c = new Candy((int)(Math.random()*360), 30);
		gp.sprites.add(c);
		candy.add(c);
	}
	private void generateChocco(){
		Chocco ch = new Chocco((int)(Math.random()*360), 30);
		gp.sprites.add(ch);
		chocco.add(ch);
	}
	private void generateGhostEnemy(){
		GhostEnemy g = new GhostEnemy((int)(Math.random()*360), 30);
		gp.sprites.add(g);
		ghost.add(g);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		if(Math.random() < 0.1){
			generateCandy();
		}
		if(Math.random() < 0.05){
			generateChocco();
		}
		if(Math.random() < 0.05){
			generateGhostEnemy();
		}
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 10;
			}
		}
		Iterator<GhostEnemy> g_iter = ghost.iterator();
		while(g_iter.hasNext()){
			GhostEnemy g = g_iter.next();
			g.proceed();
			
			if(!g.isAlive()){
				g_iter.remove();
				gp.sprites.remove(g);
				score += 20;
			}
		}
		Iterator<Candy> c_iter = candy.iterator();
		while(c_iter.hasNext()){
			Candy c = c_iter.next();
			c.proceed();
			
			if(!c.isAlive()){
				c_iter.remove();
				gp.sprites.remove(c);
			}
		}
		Iterator<Chocco> ch_iter = chocco.iterator();
		while(ch_iter.hasNext()){
			Chocco ch = ch_iter.next();
			ch.proceed();
			
			if(!ch.isAlive()){
				ch_iter.remove();
				gp.sprites.remove(ch);
			}
		}
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				die();
				return;
			}
		}
		
		Rectangle2D.Double cr;
		for(Candy c : candy){
			cr = c.getRectangle();
			if(cr.intersects(vr)){
				score += 25;
				c.notAlive();
				return;
			}
		}

		Rectangle2D.Double chr;
		for(Chocco ch : chocco){
			chr = ch.getRectangle();
			if(chr.intersects(vr)){
				score += 50;
				countchocco++;
				ch.notAlive();
				return;
			}

			if(countchocco == 15){
				countchocco=0;
				score += 1000;
			}
		}

		Rectangle2D.Double gr;
		for(GhostEnemy g : ghost){
			gr = g.getRectangle();
			if(gr.intersects(vr)){
				die();
				return;
			}
		}
	
	}
	
	public void die(){
		timer.stop();
	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			v.moveLR(-1);
			break;
		case KeyEvent.VK_RIGHT:
			v.moveLR(1);
			break;
		case KeyEvent.VK_UP:
			v.moveUD(-1);
			break;
		case KeyEvent.VK_DOWN:
			v.moveUD(1);
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		case KeyEvent.VK_R:
			score = 0;
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	public int getCountChocco(){
		return countchocco;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
