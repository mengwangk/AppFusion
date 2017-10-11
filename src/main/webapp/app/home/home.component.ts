import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal, CloudVisionService} from '../shared';

import {FileUploader} from 'ng2-file-upload';

const UPLOAD_URL = 'xx';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: [
    'home.css'
  ]

})
export class HomeComponent implements OnInit {
  account: Account;
  modalRef: NgbModalRef;

  public uploader: FileUploader = new FileUploader({url: UPLOAD_URL, allowedMimeType: ['image/png', 'image/gif', 'image/jpeg']});
  public hasFileDropZoneOver = false;

  constructor(
    private principal: Principal,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private cloudVisionService: CloudVisionService
  ) {
  }

  ngOnInit() {
    this.principal.identity().then((account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
    // console.log('upload url --' + UPLOAD_URL);
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', (message) => {
      this.principal.identity().then((account) => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.principal.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  public fileOver(e: any): void {
    this.hasFileDropZoneOver = e;
  }

}
