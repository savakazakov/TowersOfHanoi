import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.*;

public class Hanoi extends JFrame implements ActionListener, Runnable
{
	private static final int TOWER_COUNT = 3;

	private Tower[] towers = new Tower[TOWER_COUNT];
	private JButton moveButton = new JButton("Move");
	private JButton randomiseButton = new JButton("Randomise");
	private Tower selectedTower = null;

	// An ordered map to store the disks and their tower.
	private TreeMap<Disk, Integer> map;

	public Hanoi(int numberOfDisks, int animationDelay)
	{
		super("Towers of Hanoi");
		this.setSize(600,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int diskHeight = Math.min(20, (this.getHeight()-80) / numberOfDisks);

		JPanel p = new JPanel();
		JPanel towerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		towerPanel.setLayout(new GridLayout(1, TOWER_COUNT));
		p.setLayout(new BorderLayout());
		p.add(towerPanel, BorderLayout.CENTER);
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(moveButton);
		buttonPanel.add(randomiseButton);
		moveButton.addActionListener(this);
		randomiseButton.addActionListener(this);
		p.add(buttonPanel, BorderLayout.SOUTH);
		this.setContentPane(p);
		
		// Initialise the map.
		map = new TreeMap<>();

		// Create the towers
		for(int i = 0; i < TOWER_COUNT; i++)
		{
			towers[i] = new Tower(numberOfDisks, animationDelay);
			towers[i].setSize((this.getWidth() - 50) / TOWER_COUNT, this.getHeight() - 80);
			towerPanel.add(towers[i]);

			towers[i].addActionListener(this);
		}

		// Create the disks.
		for(int i = 0; i < numberOfDisks; i++)
		{
			float ratio = (float)(numberOfDisks-i) / (float)numberOfDisks;
			float brightness = 0.2f + ratio * 0.8f;

			Disk d = new Disk(new Color(0, brightness, 0), (int)((float)towers[0].getWidth() * ratio), diskHeight);

			map.put(d, 0);
			towers[0].addDisk(d);
		}

		// Make everything visible
		this.setVisible(true);
	}

	public HashMap<Disk, Integer> creatMap()
	{
		
		return null;
	}

	public void actionPerformed(ActionEvent e)
	{
		// XXX remove this from here.

		JButton b = (JButton)e.getSource();

		// moveButton.setEnabled(false);

		if(b == moveButton)
		{
			Thread t = new Thread(this);
        		t.start();
		}
		else if(b == randomiseButton)
		{
			// Get the number of disks in each tower.
			// int numberOfDisks = towers[0].getDisks().length;

			// Remove all the disks from all the towers.
			for(int i = 0; i < towers.length; i++)
			{
				Disk d = towers[i].removeDisk();
				while(d != null)
					d = towers[i].removeDisk();
			}

			// Instantiate the random generator.
			Random rand = new Random();
			int randNum;

			// Instantiate an iterator for the map.
			Iterator<Disk> itr = map.descendingKeySet().iterator();
      
			while(itr.hasNext())
			{
				// FIXME Cast code smell.
				Disk d = (Disk) itr.next();

				// Generate a random tower
				randNum = rand.nextInt(TOWER_COUNT);

				// Update the map and add the disk to the tower.
				map.replace(d, randNum);
				towers[randNum].addDisk(d);
			}
			System.out.println(map);
		}
		else 
		{
			Tower t = (Tower) b;

			// The user is trying to move a disk
			if (this.selectedTower == null)
			{
				if (t.getTowerHeight() > 0)
				{
					this.selectedTower = (Tower) b;
					this.selectedTower.setHighlight(true);
					return;
				}
			} else {

				this.selectedTower.setHighlight(false);
				Disk d = this.selectedTower.removeDisk();

				if (t.addDisk(d))
				{
					this.selectedTower = null;
				}
				else
				{
					this.selectedTower.addDisk(d);
					this.selectedTower.setHighlight(true);
				}
			}
		}

		this.repaint();
	}	

	public void run()
	{
		// Perform a recursive move of all the disks from tower0 to tower2...
        // towers[0].move(towers[2], towers[0].getTowerHeight(), towers);
		towers[0].solve(towers[2], map.size(), towers, map);
	}
	
	public static void main(String[] args)
	{
		int disks;
		int speed = 500;

		if (args.length < 1 || args.length > 2)
		{
			usage();
			return;
		}

		try
		{
			disks = Integer.parseInt(args[0]);
			if (args.length == 2)
				speed = Integer.parseInt(args[1]);
		}
		catch (Exception e)
		{
			usage();
			return;
		}

		Hanoi h = new Hanoi(disks, speed);
	}

	private static void usage()
	{
		System.out.println("Usage: java Hanoi <number of disks> [animation_speed (ms)]\n\n");
	}
}

