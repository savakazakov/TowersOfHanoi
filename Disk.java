import java.awt.*;

/**
 * This class represents a single disk in a simulation of the towers of Hanoi puzzle.
 * @see Tower
 */
public class Disk 
{
	private Color colour;			    // The colour of this disk.
	private boolean highlighted;		// Whether or not the disk is highlighted in the GUI.
	private int width;			        // The width of this disk in pixels.
	private int height;			        // The height of this disk in pixels.

	/**
	 * Constructor.
	 * @param colour The colour of the disk to create.
	 * @param width The width of the disk, in pixels.
	 * @param height The height of the disk, in pixels.
	 */
	public Disk(Color colour, int width, int height)
	{
		this.colour = colour;
		this.width = width;
		this.height = height;
		this.highlighted = false;
	}

	/**
	 * Mutator used to define whether or not this disk is highlighted.
	 * @param b True to set this disk to be highlighted. False to set the disk as not highlighted.
	 */
	public void setHighlight(boolean b)
	{
		this.highlighted = b;
	}

	/**
	 * Accessor used to get the width of this disk, in pixels.
	 * @return The width of this disk in pixels.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Draws this disk on the screen.
	 * @param g The graphic contect the draw this disk on.
	 * @param i The height of this disk, measured in disks.
	 * @param t The Tower this disk currently resides on.
	 */
	public void drawOn (Graphics g, int i, Tower t)
	{
		int startx = t.getWidth()/2-width/2;
		int starty = t.getHeight()-((i+1)*height);
	
		g.setColor(highlighted ? Color.RED : colour);
		g.fill3DRect(startx, starty, width, height,true);
	}
}
