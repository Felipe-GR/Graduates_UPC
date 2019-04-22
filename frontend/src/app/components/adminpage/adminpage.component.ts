import { Component, OnInit } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import { saveAs } from 'file-saver/FileSaver';


@Component({
  selector: 'app-adminpage',
  templateUrl: './adminpage.component.html',
  styleUrls: ['./adminpage.component.css']
})
export class AdminpageComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  users = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.listAllUsers();
  }

  listAllUsers() {
    const adminIdentifiers = { userToken: this.userToken, id: this.userId };
    // const profileNetwork = { profileUserID: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/adminlistusers', {
      adminIdentifiers,
      // profileNetwork
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        const obj = JSON.parse(data);
        this.users = obj;
        for (let i = 0; i < this.users.length; i++) {
          this.users[i].image = 'data:image/jpeg;base64,' + this.users[i].image;
        }

      },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  exportUser(id) {
    const userList = [];
    userList.push(id);
    console.log(userList);

    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userListRequest = { usersToExport: userList };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/exportusers', {
      userIdentifiers,
      userListRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.saveToFileSystem(data);
      },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }


  private saveToFileSystem(response) {
    const data = new Blob([response], { type: 'text/plain;charset=utf-8' });
    saveAs(data, 'users.xml');
  }

}
