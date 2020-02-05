<?php

	/**
	* Item.CFG to SQL
	*
	* Converts item.CFG file to a SQL file.
	*
	* @Author Lamprecht <http://www.rune-server.org/members/lamprecht/> Extended by Jony <artemkller@gmail.com>
	**/
	
	/**
	* PATH / NAME OF YOUR ITEM.CFG FILE
	**/
	
	$itemcfg = ' Put here the name of your file, make sure its in the same directory';
	
	$file = file($itemcfg );
	
	$string = 'INSERT INTO `items` (`item_id`, `item_name`, `item_description`) VALUES';


	foreach($file as $key => $line)
	{
		$lines = explode('	',$line);
		foreach($lines as $explodedNum => $exploded)
		{
			if($explodedNum == 0)
			{
				$exploded = str_replace('item = ','',$exploded);
				$string .= '('.$exploded.',';
			}
			if($explodedNum == 1)
			{
			$exploded = str_replace('_',' ',$exploded);
			$string .= '\''.mysql_escape_string($exploded).'\',';
			}
			if($explodedNum == 2)
			{
			$exploded = str_replace('_',' ',$exploded);
			$string .= '\''.mysql_escape_string($exploded).'\'),';
			}
		}
		$string .= "\n";
	}
	
	$file = "item.sql";
	
	$handle = fopen($file,'w');
	
	fwrite($handle,substr($string,0,-2).';');
	
	fclose($handle);
	
	echo 'File written.';
?>