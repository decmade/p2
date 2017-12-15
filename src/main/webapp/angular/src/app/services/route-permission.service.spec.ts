import { TestBed, inject } from '@angular/core/testing';

import { RoutePermissionService } from './route-permission.service';

describe('RoutePermissionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RoutePermissionService]
    });
  });

  it('should be created', inject([RoutePermissionService], (service: RoutePermissionService) => {
    expect(service).toBeTruthy();
  }));
});
