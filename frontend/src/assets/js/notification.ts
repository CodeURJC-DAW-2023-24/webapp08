async function showNotifications(): Promise<void> {
  const dropdownMenu = document.getElementById("dropdown-menu") as HTMLDivElement;
  dropdownMenu.innerHTML = "";
  const RESPONSE = await fetch("/notifications");
  const notifications = await RESPONSE.json();
  addElements(notifications);
  if (notifications.length === 0) {
      dropdownMenu.innerHTML = "NO TIENES NIGUNA NOTIFICACION";
  }
}

function addElements(notifications: any[]): void {
  const dropdownMenu = document.getElementById("dropdown-menu") as HTMLDivElement;

  notifications.forEach(notification => {
      const listItem = document.createElement("li") as HTMLLIElement;
      listItem.className = "dropdown-item";
      listItem.textContent = notification.content + " ";

      const checkButton = document.createElement("button") as HTMLButtonElement;
      checkButton.className = "btn btn-success";
      checkButton.innerHTML = '<i class="bi bi-check-circle"></i>';
      checkButton.addEventListener("click", () => {
          processRequest(notification, true);
      });

      const xButton = document.createElement("button") as HTMLButtonElement;
      xButton.className = "btn btn-danger";
      xButton.innerHTML = '<i class="bi bi-x-circle"></i>';
      xButton.addEventListener("click", () => {
          processRequest(notification, false);
      });

      listItem.appendChild(checkButton);
      listItem.appendChild(xButton);

      dropdownMenu.appendChild(listItem);
  });
}

async function processRequest(notification: any, boolean: boolean): Promise<void> {
  const csrfToken = (document.querySelector('input[name="_csrf"]') as HTMLInputElement).value;

  await fetch(`/processRequest?notification=${notification.id}&aceptar=${boolean}`, {
      method: 'POST',
      headers: { 'X-XSRF-TOKEN': csrfToken }
  });
}
