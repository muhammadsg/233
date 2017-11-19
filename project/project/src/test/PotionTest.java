package test;
import static org.junit.Assert.*;
import org.junit.Test;

import game.Potion;

public class PotionTest 
{	
	private class Temp extends Potion
	{
		public Temp(String name, int healingAmount)
		{
			super(name, healingAmount);
		}
	}
	
	//Constructor Test default name and healing amount
	@Test
	public void constructorTest() 
	{
		Potion pot1 = new Temp("Default", 10);
		assertEquals("Potion should be named 'Default'","Default",pot1.getName());
		assertEquals("Potion should have healing amount 10",10,pot1.getHealingAmount());
	}
	
	//getHealingAmount test
	@Test
	public void test_getHealingAmount()
	{
		Potion pot1 = new Temp("Default", 5);
		assertEquals("Potion created with healing amount 5, should show this", 5, pot1.getHealingAmount());
    }

	//Constructor Test with negative value
	@Test
	public void test_constructor_NegativeHealing()
	{
		Potion pot1 = new Temp("Default", -10);
		assertEquals("Potion should show -10 as healing amount",-10,pot1.getHealingAmount());
	}
  
	//Constructor Test with no healing
	@Test
	public void test_constructor_ZeroHealing()
	{
		Potion pot1 = new Temp("Default", 0);
		assertEquals("Potion should show 0 as healing amount",0,pot1.getHealingAmount());
	}
  
}