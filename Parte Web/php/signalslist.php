<?php
    
    require_once 'connection.php';
	require_once 'signal.php';
	
	$signal = new Signal();
	
	if(isset($_POST['id'])) {		
		$id = $_POST['id'];	
		$signal-> lists($id);		
	}
?>	