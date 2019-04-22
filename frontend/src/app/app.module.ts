import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { FooterComponent } from './components/footer/footer.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './guard/auth.guard';
import { AdminGuard } from './guard/admin.guard';
import { LightGuard } from './guard/light.guard';
import { NavbarComponent } from './components/navbar/navbar.component';
import { UserprofileComponent } from './components/userprofile/userprofile.component';
import { PostComponent } from './components/post/post.component';
import { NewpostComponent } from './components/newpost/newpost.component';
import { SearchComponent } from './components/search/search.component';
import { ShowpostComponent } from './components/showpost/showpost.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { UsernetworkComponent } from './components/usernetwork/usernetwork.component';
import { UsersettingsComponent } from './components/usersettings/usersettings.component';
import { ErrorpageComponent } from './components/errorpage/errorpage.component';
import { UserbasicinfoComponent } from './components/userbasicinfo/userbasicinfo.component';
import { AdminpageComponent } from './components/adminpage/adminpage.component';
import { AdminnavbarComponent } from './components/adminnavbar/adminnavbar.component';
import { ChatComponent } from './components/chat/chat.component';
import { ChathistoryComponent } from './components/chathistory/chathistory.component';
import { ChatwithuserComponent } from './components/chatwithuser/chatwithuser.component';
import { AdvertspageComponent } from './components/advertspage/advertspage.component';
import { FriendspostsComponent } from './components/friendsposts/friendsposts.component';
import { RecommendedpostsComponent } from './components/recommendedposts/recommendedposts.component';

library.add(fas, far);

const appRoutes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'error/:logout', component: ErrorpageComponent },
  // For Logged in users ONLY
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'adverts', component: AdvertspageComponent, canActivate: [AuthGuard] },
  {
    path: 'users/:user_id',
    children: [
      { path: '', component: UserprofileComponent, canActivate: [LightGuard] },
      { path: 'network', component: UsernetworkComponent, canActivate: [AuthGuard] }
    ]
  },
  { path: 'posts/:post_id', component: ShowpostComponent, canActivate: [AuthGuard] },
  { path: 'settings', component: UsersettingsComponent, canActivate: [AuthGuard] },
  {
    path: 'chat',
    children: [
      { path: '', component: ChatComponent, canActivate: [AuthGuard] },
      { path: ':chat_id', component: ChatComponent, canActivate: [AuthGuard] }
    ]
  },

  // Administrator pages
  { path: 'admin', component: AdminpageComponent, canActivate: [AdminGuard] },
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FooterComponent,
    RegisterComponent,
    HomeComponent,
    NavbarComponent,
    UserprofileComponent,
    PostComponent,
    NewpostComponent,
    SearchComponent,
    ShowpostComponent,
    NotificationsComponent,
    UsernetworkComponent,
    UsersettingsComponent,
    ErrorpageComponent,
    UserbasicinfoComponent,
    AdminpageComponent,
    AdminnavbarComponent,
    ChatComponent,
    ChathistoryComponent,
    ChatwithuserComponent,
    AdvertspageComponent,
    FriendspostsComponent,
    RecommendedpostsComponent,
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    FontAwesomeModule,
    RouterModule.forRoot(appRoutes, { useHash: true })
  ],
  providers: [AuthGuard, AdminGuard, LightGuard, { provide: LocationStrategy, useClass: HashLocationStrategy }],
  bootstrap: [AppComponent]
})

export class AppModule {
}
