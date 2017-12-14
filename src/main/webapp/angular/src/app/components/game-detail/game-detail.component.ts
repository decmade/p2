import { Component, OnInit, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

// rxjs
import { Subscription } from 'rxjs/Subscription';

// services
import { GameService } from '../../services/game.service';

// entities
import { Game } from '../../entities/Game';

import * as any from 'jquery';

@Component({
  selector: 'app-game-detail',
  templateUrl: './game-detail.component.html',
  styleUrls: ['./game-detail.component.css']
})
export class GameDetailComponent implements OnInit, OnDestroy {
    private gameService: GameService;

    private selectedGameSubscription: Subscription;
    private savedGameSubscription: Subscription;
    private game: Game;
    private gameClone: Game;


    @ViewChild('gameModal') modal: ElementRef;
    @ViewChild('gameForm') form: NgForm;

    constructor(gameService: GameService) {
        this.gameService = gameService;
        this.gameClone = new Game();
    }

    public onCancel(): void {
        this.unparkGame();
        this.closeModal();
    }

    public onSubmit(): void {
        this.gameService.save(this.game);
    }

    private onSavedGame(game: Game): void {
        this.closeModal();
    }

    private closeModal(): void {
        const modal = this.modal.nativeElement;

        $(modal).modal('hide');
    }

    private setGame(game: Game): void {
        const modal = this.modal.nativeElement;

        this.game = game;
        this.parkGame();

        if ( game ) {
            $(modal).modal('show');
        }
    }

    private parkGame(): void {
        Object.assign(this.gameClone, this.game);
    }

    private unparkGame(): void {
        Object.assign(this.game, this.gameClone);
    }


    ngOnInit(): void {
        this.selectedGameSubscription = this.gameService.getSelectedGame()
            .subscribe( (game) => this.setGame(game) );

        this.savedGameSubscription = this.gameService.getSavedGames()
            .subscribe( (game) => this.onSavedGame(game) );
    }

    ngOnDestroy(): void {
        this.selectedGameSubscription.unsubscribe();
        this.savedGameSubscription.unsubscribe();
    }

}
