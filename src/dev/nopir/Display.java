package dev.nopir;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class Display extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	Main main;
	
	int boxOffset = 80;
	
	int tileSize;
	
	int numberOfTiles = 10;
	
	Tile[][] tiles;
	
	int[][] rowNumbers;
	int[][] columnNumbers;
	
	boolean check = false;
	
	class Tile {
		public int x, y, size;
		
		Rectangle box;
		
		public boolean isBomb = false;
		
		public boolean isLeftClicked = false;
		public boolean isRightClicked = false;
		
		public Tile(int x, int y, int size) {
			this.x = x;
			this.y = y;
			this.size = size;
			
			box = new Rectangle(x,y,size,size);
			
			if((int)(Math.random()*2) == 0)
				isBomb = true;
		}
	}
	
	public Display(Main main) {
		this.main = main;
		
		setBackground(Color.lightGray);
		
		reset();
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Tile tile;
		
		for(int i = 0; i < tiles.length; i++)
		for(int j = 0; j < tiles[i].length; j++) {
			tile = tiles[i][j];
			
			g.setColor(Color.lightGray);
			
			if(tile.isLeftClicked)
				g.setColor(Color.black);
			
			
			g.fillRect(tile.x,tile.y,tile.size,tile.size);
			
			g.setColor(Color.black);
			g.drawRect(tile.x,tile.y,tile.size,tile.size);
			
			if(tile.isRightClicked)
				g.drawString("X",tile.x+tile.size/2,tile.y+tile.size/2);
			
			if(!check) continue;
			
			g.setColor(new Color(230,100,100));
			
			if((tile.isLeftClicked && tile.isBomb))
				g.setColor(new Color(100,230,100));
			else if((!tile.isLeftClicked && !tile.isBomb))
				g.setColor(Color.lightGray);
			
			g.fillRect(tile.x,tile.y,tile.size,tile.size);
			
			g.setColor(Color.black);
			g.drawRect(tile.x,tile.y,tile.size,tile.size);
				
			
		}
		
		for(int i = 0; i < rowNumbers.length; i++) {
			int offset = -15;
			
			for(int j = rowNumbers[i].length-1; j >= 0; j--) {
				if(rowNumbers[i][j] != 0) {
					g.drawString(rowNumbers[i][j]+"",tiles[i][0].x+offset,tiles[i][0].y+tiles[i][0].size/2);
					offset -= 10;
				}
			}
		}
		
		int index = 0;
		for(int i = 0; i < columnNumbers.length; i++) {
			int offset = -10;
			
			for(int j = columnNumbers[i].length-1; j >= 0; j--) {
				if(columnNumbers[i][j] != 0) {
					g.drawString(columnNumbers[i][j]+"",tiles[0][index].x+tiles[0][index].size/2,tiles[0][index].y+offset);
					offset -= 15;
				}
			}
			
			index++;
		}
		
		
		sleepAndRefresh();
	}
	
	public void reset() {
		tiles = new Tile[numberOfTiles][numberOfTiles];
		
		int currentRowNumber = 0; //used for drawing numbers on the side. this number will group tiles together
		int currentColumnNumber = 0;
		
		rowNumbers = new int[numberOfTiles][100];
		columnNumbers = new int[numberOfTiles][100];
		
		tileSize = (Main.height / numberOfTiles) - numberOfTiles;
		
		for(int i = 0; i < tiles.length; i++)
		for(int j = 0; j < tiles[i].length; j++) {
			tiles[i][j] = new Tile(boxOffset+j*tileSize,boxOffset+i*tileSize,tileSize);
			
			if(tiles[i][j].isBomb)
				rowNumbers[i][currentRowNumber]++;
			else
				currentRowNumber++;
		}
		
		for(int i = 0; i < tiles[0].length; i++)
		for(int j = 0; j < tiles.length; j++)
			if(tiles[j][i].isBomb)
				columnNumbers[i][currentColumnNumber]++;
			else
				currentColumnNumber++;
		
		check = false;
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) check = true;
		
		if(e.getKeyCode() == KeyEvent.VK_F1) reset();
	}
	
	public void mousePressed(MouseEvent e) {
		for(int i = 0; i < tiles.length; i++)
		for(int j = 0; j < tiles[i].length; j++) {
			Tile tile = tiles[i][j];
			
			if(!tile.box.contains(e.getPoint())) continue;
			
			if(tile.isLeftClicked || tile.isRightClicked) {
				tile.isLeftClicked = false;
				tile.isRightClicked = false;
			} else if(e.getButton() == MouseEvent.BUTTON1) {
				tile.isLeftClicked = !tile.isLeftClicked;
				tile.isRightClicked = false;
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				tile.isRightClicked = !tile.isRightClicked;
				tile.isLeftClicked = false;
			}
		}
	}
	
	public void mouseDragged(MouseEvent e) {
	
	}
	
	public void mouseMoved(MouseEvent e) {
	
	}


	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	float lastRefresh = 0;
	float maxFPS = 120;
	void sleepAndRefresh() {
		long timeSLU = (long) (System.currentTimeMillis() - lastRefresh);
		
		if(timeSLU < 1000.0/maxFPS)
			try {
				Thread.sleep((long) (1000.0/maxFPS - timeSLU));
			} catch(Exception e) {}
		
		lastRefresh = System.currentTimeMillis();
		
		repaint();
	}
}
