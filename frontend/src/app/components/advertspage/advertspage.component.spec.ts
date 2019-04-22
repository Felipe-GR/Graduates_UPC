import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertspageComponent } from './advertspage.component';

describe('AdvertspageComponent', () => {
  let component: AdvertspageComponent;
  let fixture: ComponentFixture<AdvertspageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvertspageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvertspageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
