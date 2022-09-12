'c3358'@'localhost'

CREATE USER 'c3358'@'localhost' IDENTIFIED BY 'c3358PASS';
GRANT ALL ON c3358.* TO 'c3358'@'localhost' IDENTIFIED BY 'c3358PASS';

CREATE USER 'c3358'@'localhost' IDENTIFIED WITH authentication_plugin BY 'c3358PASS';