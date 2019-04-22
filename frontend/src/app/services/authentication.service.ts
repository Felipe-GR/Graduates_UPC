import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/mapTo';
import 'rxjs/add/operator/catch';

@Injectable()
export class AuthenticationService {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  isAuthenticated(): Observable<boolean> {
    const userToken = localStorage.getItem('userToken');
    const id = localStorage.getItem('userID');
    // If there is a user token
    if (userToken) {
      // Check if the session belongs to online user
      // If not the server will send UNAUTHORIZED
      const API_URL = environment.API_URL;
      return this.http.post(API_URL + '/api/authcheck', {
        userToken: userToken,
        id: id
      }, { responseType: 'text', withCredentials: true }).mapTo(true)
        .catch(err => {
          if (err.error !== '1') {
            this.router.navigate(['/error', true], { skipLocationChange: true });
          }
          return Observable.of(false);
        });
      // Else cannot be exist a valid session
    } else {
      this.router.navigate(['/error', true], { skipLocationChange: true });
      return Observable.of(false);
    }
  }

  lightAuth(): Observable<boolean> {
    const userToken = localStorage.getItem('userToken');
    const id = localStorage.getItem('userID');
    // If there is a user token
    if (userToken) {
      // Check if the session belongs to online user
      // If not the server will send UNAUTHORIZED
      const API_URL = environment.API_URL;
      return this.http.post(API_URL + '/api/authlightcheck', {
        userToken: userToken,
        id: id
      }, { responseType: 'text', withCredentials: true }).mapTo(true)
        .catch(err => {
          this.router.navigate(['/error', true], { skipLocationChange: true });
          return Observable.of(false);
        });
      // Else cannot be exist a valid session
    } else {
      this.router.navigate(['/error', true], { skipLocationChange: true });
      return Observable.of(false);
    }
  }

  isAdmin(): Observable<boolean> {
    const userToken = localStorage.getItem('userToken');
    const id = localStorage.getItem('userID');
    // If there is a user token
    if (userToken) {
      // Check if the session belongs to online user
      // If not the server will send UNAUTHORIZED
      const API_URL = environment.API_URL;
      return this.http.post(API_URL + '/api/admincheck', {
        userToken: userToken,
        id: id
      }, { responseType: 'text', withCredentials: true }).mapTo(true)
        .catch(err => {
          return Observable.of(false);
        });
      // Else cannot be exist a valid session
    } else {
      return Observable.of(false);
    }
  }
}
