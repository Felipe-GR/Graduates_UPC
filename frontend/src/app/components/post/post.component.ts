import { Component, OnInit, Input, HostListener } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [   // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(300, style({ opacity: 1 }))
      ]),
      transition(':leave', [   // :leave is alias to '* => void'
        animate(300, style({ opacity: 0 }))
      ])
    ])
  ]
})
export class PostComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  userName: string;
  userSurname: string;
  userImage: string;
  @Input() postId: string;
  @Input() userIdPost: string;
  @Input() date: Date;
  @Input() isAd: any;
  @Input() isPublic: any;
  @Input() content: string;
  @Input() hasImage: any;
  @Input() image: string;

  imageBytes;
  renderImage = false;

  // Interests
  interestedUsers = [];
  isInterested: boolean;
  numberOfInterests: any;

  // Applications
  appliedUsers = [];
  applied: boolean;
  numberOfApplications: any;

  // Comments
  showComments = false;
  comment: string;
  comments = [];
  numberOfComments: any;

  renderApplyButton: boolean;

  screenHeight;
  screenWidth;

  @HostListener('window:resize', ['$event'])
  onResize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {
    this.onResize();
  }

  ngOnInit() {
    if (this.hasImage === '1') {
      this.getImage();
      this.renderImage = true;
    }
    this.getUserIdentifiers();
    this.getInterests();
    this.getCommentsNumber();
    if (this.isAd === '1') {
      this.getApplications();
      if (this.userId === this.userIdPost) {
        this.renderApplyButton = false;
      } else {
        this.renderApplyButton = true;
      }
    }
  }

  getImage() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const image = { imageName: this.image };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getimage', {
      userIdentifiers,
      image
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.imageBytes = 'data:image/jpeg;base64,' + obj.image;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getUserIdentifiers() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdInfo: this.userIdPost };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserbasicinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.userName = obj.name;
      this.userSurname = obj.surname;
      this.userImage = 'data:image/jpeg;base64,' + obj.image;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  interested() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const interest = { postID: this.postId, isInterested: '1' };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/interest', {
      userIdentifiers,
      interest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.getInterests();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getInterests() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const interestedUsers = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/interestsdata', {
      userIdentifiers,
      interestedUsers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.isInterested = obj.isUserInterested;
      this.numberOfInterests = obj.numberOfInterestedUsers;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getInterestedUsers() {
    this.interestedUsers = [];
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const interestedUsers = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/interestedusersinfo', {
      userIdentifiers,
      interestedUsers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      const numberOfInterests = obj.numberOfResults;
      this.interestedUsers = obj.list;
      for (let i = 0; i < numberOfInterests; i++) {
        this.interestedUsers[i].image = 'data:image/jpeg;base64,' + this.interestedUsers[i].image;
      }
      const modalButton = 'interest-modal-button-' + this.postId;
      const modal = '#interest-modal-' + this.postId;
      document.getElementById(modalButton).setAttribute('data-target', modal);
      document.getElementById(modalButton).click();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  apply() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const apply = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/apply', {
      userIdentifiers,
      apply
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.getApplications();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getApplications() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const appliedUsers = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/applicationsdata', {
      userIdentifiers,
      appliedUsers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.applied = obj.isUserInterested;
      this.numberOfApplications = obj.numberOfInterestedUsers;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getAppliedUsers() {
    this.appliedUsers = [];
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const appliedUsers = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/appliedusersinfo', {
      userIdentifiers,
      appliedUsers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.numberOfApplications = obj.numberOfResults;
      this.appliedUsers = obj.list;
      for (let i = 0; i < this.numberOfApplications; i++) {
        this.appliedUsers[i].image = 'data:image/jpeg;base64,' + this.appliedUsers[i].image;
      }
      const modalButton = 'applications-modal-button-' + this.postId;
      const modal = '#applications-modal-' + this.postId;
      document.getElementById(modalButton).setAttribute('data-target', modal);
      document.getElementById(modalButton).click();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getCommentsNumber() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const commentData = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/gettotalcomments', {
      userIdentifiers,
      commentData
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.numberOfComments = obj;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  viewComments() {
    if (this.comments.length > 0) {
      this.showComments = !this.showComments;
    } else {
      this.loadComments();
    }
  }

  loadComments() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const commentData = { postID: this.postId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getcomments', {
      userIdentifiers,
      commentData
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.comments = obj;
      for (let i = 0; i < this.comments.length; i++) {
        this.comments[i].image = 'data:image/jpeg;base64,' + this.comments[i].image;
      }
      this.showComments = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  postComment() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const commentData = { postID: this.postId, comment: this.comment };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/postcomment', {
      userIdentifiers,
      commentData
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.comment = '';
      this.ngOnInit();
      this.loadComments();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  // Open Image Modal
  openModal() {
    document.getElementById('image-modal-' + this.postId).style.display = 'block';
    document.body.style.overflow = 'hidden';
    const image = document.getElementById('content-image-' + this.postId);
    const width = image.clientWidth;
    const height = image.clientHeight;
    const ratio = width / height;
    const screenHeight = this.screenHeight - 150;
    const screenWidth = this.screenWidth - 50;
    if (height > screenHeight) {
      const targetWidth = screenHeight * ratio;
      document.getElementById('image-modal-content-' + this.postId).style.height = screenHeight + 'px';
      document.getElementById('image-modal-content-' + this.postId).style.width = targetWidth + 'px';
    } else if (width > screenWidth) {
      const targetHeight = screenWidth / ratio;
      document.getElementById('image-modal-content-' + this.postId).style.height = targetHeight + 'px';
      document.getElementById('image-modal-content-' + this.postId).style.width = screenWidth + 'px';
    }
  }

  // Close Image Modal
  closeImageModal() {
    document.getElementById('image-modal-' + this.postId).style.display = 'none';
    document.body.style.overflow = 'visible';
  }

}
