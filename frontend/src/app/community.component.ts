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
  person: Person;
  roles: String[];

  ngOnInit(): void {
    this.personService.getPerson().subscribe(
      response => {
        this.person = response as Person;
        this.roles = this.person.roles;
        if (this.roles.includes('ADMIN')) {
          this.admin = true;
        } else {
          this.admin = false;
        }
        this.loadFriends();
      },
      error => {
        this.router.navigate(['../login']);
        this.person = { alias: "", name: "", date: "", weight: 0, roles: [] };

      }
    )

  }
  constructor(public loginservice: LoginService, public personService: PersonService, public router: Router, public communityService: CommunityService) {

  }


  search(event: any): void {
    const friendContainer: HTMLElement | null = document.getElementById("friend-container");
    let name: string;
    try {
      name = event.target.value;
    } catch (error) {
      name = event;
    }
    if (friendContainer) {
      friendContainer.innerHTML = "";
      friendContainer.style.color = "black";
      friendContainer.style.fontSize = "20px";

      if (name.trim() !== "") {
        this.communityService.searchUsers(name).subscribe(
          response => {
            let names: any = response as any;
            this.addElementsMainContainer(names, this.admin);
          },
          error => {
            console.error('Error buscando usuario:', error);
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
        buttonElement.addEventListener("click", function () {
          that.sendRequest(names[i][1]);
        });

        const buttonContainer: HTMLDivElement = document.createElement("div");
        buttonContainer.appendChild(buttonElement);

        if (admin) {
          const deleteButton: HTMLButtonElement = document.createElement("button");
          deleteButton.classList.add("btn", "btn-danger", "btn-sm", "ml-2");
          deleteButton.innerHTML = '<i class="bi bi-trash"></i>';
          buttonContainer.appendChild(deleteButton);

          deleteButton.addEventListener("click", function () {
            that.deleteUser(parseInt(names[i][0]));
          });
        }

        liElement.appendChild(buttonContainer);
        ulElement.appendChild(liElement);
      }

      friendContainer.appendChild(ulElement);
    }
  }

  deleteUser(id: number) {

    this.communityService.deleteUser(id).subscribe(
      response => {
        let name = response;
        if (name == null) {
          var friendContainer = document.getElementById("friend-container");
          if (friendContainer) {
            friendContainer.innerHTML = "Usuario eliminado con exito";
            friendContainer.style.fontSize = "30px";
            friendContainer.style.color = "crimson";
          }
        }
      },
      error => {
        console.error('Error borrando usuario:', error);

      });
  }

  sendRequest(alias: string) {
    this.communityService.sendFriendRequest(alias).subscribe(
      response =>{
     let names = response
     if (names == null) {
         var friendContainer = document.getElementById("friend-container");
         if (friendContainer) {
             friendContainer.innerHTML = "Solicitud Mandada con Exito";
             friendContainer.style.fontSize = "30px";
             friendContainer.style.color = "limegreen";
         }
     }},
     error => {
      console.error('Error borrando usuario:', error);

    });
  }

  loadFriends() {

    let lFriends = this.person.friends;

    var ulElement = document.getElementById("list-group") as HTMLElement;

    if (lFriends !== undefined) {
      lFriends.forEach(function (friend: string) {
        var liElement = document.createElement("li");
        liElement.className = "list-group-item";
        liElement.textContent = friend;

        ulElement.appendChild(liElement);
      });
    }
  }

}
