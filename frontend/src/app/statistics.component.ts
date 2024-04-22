import { StatisticsService } from './../../services/statistics.service';
import { Component, OnInit } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexPlotOptions,
  ApexYAxis,
  ApexTitleSubtitle,
  ApexXAxis,
  ApexFill,
  ApexNonAxisChartSeries
} from "ng-apexcharts";
import { LoginService } from './../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';
import { Router } from '@angular/router';
export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  title: ApexTitleSubtitle;
};
@Component({
  selector: 'statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['../assets/css/bootstrap.css', '../assets/css/progress.css']
})
export class StatisticsComponent {
   chartOptions: ChartOptions;
  admin: boolean;
 person:Person;
 roles: String[];

  ngOnInit(): void {
    this.loadCharts();
    this.personService.getPerson().subscribe(
      response => {
          this.person= response as Person;
          this.roles=this.person.roles;
          if (this.roles.includes('ADMIN')) {
            this.admin = true;
          }else{
            this.admin=false;
          }

      },
      error => {
        this.router.navigate(['../login']);
        this.person = {alias:"",name:"",date:"",weight:0, roles:[]};

      }
    )
  }
  constructor(private statisticsService: StatisticsService,public loginservice:LoginService,public personService: PersonService,public router: Router) {

  }

  loadCharts() {
    this.statisticsService.getCharts().subscribe(
      response => {
        let data = response
        const data2 = Object.values(data).map(value => parseFloat(value.toString().trim()));
        const data3 = Object.keys(data);
        this.chartOptions = {
          series: [
            {
              name: "Numero de ejercicios",
              data: data2
            }
          ],
          chart: {
            height: 600,
            width: 800,
            type: "bar",
            background: '#ffffff'
          },
          plotOptions: {
            bar: {
              dataLabels: {
                position: "top" // top, center, bottom
              }
            }
          },
          dataLabels: {
            enabled: true,
            formatter: function (val) {
              return val + "";
            },
            offsetY: -20,
            style: {
              fontSize: "12px",
              colors: ["#304758"]
            }
          },

          xaxis: {
            categories: data3,
            position: "bottom",
            labels: {
              offsetY: 0
            },
            axisBorder: {
              show: false
            },
            axisTicks: {
              show: false
            },
            crosshairs: {


            },
            tooltip: {
              enabled: true,
              offsetY: -35
            }
          },
          fill: {
            colors: ["#f1db25"]
          },
          yaxis: {
            axisBorder: {
              show: false
            },
            axisTicks: {
              show: false
            },
            labels: {
              show: false,
              formatter: function (val) {
                return val + "";
              }
            }
          },
          title: {
            text: "Ejercicios / Grupo muscular",
            offsetY: 0,
            align: "center",
            style: {
              color: "black"
            }
          }
        };
      },
      error => {
        console.error('Error obteniendo novedades:', error);
      });
  }


} //End class
