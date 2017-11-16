package game;

import java.util.Scanner;

/*
 * This class is the maze that tracks the location of the
 * player, enemy, and items 
 */
public class Map
{
    private String maze[][];
    private Enemy enemy = new Enemy("Default",1,1,1);
    private Potion item = new Potion("Default", 1);
    private AudioPlayer audioPlayer = new AudioPlayer();
    private String selectedStage = "stage1.txt";
    private boolean foundEnemy = false;
    private boolean foundItem = false;
    private int enemyID = 0;
    private int itemID = 0;
    /*
     * This constructor initializes the game, loads all the game elements onto the map
     */
	public Map () 
	{	
		audioPlayer.startGameMusic();
		FileReader fileReader = new FileReader();
		maze = fileReader.translateData(fileReader.readFile("stage1.txt"));
	}
	/*
	 * This method prints the player input controls
	 */
	public void displayMenu()
	{
		System.out.println(" ---------------------------------------");
		System.out.println("MOVEMENT OPTIONS");
		System.out.println("   w   ");
		System.out.println("a  i  d");
		System.out.println("   s   ");
		System.out.println("Type one of the letters above to indicate direction of movement");
		System.out.println("or i for character menu");
	}
	/*
	 * This method prints the maze grid
	 * @param game The map
	 */
	public void displayMaze()
	{
		String[][] maze = getMaze();
		for (int i = 0; i<maze.length; i++)
		{
			System.out.print(" _");
		}
		System.out.println();
		for (int row = 0; row<maze.length; row++)
		{
		     for (int col = 0; col<maze[row].length; col++)
		     {
		    	 System.out.print("|" + maze[row][col]);
		     } 
		     System.out.print("|");
		     System.out.println();
		}
	}
	/*
	 * This method prints the inventory and player info
	 * @param player The player
	 * @param keyboard The scanner
	 */
	public void displayInventory(Player player, Scanner keyboard)
	{
		boolean exit = false;
		while(!(exit)) 
		{
			System.out.println("Health: " + player.getHealth());
			System.out.println("Level: " + player.getCurrentLevel());
			System.out.println("EXP: " + player.getCurrentExp() + "/" + player.getExpToLvl() + "\n");
			System.out.println("Small Potion: " + player.getNumSmallPotions() + "  \tType 1 to consume a small potion");
			System.out.println("Medium Potion: " + player.getNumMediumPotions() + "  \tType 2 to consume a medium potion");
			System.out.println("Large Potion: " + player.getNumLargePotions() + "  \tType 3 to consume a large potion");
			System.out.println("\nType i to exit menu");
			switch(keyboard.next())
			{
			case "1":
				player.useItem(1);
				break;
			case "2":
				player.useItem(2);
				break;
			case "3":
				player.useItem(3);
				break;
			case "i":
				exit = true;				
				break;
			}
		}
	}
	/*
	 * This method prints the player and enemy's interactions during battle
	 * @param player The player
	 * @param enemy The enemy
	 * @param game The map
	 * @param keyboard The scanner
	 */
	public void startBattle(Player player, Enemy enemy, Map game, Scanner keyboard)
	{
		while(player.getHealth() > 0 && enemy.getHealth() > 0)
		{
			System.out.println("Player Name: " + player.getName());
			System.out.println("Player HP: " + player.getHealth());
			System.out.println("Enemy Name: " + enemy.getName());
			System.out.println("Enemy HP: " + enemy.getHealth());
			System.out.println("Type a to attack");
			String choice = keyboard.next();
			if (choice.equals("a"))
			{
				audioPlayer.playAttackSFX();
				player.attack(player, enemy);
				System.out.println("You hit the " + enemy.getName() + " for " + player.getDamage() + " damage!");
				if (enemy.isAlive())
				{
					enemy.attack(player, enemy);
					System.out.println(enemy.getName() + " has hit you for " + enemy.getDamage() + " damage!");
				}
				else
				{
					audioPlayer.startGameMusic();
					audioPlayer.stopBattleMusic();
					game.foundEnemy(false);
					player.obtainExp(enemy);
					player.checkExp();
				}
			}	
		}
	}
	/*
	 * This method returns the row index of the player
	 */
	private int getRow()
	{
		int playerRow = 0;
		for (int row = 0; row<maze.length; row++)
		{
		     for (int col = 0; col<maze[row].length; col++)
		     {
		    	 if (maze[row][col] == "X")
		    	 {
		    		 playerRow = row;
		    	 }
		     } 
		}
		return playerRow;
	}
	/*
	 * This method returns the column index of the player
	 */
	private int getCol()
	{
		int playerCol = 0;
		for (int row = 0; row<maze.length; row++)
		{
		     for (int col = 0; col<maze[row].length; col++)
		     {
		    	 if (maze[row][col] == "X")
		    	 {
		    		 playerCol = col;
		    	 }
		     } 
		}
		return playerCol;
	}
	/*
	 * This method moves the player on the grid
	 */
	public void move(String choice)
	{
		int playerRow = getRow();
		int playerCol = getCol();
		// remove player from current position
		maze[playerRow][playerCol] = " ";
		switch (choice)
		{
		case "s":
			playerRow += 1;
			break;
		case "a":
			playerCol -= 1;
			break;
		case "d":
			playerCol += 1;
			break;
		case "w":
			playerRow -= 1;
			break;
		}
		// checks if player encounters an object or challenge
		checkEvent(maze,playerRow,playerCol);
		// sets player to new position if move is valid
		if (moveValid(playerRow, playerCol))
		{
			maze[playerRow][playerCol] = "X";
			audioPlayer.playMovementSFX();
		}
		else // player does not move, returns to origin
		{
			switch (choice)
			{
			case "s":
				playerRow -= 1;
				break;
			case "a":
				playerCol += 1;
				break;
			case "d":
				playerCol -= 1;
				break;
			case "w":
				playerRow += 1;
				break;
			}
			maze[playerRow][playerCol] = "X";	
		}
	}
	/*
	 * This method returns true if player is moving into an valid space
	 * @param playerRow The row index of player
	 * @param playerCol The column index of player
	 */
	private boolean moveValid(int playerRow, int playerCol)
	{
		return (!(maze[playerRow][playerCol] == "#")
			  &&!(maze[playerRow][playerCol] == "d")) ? true : false;
	}
	/*
	 * This method starts a battle when player encounters a challenge, 
	 * and picks up a item when player encounters a object
	 * @param maze The 2d array the player is traversing
	 * @param playerRow The row index of player
	 * @param playerCow The column index of player
	 */
	private void checkEvent(String maze[][],int playerRow, int playerCol)
	{
		if (maze[playerRow][playerCol] == "?")
		{
			audioPlayer.playPotionSFX();
			foundItem = true;
			item = new Spawner("Items.txt").createItem(identifyItem(playerRow, playerCol));

		}
		else if (maze[playerRow][playerCol] == "E")
		{
			audioPlayer.stopGameMusic();
			audioPlayer.startBattleMusic();
			foundEnemy(true);
			enemy = new Spawner("Enemies.txt").spawnEnemy(identifyEnemy(playerRow, playerCol));
		}
	}
	/*
	 * This method returns true if player reaches the end of the maze,
	 * false otherwise
	 */
	public boolean gameWon() 
	{
		return (maze[0][18] == "X") && selectedStage == "stage3.txt" ? true : false;
	}
	/*
	 * This method checks to see if a stage has been complete
	 * When a stage is complete it will load the next stage
	 */
	public void checkStageCompletion()
	{
		FileReader fileReader = new FileReader();
		if (selectedStage == "stage1.txt" && maze[9][19] == "X")
		{
			maze = fileReader.translateData(fileReader.readFile("stage2.txt"));
			selectedStage = "stage2.txt";
		}
		else if (selectedStage == "stage2.txt" && (maze[19][1] == "X"))
		{
			maze = fileReader.translateData(fileReader.readFile("stage3.txt"));
			selectedStage = "stage3.txt";
		}
	}
	/*
	 * This is the getter method for the grid, returns maze
	 */
	public String[][] getMaze()
	{
		return maze;
	}
	/*
	 * This is the getter method for the current stage, returns stage
	 */
	public int currentStage()
	{
		int stage = 0;
		if (selectedStage == "stage1.txt")
		{
			stage = 1;
		}
		else if (selectedStage == "stage2.txt")
		{
			stage = 2;
		}
		else if (selectedStage == "stage3.txt")
		{
			stage = 3;
		}
		return stage;
	}
	/*
	 * This method determines which enemy is encountered, returns the enemy ID 
	 * @param playerRow The player's row index on the array
	 * @param playerCol The player's column index on the array
	 */
	public int identifyEnemy(int playerRow, int playerCol)
	{
		if (currentStage() == 1)
		{
			enemyID = 1;
		}
		else if (currentStage() == 2)
		{
			if (playerRow == 18 && playerCol == 1)
			{
				enemyID = 3;
			}
			else
			{
				enemyID = 2;
			}
		}
		else if (currentStage() == 3)
		{
			if (playerRow == 1 && playerCol == 18)
			{
				enemyID = 5;
			}
			else
			{
				enemyID = 4;
			}
		}
		return enemyID;
	}
	/*
	 * This method determines which enemy is encountered, returns the enemy ID 
	 * @param playerRow The player's row index on the array
	 * @param playerCol The player's column index on the array
	 */
	public int identifyItem(int playerRow, int playerCol)
	{
		if (currentStage() == 1)
		{
			if (playerRow == 11 && playerCol == 6)
			{
				itemID = 4;
			}
			else
			{
				itemID = 1;
			}			
		}
		else if (currentStage() == 2)
		{
			if (playerRow == 16 && playerCol == 18)
			{
				itemID = 5;
			}
			else
			{
				itemID = 2;
			}			
		}
		else if (currentStage() == 3)
		{
			if (playerRow == 2 && playerCol == 6)
			{
				itemID = 6;
			}
			else
			{
				itemID = 3;
			}			
		}
		return itemID;
	}
	/*
	 * This is the getter method for the foundEnemy variable, returns foundEnemy
	 */
	public boolean foundEnemy()
	{
		return foundEnemy;
	}
	/*
	 * This is the setter method for the foundEnemy variable
	 * @param truthValue The foundEnemy variable
	 */
	public void foundEnemy(boolean truthValue)
	{
		foundEnemy = truthValue;
	}
	/*
	 * This is the getter method for the foundItem variable, returns foundItem
	 */
	public boolean foundItem()
	{
		return foundItem;
	}
	/*
	 * This is the setter method for the foundItem variable
	 * @param truthValue The foundItem variable
	 */
	public void foundItem(boolean truthValue)
	{
		foundItem = truthValue;
	}
	/*
	 * This is the getter method for the enemyID, returns enemyID
	 */
	public int getEnemyID()
	{
		return enemyID;
	}
	/*
	 * This is the getter method for the itemID, returns itemID
	 */
	public int getitemID()
	{
		return itemID;
	}
	/*
	 * This is the getter method for the enemy, returns enemy
	 */
	public Enemy getEnemy()
	{
		return enemy;
	}
	/*
	 * This is the getter method for the item, returns item
	 */
	public Potion getItem()
	{
		return item;
	}
	
	public AudioPlayer getAudioPlayer()
	{
		return audioPlayer;
	}
}