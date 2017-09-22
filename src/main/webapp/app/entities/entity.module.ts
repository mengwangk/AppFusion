import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppfusionUrlHistoryModule } from './url-history/url-history.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        AppfusionUrlHistoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppfusionEntityModule {}
