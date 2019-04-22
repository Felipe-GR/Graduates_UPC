import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecommendedpostsComponent } from './recommendedposts.component';

describe('RecommendedpostsComponent', () => {
  let component: RecommendedpostsComponent;
  let fixture: ComponentFixture<RecommendedpostsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecommendedpostsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecommendedpostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
