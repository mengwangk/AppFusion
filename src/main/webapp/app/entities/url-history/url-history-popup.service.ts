import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { UrlHistory } from './url-history.model';
import { UrlHistoryService } from './url-history.service';

@Injectable()
export class UrlHistoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private urlHistoryService: UrlHistoryService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.urlHistoryService.find(id).subscribe((urlHistory) => {
                    urlHistory.dateCreated = this.datePipe
                        .transform(urlHistory.dateCreated, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.urlHistoryModalRef(component, urlHistory);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.urlHistoryModalRef(component, new UrlHistory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    urlHistoryModalRef(component: Component, urlHistory: UrlHistory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.urlHistory = urlHistory;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
