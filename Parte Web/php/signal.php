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
		
		
		
	}
?>	
