import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ClientListComponent } from './components/client-list/client-list.component';
import { ClientFormComponent } from './components/client-form/client-form.component';
import {ServiceListComponent} from "./components/service-list/service-list.component";
import {Service} from "./model/entities/service";
import {ServiceFormComponent} from "./components/service-form/service-form.component";
import {ConsumablesListComponent} from "./components/storage-list/consumables-list.component";
import {ConsumablesFormComponent} from "./components/storage-form/consumables-form.component";
import {StaffListComponent} from "./components/staff-list/staff-list.component";
import {StaffFormComponent} from "./components/staff-form/staff-form.component";
import {ApptListComponent} from "./components/appt-list/appt-list.component";
import {ApptFormComponent} from "./components/appt-form/appt-form.component";
import {ApptRegistrationFormComponent} from "./components/appt-refistration-form/appt-registration-form.component";

const routes: Routes = [
  { path: 'clients', component: ClientListComponent },
  { path: 'services', component: ServiceListComponent },
  { path: 'storage', component: ConsumablesListComponent },
  { path: 'staff', component: StaffListComponent },
  { path: 'visits', component: ApptListComponent },

  { path: 'clients/create', component: ClientFormComponent },
  { path: 'services/create', component: ServiceFormComponent },
  { path: 'storage/create', component: ConsumablesFormComponent },
  { path: 'staff/create', component: StaffFormComponent },
  { path: 'visits/create', component: ApptFormComponent },

  { path: 'clients/update/:id', component: ClientFormComponent },
  { path: 'services/update/:id', component: ServiceFormComponent },
  { path: 'storage/update/:id', component: ConsumablesFormComponent },
  { path: 'staff/update/:id', component: StaffFormComponent },
  { path: 'visits/update/:id', component: ApptFormComponent },

  { path: 'clients/delete', component: ClientListComponent },
  { path: 'services/delete', component: ServiceListComponent },
  { path: 'storage/delete', component: ConsumablesListComponent },

  { path: 'registration', component: ApptRegistrationFormComponent },
  { path: 'login', component: ApptRegistrationFormComponent }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
