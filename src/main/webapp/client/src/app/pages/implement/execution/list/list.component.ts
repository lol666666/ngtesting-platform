import {Component, ViewEncapsulation, OnInit, AfterViewInit} from "@angular/core";

import {
  NodeEvent,
  NodeMovedRemoteEvent,
  NodeRemovedRemoteEvent,
  NodeCreatedEvent,
  NodeRenamedEvent,
  NodeSelectedEvent,
  TreeModel,
  TreeOptions
} from "../../../../components/ng2-tree";

import {GlobalState} from "../../../../global.state";
import {CONSTANT} from "../../../../utils/constant";
import {Utils} from "../../../../utils/utils";
import {RouteService} from "../../../../service/route";
import {SlimLoadingBarService} from "../../../../components/ng2-loading-bar";
import {TreeService} from "../../../../components/ng2-tree/src/tree.service";
import {CaseService} from "../../../../service/case";

@Component({
  selector: 'execution-list',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./list.scss'],
  templateUrl: './list.html'
})
export class ExecutionList implements OnInit, AfterViewInit {
  query:any = {keywords: '', status: ''};
  suiteId: number;
  data: any[];

  constructor(private _routeService:RouteService, private _state:GlobalState,
              private _treeService:TreeService, private _caseService:CaseService,
              private slimLoadingBarService:SlimLoadingBarService) {

    this._state.subscribe('suite.change', (testSuite: any) => {
      this.suiteId = testSuite.id;
      this.loadData();
    });

  }

  ngOnInit() {

  }

  ngAfterViewInit() {

  }

  delete(suiteId:string):void {

  }

  loadData() {
    let that = this;
    that._caseService.query(this.suiteId).subscribe((json:any) => {
      that.data = json.data;
      CONSTANT.CUSTOM_FIELD_FOR_PROJECT = json.customFields;

      this._state.notifyDataChanged('title.change', '测试用例');
    });
  }

  public select(testCase: any):void {
    this._state.notifyDataChanged('case.change', testCase);
  }

}
