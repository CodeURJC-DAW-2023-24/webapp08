const NUM_RESULTS = 10
let loadMore = 0;

async function initElementos(){
  loadMore = 0
  let flag = false
  const response = await fetch(`/novedades-iniciales?iteracion=${loadMore}`); //Pedir al servidor la lista de equipos basandonos en la búsqueda
  let data = await response.json()
  console.log(data);//Creo q es devuelvo unalist<novedad>
  if(data.length < NUM_RESULTS) flag = true //si no hay mas elementos que mostrar

  var contenedor = document.getElementById("container-novedades");
  //contenedor.innerHTML = "";
  agregarElementosAlContenidoPrincipal(data);
  let masNovedades = document.getElementById("contenedorCargarMas");
  if(!flag) {
    masNovedades.style.display = "block";
  }
  else{
    masNovedades.style.display = "none";
  }
}

async function cargarMas(){
  let flag = false
  loadMore = loadMore+1
  const response = await fetch(`/novedades-iniciales?iteracion=${loadMore}`); //Pedir al servidor la lista de equipos basandonos en la búsqueda
  let data = await response.json() //Ns si es necesario


  let tope = (loadMore+1)*NUM_RESULTS
  if(tope>data.length) {
    tope = data.length
    flag = true
  }
  if(tope==data.length) flag = true


  agregarElementosAlContenidoPrincipal(data)

  let masNovedades = document.getElementById("contenedor-CargarMas");
  if(!flag) {
    masNovedades.style.display = "block";
  }
  else{
    masNovedades.style.display = "none";
  }
}



  // Función asincrónica para realizar la solicitud Fetch y actualizar la página
 
  

  function agregarElementosAlContenidoPrincipal(novedades) {
   
      let containerNovedades = document.getElementById("container-novedades");
      
  
        for (let i = 0; i<novedades.length; i++){
          const row = document.createElement('div');
          row.classList.add('row', 'mb-3');
      
          // Crear dos columnas dentro de cada fila
          
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
              cardTitle.textContent = `${novedades[i].name}`;
      
              const cardText = document.createElement('p');
              cardText.classList.add('card-text');
              console.log(novedades[i].name);
              cardText.textContent = `${novedades[i].name}`;
      
              // Agregar título y descripción al cuerpo de la tarjeta
              cardBody.appendChild(cardTitle);
              cardBody.appendChild(cardText);
      
              // Agregar el cuerpo de la tarjeta a la tarjeta
              card.appendChild(cardBody);
      
              // Agregar la tarjeta a la columna
              col.appendChild(card);
      
              // Agregar la columna a la fila
              row.appendChild(col);
          
      
          // Agregar la fila al contenedor de novedades
          containerNovedades.appendChild(row);
        }
   

  }

  initElementos();







