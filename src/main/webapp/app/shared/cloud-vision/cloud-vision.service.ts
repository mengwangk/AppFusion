import { Injectable } from '@angular/core';

@Injectable()
export class CloudVisionService {

  constructor() { }

  vision() {
    console.log('Cloud vision - it works!');
  }

}
