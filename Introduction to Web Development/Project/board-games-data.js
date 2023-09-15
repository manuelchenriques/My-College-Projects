//Access to the Board Game Atlas API
'use strict';

const errors = require('./app-errors');
const fetch = require('node-fetch');

//Board Game Atlas API acess ID nad URL
const CLIENT_ID = process.env.ATLAS_CLIENT_ID;
const URL = 'https://api.boardgameatlas.com/api';

//Returns a list of all the Game Objects from BGA API named [query]
function findGameByName(query) {
    const searchURI = URL + '/search?name=' + query + `&client_id=${CLIENT_ID}`;

    return fetch(searchURI)
    .then(res => res.json())
    .catch(processError);
}

//Returns the Game Object identified by the ID present in the query
function findGameByID(query) {
    const searchURI = URL + '/search?ids=' + query + `&client_id=${CLIENT_ID}`;

    return fetch(searchURI)
    .then(res => res.json())
    .then(res => res.games[0])
    .catch(processError);
}

//Returns a list of the most popular games from the BGA API
function getPopularGames() {
    const searchURI = URL + `/search?order_by=popular&client_id=${CLIENT_ID}`;
        
    return fetch(searchURI)
    .then(res => res.json())
    .catch(processError);
}

//Returns an array with the names of all the categories ids present in the Game Object by calling the BGA API 
//and retrieving all Game Category Objects
async function getCategories() {
    let names = await fetch(`${URL}/game/categories?client_id=${CLIENT_ID}`);
    names = await names.json();

    return names;
}

//Returns an array with the names of all the mechanics ids present in the Game Object by calling the BGA API 
//and retrieving all Game Mechanic Objects
async function getMechanics() {
    let names = await fetch(`${URL}/game/mechanics?client_id=${CLIENT_ID}`);
    names = await names.json();
    
    return names;
}

//Processes the error from fetching with the BGA API
function processError(err) {
    throw errors.BAD_GATEWAY(err);
}

module.exports = {
    findGameByName,
    findGameByID,
    getPopularGames,
    getCategories,
    getMechanics
};
