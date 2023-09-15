//Logic of each of the API's functionalities
'use strict';

const errors = require('./app-errors');
const crypto = require('crypto');

module.exports = function(data_int, data_boardGames) {

    //Creates a new User Object to store the new User Object data
    async function createUser(username, password) {
        if(!username) throw errors.BAD_REQUEST('Invalid username.');
        if(!password) throw errors.BAD_REQUEST('Invalid password.');
        await data_int.getUserByName(username);

        const user = {};
        user.id = crypto.randomUUID();
        user.username = username;
        user.password = password;
        user.token = crypto.randomUUID();
        await data_int.addUser(user);
        return user;
    }

    //Returns the User Object identified by the username and password
    async function loadUser(username, password) {
        if(!username) throw errors.BAD_REQUEST('Invalid username.');
        if(!password) throw errors.BAD_REQUEST('Invalid password.');

        return await data_int.loadUser(username, password);
    }

    //Returns the User Object identified by the UserID
    async function getUser(userID) {
        if(!userID) throw errors.INTERNAL_SERVER_ERROR('No user ID.');

        return await data_int.getUser(userID);
    }

    //Returns a list of all the games from BGA API named [query]
    async function getGamesbyName(query) {
        if(!query) throw errors.BAD_REQUEST(query);

        const gameList = await data_boardGames.findGameByName(query);

        if(!gameList.error) return gameList.games.map(game => {return {gameID: game.id, gameName: game.name, gameUrl: game.url, imgUrl: game.image_url}});
        else throw errors.INTERNAL_SERVER_ERROR(gameList.error.message);
    }

    //Returns a list of the most popular games from the BGA API
    async function getPopularGames() {
        const gameList = await data_boardGames.getPopularGames();

        if(!gameList.error) return gameList.games.map(game => {return {gameID: game.id, gameName: game.name, gameUrl: game.url, imgUrl: game.image_url}});
        else throw errors.INTERNAL_SERVER_ERROR(gameList.error.message);
    }

    //Creates a new Group Object and calls borga-data-mem to store the new Group Object data
    async function createGroup(name, description, creatorID, token) {
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');
        if(!name) throw errors.BAD_REQUEST('Invalid name.');
        if(!(await data_int.getUser(creatorID))) throw errors.BAD_REQUEST('Invalid user.');

        const group = {};
        group.id = crypto.randomUUID();
        group.name = name;
        group.description = description;
        group.games = [];
        group.createdBy = creatorID;
        await data_int.addGroup(group);
        return group;
    }

    //Returns an array of Group Objects containing all the groups created by the user
    async function getUserGroupList(createdBy, token) {
        if(!token) return undefined;
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');
        const userGroups = await data_int.getGroupList(createdBy);
        return userGroups.length === 0 ? undefined : userGroups;
    }

    //Retrieves the Group Object identified by groupID and replaces the name and description
    async function editGroup(groupID, name, description, createdBy, token) {
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');

        const group = await data_int.getGroup(createdBy, groupID);
        if(group === undefined) throw errors.NOT_FOUND(groupID);
        if(name) group.name = name;
        if(description) group.description = description;
        await data_int.editGroup(group);
        return group;
    } 

    //Returns the Group Object identified by groupID
    async function getGroup(groupID, createdBy, token) {
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');
        return await data_int.getGroup(createdBy, groupID);
    }

    //Removes the Group Object identified by groupID
    async function deleteGroup(groupID, createdBy, token) {
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');
        return { groupID: await data_int.removeGroup(createdBy, groupID) };
    }

    //Adds a gameID to the games array contained inside the Group Object identified by groupID
    async function addGametoGroup(createdBy, groupID, gameID, gameName, token) {
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');
        return { gameID: await data_int.addGameToGroup(createdBy, groupID, { gameID: gameID, gameName: gameName })};
    }
    
    //Removes a gameID from the games array contained inside the Group Object identified by groupID
    async function removeGamefromGroup(groupID, gameID, createdBy, token) {
        if(await checkToken(token)) throw errors.UNAUTHORIZED('No token.');
        return { gameID: await data_int.removeGameFromGroup(createdBy, groupID, gameID) };
    }

    //Returns the resource with the details of the selected game
    async function getGameDetails(gameID) {
        const res = await data_boardGames.findGameByID(gameID);
        return {
            gameName: res.name,
            gameID: res.id,
            description: res.description,
            gameUrl: res.url,
            imgUrl: res.image_url,
            mechanics: await getMechanicNames(res.mechanics, await data_boardGames.getMechanics()),
            categories: await getCategoryNames(res.categories, await data_boardGames.getCategories())
        }
    }

    //Returns an array containing the names of the mechanics whose ids were saved on 'ids'
    async function getMechanicNames(ids, names) {
        return await ids.map(current => compareMechAndCat(current, names.mechanics).name);
    }

    //Returns an array containing the names of the categories whose ids were saved on 'ids'
    async function getCategoryNames(ids, names) {
        return await ids.map(current => compareMechAndCat(current, names.categories).name);
    }

    //Returns the first element that has the same id as either the Game Object mechanic or category
    function compareMechAndCat(obj, names) {
        return names.find(element => element.id === obj.id);
    }

    //Checks if token is valid
    async function checkToken(token) {
        return await data_int.tokenToUsername(token) ? false : true;
    }

    //Saves an entry on the database containing an AuthToken and the associated user
    async function addAuthToken(userID){
        const token = crypto.randomUUID();
        await data_int.addAuthToken(userID, token);
        return token;
    }

    //Deletes from the database the AuthToken associated with the user
    async function removeAuth(userID){
        return await data_int.deleteAuthToken(userID);
    }

    //Returns the userID associated with the token
    async function getAuthUser(token){
        return await data_int.getTokenUser(token);
    }

    return {
        createUser,
        loadUser,
        getUser,
        getGamesbyName,
        getPopularGames,
        createGroup,
        getUserGroupList,
        editGroup,
        getGroup,
        deleteGroup,
        addGametoGroup,
        removeGamefromGroup,
        getGameDetails,
        addAuthToken,
        removeAuth,
        getAuthUser
   };
};
