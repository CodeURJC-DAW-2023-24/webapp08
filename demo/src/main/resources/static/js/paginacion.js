
let loadMore = 0;
const NUM_RESULTS = 10;
async function initElementos(){
  loadMore = 0
  let flag = false
  const response = await fetch(`/novedades-iniciales?iteracion=${loadMore}`); 
  let data = await response.json();
  let novedades = data[0];
  const NUM_TOTAL = data[1];
  if(novedades.length < NUM_RESULTS || novedades.length == NUM_TOTAL) flag = true;

  agregarElementosAlContenidoPrincipal(novedades);
  let masNovedades = document.getElementById("contenedor-CargarMas");
  if(!flag) {
    masNovedades.style.display = "flex";
  }
  else{
    masNovedades.style.display = "none";
  }
}

async function cargarMas(){

  let flag = false
  loadMore = loadMore+1

  let spinnerContainer = document.getElementById("spinner-container");
  let masNovedades = document.getElementById("contenedor-CargarMas");
  spinnerContainer.style.display = "flex";
  masNovedades.style.display ="none";
  const response = await fetch(`/novedades-iniciales?iteracion=${loadMore}`); 
  let data = await response.json() 
  let novedades = data[0];
  const NUM_TOTAL = data[1];

  
  if(NUM_RESULTS > novedades.length) {
    flag = true
  }
  let tope = novedades.length + (loadMore + 1)*NUM_RESULTS;
  if(tope==NUM_TOTAL) flag = true


  agregarElementosAlContenidoPrincipal(novedades)
  spinnerContainer.style.display = "none"
 // setTimeout(() => {spinnerContainer.style.display = "none"}, 2000); // Para el dia de prueba
  

  if(!flag) {
    masNovedades.style.display = "flex";
  }
  

}



  
  function agregarElementosAlContenidoPrincipal(novedades) {
   
      let containerNovedades = document.getElementById("container-novedades");
      
  
        for (let i = 0; i<(novedades.length); i+=2){
          const row = document.createElement('div');
          row.classList.add('row', 'mb-3');
      
          // Crear dos columnas dentro de cada fila
          for (let j = 0 ; j<2; j++) {
            if (i+j < novedades.length) {
              const col = document.createElement('div');
              col.classList.add('col-md-6');
      
              // Crear la tarjeta de novedad dentro de cada columna
              const card = document.createElement('div');
              card.classList.add('card', 'mb-3');
      
              // Crear el cuerpo de la tarjeta
              const cardBody = document.createElement('div');
              cardBody.classList.add('card-body');
      
              // Añadir título y descripción a la tarjeta
              const cardTitle = document.createElement('h5');
              cardTitle.classList.add('card-title');
              cardTitle.textContent = `${novedades[i+j].name}`;
      
              const cardText = document.createElement('p');
              cardText.classList.add('card-text');
              cardText.textContent = `${novedades[i+j].name}`;
      
              // Agregar título y descripción al cuerpo de la tarjeta
              cardBody.appendChild(cardTitle);
              cardBody.appendChild(cardText);
      
              // Agregar el cuerpo de la tarjeta a la tarjeta
              card.appendChild(cardBody);
      
              // Agregar la tarjeta a la columna
              col.appendChild(card);
      
              // Agregar la columna a la fila
              row.appendChild(col);
            }
            }
          // Agregar la fila al contenedor de novedades
          containerNovedades.appendChild(row);

        }

  }


  initElementos();







