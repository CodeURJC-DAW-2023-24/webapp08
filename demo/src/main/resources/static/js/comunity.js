async function buscar(nombre) {
    var friendContainer = document.getElementById("friend-container");
    friendContainer.innerHTML="";
    friendContainer.style.color = "black";
    friendContainer.style.fontSize = "20px"; 

    if (nombre.trim() !== "") {
    const response = await fetch(`/busqueda?nombre=${nombre}`);
    let data = await response.json();
    let nombres = data.lNameId;
    let admin = data.bAdmin;
    agregarElementosAlContenidoPrincipal(nombres,admin)

    }

}

function buscarValorInput() {
    var valorInput = document.getElementById("searchInput").value;
    buscar(valorInput);
}

function agregarElementosAlContenidoPrincipal(nombres,admin) {

var friendContainer = document.getElementById("friend-container");

var ulElement = document.createElement("ul");
ulElement.classList.add("friend-list");


   for (let i=0; i<nombres.length; i++) { 
    var liElement = document.createElement("li");
    liElement.classList.add("user-item");

    liElement.textContent = `${nombres[i][1]}`;

    var buttonElement = document.createElement("button");
    buttonElement.classList.add("solicitud-btn");
    buttonElement.textContent = "Enviar solicitud";
    buttonElement.addEventListener("click", function() {
        enviarSolicitud(nombres[i][0]);
    }); 

       
    var buttonContainer = document.createElement("div");
    buttonContainer.appendChild(buttonElement);

    if (admin){
        var deleteButton = document.createElement("button");
        deleteButton.classList.add("btn", "btn-danger", "btn-sm", "ml-2");
        deleteButton.innerHTML = '<i class="bi bi-trash"></i>'; 
        buttonContainer.appendChild(deleteButton);

        deleteButton.addEventListener("click", function() {
            deleteUser(nombres[i][0]);

        });
    }
        liElement.appendChild(buttonContainer);

    

    ulElement.appendChild(liElement);
}

friendContainer.appendChild(ulElement);

}

async function deleteUser(id){
    const response = await fetch(`/deleteUser?id=${id}`,{
        method: 'POST'
      });

      let nombres = await response.json();
      if (nombres==true) {
          var friendContainer = document.getElementById("friend-container");
          friendContainer.innerHTML="Usuario eliminado con exito";
          friendContainer.style.fontSize = "30px"; 
          friendContainer.style.color = "crimson";
      }
  
  
}

async function enviarSolicitud(id) { 
    const response = await fetch(`/sendSolicitud?id=${id}`,{
        method: 'POST'
      });

    let nombres = await response.json();
    if (nombres==true) {
        var friendContainer = document.getElementById("friend-container");
        friendContainer.innerHTML="Solicitud Mandada con Exito";
        friendContainer.style.fontSize = "30px"; 
        friendContainer.style.color = "limegreen";
    }

}

async function cargarAmigos(){
    const response = await fetch("/cargarAmigos");
    let lAmigos = await response.json();

    var ulElement = document.getElementById("list-group");

    lAmigos.forEach(function(amigo) {
        var liElement = document.createElement("li");
        liElement.className = "list-group-item";
        liElement.textContent = amigo;
    
       
        ulElement.appendChild(liElement);
    });
}

cargarAmigos();

