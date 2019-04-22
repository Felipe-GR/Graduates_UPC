import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-userbasicinfo',
  templateUrl: './userbasicinfo.component.html',
  styleUrls: ['./userbasicinfo.component.css']
})
export class UserbasicinfoComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  render = false;

  @Input() userIDInfo: any;
  userName: string;
  userSurname: string;
  userImage: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.getUserIdentifiers();
  }

  getUserIdentifiers() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdInfo: this.userIDInfo };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserbasicinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.userName = obj.name;
      this.userSurname = obj.surname;
      this.userImage = 'data:image/jpeg;base64,' + obj.image;
      this.render = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

}
