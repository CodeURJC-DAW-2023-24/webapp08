/*loadRutines() {
  this.mainpageService.getRutines().subscribe(
    (response: any) => {
      let rutines: any[] = response as any;
      this.agregarElementosCalendario(rutines);
    }
  );
}

agregarElementosCalendario(rutines: any[]) {
  let dayPairs: NodeListOf<Element> = document.querySelectorAll('.day-pair');
  let reversedDayPairs: NodeListOf<Element> = document.querySelectorAll('.day-pair');
  let length = dayPairs.length;
  for (let i = 0; i < length; i++) {
    reversedDayPairs[i] = dayPairs[length - i - 1];
  }
  dayPairs = reversedDayPairs;
  let todayDate: Date = new Date();
  let newDay: number = todayDate.getDate() - (shift * 7);
  todayDate.setDate(newDay);
  //dayPairs = Array.from(dayPairs).reverse();

  let j: number = 0;
  for (let i: number = todayDate.getDay(); i > todayDate.getDay() - 7; i--) {
    let numberDay: number = (i + 7) % 7;
    let containerDay: Element = dayPairs[j];

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
    rutineDate.setDate(rutineDate.getDate() + 1); // due to the format

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
      let containerDay: Element | null = dayPairs[daysDiff];
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
  shift += 1;
  this.loadRutines();
}

next() {
  shift -= 1;
  this.loadRutines();
}
*/
