async function showNotifications() {
    var dropdownMenu = document.getElementById("dropdown-menu");
    dropdownMenu.innerHTML=""
    const RESPONSE = await fetch("/notifications");
    let notifications = await RESPONSE.json();
    addElements(notifications);
    if (notifications.length == 0) {
        dropdownMenu.innerHTML="NO TIENES NIGUNA NOTIFICACION"
    }
}

function addElements(notifications) {

    var dropdownMenu = document.getElementById("dropdown-menu");

    notifications.forEach(function(notification) {
    var listItem = document.createElement("li");

    
    listItem.className = "dropdown-item";
    
    listItem.textContent = notification.content + " ";

    var checkButton = document.createElement("button");
    checkButton.className = "btn btn-success";
    checkButton.innerHTML = '<i class="bi bi-check-circle"></i>'; 

    var xButton = document.createElement("button");
    xButton.className = "btn btn-danger";
    xButton.innerHTML = '<i class="bi bi-x-circle"></i>';

    checkButton.addEventListener("click", function() {
        processRequest(notification,true)
    });

    xButton.addEventListener("click", function() {
        processRequest(notification,false)
    });


    listItem.appendChild(checkButton);
    listItem.appendChild(xButton);

    dropdownMenu.appendChild(listItem);

    });


    async function processRequest(notification, boolean) {  
        let csrfToken = document.querySelector('input[name="_csrf"]').value;

            await fetch( `/processRequest?notification=${notification.id}&aceptar=${boolean}`,{
                method: 'POST',
                headers: { 'X-XSRF-TOKEN': csrfToken }

              });
         
        }
      

   
}