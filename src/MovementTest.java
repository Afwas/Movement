class MovementTest
{
	public static void main ( String [] args )
	{
		// Constructor Movement( pairs, tables, rounds )
		Movement myMovement = new Movement( 14, 13, 13 );
		myMovement.generateMovement( 0 );
		myMovement.showMovement();
	} // End main 
}
