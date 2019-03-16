<?php

require "init.php";
if($con){
	$name = $_POST["name"];
	$image = $_POST["image"];
	$query = "insert into products values( NULL , '" . $name . "', 25, 'some description', 3, 'url'); ";
	$result = mysqli_query($con, $query);
	$upload_path = "imageUpload/" . $con->insert_id .".jpg";
	if($result){
		file_put_contents($upload_path, base64_decode($image));
		echo "Image uploaded successfully!";
	}
	else{
		echo "Error while uploading" . $query;
	}
}
else{
	echo "Connection failed: " . $con->connect_error ;
}