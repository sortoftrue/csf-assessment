import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { View1Component } from './components/view1.component';
import { View2Component } from './components/view2.component';
import { View0Component } from './components/view0.component';

const routes: Routes = [
  {path:"", component: View0Component},
  {path:"view1", component: View1Component},
  {path:'view2/:bundleId', component: View2Component},
  {path: '**', redirectTo: '', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
