import { Routes } from '@angular/router';
import { UnauthGuard } from "./guards/unauth.guard";
import { AuthGuard } from "./guards/auth.guard";
import { UserComponent } from "./components/user/user";
// import { NotFoundComponent } from "./pages/not-found/not-found.component";
import { HomeComponent } from "./pages/home/home";
import { LoginComponent } from "./pages/login/login";
import { RegisterComponent } from "./pages/register/register";
import { TopicListComponent } from "./pages/topic-list/topic-list";
import { PostsComponent } from "./pages/posts/posts";
import { PostsCreateComponent } from "./pages/posts/posts-create";

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
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
  {
    path: 'topics',
    canActivate: [AuthGuard],
    component: TopicListComponent
  },
  {
    path: 'posts',
    canActivate: [AuthGuard],
    component: PostsComponent
  },
  {
    path: 'posts/create',
    canActivate: [AuthGuard],
    component: PostsCreateComponent
  },
  // { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' },
];


