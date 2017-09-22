import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppfusionSharedModule } from '../../shared';
import { AppfusionAdminModule } from '../../admin/admin.module';
import {
    UrlHistoryService,
    UrlHistoryPopupService,
    UrlHistoryComponent,
    UrlHistoryDetailComponent,
    UrlHistoryDialogComponent,
    UrlHistoryPopupComponent,
    UrlHistoryDeletePopupComponent,
    UrlHistoryDeleteDialogComponent,
    urlHistoryRoute,
    urlHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...urlHistoryRoute,
    ...urlHistoryPopupRoute,
];

@NgModule({
    imports: [
        AppfusionSharedModule,
        AppfusionAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UrlHistoryComponent,
        UrlHistoryDetailComponent,
        UrlHistoryDialogComponent,
        UrlHistoryDeleteDialogComponent,
        UrlHistoryPopupComponent,
        UrlHistoryDeletePopupComponent,
    ],
    entryComponents: [
        UrlHistoryComponent,
        UrlHistoryDialogComponent,
        UrlHistoryPopupComponent,
        UrlHistoryDeleteDialogComponent,
        UrlHistoryDeletePopupComponent,
    ],
    providers: [
        UrlHistoryService,
        UrlHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppfusionUrlHistoryModule {}
