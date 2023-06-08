
public class User {
	private int id_user;
	private String nom;
	private String username;
	private String password;
	public int getId_user() {
		return id_user;}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	
	private boolean is_admin=false;	
	public User() {}
		
	public User(int id_user, boolean is_admin) {}
		
	public boolean getIs_admin() {
		return is_admin;
	}
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	
	
}
