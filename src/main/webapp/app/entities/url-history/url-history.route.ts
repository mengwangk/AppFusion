import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UrlHistoryComponent } from './url-history.component';
import { UrlHistoryDetailComponent } from './url-history-detail.component';
import { UrlHistoryPopupComponent } from './url-history-dialog.component';
import { UrlHistoryDeletePopupComponent } from './url-history-delete-dialog.component';

export const urlHistoryRoute: Routes = [
    {
        path: 'url-history',
        component: UrlHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appfusionApp.urlHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'url-history/:id',
        component: UrlHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appfusionApp.urlHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const urlHistoryPopupRoute: Routes = [
    {
        path: 'url-history-new',
        component: UrlHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appfusionApp.urlHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'url-history/:id/edit',
        component: UrlHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appfusionApp.urlHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'url-history/:id/delete',
        component: UrlHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appfusionApp.urlHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
