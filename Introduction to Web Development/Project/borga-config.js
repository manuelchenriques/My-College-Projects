//Default server configurations
'use strict';

module.exports = {

	//Local website url
	devl_es_url: 'http://localhost:9200/',

	//Guest user
	guest: {
		id: '1',
		username: 'guest',
		password: 'passe123',
		token: 'cfd03665-781b-4b40-9baa-171688095b59'
	},

	//A default user
	jonas: {
		id : '2',
        username: 'jonas',
        password: '123',
        token : 'cfd03665-781b-4b40-9baa-171688095b58'
	},

	//guest default group
	guestGroup: {
        id : '1',
        name: 'My Favorite games',
        description: 'My favorite games are stored here.',
        games: [],
        createdBy : '1'
    },

	//jonas default group
	jonasGroup: {
        id : '2',
        name: 'Test Group',
        description: 'This is a group for testing.',
        games: [],
        createdBy : '2'
    },
	
	//A default game
	game: {
		gameID: 'TAAifFP590', 
		gameName: 'Root' 
	}
};