async function buscarEx(nombre) {
    var exC = document.getElementById("exs-container");
    exC.innerHTML = "";

    if (nombre.trim() !== "") {
        const response = await fetch(`/busquedaEx?nombre=${nombre}`);
        let nombres = await response.json();
        agregarElementosAlContenidoPrincipal2(nombres)

    }
}

function agregarElementosAlContenidoPrincipal2(nombres) {

    var exC = document.getElementById("exs-container");
    document.getElementById('overlay-container').style.display = 'block';
    var lista = document.getElementById('exerciseList');
    var alturaFinal = (nombres.length * 2); // Add 2px for each element

    var overlayContainer = document.getElementById('overlay-container');
    overlayContainer.style.height = alturaFinal + 'px';
    var ulElement = document.createElement("ul");
    ulElement.className="ulEl"
    ulElement.classList.add("exs-list");

    for (let i = 0; i < nombres.length; i++) {
        var liElement = document.createElement("li");
       
        liElement.classList.add("exercise-item");

        var aElement = document.createElement("a");
      
        aElement.textContent = nombres[i];
        aElement.href = "/exercise/" + nombres[i]; //Establish the url to redirect in when someone click on it Aquí puedes establecer la URL a la que debe dirigirse cuando se haga clic

        liElement.appendChild(aElement);
        ulElement.appendChild(liElement);
    }



    exC.appendChild(ulElement);

}

