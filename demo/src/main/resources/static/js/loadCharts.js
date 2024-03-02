async function loadCharts(){
   const response = await fetch ("/cargarGraficas");
   let data = await response.json()
   var dataArray = [];
    for (var key in data) {
    dataArray.push([key, data[key]]);
}
    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(drawChart);
    function drawChart() {
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
        document.getElementById('GoogleChart-1')
      ).draw(data, options);
    }

}

loadCharts();