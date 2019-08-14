import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.applet.*;
import javax.sound.sampled.*;

public class Game extends Applet implements MouseListener, KeyListener
{
	static int sizeX;
	static int sizeY;
	static Boolean[][] board;
	static Boolean oneMove; //true if ones turn false otherwise
	static int gameStage; ==
	static int oneC; //color settings for players
	static int twoC;
	static int offsetX;
	static int offsetY;
	static double size; //board size
	static double sizeChange;
	static boolean isSizing;
	
	private Clip clip = null;

	public void init()
	{
		sizeX = 6;
		sizeY = 7;
		board = new Boolean[sizeX][sizeY];
		oneMove = true;
		oneC = 1; //set colors of the players
		twoC = 2;
		offsetX = 100;
		offsetY = 100;
		size = 1.0;
		addMouseListener(this);
		addKeyListener(this);
		gameStage = 0;
        
        //adds music
		try
		{
			clip = AudioSystem.getClip();
		    clip.open(AudioSystem.getAudioInputStream(new File("theme2.wav")));
		    clip.loop(Clip.LOOP_CONTINUOUSLY);
		    clip.start();
		}
		catch(Exception e){System.out.println(e.toString());}
		repaint();
	}

	static boolean place(int row)
	{
		for(int col = board.length-1;col>=0;col--)
		{
			if(board[col][row]==null)
			{
				board[col][row] = oneMove;
				if(searchWin(row, col, oneMove))
				{
					gameStage = 1;
				}
				return true;
			}
		}
		return false;
	}

	static boolean searchWin(int row, int col, boolean side) //test if any player has won
	{
		int up;
		int right;
		int ocol = col; //to save the original values of row and column during iterating
		int orow = row;

		for(right=-1;right<=1;right++) //try all directions
		{
			for(up=-1;up<=1;up++)
			{
				if(!(right==0&&up==0)) //making sure that it doesn't check the "stay still" combination
				{
					boolean succ = true;
					for(int i=0;i<4;i++)
					{
						if(col>=0&&col<board.length && row>=0&&row<board[col].length)
						{
							if(board[col][row]==null||board[col][row]!=side)
							{
								succ = false;
							}
						}
						else
						{
							succ = false;
						}

						col+=up;
						row+=right;
					}
					if(succ)
					{
						return succ;
					}
					col = ocol;
					row = orow;
				}
			}
		}
		return false;
	}

	public void paint(Graphics g)
	{
		if(gameStage==0)
		{
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier New", Font.PLAIN, 16));
			g.drawString("Click to place a piece. Use arrow keys to scale and number keys to change piece colors.", 2, 10);
			g.setColor(getColor(oneMove));
			g.drawString("PLAYER MOVE", 2, 30);
			g.setColor(Color.BLUE);
			g.fillRect(offsetX, offsetY, (int)(sizeY*100*size), (int)(sizeX*100*size)); //draws background
			for(int col=0;col<board.length;col++) //draws pieces
			{
				for(int row=0;row<board[col].length;row++)
				{
					g.setColor(getColor(board[col][row])); //finds correct color to color piece
					g.fillOval((int)(offsetX+10*size+100*size*row), (int)(offsetY+10*size+100*size*col), (int)(80*size), (int)(80*size));
				}
			}
		}
		if(gameStage==1)
		{
			g.setColor(getColor(!oneMove));
			g.fillRect(0, 0, 1000, 1000);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Comic Sans", Font.PLAIN, 100));
			if(oneMove)
				g.drawString("PLAYER 2 WINS", 100, 500);
			else
				g.drawString("PLAYER 1 WINS", 100, 500);
			g.setFont(new Font("Comic Sans", Font.PLAIN, 60));
			g.drawString("Click to restart!", 100, 700);
		}
		if(sizeChange!=0)
		{
			size+=sizeChange;
			repaint();
		}
	}

	private Color getColor(Boolean b)
	{
		if(b==null)
			return Color.WHITE;
		if(b)
			return color(oneC);
		return color(twoC);
	}


	private Color color(int col)
	{
		switch(col)
		{
		case 1:
			return Color.RED;
		case 2:
			return Color.BLACK;
		case 3:
			return Color.YELLOW;
		case 4:
			return Color.ORANGE;
		}
		return null;
	}

	public void mouseClicked(MouseEvent e)
	{
		if(gameStage == 0)
		{
			int x = e.getX()-offsetX;
			x = (int)(x/(100*size));
			if(x<=board.length&&x>=0)
			{
				place(x);
				oneMove = !oneMove;
			}
			repaint();
		}
		else
		{
			reset();
		}

	}

	public void reset()
	{
		board = new Boolean[sizeX][sizeY];
		oneMove = true;
		gameStage = 0;
		repaint();
	}
	
	public void mousePressed(MouseEvent e)
	{

	}

	public void mouseReleased(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		double speed = 0.001;
		if(key == KeyEvent.VK_RIGHT)
		{
			sizeChange = speed;
		}
		if(key == KeyEvent.VK_LEFT)
		{
			sizeChange = -speed;
		}
		if(key == KeyEvent.VK_1)
		{
			oneC = 1;
		}
		if(key == KeyEvent.VK_2)
		{
			oneC = 2;
		}
		if(key == KeyEvent.VK_3)
		{
			oneC = 3;
		}
		if(key == KeyEvent.VK_4)
		{
			oneC = 4;
		}
		
		if(key == KeyEvent.VK_5)
		{
			twoC = 1;
		}
		if(key == KeyEvent.VK_6)
		{
			twoC = 2;
		}
		if(key == KeyEvent.VK_7)
		{
			twoC = 3;
		}
		if(key == KeyEvent.VK_8)
		{
			twoC = 4;
		}
		repaint();
	}
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT && sizeChange<0)
			sizeChange = 0;
		if(key == KeyEvent.VK_RIGHT && sizeChange>0)
			sizeChange = 0;
	}

}
