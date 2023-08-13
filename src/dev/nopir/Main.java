package dev.nopir;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static int width;
	public static int height;
	
	public Main(int width, int height) {
		Main.width = width; //yeet
		Main.height = height;
		
		Display display = new Display(this);
		add(display);
		
		setUndecorated(true);
		setSize(width,height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);
		
		addKeyListener(display);
		
	}
	
	public static void main(String[] args) {
		new Main(500,500);
	}
}
