import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {AppfusionSharedModule} from '../shared';

import {HOME_ROUTE, HomeComponent} from './';

import {FileUploadModule} from 'ng2-file-upload';

@NgModule({
  imports: [
    AppfusionSharedModule,
    RouterModule.forRoot([HOME_ROUTE], {useHash: true}),
    FileUploadModule
  ],
  declarations: [
    HomeComponent,
  ],
  entryComponents: [
  ],
  providers: [
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppfusionHomeModule {}
