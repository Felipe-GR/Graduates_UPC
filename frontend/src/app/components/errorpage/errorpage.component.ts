import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-errorpage',
  templateUrl: './errorpage.component.html',
  styleUrls: ['./errorpage.component.css']
})
export class ErrorpageComponent implements OnInit {

  logoutUser: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  // TODO na stelnw subscribe server gia logout
  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.logoutUser = params['logout'];
    });
    if (this.logoutUser === 'true') {
      localStorage.clear();
    }
  }

}
