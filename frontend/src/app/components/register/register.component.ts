import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit {

  email: string;
  password_1: string;
  password_2: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  fileToUpload: File;

  imageUploaded: boolean;

  hasError = false;
  alerts: string;
  type: 'danger';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  registerUser() {

    this.hasError = false;

    let errorsFound = false;

    if (this.fileToUpload === null) {
      this.showAlert('EMPTY_FIELDS');
      errorsFound = true;
    }

    if (this.email === '' || this.password_1 === '' || this.password_2 === '' || this.firstName === '' ||
      this.lastName === '' || this.phoneNumber === '' || this.fileToUpload === undefined) {
      this.showAlert('EMPTY_FIELDS');
      errorsFound = true;
    }

    if (this.password_1 !== this.password_2) {
      this.showAlert('PASSWORDS_NOT_MATCHING');
      errorsFound = true;
    }

    if (errorsFound === false) {

      const formData: FormData = new FormData();

      const user = {
        'email': this.email,
        'password_1': this.password_1,
        'password_2': this.password_2,
        'firstName': this.firstName,
        'lastName': this.lastName,
        'phoneNumber': this.phoneNumber
      };

      formData.append('user', JSON.stringify(user));
      formData.append('profileImage', this.fileToUpload);

      const API_URL = environment.API_URL;
      const req = this.http.post(API_URL + '/api/register',
        formData
        , { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
          this.router.navigate(['/login']); // TODO make a pop up that says "Register completed"
        },
          (err: HttpErrorResponse) => {
            this.showAlert(err.error);
            console.log(err);
          });
    }
  }

  handleFileInput(files: FileList) {
    if (files.item(0) === null) {
      this.imageUploaded = false;
    } else {
      this.fileToUpload = files.item(0);
      this.imageUploaded = true;
    }
  }

  showAlert(errorMessage) {
    console.log(errorMessage);
    if (errorMessage === 'EMAIL_EXISTS') {
      this.hasError = true;
      this.alerts = 'The email you entered is already associated with a LinkDIn account';
    } else if (errorMessage === 'EMPTY_FIELDS') {
      this.hasError = true;
      this.alerts = 'Please fill all the fields';
    } else if (errorMessage === 'PASSWORDS_NOT_MATCHING') {
      this.hasError = true;
      this.alerts = 'Password & Confirm Password fields must be the same';
    }
  }
}
