import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';

// services
import { ApiService } from './api.service';
import { AlertService } from './alert.service';

// entities
import { Game } from '../entities/Game';
import { AlertMessage } from '../entities/AlertMessage';


@Injectable()
export class GameService {
    private http: HttpClient;
    private apiService: ApiService;
    private alertService: AlertService;

    private api: string;
    private gameListSubject: BehaviorSubject<Game[]>;
    private selectedGameSubject: Subject<Game>;
    private savedGameSubject: Subject<Game>;

  constructor(apiService: ApiService, httpClient: HttpClient, alertService: AlertService) {
      this.apiService = apiService;
      this.http = httpClient;
      this.alertService = alertService;

      this.api = 'games';
      this.gameListSubject = new BehaviorSubject([]);
      this.selectedGameSubject = new Subject();
      this.savedGameSubject = new Subject();

      this.getAll();
  }

  public getGameList(): Observable<Game[]> {
      return this.gameListSubject.asObservable();
  }

  public getSelectedGame(): Observable<Game> {
    return this.selectedGameSubject.asObservable();
  }

  public setSelectedGame(game: Game): void {
    this.selectedGameSubject.next(game);
  }

  public getAll(): void {
      const url = this.apiService.getApiUrl(this.api);

      this.http.get<Game[]>(url, { withCredentials: true }).subscribe( (games) => {
          this.gameListSubject.next(games);
      });
  }

  public getSavedGames(): Observable<Game> {
    return this.savedGameSubject.asObservable();
  }

  public save(game: Game): void {
      const url = this.apiService.getApiUrl(this.api);
      const data = JSON.stringify(game);

      this.http.post<Game>(url, data, { 
            withCredentials: true,
            headers: { 'Content-Type': 'application/json' },
         } )
        .subscribe( (savedGame) => {
          this.alertService.push('game updated successfully', AlertMessage.CATEGORY_SUCCESS);
          this.savedGameSubject.next(savedGame);
        }, (error) => {
          this.alertService.push('game update failed', AlertMessage.CATEGORY_ERROR);
        });
  }

}
