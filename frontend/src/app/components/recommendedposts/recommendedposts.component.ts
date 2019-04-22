import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-recommendedposts',
  templateUrl: './recommendedposts.component.html',
  styleUrls: ['./recommendedposts.component.css']
})
export class RecommendedpostsComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  // Posts variables
  renderPosts = false;
  totalPosts = 0;
  showedPosts = 0;
  loadMoreButton = false;
  limitPosts = 5;
  posts = [];
  renderPostsArray = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.totalPosts = 0;
    this.showedPosts = 0;
    this.loadMoreButton = false;
    this.limitPosts = 5;
    this.posts = [];
    this.renderPosts = false;
    this.getPosts();
  }

  getPosts() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/recommendedposts', {
      userIdentifiers,
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.posts = obj;
      this.totalPosts = this.posts.length;
      this.showPosts();
      this.renderPosts = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  showPosts() {
    this.showedPosts = this.showedPosts + this.limitPosts;
    if (this.totalPosts > this.showedPosts) {
      this.loadMoreButton = true;
    } else {
      this.loadMoreButton = false;
    }
  }

}
