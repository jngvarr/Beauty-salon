import {Component, OnInit} from '@angular/core';
import {Employee} from "../../model/entities/employee";
import {StaffService} from "../../services/staff.service";
import {Router} from "@angular/router";
import {Client} from "../../model/entities/client";

@Component({
  selector: 'app-staff-list',
  templateUrl: './staff-list.component.html',
  styleUrl: './staff-list.component.scss'
})
export class StaffListComponent implements OnInit{

  employees: Employee[] | undefined;
  employee: Employee | undefined;
  isSearching: boolean = false;


  constructor(private staffService: StaffService, private router: Router) {
  }

  ngOnInit() {
    this.loadEmployees();
  }

  loadEmployees() {
    this.staffService.findAll().subscribe(data => {
      this.employees = data;
    });
  }

  searchByName(name: string, lastName: string) {
    this.staffService.findByName(name, lastName).subscribe((data: Employee[]) => {
      this.employees = data;
      this.isSearching = true;
    });
  }

  searchByFunction(value: string) {
    this.staffService.findByFunction(value).subscribe((data: Employee[]) => {
      this.employees = data;
      this.isSearching = true;
    });
  }

  deleteEmployee(employee: Employee) {
    if (confirm('Вы уверены, что хотите удалить клиента?')) {
      this.staffService.delete(employee.id).subscribe(() => {
        this.loadEmployees();
      });
    }
  }

  resetSearch() {
    this.isSearching = false;
    this.loadEmployees();
  }

  updateEmployee(employee: Employee) {
    this.router.navigate(['/staff/update', employee.id]);
  }

  searchByPhone(value: string) {
    this.isSearching = true;
    this.staffService.findByPhone(value).subscribe((data: Client[]) => {
      this.employees = data;
    });
  }
}
