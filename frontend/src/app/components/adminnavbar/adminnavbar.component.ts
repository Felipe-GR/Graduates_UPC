import {Component, HostListener, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {saveAs} from 'file-saver/FileSaver';

// import { sendArray } from '../adminpage/adminpage.component';

@Component({
  selector: 'app-adminnavbar',
  templateUrl: './adminnavbar.component.html',
  styleUrls: ['./adminnavbar.component.css']
})
export class AdminnavbarComponent implements OnInit {

  top = true;

  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');
  userID = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
  ) {
  }


  ngOnInit() {
  }

  @HostListener('window:scroll')
  public onScroll() {
    if (window.scrollY === 0) {
      this.top = true;
    } else {
      this.top = false;
    }
  }

  scrollToTop() {
    window.scrollTo({top: 0, behavior: 'smooth'});
  }

  logoutUser() {
    console.log('logout');
    localStorage.clear();
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/logout', {},
      {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
        this.router.navigate(['/login']);
      },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  checkAll() {
    const checkboxes = <HTMLInputElement[]><any>document.getElementsByClassName('checkbox');

    for (let i = 0; i < checkboxes.length; i++) {
      checkboxes[i].checked = true;
    }
  }

  exportSelected() {
    const checkboxes = <HTMLInputElement[]><any>document.getElementsByClassName('checkbox');

    const userList = [];
    for (let i = 0; i < checkboxes.length; i++) {
      const id = checkboxes[i].id;

      if (checkboxes[i].checked === true) {
        userList.push(id);
      }
    }
    console.log(userList);

    if (userList.length === 0) {
      alert(`You haven't selected any users`);
      return;
    }

    const userIdentifiers = {userToken: this.userToken, id: this.userID};
    const userListRequest = {usersToExport: userList};
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/exportusers', {
      userIdentifiers,
      userListRequest
    }, {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
        this.saveToFileSystem(data);
      },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  private saveToFileSystem(response) {
    const data = new Blob([response], {type: 'text/plain;charset=utf-8'});
    saveAs(data, 'users.xml');
  }

}
