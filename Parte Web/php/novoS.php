<?php
    
    require_once 'connection.php';
	require_once 'signal.php';
	
	$signal = new Signal();
	
	if(isset($_POST['user'],$_POST['gesture'],$_POST['sensor1'],$_POST['sensor2'],$_POST['sensor3'],$_POST['sensor4'],$_POST['sensor5'],$_POST['sensor6'],$_POST['sensor7'],$_POST['sensor8'])) {
		
		$user = $_POST['user'];
		$gesture = $_POST['gesture'];
		$sensor1 = $_POST['sensor1'];
		$sensor2 = $_POST['sensor2'];
		$sensor3 = $_POST['sensor3'];
		$sensor4 = $_POST['sensor4'];
		$sensor5 = $_POST['sensor5'];
		$sensor6 = $_POST['sensor6'];
		$sensor7 = $_POST['sensor7'];
		$sensor8 = $_POST['sensor8'];
		
		$signal-> insert($user, $gesture, $sensor1, $sensor2, $sensor3, $sensor4, $sensor5, $sensor6, $sensor7, $sensor8);

	}
?>	