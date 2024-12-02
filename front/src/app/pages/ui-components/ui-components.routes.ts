import { Routes } from '@angular/router';

// ui
import { AppBadgeComponent } from './badge/badge.component';
import { AppChipsComponent } from './chips/chips.component';
import { AppListsComponent } from './lists/lists.component';
import { AppMenuComponent } from './menu/menu.component';
import { AppTooltipsComponent } from './tooltips/tooltips.component';
import { AppFormsComponent } from './forms/forms.component';
import { AppTablesComponent } from './tables/tables.component';
import { AddMenuComponent } from './add-menu/add-menu.component';
import { ReservationComponent } from './reservation/reservation.component';
import { MakeReservationComponent } from './make-reservation/make-reservation.component';

export const UiComponentsRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'badge',
        component: AppBadgeComponent,
      },
      {
        path: 'chips',
        component: AppChipsComponent,
      },
      {
        path: 'lists',
        component: AppListsComponent,
      },
      {
        path: 'menu',
        component: AppMenuComponent,
      },
      {
        path: 'tooltips',
        component: AppTooltipsComponent,
      },
      {
        path: 'forms',
        component: AppFormsComponent,
      },
      {
        path: 'tables',
        component: AppTablesComponent,
      },
      {
        path: 'statistic',
        component: AddMenuComponent,
      },
      {
        path: 'add-menu',
        component: AddMenuComponent,
      },
      {
        path: 'reservations',
        component: ReservationComponent,
      },
      {
        path: 'make-reservation',
        component: MakeReservationComponent,
      }
    ],
  },
];
