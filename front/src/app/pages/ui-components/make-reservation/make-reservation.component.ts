import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { ReservationService } from '../../../services/reservation.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-make-reservation',
  templateUrl: './make-reservation.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass, NgForOf, NgIf],
  styleUrls: ['./make-reservation.component.css'],
})
export class MakeReservationComponent implements OnInit {
  currentMenus: any[] = [];
  reservations: any[] = [];
  selectedOptions: { [date: string]: { [key: string]: string | null } } = {};
  totalMenus: any[] = [];
  days: Date[] = [];
  message: string | null = null;
  success: boolean = false;
  currentSousMenus: any;
  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private reservationService: ReservationService
  ) {}

  ngOnInit(): void {
    this.fetchReservations().then((r) => this.loadMenus());
    this.loadSousMenu();
  }

  ngOnDestroy(): void {}
  async fetchReservations(): Promise<void> {
    const userId = Number(localStorage.getItem('id'));

    try {
      const response = await firstValueFrom(
        this.reservationService.getReservationsByUserId(userId)
      );
      console.log(response);
      this.reservations = response;
    } catch (error) {
      console.error('Error fetching reservations', error);
    }
  }

  loadSousMenu() {
    this.http.get('http://localhost:8080/api/categories').subscribe((res) => {
      console.log(res);
      this.currentSousMenus = res;
    });
  }

  initializeSelectedOptions(): void {
    this.days.forEach((day) => {
      const dateStr = day.toDateString();
      // Initialize each day with default values if it doesn't already exist
      this.selectedOptions[dateStr] = {
        id: null,
        entree: null,
        mainCourse: null,
        garnish: null,
        dessert: null,
        sandwich: null,
        otherMenu: null,
      };
      // Update with reservation data if available
      this.reservations.forEach((reservation) => {
        const reservationDate = new Date(
          reservation.reservationDate
        ).toDateString();
        if (reservationDate === dateStr) {
          this.selectedOptions[dateStr] = {
            id: reservation.id,
            entree: reservation.selectedEntree,
            mainCourse: reservation.selectedMainCourse,
            garnish: reservation.selectedGarnish,
            dessert: reservation.selectedDessert,
            sandwich: reservation.selectedSandwich,
          };
        }
      });
    });
  }

  isSelected(day: Date, type: string, option: string): boolean {
    const dateStr = day.toDateString();
    return this.selectedOptions[dateStr]?.[type] === option;
  }

  verifierDate(date: Date): boolean {
    const aujourdHui = new Date();

    // Réinitialiser l'heure de la date d'aujourd'hui à minuit
    aujourdHui.setHours(0, 0, 0, 0);

    // Réinitialiser l'heure de la date donnée à minuit
    const dateSansHeure = new Date(date);
    dateSansHeure.setHours(0, 0, 0, 0);

    if (dateSansHeure.getTime() === aujourdHui.getTime()) {
      // Vérifier si l'heure dépasse 9h30
      const neufHeuresTrente = new Date();
      neufHeuresTrente.setHours(9, 30, 0, 0);

      if (date.getTime() > neufHeuresTrente.getTime()) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  // Exemple d'utilisation



  selectOption(day: Date, type: string, option: string): void {
    console.log(this.verifierDate(new Date(day)));

    if(!this.verifierDate(new Date(day))){
      alert("La date est aujourd'hui et elle dépasse 9h30.")
      return;
    }

    const dateStr = day.toDateString();

    // Set the selected option for the day
    this.selectedOptions[dateStr][type] = option;

    // Deselect sandwiches if a meal option is selected
    if (type !== 'sandwich') {
      this.selectedOptions[dateStr]['sandwich'] = null; // Deselect sandwich
    }

    // Deselect meal options if a sandwich is selected
    if (type === 'sandwich') {
      Object.keys(this.selectedOptions[dateStr]).forEach((key) => {
        if (key !== 'sandwich' && key !== 'id') {
          this.selectedOptions[dateStr][key] = null; // Deselect all meals
        }
      });
    }

    console.log(this.selectedOptions);
  }

  getSandwichesForDay(day: Date): string[] {
    const dateStr = day.toDateString();

    // Find all menus that match the given date
    const menusForDay = this.currentMenus.filter(
      (menu) => new Date(menu.date).toDateString() === dateStr
    );

    // Collect all sandwiches from those menus
    return menusForDay.flatMap((menu) => menu.sandwiches || []);
  }

  // Load all menus and set up the days for the table
  loadMenus(): void {
    this.http
      .get<any[]>('http://localhost:8080/api/menus/all')
      .subscribe((data) => {
        console.log('Received menu data:', data); // Check backend data
        this.totalMenus = data;
        this.days = this.getNext7Days();
        this.initializeSelectedOptions();
        this.updateMenus(); // Make sure this uses the updated totalMenus
      });
  }

  // Get the next 7 days starting from today
  getNext7Days(): Date[] {
    const next7Days: Date[] = [];
    for (let i = 0; i < 7; i++) {
      const today = new Date();
      today.setDate(today.getDate() + i);
      next7Days.push(today);
    }
    return next7Days;
  }

  // Update the current menus for the next 7 days
  updateMenus(): void {
    const today = new Date();
    const dayOfWeek = today.getDay();
    const dailyMenusCount = 2; // Adjust this count based on your needs
    const startIndex = dayOfWeek * dailyMenusCount;
    console.log('0 ', this.currentMenus);
    // Adjust to ensure you have the correct number of menus
    let currentMenusNoDate = this.totalMenus.slice(
      startIndex,
      startIndex + dailyMenusCount * 7
    );
    console.log('currentMenusNoDate ', currentMenusNoDate);
    for (let i = 0; i < currentMenusNoDate.length; i += 2) {
      // Determine the date index based on the current index
      const dateIndex = Math.floor(i / 2);

      // Add the first menu with the associated date
      this.currentMenus.push({
        date: this.days[dateIndex],
        ...currentMenusNoDate[i],
      });

      this.currentMenus.push({
        date: this.days[dateIndex],
        ...currentMenusNoDate[i + 1],
      });
    }
    this.currentMenus.map((i: any) => {

      i.otherMenu = [];


    });
    console.log('final ', this.currentMenus);

    this.currentSousMenus.map((k: any) => {
          console.log("k.valeur" , k.valeur);
          for (var i = 0; i < this.currentMenus.length ; i++) {
            this.currentMenus[i].otherMenu.push(k.valeur[i])
          }
    });
    console.log(this.currentMenus);

    // this.currentMenus.map((i: any) => {
    //   i.otherMenu = [];
    //   this.currentSousMenus.map((k: any) => {
    //     console.log(k.valeur);
    //   });
    // });

    this.cdr.detectChanges(); // Manually trigger change detection
  }

  // Capture new values in editable fields
  updateValue(menu: any, field: string, event: any): void {
    const updatedValue = event.target.innerText.trim();
    console.log(`Updating ${field} to: ${updatedValue}`); // Debugging line

    if (field === 'sandwiches') {
      menu[field] = updatedValue
        ? updatedValue.split(',').map((item: string) => item.trim())
        : [];
    } else {
      menu[field] = updatedValue;
    }
  }

  async saveUpdates(): Promise<void> {
    const selectedReservations = this.days.map((day) => {
      const dateStr = day.toDateString();
      return {
        date: dateStr,
        ...this.selectedOptions[dateStr],
      };
    });

    console.log('Sending selected reservations:', selectedReservations);
    const userId = Number(localStorage.getItem('id'));
    // Call your API to save the selected reservations
    this.http
      .post(
        `http://localhost:8080/api/reservations/save/${userId}`,
        selectedReservations
      )
      .subscribe({
        next: async () => {
          await this.fetchReservations();
          this.initializeSelectedOptions();
          this.message = 'Reservations updated successfully!';
          this.success = true;
        },
        error: async (err) => {
          console.error('Error saving reservations:', err);
          await this.fetchReservations();
          this.initializeSelectedOptions();
          this.message = 'Error saving reservations!';
          this.success = false;
        },
      });
  }

  // Display day name based on date
  getDay(date: Date): string {
    const options: Intl.DateTimeFormatOptions = {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    };
    return date.toLocaleDateString('fr-FR', options); // Adjust the locale as needed
  }
}
