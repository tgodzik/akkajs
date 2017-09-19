import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScriptsComponent } from './scripts.component';
import { ScriptDetailComponent } from './script-detail.component';
import { AppComponent } from './app.component';
import { ScriptService } from './script.service';
import { RouterModule } from '@angular/router';

import { HttpModule }    from '@angular/http';
import { DashboardComponent } from './dashboard.component'
import { AppRoutingModule } from './app.routing.module';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule
  ],
  declarations: [
    DashboardComponent,
    AppComponent,
    ScriptsComponent,
    ScriptDetailComponent
  ],
  providers: [ScriptService],
  bootstrap: [AppComponent]
})
export class AppModule { }
