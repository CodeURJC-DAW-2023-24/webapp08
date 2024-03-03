async function searchEx(name) {
    var exC = document.getElementById("exs-container");
    exC.innerHTML = "";

    if (name.trim() !== "") {
        const response = await fetch(`/searchEx?nombre=${name}`);
        let names = await response.json();
        addElementsMainContainer2(names)

    }
}

function addElementsMainContainer2(names) {

    var exC = document.getElementById("exs-container");
    document.getElementById('overlay-container').style.display = 'block';
    var finalHeight = (names.length * 2); // Add 2px for each element

    var overlayContainer = document.getElementById('overlay-container');
    overlayContainer.style.height = finalHeight + 'px';
    var ulElement = document.createElement("ul");
    ulElement.className="ulEl"
    ulElement.classList.add("exs-list");

    for (let i = 0; i < names.length; i++) {
        var liElement = document.createElement("li");
       
        liElement.classList.add("exercise-item");

        var aElement = document.createElement("a");
      
        aElement.textContent = names[i];
        aElement.href = "/mainPage/exerciseSearch/exercise/" + names[i]; //Establish the url to redirect in when someone click on it Aquí puedes establecer la URL a la que debe dirigirse cuando se haga clic

        liElement.appendChild(aElement);
        ulElement.appendChild(liElement);
    }



    exC.appendChild(ulElement);

}

