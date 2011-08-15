import java.util.Random;

class Movement
{
	private int[][] allOpponents;
	private int[][] opponentsLeft;
	private int pairs;
	private int tables;
	private int rounds;
	private int virtualPairs;
	private int[][][] movement;
	private int[][] pairHasPlayedTable;
	private int[][] pairHasPlayedRound;

	public Movement( int pairs, int tables, int rounds )
	{
		setPairs( pairs );
		setTables( tables );
		setRounds( rounds );
		setVirtualPairs( tables * 2 );
		movement = new int[ getRounds() ][ getTables() ][ 2 ];
		pairHasPlayedTable = new int[ getPairs() + 1 ][ getTables() ];
		pairHasPlayedRound = new int[ getPairs() + 1 ][ getRounds() ];
		Seed mySeed = new Seed( getPairs() );
		allOpponents = mySeed.getOpponentArray();
		opponentsLeft = mySeed.getOpponentArray();
	}

	private void setPairs( int pairs )
	{
		this.pairs = pairs;
	}

	public int getPairs()
	{
		return pairs;
	}

	private void setVirtualPairs( int pairs )
	{
		virtualPairs = pairs;
	}

	private int getVirtualPairs()
	{
		return virtualPairs;
	}

	private void setTables( int tables )
	{
		this.tables = tables;
	}

	private void setRounds( int rounds )
	{
		this.rounds = rounds;
	}

	public int getRounds()
	{
		return rounds;
	}

	public int getTables()
	{
		return tables;
	}

	/**
	 * Returns the number of Encounters being the length
	 * of Array allOpponents. As stated in class Seed, the
	 * number of possible encounters equals ( number of pairs *
	 * (number of pairs - 1) / 2 )
	 * @return int number of encounters
	 */
	public int getEncounters()
	{
		return allOpponents.length;
	}

	public void generateMovement( int StartAtRound )
	{
		int tables = getTables();
		int encounters = getEncounters();
		int rounds = getRounds();
		int tries = 0;
		int pair1 = 0;
		int pair2 = 0;
		int nextPairs = 0;
		int counter = 0;
		int power = 2;
		int stuckAt = 0;
		boolean thisRound = false;

		Random myRandom = new Random();

		for ( int i = StartAtRound; i < rounds; i += 1 )
		{
			destroyRound( i );
			thisRound = tryRound( i );

			// This is basically the main loop.
			// It enables to abort seemingly dead ends
			// and start either halfway or all over agein
			while ( thisRound == false )
			{
				counter += 1;
				if ( counter % 1000 == 0 )
				{
					// Delete several rows
					// and start again from there
					i = getRounds() / 3;
					destroyRound( i );
				}
				if ( counter % 10000 == 0 )
				{
					// Delete all rows and start again
					destroyRound( 0 );
					generateMovement( 0 );
					counter = 0;
				}
				destroyRound( i );
				thisRound = tryRound( i );
			}
		} // Next i
	}

	public void showMovement()
	{
		System.out.printf( "\n%d pairs, %d tables, %d rounds\n\n",
			getPairs(), getTables(), getRounds() );
		for ( int i = 1; i <= getRounds(); i += 1 )
		{
			for ( int j = 1; j <= getTables(); j += 1 )
			{
				System.out.printf( "%2d:%2d-%2d ", j, 
					movement[ i - 1 ][ j - 1 ][ 0 ],
					movement[ i - 1 ][ j - 1 ][ 1 ] );
			}
			System.out.println();
		}
	}

	private void destroyRound( int startRound )
	{
		for ( int i = startRound; i < getRounds(); i += 1 )
		{
			for ( int j = 0; j < getTables(); j += 1 )
			{
				movement[ i ][ j ][ 0 ] = 0;
				movement[ i ][ j ][ 1 ] = 0;
			}
		}
		if ( getVirtualPairs() > getPairs() )
		{
		 	// There are more tables than pairs
			// Let a method handle this
			fillExtraTables();
		}
		// System.out.println( "***ROUND DESTROYED***" );
	}

	/* This method checks if a pair has played on a tabke
	 * in a previous round.
	 * @param int pair
	 * @param int table
	 * @return boolean true: has played on this table
	 */
	private boolean checkPairOnTable( int pair, int table )
	{
		for ( int i = 0; i < getRounds(); i += 1 )
		{
			if ( movement[ i ][ table ][ 0 ] == pair )
			{
				return true;
			}
			if ( movement[ i ][ table ][ 1 ] == pair )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is the working horse. It will try to fill a round,
	 * write success in the array movement and report either success or 
	 * failure to the calling method.
	 * The calling methodwill remove this round and try again with
	 * a new random.
	 * @param int the current round to fill
	 * @return boolean success ( true ) or failure
	 */
	private boolean tryRound( int round )
	{
		int[][] copyOfEncounters = new int[ getEncounters() ][ 2 ];
		copyOfEncounters = copyAllEncounters();
		Random myRandom = new Random();
		int pair1 = 0;
		int pair2 = 0;
		int[] pairsThisRound = new int[ getPairs() + 1 ];
		int numberOfExtraTables = 0;

		boolean success = false;

		for ( int j = 0; j < getTables(); j += 1 )
		{
			if ( movement[ round ][ j ][ 0 ] != 0
				&& movement[ round ][ j ][ 1 ] != 0 )
				// This table has already been filled,
				// presumably by non-playing pairs.
				// Skipping this table.
				continue;
			success = false;
			int counter = 0;
			while ( success == false )
			{
				// success would mean: the program
				// is able to locate two pairs on
				// this table in this round.
				if ( counter > 500 )
					// Tried x number of times. Report
					// failure to enable the program flow
					// to delete previous tables in this round 
					// to try again
					return false;
				int thisEncounterNumber = 
					myRandom.nextInt( getEncounters() );
				counter += 1;

				pair1 = copyOfEncounters[ thisEncounterNumber ][ 0 ];
				pair2 = copyOfEncounters[ thisEncounterNumber ][ 1 ];

				if( pair1 != 0 && pair2 !=0 )
				{
					// Check if these pairs have played this table 
					if ( checkPairOnTable( pair1, j ) == true )
						pair1 = 0;
					if ( checkPairOnTable( pair2, j ) == true )
						pair2 = 0;

					// Check if these pairs have played in this round
					if( pairsThisRound[ pair1 ] != 0 )
						pair1 = 0;
					if( pairsThisRound[ pair2 ] != 0 )
						pair2 = 0;
				}

				if ( pair1 != 0 && pair2 != 0 )
				{
					// We have success. Bookkeeping starts here
					// The main data-structure is the array
					// movement[ round ][ table ][ pairs ]
					movement[ round ][ j ][ 0 ] = pair1;
					movement[ round ][ j ][ 1 ] = pair2;
					
					pairsThisRound[ pair1 ] = 1;
					pairsThisRound[ pair2 ] = 1;

					copyOfEncounters[ thisEncounterNumber ][ 0 ] = 0;
					copyOfEncounters[ thisEncounterNumber ][ 1 ] = 0; 
		//			System.out.printf( "%2d:%2d-%2d  ", j, pair1, pair2 );
					success = true;
				}
			}
		}
	//	System.out.println();
		return true;
	}

	/**
	 * This method returns a deep copy of the array
	 * allOpponents. Need it several times so I can't
	 * play with the origigal.
	 * @return int[][] deep copy of array allopponenets
	 */
	private int[][] copyAllEncounters()
	{
		int[][] myCopy = new int[ getEncounters() ][ 2 ];
		for ( int i = 0; i < getEncounters(); i += 1 )
		{
			myCopy[ i ][ 0 ] = allOpponents[ i ][ 0 ];
			myCopy[ i ][ 1 ] = allOpponents[ i ][ 1 ];
		}
		return myCopy;
	}

	/**
	 * If there are more tables than there are  number of pairs 
	 * divided by two we fill up the spare tables with numbers up 
	 * to the number of tables times two. 
	 * The program flow will check if there are tables already
	 * filled this way and skip to next table.
	 */
	private void fillExtraTables()
	{
		int thisTable;
		for ( int i = 0; i < getRounds(); i += 1 )
		{
			// System.out.println();
			for ( int j = getVirtualPairs(); j > getPairs(); j -= 1 )
			{
				// Three hours of work in this line
				thisTable = ( ( ( j - 2 ) / 2 ) + ( j % 2 ) 
					+ getTables() - i ) % ( getTables() );
				movement[ i ][ thisTable ][ j % 2 ] = j;
			}
		}
	//	showMovement();
	}
}
