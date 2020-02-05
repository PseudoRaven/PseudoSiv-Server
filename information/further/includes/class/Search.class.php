<?php
	/**
	* Search.class
	*
	* Handling searching for items system
	*
	* @Author Jony <artemkller@gmail.com> <www.driptone.com>
	**/
	
	Class Search
	{
		/**
		* Properties
		**/
		
		protected $pdo;
		private $like;
		private $row;
		private $items;
		
		/**
		* Constructor
		*
		* Creating MySQL connection using PDO
		**/
		
		public function __construct($pdo)
		{
			$this->pdo = $pdo;
		}
		
		/**
		* Method getItems
		*
		* Gets ALL items LIKE the entered search value
		*
		* @parm item The entered searched value.
		**/
		
		public function getItems($item, $offset, $rows)
		{
			$this->like = $this->pdo->prepare("SELECT * FROM items WHERE item_name LIKE :item LIMIT $offset, $rows");
			$this->like->execute(array
			(
				":item" => '%'.$item.'%'
			));
			
			/** If there's a row LIKE the entered value **/
			
			if ($this->like->rowCount())
			{
				while ($this->row = $this->like->fetch(PDO::FETCH_ASSOC))
				{
					echo
					'
					<tr>
						<td>'.$this->row['item_id'].'</td>
						<td>'.$this->row['item_name'].'</td>
						<td>'.$this->row['item_description'].'</td>
					</tr>
					';	
				}
			}
			else
			{
				/** If not, then throw a new error.. **/
				
				throw new exception ("Could not find items!");
			}
		}
		
		/**
		* Method getItemsCount
		*
		* Gets the number of rows found for the searched item
		*
		* @parm item The entered search value.
		**/
		
		public function getItemsCount($item)
		{
			$this->items = $this->pdo->prepare("SELECT * FROM items WHERE item_name LIKE :item");
			$this->items->execute(array
			(
				":item" => '%'.$item.'%'
			));
			
			/** Returns how many results we have **/
			
			return $this->items->rowCount();
		}
		
		/**
		* Method loadSuggestions
		*
		* Load suggestions LIKE the entered value, then client
		* can click on one of the suggestions to search it.
		*
		* @parm data The entered value.
		**/
		
		public function loadSuggestions($data)
		{
			//How many results to show in the suggestion page?
			$limit = 10;
			
			$this->items = $this->pdo->prepare("SELECT * FROM items WHERE item_name LIKE :data LIMIT 10");
			$this->items->execute(array
			(
				":data" => '%'.$data.'%'
			));
			
			if ($this->items->rowCount())
			{
				while ($this->row = $this->items->fetch(PDO::FETCH_ASSOC))
				{
				echo '<li onclick=\'form.val("'.$this->row['item_name'].'"); list.html(""); search(); \'>'.$this->row['item_name'].'<span style="float: right; padding-right: 5px;">'.$this->row['item_id'].'</span></li>';
				}
				
				/** If more than 10 results, show a load More button. **/
				
				if ($this->getItemsCount($data) > 10)
				{
					echo '<a href="index.php?item='.$data.'"><li> Load more ('.$this->getItemsCount($data).' Results found)</li></a>';
				}
			}	
			else
			{
				/** No results **/
				
				echo '<br />Could not find any results!<br />';
			}			
		}
	}
?>