//Script to update groups in 'groupList.hbs'
'use strict';

//Assigns an event to the buttons to update a group
function updateGroup() {
    const updateButtons = document.querySelectorAll('.btn.btn-lg.btn-dark.align-content-center');
    updateButtons.forEach(updateButtons => {
        updateButtons.onclick = onUpdateGroup;
    });
}

//Event handling of edit group button
async function onUpdateGroup() {
    const groupID = this.id.substr(10);

    try {
        const reqBody = {name: document.querySelector('#groupName').value, description: document.querySelector('#groupDesc').value};
        await apiUpdateGroup(groupID, reqBody);
    }catch (err){
        alert(err);
    }
}

//Updates group
async function apiUpdateGroup(groupID, reqBody) {
    try {
        const updateGroup = await fetch('/api/global/groups/' + groupID, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(reqBody)
        });
    
        if(updateGroup.status === 200) {
            window.location.replace("/groups");
            return;
        }
        throw new Error("Unable to update group with id " + groupID + '\n' + updateGroup.status + ' ' + updateGroup.statusText);
    } catch(err) {
        throw err;
    }
}