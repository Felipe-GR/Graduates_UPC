import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

  chatID;

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  showConversation = false;

  activeChats = [];
  userIDsChats = [];
  chatIDs = [];
  chatHistory = [];

  message: string;
  interval;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.chatID = +params['chat_id'];
      this.getActiveChats();
      if (this.chatID) {
        this.loadChatRoom();
        // Get new content after 30 seconds
        this.interval = setInterval(() => { this.loadChatRoom(); }, 30000);
      }
    });
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

  getActiveChats() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getactivechats', {
      userIdentifiers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.activeChats = obj;

      for (let i = 0; i < obj.length; i++) {
        const uIDString: any = obj[i].user1.toString();
        if (uIDString === this.userId) {
          this.userIDsChats.push(obj[i].user2);
          this.chatIDs.push(obj[i].id);
        } else {
          this.userIDsChats.push(obj[i].user1);
          this.chatIDs.push(obj[i].id);
        }
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  loadChatRoom() {
    this.route.params.subscribe((params) => {
      this.chatID = +params['chat_id'];
      const userIdentifiers = { userToken: this.userToken, id: this.userId };
      const chatMessageContent = { chatID: this.chatID };
      const API_URL = environment.API_URL;
      const req = this.http.post(API_URL + '/api/getchatmessages', {
        userIdentifiers,
        chatMessageContent
      }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        const obj = JSON.parse(data);
        this.chatHistory = obj;
        this.showConversation = true;
      },
        (err: HttpErrorResponse) => {
          console.log(err);
          this.router.navigate(['/error', false], { skipLocationChange: true });
        });
    });
  }

  sendMessage() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const chatMessageContent = { chatID: this.chatID, messageContent: this.message };
    console.log(chatMessageContent);
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/sendchatmessage', {
      userIdentifiers,
      chatMessageContent
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.message = '';
      this.loadChatRoom();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
