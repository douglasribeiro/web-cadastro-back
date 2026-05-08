import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComerciaisComponent } from './comerciais.component';

describe('ComerciaisComponent', () => {
  let component: ComerciaisComponent;
  let fixture: ComponentFixture<ComerciaisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComerciaisComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComerciaisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
