import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal, CloudVisionService } from '../shared';

import { FileUploader, FileItem, ParsedResponseHeaders } from 'ng2-file-upload';

const UPLOAD_URL = _UPLOAD_URL_;

@Component( {
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

} )
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    cloudVisionOutput: string;
    public uploader: FileUploader = new FileUploader(
        { url: UPLOAD_URL, allowedMimeType: ['image/png', 'image/gif', 'image/jpeg'] }
    );
    public hasFileDropZoneOver = false;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private cloudVisionService: CloudVisionService
    ) {
        this.uploader.onErrorItem = ( item, response, status, headers ) => this.onErrorItem( item, response, status, headers );
        this.uploader.onSuccessItem = ( item, response, status, headers ) => this.onSuccessItem( item, response, status, headers );
    }

    ngOnInit() {
        this.principal.identity().then(( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then(( account ) => {
                this.account = account;
            } );
        } );
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    public fileOver( e: any ): void {
        this.hasFileDropZoneOver = e;
    }

    onSuccessItem( item: FileItem, response: string, status: number, headers: ParsedResponseHeaders ): any {
        const data = JSON.parse( response );
        console.log( 'response --' + response );
    }

    onErrorItem( item: FileItem, response: string, status: number, headers: ParsedResponseHeaders ): any {
        const error = JSON.parse( response );
    }
}
