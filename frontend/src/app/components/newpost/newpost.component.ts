import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-newpost',
  templateUrl: './newpost.component.html',
  styleUrls: ['./newpost.component.css']
})
export class NewpostComponent implements OnInit {

  @Output() messageEvent = new EventEmitter<string>();

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  postContent: string;
  isAd: boolean;
  isPublic: boolean;
  fileToUpload: File;
  image: string;
  fileName;

  postShared = false;
  alerts: string;
  type: 'success';

  postID;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.isAd = false;
    this.isPublic = false;
  }

  share() {
    this.postShared = false;
    if (!this.fileToUpload) {
      this.shareTextPost();
    } else {
      this.shareImagePost();
    }
    this.sendRefreshMessage();
  }

  shareTextPost() {
    if (this.postContent) {
      const userIdentifiers = { userToken: this.userToken, id: this.userId };
      const postData = { postContent: this.postContent, isAd: this.isAd, isPublic: this.isPublic };
      const API_URL = environment.API_URL;
      const req = this.http.post(API_URL + '/api/newpost', {
        userIdentifiers,
        postData
      }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        // Reset fields
        this.postContent = '';
        this.isAd = false;
        this.isPublic = false;
        this.showAlert();
      },
        (err: HttpErrorResponse) => {
          console.log(err);
          this.router.navigate(['/error', false], { skipLocationChange: true });
        });
    }
  }

  shareImagePost() {
    const formData: FormData = new FormData();
    const userIdentifiers = { 'userToken': this.userToken, 'id': this.userId };
    const postData = { 'postContent': this.postContent, 'isAd': this.isAd, 'isPublic': this.isPublic };
    formData.append('userIdentifiers', JSON.stringify(userIdentifiers));
    formData.append('postData', JSON.stringify(postData));
    formData.append('image', this.fileToUpload);
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/postimage',
      formData
      , { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        this.fileToUpload = null;
        // Reset fields
        this.postContent = '';
        this.isAd = false;
        this.isPublic = false;
        this.fileName = '';
        this.showAlert();
      },
        (err: HttpErrorResponse) => {
          console.log(err);
        });
  }

  handleFileInput(files: FileList) {
    if (files.item(0) !== null) {
      this.fileToUpload = files.item(0);
    }
  }

  showAlert() {
    this.postShared = true;
    this.alerts = 'Post Uploaded!';
    setTimeout(function () { document.getElementById('close-alert').click(); }, 5000);
  }

  sendRefreshMessage() {
    this.messageEvent.emit('1');
  }
}
