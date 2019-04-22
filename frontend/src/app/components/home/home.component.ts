import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { FriendspostsComponent } from '../friendsposts/friendsposts.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  @ViewChild(FriendspostsComponent) child;
  message: string;

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  pageType;
  renderMostRecent;
  renderRecommended;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.pageType = 'Most Recent';
    this.renderMostRecent = true;
    this.renderRecommended = false;
  }

  changeHomeType(type) {
    if (type === 1) {
      this.pageType = 'Most Recent';
      this.renderMostRecent = true;
      this.renderRecommended = false;
    }
    if (type === 2) {
      this.pageType = 'Recommended for you';
      this.renderMostRecent = false;
      this.renderRecommended = true;
    }
  }

  async recieveRefreshCommand($event) {
    this.message = $event;
    // this.constructor();
    // console.log(this.message);
    await this.delay(1500);
    if (this.message === '1') {
      this.child.ngOnInit();
    }
  }

  async delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}
