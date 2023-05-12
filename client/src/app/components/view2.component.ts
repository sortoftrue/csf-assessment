import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, firstValueFrom, throwError } from 'rxjs';
import { Bundle } from '../models/Bundle';

@Component({
  selector: 'app-view2',
  templateUrl: './view2.component.html',
  styleUrls: ['./view2.component.css']
})
export class View2Component {

  //private origin: string = "http://localhost:8080"
  private origin:string = ""

  bundleId!: String;
  returnedBundle!: Bundle;

  constructor(private activatedRoute: ActivatedRoute,
    private router: Router, private httpClient: HttpClient) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      async (params) => {
        this.bundleId = params['bundleId'];

        this.httpClient.get(this.origin + '/bundle' + '/' + this.bundleId)
          .pipe(
            catchError((error) => {
              console.error(error);
              alert(error.error.error); // logs the payload of the error response
              return throwError('Something went wrong');
            })
          )
          .subscribe((response: any) => {
            console.log(response);
            this.returnedBundle = response;

            const substring = this.returnedBundle.date.substring(0, 10)
            const [day, month, year, end] = substring.split('-');
            // console.log(day)
            // console.log(month)
            // console.log(year)
            const date = new Date(+year, +month - 1, +day);
            console.log(date)
            this.returnedBundle.date = date

          });

        // this.returnedBundle = await this.getBundle(this.bundleId);

      }
    );
  }

  getBundle(bundleId: String) {

    const promise$: Promise<any> = firstValueFrom(this.httpClient
      .get<Bundle>(origin + '/bundle' + '/' + bundleId))

    return promise$;

  }

  back() {
    this.router.navigate(['/view1'])
  }

  backHome() {
    this.router.navigate([''])
  }

}
