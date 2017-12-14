import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-styled-number',
  templateUrl: './styled-number.component.html',
  styleUrls: ['./styled-number.component.css']
})

export class StyledNumberComponent implements OnInit {

    @Input() private number: number;

    constructor() { }

    public getClass(): any {
        return {
            'text-danger': ( this.number < 0 ),
            'text-success': ( this.number > 0 ),
        };
    }

    ngOnInit() {
    }

}
