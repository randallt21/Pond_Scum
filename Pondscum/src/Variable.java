
// TODO: Auto-generated Javadoc
/**
 * The Class Variable.
 */
public class Variable {
	
	/** The total. */
	private long total;
	
	/** The loc y. */
	private int loc_x, loc_y;

	/**
	 * Instantiates a new variable.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Variable(int x, int y) {
		total = 0;
		loc_x = x;
		loc_y = y;
	}

	/**
	 * Sets the total.
	 *
	 * @param s_mat the new total
	 */
	public void setTotal(long s_mat) {
		total += s_mat;
	}
	
	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}
	
	/**
	 * Gets the loc x.
	 *
	 * @return the loc x
	 */
	public int getLoc_x() {
		return loc_x;
	}
	
	/**
	 * Gets the loc y.
	 *
	 * @return the loc y
	 */
	public int getLoc_y() {
		return loc_y;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + loc_x + "," + loc_y + ") Total = " + total;
		
	}
}
