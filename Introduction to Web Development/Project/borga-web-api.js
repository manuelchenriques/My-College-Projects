//Implements the API routes one by one
'use strict';

const express = require('express');
const openAPIUI = require('swagger-ui-express');
const openAPIFile = require('./documents/borga-api-spec.json');
const errors = require('./app-errors');

module.exports = (services) => {
    //Handles errors
    function onError(req, res, err) {
        //If error is undefined then it should return 500 Internal Server Error
        if(err.code === undefined) {
            err = errors.INTERNAL_SERVER_ERROR(req.code);
        }
        //Returns the error in the response
        res.status(err.code);
        res.json(err);
	}

    //Retrieves a username and password from the request body and creates a new User Object by calling borga-services. Response contains the new User Object
    async function createUser(req, res) {
        try {
            const newUser = await services.createUser(req.body.username, req.body.password);
            res.json(newUser);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Sends a response contaning the User Object identified by UserID, which was retrived from the request
    async function getUser(req, res) {
        try {
            const user = await services.getUser(req.get('userID'));
            res.json(user);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Retrieves name from the request and uses borga-services to get a list of all the Game Objects with that name
    async function searchGames(req, res) {
        try {
            const games = await services.getGamesbyName(req.query.name);
            res.json(games);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Response contains a list containing the most popular games from the BGA API
    async function getPopularGames(req, res) {
        try {
            const popularGames = await services.getPopularGames();
            res.json(popularGames);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Response contains a Game Object corresponding to the ID parameter from the BGA API
    async function getGameByID(req, res) {
        try {
            const game = await services.getGameDetails(req.params.gameID);
            res.json(game);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Retrieves the name, description and createdBy (userID) from the request body and creates a new Group Object by calling borga-services. Response contains the new Group Object
    async function createGroup(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const newGroup = await services.createGroup(req.body.name, req.body.description, req.body.createdBy, authorization || getToken(req.user.token));
            res.json(newGroup);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Response contains a list containg all the Group Objects stored by the API
    async function getGroups(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const groupList = await services.getUserGroupList(req.get('userID') || req.user.id, authorization || getToken(req.user.token));
            res.json(groupList);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Updates the Group Object's name amd description, identified by the parameter groupID. The response contains the updated Group Object
    async function updateGroup(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const updatedGroup = await services.editGroup(req.params.groupID, req.body.name, req.body.description, req.get('userID') || req.user.id, authorization || getToken(req.user.token));
            res.json(updatedGroup);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Sends a response contaning the Group Object identified by the parameter groupID, which was retrived from the request
    async function getGroup(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const group = await services.getGroup(req.params.groupID, req.get('userID') || req.user.id, authorization || getToken(req.user.token));
            res.json(group);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Removes a Group Object from memory and sends a response containing the deleted Group Object
    async function deleteGroup(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const deletedGroupID = await services.deleteGroup(req.params.groupID, req.get('userID') || req.user.id, authorization || getToken(req.user.token));
            res.json(deletedGroupID);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Adds the gameID to the Game Object array contained in the Group Object. The Group Object and Game Object ID are retrived from the request. Response contains the Game Object's ID
    async function addGameToGroup(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const addedGameID = await services.addGametoGroup(req.get('userID') || req.user.id, req.params.groupID, req.body.gameID, req.body.gameName, authorization || getToken(req.user.token));
            res.json(addedGameID);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Removes the gameID to the Game Object array contained in the Group Object. The Group Object and Game Object ID are retrived from the request. Response contains the Game Object's ID
    async function removeGameFromGroup(req, res) {
        try {
            var authorization;
            if(req.get('Authorization')) {
                authorization = getToken(req.get('Authorization'));
            }
            const removedGameID = await services.removeGamefromGroup(req.params.groupID, req.get('gameID'), req.get('userID') || req.user.id, authorization || getToken(req.user.token));
            res.json(removedGameID);
        } catch(err) {
            onError(req, res, err);
        }
    }

    //Filters the string containing the token.
    function getToken(bearerToken) {
        try {
            return bearerToken.split(" ")[1];
        } catch(err) {
            throw err;
        }
    }

    const router = express.Router();

    router.use('/documents', openAPIUI.serve);
    router.get('/documents', openAPIUI.setup(openAPIFile));

    router.use(express.json());

    //Resource: /user
    router.post('/user', createUser);
    router.get('/user', getUser);

    //Resource: /games
    router.get('/games', searchGames);

    //Resource: /games/popular
    router.get('/games/popular', getPopularGames);

    //Resource: /games/:gameID
    router.get('/games/:gameID', getGameByID);

    //Resource: /groups
    router.post('/groups', createGroup);
    router.get('/groups', getGroups);

    //Resource: /groups/:groupID
    router.put('/groups/:groupID', updateGroup);
    router.get('/groups/:groupID', getGroup);
    router.delete('/groups/:groupID', deleteGroup);

    //Resource: /groups/:groupID/games
    router.post('/groups/:groupID/games', addGameToGroup);
    router.delete('/groups/:groupID/games', removeGameFromGroup);

    return router;
};
