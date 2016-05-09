<?php
    
    require_once 'connection.php';
    require_once 'user.php';
	
    $user = new User();
	
    if(isset($_POST['email'])) {
		
		$email = $_POST['email'];
		$user-> email($email);
	
	}

?>		