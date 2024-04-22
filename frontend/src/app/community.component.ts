import { CommunityService } from './../../services/community.service';
import { Component } from '@angular/core';
import { LoginService } from './../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';
import { Router } from '@angular/router';
@Component({
  selector: 'community',
  templateUrl: './community.component.html',
  styleUrl: '../assets/css/comunityStyle.css'
})
export class CommunityComponent {
  admin: boolean;
  person:Person;
  roles: String[];

   ngOnInit(): void {
     this.personService.getPerson().subscribe(
       response => {
           this.person= response as Person;
           this.roles=this.person.roles;
           if (this.roles.includes('ADMIN')) {
             this.admin = true;
           }else{
             this.admin=false;
           }

       },
       error => {
         this.router.navigate(['../login']);
         this.person = {alias:"",name:"",date:"",weight:0, roles:[]};

       }
     )
   }
   constructor(public loginservice:LoginService,public personService: PersonService,public router: Router,public communityService: CommunityService) {
   //this.loadFriends();

   }


  /*search(event: any): void {
    const friendContainer: HTMLElement | null = document.getElementById("friend-container");
    let name = event.target.value;
    if (friendContainer) {
        friendContainer.innerHTML = "";
        friendContainer.style.color = "black";
        friendContainer.style.fontSize = "20px";

        if (name.trim() !== "") {
            fetch(`/searchUsers?nombre=${name}`)
                .then(response => response.json())
                .then(data => {
                    let names: string[][] = data.lNameId;
                    let admin: boolean = data.bAdmin;
                    this.addElementsMainContainer(names, admin);
                });
        }
    }
}

 searchValueInput(): void {
    const valueInput: string = (document.getElementById("searchInput") as HTMLInputElement).value;
    this.search(valueInput);
}

 addElementsMainContainer(names: string[][], admin: boolean): void {
    const friendContainer: HTMLElement | null = document.getElementById("friend-container");
    if (friendContainer) {
        const ulElement: HTMLUListElement = document.createElement("ul");
        ulElement.classList.add("friend-list");

        for (let i = 0; i < names.length; i++) {
            const liElement: HTMLLIElement = document.createElement("li");
            liElement.classList.add("user-item");
            liElement.textContent = `${names[i][1]}`;

            const buttonElement: HTMLButtonElement = document.createElement("button");
            buttonElement.classList.add("request-btn");
            buttonElement.textContent = "Enviar solicitud";
            let that = this
            buttonElement.addEventListener("click", function() {
                that.sendRequest(names[i][0]);
            });

            const buttonContainer: HTMLDivElement = document.createElement("div");
            buttonContainer.appendChild(buttonElement);

            if (admin) {
                const deleteButton: HTMLButtonElement = document.createElement("button");
                deleteButton.classList.add("btn", "btn-danger", "btn-sm", "ml-2");
                deleteButton.innerHTML = '<i class="bi bi-trash"></i>';
                buttonContainer.appendChild(deleteButton);

                deleteButton.addEventListener("click", function() {
                    that.deleteUser(names[i][0]);
                });
            }

            liElement.appendChild(buttonContainer);
            ulElement.appendChild(liElement);
        }

        friendContainer.appendChild(ulElement);
    }
}

  deleteUser(id: string) {
    let csrfToken: string | null = (document.querySelector('input[name="_csrf"]') as HTMLInputElement)?.value;

    const RESPONSE = await fetch(`/deleteUser?id=${id}`,{
        method: 'POST',
        headers: { 'X-XSRF-TOKEN': csrfToken }
    });

    let names = await RESPONSE.json();
    if (names == true) {
        var friendContainer = document.getElementById("friend-container");
        if (friendContainer) {
            friendContainer.innerHTML = "Usuario eliminado con exito";
            friendContainer.style.fontSize = "30px";
            friendContainer.style.color = "crimson";
        }
    }
}

  sendRequest(id: string) {
    let csrfToken: string | null = (document.querySelector('input[name="_csrf"]') as HTMLInputElement)?.value;

    const RESPONSE = await fetch(`/sendRequest?id=${id}`,{
        method: 'POST',
        headers: { 'X-XSRF-TOKEN': csrfToken }
    });

    let names = await RESPONSE.json();
    if (names == true) {
        var friendContainer = document.getElementById("friend-container");
        if (friendContainer) {
            friendContainer.innerHTML = "Solicitud Mandada con Exito";
            friendContainer.style.fontSize = "30px";
            friendContainer.style.color = "limegreen";
        }
    }
}

  loadFriends()  {
    const RESPONSE = await fetch("/loadFriends");
    let lFriends = await RESPONSE.json();

    var ulElement = document.getElementById("list-group") as HTMLElement;

    lFriends.forEach(function(friend: string) {
        var liElement = document.createElement("li");
        liElement.className = "list-group-item";
        liElement.textContent = friend;

        ulElement.appendChild(liElement);
    });
}
*/
}
