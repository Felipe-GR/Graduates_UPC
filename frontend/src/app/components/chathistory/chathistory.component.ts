import { Component, OnInit, Input, AfterViewChecked, ElementRef, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-chathistory',
  templateUrl: './chathistory.component.html',
  styleUrls: ['./chathistory.component.css']
})
export class ChathistoryComponent implements OnInit, AfterViewChecked {

  @ViewChild('chathistoryContent') private myScrollContainer: ElementRef;

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  @Input() chatID;
  @Input() chatHistory = [];

  chatUserId;
  chatUserName;
  chatUserSurname;
  chatImage;

  render = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.getChatInfo();
    this.scrollToBottom();
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch (err) { }
  }

  getChatInfo() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const chat = { chatID: this.chatID };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getchatbyid', {
      userIdentifiers,
      chat
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      const uIDString: any = obj.user1.toString();
      if (uIDString === this.userId) {
        this.chatUserId = obj.user2;
      } else {
        this.chatUserId = obj.user1;
      }
      this.render = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
