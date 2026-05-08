import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

(window as any).Buffer = Buffer;
(window as any).global = window;
(window as any).process = { env: {} };

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
