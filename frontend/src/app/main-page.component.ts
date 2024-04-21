import { Component, OnInit } from '@angular/core';
import { MainPageService } from '../../services/main-page.service';
import { LoginService } from './../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';

@Component({
  selector: 'main-page',
  templateUrl: './main-page.component.html',
  styleUrl: '../assets/css/mainPageStyle.css'
})
export class MainPageComponent {
  loadMore: number = 0;
  NUM_RESULTS: number = 10;
  data: any;
  admin: boolean;
  person:Person;
  roles: String[];

  constructor(public mainpageService:MainPageService, public loginservice:LoginService,public personService: PersonService) {
  }

  ngOnInit(): void {
    if (this.loginservice.isLogged()){
      this.person= this.loginservice.currentUser();}
      this.roles=this.person.roles;
      if (this.roles.includes('ADMIN')) {
        this.admin = true;
      }else{
        this.admin=false;
      }
      this.initElements()

    }
                //////NEWS/////////
    initElements(): any {
    this.loadMore = 0;
    let flag: boolean = false;
    this.mainpageService.getNews(0).subscribe(
      response => {
        this.data = response;
        let news: any[] = this.data;
        this.addElementsMainContainer(news);
        flag = news.length == this.NUM_RESULTS;
        let moreNews: HTMLElement | null = document.getElementById("container-loadMore");
      if (moreNews) {
      moreNews.style.display = flag ? "flex" : "none";
    }
      },
      error => {
        console.error('Error obteniendo novedades:', error);
      });
  }

   loadMoreFoo() {
    let flag: boolean = false;
    this.loadMore++;
    let spinnerContainer: HTMLElement | null = document.getElementById("spinner-container");
    let moreNews: HTMLElement | null = document.getElementById("container-loadMore");
    if (spinnerContainer && moreNews) {
      spinnerContainer.style.display = "flex";
      moreNews.style.display = "none";
    }
    this.mainpageService.getNews(this.loadMore).subscribe(
      response => {
        this.data = response;
        let news: any[] = this.data;
        this.addElementsMainContainer(news);
        flag = news.length == this.NUM_RESULTS;
        let moreNews: HTMLElement | null = document.getElementById("container-loadMore");
        if (spinnerContainer) {
          spinnerContainer.style.display = "none";
        }
        if (moreNews) {
          moreNews.style.display = flag ? "flex" : "none";
        }
      },
      error => {
        console.error('Error obteniendo novedades:', error);
      });
  }

  addElementsMainContainer(news: any[]): void {
    let containerNews: HTMLElement | null = document.getElementById("container-news");
    if (!containerNews) return;
    if (news.length === 0) {
      containerNews.innerText = "NO TIENES NINGUNA NOVEDAD";
      containerNews.style.textAlign = "center";
      containerNews.style.color = "white";
      containerNews.style.fontSize = "30px";
    }
    for (let i: number = 0; i < news.length; i += 2) {
      let row: HTMLDivElement = document.createElement('div');
      row.classList.add('row', 'mb-3');
      for (let j: number = 0; j < 2; j++) {
        if (i + j < news.length) {
          let col: HTMLDivElement = document.createElement('div');
          col.classList.add('col-md-6');
          let card: HTMLDivElement = document.createElement('div');
          card.classList.add('card', 'mb-3');
          let cardBody: HTMLDivElement = document.createElement('div');
          cardBody.classList.add('card-body');
          let cardTitle: HTMLHeadingElement = document.createElement('h5');
          cardTitle.classList.add('card-title');
          cardTitle.textContent = `NOVEDAD`;
          let cardText: HTMLParagraphElement = document.createElement('p');
          cardText.classList.add('card-text');
          let link: HTMLAnchorElement = document.createElement('a');
          link.href = `./mainPage/showRutine?id=${news[i + j].rutine.id}`;
          link.textContent = `Nueva rutina de: ${news[i + j].alias}`;
          link.style.textDecoration = 'none';
          cardText.appendChild(link);
          cardBody.appendChild(cardTitle);
          cardBody.appendChild(cardText);
          card.appendChild(cardBody);
          col.appendChild(card);
          row.appendChild(col);
        }
      }
      containerNews.appendChild(row);
    }
  }

            /////NOTIFICATIONS //////////
}
