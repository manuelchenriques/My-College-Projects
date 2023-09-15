//Module that communicates with external database and returns user/group data
'use strict'

const errors = require('./app-errors');
const fetch = require('node-fetch');

module.exports = function(es_spec) {

    //elasticsearch URL
    const dbUrl = `${es_spec.url}`;

    //elasticsearch indices for each object saved
    const groupsUrl = createdBy => `${dbUrl}${es_spec.prefix}_${createdBy}_groups`;
    const usersUrl = `${dbUrl}${es_spec.prefix}_users`;
    const tokensUrl = `${dbUrl}${es_spec.prefix}_tokens`;


    //Adds a new entry to the external database containing an userID and his AuthToken
    async function addAuthToken(userID, token) {
        const entry = { id : userID, token : token };
        try {
            const response = await fetch(`${tokensUrl}/_doc/${userID}?refresh=wait_for`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(entry)
                });
            const answer = await response.json();
            return answer._source;
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        };
    }

    //Deletes the database entry containing the AuthToken of the user
    async function deleteAuthToken(userID) {
        try {
            const response = await fetch(`${tokensUrl}/_doc/${userID}?refresh=wait_for`,
                {
                    method: 'DELETE'
                });
            if (response.status === 200) {
                const answer = response.json();
                return answer._source;
            }
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        };
        throw errors.NOT_FOUND(userID);
    }

    //Verifies if the given token is associated with any user
    async function getTokenUser(token) {
        try {
            const response = await fetch(`${tokensUrl}/_search?q=token:${token}`)
            if (response.status === 200){
                const answer = await response.json();
                const hits = answer.hits.hits;
                return hits[0]._id;
            }
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        };
        throw errors.NOT_FOUND(token);
    }

    //Checks if the user ID is valid
    async function isUser(id) {
        if(!(await getUser(id))) {
            throw errors.UNAUTHORIZED(id);
        }
    }

    //Adds a User Object to the external database
    async function addUser(user) {
        try {
            const check = await fetch(`${usersUrl}/_doc/${user.id}`);  //Checking so not to add the default users again
            if(check.status === 200) return user.id;

            await fetch(`${usersUrl}/_doc/${user.id}?refresh=wait_for`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(user)
                });
            return user.id;
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        }
    }

    //Retrieves user information from the external database
    async function loadUser(username, password) {
        try {
            const response = await fetch(`${usersUrl}/_search?q=username:${username}`);
            if (response.status === 200) {
                const answer = await response.json();
                const hits = answer.hits.hits;
                if(hits.length > 1) throw errors.CONFLICT('More than one user share a username.');
                const user = hits[0];
                if(!(user === undefined)) {
                    if(user._source.password != password) throw errors.CONFLICT('Password not recognized.');
                    return user._source;
                }
            }
        } catch (err) {
            if(err.name == 'CONFLICT') throw errors.CONFLICT(err.info);
            throw errors.BAD_GATEWAY(err);
        }
        throw errors.NOT_FOUND(username);
    }

    //Get user by Id from database
    async function getUser(userID) {
        try {
            const response = await fetch(`${usersUrl}/_doc/${userID}`);
            if (response.status === 200) {
                const answer = await response.json();
                return answer._source;
            }
        } catch (err) {
            throw errors.BAD_GATEWAY(err);
        }
        throw errors.NOT_FOUND(userID);
    }

    //Retrieves the user Object through it's token
    async function tokenToUsername(token) {
        try {
            const response = await fetch(`${usersUrl}/_search?q=token:${token}`);
            if (response.status === 200) {
                const answer = await response.json();
                const user = answer.hits.hits[0];
                return user._source;
            }
        } catch (err) {
            throw errors.BAD_GATEWAY(err);
        }
        throw errors.NOT_FOUND(token);
    }

    //Adds a Group Object to the external database
    async function addGroup(group) {
        isUser(group.createdBy);
        try {
            const check = await fetch(`${groupsUrl(group.createdBy)}/_doc/${group.id}`);    //Checking so not to add the default groups again
            if(check.status === 200) return group.id;

            await fetch(`${groupsUrl(group.createdBy)}/_doc/${group.id}?refresh=wait_for`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(group)
            });
            return group.id;
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        }
    }

    //Removes a Group Object from the external Database
    async function removeGroup(createdBy, groupID) {
        isUser(createdBy);
        try {
			const response = await fetch(`${groupsUrl(createdBy)}/_doc/${groupID}?refresh=wait_for`,
                {
                    method: 'DELETE'
                });
			if(response.status === 200) return groupID;
		} catch(err) {
			throw errors.BAD_GATEWAY(err);
		}
		throw errors.NOT_FOUND(groupID);
    }

    //Retrieves group information from the database through it's group ID
    async function getGroup(createdBy, groupID) {
        isUser(createdBy);
        try {
			const response = await fetch(`${groupsUrl(createdBy)}/_doc/${groupID}`);
			if(response.status === 200) {
				const answer = await response.json();
				return answer._source;
			}
		} catch(err) {
			throw errors.BAD_GATEWAY(err);
		}
		throw errors.NOT_FOUND("Group " + groupID);
    }

    //Retrieves all information about all groups registered in the database
    async function getGroupList(createdBy) {
        isUser(createdBy);
        try {
            const response = await fetch(`${groupsUrl(createdBy)}/_search`);
            if(response.status === 404) {
                return [];
            }
            const answer = await response.json();
            const hits = answer.hits.hits;
            const groups = hits.map(hit => hit._source);
            return groups;
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        }
    }

    //Allows to change a Group Object's name and description
    async function editGroup(group) {
        isUser(group.createdBy);
        try {
            const response = await fetch(`${groupsUrl(group.createdBy)}/_doc/${group.id}?refresh=wait_for`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(group)
            });
            if(response.status === 200) return group.id;
        } catch(err) {
            throw errors.BAD_GATEWAY(err);
        }
        throw errors.NOT_FOUND(group.id);
    }

    //Adds a Game Object to a Group Object identified by a group ID
    async function addGameToGroup(createdBy, groupID, gameObj) {
        isUser(createdBy);
        const update = {
            script: {
                source: 'ctx._source.games.add(params.gameObj)',
                lang: "painless",
                params: { gameObj }
            }
        };
        const checkGame = {
            'query': {
                'bool': {
                    'filter': {
                        'match': {'games.gameID': gameObj.gameID}   
                    } 
                }
            }
        };
        try {
            const check = await fetch(`${groupsUrl(createdBy)}/_search`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(checkGame)
            });
            if(check.status === 200) {
                const answer = await check.json();
                const hits = answer.hits.hits;
                if(hits.length > 0) throw errors.CONFLICT('That game already exists. ' + gameObj.gameID);
            }

            const response = await fetch(`${groupsUrl(createdBy)}/_update/${groupID}`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(update)
            });
            if(response.status === 200) return gameObj.gameID;
        } catch(err) {
            if(err.name == 'CONFLICT') throw errors.CONFLICT(err.info);
            throw errors.BAD_GATEWAY(err);
        }
        throw errors.NOT_FOUND("Group " + groupID);
    }

    //Removes a Game Object from a Group Object identified by a group ID
    async function removeGameFromGroup(createdBy, groupID, gameID) {
        isUser(createdBy);
        const remove = {
            script: {
                source: "for(int i = 0; i < ctx._source.games.length; i++) {\n" +
                "if(ctx._source.games[i].gameID == params.gameID) {\n" +
                "ctx._source.games.remove(i);\n" +
                "}\n" +
                "}\n",
                lang: "painless",
                params: { gameID }
            }
        };
        const checkGame = {
            'query': {
                'bool': {
                    'filter': {
                        'match': {'games.gameID': gameID}   
                    } 
                }
            }
        };
        try {
            const check = await fetch(`${groupsUrl(createdBy)}/_search`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(checkGame)
            });
            if(check.status === 200) {
                const answer = await check.json();
                const hits = answer.hits.hits;
                if(hits.length == 0) throw errors.NOT_FOUND('The game does not exist. ' + gameID);
            }

            const response = await fetch(`${groupsUrl(createdBy)}/_update/${groupID}?refresh=wait_for`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(remove)
            });
            if (response.status === 200) return gameID;
        } catch(err) {
            if(err.name == 'NOT_FOUND') throw errors.NOT_FOUND(err.info);
            throw errors.BAD_GATEWAY(err);
        }
    }

    //Checks a user with said username
    async function getUserByName(username) {
        try {
            const response = await fetch(`${usersUrl}/_search?q=username:${username}`);
            if (response.status === 200) {
                const answer = await response.json();
                if(answer.hits.hits.length > 0) throw errors.CONFLICT(username);
            }
        } catch (err) {
            if(err.name == 'CONFLICT') throw errors.CONFLICT(err.info);
            throw errors.BAD_GATEWAY(err);
        }
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
};