<?php
    
    require_once 'connection.php';
	
	class Signal {
		
		private $db;
		private $connection;
		
		function __construct() {
			$this -> db = new DB_Connection();
			$this -> connection = $this->db->getConnection();
		}
		
		public function lists($user)
		{
			$query =
			"SELECT g.* ,  'S' AS status 
			FROM  `gesture` AS g,  `usergesture` AS ug
			WHERE g.idgesture = ug.idgesture
			AND ug.iduser = $user
			UNION 
			SELECT g.* ,  'N' AS 
			STATUS 
			FROM  `gesture` AS g
			WHERE g.idgesture NOT IN (	SELECT idgesture
										FROM  `usergesture` 
										WHERE iduser = $user )
			ORDER BY symbol";
			$result = mysqli_query($this->connection, $query);
			
			$arr = array();
			
			while($row = mysqli_fetch_assoc($result)){
				$arr[] = $row;
			}
			echo json_encode($arr);
			
			mysqli_close($this->connection);
			
		}
		
		public function listug($user)
		{
			$query =
			"SELECT symbol, CONCAT(sensor1, ',' ,sensor2, ',' , sensor3, ',' , sensor4, ',' , sensor5, ',' , sensor6, ',' , sensor7, ',' , sensor8, ',') AS line 
			   FROM usergesture ug, gesture g
			  WHERE ug.idgesture = g.idgesture
			    AND iduser = $user
			  ORDER BY symbol";
			$result = mysqli_query($this->connection, $query);
			
			$arr = array();
			
			while($row = mysqli_fetch_assoc($result)){
				$arr[] = $row;
			}
			echo json_encode($arr);
			
			mysqli_close($this->connection);
			
		}
		
		public function insert($user, $gesture, $sensor1, $sensor2, $sensor3, $sensor4, $sensor5, $sensor6, $sensor7, $sensor8)
		{
			$query = "SELECT * from usergesture WHERE iduser = $user AND idgesture = $gesture";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result) == 0){
				$query = "INSERT INTO usergesture
						(iduser, idgesture, sensor1, sensor2, sensor3, sensor4, sensor5, sensor6, sensor7, sensor8) VALUES 
						($user,  $gesture, $sensor1, $sensor2, $sensor3, $sensor4, $sensor5, $sensor6, $sensor7, $sensor8)";
				$inserted = mysqli_query($this -> connection, $query);
				if($inserted == 1 ){
					$json['success'] = 'Sinal Gravado';
				}else{
					$json['error'] = 'Erro na Gravação';
				}
				echo json_encode($json);
			}else{
				$query = "UPDATE usergesture
						SET sensor1 = $sensor1,
							sensor2 = $sensor2,
							sensor3 = $sensor3,
							sensor4 = $sensor4,
							sensor5 = $sensor5,
							sensor6 = $sensor6,
							sensor7 = $sensor7,
							sensor8 = $sensor8
						WHERE iduser = $user 
						  AND idgesture = $gesture";
				$updated = mysqli_query($this -> connection, $query);
				if($updated == 1 ){
					$json['success'] = 'Sinal Alterado';
				}else{
					$json['error'] = 'Erro na Alteração';
				}
				echo json_encode($json);
			}
			mysqli_close($this->connection);
			
		}
		
		public function next($user){
			$query = "SELECT * 
					FROM  `gesture` AS g
					WHERE g.idgesture NOT 
					IN (
						SELECT idgesture
						FROM  `usergesture` 
						WHERE iduser = $user
					)
					ORDER BY g.symbol";
					
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){			
				$row = mysqli_fetch_assoc($result);				
				echo json_encode($row);
			}else{
				$json['symbol'] = 'completed';
				echo json_encode($json);
			}
			mysqli_close($this->connection);
			
		}
		
		public function completed($user){
			$query = "SELECT COUNT( * ) AS number 
					FROM  `gesture` AS g
					WHERE g.idgesture NOT 
					IN (

					SELECT idgesture
					FROM  `usergesture` 
					WHERE iduser = $user
					)
					ORDER BY g.symbol";
					
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$row = mysqli_fetch_assoc($result);
				if ($row["number"] != 0){				
					$json['incomplete'] = $row["number"];
					echo json_encode($json);	
				}else{
					$json['completed'] = '0';
					echo json_encode($json);
				}
			}
						
			mysqli_close($this->connection);	
		}	
	}
?>	
