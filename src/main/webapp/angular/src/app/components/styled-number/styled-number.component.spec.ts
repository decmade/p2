import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StyledNumberComponent } from './styled-number.component';

describe('StyledNumberComponent', () => {
  let component: StyledNumberComponent;
  let fixture: ComponentFixture<StyledNumberComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StyledNumberComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StyledNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
