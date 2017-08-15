import { Component } from '@angular/core';

export class Script {
  id: string;
  code: string;
  name: string;
}

const scripts: Script[] = [
  { id: '1', code: 'println(Hello world!)', name: 'Simple1' },
  { id: '2', code: 'println(Hello world!)', name: 'Simple2' },
  { id: '3', code: 'println(Hello world!)', name: 'Simple3' },
  { id: '4', code: 'println(Hello world!)', name: 'Simple4' },
  { id: '5', code: 'println(Hello world!)', name: 'Simple5' },
  { id: '6', code: 'println(Hello world!)', name: 'Simple6' },
  { id: '7', code: 'println(Hello world!)', name: 'Simple7' },
  { id: '8', code: 'println(Hello world!)', name: 'Simple8' },
  { id: '9', code: 'println(Hello world!)', name: 'Simple9' }
]

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Available scripts';
  selectedScript: Script;
  allScripts = scripts;

  onSelect(script: Script): void {
    this.selectedScript = script;
  }
}
