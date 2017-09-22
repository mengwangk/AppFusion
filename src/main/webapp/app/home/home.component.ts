import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';

import {FileUploader} from 'ng2-file-upload';

const UPLOAD_URL = 'https://www.appfusion.ml/api/';

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

  public uploader: FileUploader = new FileUploader({url: UPLOAD_URL});
  public hasFileDropZoneOver = false;

  constructor(
    private principal: Principal,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager
  ) {
  }

  ngOnInit() {
    this.principal.identity().then((account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
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

  public fileOverBase(e: any): void {
    this.hasFileDropZoneOver = e;
  }

}
