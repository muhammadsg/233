package project;

import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * This class is a panel that displays the maze, player, items, and enemies
 */
public class GamePanel 
{
	private ImageIcon wallDark = new ImageIcon("WallDark.png");
	private ImageIcon wall = new ImageIcon("wall.png");
	private ImageIcon road = new ImageIcon("grass.png");
	private ImageIcon player = new ImageIcon("player.png");
	private ImageIcon item = new ImageIcon("item.png");
	private ImageIcon zombie = new ImageIcon("zombie.png");
	private ImageIcon skeletonMinion = new ImageIcon("skeleton_minion.png");
	private ImageIcon skeletonKing = new ImageIcon("skeleton_king.png");
	private ImageIcon blackKnight = new ImageIcon("black_knight.png");
	private ImageIcon reaper = new ImageIcon("reaper.png");
	
	private JLabel[][] labels;
	private final int ROW = 20;
	private final int COL = 20;
	
	public JPanel createPanel(Map game) 
	{ 
		// panel displaying the maze
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(ROW,COL));
		// creates 20 x 20 labels and adds to panel grid
		labels = new JLabel[ROW][COL];
		for (int r = 0; r < ROW; r++)
		{
		     for (int c = 0; c < COL; c++)
		     {
		    	 labels[r][c] = new JLabel();
		    	 panel.add(labels[r][c]);
		     }
		}
		update(game);
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		JPanel gamePanel = new JPanel();
		gamePanel.setBounds(0, 0, 650, 650);
		gamePanel.add(panel);
		return gamePanel;
	}
	/*
	 * This method updates the maze with the correct images
	 * @param game The map
	 */
	public void update(Map game)
	{
		String[][] maze = game.getMaze();
		for (int i = 0; i < ROW; i++)
		{
		     for (int j = 0; j < COL; j++)
		     {
		    	 if (maze[i][j] == "#") 
		    	 {
		    		 labels[i][j].setIcon(wall); 
		    	 }
		    	 else if(maze[i][j] == "d") 
		    	 {
		    		 labels[i][j].setIcon(wallDark);
		    	 }
		    	 else if (maze[i][j] == " ") 
		    	 {
		    		 labels[i][j].setIcon(road);
		    	 }
		    	 else if (maze[i][j] == "X") 
		    	 {
		    		 labels[i][j].setIcon(player);
		    	 }
		    	 else if (maze[i][j] == "E")
		    	 {
		    		  ;
		    		 if (game.currentStage() == 1)
		    		 {
		    			 labels[i][j].setIcon(zombie);
		    		 }
		    		 else if (game.currentStage() == 2)
		    		 {
		    			 if (i == 18 && j == 1)
		    			 {
		    				 labels[i][j].setIcon(skeletonKing);
		    			 }
		    			 else
		    			 {
		    				 labels[i][j].setIcon(skeletonMinion);
		    			 }		 
		    		 }
		    		 else if (game.currentStage() == 3)
		    		 {
		    			 if (i == 1 && j == 18)
		    			 {
		    				 labels[i][j].setIcon(reaper);
		    			 }
		    			 else
		    			 {
		    				 labels[i][j].setIcon(blackKnight);
		    			 }		 
		    		 }
		    	 }
		    	 else if (maze[i][j] == "?") 
		    	 {
		    		 labels[i][j].setIcon(item);
		    	 }
		    	 
		     } 
		}
	}
}