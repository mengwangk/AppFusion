import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UrlHistory } from './url-history.model';
import { UrlHistoryPopupService } from './url-history-popup.service';
import { UrlHistoryService } from './url-history.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-url-history-dialog',
    templateUrl: './url-history-dialog.component.html'
})
export class UrlHistoryDialogComponent implements OnInit {

    urlHistory: UrlHistory;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private urlHistoryService: UrlHistoryService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.urlHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.urlHistoryService.update(this.urlHistory));
        } else {
            this.subscribeToSaveResponse(
                this.urlHistoryService.create(this.urlHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<UrlHistory>) {
        result.subscribe((res: UrlHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: UrlHistory) {
        this.eventManager.broadcast({ name: 'urlHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-url-history-popup',
    template: ''
})
export class UrlHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private urlHistoryPopupService: UrlHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.urlHistoryPopupService
                    .open(UrlHistoryDialogComponent as Component, params['id']);
            } else {
                this.urlHistoryPopupService
                    .open(UrlHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
