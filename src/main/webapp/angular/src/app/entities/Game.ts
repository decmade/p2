import { Sport } from './Sport';
import { Team } from './Team';
import { Venue } from './Venue';

export class Game {
    id: number;
    number: number;
    attendance: number;
    weather: string;
    status: string;
    scheduled: Date;
    sport: Sport;
    homeTeam: Team;
    awayTeam: Team;
    venue: Venue;
    homeScore: number;
    homeSpread: number;
    homeMoneyLine: number;
    awayScore: number;
    awaySpread: number;
    awayMoneyLine: number;
    total;
}
