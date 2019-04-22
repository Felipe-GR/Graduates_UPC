import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class LightGuard {

  authService: AuthenticationService;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {
    this.authService = new AuthenticationService(route, router, http);
  }

  canActivate(): Observable<boolean> {
    return this.authService.lightAuth();
  }

}
