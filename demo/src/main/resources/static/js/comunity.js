async function buscar(nombre) {
    var friendContainer = document.getElementById("friend-container");
    friendContainer.innerHTML="";

    if (nombre.trim() !== "") {
    const response = await fetch(`/busqueda?nombre=${nombre}`);
    let nombres = await response.json();
    agregarElementosAlContenidoPrincipal(nombres)

    }

}

function buscarValorInput() {
    var valorInput = document.getElementById("searchInput").value;
    buscar(valorInput);
}

function agregarElementosAlContenidoPrincipal(nombres) {

var friendContainer = document.getElementById("friend-container");

var ulElement = document.createElement("ul");
ulElement.classList.add("friend-list");


   for (let i=0; i<nombres.length; i++) { //REVISAR AJKSLDHFHLSAKJDHFLKSJDHFKSJLHFKLSJDHFSKLJHFSLKDJHFLSKJHDLKDJFHSKJLH ###############################################################
    var liElement = document.createElement("li");
    liElement.classList.add("user-item");

   //var textNode = document.createTextNode(`${nombre}`);
    liElement.textContent = `${nombres[i][1]}`;

    var buttonElement = document.createElement("button");
    buttonElement.classList.add("solicitud-btn");
    buttonElement.textContent = "Enviar solicitud";
    buttonElement.addEventListener("click", function() {
        enviarSolicitud(nombres[i][0]);
    }); //Enviar solicitud al otro usuario se resume en añadirle una notificación enviarSolicitud(nombres[i][0])

    //liElement.appendChild(textNode);
    liElement.appendChild(buttonElement);

    ulElement.appendChild(liElement);
}

friendContainer.appendChild(ulElement);

}

async function enviarSolicitud(id) { //Ns xq no se pone el tipo del parámetro recibido
    const response = await fetch(`/sendSolicitud?id=${id}`,{
        method: 'POST'
      });

    let nombres = await response.json();
    if (nombres==true) {
        var friendContainer = document.getElementById("friend-container");
        friendContainer.innerHTML="Solicitud Mandada con Exito";
    }

}


async function cargarAmigos(){
    const response = await fetch("/cargarAmigos");
    let lAmigos = await response.json();

    var ulElement = document.getElementById("list-group");

    lAmigos.forEach(function(amigo) {
        // Crear un elemento li
        var liElement = document.createElement("li");
        liElement.className = "list-group-item";
        liElement.textContent = amigo;
    
        // Agregar el elemento li al elemento ul
        ulElement.appendChild(liElement);
    });
}

cargarAmigos();

