/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AppfusionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UrlHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/url-history/url-history-detail.component';
import { UrlHistoryService } from '../../../../../../main/webapp/app/entities/url-history/url-history.service';
import { UrlHistory } from '../../../../../../main/webapp/app/entities/url-history/url-history.model';

describe('Component Tests', () => {

    describe('UrlHistory Management Detail Component', () => {
        let comp: UrlHistoryDetailComponent;
        let fixture: ComponentFixture<UrlHistoryDetailComponent>;
        let service: UrlHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AppfusionTestModule],
                declarations: [UrlHistoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UrlHistoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(UrlHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UrlHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UrlHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UrlHistory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.urlHistory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
