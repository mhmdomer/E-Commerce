<?php

require "init.php";
if($con){
	$query = "select * from products";
	$result = mysqli_query($con, $query);
	$response = array();
	while ($row = mysqli_fetch_array($result)) {
			array_push(
				$response, array("id"=>$row[0],
				"name"=>$row[1],
			  	"price"=>$row[2],
				"description"=>$row[3],
				"in_stock"=>$row[4]
			));
	}
	echo json_encode($response);
}
else{
	echo "connection error";
}