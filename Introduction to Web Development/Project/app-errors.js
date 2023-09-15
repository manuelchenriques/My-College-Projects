//A list of errors throw by the API
'use strict';

function buildErrorList() {
	const errors = {};

	function addError(code, name, message) {
		errors[name] = info => {
			return { code, name, message, info };
		};
	}

	//Errors type 400
	addError(400, 'BAD_REQUEST', 'Request not understood by the server due to invalid syntax');
	addError(401, 'UNAUTHORIZED', 'Client not authenticated');
	addError(404, 'NOT_FOUND', 'The item does not exist');
	addError(409, 'CONFLICT', 'Request conflicts with the current state of the server'); 

	//Errors type 500
	addError(500, 'INTERNAL_SERVER_ERROR', 'The server encountered an unexpected condition that prevented it from fulfilling the request');
	addError(502, 'BAD_GATEWAY', 'Got an invalid response while working on the Gateway to get a response');
	
	
	return errors;
}

const errorList = buildErrorList();

module.exports = errorList;
