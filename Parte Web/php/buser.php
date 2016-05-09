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
			$query = "Select * from user where email='$email' and password = '$password' ";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$json['success'] = 'Bem Vindo';
				echo json_encode($json);
				mysqli_close($this -> connection);
			}else{
				$json['error'] = "UsuÃ¡rio ou senha invÃ¡lidos";
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
					$json['success'] = 'UsuÃ¡rio Criado';
				}else{
					$json['error'] = 'Erro na CriaÃ§Ã£o';
				}
				echo json_encode($json);
				mysqli_close($this->connection);
			}else{
				$json['error'] = 'UsuÃ¡rio Existente';
				echo json_encode($json);
				mysqli_close($this->connection);
			}
			
		}

		
		public function edit($id,$password, $newpassword)
		{
			$query = "Select * from user where idUser ='$id' and password = '$password'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)<1){
				$query = "update user set password = '$newpassword' where idUser='$id";

				$updated = mysqli_query($this -> connection, $query);
				if($updated == 1 ){
					$json['success'] = 'Senha alterada';
				}else{
					$json['error'] = 'Erro na AlteraÃ§Ã£o';
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

    				$row = mysqli_fetch_assoc($result)
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

                 					<td width='500'>SignsOn - Recuperação de Senha</td>
                				</tr>
                				<tr>
                  					<td width='320'>E-mail:<b>$email</b></td>
       						</tr>
        					<tr>
                  					<td width='320'>Sua senha:<b>$password</b></td>
                				</tr>
       						
          					<tr>
           						 <td>Este e-mail foi enviado em <b>$data_envio</b> às <b>$hora_envio</b></td>
          					</tr>
       					 </table>
    				</html>
    				"; 

				// emails para quem será enviado o formulário
    				
    				$destino = $email;
    				$assunto = "SignsOn - Recuperação de Senha";
 
    				// É necessário indicar que o formato do e-mail é html

    				$headers  = 'MIME-Version: 1.0' . "\r\n";
        			$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
    	    			$headers .= 'From: SignsOn <mariana.tg2016@gmail.com>';

     
    				$enviaremail = mail($destino, $assunto, $arquivo, $headers);
    				if($enviaremail){
    					$mgm = "E-MAIL ENVIADO COM SUCESSO! <br> O link será enviado para o e-mail fornecido no formulário";
    					echo " <meta http-equiv='refresh' content='10;URL=contato.php'>";
    				} else {
    					$mgm = "ERRO AO ENVIAR E-MAIL!";
    					echo "";
    				}


			} else {
    				echo "0 results";
			}
			
		}
		
		
	}
?>	