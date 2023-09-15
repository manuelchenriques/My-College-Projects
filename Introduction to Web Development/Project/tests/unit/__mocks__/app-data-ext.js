'use strict';

const games = {"OIXt3DmJU0": {"gameID": "OIXt3DmJU0"}};

const queries = {"gameID": "OIXt3DmJU0"};

function findGameByID(query) {
	const game = games[query];
	if (!game) return { };
    return {game};
}
	
module.exports = {
	games,
	queries,
	findGameByID
};
