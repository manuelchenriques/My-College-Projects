'use strict';

const config = require('../../../borga-config');
const services_builder = require('../../../borga-services');
const data_mem = require('../../../borga-data-mem')();
const mock_data_ext = require('../__mocks__/app-data-ext');
const services = services_builder(data_mem, mock_data_ext);

data_mem.addUser(config.guest);
data_mem.addGroup(config.guestGroup);

describe('Search tests', () => {
	test('search board game without a query', async () => {
		try {
			const result = await services.getGamesbyName(undefined);
			//Added expect to test in case the code runs without errors which shouldn't happen
			expect(result).toBeUndefined();
		} catch (err) {expect(err.name).toEqual('BAD_REQUEST');}
	});
});

describe('CreateGroup test', () => {
	test('Create group without token', async () => {
		try {
			const result = await services.createGroup('Fav', 'My favorite games', '1');
			expect(result).toBeUndefined();
		} catch (err) {expect(err.name).toEqual('UNAUTHORIZED');}
	})

	test('Create group without name', async () => {
		try {
			const result = await services.createGroup('', 'My favorite games', '1', 'cfd03665-781b-4b40-9baa-171688095b59');
			expect(result).toBeUndefined();
		} catch (err) {expect(err.name).toEqual('BAD_REQUEST');}
	})

	test('Successful Create group', async () => {
		const result = await services.createGroup('test1', 'test', '1', 'cfd03665-781b-4b40-9baa-171688095b59');
		expect(result).toBeDefined();
		let id = result.id;
		expect(result).toEqual({ createdBy: '1', description: "test", games: [], id: id, name: "test1" });
	})
});

describe('Add game to group tests', () => {
	test('add game to group without token', async () => {
		try {await services.addGametoGroup('1', '1', 'OIXt3DmJU0', 'catan');}
		catch (err) {expect(err.name).toEqual('UNAUTHORIZED');}
	})

	test('add game to group that does not exist', async () =>{
		try {await services.addGametoGroup('1', '0', 'OIXt3DmJU0', 'catan', 'cfd03665-781b-4b40-9baa-171688095b59');} 
		catch (err) {expect(err.name).toEqual('NOT_FOUND');}
	})

	test('add game to group with game', async () => {
		const result = await services.addGametoGroup('1', '1', 'OIXt3DmJU0', 'catan', 'cfd03665-781b-4b40-9baa-171688095b59');
		expect(result).toBeDefined();
		expect(result).toEqual({ gameID: "OIXt3DmJU0" });
	})
});