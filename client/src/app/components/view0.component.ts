import { Component } from '@angular/core';
import { Bundle, BundleSummary } from '../models/Bundle';
import { ActivatedRoute, Router } from '@angular/router';
import { async, firstValueFrom } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-view0',
  templateUrl: './view0.component.html',
  styleUrls: ['./view0.component.css']
})
export class View0Component {

  //private origin:string = "http://localhost:8080"
  private origin:string = ""

  returnedSummaries!: BundleSummary[];

  constructor(private activatedRoute: ActivatedRoute,
    private router: Router, private httpClient: HttpClient) { }

    ngOnInit():void{
      this.getSummaries().then(
        (result) => {
          //console.info(result)
          this.returnedSummaries = result

          this.returnedSummaries.map(
            (summary)=>{
              console.info(summary.date)
              const substring = summary.date.substring(0,10)
              const[day,month,year,end] = substring.split('-');
              // console.log(day)
              // console.log(month)
              // console.log(year)
              const date = new Date(+year, +month -1 , +day);
              console.log(date)
              summary.date = date
            }
          )
        }
      );
    }

    getSummaries(){

      const promise$: Promise<any> = firstValueFrom(this.httpClient
        .get<BundleSummary[]>(this.origin + '/bundles'))
  
      return promise$;
  
    }

    goUpload(){
      this.router.navigate(['/view1'])
    }

}
