//Server initialization module
'use strict';

const default_port = 3000;
const port = process.env.PORT || default_port;

const config = require('./borga-config');

//elasticsearch specifications
const es_spec = {
    url: config.devl_es_url,
    prefix: 'borga'
};

//Memory path defines if the server is using data_mem or elastic search
const memoryPath = './borga-data-mem';

const data_mem = require(memoryPath)(es_spec);

const app = require('./borga-server')(data_mem);

async function defaultValues() {
    //Default users
    await data_mem.addUser(config.guest).catch(err => console.log('Error adding guest user. ' + err.message + '.'));
    await data_mem.addUser(config.jonas).catch(err => console.log('Error adding jonas user. ' + err.message + '.'));

    //Default groups
    await data_mem.addGroup(config.guestGroup).catch(err => console.log('Error adding guest group. ' + err.message + '.'));
    await data_mem.addGroup(config.jonasGroup).catch(err => console.log('Error adding jonas group. ' + err.message + '.'));

    //Default games
    await data_mem.addGameToGroup('1', '1', config.game).catch(err => console.log('Error adding game to guest group. ' + err.message + '.'));
};

Promise.allSettled([ defaultValues() ]).then(() => app.listen(port, console.log('Listening to port ' + port + '...')));