async function search(name) {
    var friendContainer = document.getElementById("friend-container");
    friendContainer.innerHTML="";
    friendContainer.style.color = "black";
    friendContainer.style.fontSize = "20px"; 

    if (name.trim() !== "") {
    const RESPONSE = await fetch(`/searchUsers?nombre=${name}`);
    let data = await RESPONSE.json();
    let names = data.lNameId;
    let admin = data.bAdmin;
    addElementsMainContainer(names,admin)

    }

}

function searchValueInput() {
    var valueInput = document.getElementById("searchInput").value;
    search(valueInput);
}

function addElementsMainContainer(names,admin) {

var friendContainer = document.getElementById("friend-container");

var ulElement = document.createElement("ul");
ulElement.classList.add("friend-list");


   for (let i=0; i<names.length; i++) { 
    var liElement = document.createElement("li");
    liElement.classList.add("user-item");

    liElement.textContent = `${names[i][1]}`;

    var buttonElement = document.createElement("button");
    buttonElement.classList.add("request-btn");
    buttonElement.textContent = "Enviar solicitud";
    buttonElement.addEventListener("click", function() {
        sendRequest(names[i][0]);
    }); 

       
    var buttonContainer = document.createElement("div");
    buttonContainer.appendChild(buttonElement);

    if (admin){
        var deleteButton = document.createElement("button");
        deleteButton.classList.add("btn", "btn-danger", "btn-sm", "ml-2");
        deleteButton.innerHTML = '<i class="bi bi-trash"></i>'; 
        buttonContainer.appendChild(deleteButton);

        deleteButton.addEventListener("click", function() {
            deleteUser(names[i][0]);

        });
    }
        liElement.appendChild(buttonContainer);

    

    ulElement.appendChild(liElement);
}

friendContainer.appendChild(ulElement);

}

async function deleteUser(id){
    let csrfToken = document.querySelector('input[name="_csrf"]').value;

    const RESPONSE = await fetch(`/deleteUser?id=${id}`,{
        method: 'POST',
        headers: { 'X-XSRF-TOKEN': csrfToken }

      });

      let names = await RESPONSE.json();
      if (names==true) {
          var friendContainer = document.getElementById("friend-container");
          friendContainer.innerHTML="Usuario eliminado con exito";
          friendContainer.style.fontSize = "30px"; 
          friendContainer.style.color = "crimson";
      }
  
  
}


async function sendRequest(id) { 
    let csrfToken = document.querySelector('input[name="_csrf"]').value;

    const RESPONSE = await fetch(`/sendRequest?id=${id}`,{
        method: 'POST',
       headers: { 'X-XSRF-TOKEN': csrfToken }
      });

    let names = await RESPONSE.json();
    if (names==true) {
        var friendContainer = document.getElementById("friend-container");
        friendContainer.innerHTML="Solicitud Mandada con Exito";
        friendContainer.style.fontSize = "30px"; 
        friendContainer.style.color = "limegreen";
    }

}

async function loadFriends(){
    const RESPONSE = await fetch("/loadFriends");
    let lFriends = await RESPONSE.json();

    var ulElement = document.getElementById("list-group");

    lFriends.forEach(function(friend) {
        var liElement = document.createElement("li");
        liElement.className = "list-group-item";
        liElement.style.display = "flex";
        liElement.style.justifyContent = "space-between";
        liElement.textContent = friend;

        var buttonContainer = document.createElement("div");
        var deleteButton = document.createElement("button");
        deleteButton.classList.add("btn", "btn-danger", "btn-sm", "ml-2");
        deleteButton.innerHTML = '<i class="bi bi-trash"></i>'; 
        buttonContainer.appendChild(deleteButton);
    
         deleteButton.addEventListener("click", function() {
                deleteFriend(friend);
            });
    
        liElement.appendChild(buttonContainer);
       
        ulElement.appendChild(liElement);
    });
}

async function deleteFriend(alias){
    let csrfToken = document.querySelector('input[name="_csrf"]').value;

     await fetch(`/deleteFriend?alias=${alias}`,{
        method: 'POST',
        headers: { 'X-XSRF-TOKEN': csrfToken }

      });
      var ulElement = document.getElementById("list-group");
      ulElement.innerHTML = "";
      loadFriends();
  
  
}

loadFriends();

