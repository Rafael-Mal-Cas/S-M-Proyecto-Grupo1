<%@ page import="java.io.BufferedReader, java.io.FileReader, java.util.ArrayList, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de Coches BMW</title>
    <link rel="stylesheet" href="Style_index.css">
    <link rel="stylesheet" href="catalogo.css">
</head>
<body>
    <!-- Navbar -->
    <div class="navbar">
        <div class="logo">BMW Catalogo</div>
        <div class="user-menu">
            <div class="user-icon">👤</div>
            <div class="dropdown-content" id="dropdownMenu">
                <span class="username">Usuario</span>
                <a href="#">Mi perfil</a>
                <a href="#">Cerrar sesión</a>
            </div>
        </div>
    </div>

    <div class="catalogo-container">
        <div class="catalogo-header">
            <h1>Catálogo de Coches BMW</h1>
            <p>Descubre nuestra exclusiva gama de vehículos</p>
        </div>

        <!-- Filtros -->
        <div class="filtros-container">
            <select id="filtro-modelo">
                <option value="">Todos los modelos</option>
                <option value="Serie 3">Serie 3</option>
                <option value="X5">X5</option>
                <option value="i8">i8</option>
                <option value="M4">M4</option>
                <option value="X1">X1</option>
                <option value="Z4">Z4</option>
                <option value="X3">X3</option>
                <option value="M2">M2</option>
                <option value="i3">i3</option>
                <option value="Serie 5">Serie 5</option>
                <option value="X6">X6</option>
                <option value="X2">X2</option>
                <option value="Serie 7">Serie 7</option>
                <option value="X4">X4</option>
                <option value="M5">M5</option>
                <option value="iX">iX</option>
                <option value="M8">M8</option>
                <option value="X7">X7</option>
                <option value="Serie 1">Serie 1</option>
                <option value="i4">i4</option>
            </select>

            <select id="filtro-combustible">
                <option value="">Todos los combustibles</option>
                <option value="Gasolina">Gasolina</option>
                <option value="Diesel">Diesel</option>
                <option value="Híbrido">Híbrido</option>
                <option value="Eléctrico">Eléctrico</option>
            </select>

            <input type="number" id="filtro-precio-min" placeholder="Precio mínimo" min="0">
            <input type="number" id="filtro-precio-max" placeholder="Precio máximo" min="0">

            <button onclick="filtrarCatalogo()">Filtrar</button>
            <button onclick="resetFiltros()">Resetear</button>
        </div>

        <!-- Grid de coches -->
        <div class="catalogo-grid">
            <%
                // Ruta al archivo CSV (ajusta según tu estructura de proyecto)
                String csvFile = getServletContext().getRealPath("/bmw_cars.csv");
                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ",";
                boolean firstLine = true;
                
                try {
                    br = new BufferedReader(new FileReader(csvFile));
                    while ((line = br.readLine()) != null) {
                        // Saltar la primera línea (cabeceras)
                        if (firstLine) {
                            firstLine = false;
                            continue;
                        }
                        
                        String[] coche = line.split(cvsSplitBy);
                        
                        // Comprobar que tenemos todos los campos necesarios
                        if (coche.length >= 10) {
                            String id = coche[0];
                            String brand = coche[1];
                            String model = coche[2];
                            String year = coche[3];
                            String color = coche[4];
                            String price = coche[5];
                            String engine = coche[6];
                            String fuel = coche[7];
                            String image = coche[8];
                            
                            // Limpiar la URL de la imagen si es necesario
                            image = image.replace("\"", "");
            %>
            <div class="coche-card" data-modelo="<%= model %>" data-combustible="<%= fuel %>" data-precio="<%= price %>">
                <img src="<%= image %>" alt="<%= brand %> <%= model %>" class="coche-imagen" onerror="this.src='https://via.placeholder.com/300x180?text=Imagen+no+disponible'">
                <div class="coche-info">
                    <h2><%= brand %> <%= model %></h2>
                    <p>Año: <%= year %></p>
                    <p>Color: <%= color %></p>
                    <p>Motor: <%= engine %></p>
                    <p>Combustible: <%= fuel %></p>
                    <div class="coche-precio"><%= price %> €</div>
                </div>
            </div>
            <%
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            %>
        </div>
    </div>

    <script>
        // Mostrar/ocultar menú desplegable
        document.querySelector('.user-icon').addEventListener('click', function() {
            document.getElementById('dropdownMenu').classList.toggle('show');
        });

        // Cerrar el menú desplegable si se hace clic fuera de él
        window.onclick = function(event) {
            if (!event.target.matches('.user-icon')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }

        // Función para filtrar el catálogo
        function filtrarCatalogo() {
            const modelo = document.getElementById('filtro-modelo').value.toLowerCase();
            const combustible = document.getElementById('filtro-combustible').value.toLowerCase();
            const precioMin = parseFloat(document.getElementById('filtro-precio-min').value) || 0;
            const precioMax = parseFloat(document.getElementById('filtro-precio-max').value) || Infinity;
            
            const coches = document.querySelectorAll('.coche-card');
            
            coches.forEach(coche => {
                const cocheModelo = coche.getAttribute('data-modelo').toLowerCase();
                const cocheCombustible = coche.getAttribute('data-combustible').toLowerCase();
                const cochePrecio = parseFloat(coche.getAttribute('data-precio'));
                
                const coincideModelo = modelo === '' || cocheModelo.includes(modelo);
                const coincideCombustible = combustible === '' || cocheCombustible.includes(combustible);
                const coincidePrecio = cochePrecio >= precioMin && cochePrecio <= precioMax;
                
                if (coincideModelo && coincideCombustible && coincidePrecio) {
                    coche.style.display = 'block';
                } else {
                    coche.style.display = 'none';
                }
            });
        }
        
        // Función para resetear los filtros
        function resetFiltros() {
            document.getElementById('filtro-modelo').value = '';
            document.getElementById('filtro-combustible').value = '';
            document.getElementById('filtro-precio-min').value = '';
            document.getElementById('filtro-precio-max').value = '';
            
            const coches = document.querySelectorAll('.coche-card');
            coches.forEach(coche => {
                coche.style.display = 'block';
            });
        }
    </script>
</body>
</html>