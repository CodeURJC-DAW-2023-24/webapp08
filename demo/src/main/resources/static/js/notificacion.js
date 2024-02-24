async function verNotificaciones() {
    var dropdownMenu = document.getElementById("dropdown-menu");
    dropdownMenu.innerHTML=""
    const response = await fetch("/notificaciones");
    let notificaciones = await response.json();
    agregarElementos(notificaciones);
}

function agregarElementos(notificaciones) {
    // Crear los elementos de lista y enlaces

    var dropdownMenu = document.getElementById("dropdown-menu");

    notificaciones.forEach(function(notificacion) {
        // Crear el elemento de lista <li>
    var listItem = document.createElement("li");

    // Crear el enlace
    
    listItem.className = "dropdown-item";
    
    listItem.textContent = notificacion.contenido + " ";

    // Crear botón de check verde
    var checkButton = document.createElement("button");
    checkButton.className = "btn btn-success";
    checkButton.innerHTML = '<i class="bi bi-check-circle"></i>'; // Icono de check verde de Bootstrap

    // Crear el botón de X roja con Bootstrap
    var xButton = document.createElement("button");
    xButton.className = "btn btn-danger";
    xButton.innerHTML = '<i class="bi bi-x-circle"></i>'; // Icono de X roja de Bootstrap

    // Agregar los eventos de clic a los botones (si es necesario)
    checkButton.addEventListener("click", function() {
        procesarSolicitud(notificacion,true)// Agregar aquí la lógica para manejar el evento de clic del botón de check
    });

    xButton.addEventListener("click", function() {
        procesarSolicitud(notificacion,false)// Agregar aquí la lógica para manejar el evento de clic del botón de check
        // Agregar aquí la lógica para manejar el evento de clic del botón X
    });

    // Agregar los elementos al listItem

    listItem.appendChild(checkButton);
    listItem.appendChild(xButton);

    // Agregar el listItem al menú desplegable
    dropdownMenu.appendChild(listItem);

    });


    async function procesarSolicitud(notificacion, booleano) {  
            await fetch( `/procesarSolicitud?notificacion=${notificacion.id}&aceptar=${booleano}`,{
                method: 'POST'
              });
         
              verNotificaciones(); //para que se borre la que acaba de aceptar
        }
      

   
}