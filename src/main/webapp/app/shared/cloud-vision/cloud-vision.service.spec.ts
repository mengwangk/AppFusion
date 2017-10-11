import { TestBed, inject } from '@angular/core/testing';

import { CloudVisionService } from './cloud-vision.service';

describe('CloudVisionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CloudVisionService]
    });
  });

  it('should be created', inject([CloudVisionService], (service: CloudVisionService) => {
    expect(service).toBeTruthy();
  }));
});
