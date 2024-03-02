async function verNotificaciones() {
    var dropdownMenu = document.getElementById("dropdown-menu");
    dropdownMenu.innerHTML=""
    const response = await fetch("/notificaciones");
    let notificaciones = await response.json();
    agregarElementos(notificaciones);
    if (notificaciones.length == 0) {
        dropdownMenu.innerHTML="NO TIENES NIGUNA NOTIFICACION"
    }
}

function agregarElementos(notificaciones) {

    var dropdownMenu = document.getElementById("dropdown-menu");

    notificaciones.forEach(function(notificacion) {
    var listItem = document.createElement("li");

    
    listItem.className = "dropdown-item";
    
    listItem.textContent = notificacion.contenido + " ";

    var checkButton = document.createElement("button");
    checkButton.className = "btn btn-success";
    checkButton.innerHTML = '<i class="bi bi-check-circle"></i>'; 

    var xButton = document.createElement("button");
    xButton.className = "btn btn-danger";
    xButton.innerHTML = '<i class="bi bi-x-circle"></i>';

    checkButton.addEventListener("click", function() {
        procesarSolicitud(notificacion,true)
    });

    xButton.addEventListener("click", function() {
        procesarSolicitud(notificacion,false)
    });


    listItem.appendChild(checkButton);
    listItem.appendChild(xButton);

    dropdownMenu.appendChild(listItem);

    });


    async function procesarSolicitud(notificacion, booleano) {  
            await fetch( `/procesarSolicitud?notificacion=${notificacion.id}&aceptar=${booleano}`,{
                method: 'POST'
              });
         
        }
      

   
}