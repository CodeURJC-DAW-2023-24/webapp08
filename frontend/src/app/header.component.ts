import { HeaderService } from './../../services/header.service';
import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css'
  ]
})
export class HeaderComponent {
  @Input() searchOptions: { search: boolean, search2: boolean, admin:boolean } = { search: false, search2: true, admin:false };
  constructor(public loginservice:LoginService, public headerService:HeaderService,public personService: PersonService, public router: Router) { }
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
  );

  }
   showNotifications(){
    const dropdownMenu = document.getElementById("dropdown-menu") as HTMLDivElement;
    dropdownMenu.innerHTML = "";
    this.headerService.getRequests().subscribe(
      response => {
      let notifications: any[] = response as any;
      this.addElements(notifications);
      if (notifications.length === 0) {

        dropdownMenu.innerHTML = "NO TIENES NIGUNA NOTIFICACION";
    }
      },
      error => {
        console.error('Error obteniendo novedades:', error);
      });
}

 addElements(notifications: any[]): void {
    const dropdownMenu = document.getElementById("dropdown-menu") as HTMLDivElement;

    notifications.forEach(notification => {
        const listItem = document.createElement("li") as HTMLLIElement;
        listItem.className = "dropdown-item";
        listItem.textContent = notification.content + " ";

        const checkButton = document.createElement("button") as HTMLButtonElement;
        checkButton.className = "btn btn-success";
        checkButton.innerHTML = '<i class="bi bi-check-circle"></i>';
        checkButton.addEventListener("click", () => {
            this.processRequest(notification, true);
        });

        const xButton = document.createElement("button") as HTMLButtonElement;
        xButton.className = "btn btn-danger";
        xButton.innerHTML = '<i class="bi bi-x-circle"></i>';
        xButton.addEventListener("click", () => {
            this.processRequest(notification, false);
        });

        listItem.appendChild(checkButton);
        listItem.appendChild(xButton);

        dropdownMenu.appendChild(listItem);
    });
}

 processRequest(notification: any, accepted: boolean) {
     this.headerService.proccessRequest(notification.id,accepted).subscribe(
        response => {
        let notifications: any[] = response as any;

        if (notifications == null) {
          const dropdownMenu = document.getElementById("dropdown-menu") as HTMLDivElement;
          dropdownMenu.innerHTML = "NO TIENES NIGUNA NOTIFICACION";
      } else {
        this.showNotifications();
      }
        },
        error => {
          console.error('Error aceptando la solicitud:', error);
        });
  }
  onKeyDown(event: KeyboardEvent) {
    if (event.keyCode === 13) {
      // Si se presiona Enter, llamar a la función searchEx con el valor del campo de entrada
      const inputValue = (event.target as HTMLInputElement).value;
      this.searchEx(inputValue, event);
    }
  }
  searchEx(value:string, event:any){
      const container: HTMLElement | null = document.getElementById("friend-container");
      let name: string;
      try {
        name = event.target.value;
      } catch (error) {
        name = event;
      }
      if (container) {
        container.innerHTML = "";
        container.style.color = "black";
        container.style.fontSize = "20px";

        if (name.trim() !== "") {
         // this.communityService.searchUsers(name).subscribe(
         //   response => {
         //     let names: any = response as any;
         //     this.addElementsMainContainer(names);
         //   },)
        }
      }

  }
  addElementsMainContainer(names: string[]): void {
      const exC = document.getElementById("exs-container");
      const container: HTMLElement | null = document.getElementById("overlay-container");
      if(container){
      container.style.display = 'block';
      const finalHeight = names.length * 2; // Add 2px for each element
      container.style.height = finalHeight + 'px';

      const ulElement = document.createElement("ul");
      ulElement.className = "ulEl";
      ulElement.classList.add("exs-list");

      for (let i = 0; i < names.length; i++) {
          const liElement = document.createElement("li");
          liElement.classList.add("exercise-item");

          const aElement = document.createElement("a");
          aElement.textContent = names[i];
          aElement.href = "/mainPage/exerciseSearch/exercise/" + names[i]; // Establecer la URL a la que debe dirigirse cuando se haga clic

          liElement.appendChild(aElement);
          ulElement.appendChild(liElement);
      }
      if(exC){exC.appendChild(ulElement);}
  }

  }


}


