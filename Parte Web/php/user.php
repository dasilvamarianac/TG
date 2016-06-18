<?php
    
    require_once 'connection.php';
	
	class User {
		
		private $db;
		private $connection;
		
		function __construct() {
			$this -> db = new DB_Connection();
			$this -> connection = $this->db->getConnection();
		}
		
		public function login($email,$password)
		{
			$query = "Select * from user where email='$email'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$query = "Select * from user where email='$email' and password = '$password' ";
				$result = mysqli_query($this->connection, $query);
				if(mysqli_num_rows($result)>0){
					
					$row = mysqli_fetch_assoc($result);				
					$json['success'] = 'Bem Vindo';
					$json['id'] =  $row["idUser"];
					$json['status'] = $row["status"];
					echo json_encode($json);
					mysqli_close($this -> connection);

				}else{
					$json['error'] = "Senha";
					echo json_encode($json);
					mysqli_close($this->connection);
				}
			}else{
				$json['error'] = "Usuario";
				echo json_encode($json);
				mysqli_close($this->connection);
			}
			
		}
		
		public function novo($email,$password)
		{
			$query = "Select * from user where email='$email'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)<1){
				$query = "insert into user (email, password) values ( '$email','$password')";
				$inserted = mysqli_query($this -> connection, $query);
				if($inserted == 1 ){
					$json['success'] = 'Usuário Criado';
				}else{
					$json['error'] = 'Erro na Criação';
				}
				echo json_encode($json);
				mysqli_close($this->connection);
			}else{
				$json['error'] = 'Usuário Existente';
				echo json_encode($json);
				mysqli_close($this->connection);
			}
			
		}

		
		public function edit($id, $password, $newpassword)
		{
			$query = "Select * from user where idUser ='$id' and password = '$password'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$query = "update user set password = '$newpassword' where idUser='$id'";

				$updated = mysqli_query($this -> connection, $query);
				if($updated == 1 ){
					$json['success'] = 'Senha alterada';
				}else{
					$json['error'] = 'Erro na Alteração'; 
				}
				echo json_encode($json);
				mysqli_close($this->connection);
			}else{
				$json['error'] = 'Senha Incorreta';
				echo json_encode($json);
				mysqli_close($this->connection);
			}
			
		}

		public function email($email)
		{
			$query = "Select * from user where email='$email'";
			$result = mysqli_query($this->connection, $query);
			if (mysqli_num_rows($result) == 1) {
				$data_envio = date('d/m/Y');
				$hora_envio = date('H:i:s');

    				$row = mysqli_fetch_assoc($result);
        			$password = $row["password"];
   			 	
				$arquivo = "
   					 <style type='text/css'>
    						body {
    							margin:0px;
    							font-family:Verdane;
    							font-size:12px;
    							color: #666666;
    						}
    						a{
    							color: #666666;
    							text-decoration: none;
    						}			
    						a:hover {
    							color: #FF0000;
   							text-decoration: none;
    						}
    					</style>
    				<html>
        				<table width='510' border='1' cellpadding='1' cellspacing='1' bgcolor='#CCCCCC'>
            					<tr>

                 					<td width='500'>SignsOn - Recupera��o de Senha</td>
                				</tr>
                				<tr>
                  					<td width='320'>E-mail:<b>$email</b></td>
       						</tr>
        					<tr>
                  					<td width='320'>Sua senha:<b>$password</b></td>
                				</tr>
       						
          					<tr>
           						 <td>Este e-mail foi enviado em <b>$data_envio</b> �s <b>$hora_envio</b></td>
          					</tr>
       					 </table>
    				</html>
    				"; 

    				
    				$destino = $email;
    				$assunto = "SignsOn - Recupera��o de Senha";
 

    				$headers  = 'MIME-Version: 1.0' . "\r\n";
        			$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
    	    		$headers .= 'From: SignsOn <mariana.tg2016@gmail.com>';

     
    				$enviaremail = mail($destino, $assunto, $arquivo, $headers);
    				if($enviaremail){
    					$json['success'] = 'E-mail enviado com sucesso';
    				} else {
    					$json['error'] = 'Erro no envio';
    				}

				
			} else {
    				$json['error'] = 'Erro no envio';
			}
			echo json_encode($json);
			mysqli_close($this->connection);
			
		}
		
		
	}
?>	
