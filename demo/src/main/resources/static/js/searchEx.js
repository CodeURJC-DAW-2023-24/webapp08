async function buscar(nombre) {
    var exsContainer = document.getElementById("exs-container");
    exsContainer.innerHTML="";

    if (nombre.trim() !== "") {
    const response = await fetch(`/busquedaEx?nombre=${nombre}`);
    let nombres = await response.json();
    agregarElementosAlContenidoPrincipal(nombres)

    }

}
function agregarElementosAlContenidoPrincipal(nombres) {

    var friendContainer = document.getElementById("exs-container");
    
    var ulElement = document.createElement("ul");
    ulElement.classList.add("exs-list");
    
    
       for (let i=0; i<nombres.length; i++) { //REVISAR AJKSLDHFHLSAKJDHFLKSJDHFKSJLHFKLSJDHFSKLJHFSLKDJHFLSKJHDLKDJFHSKJLH ###############################################################
        var liElement = document.createElement("li");
          
       //var textNode = document.createTextNode(`${nombre}`);
        liElement.textContent = `${nombres[i][1]}`;
    
        
        liElement.appendChild(buttonElement);
    
        ulElement.appendChild(liElement);
    }
    
    friendContainer.appendChild(ulElement);
    
    }
    