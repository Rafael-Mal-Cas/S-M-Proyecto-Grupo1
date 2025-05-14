@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private String contraseña;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;

    // Getters y Setters
}
