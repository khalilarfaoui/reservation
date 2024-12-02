import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatTable } from '@angular/material/table';
import { MatCard, MatCardContent, MatCardTitle } from '@angular/material/card';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { NgForOf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-menu',
  templateUrl: './add-menu.component.html',
  standalone: true,
  imports: [
    MatTable,
    ReactiveFormsModule,
    MatCardTitle,
    MatCard,
    MatLabel,
    MatCardContent,
    MatFormField,
    MatInput,
    MatButton,
    NgForOf,
  ],
  styleUrls: ['./add-menu.component.css'],
})
export class AddMenuComponent implements OnInit {
  // Form to input the name of the new column (e.g., sandwich, dessert)
  newColumnForm: FormGroup;

  // Holds the table data dynamically
  menuColumns: string[] = ['entree', 'mainCourse', 'garnish', 'dessert']; // Existing columns
  tableData: any[] = []; // Data for each column

  constructor(private fb: FormBuilder, private http: HttpClient , private router : Router) {
    this.newColumnForm = this.fb.group({
      columnName: [''], // Form field for column name
    });
  }

  ngOnInit(): void {
    // Load the initial menu data from backend (optional, if needed)
    this.loadCategorie()
  }


  currentSousMenus:any
  loadCategorie(){

      this.http.get('http://localhost:8080/api/categories').subscribe(res=>{
        console.log(res);
        this.currentSousMenus = res
      })

  }

  // Add a new column
  Ajouter() {
    console.log(this.newColumnForm.value.columnName);

    const obj = {
      name : this.newColumnForm.value.columnName,
      valeur : [
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
        '__',
      ]
    }

    this.http.post('http://localhost:8080/api/categories', obj).subscribe(
      (response) => {
        console.log('Column added successfully:', response);
        this.router.navigateByUrl('dashboard')
      },
      (error) => {
        console.error('Error adding column', error);
      }
    );






    //   if (this.newColumnForm.valid) {
    //     const newColumn = this.newColumnForm.value.columnName;
    //     if (newColumn && !this.menuColumns.includes(newColumn)) {
    //       this.menuColumns.push(newColumn);
    //       this.tableData.forEach(row => row[newColumn] = '');
    //       this.tableData.push({ [newColumn]: '' });

    //       this.http.post('http://localhost:8080/api/menus/add-row', { columnName: newColumn })
    //         .subscribe(response => {
    //           console.log('Column added successfully:', response);
    //         }, error => {
    //           console.error('Error adding column', error);
    //         });

    //       this.newColumnForm.reset();
    //     }
    //   } else {
    //     alert('Please enter a column name');
    //   }
  }


  supprimer(id: number) {
    this.http.delete(`http://localhost:8080/api/categories/${id}`).subscribe(() => {
      this.loadCategorie(); // Reload categories after deletion
    });
  }

  modifier(element: any) {
    const updatedName = prompt('Modifier le nom de la colonne', element.name);
    if (updatedName !== null && updatedName !== element.name) {
      const updatedColumn = { ...element, name: updatedName };
      this.http.put(`http://localhost:8080/api/categories/${element.id}`, updatedColumn).subscribe(() => {
        this.loadCategorie(); // Reload categories after update
      });
    }
  }
}
