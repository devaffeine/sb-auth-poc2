CREATE USER 'exporter'@'%' IDENTIFIED BY 'exporterpassword' WITH MAX_USER_CONNECTIONS 3;
GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'exporter'@'%';

CREATE TABLE IF NOT EXISTS tb_users (
                                        id INT AUTO_INCREMENT,
                                        username VARCHAR(255),
    password VARCHAR(512),
    PRIMARY KEY(id)
    );

ALTER TABLE tb_users ADD INDEX (username);