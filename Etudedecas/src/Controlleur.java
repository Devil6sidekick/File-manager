import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import java.sql.*;

public class Controlleur {
	
	public Controlleur() {}

    public void insertUser() throws SQLException {
    	String url = "jdbc:mysql://localhost:3306/os";
    	String username = "root";
    	String password = "Redded2003$";
    	try (Connection connection = DriverManager.getConnection(url, username, password)) {
    	    String query = "INSERT INTO user (username, password,is_admin) VALUES ('drago','regime',0)";
    	    PreparedStatement statement = connection.prepareStatement(query);
    	    int rows=statement.executeUpdate();
    	    if(rows>0) {System.out.println("successfully added user");}
    	    else {System.out.println("unsuccessfully added user");}}
    }
    
    public User authenticate(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/os";
        String theusername = "root";
        String thepassword = "Redded2003$";
        User user = null;
        try {
            Connection connection = DriverManager.getConnection(url, theusername, thepassword);
            String query = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                int id = res.getInt("iduser");
                String name = res.getString("name");
                boolean isAdmin = res.getBoolean("is_admin");
                user = new User();
                user.setId_user(id);
                user.setIs_admin(isAdmin);
                System.out.println("Login successful "+name+"!");
            }else {System.out.println("Login unsuccessful!");}
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
   // get root for each user
    public Dossier getroot(int userId) {
        Dossier folder = null;
        String url = "jdbc:mysql://localhost:3306/os";
        String username = "root";
        String password = "Redded2003$";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM folder WHERE  id_user = ? AND id_parent_folder= 0";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int folderId = rs.getInt("idfolder");
                    String name = rs.getString("name");

                    folder = new Dossier();
                    folder.setId_element(folderId);
                    folder.setNom(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return folder;
    }
    //obtenir la liste des elements dans un dossier
    public List<Pair<Element, String>> getElementsFromParent(int idParentFolder) {
        List<Pair<Element, String>> elementList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/os";
        String username = "root";
        String password = "Redded2003$";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Select folders
            String folderQuery = "SELECT * FROM folder WHERE id_parent_folder = ?";
            PreparedStatement folderStatement = conn.prepareStatement(folderQuery);
            folderStatement.setInt(1, idParentFolder);
            try (ResultSet folderResultSet = folderStatement.executeQuery()) {
                while (folderResultSet.next()) {
                    int folderId = folderResultSet.getInt("idfolder");
                    String folderName = folderResultSet.getString("name");
                    Dossier dossier = new Dossier();
                    dossier.setId_element(folderId);
                    dossier.setNom(folderName);
                    elementList.add(new Pair<>(dossier, "dossier"));
                }
            }

            // Select files
            String fileQuery = "SELECT * FROM file WHERE id_parent_folder = ?";
            PreparedStatement fileStatement = conn.prepareStatement(fileQuery);
            fileStatement.setInt(1, idParentFolder);
            try (ResultSet fileResultSet = fileStatement.executeQuery()) {
                while (fileResultSet.next()) {
                    int fileId = fileResultSet.getInt("idfile");
                    String fileName = fileResultSet.getString("name");
                    Fichier fichier = new Fichier();
                    fichier.setId_element(fileId);
                    fichier.setNom(fileName);
                    elementList.add(new Pair<>(fichier, "fichier"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elementList;
    
    }
    public void UserFunctonFolder(int folder_id,int Userid) {
    	Scanner scanner= new Scanner(System.in); 
    	List<Pair<Element, String>> folderlist=getElementsFromParent(folder_id);
    	
		int elemselector=0;
		List<Pair<Integer, Element>> selectionlist = new ArrayList();
		System.out.println("Elements in the folder:");
		//printing elements in folder
		for(Pair<Element, String>c:folderlist) {
			elemselector+=1;
			selectionlist.add(new Pair<>(elemselector, c.left));
						
			System.out.print(elemselector+"."+c.left.getNom()+":"+c.right+"\n");
		}
		System.out.print("\n");
		System.out.println("Please select what you wish to do:\n1.Create file\n2.Create Folder\n3.Select element\n4.Log out");
		int choix = scanner.nextInt();
		switch (choix) {
		case 1:
			//Create file
			System.out.println("Please insert the name and size of the file: ");
			String fname=scanner.next();
			int fsize=scanner.nextInt();
			int elementId=createElement(fname,folder_id,Userid,fsize);
			createFile(fname,folder_id,elementId);
			break;
		case 2:
			//Create folder
			System.out.println("Please insert the name of the folder: ");
			String foname=scanner.next();
			int elementid=createElement(foname,folder_id,Userid,0);
			createFolder(foname,folder_id,Userid,elementid);
			break;
		case 3:
			//Select element
			System.out.println("Pick which element by inserting it's number");
			int element=scanner.nextInt();
			for(Pair<Integer, Element> c:selectionlist) {if(element==c.left) {
				for(Pair<Element,String> e:folderlist) {
					if(c.right==e.left) {
						if(e.right.equals("dossier")) {								
							UserFunctonFolder(e.left.getId_element(),Userid);}
						else {
						//element is a file; show size
								
					} }
				}}}break;				
		case 4:
			//log out
			System.exit(0);
			break;
		}
   }
    
    
    public int createElement(String name, int idParentFolder, int userId, int size) {
    	String url = "jdbc:mysql://localhost:3306/os";
        String theusername = "root";
        String thepassword = "Redded2003$";
        try (Connection conn = DriverManager.getConnection(url, theusername, thepassword)) {
            String elementQuery = "INSERT INTO element (name, id_parent_folder, userid, size) VALUES (?, ?, ?, ?)";
            PreparedStatement elementStatement = conn.prepareStatement(elementQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            elementStatement.setString(1, name);
            elementStatement.setInt(2, idParentFolder);
            elementStatement.setInt(3, userId);
            elementStatement.setInt(4, size);

            // Execute the query
            elementStatement.executeUpdate();

            // Get the auto-generated element ID
            ResultSet generatedKeys = elementStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int elementId = generatedKeys.getInt(1);
                System.out.println("Element created with ID: " + elementId);
                return elementId;
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        
    }
    
    
    public void createFile(String name, int idParentFolder, int elementId) {
    	String url = "jdbc:mysql://localhost:3306/os";
        String theusername = "root";
        String thepassword = "Redded2003$";
        try (Connection conn = DriverManager.getConnection(url, theusername, thepassword)) {
            String fileQuery = "INSERT INTO file (name, id_parent_folder, idelement) VALUES (?, ?, ?)";
            PreparedStatement fileStatement = conn.prepareStatement(fileQuery);
            fileStatement.setString(1, name);
            fileStatement.setInt(2, idParentFolder);
            fileStatement.setInt(3, elementId);

            // Execute the query
            fileStatement.executeUpdate();

            System.out.println("File created with name: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createFolder(String name, int idParentFolder, int userId, int elementId) {
        String url = "jdbc:mysql://localhost:3306/os";
        String theusername = "root";
        String thepassword = "Redded2003$";
        
        try (Connection conn = DriverManager.getConnection(url, theusername, thepassword)) {
            // Insert into the folder table with the provided element ID
            String folderQuery = "INSERT INTO folder (idelement, id_parent_folder, name, id_user) VALUES (?, ?, ?, ?)";
            PreparedStatement folderStatement = conn.prepareStatement(folderQuery, Statement.RETURN_GENERATED_KEYS);
            folderStatement.setInt(1, elementId);
            folderStatement.setInt(2, idParentFolder);
            folderStatement.setString(3, name);
            folderStatement.setInt(4, userId);

            // Execute the query
            folderStatement.executeUpdate();

            // Get the auto-generated folder ID
            ResultSet generatedKeys = folderStatement.getGeneratedKeys();
            int folderId = -1;
            if (generatedKeys.next()) {
                folderId = generatedKeys.getInt(1);
            }

            if (folderId != -1) {
                System.out.println("Folder created with ID: " + folderId + " and name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
	
	
		
	

