create schema dbo;

create table dbo.User_Stats(
                               id serial not null,
                               games_played int not null,
                               games_won int not null,
                               PRIMARY KEY(id)
);

create table dbo.Users(
                          id UUID not null,
                          username varchar(64) unique not null,
                          passwordInfo varchar(256) not null,
                          stats int not null,
                          primary key (id),
                          foreign key (stats) references dbo.User_Stats(id)
);

create table dbo.Tokens(
                           userID UUID,
                           tokenValidationInfo varchar(256) NOT NULL,
                           created_at bigint not null,
                           last_used_at bigint not null ,
                           PRIMARY KEY (tokenValidationInfo),
                           FOREIGN KEY (userID) REFERENCES dbo.Users(id)
);

create table dbo.Game_Settings(
                            id serial not null,
                            gridX int not null,
                            gridY int not null,
                            shotsPerRound int not null,
                            setupTime bigint not null,
                            roundTime bigint not null,
                            fleetSchema jsonb,
                            PRIMARY KEY (id)
);

create table dbo.Games(
                          id serial not null,
                          state varchar(64) not null,
                          updated bigint not null,
                          deadline bigint,
                          created bigint not null,
                          player1 UUID not null,
                          player2 UUID  null,
                          fleet1 jsonb not null,
                          fleet2 jsonb not null,
                          board1 jsonb not null,
                          board2 jsonb not null,
                          setting int not null,
                          primary key(id),
                          foreign key (player1) references dbo.Users(id),
                          foreign key (player2) references dbo.Users(id),
                          foreign key (setting) references dbo.Game_Settings(id)
);



