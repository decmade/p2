import { Sport } from './Sport';
import { Team } from './Team';
import { Venue } from './Venue';

export class Game {
    id: Number;
    number: Number;
    attendance: Number;
    weather: String;
    status: String;
    scheduled: Date;
    sport: Sport;
    homeTeam: Team;
    awayTeam: Team;
    venue: Venue;
    homeScore: Number;
    awayScore: Number;
}
