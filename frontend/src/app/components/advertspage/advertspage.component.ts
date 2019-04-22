import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-advertspage',
  templateUrl: './advertspage.component.html',
  styleUrls: ['./advertspage.component.css']
})
export class AdvertspageComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  adverts = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) {
  }

  ngOnInit() {
    this.getAds();
  }

  getAds() {
    const userIdentifiers = {userToken: this.userToken, id: this.userId};
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/adverts', {
      userIdentifiers
    }, {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.adverts = obj;

      },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], {skipLocationChange: true});
      });
  }

}
