package modelo;

public class User {
    private int id;
    private String nombre;
    private String apellidos;
    private String genero;
    private String email;
    private String numeroTelefono;
    private String usuario;
    private String contrasena;

    public User(int id, String nombre, String apellidos, String genero, 
               String email, String numeroTelefono, String usuario, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
        this.email = email;
        this.numeroTelefono = numeroTelefono;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public User() {
		// TODO Auto-generated constructor stub
	}

	// Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getGenero() {
        return genero;
    }

    public String getEmail() {
        return email;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método toString() útil para debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", genero='" + genero + '\'' +
                ", email='" + email + '\'' +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }

	public void setImagen(String string) {
		// TODO Auto-generated method stub
		
	}
}