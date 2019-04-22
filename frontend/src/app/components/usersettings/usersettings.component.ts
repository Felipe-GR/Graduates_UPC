import { Component, OnInit, Input } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-usersettings',
  templateUrl: './usersettings.component.html',
  styleUrls: ['./usersettings.component.css']
})
export class UsersettingsComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  phoneNumber: string;
  isPhonePublic: boolean;
  city: string;
  isCityPublic;
  profession: string;
  isProfessionPublic;
  company: string;
  isCompanyPublic;
  education: string;
  isEducationPublic;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.getUserInfo();
  }

  getUserInfo() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdInfo: this.userId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.phoneNumber = obj.phoneNumber;
      this.isPhonePublic = !!obj.isPhonePublic;
      this.city = obj.city;
      this.isCityPublic = !!obj.isCityPublic;
      this.profession = obj.profession;
      this.isProfessionPublic = !!obj.isProfessionPublic;
      this.company = obj.company;
      this.isCompanyPublic = !!obj.isCompanyPublic;
      this.education = obj.education;
      this.isEducationPublic = !!obj.isEducationPublic;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  updateUserInfo() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoUpdate = {
      phoneNumber: this.phoneNumber, city: this.city, profession: this.profession, company: this.company, education: this.education,
      isPhonePublic: +this.isPhonePublic, isCityPublic: +this.isCityPublic, isProfessionPublic: +this.isProfessionPublic,
      isCompanyPublic: +this.isCompanyPublic, isEducationPublic: +this.isEducationPublic
    };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/setuserinfo', {
      userIdentifiers,
      userInfoUpdate
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.router.navigate(['/home']);
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
