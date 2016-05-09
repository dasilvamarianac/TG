<?php
    
    require_once 'connection.php';
	require_once 'user.php';
	
	$user = new User();
	
	if(isset($_POST['email'],$_POST['password'])) {
		
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		$user-> login($email, $password);

	}
?>