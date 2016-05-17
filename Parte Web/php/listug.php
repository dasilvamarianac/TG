<?php
    
    require_once 'connection.php';
	require_once 'signal.php';
	
	$signal = new Signal();
	
	if(isset($_POST['user'])) {		
		$user = $_POST['user'];	
		$signal-> listug($user);		
	}
	
?>	