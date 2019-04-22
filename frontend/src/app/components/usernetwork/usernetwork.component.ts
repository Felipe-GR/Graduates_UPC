import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-usernetwork',
  templateUrl: './usernetwork.component.html',
  styleUrls: ['./usernetwork.component.css']
})
export class UsernetworkComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  profileUserID;

  totalUsers = 0;
  users = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.profileUserID = +params['user_id'];
      this.totalUsers = 0;
      this.users = [];
      this.getConnectedUsers();
    });

  }

  getConnectedUsers() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const profileNetwork = { profileUserID: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getconnectedusers', {
      userIdentifiers,
      profileNetwork
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.totalUsers = obj.numberOfResults;
      for (let i = 0; i < this.totalUsers; i++) {
        this.users.push(obj.list[i]);
        this.users[i].image = 'data:image/jpeg;base64,' + this.users[i].image;
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
