<?php

	/**											
	* MYSQL HOST HERE (Example: localhost)	
	**/										

	DEFINE ("MYSQL_HOST", "localhost");		

	/**										
	* MYSQL USER HERE (Example: root)			
	**/											

	DEFINE ("MYSQL_USER", "pax");				
	
	/**											
	* MYSQL PASSWORD HERE (Example: password)	
	**/													

	DEFINE ("MYSQL_PASSWORD", "kagani256");	
	
	try
	{
		$pdo = new PDO('mysql:host='.MYSQL_HOST.';dbname=kagani', MYSQL_USER, MYSQL_PASSWORD);
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	}
	catch(PDOException $e)
	{
		echo $e->getMessage();
	}
?>