import javax.swing.*;

import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * This class models a single Tower in a Towers of Hanoi Simulation.
 */
public class Tower extends JButton 
{
	private int animationDelayMS = 500;		// Speed of animation when auto-complete is turned on.
	private int towerHeight;			    // Number of disks on this tower.
	private Disk[] disks;				    // Disks on this tower, in order (bottom to top).

	/**
	 * Tower constructor. 
	 * The Tower is initially created empty.
	 *
	 * @param numberOfDisks The total number of disks in the game.
	 * @param animationDelay: The inter-frame delay used during animation.
	 */
	public Tower(int numberOfDisks, int animationDelayMS)
	{
		this.animationDelayMS = animationDelayMS;
		this.disks = new Disk[numberOfDisks];
		this.towerHeight = 0;
	}

	/** 
	 * @return Disk[]
	 */
	public Disk[] getDisks()
	{
		return disks;
	}
	
	/** 
	 * @param disks
	 */
	public void setDisks(Disk[] disks)
	{
		this.disks = disks;
	}

	/**
	 * Move the given number of disks from this tower to the tower specified, without
	 * breaking the rules of the puzzle.
	 *
	 * @param destination The Tower to move the disks to.
	 * @param disksToMove The number of disks to move from this tower (counted from the top of the tower)
	 * @param towers Collection of all the towers in the game.
	 */
	public void move(Tower destination, int disksToMove, Tower[] towers)
	{
		// Work out the spare tower.
		Tower via = otherTower(this, destination, towers);

		// Logic for the movement of discs.
		if(disksToMove == 1)
		{
			Disk d = this.removeDisk();
			destination.addDisk(d);
			// Update the GUI. Do this in the terminating condition...
			this.repaint();
			destination.repaint();
			this.animationDelay();
		}
		else
		{
			this.move(via, disksToMove-1, towers);
			this.move(destination, 1, towers);
			via.move(destination, disksToMove-1, towers);
		}
	}

	/**
	 * This assumes it's called on the tower with the biggest disk at the bottom.
	 */
	public void solve(Tower destination, int disksToMove, Tower[] towers, TreeMap<Disk, Integer> map)
	{
		// Work out the spare tower.
		Tower via = otherTower(this, destination, towers);

		// The final(smallest) disk to be placed on top.
		if(disksToMove == 1)
		{
			// FIXME Can be optimised.
			Disk d = this.removeDisk();
			destination.addDisk(d);

			// Update the GUI. Do this in the terminating condition...
			this.repaint();
			destination.repaint();
			this.animationDelay();
		}
		else
		{
			// Optimisation.
			// If there are already disks at the of the destination, 
			// that are already in place and in the correct order.
			// System.out.println(disksToMove - 1);
			// System.out.println(destination.getDisks()[ disksToMove - 1 ]);
			// System.out.println(destination.getDisks()[ disksToMove - 1 ]);
			// while( towers[ map.get( (Disk) map.keySet().toArray()[disksToMove - 1] ) ] == destination )
			System.out.println(map);
			System.out.println(disksToMove - 1 + "disksToMove");
			System.out.println((int) map.values().toArray()[disksToMove -1]);

			// while( towers[ (int) map.values().toArray()[disksToMove - 1] ] == destination )
			// {
			// 	disksToMove--;
			// }

			System.out.println("end");

			// Find the tower with the next biggest disk.
			Tower nextTower;

			

			// this.solve(via, disksToMove-1, towers, map);
			// this.solve(destination, 1, towers, map);
			// via.solve(destination, disksToMove, towers, map);

			// // this.solve(via, disksToMove, towers, map);
			// // map.higherKey(key)
			// this.move(via, disksToMove-1, towers);
			// this.move(destination, 1, towers);
			// via.move(destination, disksToMove-1, towers);
		}
	}

	public Disk findDisk(int disksToMove, HashMap<Disk, Integer> map)
	{
		return (Disk) map.keySet().toArray()[disksToMove - 1];
	}

	/**
	 * Utility function that finds the 'spare' tower from the given collection of three towers.
	 * @param a The first tower.
	 * @param b The second tower.
	 * @param towers The collection of all towers.
	 * @return the third tower from the given collection, that is neither a nor b.
	 */
	public Tower otherTower(Tower a, Tower b, Tower[] towers)
	{
		for (Tower t : towers)
		{
			if (t != a && t != b)
				return t;
		}

		return null;
	}

	/**
	 * Adds this given disk to the top of this tower, provided it is a legal move.
	 * The move is legal IFF the new disk is smaller than the current top disk, or this tower is empty.
	 * @param d The disk to add.
	 * @return True if the disk was added. False otherwise.
	 */
	public boolean addDisk(Disk d)
	{
		if (towerHeight == 0 || d.getWidth() < disks[towerHeight-1].getWidth())
		{
			disks[towerHeight] = d;
			towerHeight++;
			return true;
		}

		return false;
	}

	/**
	 * Removes the top disk from this tower, if the tower is not empty.
	 * @return the top disk, if the tower is not empty. Null otherwise.
	 */
	public Disk removeDisk()
	{
		Disk d = null;

		if (towerHeight > 0)
		{
			d = disks[towerHeight-1];
			towerHeight--;
		}
		
		return d;
	}

	/**
	 * Accessor method.
	 * @return The number of disks in this tower.
	 */
	public int getTowerHeight()
	{
		return towerHeight;
	}


	/**
	 * Wait for an inter-frame delay, as specified in the tower constructor
	 */
	public void animationDelay()
	{
		try {
			Thread.sleep(animationDelayMS);
		} catch (Exception e) {}
	}

	/**
	 * Highlight (or de-highlight) the visualisation of all the disks in this tower in the GUI.
	 * @param b True to highlight this tower, False to de-highlight this tower.
	 */
	public void setHighlight(boolean b)
	{
		if (towerHeight > 0)
			disks[towerHeight-1].setHighlight(b);
	}

	/**
	 * Paint method. Overrides paint method to draw the tower and disks.
	 * @param gr. The graphcis context on which to draw the towers and disks.
	 */
	public void paint(Graphics gr)
	{
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		synchronized(this)
		{
			int width = this.getWidth()/20;
			int height = (int)(this.getHeight() * 0.9);
			
			g.clearRect(0,0,getWidth(),getHeight());

			g.setColor(Color.GRAY);
			g.fill3DRect(this.getWidth()/2-width/2, this.getHeight()-height, width, height, true);

			for (int i = 0; i < towerHeight; i++)
				disks[i].drawOn(g, i, this);
		}
			
		gr.drawImage(img, 0, 0, this);
	}

}
