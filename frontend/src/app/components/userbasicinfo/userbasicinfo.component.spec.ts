import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserbasicinfoComponent } from './userbasicinfo.component';

describe('UserbasicinfoComponent', () => {
  let component: UserbasicinfoComponent;
  let fixture: ComponentFixture<UserbasicinfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserbasicinfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserbasicinfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
