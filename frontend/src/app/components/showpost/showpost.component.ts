import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-showpost',
  templateUrl: './showpost.component.html',
  styleUrls: ['./showpost.component.css']
})
export class ShowpostComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  renderPostComponent = false;
  userIDPost: string;
  isAd: string;
  isPublic: string;
  timestamp: string;
  content: string;
  hasImage;
  image;
  postID;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.postID = +params['post_id'];
      this.getPost();
    });
  }

  getPost() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const postRequest = { postID: this.postID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getpost', {
      userIdentifiers,
      postRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.userIDPost = obj.userId;
      this.isAd = obj.isAdvertisment;
      this.isPublic = obj.isPublic;
      this.timestamp = obj.timestamp;
      this.content = obj.content;
      this.hasImage = obj.hasImage;
      this.image = obj.image;
      this.renderPostComponent = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
