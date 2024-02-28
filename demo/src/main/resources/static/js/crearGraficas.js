async function crearGraficas(){
   const response = await fetch ("/cargarGraficas");
   let datos = await response.json()
   var dataArray = [];
    for (var key in datos) {
    dataArray.push([key, datos[key]]);
}
    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(dibujarGrafico);
    function dibujarGrafico() {
      // Tabla de datos: valores y etiquetas de la gráfica
      var data = google.visualization.arrayToDataTable([
        ['Texto', 'Numero de ejercicios'],
        ...dataArray
         
      ]);
      var options = {
        title: 'Ejercicios / grupo muscular',
        colors: ['#f1db25'],
        backgroundColor: '#f2f2f2',
        vAxis: {
            format: '0'
        }
      }
      // Dibujar el gráfico
      new google.visualization.ColumnChart( 
      //ColumnChart sería el tipo de gráfico a dibujar
        document.getElementById('GraficoGoogleChart-ejemplo-1')
      ).draw(data, options);
    }

}

crearGraficas();