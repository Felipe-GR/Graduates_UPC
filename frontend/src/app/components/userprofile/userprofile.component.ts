import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {

  requestConnectButton = false;
  connectPendingButton = false;
  connectedButton = false;
  networkButton = false;

  // Retrieve user's data from local storage
  email = localStorage.getItem('email');
  userToken = localStorage.getItem('userToken');
  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');
  userId = localStorage.getItem('userID');

  hideElements = false;

  // Variables for requested user's profile
  profileFirstName: string;
  profileSurname: string;
  profilePhoneNumber: string;
  profileCity: string;
  profileProfession: string;
  profileCompany: string;
  profileEducation: string;
  profileImage: string;
  profileUserID;

  // Posts variables
  totalPosts = 0;
  showedPosts = 0;
  loadMoreButton = false;
  page = 0;
  limitPosts = 5;
  posts = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {

    this.route.params.subscribe((params) => {
      this.profileUserID = +params['user_id'];
      // Posts variables
      this.totalPosts = 0;
      this.showedPosts = 0;
      this.loadMoreButton = false;
      this.page = 0;
      this.limitPosts = 5;
      this.posts = [];
      window.scrollTo(0, 0);
      this.checkIfAdmin();
      this.loadProfile();
    });
  }

  checkIfAdmin() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/isadmin', {
      userIdentifiers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      if (obj === true) {
        this.hideElements = true;
      } else {
        this.hideElements = false;
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  loadProfile() {
    if (this.userId === this.profileUserID.toString()) {
      this.requestConnectButton = false;
      this.connectPendingButton = false;
      this.connectedButton = false;
      this.networkButton = false;
    } else {
      this.checkConnectStatus();
    }
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const requestProfile = { profileUserID: this.profileUserID };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/user', {
      userIdentifiers,
      requestProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profileImage = 'data:image/jpeg;base64,' + obj.profileImage;
      const userObj = JSON.parse(obj.user);
      this.profileFirstName = userObj.firstName;
      this.profileSurname = userObj.lastName;
      this.getUserInfo();
      this.getPosts();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  getUserInfo() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdInfo: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profilePhoneNumber = obj.phoneNumber;
      this.profileCity = obj.city;
      this.profileProfession = obj.profession;
      this.profileCompany = obj.company;
      this.profileEducation = obj.education;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  getPosts() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const pageRequest = { profileUserID: this.profileUserID, pageNumber: this.page, limit: this.limitPosts };
    this.page++;
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getprofileposts', {
      userIdentifiers,
      pageRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.totalPosts = obj.totalElements;
      if (this.totalPosts > 0) {
        const numberOfPosts = obj.numberOfElements;
        this.showedPosts = this.showedPosts + numberOfPosts;
        if (this.totalPosts > this.showedPosts) {
          this.loadMoreButton = true;
        } else {
          this.loadMoreButton = false;
        }
        for (let i = 0; i < numberOfPosts; i++) {
          this.posts.push(obj.content[i]);
        }
      } else {
        this.posts = null;
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  checkConnectStatus() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const targetProfile = { profileUserID: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/connectstatus', {
      userIdentifiers,
      targetProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      if (obj.friends === '0') {
        this.requestConnectButton = true;
        this.connectPendingButton = false;
        this.connectedButton = false;
        this.networkButton = false;
      } else {
        // Show network if only you are connected to this person
        if (obj.pending === '1') {
          this.networkButton = false;
          this.requestConnectButton = false;
          this.connectPendingButton = true;
          this.connectedButton = false;
        } else {
          // They are connected
          this.networkButton = true;
          this.requestConnectButton = false;
          this.connectPendingButton = false;
          this.connectedButton = true;
        }
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  sendConnectRequest() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const friendRequest = { userRequestID: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/sendconnect', {
      userIdentifiers,
      friendRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.checkConnectStatus();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
