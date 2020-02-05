<?php
	
	/**
	* Config.inc
	*
	* In this file we will include all classes
	* and create them.
	*
	* @author Jony <artemkller@gmail.com>
	**/
	
	/**
	* Autoloading Classes
	*
	* @parm class_name The classname w'ere
	* including.
	**/
	
	function classAutoLoad($class) {
		if (file_exists("includes/class/$class.class.php"))
			include("includes/class/".$class.".class.php");
	}

	spl_autoload_register('classAutoload');
?>