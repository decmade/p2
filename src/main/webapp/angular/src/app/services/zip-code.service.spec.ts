import { TestBed, inject } from '@angular/core/testing';

import { ZipCodeService } from './zip-code.service';

describe('ZipCodeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ZipCodeService]
    });
  });

  it('should be created', inject([ZipCodeService], (service: ZipCodeService) => {
    expect(service).toBeTruthy();
  }));
});
