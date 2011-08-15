public class Seed
{
	private int pairs;
	private int[][] allOpponents; 

	/**
	 * Constructor takes the number of pairs to generate
	 * all possible encounters.
	 * @param int number of pairs
	 */
	public Seed( int pairs )
	{
		setPairs( pairs );
		// Calling mehod generateOpponents() here ensures
		// the array allOpponents is filled
		this.generateOpponents();
	}

	/**
	 * Sets the number of pairs. Used in the constructor.
	 * Can't think of any other possible use.
	 * @param int number of pairs
	 * @return void
	 */
	private void setPairs( int pairs )
	{
		this.pairs = pairs;
	}

	/**
	 * Returns the number of pairs for this Object
	 * @return int
	 */
	public int getPairs()
	{
		return pairs;
	}

	/**
	 * Returns the acual array with the oponents list.
	 * Note that the length of the array is the number 
	 * of possible encounters. 
	 * @return int[][]
	 */
	public int[][] getOpponentArray()
	{
		return allOpponents;
	}

	/**
	 * Method to generate the list (array) of all possible
	 * encounters.
	 * Retrieve this array through getOpponentArray()
	 * @return void
	 */
	private void generateOpponents()
	{
		/**
		 * 14 pairs each have 13 opponents. But 14 * 13 would
		 * count pair 2 vs pair 5 *and* pair 5 vs pair 2 hence
		 * divide by 2
		 */
		allOpponents = 
			new int[ ( getPairs() * ( getPairs() - 1 ) / 2 ) ][ 2 ];
		int counter = 0;
		for ( int i = 0; i < getPairs(); i += 1 )
		{
			for( int j = 0; j < getPairs(); j += 1 )
			{
				if ( i > j )
				{
					allOpponents[ counter ][ 0 ] = i + 1;
					allOpponents[ counter ][ 1 ] = j + 1;
					counter += 1;
				}
			}
		}
	}

	/**
	 * Method to print the list of all possible encounters.
	 * This is a sort of toString() for this class
	 * @return void
	 */
	public void printOpponents()
	{
		for( int i = 0; i < allOpponents.length; i += 1 )
		{
			System.out.printf( "%2d - %2d\t", allOpponents[ i ][ 0 ],
				allOpponents[ i ][ 1 ] );
			if ( ( i + 1 ) % 5 == 0 )
				System.out.print( "\n" );
		}
		System.out.println();
	}
}
