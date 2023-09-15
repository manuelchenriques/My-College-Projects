'use strict';

const fetch   = require('node-fetch');
const request = require('supertest');

const config = require('../../../borga-config');
const server = require('../../../borga-server');

const es_spec = {
	url: config.devl_es_url,
	prefix: 'test'
};

const data_mem = require('../../../borga-db')(es_spec);

test('Confirm database is running', async () => {
	const response = await fetch(`${es_spec.url}_cat/health`);
	expect(response.status).toBe(200);
});

describe('Integration tests', () => {
	
	const app = server(data_mem);

	afterAll(async () => {
		await fetch(
			`${es_spec.url}${es_spec.prefix}_${config.guest.id}_groups`,
			{ method: 'DELETE' }
		);
	});

	test('Adding guest user', async () => {
		const userID = await data_mem.addUser(config.guest).catch(err => console.log('Error adding guest user. ' + err.message + '.'));

		expect(userID).toBeTruthy();
		expect(userID).toEqual(config.guest.id);
	});

	test('Adding jonas user', async () => {
    	const userID = await data_mem.addUser(config.jonas).catch(err => console.log('Error adding jonas user. ' + err.message + '.'));

		expect(userID).toBeTruthy();
		expect(userID).toEqual(config.jonas.id);
	});
	
	test('Get empty group list', async () => {
		const response = await request(app)
			.get('/api/global/groups')
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.expect('Content-Type', /json/)
			.expect(200); // or see below
					
		expect(response.status).toBe(200); // or see above
		expect(response.body).not.toBeTruthy();
		expect(response.body).toEqual('');
	});
		
	test('Add group', async () => {
		const addResponse = await request(app)
			.post('/api/global/groups')
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('Accept', 'application/json')
			.send({ name: 'Group', description: 'ABC', createdBy: '1' })
			.expect('Content-Type', /json/)
			.expect(200);
		
		expect(addResponse.body).toBeTruthy();
		expect(addResponse.body.createdBy).toEqual('1');

		const listResponse = await request(app)
			.get('/api/global/groups')
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.expect('Content-Type', /json/)
			.expect(200);

		expect(listResponse.body).toBeTruthy();
		expect(listResponse.body).toHaveLength(1);   //acknowledging the default value
		expect(listResponse.body[0].createdBy).toEqual('1');
	});

	test('Get group', async () => {
		const listResponse = await request(app)
			.get('/api/global/groups')
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.expect('Content-Type', /json/)
			.expect(200);

		expect(listResponse.body).toBeTruthy();
		expect(listResponse.body).toHaveLength(1);

		const groupResponse = await request(app)
			.get(`/api/global/groups/${listResponse.body[0].id}`)
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.expect('Content-Type', /json/)
			.expect(200);

		expect(groupResponse.body).toBeTruthy();
		expect(groupResponse.body.id).toEqual(listResponse.body[0].id);
	});

    test('Add game to group', async () => {
		const game = { gameID: 'TAAifFP590', gameName: 'Root' };

        const getResponse = await request(app)
			.get('/api/global/groups')
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.expect('Content-Type', /json/)
			.expect(200);

        expect(getResponse.body).toBeTruthy();
        expect(getResponse.body).toHaveLength(1);

		const addResponse = await request(app)
			.post(`/api/global/groups/${getResponse.body[0].id}/games`)
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.send({ gameID: game.gameID, gameName: game.gameName })
			.expect('Content-Type', /json/)
			.expect(200);
		
		expect(addResponse.body).toBeTruthy();
		expect(addResponse.body.gameID).toEqual(game.gameID);

		const listResponse = await request(app)
			.get(`/api/global/groups/${getResponse.body[0].id}`)
			.set('Authorization', `Bearer ${config.guest.token}`)
			.set('userID', config.guest.id)
			.set('Accept', 'application/json')
			.expect('Content-Type', /json/)
			.expect(200);

		expect(listResponse.body).toBeTruthy();
		expect(listResponse.body.games[0].gameID).toEqual(game.gameID);
	});
});
