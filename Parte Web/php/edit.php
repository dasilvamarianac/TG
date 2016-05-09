<?php
    
    require_once 'connection.php';
	require_once 'user.php';
	
	$user = new User();
	
	if(isset($_POST['id'],$_POST['password'],$_POST['newpassword'])) {
		
		$id = $_POST['id'];
		$password = $_POST['password'];
		$newpassword = $_POST['newpassword'];
		
		$user-> edit($id, $password, $newpassword);
		
	}
?>	