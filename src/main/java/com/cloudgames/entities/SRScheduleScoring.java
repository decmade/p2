package com.cloudgames.entities;

import java.util.List;

public class SRScheduleScoring {
	public int home_points;
	public int away_points;
	public List<SRSchedulePeriod> periods;
}
/*
"scoring": {
"home_points": 24,
"away_points": 21,
"periods": [
    {
        "period_type": "quarter",
        "id": "df064ca2-e987-4523-8eac-585da60dcdb7",
        "number": 1,
        "sequence": 1,
        "home_points": 7,
        "away_points": 0
    },
    {
        "period_type": "quarter",
        "id": "5323093e-933a-4e71-a363-6e7d9f77a937",
        "number": 2,
        "sequence": 2,
        "home_points": 7,
        "away_points": 7
    },
    {
        "period_type": "quarter",
        "id": "1a2d056a-31aa-4119-aa9b-11ea73bdbe60",
        "number": 3,
        "sequence": 3,
        "home_points": 10,
        "away_points": 0
    },
    {
        "period_type": "quarter",
        "id": "6a8529b8-7c54-4d6d-9bdb-795612855e5b",
        "number": 4,
        "sequence": 4,
        "home_points": 0,
        "away_points": 14
    }
]
}
*/