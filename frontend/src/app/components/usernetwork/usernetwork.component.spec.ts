import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UsernetworkComponent } from './usernetwork.component';

describe('UsernetworkComponent', () => {
  let component: UsernetworkComponent;
  let fixture: ComponentFixture<UsernetworkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UsernetworkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UsernetworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
