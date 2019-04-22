import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatwithuserComponent } from './chatwithuser.component';

describe('ChatwithuserComponent', () => {
  let component: ChatwithuserComponent;
  let fixture: ComponentFixture<ChatwithuserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChatwithuserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatwithuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
