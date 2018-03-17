/*
  EMPRESA
  -------
*/
INSERT INTO EMPRESA (ID, RAZAO_SOCIAL, NOME_FANTASIA, CNPJ, INSCRICAO_ESTADUAL,  INSCRICAO_MUNICIPAL, TIPO, DATA_CADASTRO,  EMAIL, CODIGO_IBGE_CIDADE, CODIGO_IBGE_UF,TIPO_CONTROLE_ESTOQUE)
VALUES (1, '${razaoSocial}', '${fantasia}', '${cnpj}', '${inscricaoEstadual}',  '${inscricaoMunicipal}', 'M',now(),'${email}', '${codigoIbgeCidade}', '${codigoIbgeUf}','D');


/*
  EMPRESA_ENDERECO
  -------
*/
insert into EMPRESA_ENDERECO (ID, ID_EMPRESA, LOGRADOURO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, CEP, MUNICIPIO_IBGE, UF, PRINCIPAL, ENTREGA, COBRANCA, CORRESPONDENCIA)
VALUES (1, 1, '${logradouro}', '${numero}', '${complemento}', '${bairro}', '${cidade}', '${cep}', '${codigoIbgeCidade}',
           '${uf}', 'S', 'S', 'S', 'S');

/*
  EMPRESA_TELEFONE
  -------
*/
insert into EMPRESA_TELEFONE (ID, ID_EMPRESA, TIPO, NUMERO) values(1,1,0,'${fone}');


/*
  PESSOA
*/

INSERT INTO PESSOA (ID, NOME, TIPO, EMAIL, SITE, CLIENTE, FORNECEDOR, COLABORADOR, TRANSPORTADORA)
VALUES ('1', '${nomeUsuario}', 'J', '${email}', NULL, 'N', 'N', 'S', 'N');



/*
  EMPRESA_PESSOA
  -------
*/

INSERT INTO EMPRESA_PESSOA(ID,ID_EMPRESA,ID_PESSOA,RESPONSAVEL_LEGAL) values (1,1,1,'N');

/*
  PESSOA_ENDERECO
  -------
*/
INSERT INTO PESSOA_ENDERECO (ID, ID_PESSOA, LOGRADOURO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, CEP, MUNICIPIO_IBGE, UF, PRINCIPAL, ENTREGA, COBRANCA, CORRESPONDENCIA)
VALUES
  ('1', '1', '${logradouro}', '${numero}', '${complemento}', '${bairro}', '${cidade}', '${cep}', '${codigoIbgeCidade}',
        '${uf}', 'S', NULL, NULL, NULL);


/*
  PESSOA_JURIDICA
  ---------------
*/
insert into PESSOA_JURIDICA (ID, ID_PESSOA, CNPJ) values(1,1,'${cnpj}');


/*
  SETOR
  -----
*/
INSERT INTO SETOR (ID, ID_EMPRESA, NOME, DESCRICAO) VALUES (1, 1, 'ADMINISTRACAO', NULL);


/*
  CARGO
*/
INSERT INTO CARGO (ID, ID_EMPRESA, NOME, DESCRICAO, SALARIO, CBO_1994, CBO_2002) VALUES (1, 1, 'ADMINISTRADOR', NULL, 5000, NULL, '252105');

/*
  COLABORADOR
*/
INSERT INTO COLABORADOR (ID, ID_PESSOA, ID_NIVEL_FORMACAO, ID_CARGO, ID_SETOR, ID_TIPO_COLABORADOR, ID_SITUACAO_COLABORADOR)
VALUES (1, 1, 1, 1, 1, 1, 1);


/*
  USUARIO
  -------
*/
INSERT INTO USUARIO (ID, ID_COLABORADOR, ID_PAPEL, LOGIN, SENHA, DATA_CADASTRO, ADMINISTRADOR) VALUES (1, 1, 1, '${email}', '${senha}',now(), 'S');

