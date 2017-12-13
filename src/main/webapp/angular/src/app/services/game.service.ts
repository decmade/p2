import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

// services
import { ApiService } from './api.service';

// entities
import { Game } from '../entities/Game';


@Injectable()
export class GameService {
    private http: HttpClient;
    private apiService: ApiService;

    private api: string;
    private gameListSubject: BehaviorSubject<Game[]>;
    private selectedGameSubject: BehaviorSubject<Game>;

  constructor(apiService: ApiService, httpClient: HttpClient) {
      this.apiService = apiService;
      this.http = httpClient;

      this.api = 'games';
      this.gameListSubject = new BehaviorSubject([]);
      this.selectedGameSubject = new BehaviorSubject(null);

      this.getAll();
  }

  public getGameList(): Observable<Game[]> {
      return this.gameListSubject.asObservable();
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

}
