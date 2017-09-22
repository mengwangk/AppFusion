import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UrlHistory } from './url-history.model';
import { UrlHistoryPopupService } from './url-history-popup.service';
import { UrlHistoryService } from './url-history.service';

@Component({
    selector: 'jhi-url-history-delete-dialog',
    templateUrl: './url-history-delete-dialog.component.html'
})
export class UrlHistoryDeleteDialogComponent {

    urlHistory: UrlHistory;

    constructor(
        private urlHistoryService: UrlHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.urlHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'urlHistoryListModification',
                content: 'Deleted an urlHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-url-history-delete-popup',
    template: ''
})
export class UrlHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private urlHistoryPopupService: UrlHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.urlHistoryPopupService
                .open(UrlHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
