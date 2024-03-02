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
      // Table date: values and grafic labels
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
      // Draw the chart
      new google.visualization.ColumnChart( 
      //ColumnChart its the tipe of chart
        document.getElementById('GraficoGoogleChart-ejemplo-1')
      ).draw(data, options);
    }

}

crearGraficas();