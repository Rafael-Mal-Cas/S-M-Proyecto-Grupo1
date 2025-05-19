<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.catalogo" %>
<%@ page import="modelo.coche" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Catálogo de Coches </title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="Style/Style_index.css" />
    <link rel="stylesheet" href="Style/Style_catalogo.css" />
    <link rel="icon" type="image/png" href="Style/logo.png" />

</head>
<body>

<header class="navbar">
    <div style="display: flex; align-items: center; gap: 20px;">
        <div class="logo">Venta de coches</div>
        <% 
            String usuario = (String) session.getAttribute("username");
            if (usuario != null) {
        %>
            <a href="catalogo.jsp" style="color: white; text-decoration: none; font-weight: bold;">Catálogo</a>
        <%
            }
        %>
    </div>
    <div class="user-menu">
        <% 
            if (usuario == null) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>
        <i class="fas fa-user-circle user-icon" onclick="toggleDropdown()"></i>
        <div id="dropdown" class="dropdown-content">
            <span class="username"><strong><%= usuario %></strong></span>
            <a href="Cuenta.jsp">Cuenta</a>
            <a href="logout.jsp">Cerrar sesión</a>
        </div>
    </div>
</header>


<main>
    <div class="catalogo-container">
        <!-- FILTROS A LA IZQUIERDA -->
        <div class="filtros-container">
            <h3>Filtros</h3>

            <label for="filtro-modelo">Modelo</label>
            <select id="filtro-modelo">
                <option value="">Todos los modelos</option>
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

            <label for="filtro-combustible">Combustible</label>
            <select id="filtro-combustible">
                <option value="">Todos los combustibles</option>
                <option value="Gasolina">Gasolina</option>
                <option value="Diesel">Diesel</option>
                <option value="Híbrido">Híbrido</option>
                <option value="Eléctrico">Eléctrico</option>
            </select>

            <label for="filtro-precio-min">Precio mínimo (€)</label>
            <input type="number" id="filtro-precio-min" min="0" placeholder="Min">

            <label for="filtro-precio-max">Precio máximo (€)</label>
            <input type="number" id="filtro-precio-max" min="0" placeholder="Max">

            <button type="button" onclick="filtrarCatalogo()">Filtrar</button>
            <button type="button" onclick="resetFiltros()">Resetear</button>
        </div>

        <!-- CATÁLOGO DE COCHES AL CENTRO -->
        <div class="catalogo-grid">
            <%
                catalogo catalogoBMW = new catalogo();
                String ruta = application.getRealPath("/WEB-INF/BMW_catalogo.csv");
                catalogoBMW.cargarDesdeCSV(ruta);

                ArrayList<coche> coches = catalogoBMW.getCoches(); // todos los coches sin filtro server side

                for (coche c : coches) {
            %>
            <div class="coche-card" data-modelo="<%= c.getModelo().toLowerCase() %>" data-combustible="<%= c.getCombustible().toLowerCase() %>" data-precio="<%= c.getPrecio() %>">
                <img src="<%= c.getImagen() %>" alt="<%= c.getMarca() + " " + c.getModelo() %>" class="coche-imagen" onerror="this.src='https://placehold.co/300x180?text=Sin+imagen';">
                <div class="coche-info">
                    <h2><%= c.getMarca() %> <%= c.getModelo() %></h2>
                    <p>Año: <%= c.getAnio() %></p>
                    <p>Color: <%= c.getColor() %></p>
                    <p>Motor: <%= c.getMotor() %></p>
                    <p>Combustible: <%= c.getCombustible() %></p>
                    <div class="coche-precio precio-sin-formato" data-precio-raw="<%= c.getPrecio() %>"><%= c.getPrecio() %> €</div>
                </div>
            </div>
            <%
                }
            %>
        </div>
    </div>
</main>

<script>
let lastScroll = 0;
const navbar = document.querySelector('.navbar');

window.addEventListener('scroll', () => {
    const currentScroll = window.scrollY;
    if (currentScroll > lastScroll && currentScroll > 50) {
        navbar.classList.add('hidden'); // scroll hacia abajo
    } else {
        navbar.classList.remove('hidden'); // scroll hacia arriba
    }
    lastScroll = currentScroll;
});
</script>


 <script>
    function toggleDropdown() {
        document.getElementById("dropdown").classList.toggle("show");
    }

    window.onclick = function(event) {
        if (!event.target.matches('.user-icon')) {
            const dropdown = document.getElementById("dropdown");
            if (dropdown && dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        }
    }
</script>


<script>
    function filtrarCatalogo() {
        const modelo = document.getElementById('filtro-modelo').value.toLowerCase();
        const combustible = document.getElementById('filtro-combustible').value.toLowerCase();
        const precioMin = parseFloat(document.getElementById('filtro-precio-min').value) || 0;
        const precioMaxInput = document.getElementById('filtro-precio-max').value;
        const precioMax = precioMaxInput === "" ? Infinity : parseFloat(precioMaxInput);

        const coches = document.querySelectorAll('.coche-card');

        coches.forEach(coche => {
            const cocheModelo = coche.getAttribute('data-modelo');
            const cocheCombustible = coche.getAttribute('data-combustible');
            const cochePrecio = parseFloat(coche.getAttribute('data-precio'));

            const coincideModelo = modelo === '' || cocheModelo.includes(modelo);
            const coincideCombustible = combustible === '' || cocheCombustible.includes(combustible);
            const coincidePrecio = cochePrecio >= precioMin && cochePrecio <= precioMax;

            if (coincideModelo && coincideCombustible && coincidePrecio) {
                coche.style.display = 'flex';
            } else {
                coche.style.display = 'none';
            }
        });
    }

    function resetFiltros() {
        document.getElementById('filtro-modelo').value = '';
        document.getElementById('filtro-combustible').value = '';
        document.getElementById('filtro-precio-min').value = '';
        document.getElementById('filtro-precio-max').value = '';

        const coches = document.querySelectorAll('.coche-card');
        coches.forEach(coche => {
            coche.style.display = 'flex';
        });
    }
</script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Formatear precios al cargar la página
        const precios = document.querySelectorAll('.precio-sin-formato');
        precios.forEach(precioElement => {
            const valorNumerico = parseFloat(precioElement.getAttribute('data-precio-raw'));
            precioElement.textContent = new Intl.NumberFormat('es-ES', {
                style: 'decimal',
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }).format(valorNumerico) + ' €';
        });
    });
</script>

</body>
</html>