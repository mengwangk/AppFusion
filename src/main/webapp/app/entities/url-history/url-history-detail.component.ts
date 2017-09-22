import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { UrlHistory } from './url-history.model';
import { UrlHistoryService } from './url-history.service';

@Component({
    selector: 'jhi-url-history-detail',
    templateUrl: './url-history-detail.component.html'
})
export class UrlHistoryDetailComponent implements OnInit, OnDestroy {

    urlHistory: UrlHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private urlHistoryService: UrlHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUrlHistories();
    }

    load(id) {
        this.urlHistoryService.find(id).subscribe((urlHistory) => {
            this.urlHistory = urlHistory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUrlHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'urlHistoryListModification',
            (response) => this.load(this.urlHistory.id)
        );
    }
}
