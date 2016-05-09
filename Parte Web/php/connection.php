<?php

	require_once 'config.php';
	
	class DB_Connection {
		
		private $connect;
		
		function __construct() {
			
			$this->connect = mysqli_connect(hostname, username, password, db_name)
			or die("Erro na conexão com o banco de dados");
			
		}
		
		public function getConnection()
		{
			return $this->connect;
		}
	}
?>