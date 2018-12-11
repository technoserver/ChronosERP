create table nutriente
(
  id   serial not null,
  nome varchar(100),
  primary key (id)
);

insert into nutriente (id, nome)
values (1, 'Valor Calorico');
insert into nutriente (id, nome)
values (2, 'Carboidratos');
insert into nutriente (id, nome)
values (3, 'Proteinas');
insert into nutriente (id, nome)
values (4, 'Gorduras Totais');
insert into nutriente (id, nome)
values (5, 'Gorduras Saturadas ');
insert into nutriente (id, nome)
values (6, 'Gorduras Trans');
insert into nutriente (id, nome)
values (7, 'Fibra alimentar');
insert into nutriente (id, nome)
values (8, 'Sodio');
insert into nutriente (id, nome)
values (9, 'Calcio');
insert into nutriente (id, nome)
values (10, 'Cinzas');
insert into nutriente (id, nome)
values (11, 'Manganes');
insert into nutriente (id, nome)
values (12, 'Zinco');
insert into nutriente (id, nome)
values (13, 'Magnesio');
insert into nutriente (id, nome)
values (14, 'Ferro');
insert into nutriente (id, nome)
values (15, 'Cobre');
insert into nutriente (id, nome)
values (16, 'Fosforo');
insert into nutriente (id, nome)
values (17, 'Potassio');
insert into nutriente (id, nome)
values (18, 'Lipideos');


create table tabela_nutricional_cabecalho
(
  id      serial not null,
  nome    varchar(100),
  porcao  decimal(18, 6) default 0,
  unidade varchar(8),
  primary key (id)
);

create table tabela_nutricional_detalhe
(
  id                              serial         not null,
  id_tabela_nutricional_cabecalho integer,
  id_nutriente                    integer        not null,
  quantidade                      decimal(18, 6) not null,
  vd                              decimal,
  primary key (id),
  foreign key (id_nutriente) references nutriente (id),
  foreign key (id_tabela_nutricional_cabecalho) references tabela_nutricional_cabecalho (id)
);


alter table produto
  add column dias_validade integer default 0;
alter table produto
  add column id_tabela_nutricional_cabecalho integer;
ALTER TABLE produto
  ADD CONSTRAINT produto_id_tabela_nutricional_cabecalho_fkey FOREIGN KEY (id_tabela_nutricional_cabecalho) REFERENCES tabela_nutricional_cabecalho (id);