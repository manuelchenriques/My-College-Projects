//Script to delete groups in 'groupList.hbs'
'use strict';

//Assigns an event to the buttons to delete a group
function deleteGroup() {
    const deleteButtons = document.querySelectorAll('.btn.btn-dark.btn-sm.mb-2.delete');
    deleteButtons.forEach(deleteButton => {
        deleteButton.onclick = onDeleteGroup;
    });
    return;
}

//Event handling of delete group button
async function onDeleteGroup() {
    const deleteButton = this.id.substr(9);
    try {
        await apiDeleteGroup(deleteButton);
        deleteTableEntry(deleteButton);
    } catch(err) {
        alert(err);
    }
}

//Deletes table entry without the need of refreshing the web page
function deleteTableEntry(groupID) {
    const tableEntryId = '#entry-' + groupID;
	const tableEntry = document.querySelector(tableEntryId);
	tableEntry.parentNode.removeChild(tableEntry);
    const table = document.querySelector('#table');
    if (table.rows.length === 1) location.reload();
}

//Deletes the group
async function apiDeleteGroup(groupID) {
    try {
        const delGroup = await fetch('/api/global/groups/' + groupID, { method: 'DELETE' });
        if(delGroup.status === 200) return;
        throw new Error("Unable to delete group with id " + groupID + '\n' + delGroup.status + ' ' + delGroup.statusText);
    } catch(err) {
        throw err;
    }
}