import { Routes } from '@angular/router';
import { UnauthGuard } from "./guards/unauth.guard";
import { AuthGuard } from "./guards/auth.guard";
import { UserComponent } from "./components/user/user";
// import { NotFoundComponent } from "./pages/not-found/not-found.component";
import { HomeComponent } from "./pages/home/home";
import { LoginComponent } from "./pages/login/login";
import { RegisterComponent} from "./pages/register/register";

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
  {
    path: 'home',
    canActivate: [UnauthGuard],
    component: HomeComponent
  },
  {
    path: 'register',
    canActivate: [UnauthGuard],
    component: RegisterComponent
  },
  {
    path: 'login',
    canActivate: [UnauthGuard],
    component: LoginComponent
  },
  {
    path: 'user',
    canActivate: [AuthGuard],
    component: UserComponent
  },
  // { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' },
];


