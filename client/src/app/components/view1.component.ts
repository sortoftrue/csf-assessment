import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, firstValueFrom, throwError } from 'rxjs';

@Component({
  selector: 'app-view1',
  templateUrl: './view1.component.html',
  styleUrls: ['./view1.component.css']
})
export class View1Component implements OnInit {

  // private origin:string = "http://localhost:8080"
  private origin:string = ""

  @ViewChild('file') zipFile!: ElementRef;
  form!: FormGroup;

  constructor(private http: HttpClient, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      'zipFile': this.fb.control('', [Validators.required]),
      'comments': this.fb.control('Comments here'),
      'name': this.fb.control('Name here', [Validators.required]),
      'title': this.fb.control('Give a title', [Validators.required])
    })
  }

  back() {
    this.router.navigate([''])
  }

  public submit() {
    const formData = new FormData();

    formData.set('comments', this.form.get('comments')?.value)
    formData.set('name', this.form.get('name')?.value)
    formData.set('title', this.form.get('title')?.value)
    formData.set('file', this.zipFile.nativeElement.files[0])

    this.http.post(this.origin +'/upload', formData)
      .pipe(
        catchError((error) => {
          console.error(error);
          alert(error.error.error); // logs the payload of the error response
          return throwError('Something went wrong');
        })
      )
      .subscribe((response: any) => {
        console.log(response);
        this.router.navigate(['/view2', response.bundleId]);
      });

    //   firstValueFrom(
    //     this.http.post('http://localhost:8080/upload',formData)
    //   ).then(
    //     (response)=>{
    //       // console.info(response)

    //       //@ts-ignore
    //       if(response.error){
    //         console.info("triggering error")
    //         //@ts-ignore
    //         alert(response.error)
    //       } else{
    //         //@ts-ignore
    //         this.router.navigate(['/view2', response.bundleId]);
    //       }
    //     }
    //   )
    // }

    }
  }
