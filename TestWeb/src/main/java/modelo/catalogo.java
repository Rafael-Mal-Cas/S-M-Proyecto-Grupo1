package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class catalogo {
    private ArrayList<coche> coches;

    public catalogo() {
        coches = new ArrayList<>();
    }

    public void cargarDesdeCSV(String ruta) {
        coches.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            boolean primeraLinea = true;

            while ((line = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                String[] campos = line.split(",");

                if (campos.length >= 9) {
                    String id = campos[0].trim();
                    String marca = campos[1].trim();
                    String modelo = campos[2].trim();
                    int anio = Integer.parseInt(campos[3].trim());
                    String color = campos[4].trim();
                    double precio = Double.parseDouble(campos[5].trim());
                    String motor = campos[6].trim();
                    String combustible = campos[7].trim();
                    String imagen = campos[8].trim().replace("\"", "");

                    coche c = new coche(id, marca, modelo, anio, color, precio, motor, combustible, imagen);
                    coches.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Nuevo método para cargar coches desde la base de datos
    public void cargarDesdeDB(Connection conn) {
        coches.clear();
        String sql = "SELECT id, marca, modelo, anio, color, precio, motor, combustible, imagen FROM coches";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int anio = rs.getInt("anio");
                String color = rs.getString("color");
                double precio = rs.getDouble("precio");
                String motor = rs.getString("motor");
                String combustible = rs.getString("combustible");
                String imagen = rs.getString("imagen");

                coche c = new coche(id, marca, modelo, anio, color, precio, motor, combustible, imagen);
                coches.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<coche> getCoches() {
        return coches;
    }

    // Método para filtrar según parámetros
    public ArrayList<coche> filtrar(String modelo, String combustible, double precioMin, double precioMax) {
        ArrayList<coche> resultado = new ArrayList<>();

        for (coche c : coches) {
            // Filtro modelo (si modelo es null o vacío se ignora filtro)
            if (modelo != null && !modelo.isEmpty()) {
                if (!c.getModelo().equalsIgnoreCase(modelo)) {
                    continue;
                }
            }

            // Filtro combustible (si null, vacío o "Todos los combustibles" se ignora filtro)
            if (combustible != null && !combustible.isEmpty() && !combustible.equalsIgnoreCase("Todos los combustibles")) {
                if (!c.getCombustible().equalsIgnoreCase(combustible)) {
                    continue;
                }
            }

            // Filtro precio mínimo
            if (c.getPrecio() < precioMin) {
                continue;
            }

            // Filtro precio máximo
            if (c.getPrecio() > precioMax) {
                continue;
            }

            // Si pasa todos los filtros, se añade
            resultado.add(c);
        }

        return resultado;
    }
}