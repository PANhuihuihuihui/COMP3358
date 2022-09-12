CREATE TABLE 24Game_UserList (
    username varchar(16) NOT NULL, 
    password varchar(32) NOT NULL, 
    win_number INT(5), 
    game_number INT(5), 
    avg_time FLOAT(10),
    PRIMARY KEY username (username)
);