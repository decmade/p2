import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseballComponent } from './baseball.component';

describe('BaseballComponent', () => {
  let component: BaseballComponent;
  let fixture: ComponentFixture<BaseballComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BaseballComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseballComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});