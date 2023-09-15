//Script to delete games in 'group.hbs'
'use strict'

//Assigns an event to the buttons to delete a game
function deleteGame() {
    const RemoveButton = document.querySelectorAll('.btn.btn-dark.mb-2.btn-sm');
    RemoveButton.forEach(RemoveButton => {
        RemoveButton.onclick = onGameDelete;
    });
}

//Event handling of remove game button
async function onGameDelete() {
    const filterInfo = this.id.split(" ");
    const groupID = filterInfo[0];
    const gameID = filterInfo[1];
    await apiDeleteGame(groupID, gameID);
}

//Removes the group
async function apiDeleteGame(groupID, gameID) {
    try {
        const rGame = await fetch('/api/global/groups/' + groupID + '/games',
        {
            method: 'DELETE',
            headers: {
                'Content-Type':'application/json',
                'gameID' : gameID
            }
        });
        if (rGame.status === 200) {
            window.location.replace("/groups");
            return;
        }
        throw new Error("Unable to delete game " + gameID + " from group " + groupID);
    } catch(err) {
        throw err;
    }
}