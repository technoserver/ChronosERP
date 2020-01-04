ALTER TABLE pessoa
    add COLUMN observacao text default '';

ALTER TABLE pessoa_fisica
    add COLUMN apelido varchar(50) default '';