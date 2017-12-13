import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SoccerComponent } from './soccer.component';

describe('FootballComponent', () => {
  let component: SoccerComponent;
  let fixture: ComponentFixture<SoccerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SoccerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SoccerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});