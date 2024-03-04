let shift = 0;

async function loadRutines() {
    const RESPONSE = await fetch("/loadRutines");
    let rutines = await RESPONSE.json();
    agregarElementosCalendario(rutines);

}

async function agregarElementosCalendario(rutines) {

    let dayPairs = document.querySelectorAll('.day-pair');

    let todayDate = new Date();
    let newDay = todayDate.getDate() - (shift * 7);
    todayDate.setDate(newDay);

    dayPairs = Array.from(dayPairs).reverse();

    let j = 0
    for (let i = todayDate.getDay(); i > todayDate.getDay() - 7; i--) {
        let numberDay = (i + 7) % 7
        let containerDay = dayPairs[j]

        let dayName = containerDay.querySelector('.day-name');
        dayName.textContent = obtainDayName(numberDay);

        let dayNumber = containerDay.querySelector('.day-number');
        let dateAux = new Date(todayDate);
        newDay = dateAux.getDate() - j;
        dateAux.setDate(newDay)
        dayNumber.textContent = dateAux.toLocaleDateString();

        let calendaryContent = containerDay.querySelector('.day-calendary-content');
        calendaryContent.innerHTML = "";
        j += 1
    }
   
    rutines.forEach(rutine => {
        let rutineDate = rutine.date;
        rutineDate = rutineDate.split('T')[0];
        rutineDate = new Date(rutineDate);
        rutineDate.setDate(rutineDate.getDate() + 1); //due to the format

        todayDate.setUTCHours(0, 0, 0, 0);
        rutineDate.setUTCHours(0, 0, 0, 0);

        let startTime = rutineDate.getTime();
        let endTime = todayDate.getTime();

        // milliseconds
        let millisecondsDiff = (endTime - startTime);

        // from miliseconds to days
        const MILLINDAY = 1000 * 60 * 60 * 24;
        let daysDiff = Math.floor(millisecondsDiff / MILLINDAY);

        if ((0 <= daysDiff) && (daysDiff < 7)) {
            let containerDay = dayPairs[daysDiff]
            let calendaryContent = containerDay.querySelector('.day-calendary-content');
            calendaryContent.innerHTML += `<a href="/mainPage/showRutine?id=${rutine.id}" style="text-decoration: none; color: black;">${rutine.name}</a>`;
        }
    });
}

function obtainDayName(numberDay) {
    const WEEKDAYS = ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
    return WEEKDAYS[numberDay];
}

function previous() {
    shift += 1;
    loadRutines();
}

function next() {
    shift -= 1;
    loadRutines();
}

loadRutines();
