import java.util.List;
import java.util.Scanner;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import java.sql.*;
public class IHM {

	public static void main(String[] args) throws SQLException {
		String dbpassword="Redded2003$";
		//connecting to database
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e1) {
			throw new RuntimeException("error");
		}
		String url="jdbc:mysql://localhost:3306/os";
		Connection connexion=null;
		try {
			connexion=DriverManager.getConnection(url,"root",dbpassword);
			
		}catch(SQLException e) {
			System.out.println("Connexion echouée");
		}
		
		//
		Controlleur controlleur=new Controlleur();
		System.out.println("Hello,Please insert the username and password for authentication");
		Scanner scanner= new Scanner(System.in);
		String username=scanner.next();
		String password=scanner.next();
		
		User user=controlleur.authenticate(username, password);
		
		//Functionalities of a normal user
		
		Dossier root=controlleur.getroot(user.getId_user());
		
		controlleur.UserFunctonFolder(root.getId_element(),user.getId_user());
		
		
		
		
		
		
		
		}
	}
