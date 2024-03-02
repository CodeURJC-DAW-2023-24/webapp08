let desplazamiento = 0;

async function cargarRutinas() {
    const response = await fetch("/cargarRutinas");
    let rutinas = await response.json();
    agregarElementosCalendario(rutinas);

}

async function agregarElementosCalendario(rutinas) {

    let dayPairs = document.querySelectorAll('.day-pair');

    let fechaHoy = new Date();
    let nuevoDia = fechaHoy.getDate() - (desplazamiento * 7);
    fechaHoy.setDate(nuevoDia);

    dayPairs = Array.from(dayPairs).reverse();

    let j = 0
    for (let i = fechaHoy.getDay(); i > fechaHoy.getDay() - 7; i--) {
        let numeroDia = (i + 7) % 7
        let diaContainer = dayPairs[j]

        let dayName = diaContainer.querySelector('.day-name');
        dayName.textContent = obtenerNombreDia(numeroDia);

        let dayNumber = diaContainer.querySelector('.day-number');
        let fechaAux = new Date(fechaHoy);
        nuevoDia = fechaAux.getDate() - j;
        fechaAux.setDate(nuevoDia)
        dayNumber.textContent = fechaAux.toLocaleDateString();

        let calendaryContent = diaContainer.querySelector('.day-calendary-content');
        calendaryContent.innerHTML = "";
        j += 1
    }
   
    rutinas.forEach(rutina => {
        let fechaRutina = rutina.date;
        fechaRutina = fechaRutina.split('T')[0];
        fechaRutina = new Date(fechaRutina);
        fechaRutina.setDate(fechaRutina.getDate() + 1); //due to the format

        fechaHoy.setUTCHours(0, 0, 0, 0);
        fechaRutina.setUTCHours(0, 0, 0, 0);

        let tiempoInicio = fechaRutina.getTime();
        let tiempoFin = fechaHoy.getTime();

        // milliseconds
        let diferenciaMilisegundos = (tiempoFin - tiempoInicio);

        // from miliseconds to days
        const milisegundosEnUnDia = 1000 * 60 * 60 * 24;
        let diferenciaDias = Math.floor(diferenciaMilisegundos / milisegundosEnUnDia);

        if ((0 <= diferenciaDias) && (diferenciaDias < 7)) {
            let diaContainer = dayPairs[diferenciaDias]
            let calendaryContent = diaContainer.querySelector('.day-calendary-content');
            calendaryContent.innerHTML += `<a href="/verRutina?id=${rutina.id}" style="text-decoration: none; color: black;">${rutina.name}</a>`;
        }
    });
}

function obtenerNombreDia(numeroDia) {
    const diasSemana = ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
    return diasSemana[numeroDia];
}

function anterior() {
    desplazamiento += 1;
    cargarRutinas();
}

function siguiente() {
    desplazamiento -= 1;
    cargarRutinas();
}

cargarRutinas();
