<?php 

	require("includes/db.inc.php");
	include("includes/config.inc.php");
	
	/**
	* Creating a new object
	**/
	
	$search = new Search($pdo);
	
	/**
	* How many results per page
	**/
	
	$rows_per_page = 50;
	
	/** 
	* Default page ID
	**/
	
	$page_id = 1;

	/**
	* Change the page ID to the page you clicked on, on click
	**/
	
	if(isset($_GET['page']) && $_GET['page'] != 0)
	{
		$page_id = $_GET['page'];
	}
	
	/**
	* This will calculate how many results needed to be shown on the current page ID.
	**/
	
	$page_offset = ($rows_per_page * $page_id) - $rows_per_page;
?>
<html>
	<head>
		<title>Kagani - Item Database</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta name="keywords" content="best, rs3, rsps, real, rs, runescape private, private server, runescape 3, server, no member, free">
		<meta name="description" content="Kagani is the greatest RS3 private server offering a friendly community, lots of content. Free to play.">
		<script src="../../assets/js/ie/html5shiv.js"></script>
		<link rel="stylesheet" href="../../assets/css/main.css" />
		<link rel="stylesheet" href="../../assets/css/ie8.css" />
	</head>
	<body id="top">
	
	<header id="header">
				<nav>
					<a href="#menu">Menu</a>
				</nav>
				<div class="content">
				<h1>Kagani - Item Database</h1>
				</div>
			</header>
			
			<nav id="menu">
						<div class="inner">
							<h2>Menu</h2>
							<ul class="links">
								<li><a href="https://kagani.net">Home</a></li>
								<li><a href="https://kagani.net/forums">Forums</a></li>
								<li><a href="https://kagani.net/play">Play</a></li>
								<li><a href="https://kagani.net/store">Store</a></li>
								<li><a href="https://kagani.net/hiscores">Hiscores</a></li>
								<li><a href="https://kagani.net/vote">Vote</a></li>
								<li><a href="https://kagani.net/cpanel">Account Management</a></li>
								<li><a href="https://kagani.net/database">Database</a></li>
							</ul>
							<a href="#" class="close">Exit</a>
						</div>
					</nav>
					
	<div class="container">
		<div class="content_margin">
		<br />
		<div class="alert alert-error" id="alert" style="display: none;" ></div>
		<br />
			<center><form action="index.php" method="get" id="form">
			<div class="4u 12u$(xsmall)">
			<input type="text" id="search" name="item" class="textbox_style" autofocus="" placeholder="Search for an item">
			</div><br />
			<input type="submit" class="submit" value="Search"><br />
			</form>
			<ul class="menus" style="display: none;">
			</ul>
        </div>
		
		<center><div class="content_margin_less">	
			<table class="table table-striped">
				<thead>
					<tr>
						<?php
						if (isset($_GET['item']))
						{
							echo 
							'
							<th>Item ID</th>				
							<th>Item Name</th>
							<th>Item Description</th>
							';
						}
						?>
					</tr>
				</thead>
				
				<tbody>
				<?php
					if (isset($_GET['item']))
					{
						// Trying to search for items like this.
						try
						{
							//Sending a request for the Method getItems with its name, offset, and rows per page.
							$search->getItems($_GET['item'], $page_offset, $rows_per_page);
							
							//Now we are going to use the Ceil() function for our pagination to divide.
							$page_count = ceil($search->getItemsCount($_GET['item']) / $rows_per_page);
						}
						catch (Exception $e)
						{
							//Error found, print it (Items not found).
							echo $e->getMessage();
						}
					}
				?>
				</tbody>
			</table>
			
			<br /> <br />
		</div>
		
		<center><?php
			
			// Url format for pagination.
			if (isset($_GET['item']))
			{
				$url = "?item=".$_GET['item']."&";
			}

			// Var pagination is what holds the pagination buttons, it will paginate it.
			$pagination = '';
			
			// Checking if we have searched.
			if (isset($_GET['item']))
			{	
				//This checks if there are more than one page, else it will show 1 button in the pagination.
				if (isset($page_count) && $page_count > '1' ) 
				{
					//How many pagination buttons we want.
					$length = 10;
					
					/**
					* Math
					* Let's do some math to get our pagination results.
					**/
					
					$min = ($length % 2 == 0) ? ($length / 2) - 1 : ($length - 1) / 2;
					$max = ($length % 2 == 0) ? $min + 1 : $min;
					
					$min_pages = $page_id- $min;
					$max_pages = $page_id+ $max;
					
					$min_pages = ($min_pages < 1) ? 1 : $min_pages;
					$max_pages = ($max_pages < ($min_pages + $length - 1)) ? $min_pages + $length - 1 : $max_pages;
					
					if ($max_pages > $page_count) 
					{
						$min_pages = ($min_pages > 1) ? $page_count - $length + 1 : 1;
						$max_pages = $page_count;
					}

					$min_pages = ($min_pages < 1) ? 1 : $min_pages;
					
					/**
					* Pagination Start
					**/
					
					// If we went far away in the pagination, this will show the back to the first result button.
					
					if ( ($page_id > ($length - $min)) && ($page_count > $length) ) 
					{
						$pagination .= '<a class="num"  title="First" href="'.$url.'page=1">&lt;&lt;</a> ';
					}
					
					// This will show the Previous page button if the current page is NOT 1.
					
					if ($page_id != 1) 
					{
						$pagination .= '<a class="num" href="'.$url.'page='.($page_id-1). '">Previous</a> ';
					}
					
					// Now let's go through a loop (To show all buttons of our pages).
					
					for ($i = $min_pages;$i <= $max_pages;$i++) 
					{
						// Shows the page buttons.
						if ($i == $page_id)
						{
							$pagination .= '<span class="num"><strong>' . $i . '</strong></span> ';
						}
						else
						{
							$pagination.= '<a class="num" href="'.$url.'page='.$i. '">'.$i.'</a> ';
						}
					}
					
					//Shows the next button, if theres a next page.
					if ($page_id < $page_count) 
					{
						$pagination.= ' <a class="num" href="'.$url.'page='.($page_id + 1) . '">Next</a>';
					}

					//Shows the last button if we're not at the last page.
					if (($page_id< ($page_count - $max)) && ($page_count > $length)) 
					{
						$pagination .= ' <a class="num" title="Last" href="'.$url.'page='.$page_count. '">&gt;&gt;</a>';
					}
				}
				else
				{
					// Shows button '1'.
					$pagination.= '<a href="#">1</a>';
				}
			?>	
						<div class="pagination">
							<ul>
								<?php 
									
								//Showing our pagination results.
								echo $pagination; 
									?>
							</ul>
							<div id="results">
								<?php
									// Checking if we've searched for something.
									if (isset($_GET['item']))
									{
										//Gets the count of total results.
										echo
										'
										Total results found:
										<span class="label label-inverse">'
										.$search->getItemsCount($_GET['item']).
										'</span>
										';
									}
								?>
							</div>
						</div>	

<?php
			}
?>
	</div>
	</body>
	
	<footer id="footer">
				<ul class="icons">
					<li><a href="#" class="icon fa-facebook"><span class="label">Facebook</span></a></li>
					<li><a href="https://youtube.com/noty2605" target="blank" class="icon fa-youtube"><span class="label">YouTube</span></a></li>
					<li><a href="https://twitter.com/kaganirsps" target="blank" class="icon fa-twitter"><span class="label">Twitter</span></a></li>
					<li><a href="#" class="icon fa-instagram"><span class="label">Instagram</span></a></li>
				</ul>
				<p class="copyright">Copyright &copy; 2016 <a href="https://kagani.net" target="blank">Kagani</a>. Developed by: <a href="https://kagani.net/forums/index.php?/user/2-pax" target="blank">Pax M</a>.<br /> We are not affiliated with <a href="http://jagex.com" target="blank">Jagex Ltd</a> or <a href="http://runescape.com" target="blank">RuneScape</a>.</p>
			</footer>

			<script src="../assets/js/jquery.min.js"></script>
			<script src="../../assets/js/jquery.scrolly.min.js"></script>
			<script src="../../assets/js/skel.min.js"></script>
			<script src="../../assets/js/util.js"></script>
			<script src="../../assets/js/main.js"></script>
<script>	
	$("#form").submit(function () 
	{
		if ($("#search").val() == '')
		{
			$("#alert").fadeIn("slow");
			$("#alert").html("Please enter something before searching!");
			setTimeout(function() {
				$("#alert").fadeOut("slow"); 
			}, 2000);   
			return false;
		}
	});
	
 	$(document).ready(function () {
		list = $(".menus");
		form = $("#search");
		form.keyup(function () {
		
			if($('#search').data("searchtimer") !== undefined) 
			{
				clearTimeout($('#search').data("searchtimer"));
			}
			
			if($(this).val().length > 0)
			{
				load();
				var timer = setTimeout(searchMe, 1500); 
				
				$(this).data("searchtimer", timer);
			} else {
				list.slideUp("slow");
				unload();	
			}			
		});
	});
	
	function searchMe() {
		$.post("ajax_suggestions.php", { search : form.val() }, function(data) {
			list.slideDown("slow");
			list.html(data);
			unload();
		});
	}
	
	function search() {
		list.hide();
		
		$("#form").submit();
	}
	
	function load() {
		$("#loading").show();
	}
	
	function unload() {
		$("#loading").hide();
	}
	</script>
	</body>
</html>