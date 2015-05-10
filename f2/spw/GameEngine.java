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
	private ArrayList<AtomicBomb> atomicbomb = new ArrayList<AtomicBomb>();
	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.05;
	private int countchocco = 0;
	private int countcandy = 0;
	private int bomb = 0;
	private int heart = 3;

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
	private void generateAtomicBomb(){
		AtomicBomb ab = new AtomicBomb(-100, -10);
		gp.sprites.add(ab);
		atomicbomb.add(ab);
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
		Iterator<AtomicBomb> ab_iter = atomicbomb.iterator();
		while(ab_iter.hasNext()){
			AtomicBomb ab = ab_iter.next();
			ab.proceed();
			
			if(!ab.isAlive()){
				ab_iter.remove();
				gp.sprites.remove(ab);
			}
		}
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double abr;
		for(Enemy e : enemies){
			er = e.getRectangle();
			for(AtomicBomb ab : atomicbomb){
				abr = ab.getRectangle();
				if(abr.intersects(er)){
					score += 100;
					e.notAlive();
				}
			}	
			if(er.intersects(vr)){
				if(heart>0){
					heart--;
					e.notAlive();
				}
				else 
					die();
				return;
			}
		}
		
		Rectangle2D.Double cr;
		for(Candy c : candy){
			cr = c.getRectangle();
			if(cr.intersects(vr)){
				score += 25;
				countcandy++;
				c.notAlive();
				return;
			}
			if(countcandy == 15){
				bomb++;
				countcandy=0;
				
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
			for(AtomicBomb ab : atomicbomb){
				abr = ab.getRectangle();
				if(abr.intersects(gr)){
					score += 100;
					g.notAlive();
				}
			}
			if(gr.intersects(vr)){
				if(heart>0){
					g.notAlive();
					heart--;
				}
				else
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
		case KeyEvent.VK_SPACE:
			if(bomb>0){
				generateAtomicBomb();
				bomb--;
			}
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	public int getCountChocco(){
		return countchocco;
	}

	public int getCountCandy(){
		return countcandy;
	}
	
	public int getBomb(){
		return bomb;
	}

	public int getHeart(){
		return heart;
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
