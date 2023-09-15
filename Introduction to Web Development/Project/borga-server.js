//Server API module
'use strict';

const express = require('express');
const cookieParser = require('cookie-parser');
const session = require('express-session');
const passport = require('passport');

passport.serializeUser((userInfo, done) => { done(null, userInfo); });
passport.deserializeUser((userInfo, done) => { done(null, userInfo); });

module.exports = function(data_mem) {    
    const board_games = require('./board-games-data');

    const services = require('./borga-services')(data_mem, board_games);

    const webapi = require('./borga-web-api')(services);
    const website = require('./borga-web-site')(services);

    const app = express();
    app.use(session({
        secret: 'isel-ipw',
        resave: false,
        saveUninitialized: false
    }));
    app.use(passport.initialize());
    app.use(passport.session());
    app.use(cookieParser());

    app.set('view engine', 'hbs');

    app.use('/favicon.ico', express.static('views/images/favicon.ico'));
    app.use('/images', express.static('views/images'));
    app.use('/static-files', express.static('views/static-files'));

    app.use('/api/global', webapi);
    app.use('/', website);

    return app;
};