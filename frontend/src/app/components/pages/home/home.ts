import { Component } from '@angular/core';
import { MainPage } from "../main-page/main-page";

@Component({
  selector: 'app-home',
  imports: [MainPage],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {}
