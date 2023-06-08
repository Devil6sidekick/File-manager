
public abstract class Element {
	private int id_element;
	private String name;
	public int getId_element() {
		return id_element;
	}
	public void setId_element(int id_element) {
		this.id_element = id_element;
	}
	private int id_user;
	private String nom;
	private Dossier parent;
	public int getTaille() {
		return 0;
		}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
}
