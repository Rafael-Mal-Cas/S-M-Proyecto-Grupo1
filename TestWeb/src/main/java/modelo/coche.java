package modelo;

public class coche {
    private String id;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private double precio;
    private String motor;
    private String combustible;
    private String imagen;

    public coche(String id, String marca, String modelo, int anio2, String color,
                 double precio2, String motor, String combustible, String imagen) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio2;
        this.color = color;
        this.precio = precio2;
        this.motor = motor;
        this.combustible = combustible;
        this.imagen = imagen;
    }

    public coche() {
        // Constructor vacío
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public String getColor() {
        return color;
    }

    public double getPrecio() {
        return precio;
    }

    public String getMotor() {
        return motor;
    }

    public String getCombustible() {
        return combustible;
    }

    public String getImagen() {
        return imagen;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // toString opcional
    @Override
    public String toString() {
        return "Coche{" +
                "id='" + id + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio='" + anio + '\'' +
                ", color='" + color + '\'' +
                ", precio='" + precio + '\'' +
                ", motor='" + motor + '\'' +
                ", combustible='" + combustible + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
