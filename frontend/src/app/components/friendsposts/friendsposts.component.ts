import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-friendsposts',
  templateUrl: './friendsposts.component.html',
  styleUrls: ['./friendsposts.component.css']
})
export class FriendspostsComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  // Posts variables
  renderPosts = false;
  totalPosts = 0;
  showedPosts = 0;
  loadMoreButton = false;
  page = 0;
  limitPosts = 5;
  posts = [];
  friendsInterests = [];
  friendsInterestsIDs = [];
  friendsInterestsPostsIDs = [];
  friendsCommentsIDs = [];
  friendsCommentsPostsIDs = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.totalPosts = 0;
    this.showedPosts = 0;
    this.loadMoreButton = false;
    this.page = 0;
    this.limitPosts = 5;
    this.posts = [];
    this.renderPosts = false;
    this.friendsInterestsIDs = [];
    this.friendsInterestsPostsIDs = [];
    this.friendsCommentsIDs = [];
    this.friendsCommentsPostsIDs = [];
    this.getFriendsActivity();
    this.getPosts();
  }

  getFriendsActivity() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getfriendsactivity', {
      userIdentifiers,
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      if (data !== 'null') {
        this.friendsInterests = obj;
        for (let i = 0; i < obj[0].length; i++) {
          this.friendsInterestsIDs.push(obj[0][i][0]);
          this.friendsInterestsPostsIDs.push(obj[0][i][1]);
        }
        for (let i = 0; i < obj[1].length; i++) {
          this.friendsCommentsIDs.push(obj[1][i][0]);
          this.friendsCommentsPostsIDs.push(obj[1][i][1]);
        }
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  getPosts() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const pageRequest = { pageNumber: this.page, limit: this.limitPosts };
    this.page++;
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getpostsfromfriends', {
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
      this.renderPosts = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
