//Accesses user and group data
'use strict';

const errors = require('./app-errors');

//Local list used to save the Users, Groups and Tokens
const userList = {};
const groupList = {};
const tokenList = {};

module.exports = () => {
    //Adds a new entry to the external database containing an userID and his AuthToken
    async function addAuthToken(userID, token) {
        const entry = { id : userID, token : token };
        
        tokenList[entry.id] = entry;
        return entry.id;
    }

    //Deletes the database entry containing the AuthToken of the user
    async function deleteAuthToken(userID) {
        if(!tokenList[userID]) throw errors.NOT_FOUND(userID);

        delete tokenList[userID];
        return userID;
    }

    //Verifies if the given token is associated with any user
    async function getTokenUser(token) {
        var entryObj;
        Object.values(tokenList).forEach(entry => {
            if(entry.token == token) {
                entryObj = entry;
                return;
            }
        });
        if(!entryObj) throw errors.NOT_FOUND(token);

        return entryObj.id;
    }

    //Checks if the user ID is valid
    async function isUser(id) {
        if(!(await getUser(id))) {
            throw errors.UNAUTHORIZED(id);
        }
    }

    //Adds a new user and token to the userList and tokens, respectively
    async function addUser(user) {
        userList[user.id] = user;
        return user.id;
    }

    //Retrieves user information from the local database
    async function loadUser(username, password) {
        var userObj;
        Object.values(userList).forEach(user => {
            if(user.username == username) {
                if(userObj) throw errors.CONFLICT('More than one user share a username.');
                if(user.password == password) userObj = user;
                else throw errors.CONFLICT('Password not recognized.');
            }
        });
        if(!userObj) throw errors.NOT_FOUND(username);

        return userObj;
    }

    //Returns the user identified by the UserID
    async function getUser(userID) {
        const userObj = userList[userID];
        if(!userObj) throw errors.NOT_FOUND(userID);

        return userObj;
    }

    //Returns the user id corresponding to the token given as a parameter, if there is any. Used to verify the token authenticity
    async function tokenToUsername(token) {
        var userObj;
        Object.values(userList).forEach(user => {
            if(user.token == token) {
                userObj = user;
                return;
            }
        });
        if(!userObj) return userObj;

        return userObj.token;
    }

    //Saves the new Group Object locally
    async function addGroup(group) {
        isUser(group.createdBy);

        groupList[group.id] = group;
        return group.id;
    }

    //Removes the Group Object identified by groupID from the local memory
    async function removeGroup(createdBy, groupID) {
        isUser(createdBy);
        if(!groupList[groupID]) throw errors.NOT_FOUND(groupID);

        delete groupList[groupID];
        return groupID;
    }

    //Returns the Group Object identified by groupID
    async function getGroup(createdBy, groupID) {
        isUser(createdBy);
        const groupObj = groupList[groupID];
        if(!groupObj) throw errors.NOT_FOUND(groupID);
        
        return groupObj;
    }

    //Returns a list containing all the Group Objects saved locally of a user
    async function getGroupList(createdBy) {
        isUser(createdBy);
        return Object.values(groupList).filter(group => group.createdBy == createdBy);
    }

    //Replaces an existing Group Object
    async function editGroup(group) {
        isUser(group.createdBy);
        if(!groupList[group.id]) throw errors.NOT_FOUND(group.id);

        groupList[group.id] = group;
        return group.id;
    }

    //Adds a gameID to the games array contained inside the Group Object identified by groupID
    async function addGameToGroup(createdBy, groupID, gameObj) {
        isUser(createdBy);
        if(!groupList[groupID]) throw errors.NOT_FOUND(groupID);
        Object.values(groupList[groupID].games).forEach(game => {
            if(game.gameID == gameObj.gameID) throw errors.CONFLICT('That game already exists. ' + gameObj.gameID);
        });

        groupList[groupID].games.push(gameObj);
        return gameObj.gameID;
    }

    //Removes a gameID from the games array contained inside the Group Object identified by groupID
    async function removeGameFromGroup(createdBy, groupID, gameID) {
        isUser(createdBy);
        if(!groupList[groupID]) throw errors.NOT_FOUND(groupID);
        var gameObj;
        Object.values(groupList[groupID].games).forEach(game => {
            if(game.gameID == gameID) gameObj = game;
        });
        if(!gameObj) throw errors.NOT_FOUND('The game does not exist. ' + gameID);
        
        groupList[groupID].games = groupList[groupID].games.filter(element => element.gameID != gameID);
        return gameID;
    }

    //Checks if there is a user with said username
    async function getUserByName(username) {
        Object.values(userList).forEach(user => {
            if(user.username == username) throw errors.CONFLICT(username);
        });
    }

    return {
        addAuthToken,
        deleteAuthToken,
        getTokenUser,
        addUser,
        loadUser,
        getUser,
        tokenToUsername,
        addGroup,
        removeGroup,
        getGroup,
        getGroupList,
        editGroup,
        addGameToGroup,
        removeGameFromGroup,
        getUserByName
    };
}

