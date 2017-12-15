import { Component, OnInit, OnDestroy } from '@angular/core';

import { Subscription } from 'rxjs/Subscription';

// service
import { GameService } from '../../services/game.service';
import { GoogleMapService } from '../../services/google-map.service';
import { AuthenticationService } from '../../services/authentication.service';

// entites
import { Game } from '../../entities/Game';
import { Venue } from '../../entities/Venue';
import { User } from '../../entities/User';

import * as any from 'jquery';



@Component({
  selector: 'app-football',
  templateUrl: './football.component.html',
  styleUrls: ['./football.component.css']
})
export class FootballComponent implements OnInit, OnDestroy {

    private gameService: GameService;
    private mapService: GoogleMapService;
    private authService: AuthenticationService;

    private gameListSubscription: Subscription;
    private userSubscription: Subscription;

    private games: Game[];
    private user: User;

    constructor(gameService: GameService, googleMapService: GoogleMapService, authService: AuthenticationService ) {
        this.gameService = gameService;
        this.mapService = googleMapService;
        this.authService = authService;
    }

    public getScheduledGames(): Game[] {
        return this.games.filter( (game) => (game.status === 'scheduled') );
    }

    public onEditClick(game: Game): void {
        this.gameService.setSelectedGame(game);
    }

    public getMapUrl(venue: Venue): string {
        return this.mapService.getVenueMapUrl(venue);
    }

    private setGames( games: Game[] ) {
        this.games = games.filter( (game) => game.sport.description === 'Football' );

        this.sortGames();
    }

    private sortGames(): void {
        this.games.sort( (a, b) => {
            switch (true) {
                case ( a.scheduled < b.scheduled ) :
                    return -1;
                case ( a.scheduled > b.scheduled ) :
                    return 1;
                default :
                    return 0;
            }
        });
    }

    ngOnInit(): void {
        this.gameListSubscription = this.gameService.getGameList()
            .subscribe( (games) => this.setGames(games) );
        
        this.userSubscription = this.authService.getCurrentUser()
            .subscribe( (user) => this.user = user );
    }

    ngOnDestroy(): void {
        this.gameListSubscription.unsubscribe();

        this.userSubscription.unsubscribe();
    }

}
