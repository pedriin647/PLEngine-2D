package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {


	public static JFrame frame;
	
	private boolean isRunning = true;
	private Thread thread;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int scale = 3;
	private BufferedImage image;
	
	public Game() {
		setPreferredSize(new Dimension(WIDTH*scale,HEIGHT*scale));
		FrameInit();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
	}
	

	public void FrameInit() {
		frame = new JFrame("PL Engine");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) {
		
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		
	}
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(19,19,19));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		g.setFont(new Font("Arial",Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Olá Mundo!", 20,20);
		
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*scale, HEIGHT*scale, null);
		bs.show();
		
		
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >=1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				
				System.out.println("FPS:" + frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
		
		
	}
	

}
