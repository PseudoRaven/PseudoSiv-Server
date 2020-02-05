<?php

	/**
	* Ajax_suggestions
	*
	* Sends a query with the entered value to grab suggestions
	* and then sends it to our ajax request.
	**/
	
	require("includes/db.inc.php");
	include("includes/config.inc.php");
	
	/**
	* Creating a new object
	**/
	
	$search = new Search($pdo);
	
	// If the post was actually sent from the ajax.
	if (isset($_POST['search']))
	{
		/**
		* Load suggestions from our loadSuggestions method.
		**/
		
		echo $search->loadSuggestions($_POST['search']);
	}
	
?>