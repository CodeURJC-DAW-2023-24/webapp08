import { Component, OnInit } from '@angular/core';
import { MainPageService } from '../services/main-page.service';
import { LoginService } from '../services/login.service';
import { Person } from '../models/person.model';
import { PersonService } from '../services/person.service';

@Component({
  selector: 'main-page',
  templateUrl: './main-page.component.html',
  styleUrl: '../../assets/css/mainPageStyle.css'
})
export class MainPageComponent {
  loadMore: number = 0;
  NUM_RESULTS: number = 10;
  data: any;
  shift: number = 0;
  admin: boolean;
  person: Person;
  roles: String[];

  constructor(public mainpageService: MainPageService, public loginservice: LoginService, public personService: PersonService) {
  }

  ngOnInit(): void {
    this.initElements();
    this.loadRutines();
    this.personService.getPerson().subscribe(
      response => {
        this.person = response as Person;
        this.roles = this.person.roles;
        if (this.roles.includes('ADMIN')) {
          this.admin = true;
        } else {
          this.admin = false;
        }

      },
    );
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
    const content: HTMLElement | null = document.querySelector('.content');
    if (content) {
      content.style.backgroundImage = `url('assets/images/gimnasio.jpg')`;
      content.style.backgroundSize = 'cover';
      content.style.backgroundRepeat = 'no-repeat';
    }
  }

  /////RUTINES //////////

  loadRutines() {
    this.mainpageService.getRutines().subscribe(
      (response: any) => {
        let rutines: any[] = response as any;
        this.agregarElementosCalendario(rutines);
      },
      error => {
        console.error('Error obteniendo rutinas:', error);
      });
  }

  agregarElementosCalendario(rutines: any[]) {
    let dayPairs: NodeListOf<Element> = document.querySelectorAll('.day-pair');
    let reversedDayPairs: Element[] = Array.from(dayPairs).reverse();

    let todayDate: Date = new Date();
    let newDay: number = todayDate.getDate() - (this.shift * 7);
    todayDate.setDate(newDay);

    let j: number = 0;
    for (let i: number = todayDate.getDay(); i > todayDate.getDay() - 7; i--) {
      let numberDay: number = (i + 7) % 7;
      let containerDay: Element = reversedDayPairs[j];

      let dayName: Element | null = containerDay.querySelector('.day-name');
      if (dayName) dayName.textContent = this.obtainDayName(numberDay);

      let dayNumber: Element | null = containerDay.querySelector('.day-number');
      let dateAux: Date = new Date(todayDate);
      newDay = dateAux.getDate() - j;
      dateAux.setDate(newDay);
      if (dayNumber) dayNumber.textContent = dateAux.toLocaleDateString();

      let calendaryContent: Element | null = containerDay.querySelector('.day-calendary-content');
      if (calendaryContent) calendaryContent.innerHTML = "";
      j += 1;
    }

    rutines.forEach(rutine => {
      let rutineDate: Date = new Date(rutine.date.split('T')[0]);
      rutineDate.setDate(rutineDate.getDate()); // due to the format

      todayDate.setUTCHours(0, 0, 0, 0);
      rutineDate.setUTCHours(0, 0, 0, 0);

      let startTime: number = rutineDate.getTime();
      let endTime: number = todayDate.getTime();

      // milliseconds
      let millisecondsDiff: number = (endTime - startTime);

      // from milliseconds to days
      const MILLINDAY: number = 1000 * 60 * 60 * 24;
      let daysDiff: number = Math.floor(millisecondsDiff / MILLINDAY);

      if ((0 <= daysDiff) && (daysDiff < 7)) {
        let containerDay: Element | null = reversedDayPairs[daysDiff];
        let calendaryContent: Element | null = containerDay?.querySelector('.day-calendary-content');
        if (calendaryContent) {
          calendaryContent.innerHTML += `<a href="/mainPage/showRutine?id=${rutine.id}" style="text-decoration: none; color: black;">${rutine.name}</a>`;
        }
      }
    });
  }

  obtainDayName(numberDay: number) {
    const WEEKDAYS: string[] = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
    return WEEKDAYS[numberDay];
  }

  previous() {
    this.shift += 1;
    this.loadRutines();
  }

  next() {
    this.shift -= 1;
    this.loadRutines();
  }



}
