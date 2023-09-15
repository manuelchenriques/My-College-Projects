//Logic of the visual part of the API
'use strict';

const errors = require('./app-errors');
const express = require('express');

module.exports = function (services) {
	
	//Renders the resource 'home.hbs'
	function getHomepage(req, res) {
		const username = getUsername(req.user);
		res.render('home', { username });
	} 

	//Renders the resource 'search_game.hbs'
	function getSearchPage(req, res) {
		const username = getUsername(req.user);
		res.render('search_game', { username });
	}

	//Renders the resource 'createGroup.hbs'
	function getCreateGroupPage(req, res) {
		if(!req.isAuthenticated()){
			res.redirect('/login');
			return;
		}
		const username = getUsername(req.user);
		const error = req.session.error;
		req.session.error = null;
		res.render('createGroup', { username, error });
	}

	//Renders the resource 'login.hbs'
	function getLogInPage(req, res) {
		if (req.isAuthenticated()) res.redirect('/')
		const username = getUsername(req.user);
		const error = req.session.error;
		req.session.error = null;
		res.render('login', { username, error });
	}

	//Renders the resource 'createUser.hbs'
	function getSignUpPage(req, res) {
		const username = getUsername(req.user);
		const error = req.session.error;
		req.session.error = null;
		res.render('createUser', { username, error });
	}

	//Renders the resource 'games.hbs' after making a call to the services module to retrieve the Game Objects
	//that were found using the query name
	async function findGame(req, res) {
		const username = getUsername(req.user);
		const header = 'Find Game Result';
		const query = req.query.name;
		try {
			const games = await services.getGamesbyName(query);
			res.render('games', { username, header, query, games });
		} catch(err) {
			switch(err.name) {
				case 'BAD_REQUEST':
					res.status(400).render('games', { username, header, error: 'Syntax not understood by the server.' });
					break;
				case 'NOT_FOUND':
					res.status(404).render('games', { username, header, query, error: 'No games found for this query.' });
					break;
				case 'BAD_GATEWAY':
					res.status(502).render('games', { username, header, query, error: `Unable to find ${query}.` });
					break;
				default:
					res.status(err.code).render('games', { username, header, query, error: err.info });
					break;
			}
		}
	}

	//Renders the resource 'games.hbs' with the list of the most popular BGA games
	async function getPopular(req, res) {
		const username = getUsername(req.user);
		const header = 'Popular Games';
		try{
			const games = await services.getPopularGames();
			res.render('games', { username, header, games });
		} catch(err) {
			switch(err.name) {
				case 'NOT_FOUND':
					res.status(404).render('games', { username, header, error: 'No games found for this query.' });
					break;
				case 'BAD_GATEWAY':
					res.status(502).render('games', { username, header, query, error: `Unable to find ${query}.` });
					break;
				default:
					res.status(err.code).render('games', { username, header, error: err.info });
					break;
			}
		}
	}

	//Renders the resource 'game.hbs' with the game information to be shown to the user
	//Allows the user to add the game to one of the Group Objects created by them
	async function gameDetails(req, res) {
		const username = getUsername(req.user);
		const id = req.user ? req.user.id : undefined;
		const header = 'Game Details';
		const gameID = req.params.gameID;
		const error = req.session.error;
		req.session.error = null;
		const token = req.user ? getToken(req.user.token) : undefined;
		try {
			const game = await services.getGameDetails(gameID);
			const groups = await services.getUserGroupList(id, token);
			res.render('game', { username, header, game, groups, error });
		} catch(err) {
			switch(err.name) {
				case 'MISSING_PARAM':
					res.status(400).render('game', { username, header, error: 'No gameID provided.' });
					break;
				case 'NOT_FOUND':
					res.status(404).render('game', { username, header, error: `No game found with id ${gameID}.` });
					break;
				default:
					res.status(err.code).render('game', { username, header, error: err.info });
					break;
			}
		}
	}

	//Calls the services module so to add a new Group Object to the database with the requested parameters
	//Redirects to '/groups' after adding the new Group Object
	async function createGroup(req, res) {
		if(!req.isAuthenticated()){
			res.redirect('/login');
			return;
		}
		const name = req.body.name;
		const description = req.body.description;
		const userID = req.user ? req.user.id : undefined;
		try {
			const token = getToken(req.user.token);
			await services.createGroup(name, description, userID, token);
			res.redirect('/groups');
		} catch(err) {
			switch(err.name) {
				case 'UNAUTHORIZED':
					req.session.error = 'Login required.';
					res.redirect('/createGroup');
					break;
				case 'BAD_REQUEST':
					req.session.error = err.info;
					res.redirect('/createGroup');
					break;
				default:
					req.session.error = err.info;
					res.redirect('/createGroup');
					break;
			}
		}
	}

	//Renders the resource 'editGroup' and fill the inputs with the group info
	async function getEditGroupPage(req, res) {
		const username = req.user ? req.user.username : undefined;
		const id = req.user ? req.user.id : undefined;
		try {
			const token = getToken(req.user.token);
			const group = await services.getGroup(req.params.groupID, id, token);
			const groupID = group.id;
			const groupName = group.name;
			const groupDesc = group.description;
			res.render('editGroup', { username, groupID, groupName, groupDesc });
		} catch(err) {
			switch(err.name) {
				case 'NOT_FOUND':
					res.status(404).render('editGroup', { username, error: 'Group not found.' });
					break;
				default:
					res.status(err.code).render('editGroup', { username, error: err.info });
					break;
			}
		}
	}
	
	//Renders the resource 'groupList.hbs' after making a call to the services module to retrieve
	//information about all the Group Objects stored in the database
	async function listSavedGroups(req, res) {
		if(!req.isAuthenticated()){
			res.redirect('/login');
			return;
		}
		const username = getUsername(req.user);
		const id = req.user ? req.user.id : undefined;
		try {
			const token = getToken(req.user.token);
			const userGroups = await services.getUserGroupList(id, token);
			res.render('groupList', { username, userGroups });
		} catch(err) {
			switch(err.name) {
				case 'UNAUTHORIZED':
					res.status(401).render('groupList', { username, error: 'Login required.' });
					break;
				default:
					res.status(err.code).render('groupList', { username, error: err.info });
					break;
			}
		}
	}

	//Renders the resource 'group.hbs' with the data of the Group Object identified by groupID
	async function showGroupDetails(req, res) {
		if(!req.isAuthenticated()){
			res.redirect('/login');
			return;
		}
		const username = getUsername(req.user);
		const header = 'Group Details';
		const groupID = req.params.groupID;
		const id = req.user ? req.user.id : undefined;
		try {
			const token = getToken(req.user.token);
			const group = await services.getGroup(groupID, id, token);
			const creatorUser = await services.getUser(group.createdBy);
			res.render('group', { username, header, group, creatorUser});
		} catch(err) {
			switch(err.name) {
				case 'MISSING_PARAM':
					res.status(400).render('group', { username, header, error: 'No groupID provided.' });
					break;
				case 'UNAUTHORIZED':
					res.status(401).render('group', { username, header, error: 'Login required.' });
					break;
				case 'NOT_FOUND':
					res.status(404).render('group', { username, header, error: `No group found with id ${groupID}.` });
					break;
				default:
					res.status(err.code).render('group', { username, header, error: err.info });
					break;
			}
		}
	}

	//Adds a Game Object to a Group Object
	//Redirects to '/game/:gameID' so that the user can still look at the game information
	async function addGametoGroup(req, res) {
		if(!req.isAuthenticated()) {
			res.redirect('/login');
			return;
		}
		const groupID = req.params.groupID;
		const gameID = req.body.gameID;
		const gameName = req.body.gameName;
		const id = req.user ? req.user.id : undefined;
		try {
			const token = getToken(req.user.token);
			await services.addGametoGroup(id, groupID, gameID, gameName, token);
			res.redirect(`/game/${gameID}`);
		} catch(err) {
			switch(err.name) {
				case 'CONFLICT':
					req.session.error = 'Group already contains this game!';
					res.redirect('/game/' + gameID);
					break;
				case 'NOT_FOUND':
					req.session.error = 'Group does not exist.';
					res.redirect('/game/' + gameID);
					break;
				case 'UNAUTHORIZED':
					req.session.error = 'Login required.';
					res.redirect('/game/' + gameID);
					break;
				default:
					req.session.error = err.info;
					res.redirect('/game/' + gameID);
					break;
			}
		}
	}

	//Creates a new user on the DB and uses the credentials to authenticate the user
	async function createUser(req, res) {
		const password = req.body.password;
		const username = req.body.username;
		try {
			const user = await services.createUser(username, password);
			req.login({username: user.username, id: user.id, token: "Bearer " + user.token}, err => {
				if(err) {
					res.status(err.status).render('createUser', { error: 'Unable to sign in.' });
				}
				res.redirect('/');
			});
		} catch(err) {
			switch(err.name) {
				case 'NOT_FOUND':
					req.session.error = 'Unable to create user.';
					res.redirect('/signin');
					break;
				case 'BAD_REQUEST':
					req.session.error = err.info + '.';
					res.redirect('/signin');
					break;
				case 'CONFLICT':
					req.session.error = 'There is already a user with the name ' + err.info + '.';
					res.redirect('/signin');
					break;
				case 'BAD_GATEWAY':
					req.session.error = 'Unable to connect to the database for an unknown reason.';
					res.redirect('/signin');
					break;
				default:
					req.session.error = err.info;
					res.redirect('/signin');
					break;
			}
		}
	}

	//Obtains the user credentials from the inputs authenticates the user. If the 'remember me' checkbox is selected
	//saves an AuthToken on the cookie for future use
	async function userLogin(req, res) {
		const username = req.body.username;
		const password = req.body.password;
		try {
			const user = await services.loadUser(username, password);
			if (req.body.checkbox !== undefined){
				const token = await services.addAuthToken(user.id);
				res.cookie('AuthToken', token);
			}
			req.login({username: user.username, id: user.id, token: "Bearer " + user.token}, err => {
				if(err) {
					res.status(err.status).render('login', { error: 'Unable to login.' });
				}
				res.redirect('/');
			});
		} catch(err) {
			switch(err.name) {
				case 'CONFLICT':
					req.session.error = 'Username and password do not match.';
					res.redirect('/login');
					break;
				case 'NOT_FOUND':
					req.session.error =  'Unable to find user.';
					res.redirect('/login');
					break;
				case 'BAD_REQUEST':
					req.session.error = err.info;
					res.redirect('/login');
					break;
				case 'BAD_GATEWAY':
					req.session.error = 'Unable to connect to the database for an unknown reason.';
					res.redirect('/login');
					break;
				default:
					req.session.error = err.info;
					res.redirect('/login');
					break;
			}
		}
	}

	//Removes the authentication and deletes the AuthToken from the cookie
	async function userLogout(req, res) {
		await req.logout();
		if (req.cookies.AuthToken !== undefined) {
			try {
				const userID = await services.getAuthUser(req.cookies.AuthToken);
				await services.removeAuth(userID);
			} catch(err) {
				console.log(err);
			}
			res.clearCookie("AuthToken");
		}
		res.redirect('/');
	}

	//Filters the string containing the token
	function getToken(bearerToken) {
        try {
			return bearerToken.split(" ")[1];
		} catch(err) {
			throw errors.UNAUTHORIZED('Invalid token.');
		}
	}

	//Returns username
	function getUsername(user) {
		return user ? user.username : undefined;
	}

	//If there is one, uses the AuthToken, obtained from the cookie, to automatically log in and authenticate the user
	async function Authentication(req, res, next){
		if (req.isAuthenticated() || req.cookies.AuthToken === undefined) return next();
		try {
			const userID = await services.getAuthUser(req.cookies.AuthToken);
			const user = await services.getUser(userID);
			req.login({username: user.username, id: user.id, token: "Bearer " + user.token}, err => {
				return next(err);
			});
		} catch(err) {
			return next(err);
		}
	}

	//Routing
	const router = express.Router();

	router.use(express.urlencoded({ extended: true }));
	router.use(express.json());
	router.use(Authentication)
	
	//Homepage
	router.get('/', getHomepage);
	
	//Search page
	router.get('/search', getSearchPage);

	//Create Group page
	router.get('/createGroup', getCreateGroupPage);

	//Shows login page
	router.get('/login', getLogInPage);

	//Create User page
	router.get('/signin', getSignUpPage);
	
	//Finds Game Objects in BGA
	router.get('/games', findGame);

	//Shows popular games
	router.get('/games/popular', getPopular);

	//Shows game Object details
	router.get('/game/:gameID', gameDetails);

	//Adds new Group Object
	router.post('/groups', createGroup);

	//Edit Group page
	router.get('/editGroupPage/:groupID', getEditGroupPage);

	//Lists all Group Objects
	router.get('/groups', listSavedGroups);
	
	//Shows Group Object
	router.get('/group/:groupID', showGroupDetails);

	//Adds a Game Object to a Group Object
	router.post('/groups/:groupID/games', addGametoGroup);

	//Creates new User
	router.post('/user', createUser);

	//Logs user in
	router.post('/login', userLogin);

	//Logs user out
	router.post('/logout', userLogout);
	
	return router;
};
