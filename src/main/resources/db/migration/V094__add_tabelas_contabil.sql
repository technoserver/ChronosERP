alter table pdv_venda_cabecalho
    add column id_orcamento integer;

ALTER TABLE pdv_venda_cabecalho
    ADD CONSTRAINT pdv_venda_cabecalho_id_orcamento_fkey FOREIGN KEY (id_orcamento) REFERENCES orcamento_cabecalho (id);


--------------------------------------------------------------
-- Plano contabil do SPED.
-- ------------------------------------------------------------

CREATE TABLE PLANO_CONTA_REF_SPED
(
    ID              SERIAL NOT NULL,
    COD_CTA_REF     VARCHAR(30),
    DESCRICAO       TEXT,
    ORIENTACOES     TEXT,
    INICIO_VALIDADE DATE,
    FIM_VALIDADE    DATE,
    TIPO            CHAR(1),
    PRIMARY KEY (ID)
);

-- ------------------------------------------------------------
-- Armazena o Plano de Contas vinculado à empresa.
-- ------------------------------------------------------------

CREATE TABLE PLANO_CONTA
(
    ID            SERIAL NOT NULL,
    ID_EMPRESA    INTEGER,
    NOME          VARCHAR(100),
    DATA_INCLUSAO DATE,
    MASCARA       VARCHAR(50),
    NIVEIS        INTEGER,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID)
);

-- ------------------------------------------------------------
-- Contas contábeis da empresa.
-- ------------------------------------------------------------
DROP TABLE IF EXISTS CONTABIL_CONTA;
CREATE TABLE CONTABIL_CONTA
(
    ID                      SERIAL       NOT NULL,
    ID_PLANO_CONTA          INTEGER      NOT NULL,
    ID_CONTABIL_CONTA       INTEGER,
    ID_PLANO_CONTA_REF_SPED INTEGER,
    CLASSIFICACAO           VARCHAR(30)  NOT NULL,
    TIPO                    CHAR(1),
    DESCRICAO               VARCHAR(100) NOT NULL,
    DATA_INCLUSAO           DATE,
    SITUACAO                CHAR(1),
    NATUREZA                CHAR(1),
    PATRIMONIO_RESULTADO    CHAR(1),
    LIVRO_CAIXA             CHAR(1),
    DFC                     CHAR(1),
    ORDEM                   VARCHAR(20),
    CODIGO_REDUZIDO         VARCHAR(10),
    CODIGO_EFD              CHAR(2),
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_PLANO_CONTA_REF_SPED) REFERENCES PLANO_CONTA_REF_SPED (ID),
    FOREIGN KEY (ID_CONTABIL_CONTA) REFERENCES CONTABIL_CONTA (ID),
    FOREIGN KEY (ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)
);

/*
	Plano de Contas Referenciado Sped
*/
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (1, '1', 'ATIVO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (2, '1.01', 'CIRCULANTE', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (3, '1.01.01', 'DISPONIBILIDADES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (4, '1.01.01.01.00', 'Caixa',
        'Contas que registram valores em dinheiro e em cheques em caixa, recebidos e ainda não depositados, pagáveis irrestrita e imediatamente.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (5, '1.01.01.02.00', 'Bancos',
        'Contas que registram disponibilidades, mantidas em instituições financeiras, não classificáveis em outras contas deste plano referencial.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (6, '1.01.01.03.00', 'Recursos no Exterior Decorrentes de Exportação',
        'Contas que registram movimentação de recursos em instituições financeiras no exterior, nos termos do art. 1o. da Lei no 11.371/2006.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (7, '1.01.01.04.00', 'Contas Bancárias  Subvenções',
        'Contas que registram disponibilidades, nas instituições imunes ou isentas, de recursos de aplicações vinculadas ao objeto das subvenções, mantidas em instituições financeiras.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (8, '1.01.01.05.00', 'Contas Bancárias  Doações',
        'Contas que registram disponibilidades, nas instituições imunes ou isentas, de recursos de aplicações vinculadas ao objeto das doações, mantidas em instituições financeiras.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (9, '1.01.01.06.00', 'Contas Bancárias  Outros Recursos Sujeitos a Restrições',
        'Contas que registram disponibilidades, nas instituições imunes ou isentas, de outros recursos sujeitos a restrições, mantidas em instituições financeiras.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (10, '1.01.01.07.00', 'Títulos e Valores Mobiliários',
        'Contas que registram as aplicações em títulos e valores mobiliários, de recursos de livre movimentação, cujo vencimento ou resgate venha a ocorrer no curso do ano,calendário subseqüente.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (11, '1.01.01.07.01', 'Valores Mobiliários , Mercado de Capitais Interno',
        'Contas que registram as aplicações no mercado de capitais do Brasil, de recursos de livre movimentação, cujo vencimento ou resgate venha a ocorrer no curso do ano,calendário subseqüente.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (12, '1.01.01.07.02', 'Valores Mobiliários , Mercado de Capitais Externo',
        'Contas que registram as aplicações no mercado de capitais do exterior, de recursos de livre movimentação, cujo vencimento ou resgate venha a ocorrer no curso do ano,calendário subseqüente.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (13, '1.01.01.08.00', 'Valores Mobiliários  Aplicações de Subvenções  ',
        'Contas que correspondem, nas instituições imunes ou isentas, às aplicações financeiras de recursos oriundos de subvenções.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (14, '1.01.01.09.00', 'Valores Mobiliários  Aplicações de Doações',
        'Contas que correspondem, nas instituições imunes ou isentas, às aplicações financeiras de recursos oriundos de doações.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (15, '1.01.01.10.00', 'Valores Mobiliários  Aplicações de Outros Recursos Sujeitos a Restrições',
        'Contas que correspondem, nas instituições imunes ou isentas, às aplicações financeiras de outros recursos sujeitos a restrições.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (16, '1.01.01.11.00', 'Outras', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (17, '1.01.03', 'ESTOQUES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (18, '1.01.03.01.00', 'Estoques',
        'Contas que registram o valor do saldo das contas dos estoques de matérias,primas, materiais secundários, produtos em elaboração, produtos acabados e mercadorias para revenda, na data da apuração dos resultados. Observar, quanto aos estoques, as orientações contidas na Instrução Normativa SRF no 51, de 1978, e no PN CST no 6, de 1979.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (19, '1.01.03.01.01', 'Mercadorias para Revenda',
        'Contas que registram o valor do saldo das contas de estoques de mercadorias para revenda, na data de apuração dos resultados. Observar, quanto aos estoques, as orientações contidas na Instrução Normativa SRF no 51, de 1978, e no PN CST no 6, de 1979.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (20, '1.01.03.01.02', 'Insumos (materiais diretos)',
        'Contas que registram o valor do saldo das contas de estoques de matérias primas e materiais diretos, na data de apuração dos resultados. Observar, quanto aos estoques, as orientações contidas na Instrução Normativa SRF no 51, de 1978, e no PN CST no 6, de 1979.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (21, '1.01.03.01.03', 'Produtos em Elaboração',
        'Contas que registram o valor do saldo das contas de estoques de produtos em elaboração, na data de apuração dos resultados. Observar, quanto aos estoques, as orientações contidas na Instrução Normativa SRF no 51, de 1978, e no PN CST no 6, de 1979.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (22, '1.01.03.01.04', 'Produtos Acabados',
        'Contas que registram o valor do saldo das contas de estoques de produtos acabados, na data de apuração dos resultados. Observar, quanto aos estoques, as orientações contidas na Instrução Normativa SRF no 51, de 1978, e no PN CST no 6, de 1979.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (23, '1.01.03.01.05', 'Serviços em andamento',
        'Contas que registram o valor do saldo das contas de serviços em andamento, na data de apuração dos resultados. Observar, quanto aos estoques, as orientações contidas na Instrução Normativa SRF no 51, de 1978, e no PN CST no 6, de 1979.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (24, '1.01.03.01.06', 'Insumos Agropecuários',
        'Contas que registram, nas empresas com atividade rural, o valor do saldo das contas de insumos agropecuários, na data de apuração dos resultados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (25, '1.01.03.01.07', 'Produtos Agropecuários em Formação',
        'Contas que registram, nas empresas com atividade rural, o valor do saldo das contas de produtos agropecuários em formação, na data de apuração dos resultados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (26, '1.01.03.01.08', 'Produtos Agropecuários Acabados',
        'Contas que registra, nas empresas com atividade rural, o valor do saldo das contas de estoques de produtos agropecuários acabados, na data de apuração do resultado.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (27, '1.01.03.02.00', 'Imóveis Destinados à Venda',
        'Contas utilizadas pela pessoa jurídica que exerce atividade imobiliária para indicar o estoque de imóveis destinados à venda existente na data da apuração dos resultados. Atenção: as construções em andamento de imóveis destinados à venda devem ser incluídas na conta Construções em Andamento de Imóveis Destinados à Venda',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (28, '1.01.03.02.01', 'Construções em Andamento de Imóveis Destinados à Venda',
        'Contas utilizadas pela pessoa jurídica que exerce atividade imobiliária para indicar os imóveis em construção para futura comercialização',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (29, '1.01.03.03.00', 'Estoques Destinados à Doação',
        'Contas que registram, nas instituições imunes ou isentas, estoques destinados à doação.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (30, '1.01.03.04.00', 'Outras', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (31, '1.01.05', 'CRÉDITOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (32, '1.01.05.01.00', 'Adiantamentos a Fornecedores',
        'Contas que registram aos adiantamentos feitos a fornecedores de matérias,primas ou mercadorias para revenda.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (33, '1.01.05.02.00', 'Clientes',
        'Contas que registram as contas a receber com vencimento até o final do ano,calendário subseqüente.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (34, '1.01.05.03.00', 'Créditos Fiscais CSLL  Diferenças Temporárias e Base de Cálculo Negativa',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos créditos fiscais com realização no exercício seguinte e das diferenças temporárias, inclusive as decorrentes da base de cálculo negativa, relativos à CSLL, conforme Deliberação CVM no 273, de 20 de agosto de 1998.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (35, '1.01.05.04.00', 'Créditos Fiscais IRPJ  Diferenças Temporárias e Prejuízos Fiscais',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos créditos fiscais com realização no exercício seguinte e das diferenças temporárias, inclusive as decorrentes dos prejuízos fiscais, relativos ao IRPJ, conforme Deliberação CVM nº 273, de 20 de agosto de 1998.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (36, '1.01.05.05.00', 'Impostos e Contribuições a Recuperar',
        'Contas correspondentes aos impostos e contribuições a recuperar no final do ano,calendário.', null,
        '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (37, '1.01.05.05.01', 'Imposto de Renda a Recuperar',
        'Contas correspondentes ao Imposto de REnda a recuperar no final do período de apuração.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (38, '1.01.05.05.02', 'IPI a Recuperar',
        'Contas correspondentes ao IPI a recuperar no final do período de apuração.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (39, '1.01.05.05.03', 'PIS e COFINS a Recuperar',
        'Contas correspondentes ao PIS e à Cofins a recuperar no final do período de apuração.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (40, '1.01.05.05.04', 'CSLL a Recuperar',
        'Contas correspondentes  à CSLL a recuperar no final do período de apuração.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (41, '1.01.05.05.05', 'ICMS e Contribuições a Recuperar',
        'Contas correspondentes ao ICMS a recuperar no final do período de apuração.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (42, '1.01.05.05.06', 'Tributos Municipais a Recuperar',
        'Contas correspondentes a tributos municipais a recuperar no final do período de apuração.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (43, '1.01.05.05.90', 'Outros Impostos e Contribuições a Recuperar',
        'Contas correspondentes a outros impostos a recuperar no final do período de apuração.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (44, '1.01.05.06.00', 'Créditos por Contribuições e Doações',
        'Contas que registram, nas instituições imunes ou isentas, créditos por contribuições ou doações.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (45, '1.01.05.07.00', 'Outras', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (46, '1.01.07', 'DESPESAS DO EXERCÍCIO SEGUINTE', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (47, '1.01.07.01.00', 'Despesas do Exercício Seguinte',
        'Contas correspondentes a pagamentos antecipados, cujos benefícios ou prestação de serviços à pessoa jurídica ocorrerão durante o exercício seguinte. São valores relativos a despesas que efetivamente pertencem ao exercício seguinte.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (48, '1.01.07.02.00', 'Outras Contas',
        'Incluir, dentre outras, a soma das contas/subcontas do Circulante que registram, dentre outras, a correção monetária relativa à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal, na forma estabelecida nos arts. 32 e 33 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (49, '1.01.09', 'CONTAS RETIFICADORAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (50, '1.01.09.01.00', '(,) Contas Retificadoras',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores que retificam este grupo, tais como: duplicatas descontadas, provisões para créditos de liquidação duvidosa, provisões para ajuste do estoque ao valor de mercado, quando este for inferior, contas redutoras dos créditos.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (51, '1.01.09.01.01', '(,) Duplicatas Descontadas',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das duplicatas descontadas que retificam este grupo',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (52, '1.01.09.01.03', '(,) Provisões para Créditos de Liquidação Duvidosa',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das provisões para créditos de liquidação duvidosa que retificam este grupo.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (53, '1.01.09.01.05', '(,) Provisão para Ajuste do Estoque ao Valor de Mercado',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das provisões para ajuste do estoque ao valor de mercado que retificam este grupo.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (54, '1.01.09.01.07', '(,) Provisões para Ajuste ao Valor Provável de Realização',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das provisões para ajuste do estoque ao valor provável de realização que retificam este grupo.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (55, '1.01.09.01.90', '(,) Outras Contas Retificadoras',
        'Contas que registram parcelas a serem subtraídas do circulante que não possam ser classificadas nos itens precedentes.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (56, '1.04', 'REALIZÁVEL A LONGO PRAZO', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (57, '1.04.01', 'CRÉDITOS', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (58, '1.04.01.01.00', 'Clientes',
        'Contas que registram os créditos a receber de terceiros, relativos a eventuais contas de clientes, títulos a receber, adiantamentos, etc., com prazo de recebimento posterior ao exercício seguinte à data do balanço.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (59, '1.04.01.02.00', 'Créditos com Pessoas Ligadas (Físicas/Jurídicas)',
        'Contas correspondentes a vendas, adiantamentos ou empréstimos a sociedades coligadas ou controladas, diretores, acionistas ou participantes da empresa, que não constituam negócios usuais na exploração do objeto social da pessoa jurídica.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (60, '1.04.01.03.00', 'Títulos e Valores Mobiliários',
        'Contas correspondentes às aplicações em títulos e valores mobiliários com vencimento posterior ao exercício seguinte, e investimentos em outras sociedades que não tenham caráter permanente, inclusive os feitos com incentivos fiscais.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (61, '1.04.01.04.00', 'Depósitos Judiciais',
        'Contas que registram aos depósitos judiciais efetuados, a qualquer título, pendentes de decisão.', null,
        '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (62, '1.04.01.05.00', 'Créditos Fiscais CSLL  Diferenças Temporárias e Base de Cálculo Negativa',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos créditos fiscais com realização após o exercício seguinte e das diferenças temporárias, inclusive as decorrentes da base de cálculo negativa, relativos à CSLL, conforme Deliberação CVM no 273, de 1998.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (63, '1.04.01.06.00', 'Créditos Fiscais IRPJ  Diferenças Temporárias e Prejuízos Fiscais',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos créditos fiscais com realização após o exercício seguinte e das diferenças temporárias, inclusive as decorrentes dos prejuízos fiscais, relativos ao IRPJ, conforme Deliberação CVM no 273, de 1998.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (64, '1.04.01.07.00', 'Créditos por Contribuições e Doações',
        'Contas que registram, nas instituições imunes ou isentas, créditos por contribuições ou doações com vencimento após final do exercício subseqüente.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (65, '1.04.01.08.00', 'Outras Contas',
        'Contas que registram, entre outras, a soma das contas/subcontas do Realizável a Longo Prazo que registram a correção monetária relativa à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal, na forma estabelecida nos arts. 32 e 33 do Decreto no 332, de 1991.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (66, '1.04.01.09.00', '(,) Contas Retificadoras',
        'Contas que registram parcelas a serem subtraídas do Realizável a Longo Prazo correspondentes a valores que retificam este grupo.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (67, '1.07', 'PERMANENTE', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (68, '1.07', 'NÃO CIRCULANTE', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (69, '1.07.00', 'REALIZÁVEL A LONGO PRAZO', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (70, '1.07.00.01.00', 'Clientes',
        'Contas que registram os créditos a receber de terceiros, relativos a eventuais contas de clientes, títulos a receber, adiantamentos, etc., com prazo de recebimento posterior ao exercício seguinte à data do balanço.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (71, '1.07.00.02.00', 'Créditos com Pessoas Ligadas (Físicas/Jurídicas)',
        'Contas correspondentes a vendas, adiantamentos ou empréstimos a sociedades coligadas ou controladas, diretores, acionistas ou participantes da empresa, que não constituam negócios usuais na exploração do objeto social da pessoa jurídica.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (72, '1.07.00.03.00', 'Valores Mobiliários',
        'Contas correspondentes às aplicações em títulos com vencimento posterior ao exercício seguinte, e investimentos em outras sociedades que não tenham caráter permanente, inclusive os feitos com incentivos fiscais.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (73, '1.07.00.04.00', 'Depósitos Judiciais',
        'Contas que registram aos depósitos judiciais efetuados, a qualquer título, pendentes de decisão.', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (74, '1.07.00.05.00', 'Créditos Fiscais CSLL  Diferenças Temporárias e Base de Cálculo Negativa',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos créditos fiscais com realização após o exercício seguinte e das diferenças temporárias, inclusive as decorrentes da base de cálculo negativa, relativos à CSLL, conforme Deliberação CVM no 273, de 1998.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (75, '1.07.00.06.00', 'Créditos Fiscais IRPJ  Diferenças Temporárias e Prejuízos Fiscais',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos créditos fiscais com realização após o exercício seguinte e das diferenças temporárias, inclusive as decorrentes dos prejuízos fiscais, relativos ao IRPJ, conforme Deliberação CVM no 273, de 1998.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (76, '1.07.00.07.00', 'Créditos por Contribuições e Doações',
        'Contas que registram, nas instituições imunes ou isentas, créditos por contribuições ou doações com vencimento após final do exercício subseqüente.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (77, '1.07.00.08.00', 'Outras Contas',
        'Contas que registram, entre outras, a soma das contas/subcontas do Realizável a Longo Prazo que registram a correção monetária relativa à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal, na forma estabelecida nos arts. 32 e 33 do Decreto no 332, de 1991.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (78, '1.07.00.90.00', '(,) Duplicatas Descontadas',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das duplicatas descontadas que retificam este grupo',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (79, '1.07.00.93.00', '(,) Provisões para Créditos de Liquidação Duvidosa',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das provisões para créditos de liquidação duvidosa que retificam este grupo.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (80, '1.07.00.95.00', '(,) Provisões para Ajuste ao Valor Provável de Realização',
        'Contas que registram parcelas a serem subtraídas do circulante, correspondentes a valores das provisões para ajuste do estoque ao valor provável de realização que retificam este grupo.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (81, '1.07.00.97.00', '(,) Outras Contas Retificadoras',
        'Contas que registram parcelas a serem subtraídas do Realizável a Longo Prazo que não possam ser classificadas nos itens precedentes.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (82, '1.07.01', 'INVESTIMENTOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (83, '1.07.01.01.00', 'Participações Permanentes em Coligadas ou Controladas',
        'Contas que registram investimentos permanentes, na forma de participação em outras sociedades coligadas e/ou controladas, ainda que se trate de investimento não relevante.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (84, '1.07.01.02.00', 'Investimentos Decorrentes de Incentivos Fiscais',
        'Contas que registram os investimentos decorrentes de incentivos fiscais representados por ações novas da Embraer ou de empresas nacionais de informática ou por participação direta decorrente da troca do CI  Certificado de Investimento por ações pertencentes às carteiras de Fundos (Finor, Finam e Fiset). Inclui,se a aquisição de quotas representativas de direitos de comercialização sobre produção de obras audiovisuais cinematográficas brasileiras de produção independente, com projetos  previamente aprovados pelo Ministério da Cultura, realizada no mercado de capitais, em ativos previstos em lei e autorizados pela Comissão de Valores Mobiliários (CVM).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (85, '1.07.01.03.00', 'Outros Investimentos',
        'Contas correspondentes aos direitos de qualquer natureza que não se destinem à manutenção da atividade da companhia ou da empresa e que não se classifiquem no ativo circulante ou realizável a longo prazo, tais como: o imóvel não utilizado na exploração ou na manutenção das atividades da empresa e que não se destine à revenda, e os recursos florestais destinados à proteção do solo ou à preservação da natureza, entre outros.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (86, '1.07.01.04.00', 'Ágios em Investimentos',
        'Contas correspondentes ao ágio por diferença de valor de mercado dos bens, por valor de rentabilidade futura, por fundo de comércio, intangíveis, ou outras razões econômicas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (87, '1.07.01.05.00', 'Correção Monetária , Diferença IPC/BTNF (Lei no 8.200/1991)',
        'Contas/subcontas dos investimentos que registram a correção monetária relativa à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal, na forma estabelecida nos arts. 32 e 33 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (88, '1.07.01.06.00', 'Correção Monetária Especial (Lei no 8.200/1991)',
        'Contas/subcontas dos investimentos que registram a correção monetária especial, na forma do art. 44 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (89, '1.07.01.07.00', '(,) Deságios e Provisão para Perdas Prováveis em Investimentos',
        'Contas que registram: a) o deságio por diferença de valor de mercado dos bens, por valor de rentabilidade futura e por fundo de comércio, intangíveis, ou outras razões econômicas; b) o valor correspondente à provisão para perdas em investimentos registrados pelo método de custo e à provisão para perdas em investimentos avaliados pelo método da equivalência patrimonial, sendo que, neste último caso, deve ser informado somente o valor das perdas efetivas ou potenciais já previstas, mas não reconhecidas contabilmente pela coligada ou controlada.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (90, '1.07.01.90.00', 'Outras Contas',
        'Contas que registram bens e direitos classificáveis em Investimentos que não possam ser classificadas nos itens precedentes.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (91, '1.07.01.97.00', '(,) Outras Contas Retificadoras',
        'Contas que registram parcelas a serem subtraídas de Investimentos que não possam ser classificadas nos itens precedentes.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (92, '1.07.04', 'IMOBILIZADO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (93, '1.07.04.01.00', 'Terrenos',
        'Contas que registram os terrenos de propriedade da pessoa jurídica utilizados nas operações, ou seja, onde se localizam a fábrica, os depósitos, os escritórios, as filiais, as lojas, etc., inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens Contas que registram os terrenos de propriedade da pessoa jurídica utilizados nas operações, ou seja, onde se localizam a fábrica, os depósitos, os escritórios, as filiais, as lojas, etc., inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (94, '1.07.04.02.00', 'Edifícios e Construções',
        'Contas que registram os edifícios, melhoramentos e obras integradas aos terrenos, e os serviços e instalações provisórias, necessários à construção e ao andamento das obras, tais como: limpeza do terreno, serviços topográficos, sondagens de reconhecimento, terraplenagem, e outras similares, inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens Atenção: As construções em andamento devem ser informadas na conta  Construções em Andamento.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (95, '1.07.04.02.01', ' Construções em Andamento',
        'Contas que registram as construções em andamento de edifícios, melhoramentos e obras integradas aos terrenos, e os serviços e instalações provisórias, necessários à construção e ao andamento das obras, tais como: limpeza do terreno, serviços topográficos, sondagens de reconhecimento, terraplenagem, e outras similares, inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (96, '1.07.04.03.00', 'Equipamentos, Máquinas e Instalações Industriais',
        'Contas que registram os equipamentos, máquinas e instalações industriais utilizados no processo de produção da pessoa jurídica, inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (97, '1.07.04.04.00', 'Veículos',
        'Contas que registram os veículos de propriedade da pessoa jurídica. inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens Atenção: Os veículos de uso direto na produção, como empilhadeiras e similares, devem ser informados na conta Equipamentos, Máquinas e Instalações Industriais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (98, '1.07.04.04.01', 'Embarcações',
        'Contas que registram as embarcações de propriedade da pessoa jurídica., inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (99, '1.07.04.04.02', 'Aeronaves',
        'Contas que registram as aeronaves de propriedade da pessoa jurídica., inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (100, '1.07.04.05.00', 'Móveis, Utensílios e Instalações Comerciais',
        'Contas que registram os móveis, utensílios e instalações comerciais., inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (101, '1.07.04.06.00', 'Recursos Minerais',
        'Contas que registram os direitos de exploração de jazidas de minério, de pedras preciosas, e similares, inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (102, '1.07.04.07.00', 'Florestamento e Reflorestamento',
        'Contas que registram os recursos florestais destinados à exploração dos respectivos frutos e ao corte para comercialização, consumo ou industrialização, inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (103, '1.07.04.08.00', 'Direitos Contratuais de Exploração de Florestas',
        'Contas que registram os direitos contratuais de exploração de florestas com prazo de exploração superior a dois anos., inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (104, '1.07.04.09.00', 'Outras Imobilizações',
        'Contas que registram outras imobilizações, tais como: benfeitorias em propriedades arrendadas que se incorporam ao imóvel arrendado e revertem ao proprietário do imóvel ao final da locação, adiantamentos para inversões fixas, reprodutores, matrizes e as culturas permanentes da atividade rural, e similares, inclusive os decorrentes de operações que transfiram à companhia os benefícios, riscos e controle desses bens',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (105, '1.07.04.10.00', 'Correção Monetária , Diferença IPC/BTNF (Lei no 8.200/1991)',
        'Contas/subcontas do imobilizado que registram a correção monetária relativa à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal, na forma estabelecida nos arts. 32 e 33 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (106, '1.07.04.11.00', 'Correção Monetária Especial (Lei no 8.200/1991)',
        'Contas/subcontas do imobilizado que registram a correção monetária especial na forma do art. 44 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (107, '1.07.04.12.00', '(,) Depreciações, Amortizações e Quotas de Exaustão',
        'Contas que registram as depreciações, amortizações e quotas de exaustão das contas do imobilizado.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (108, '1.07.04.90.00', '(,) Outras Contas Redutoras do Imobilizado',
        'Outras contas redutoras do Imobilizado, inclusive a provisão para perda decorrente da análise de  recuperação (art. 183, §3º, da Lei 6.404/76)',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (109, '1.07.05', 'INTANGÍVEL', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (110, '1.07.05.01.00', 'Concessões', 'Contas que registram os custos com aquisição de concessões', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (111, '1.07.05.03.00', 'Marcas e Patentes', 'Contas que registram os custos com aquisição de marcas e patentes',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (112, '1.07.05.05.00', 'Direitos Autorais', 'Contas que registram os custos com aquisição de direitos autorais',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (113, '1.07.05.07.00', 'Fundo de Comércio', 'Contas que registram os custos com aquisição de fundos de comércio',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (114, '1.07.05.09.00', 'Software ou Programas de Computador',
        'Contas que registram os custos com aquisição/desenvolvimento de programas de computador', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (115, '1.07.05.11.00', 'Franquias', 'Contas que registram os custos com aquisição de franquias', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (116, '1.07.05.13.00', 'Desenvolvimento de Produtos',
        'Contas que registram os custos com o desenvolvimento de novos produtos', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (117, '1.07.05.15.00', 'Outras',
        'Contas que registram os custos com aquisição de outros itens classificáveis no intangível', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (118, '1.07.05.90.00', '(,) Amortização do Intangível',
        'Contas correspondentes à amortização das contas do ativo intangível', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (119, '1.07.05.97.00', '(,) Outras Contas Redutoras do Intangível',
        'Outras contas redutoras o intangível, inclusive a provisão para perda decorrente da análise de  recuperação (art. 183, §3º, da Lei 6.404/76)',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (120, '1.07.07', 'DIFERIDO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (121, '1.07.07.01.00', 'Despesas Pré,Operacionais ou Pré,Industriais',
        'Contas que registram os gastos de organização e administração, encargos financeiros líquidos, estudos, projetos e detalhamentos, juros a acionista na fase de implantação e gastos preliminares de operação. O saldo existente em 31 de dezembro de 2008 no ativo diferido que, pela sua natureza, não puder ser alocado a outro grupo de contas, poderá permanecer no ativo sob essa classificação até sua completa amortização, sujeito à análise sobre a recuperação',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (122, '1.07.07.02.00', 'Despesas com Pesquisas Científicas ou Tecnológicas',
        'Contas que registram os gastos com pesquisa científica ou tecnológica. O saldo existente em 31 de dezembro de 2008 no ativo diferido que, pela sua natureza, não puder ser alocado a outro grupo de contas, poderá permanecer no ativo sob essa classificação até sua completa amortização, sujeito à análise sobre a recuperação',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (123, '1.07.07.03.00', 'Demais Aplicações em Despesas Amortizáveis',
        'Contas que registram os gastos com pesquisas e desenvolvimento de produtos, com a implantação de sistemas e métodos e com reorganização. O saldo existente em 31 de dezembro de 2008 no ativo diferido que, pela sua natureza, não puder ser alocado a outro grupo de contas, poderá permanecer no ativo sob essa classificação até sua completa amortização, sujeito à análise sobre a recuperação',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (124, '1.07.07.04.00', 'Correção Monetária , Diferença IPC/BTNF (Lei no 8.200/1991)',
        'Contas/subcontas do ativo diferido que registram a correção monetária relativa à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal, na forma estabelecida nos arts. 32 e 33 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (125, '1.07.07.05.00', 'Correção Monetária Especial (Lei no 8.200/1991)',
        'Contas/subcontas do ativo diferido que registram a correção monetária especial, na forma do art. 44 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (126, '1.07.07.06.00', '(,) Amortização do Diferido',
        'Contas correspondentes à amortização das contas do ativo diferido.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (127, '2', 'PASSIVO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (128, '2.01', 'CIRCULANTE', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (129, '2.01.01', 'OBRIGAÇÕES DE CURTO PRAZO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (130, '2.01.01.01.00', 'Fornecedores',
        'Contas que registram o valor a pagar correspondentes à compra de matérias,primas, bens, insumos e mercadorias.(Podem ser informados, também, os adiantamentos de clientes efetuados até 31.12.2008)',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (131, '2.01.01.01.01', 'Adiantamentos de Clientes',
        'Contas que registram o valor correspondente a adiantamentos de clientes.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (132, '2.01.01.02.00', 'Financiamentos a Curto Prazo',
        'Contas que registram os credores por financiamentos e financiamentos bancários a curto prazo, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Atenção: as obrigações resultantes de financiamentos obtidos com pessoas físicas ou outras empresas que não sejam instituições financeiras devem ser informadas nesta conta.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (133, '2.01.01.02.01', 'Financiamentos a Curto Prazo , Sistema Financeiro Nacional',
        'Contas que registram os credores por financiamentos  a curto prazo, obtidos junto ao Sistema Financeiro Nacional, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Atenção: as obrigações resultantes de operações de Arrendamento Mercantil (Leasing Financeiro) devem ser informadas na conta Financiamentos a Curto Prazo  Outros.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (134, '2.01.01.02.02', 'Arrendamento Mercantil (Financeiro) a Curto Prazo , Sistema Financeiro Nacional',
        'Contas que registram as obrigações de curto prazo relativas a arrendamento mercantil financeiro contratado junto a empresas integrantes do Sistema Financeiro Nacional',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (135, '2.01.01.02.03', 'Financiamentos a Curto Prazo , Outros',
        'Contas que registram os credores por financiamentos a curto prazo, obtidos no Brasil, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Atenção: as obrigações resultantes de financiamentos obtidos com pessoas físicas ou outras empresas que não sejam instituições financeiras devem ser informadas nesta conta .',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (136, '2.01.01.02.04', 'Financiamentos a Curto Prazo , Exterior',
        'Contas que registram os credores por financiamentos a curto prazo, obtidos no exterior, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Contas que registram os credores por financiamentos a curto prazo, obtidos no exterior, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (137, '2.01.01.02.05', 'Arrendamento Mercantil (Financeiro) a Curto Prazo , Exterior',
        'Contas que registram as obrigações das pessoas jurídicas relativas a arrendamento mercantil financeiro contratado junto a empresas não sediadas no Brasil',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (138, '2.01.01.03.00', 'Impostos, Taxas e Contribuições a Recolher',
        'Contas que registram as obrigações da pessoa jurídica relativas a impostos, taxas e contribuições. Atenção: não incluir, nesta conta, o valor do FGTS, do PIS, da COFINS e das Contribuições Previdenciárias a recolher e o valor correspondente à provisão para a contribuição social sobre o lucro líquido e para o imposto de renda.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (139, '2.01.01.03.01', 'IPI a Recolher',
        'Contas correspondentes ao IPI a Recolher no final do período de apuração.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (140, '2.01.01.03.02', 'ICMS e Contribuições a Recolher',
        'Contas correspondentes ao ICMS a Recolher no final do período de apuração.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (141, '2.01.01.03.03', 'Tributos Municipais a Recolher',
        'Contas correspondentes a tributos municipais a Recolher no final do período de apuração.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (142, '2.01.01.04.00', 'FGTS a Recolher', 'Contas que registram o valor do FGTS a recolher', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (143, '2.01.01.05.00', 'PIS e COFINS a Recolher', 'Contas que registram o valor do PIS e da COFINS a recolher',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (144, '2.01.01.06.00', 'Contribuições Previdenciárias a Recolher',
        'Contas que registram  o valor das Contribuições Previdenciárias a recolher', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (145, '2.01.01.06.90', 'Outros tributos a recolher',
        'Contas correspondentes a tributos a recolher não classificáveis em contas específicas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (146, '2.01.01.07.00', 'Salários a Pagar',
        'Contas que registram o valor correspondente aos salários, ordenados, horas extras, adicionais e prêmios a serem pagos no exercício subseqüente.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (147, '2.01.01.08.00', 'Dividendos Propostos ou Lucros Creditados',
        'Contas correspondentes aos dividendos aprovados pela Assembléia, creditados aos acionistas ou propostos pela administração da pessoa jurídica na data do balanço, como parte da destinação proposta para os lucros.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (148, '2.01.01.09.00', 'Provisão para a Contribuição Social sobre o Lucro Líquido',
        'Conta correspondente à provisão para a contribuição social sobre o lucro líquido a pagar.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (149, '2.01.01.10.00', 'Provisão para o Imposto de Renda',
        'Conta correspondente ao saldo a pagar da provisão para o imposto de renda.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (150, '2.01.01.11.00', 'Débitos Fiscais CSLL  Diferenças Temporárias',
        'As companhias abertas, obrigatoriamente, deverão informar, nestas contas, o valor dos débitos fiscais com realização no exercício seguinte e das diferenças temporárias, relativos à CSLL, conforme Deliberação CVM no 273, de 20 de agosto de 1998.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (151, '2.01.01.12.00', 'Débitos Fiscais IRPJ  Diferenças Temporárias',
        'As companhias abertas, obrigatoriamente, deverão informar, nestas contas, o valor dos débitos fiscais com realização no exercício seguinte e das diferenças temporárias, relativos ao IRPJ, conforme Deliberação CVM no 273, de 20 de agosto de 1998.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (152, '2.01.01.12.10', 'Provisões de Natureza Fiscal',
        'Contas que registram, a partir de 01.01.2008, outras provisões de natureza fiscal.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (153, '2.01.01.12.20', 'Provisões de Natureza Trabalhista',
        'Contas que registram, a partir de 01.01.2008, outras provisões de natureza trabalhista.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (154, '2.01.01.12.30', 'Provisões de Natureza Cível',
        'Contas que registram, a partir de 01.01.2008, outras provisões de natureza cível.', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (155, '2.01.01.12.40', 'Doações e Subvenções para Investimentos',
        'Contas que registram, a partir de 01.01.2008, as doações e subvenções para investimento, enquanto não transferidas para o resultado do exercício.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (156, '2.01.01.13.00', 'Outras Contas',
        'Contas que registram comissões a pagar ou provisionadas de retenções contratuais, de obrigações decorrentes do fornecimento ou utilização de serviços  (energia elétrica, água, telefone, propaganda, honorários profissionais de terceiros, aluguéis) e outras contas não citadas nas contas anteriores. Atenção: também são incluídas, nesta conta, as provisões para registro de obrigações, tais como as provisões para: férias, gratificações a empregados (inclusive encargos sociais a pagar e FGTS a recolher sobre tais provisões), e outras de natureza semelhante, ainda que não dedutíveis.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (157, '2.01.01.14.00', '(,) Contas Retificadoras',
        'Contas correspondentes às contas retificadoras do passivo circulante.', null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (158, '2.01.01.90.00', '(,) Contas Retificadoras',
        'Contas correspondentes às contas retificadoras do passivo circulante.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (159, '2.03', 'EXIGÍVEL A LONGO PRAZO', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (160, '2.03', 'NÃO,CIRCULANTE', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (161, '2.03.01', 'OBRIGAÇÕES A LONGO PRAZO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (162, '2.03.01.01.00', 'Fornecedores',
        'Contas que registram valores a pagar relativos à compra de matérias,primas, bens, insumos e mercadorias e o valor correspondente a adiantamentos de clientes, com prazo de pagamento posterior ao exercício seguinte à data do balanço.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (163, '2.03.01.02.00', 'Financiamentos a Longo Prazo',
        'Contas que registram as obrigações a longo prazo da pessoa jurídica com instituições financeiras do País e do exterior ou contas que registram os financiamentos a longo prazo, para compra de bens e equipamentos, feitos diretamente pelo fornecedor.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (164, '2.03.01.02.01', 'Financiamentos a Longo Prazo , Sistema Financeiro Nacional',
        'Contas que registram os credores por financiamentos a longo prazo, obtidos junto ao Sistema Financeiro Nacional, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Atenção: as obrigações resultantes de operações de Arrendamento Mercantil (Leasing Financeiro) devem ser informadas na conta Financiamentos a Longo Prazo  Brasil  Outros',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (165, '2.03.01.02.02', 'Arrendamento Mercantil (Financeiro) a Longo Prazo , Sistema Financeiro Nacional',
        'Contas que registram as obrigações de longo prazo relativas a arrendamento mercantil financeiro contratado junto a empresas integrantes do Sistema Financeiro Nacional',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (166, '2.03.01.02.03', 'Financiamentos a Longo Prazo  Brasil , Outros',
        'Contas que registram os credores por financiamentos de longo prazo, obtidos no Brasil, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Atenção: as obrigações resultantes de financiamentos obtidos com pessoas físicas ou outras empresas que não sejam instituições financeiras devem ser informadas nesta conta.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (167, '2.03.01.02.04', 'Financiamentos a Longo Prazo  Exterior',
        'Contas que registram os credores por financiamentos a longo prazo, obtidos no exterior, encargos financeiros a transcorrer e juros a pagar de empréstimos e financiamentos. Atenção: as obrigações resultantes de operações de Arrendamento Mercantil (Leasing Financeiro) contratadas no exterior devem ser informadas na conta Arrendamento Mercantil (Financeiro) a Longo Prazo  Exterior',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (168, '2.03.01.02.05', 'Arrendamento Mercantil (Financeiro) a Longo Prazo  Exterior',
        'Contas que registram as obrigações de longo prazo relativas a arrendamento mercantil financeiro contratado junto a empresas não sediadas no Brasil',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (169, '2.03.01.03.00', 'Empréstimos de Sócios/Acionistas Não Administradores',
        'Contas relativas a empréstimos concedidos à pessoa jurídica por sócios e acionistas não administradores.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (170, '2.03.01.04.00', 'Créditos de Pessoas Ligadas (Físicas/Jurídicas)',
        'Contas que registram compras, adiantamentos ou empréstimos de sociedades coligadas ou controladas, diretores, acionistas ou participantes da empresa, que não constituam negócios usuais na exploração do objeto social da pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (171, '2.03.01.05.00', 'Provisão para o Imposto de Renda sobre Lucros Diferidos',
        'Conta que registra o imposto de renda sobre lucros diferidos, tais como: lucro inflacionário não realizado, contratos a longo prazo relativos a fornecimento de bens e de construção por empreitada para o poder público e suas empresas, ganho de capital oriundo de desapropriação, ganho de capital por venda de bens do ativo permanente com recebimento parcelado a longo prazo e depreciação acelerada.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (172, '2.03.01.06.00', 'Débitos Fiscais CSLL , Diferenças Temporárias',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos débitos fiscais com realização após o exercício seguinte e das diferenças temporárias, relativos à CSLL, conforme Deliberação CVM nº 273, de 1998',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (173, '2.03.01.07.00', 'Débitos Fiscais IRPJ , Diferenças Temporárias',
        'As companhias abertas, obrigatoriamente, devem informar, nestas contas, o valor dos débitos fiscais com realização após o exercício seguinte e das diferenças temporárias, relativos ao IRPJ, conforme Deliberação CVM no 273, de 1998.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (174, '2.03.01.07.10', 'Outras Provisões de Natureza Fiscal',
        'Contas que registram, a partir de 01.01.2008, as outras provisões de natureza fiscal, enquanto não transferidas para o resultado do exercício.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (175, '2.03.01.07.20', 'Outras Provisões de Natureza Trabalhista',
        'Contas que registram, a partir de 01.01.2008, as outras provisões de natureza trabalhista, enquanto não transferidas para o resultado do exercício.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (176, '2.03.01.07.30', 'Outras Provisões de Natureza Cível',
        'Contas que registram, a partir de 01.01.2008, as outras provisões de natureza cível, enquanto não transferidas para o resultado do exercício.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (177, '2.03.01.07.40', 'Doações e Subvenções para Investimentos',
        'Contas que registram, a partir de 01.01.2008, as doações e subvenções para investimento, enquanto não transferidas para o resultado do exercício.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (178, '2.03.01.08.00', 'Outras Contas',
        'Contas que registram obrigações, não especificadas nos itens precedentes, cujo vencimento ocorrerá em período posterior ao exercício seguinte. Atenção: não incluir, nesta conta, o valor contratado das vendas a prazo ou a prestação para recebimento após o término do ano,calendário subseqüente, no caso de atividade imobiliária, e os juros e demais receitas financeiras recebidos antecipadamente em transações financeiras. Esses valores devem ser informados em Resultados de Exercícios Futuros.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (179, '2.03.01.09.00', '(,) Contas Retificadoras', 'Contas retificadoras do Exigível de Longo Prazo', null,
        '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (180, '2.03.01.90.00', '(,) Contas Retificadoras', 'Contas retificadoras do Exigível de Longo Prazo',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (181, '2.03.03.', 'RECEITAS DIFERIDAS', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (182, '2.03.03.01.00', 'Receitas Diferidas',
        'Saldo remanescente da conta Resultado de Exercícios Futuros onde a pessoa jurídica que explore as atividades de compra e venda, loteamento, incorporação e construção de imóveis indicava o valor contratado das vendas a prazo ou a prestação para recebimento após o término do ano,calendário subseqüente, no caso de atividade imobiliária. Também se consideravam como receitas de exercícios futuros os juros e demais receitas financeiras recebidos antecipadamente em transações financeiras.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (183, '2.03.03.03.00', '(,) Custos Correspondentes às Receitas Diferidas',
        'Contas correspondentes aos custos e despesas de exercícios futuros correspondentes às receitas indicadas na conta precedente.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (184, '2.05', 'RESULTADO DE EXERCÍCIOS FUTUROS', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (185, '2.05.01', 'RESULTADO DE EXERCÍCIOS FUTUROS', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (186, '2.05.01.01.00', 'Receita de Exercícios Futuros',
        'A pessoa jurídica que explore as atividades de compra e venda, loteamento, incorporação e construção de imóveis indicará, nestas contas, o valor contratado das vendas a prazo ou a prestação para recebimento após o término do ano,calendário subseqüente, no caso de atividade imobiliária. Também se consideram como receitas de exercícios futuros os juros e demais receitas financeiras recebidos antecipadamente em transações financeiras.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (187, '2.05.01.02.00', '(,) Custos e Despesas Correspondentes',
        'Contas correspondentes aos custos e despesas de exercícios futuros correspondentes às receitas indicadas na conta precedente.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (188, '2.07', 'PATRIMÔNIO LÍQUIDO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (189, '2.07.01', 'CAPITAL REALIZADO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (190, '2.07.01.01.00', 'Capital Subscrito de Domiciliados e Residentes no País',
        'Contas correspondentes ao capital subscrito de domiciliados no País.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (191, '2.07.01.02.00', '(,) Capital a Integralizar de Domiciliados e Residentes no País',
        'Contas correspondentes ao capital social subscrito de domiciliados no País que não tenha sido integralizado.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (192, '2.07.01.03.00', 'Capital Subscrito de Domiciliados e Residentes no Exterior',
        'Contas correspondentes ao capital subscrito de domiciliados no exterior.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (193, '2.07.01.04.00', '(,) Capital a Integralizar de Domiciliados e Residentes no Exterior',
        'Contas correspondentes ao capital social subscrito de domiciliados no exterior que não tenha sido integralizado.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (194, '2.07.04', 'RESERVAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (195, '2.07.04.01.00', 'Reservas de Capital',
        'Contas correspondentes às reservas constituídas pela correção monetária do capital, por incentivos fiscais, por ágio na emissão de ações, por alienação de partes beneficiárias.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (196, '2.07.04.02.00', 'Reservas de Reavaliação',
        'Contas correspondentes aos saldos dos reservas de reavaliação ainda não realizadas, decorrentes de reavaliação de ativos próprios e de ativos de coligadas e controladas, estes avaliados pelo método da equivalência patrimonial.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (197, '2.07.04.03.00', 'Reservas de Lucros',
        'Contas correspondentes às reservas constituídas pela destinação de lucros da empresa, tais como: reserva legal, reservas estatutárias, reserva para contingências, reserva de lucros a realizar, reserva de lucros para expansão, reserva especial para dividendo obrigatório não distribuído e reserva de exaustão incentivada de recursos minerais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (198, '2.07.04.03.01', 'Reservas de Lucros , Doações e Subvenções para Investimentos',
        'Contas que registram, a partir de 01.01.2008, as doações e subvenções para investimento', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (199, '2.07.04.03.02', 'Reservas de Lucros , Prêmio na Emissão de Debêntures',
        'Contas que registram, a partir de 01.01.2008, os prêmios na emissão de debêntures', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (200, '2.07.04.04.00', 'Reserva para Aumento de Capital (Lei no 9.249/1995, art. 9o, § 9o)',
        'Conta correspondente à reserva constituída em 1996 com o montante dos juros sobre o capital próprio deduzidos como despesa financeira, mas mantidos no patrimônio da empresa, caso esta tenha optado pela faculdade prevista no § 9o do art. 9o da Lei no 9.249, de 1995.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (201, '2.07.04.05.00', 'Outras Reservas',
        'Contas correspondentes às demais reservas não consignadas nos itens anteriores, tais como o saldo devedor ou credor da conta de correção monetária correspondente à diferença, em relação ao ano de 1990, entre o IPC e o BTN Fiscal e o saldo da correção especial das contas do ativo permanente efetuada com base nos arts. 33 e 44 do Decreto no 332, de 1991.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (202, '2.07.05', 'AJUSTES DE AVALIAÇÃO PATRIMONIAL', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (203, '2.07.05.01.00', 'Ajustes às Normas Internacionais de Contabilidade',
        'Contrapartidas de aumentos ou diminuições de valor atribuídos a elementos do ativo e do passivo, em decorrência da sua avaliação a valor justo, nos casos previstos nesta Lei ou, em normas expedidas pela Comissão de Valores Mobiliários, com base na competência conferida pelo § 3o do art. 177 da Lei 6.404/76 (enquanto não computadas no resultado do exercício em obediência ao regime de competência,) ',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (204, '2.07.05.01.01', '(,) Ajustes às Normas Internacionais de Contabilidade',
        'Contrapartidas de aumentos ou diminuições de valor atribuídos a elementos do ativo e do passivo, em decorrência da sua avaliação a valor justo, nos casos previstos nesta Lei ou, em normas expedidas pela Comissão de Valores Mobiliários, com base na competência conferida pelo § 3o do art. 177 da Lei 6.404/76 (enquanto não computadas no resultado do exercício em obediência ao regime de competência,) ',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (205, '2.07.07', 'OUTRAS CONTAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (206, '2.07.07.01.00', 'Lucros Acumulados e/ou Saldo à Disposição da Assembléia',
        'Contas correspondentes aos lucros acumulados ou do saldo à disposição da assembléia.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (207, '2.07.07.02.00', '(,) Prejuízos Acumulados', 'Contas correspondentes aos prejuízos acumulados.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (208, '2.07.07.03.00', '(,) Ações em Tesouraria',
        'Contas que registrem as aquisições de ações da própria empresa.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (209, '2.07.07.04.00', 'Outras',
        'Outras contas classificáveis no patrimônio líquido que não tenham correspondência nas contas Lucros Acumulados e/ou Saldo à Disposição da Assembléia, Prejuízos Acumulados, Ações em Tesouraria.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (210, '2.08', 'PATRIMÔNIO SOCIAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (211, '2.08.01', 'FUNDO PATRIMONIAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (212, '2.08.01.01.00', 'Fundo Patrimonial',
        'Contas que registrem, nas instituições imunes ou isentas, o Fundo Patrimonial.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (213, '2.08.04', 'RESERVAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (214, '2.08.04.01.00', 'Reservas Patrimoniais',
        'Contas correspondentes, nas instituições imunes ou isentas, às reservas patrimoniais.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (215, '2.08.04.02.00', 'Reservas Estatutárias',
        'Contas correspondentes, nas instituições imunes ou isentas, às reservas estatutárias.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (216, '2.08.07', 'OUTRAS CONTAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (217, '2.08.07.01.00', 'Superávits Acumulados',
        'Contas correspondentes, nas instituições imunes ou isentas, aos superávits acumulados.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (218, '2.08.07.02.00', 'Déficits Acumulados',
        'Contas correspondentes, nas instituições imunes ou isentas, aos déficits acumulados.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (219, '3', 'RESULTADO LÍQUIDO DO PERÍODO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (220, '3.01', 'RESULTADO LÍQUIDO DO PERÍODO ANTES DO IRPJ E DA CSLL , ATIVIDADE GERAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (221, '3.01.01', 'RESULTADO OPERACIONAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (222, '3.01.01.01', 'RECEITA LIQUIDA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (223, '3.01.01.01.01', 'RECEITA BRUTA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (224, '3.01.01.01.01.01.00', 'Receita da Exportação de Produtos',
        'Contas que registram as receitas de exportação.', null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (225, '3.01.01.01.01.01.01', 'Receita de Exportação Direta de Mercadorias e Produtos',
        'Contas que registram o valor da receita auferida em decorrência da exportação direta de mercadorias e produtos.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (226, '3.01.01.01.01.01.02',
        'Receita de Vendas de Mercadorias e Produtos a Comercial Exportadora com Fim Específico de Exportação',
        'Contas que registram o valor da receita auferida em decorrência da venda de mercadorias e produtos a empresa comercial exportadora, com fim específico de exportação.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (227, '3.01.01.01.01.01.03', 'Receita de Exportação de Serviços',
        'Contas que registram o valor da receita auferida em decorrência da exportação direta de serviços', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (228, '3.01.01.01.01.02.00', 'Receita da Venda no Mercado Interno de Produtos de Fabricação Própria',
        'Contas que registram a receita auferida no mercado interno correspondente à venda de produtos de fabricação própria e as receitas auferidas na industrialização por encomenda ou por conta e ordem de terceiros. (Não se incluem o valor correspondente ao Imposto sobre Produtos Industrializados (IPI) cobrado destacadamente do comprador ou contratante, uma vez que o vendedor é mero depositário e este imposto não integra o preço de venda da mercadoria, e, também, o valor correspondente ao ICMS cobrado na condição de substituto.)',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (229, '3.01.01.01.01.03.00', 'Receita da Revenda de Mercadorias no Mercado Interno',
        'Contas que registram receita auferida no mercado interno, correspondente à revenda de mercadorias e o resultado auferido nas operações de conta alheia.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (230, '3.01.01.01.01.04.00', 'Receita da Prestação de Serviços  Mercado Interno',
        'Contas que registram a receita decorrente dos serviços prestados.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (231, '3.01.01.01.01.05.00', 'Receita das Unidades Imobiliárias Vendidas',
        'As pessoas jurídicas que exploram atividades imobiliárias devem indicar, nestas contas, o montante das receitas das unidades imobiliárias vendidas, apropriadas ao resultado, inclusive as receitas transferidas de Resultados de Exercícios Futuros e os custos recuperados de períodos de apuração anteriores.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (232, '3.01.01.01.01.06.00', 'Receita de Locação de Bens Móveis e Imóveis',
        'Contas que registram a receita decorrente da locação de bens móveis e imóveis', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (233, '3.01.01.01.01.07.00', 'Outras',
        'Outras contas que registrem valores componentes da receita bruta não especificadas nos itens anteriores.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (234, '3.01.01.01.03', 'DEDUÇÕES DA RECEITA BRUTA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (235, '3.01.01.01.03.01.00', '(,) Vendas Canceladas, Devoluções e Descontos Incondicionais',
        'Contas representativas das vendas canceladas, a devoluções de vendas e a descontos incondicionais concedidos sobre  receitas constantes das contas integrantes do grupo RECEITA BRUTA',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (236, '3.01.01.01.03.02.00', '(,) ICMS',
        'Contas que registram o total do Imposto Sobre Operações Relativas à Circulação de Mercadorias e Sobre Prestação de Serviços de Transporte Interestadual e Intermunicipal e de Comunicação (ICMS) calculado sobre as receitas das vendas e de serviços constantes das contas integrantes do grupo RECEITA BRUTA. Informar o resultado da aplicação das alíquotas sobre as respectivas receitas, e não o montante recolhido, durante o período de apuração, pela pessoa jurídica.O valor referente ao ICMS pago como substituto não deve ser incluído nesta conta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (237, '3.01.01.01.03.03.00', '(,) Cofins',
        'vigente à época da ocorrência dos fatos geradores, incidente sobre as receitas das contas integrantes do grupo RECEITA BRUTA. O valor informado deve ser apurado de forma centralizada pelo estabelecimento matriz, quando a pessoa jurídica possuir mais de um estabelecimento (Lei no 9.779, de 1999, art. 15, III). Não incluir a Cofins incidente sobre as demais receitas operacionais, que deverá ser informada em conta distinta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (238, '3.01.01.01.03.04.00', '(,) PIS/Pasep',
        'Contas que registram as contribuições para o PIS/Pasep apurado sobre a receita de vendas em consonância com a legislação vigente à época da ocorrência dos fatos geradores, incidente sobre as receitas das contas integrantes do grupo RECEITA BRUTA. O valor informado deve ser apurado de forma centralizada pelo estabelecimento matriz, quando a pessoa jurídica possuir mais de um estabelecimento (Lei no 9.779, de 1999, art. 15, III). Não incluir o PIS/Pasep incidente sobre as demais receitas operacionais, que deverá ser informada em conta distinta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (239, '3.01.01.01.03.05.00', '(,) ISS',
        'Contas que registram o Imposto sobre Serviço de qualquer Natureza (ISS) relativo às receitas de serviços,conforme legislação específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (240, '3.01.01.01.03.06.00', '(,) Demais Impostos e Contribuições Incidentes sobre Vendas e Serviços',
        'Contas que registrem os demais impostos e contribuições incidentes sobre as receitas das vendas de que tratam as contas integrantes do grupo RECEITA BRUTA, que guardem proporcionalidade com o preço e sejam considerados redutores das receitas de vendas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (241, '3.01.01.03', 'CUSTO DOS BENS E SERVIÇOS VENDIDOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (242, '3.01.01.03.01', 'CUSTO DOS PRODUTOS DE FABRICAÇÃO PRÓPRIA VENDIDOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (243, '3.01.01.03.01.00.00', 'Custo dos Produtos de Fabricação Própria Vendidos', '', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (244, '3.01.01.03.01.01.00', 'Estoques no Início do Período de Apuração',
        'Contas que registram os estoques de insumos, de produtos em elaboração e de produtos acabados existentes no início do período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (245, '3.01.01.03.01.02.00', 'Compras de Insumos à Vista',
        'Contas que registram as aquisições à vista, durante o período de apuração, de matéria,prima, material secundário e material de embalagem, no mercado interno e externo, para utilização no processo produtivo, os valores referentes aos custos com transporte e seguro até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (246, '3.01.01.03.01.03.00', 'Compras de Insumos a Prazo',
        'Contas que registram as aquisições a prazo, durante o período de apuração, de matéria,prima, material secundário e material de embalagem, no mercado interno e externo, para utilização no processo produtivo, os valores referentes aos custos com transporte e seguro até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (247, '3.01.01.03.01.04.00', 'Remuneração a Dirigentes de Indústria',
        'Contas que registram: a) a remuneração mensal e fixa dos dirigentes diretamente ligados à produção, pelo valor total do custo incorrido no período de apuração, exceto os encargos sociais (Previdência Social e FGTS) que são informados em conta distinta; b) o valor relativo aos custos incorridos com salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores, se ligados diretamente à produção (PN Cosit nº 11, de 30 de setembro de 1992). Atenção: deve ser incluído nesta conta o valor das gratificações dos dirigentes ligados à produção, inclusive o 13º salário.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (248, '3.01.01.03.01.05.00', 'Custo do Pessoal Aplicado na Produção',
        'Contas que representem do custo com ordenados, salários e outros custos com empregados ligados à produção da empresa, tais como: seguro de vida, contribuições ao plano PAIT, custos com programa de  previdência privada, contribuições para os Fundos de Aposentadoria Programada Individual (Fapi), e outras de caráter remuneratório.Inclusive os custos com supervisão direta, manutenção e guarda das instalações, decorrentes de vínculo empregatício com a pessoa jurídica.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (249, '3.01.01.03.01.06.00', 'Encargos Sociais  Previdência Social',
        'Contas que registram as contribuições para a Previdência Social (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (250, '3.01.01.03.01.07.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para o FGTS (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (251, '3.01.01.03.01.08.00', 'Encargos Sociais  Outros',
        'Contas que registram encargos sociais, relativos ao pessoal ligado diretamente à produção, não classificados nas contas Encargos Sociais  Previdência Social e Encargos Sociais  FGTS .',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (252, '3.01.01.03.01.09.00', 'Alimentação do Trabalhador',
        'Contas que registram os custos com alimentação do pessoal ligado diretamente à produção, realizados durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (253, '3.01.01.03.01.10.00', 'Manutenção e Reparo de Bens Aplicados na Produção',
        'Contas que representam somente os custos realizados com reparos que não implicaram aumento superior a um ano da vida útil prevista no ato da aquisição do bem.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (254, '3.01.01.03.01.11.00', 'Arrendamento Mercantil',
        'Contas que representam o valor do custo incorrido a título de contraprestação de arrendamento mercantil de bens alocados na produção, segundo contratos celebrados com observância da Lei nº 6.099, de 12 de setembro de 1974, com as alterações da Lei nº 7.132, de 26 de outubro de 1983. Os custos com aluguel de outros bens alocados à produção, mediante contrato diferente do de arrendamento mercantil, devem ser indicados em Outros Custos. Os valores referentes a bens que não sejam intrinsecamente relacionados com a produção devem ser informados na conta Arrendamento Mercantil do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (255, '3.01.01.03.01.12.00', 'Encargos de Depreciação, Amortização e Exaustão',
        'Contas que registram os encargos a esses títulos com bens aplicados diretamente na produção. Os encargos que não forem decorrentes de bens intrinsecamente relacionados com a produção devem ser informados na conta Encargos de Depreciação e Amortização do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (256, '3.01.01.03.01.13.00', 'Constituição de Provisões',
        'Contas que registram os encargos com a constituição de provisões que devam ser imputados aos custos de produção da empresa no período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (257, '3.01.01.03.01.14.00', 'Serviços Prestados por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados à pessoa jurídica por pessoa física sem vínculo empregatício, relacionados com a atividade industrial da pessoa jurídica .',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (258, '3.01.01.03.01.15.00', 'Serviços Prestados Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados por pessoa jurídica, relacionados com atividade industrial da pessoa jurídica declarante.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (259, '3.01.01.03.01.16.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (260, '3.01.01.03.01.17.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (261, '3.01.01.03.01.18.00', 'Outros Custos',
        'Contas que representam os demais custos da empresa no processo de produção, para os quais não haja conta mais específica ou cujas classificações contábeis não se adaptem à nomenclatura específica, tais como: custo referente ao valor de bens de consumo eventual; as quebras ou perdas de estoque, e as ocorridas na fabricação, no transporte e manuseio.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (262, '3.01.01.03.01.19.00', '(,) Estoques no Final do Período de Apuração',
        'Contas que representam o valor total dos estoques existentes no final do período de apuração, conforme a seguir: a) os estoques relativos aos insumos devem ser avaliados com exclusão dos impostos e contribuições recuperáveis, observadas as disposições da legislação pertinente; b) os estoques de produtos em elaboração devem ser avaliados com exclusão dos impostos e contribuições recuperáveis. O contribuinte que mantiver sistema de contabilidade de custo integrado e coordenado com o restante da escrituração pode utilizar os custos nele apurados para avaliação dos estoques de produtos em fabricação. Em caso negativo, tais estoques devem ser avaliados segundo o disposto no art. 296 do Decreto nº 3.000, de 1999, hipótese em que o valor de uma unidade em fabricação é avaliada: b.1) pela soma dos produtos obtidos mediante a multiplicação da quantidade de cada matéria,prima agregada por uma vez e meia o maior custo dessa matéria,prima no período de apuração; ou b.2) em 80% (oitenta por cento) do valor do produto acabado que tiver sido avaliado em 70% (setenta por cento) do maior preço de venda, sem exclusão do ICMS, no período de apuração. Os critérios de avaliação acima referidos devem ser observados na escrituração da empresa. c) os estoques de produtos acabados de fabricação própria devem ser inventariados no último dia do período de apuração. Se a empresa mantiver sistema de contabilidade de custo integrado e coordenado com o restante da escrituração pode utilizar os custos nele apurados para avaliação dos estoques de produtos acabados. Caso contrário, deverá observar, na contabilidade, a avaliação desses estoques tomando por base 70% (setenta por cento) do maior preço de venda do produto durante o período de apuração, sem exclusão do ICMS.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (263, '3.01.01.03.03', 'CUSTO DAS MERCADORIAS REVENDIDAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (264, '3.01.01.03.03.00.00', 'Custo das Mercadorias Revendidas', '', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (265, '3.01.01.03.03.01.00', 'Estoques no Início do Período de Apuração',
        'Contas que registram os estoques de mercadorias para revenda existentes no início do período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (266, '3.01.01.03.03.02.00', 'Compras de Mercadorias à Vista',
        'Contas que representam: a) o valor das mercadorias adquiridas à vista, no período de apuração, e destinadas à revenda; b) valor das mercadorias para revenda importadas do exterior pela própria pessoa jurídica. Quando for o caso, devem ser adicionados ao valor das compras de mercadorias os custos com transporte e seguro dessas mercadorias até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro. Atenção: não devem ser informados os valores relativos a transferências de mercadorias entre matriz e filiais e entre filiais.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (267, '3.01.01.03.03.03.00', 'Compras de Mercadorias a Prazo',
        'Contas que representam: a) o valor das mercadorias adquiridas a prazo, no período de apuração, e destinadas à revenda; b) valor das mercadorias para revenda importadas do exterior pela própria pessoa jurídica. Quando for o caso, devem ser adicionados ao valor das compras de mercadorias os custos com transporte e seguro dessas mercadorias até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro. Atenção: não devem ser informados os valores relativos a transferências de mercadorias entre matriz e filiais e entre filiais.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (268, '3.01.01.03.03.04.00', '(,) Estoques no Final do Período de Apuração',
        'Contas que registram dos estoques de mercadorias para revenda existentes na data de encerramento do período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (269, '3.01.01.03.05', 'CUSTO DOS SERVIÇOS VENDIDOS', '', null, '2008,12,31', 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (270, '3.01.01.03.05.00.00', 'Custo dos Serviços Vendidos', '', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (271, '3.01.01.03.05.01.00', 'Saldo Inicial de Serviços em Andamento',
        'Contas que registram os serviços não acabados constante do balanço correspondente ao período de apuração imediatamente anterior.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (272, '3.01.01.03.05.02.00', 'Material Aplicado na Produção dos Serviços',
        'Contas correspondentes aos materiais aplicados diretamente na produção dos serviços durante o período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (273, '3.01.01.03.05.03.00', 'Remuneração de Dirigentes de Produção dos Serviços',
        'Contas que registram: a) a remuneração mensal e fixa dos dirigentes diretamente ligados à produção dos serviços pelo valor total do custo incorrido no período de apuração, exceto os encargos sociais (Previdência Social e FGTS) que são informados em conta distinta; b) o valor relativo aos custos incorridos com salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores, se ligados diretamente à produção (PN Cosit nº 11, de 30 de setembro de 1992). Atenção: deve ser incluído nesta conta o valor das gratificações dos dirigentes ligados à produção, inclusive o 13º salário.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (274, '3.01.01.03.05.04.00', 'Custo do Pessoal Aplicado na Produção dos Serviços',
        'Contas que registram os custos com mão,de,obra com vínculo empregatício aplicada diretamente na produção dos serviços.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (275, '3.01.01.03.05.05.00', 'Serviços Prestados por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados à pessoa jurídica por pessoa física sem vínculo empregatício, relacionados com a atividade de prestação de serviços da empresa.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (276, '3.01.01.03.05.06.00', 'Serviços Prestados Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados por pessoa jurídica à pessoa jurídica declarante, relacionados com sua atividade de prestação de serviços.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (277, '3.01.01.03.05.07.00', 'Encargos Sociais  Previdência Social',
        'Contas que registram as contribuições para a Previdência Social (inclusive dos dirigentes ligados à prestação dos serviços , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção dos serviços.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (278, '3.01.01.03.05.08.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para o FGTS (inclusive dos dirigentes de empresa , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção dos serviços.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (279, '3.01.01.03.05.09.00', 'Encargos Sociais  Outros',
        'Contas que registram encargos sociais, relativos ao pessoal ligado diretamente à produção dos serviços, não classificados nas contas Encargos Sociais  Previdência Social e Encargos Sociais  FGTS.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (280, '3.01.01.03.05.10.00', 'Alimentação do Trabalhador',
        'Contas que registram os custos com alimentação do pessoal ligado diretamente à produção dos serviços, realizados durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (281, '3.01.01.03.05.11.00', 'Encargos de Depreciação e Amortização',
        'Contas que registram os encargos a esses títulos com bens aplicados diretamente na produção dos serviços. Os encargos que não forem decorrentes de bens intrinsecamente relacionados com a produção devem ser informados na conta Encargos de Depreciação e Amortização do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (282, '3.01.01.03.05.12.00', 'Arrendamento Mercantil',
        'Contas que representam o valor do custo incorrido a título de contraprestação de arrendamento mercantil de bens alocados na produção dos serviços, segundo contratos celebrados com observância da Lei no 6.099, de 12 de setembro de 1974, com as alterações da Lei no 7.132, de 26 de outubro de 1983. Os custos com aluguel de outros bens alocados à produção, mediante contrato diferente do de arrendamento mercantil, devem ser indicados em Outros Custos. Os valores referentes a bens que não sejam intrinsecamente relacionados com a produção devem ser informados na conta Arrendamento Mercantil do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (283, '3.01.01.03.05.13.00', 'Constituição de Provisões',
        'Contas que registram os encargos com a constituição de provisões que devam ser imputados aos custos de produção da empresa no período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (284, '3.01.01.03.05.14.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram  as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade de prestação de serviços.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (285, '3.01.01.03.05.15.00', 'Royalties e Assistência Técnica , EXTERIOR',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliadas no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade de prestação de serviços.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (286, '3.01.01.03.05.16.00', 'Outros Custos',
        'Contas que representam os demais custos da empresa no processo de produção dos serviços, para os quais não haja conta mais específica ou cujas classificações contábeis não se adaptem à nomenclatura específica, tais como:custo referente ao valor de bens de consumo eventual; as quebras ou perdas de estoque, e as ocorridas na fabricação, no transporte e manuseio.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (287, '3.01.01.03.05.17.00', '(,) Saldo Final de Serviços em Andamento',
        'Contas que representam os serviços não acabados e não faturados constantes do balanço correspondente ao período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (288, '3.01.01.03.07', 'CUSTO DAS UNIDADES IMOBILIÁRIAS VENDIDAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (289, '3.01.01.03.07.01.00', 'Custo das Unidades Imobiliárias Vendidas',
        'Contas que registram, na empresa que tiver por objeto a compra de imóveis para venda ou que promover empreendimento de desmembramento ou loteamento de terrenos, incorporação imobiliária ou construção de prédio destinado à venda, os valores dos custos correspondentes às unidades imobiliárias vendidas apropriados ao resultado do período de apuração. A recuperação de custos do próprio período é computada no montante a ser indicado nesta conta. Os custos recuperados correspondentes a períodos de apuração anteriores devem ser indicados na conta Receita das Unidades Imobiliárias Vendidas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (290, '3.01.01.03.09', 'AJUSTES DE ESTOQUES DECORRENTES DE ARBITRAMENTO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (291, '3.01.01.03.09.01.00', 'Ajustes de Estoques Decorrentes de Arbitramento',
        'Contas que, na pessoa jurídica submetida à apuração anual do imposto e que teve seu lucro arbitrado em um ou mais trimestres do ano,calendário, representam o  valor, positivo ou negativo, correspondente à diferença entre os estoques iniciais do período imediatamente subseqüente ao arbitramento e os estoques finais do período imediatamente anterior ao arbitramento. Caso haja arbitramento em mais de um trimestre do ano,calendário, não consecutivos, as contas devem representar a soma algébrica das diferenças apuradas em relação a cada período arbitrado.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (292, '3.01.01.05', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (293, '3.01.01.05.01', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (294, '3.01.01.05.01.01.00', 'Variações Cambiais Ativas',
        'Contas que registram os ganhos apurados em razão de variações ativas Decorrentes da atualização dos direitos de crédito e obrigações, calculados com base nas variações nas taxas de câmbio. Atenção: 1) As variações cambiais ativas decorrentes dos direitos de crédito e de obrigações, em função da taxa de câmbio, são consideradas como receita financeira, inclusive para fins de cálculo do lucro da exploração (Lei nº 9.718, art. 9º c/c art. 17); 2) Nas atividades de compra e venda, loteamento, incorporação e construção de imóveis, as variações cambiais ativas são reconhecidas como receita segundo as normas constantes da IN SRF nº 84/79, de 20 de dezembro de 1979, da IN SRF nº 23/83, de 25 de março de 1983, e da IN SRF nº 67/88, de 21 de abril de 1988 (IN SRF nº 25/99, de 25 de fevereiro de 1999).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (295, '3.01.01.05.01.02.00', 'Ganhos Auferidos no Mercado de Renda Variável, exceto Day,Trade',
        'Contas que registram: a) o somatório dos ganhos auferidos, em cada mês do período de apuração, em operações realizadas nas bolsas de valores, de mercadorias, de futuros e assemelhadas, existentes no País; b) os ganhos auferidos nas alienações, fora de bolsa, de ouro, ativo financeiro, e de participações societárias, exceto as alienações de participações societárias permanentes em sociedades coligadas e controladas e de participações societárias que permanecerem no ativo da pessoa jurídica até o término do ano,calendário seguinte ao de suas aquisições; e c) os rendimentos auferidos em operações de swap e no resgate de quota de fundo de investimento cujas carteiras sejam constituídas, no mínimo, por 67% (sessenta e sete por cento) de ações no mercado à vista de bolsa de valores ou entidade assemelhada (Lei nº 9.532, de 1997, art. 28, alterado pela MP nº 1.636, de 1998, art. 2º, e reedições).Considera,se ganho o resultado positivo auferido nas operações citadas acima, realizadas em cada mês, admitida a dedução dos custos e despesas incorridos, necessários à realização das operações. Atenção: 1) Os ganhos auferidos em operações day,trade devem ser informados em conta específica. 2) O valor correspondente às perdas incorridas no mercado de renda variável, exceto day,trade, deve ser informado em conta específica. 3) São consideradas assemelhadas às bolsas de valores, de mercadorias e de futuros as entidades cujo objeto social seja  análogo ao das referidas bolsas e que funcionem sob a supervisão e fiscalização da Comissão de Valores Mobiliários (CVM).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (296, '3.01.01.05.01.03.00', 'Ganhos em Operações Day,Trade',
        'Contas que registram os ganhos diários auferidos, em cada mês do período de apuração, em operações day,trade. Considera,se ganho o resultado positivo auferido nas operações citadas acima, realizadas em cada mês, admitida a dedução dos custos e despesas incorridos, necessários à realização das operações. Não se caracteriza como day,trade o exercício da opção e a venda ou compra do ativo no mercado à vista, no mesmo dia. Também não se caracterizam como day,trade as operações iniciadas por intermédio de uma instituição e encerradas em outra, quando houver a liquidação física mediante movimentação de títulos ou valores mobiliários em custódia. Atenção: o valor correspondente às perdas incorridas nas operações day,trade deve ser informado em conta específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (297, '3.01.01.05.01.04.00', 'Receitas de Juros sobre o Capital Próprio',
        'Contas que registram os juros recebidos, a título de remuneração do capital próprio, em conformidade com o art. 9o da Lei no 9.249, de 1995. O valor informado deve corresponder ao total dos juros recebidos antes do desconto do imposto de renda na fonte. O valor do imposto de renda retido na fonte, para as pessoas jurídicas tributadas pelo lucro real, é considerado antecipação do imposto devido no encerramento do período de apuração ou, ainda, pode ser compensado com aquele que for retido, pela beneficiária, por ocasião do pagamento ou crédito de juros a título de remuneração do capital próprio, ao seu titular ou aos seus sócios.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (298, '3.01.01.05.01.05.00', 'Outras Receitas Financeiras',
        'Contas que registram receitas auferidas no período de apuração relativas a juros, descontos, lucro na operação de reporte, prêmio de resgate de títulos ou debêntures e rendimento nominal auferido em aplicações financeiras de renda fixa, não incluídas nas contas precedentes deste  grupo. As receitas dessa natureza, derivadas de operações com títulos vencíveis após o encerramento do período de apuração, serão rateadas segundo o regime de competência.Atenção: 1) As variações monetárias ativas decorrentes da atualização dos direitos de crédito e das obrigações, em função de índices ou coeficientes aplicáveis por disposição legal ou contratual, devem ser informadas como receita financeira; 2) As variações cambiais ativas devem ser informadas na conta Variações Cambiais Ativas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (299, '3.01.01.05.01.06.00', 'Ganhos na Alienação de Participações Não Integrantes do Ativo Permanente',
        'Contas que registram os ganhos auferidos na alienação de ações, títulos ou quotas de capital não integrantes do ativo permanente, desde que não incluídos na conta Ganhos Auferidos no Mercado de Renda Variável, exceto Day,Trade.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (300, '3.01.01.05.01.07.00', 'Resultados Positivos em Participações Societárias',
        'Contas que registram: a) os lucros e dividendos derivados de investimentos avaliados pelo custo de aquisição; b) os ganhos por ajustes no valor de investimentos relevantes avaliados pelo método da equivalência patrimonial, decorrentes de lucros apurados nas controladas e coligadas. Atenção: considera,se controlada a filial, a agência, a sucursal, a dependência ou o escritório de representação no exterior, sempre que os respectivos ativos e passivos não estejam incluídos na contabilidade da investidora, por força de normatização específica. c) as bonificações recebidas.  Atenção: 1) as bonificações recebidas, decorrentes da incorporação de lucros ou reservas não tributados na forma do art. 35 da Lei nº 7.713, de 1988, ou apurados nos anos,calendário de 1994 ou 1995, são consideradas a custo zero, não afetando o valor do investimento nem o resultado do período de apuração (art. 3º da Lei nº 8.849, de 1994, e art. 3º da Lei nº 9.064, de 1995). 2) o caso de investimento avaliado pelo custo de aquisição, as bonificações recebidas, decorrentes da incorporação de lucros ou reservas tributados na forma do art. 35 da Lei nº 7.713, de 1988, e de lucros ou reservas apurados no ano,calendário de 1993 ou a partir do ano,calendário de 1996, são registradas tomando,se como custo o valor da parcela dos lucros ou reservas capitalizados. d) os lucros e dividendos de participações societárias avaliadas pelo custo de aquisição; Atenção: os lucros ou dividendos recebidos em decorrência de participações societárias avaliadas pelo custo de aquisição adquiridas até 6 (seis) meses antes da data do recebimento devem ser registrados como diminuição do valor do custo, não sendo incluídos nesta conta. e) os resultados positivos decorrentes de participações societárias no exterior avaliadas pelo patrimônio líquido, os dividendos de participações avaliadas pelo custo de aquisição e os resultados de equivalência patrimonial relativos a filiais, sucursais ou agências da pessoa jurídica localizadas no exterior, em decorrência de operações realizadas naquelas filiais, sucursais ou agências. Os lucros auferidos no exterior serão adicionados ao lucro líquido, para efeito de determinação do lucro real, no período de apuração correspondente ao balanço levantado em 31 de dezembro do ano,calendário em que tiverem sido disponibilizados, observando,se o disposto nos arts. 394 e 395 do Decreto nº 3.000, de 1999, e no art. 74 da Medida Provisória nº 2.158,35, de 24 de agosto de 2001.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (301, '3.01.01.05.01.07.10',
        'Amortização de Deságio nas Aquisições de Investimentos Avaliados pelo Patrimônio Líquido',
        'Contas que registram as amortizações de deságios nas aquisições de investimentos avaliados pelo patrimônio líquido. O valor amortizado que for excluído do lucro líquido para determinação do lucro real deve ser controlado na Parte B do Livro de Apuração do Lucro Real até a alienação ou baixa da participação societária, quando, então, deve ser adicionado ao lucro líquido para determinação do lucro real no período de apuração em que for computado o ganho ou perda de capital havido.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (302, '3.01.01.05.01.08.00', 'Resultados Positivos em SCP',
        'Conta utilizada pelas pessoas jurídicas que forem sócias ostensivas de sociedades em conta de participação, para o registro: a) de lucros derivados de participação em SCP, avaliadas pelo custo de aquisição; b) dos ganhos por ajustes no valor de participação em SCP, avaliadas pelo método da equivalência patrimonial. Atenção: os lucros recebidos de investimento em SCP, avaliado pelo custo de aquisição, ou a contrapartida do ajuste do investimento ao valor do patrimônio líquido da SCP, no caso de investimento avaliado por esse método, podem ser excluídos na determinação do lucro real dos sócios, pessoas jurídicas, das referidas sociedades (Decreto nº 3.000, de 1999, art. 149).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (303, '3.01.01.05.01.09.00', 'Rendimentos e Ganhos de Capital Auferidos no Exterior',
        'Contas que registram os rendimentos e ganhos de capital auferidos no exterior diretamente pela pessoa jurídica domiciliada no Brasil, pelos seus valores antes de descontado o tributo pago no país de origem. Esses valores podem, no caso de apuração trimestral do imposto, ser excluídos na apuração do lucro real do 1o ao 3o trimestres, devendo ser adicionados ao lucro líquido na apuração do lucro real referente ao 4º trimestre. Atenção: Os ganhos de capital referentes a alienações de bens e direitos do ativo permanente situados no exterior devem ser informados na conta Outras Receitas Não Operacionais..',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (304, '3.01.01.05.01.10.00', 'Reversão dos Saldos das Provisões Operacionais',
        'Contas que registram a reversão de saldos não utilizados das provisões constituídas no balanço do período de apuração imediatamente anterior para fins de apuração do lucro real (Lei no 9.430, de 1996, art. 14).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (305, '3.01.01.05.01.10.10', 'Prêmios Recebidos na Emissão de Debêntures',
        'Contas que registram, a partir de 01.01.2008, os prêmios recebidos na emissão de debêntures.', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (306, '3.01.01.05.01.10.20', 'Doações e Subvenções para Investimentos',
        'Contas que registram, a partir de 01.01.2008, as doações e subvenções para investimento.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (307, '3.01.01.05.01.10.30', 'Contrapartida dos Ajustes ao Valor Presente',
        'Contrapartida do ajuste ao valor presente dos elementos do ativo e do passivo (art. 183, inciso VIII, e art. 184, inciso III da Lei 6.404/76)',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (308, '3.01.01.05.01.10.40', 'Contrapartida de outros Ajustes às Normas Internacionais de Contabilidade',
        'Contrapartida de outros ajustes decorrentes da adequação às Normas Internacionais de Contabilidade',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (309, '3.01.01.05.01.11.00', 'Outras Receitas Operacionais',
        'Contas que registram todas as demais receitas que, por definição legal, sejam consideradas operacionais, tais como: a) aluguéis de bens por empresa que não tenha por objeto a locação de móveis e imóveis; b) recuperações de despesas operacionais de períodos de apuração anteriores, tais como: prêmios de seguros, importâncias levantadas das contas vinculadas do FGTS, ressarcimento de desfalques, roubos e furtos, etc. As recuperações de custos e despesas no decurso do próprio período de apuração devem ser creditadas diretamente às contas de resultado em que foram debitadas; c) os créditos presumidos do IPI para ressarcimento do valor da Contribuição ao PIS/Pasep e Cofins; d) multas ou vantagens a título de indenização em virtude de rescisão contratual (Lei nº 9.430, de 1996, art. 70, § 3º, II); e) o crédito presumido da contribuição para o PIS/Pasep e da Cofins concedido na forma do art. 3º da Lei nº 10.147, de 2000.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (310, '3.01.01.07', 'DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (311, '3.01.01.07.01', 'DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (312, '3.01.01.07.01.01.00', 'Remuneração a Dirigentes e a Conselho de Administração',
        'Contas que registram a despesa incorrida relativa à remuneração mensal e fixa atribuída ao titular de firma individual, aos sócios, diretores e administradores de sociedades, ou aos representantes legais de sociedades estrangeiras, as despesas incorridas com os salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores (PN Cosit nº 11, de 1992), e o valor referente às remunerações atribuídas aos membros do conselho fiscal/administração/consultivo.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (313, '3.01.01.07.01.02.00', 'Ordenados, Salários, Gratificações e Outras Remunerações a Empregados',
        'Contas que registram as despesas com ordenados, salários, gratificações e outras despesas com empregados, tais como: comissões, moradia, seguro de vida, contribuições pagas ao plano PAIT, despesas com programa de previdência privada, contribuições para os Fundos de Aposentadoria Programada Individual (Fapi), e outras de caráter remuneratório. Atenção: 1) As despesas correspondentes a salários, ordenados, gratificações e outras remunerações referentes à área de saúde, tais como assistência médica, odontológica e farmacêutica, devem ser indicadas na conta Assistência Médica, Odontológica e Farmacêutica a Empregados. 2) Não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (314, '3.01.01.07.01.02.01', 'Ordenados, Salários Gratificações e Outras Remunerações a Empregados',
        'Contas que registram as despesas com ordenados, salários, gratificações e outras despesas com empregados, tais como: comissões, moradia, seguro de vida e outras de caráter remuneratório. Atenção: 1) As despesas correspondentes a salários, ordenados, gratificações e outras remunerações referentes à área de saúde, tais como assistência médica, odontológica e farmacêutica, devem ser indicadas na conta específica. 2) Não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (315, '3.01.01.07.01.02.03', 'Planos de Poupança e Investimentos de Empregados',
        'Contas que registram o valor total dos gastos efetuados com Planos de Poupança e Investimentos (PAIT).',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (316, '3.01.01.07.01.02.05', 'Fundo de Aposentadoria Programada Individual de Empregados',
        'Contas que registram o valor total dos gastos efetuados com Fundos de Aposentadoria Programada Individual (FAPI).',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (317, '3.01.01.07.01.02.07', 'Plano de Previdência Privada de Empregados',
        'Contas que registram o valor total dos gastos efetuados com Planos de Previdência Privada.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (318, '3.01.01.07.01.02.09', 'Outros Gastos com Pessoal',
        'Contas que registram os gastos com empregados não enquadrados nas contas precedentes Atenção: 1) As despesas correspondentes a salários, ordenados, gratificações e outras remunerações referentes à área de saúde, tais como assistência médica, odontológica e farmacêutica, devem ser indicadas na conta  Assistência Médica, Odontológica e Farmacêutica a Empregados; 2) não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (319, '3.01.01.07.01.03.00', 'Prestação de Serviços por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica, as despesas correspondentes aos serviços prestados por pessoa física que não tenha vínculo empregatício com a pessoa jurídica, tais como: comissões, corretagens, gratificações, honorários, direitos autorais e outras remunerações, inclusive as relativas a empreitadas de obras exclusivamente de trabalho e as decorrentes de fretes e carretos em geral.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (320, '3.01.01.07.01.04.00', 'Prestação de Serviço Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica, o valor das despesas correspondentes aos serviços prestados por outra pessoa jurídica à pessoa jurídica declarante.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (321, '3.01.01.07.01.04.01', 'Serviços Prestados por Cooperativa de Trabalho',
        'Contas que registram os serviços prestados por cooperativa de trabalho', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (322, '3.01.01.07.01.04.02', 'Locação de Mão,de,obra',
        'Contas que registram o valor total dos gastos efetuados no período com a contratação de serviços executados mediante cessão de mão,de,obra ou empreitada, inclusive em regime temporário, sujeitos à retenção de contribuição previdenciária, nos termos do art. 219 do Regulamento da Previdência Social , RPS, aprovado pelo Decreto nº 3.048, de 1999',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (323, '3.01.01.07.01.05.00', 'Encargos Sociais  Previdência Social',
        'Contas que registram as contribuições para a Previdência Social, não computadas nos custos (inclusive dos dirigentes  PN CST no 35, de 31 de agosto de 1981).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (324, '3.01.01.07.01.06.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para a o FGTS, não computadas nos custos (inclusive dos dirigentes , PN CST no 35, de 31 de agosto de 1981).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (325, '3.01.01.07.01.07.00', 'Encargos Sociais  Outros',
        'Contas que registram os demais encargos sociais, não computados nos custos ou nas contas Encargos Sociais , Previdência Social ou Encargos Sociais , FGTS',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (326, '3.01.01.07.01.08.00', 'Doações e Patrocínios de Caráter Cultural e Artístico (Lei no 8.313/1991)',
        'Contas que registram as doações e patrocínios efetuados no período de apuração em favor de projetos culturais previamente aprovados pelo Ministério da Cultura ou pela Agência Nacional do Cinema (Ancine), observada a legislação de concessão dos projetos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (327, '3.01.01.07.01.09.00', 'Doações a Instituições de Ensino e Pesquisa (Lei nº 9.249/1995, art.13, § 2º)',
        'Contas que registram as doações a instituições de ensino e pesquisa cuja criação tenha sido autorizada por lei federal e que preencham os requisitos dos incisos I e II do art. 213 da Constituição Federal, de 1988, que são: a) comprovação de finalidade não,lucrativa e aplicação dos excedentes financeiros em educação; b) assegurar a destinação do seu patrimônio a outra escola comunitária, filantrópica ou confessional, ou ao Poder Público, no caso de encerramento de suas atividades.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (328, '3.01.01.07.01.10.00', 'Doações a Entidades Civis',
        'Contas que registram as doações efetuadas a: a) entidades civis, legalmente constituídas no Brasil, sem fins lucrativos, que prestem serviços gratuitos em benefício de empregados da pessoa jurídica doadora, e respectivos dependentes, ou em benefício da comunidade na qual atuem; e b) Organizações da Sociedade Civil de Interesse Público (OSCIP), qualificadas segundo as normas estabelecidas na Lei no 9.790, de 23 de março de 1999.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (329, '3.01.01.07.01.11.00', 'Outras Contribuições e Doações',
        'Contas que registram as doações feitas, entre outras, aos Fundos controlados pelos Conselhos Municipais, Estaduais e Nacional dos Direitos da Criança e do Adolescente.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (330, '3.01.01.07.01.12.00', 'Alimentação do Trabalhador',
        'Contas que registram as despesas com alimentação do pessoal não ligado à produção, realizadas durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (331, '3.01.01.07.01.13.00', 'PIS/Pasep',
        'Contas que registram as Contribuições para o PIS/Pasep incidente sobre as demais receitas operacionais.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (332, '3.01.01.07.01.14.00', 'Cofins',
        'Contas que registram a parcela da Cofins incidente sobre as demais receitas operacionais.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (333, '3.01.01.07.01.15.00', 'CPMF',
        'Contas que registram a Contribuição Provisória sobre Movimentação ou Transmissão de Valores e de Créditos de Natureza Financeira.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (334, '3.01.01.07.01.16.00', 'Demais Impostos, Taxas e Contribuições, exceto IR e CSLL',
        'Contas que registram os demais Impostos, Taxas e Contribuições, exceto: a) incorporadas ao custo de bens do ativo permanente; b) correspondentes aos impostos não recuperáveis, incorporados ao custo das matérias,primas, materiais secundários, materiais de embalagem e mercadorias destinadas à revenda; c) correspondentes aos impostos recuperáveis; d) correspondentes aos impostos e contribuições redutores da receita bruta; e) correspondentes às Contribuições para o PIS/Pasep e à Cofins incidentes sobre as demais receitas operacionais, e à CPMF, indicados em contas específicas; f) correspondentes à contribuição social sobre o lucro líquido e ao imposto de renda devidos, que são informados em contas específicas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (335, '3.01.01.07.01.17.00', 'Arrendamento Mercantil',
        'Contas que registram as despesas, não computadas nos custos, pagas ou creditadas a título de contraprestação de arrendamento mercantil, decorrentes de contrato celebrado com observância da Lei no 6.099, de 12 de setembro de 1974, com as alterações da Lei no 7.132, de 26 de outubro de 1983, e da Portaria MF no 140, de 1984',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (336, '3.01.01.07.01.18.00', 'Aluguéis',
        'Contas que registram as despesas com aluguéis não decorrentes de arrendamento mercantil.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (337, '3.01.01.07.01.19.00', 'Despesas com Veículos e de Conservação de Bens e Instalações',
        'Contas que registram as despesas relativas aos bens que não estejam ligados diretamente à produção, as realizadas com reparos que não impliquem aumento superior a um ano da vida útil do bem, prevista no ato de sua aquisição, e as relativas a combustíveis e lubrificantes para veículos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (338, '3.01.01.07.01.20.00', 'Propaganda e Publicidade',
        'Contas que registram as despesas com propaganda e publicidade.', null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (339, '3.01.01.07.01.20.01',
        'Propaganda, Publicidade e Patrocínio (Associações Desportivas que Mantenham Equipe de Futebol Profissional)',
        'Contas que registram as despesas relativas a propaganda publicidade e patrocínio com associações desportivas que mantenham equipe de futebol profissional e possuam registro na Federação de Futebol do respectivo Estado, a título de propaganda, publicidade e patrocínio.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (340, '3.01.01.07.01.20.02', 'Propaganda, Publicidade e Patrocínio',
        'Contas que registram de propaganda, publicidade, exceto as classificadas na conta precedente', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (341, '3.01.01.07.01.21.00', 'Multas', 'Contas que registram as despesas com multas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (342, '3.01.01.07.01.22.00', 'Encargos de Depreciação e Amortização',
        'Contas que registram apenas os encargos a esses títulos, com bens não aplicados diretamente na produção. Inclui a amortização dos ajustes de variação cambial contabilizada no ativo diferido, relativa à atividade geral da pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (343, '3.01.01.07.01.23.00', 'Perdas em Operações de Crédito',
        'Contas que registram as perdas no recebimento de créditos decorrentes das atividades da pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (344, '3.01.01.07.01.24.00', 'Provisões para Férias e 13o Salário de Empregados',
        'Contas que registram as despesas com a constituição de provisões para: a) pagamento de remuneração correspondente a férias e adicional de férias de empregados, inclusive encargos sociais (Decreto no 3.000, de 1999, art. 337, e PN CST no 7, de 1980); b) o 13o salário, no caso de apuração trimestral do imposto, inclusive encargos sociais (Decreto no 3.000, de 1999, art. 338).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (345, '3.01.01.07.01.25.00', 'Provisão para Perda de Estoque',
        'Contas que registram as despesas com a constituição de provisão para perda de estoque', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (346, '3.01.01.07.01.26.00', 'Demais Provisões',
        'Contas que registram as despesas com provisões não relacionadas  em contas específicas', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (347, '3.01.01.07.01.27.00', 'Gratificações a Administradores',
        'Contas que registram as gratificações a administradores.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (348, '3.01.01.07.01.28.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram as despesas correspondentes às importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que não estejam relacionados com a produção de bens e/ou serviços.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (349, '3.01.01.07.01.29.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as despesas correspondentes às importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que não estejam relacionados com a produção de bens e/ou serviços.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (350, '3.01.01.07.01.30.00', 'Assistência Médica, Odontológica e Farmacêutica a Empregados',
        'Indicar o valor das despesas com assistência médica, odontológica e farmacêutica. Atenção: o valor referente à contratação de serviços de profissionais liberais sem vínculo empregatício ou de sociedades civis deve ser informado nas contas Prestação de Serviços por Pessoa Física sem Vínculo Empregatício ou Prestação de Serviço Pessoa Jurídica, conforme o caso.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (351, '3.01.01.07.01.31.00', 'Pesquisas Científicas e Tecnológicas',
        'Contas que registram as despesas efetuadas a esse título, inclusive a contrapartida das amortizações daquelas registradas no ativo diferido',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (352, '3.01.01.07.01.32.00', 'Bens de Natureza Permanente Deduzidos como Despesa',
        'Contas que registram as despesas com aquisição de bens do ativo imobilizado cujo prazo de vida útil não ultrapasse um ano, ou, caso exceda esse prazo, tenha valor unitário igual ou inferior ao fixado no art. 301 do Decreto no 3.000, de 1999.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (353, '3.01.01.07.01.33.00', 'Outras Despesas Operacionais',
        'Contas que registram as demais despesas operacionais, cujos títulos não se adaptem à nomenclatura específica desta ficha, tais como: a) contribuição sindical; b) prêmios de seguro; c) fretes e carretos que não componham os custos; d) despesas com viagens, diárias e ajudas de custo; e) transporte de empregados.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (354, '3.01.01.07.01.33.01', 'Despesas com viagens, diárias e ajusta de custo',
        'Contas que registram as despesas operacionais com viagens, diárias e ajuda de custo', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (355, '3.01.01.07.01.33.90', 'Outras Despesas Operacionais',
        'Contas que registram as demais despesas operacionais, cujos títulos não se adaptem à nomenclatura específica desta ficha, tais como: a) contribuição sindical; b) prêmios de seguro; c) fretes e carretos que não componham os custos; d) transporte de empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (356, '3.01.01.09', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (357, '3.01.01.09.01', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (358, '3.01.01.09.01.01.00', '(,) Variações Cambiais Passivas',
        'Contas que registram as perdas monetárias passivas resultantes da atualização dos direitos de créditos e das obrigações, calculadas com base nas variações nas taxas de câmbio (Lei no 9.069, de 1995, art.52, e Lei no 9.249, de 1995, art. 8o).Inclusive a variação cambial passiva correspondente: a) à atualização das obrigações e dos créditos em moeda estrangeira, registrada em qualquer data e apurada no encerramento do período de apuração em função da taxa de câmbio vigente; b) às operações com moeda estrangeira e conversão de obrigações para moeda nacional, ou novação dessas obrigações, ou sua extinção, total ou parcial, em virtude de capitalização, dação em pagamento, compensação, ou qualquer outro modo, desde que observadas as condições fixadas pelo Banco Central do Brasil. Atenção: a amortização dos ajustes de variação cambial contabilizada no ativo diferido deve ser informada na conta Encargos de Depreciação e Amortização (Lei no 9.816, de 1999, art. 2o, e Lei no 10.305, de 2001). ',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (359, '3.01.01.09.01.02.00', '(,) Perdas Incorridas no Mercado de Renda Variável, exceto Day,Trade',
        'Contas que registram: a) o somatório das perdas incorridas, em cada mês do período de apuração, em operações realizadas nas bolsas de valores, de mercadorias, de futuros e assemelhadas, existentes no País; b) as perdas incorridas nas alienações, fora de bolsa, de ouro, ativo financeiro, e de participações societárias, exceto as alienações de participações societárias permanentes em sociedades coligadas e controladas e de participações societárias que permanecerem no ativo da pessoa jurídica até o término do ano,calendário seguinte ao de suas aquisições; e c) as perdas em operações de swap e no resgate de quota de fundo de investimento que mantenha, no mínimo, 67% (sessenta e sete por cento) de ações negociadas no mercado à vista de bolsa de valores ou entidade assemelhada (Lei no 9.532, de 1997, art. 28, alterado pela MP no 1.636, de 1998, art. 2o, e reedições). São consideradas assemelhadas às bolsas de valores, de mercadorias e de futuros as entidades cujo objeto social seja análogo ao das referidas bolsas e que funcionem sob a supervisão e fiscalização da Comissão de Valores Mobiliários (CVM). Atenção: as perdas apuradas em operações day,trade devem ser informadas em conta própria. ',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (360, '3.01.01.09.01.03.00', '(,) Perdas em Operações Day,Trade',
        'Contas que registram o somatório das perdas diárias apuradas, em cada mês do período de apuração, em operações day,trade.Não se caracteriza como day,trade o exercício da opção e a venda ou compra do ativo no mercado à vista, no mesmo dia.Também não se caracterizam como day,trade as operações iniciadas por intermédio de uma instituição e encerradas em outra, quando houver a liquidação física mediante movimentação de títulos ou valores mobiliários em custódia.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (361, '3.01.01.09.01.04.00', '(,) Juros sobre o Capital Próprio',
        'Contas que registram as despesas com juros pagos ou creditados individualizadamente a titular, sócios ou acionistas, a título de remuneração do capital próprio, calculados sobre as contas do patrimônio liquido e limitados à variação, pro rata dia, da Taxa de Juros de Longo Prazo (TJLP) observando,se o regime de competência (Lei no 9.249, de 1995, art. 9o).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (362, '3.01.01.09.01.05.00', '(,) Outras Despesas Financeiras',
        'Contas que registram as despesas relativas a juros, não incluídas nas em outras contas, a descontos de títulos de crédito e ao deságio na colocação de debêntures ou outros títulos. Tais despesas serão obrigatoriamente rateadas, segundo o regime de competência. Atenção: 1) as variações monetárias passivas decorrentes da atualização das obrigações, em função de índices ou coeficientes aplicáveis por disposição legal ou contratual, devem ser informadas como despesa financeira; 2) as variações cambiais passivas não devem ser informadas nesta conta, e sim na conta Variações Cambiais Passivas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (363, '3.01.01.09.01.06.00', '(,) Prejuízos na Alienação de Participações Não Integrantes do Ativo Permanente',
        'Contas que registram os prejuízos havidos em virtude de alienação de ações, títulos ou quotas de capital não integrantes do ativo permanente, desde que não incluídos nas contas Perdas Incorridas no Mercado de Renda Variável, exceto Day,Trade ou Perdas em Operações Day,Trade.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (364, '3.01.01.09.01.07.00', '(,) Resultados Negativos em Participações Societárias',
        'Contas que registram as perdas por ajustes no valor de investimentos relevantes avaliados pelo método da equivalência patrimonial, decorrentes de prejuízos apurados nas controladas e coligadas. Atenção:considera,se controlada a filial, a agência, a sucursal, a dependência ou o escritório de representação no exterior, sempre que os respectivos ativos e passivos não estejam incluídos na contabilidade da investidora, por força de normatização específica. Devem, também, ser indicados nesta conta os resultados negativos derivados de participações societárias no exterior, avaliadas pelo patrimônio líquido. Incluem,se, nestas informações, as perdas apuradas em filiais, sucursais e agências da pessoa jurídica localizadas no exterior.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (365, '3.01.01.09.01.07.10',
        '(,) Amortização de Ágio nas Aquisições de Investimentos Avaliados pelo Patrimônio Líquido',
        'Contas que registram o valor da amortização registrada no período, referente ao ágio nas aquisições de investimentos avaliados pelo método da equivalência patrimonial. Atenção: O valor amortizado deve ser adicionado ao lucro líquido, para determinação do lucro real, e controlado na Parte B do Livro de Apuração do Lucro Real até a alienação ou baixa da participação societária, quando, então, pode ser excluído do lucro líquido, para determinação do lucro real.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (366, '3.01.01.09.01.08.00', '(,) Resultados Negativos em SCP',
        'Conta utilizada pelos sócios ostensivos, pessoas jurídicas, de sociedades em conta de participação, para indicar as perdas por ajustes no valor de participação em SCP, avaliada pelo método da equivalência patrimonial.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (367, '3.01.01.09.01.09.00', '(,) Perdas em Operações Realizadas no Exterior',
        'Contas que registram as perdas em operações realizadas no exterior diretamente pela pessoa jurídica domiciliada no Brasil, com exceção das perdas de capital decorrentes da alienação de bens e direitos do ativo permanente situados no exterior, que devem ser indicadas na conta Outras Despesas Não Operacionais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (368, '3.01.01.09.01.10.00', '(,) Contrapartida dos Ajustes ao Valor Presente',
        'Contrapartida do ajuste ao valor presente dos elementos do ativo e do passivo (art. 183, inciso VIII, e art. 184, inciso III da Lei 6.404/76)',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (369, '3.01.01.09.01.11.00', '(,) Contrapartida de outros Ajustes às Normas Internacionais de Contabilidade',
        'Contrapartida de outros ajustes decorrentes da adequação às Normas Internacionais de Contabilidade', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (370, '3.01.01.09.01.12.00', '(,) Contrapartida dos Ajustes de Valor do Imobilizado e Intangível',
        'Contrapartida dos ajustes decorrentes da análise de recuperação dos valores registrados no imobilizado e no  intangível (art. 183, § 3º, da Lei 6.404/76)',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (371, '3.01.03', 'OUTRAS RECEITAS E OUTRAS DESPESAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (372, '3.01.03.01', 'RECEITAS E DESPESAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (373, '3.01.03.01.01', 'RECEITAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (374, '3.01.03.01.01.01.00', 'Receitas de Alienações de Bens e Direitos do Ativo Permanente',
        'Contas que registram as receitas auferidas por meio de alienações, inclusive por desapropriação, de bens e direitos do ativo permanente. O valor relativo às receitas obtidas pela venda de sucata e de bens ou direitos do ativo permanente baixados em virtude de terem se tornado imprestáveis, obsoletos ou caído em desuso deve ser informado na conta Outras Receitas Não Operacionais Os valores correspondentes ao ganho ou perda de capital decorrente da alienação de bens e direitos do ativo permanente situados no exterior devem ser indicados, pelo seu resultado, nas contas Outras Receitas Não Operacionais ou Outras Despesas Não Operacionais, conforme o caso.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (375, '3.01.03.01.01.01.10',
        'Ganhos de Capital por Variação Percentual em Participação Societária Avaliada pelo Patrimônio Líquido',
        'Contas que registram o ganho de capital resultante de acréscimo, por variação percentual, do valor do patrimônio líquido de investimento avaliado pelo método da equivalência patrimonial. Atenção: Esse valor deve ser excluído do lucro líquido para determinação do lucro real no período de apuração.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (376, '3.01.03.01.01.02.00', 'Outras Receitas Não Operacionais',
        'Contas que registram: a) todas as demais receitas decorrentes de operações não incluídas nas atividades principais e acessórias da empresa, tais como: a reversão do saldo da provisão para perdas prováveis na realização de investimentos e a reserva de reavaliação realizada no período de apuração, quando computada em conta de resultado; b) os ganhos de capital por variação na percentagem de participação no capital social de coligada ou controlada, quando o investimento for avaliado pela equivalência patrimonial (Decreto no 3.000, de 1999, art. 428); c) os ganhos de capital decorrentes da alienação de bens e direitos do ativo permanente situados no exterior. Devem ser indicadas tanto as contas que registram as receitas quanto as que registram os custos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (377, '3.01.03.01.03', 'DESPESAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (378, '3.01.03.01.03.01.00', '(,) Valor Contábil dos Bens e Direitos Alienados',
        'Contas que registram o contábil dos bens do ativo permanente baixados no curso do período de apuração cuja receita da venda tenha sido indicada na conta Receitas de Alienações de Bens e Direitos do Ativo Permanente.  O valor contábil de bens ou direitos baixados em virtude de terem se tornado imprestáveis, obsoletos ou caído em desuso e o valor contábil de bens ou direitos situados no exterior devem ser informados na conta Outras Despesas Não Operacionais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (379, '3.01.03.01.03.01.10',
        '(,) Perdas de Capital por Variação Percentual em Participação Societária Avaliada pelo Patrimônio Líquido',
        'Contas que registram a perda de capital resultante de redução, por variação percentual, do valor do patrimônio líquido de investimento avaliado pelo método da equivalência patrimonial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (380, '3.01.03.01.03.02.00', '(,) Outras Despesas Não Operacionais',
        'Contas que registram: a) o valor contábil dos bens do ativo permanente baixados no curso do período de apuração não incluídos na conta precedente e a despesa com a constituição da provisão para perdas prováveis na realização de investimentos. Atenção: Sobre a definição de valor contábil, consultar o § 1o do art. 418 e o art. 426 do Decreto no 3.000, de 1999. b) as perdas de capital por variação na percentagem de participação no capital social de coligada ou controlada no Brasil, quando o investimento for avaliado pela equivalência patrimonial (Decreto no 3.000, de 1999, art. 428).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (381, '3.01.05', 'PARTICIPAÇÕES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (382, '3.01.05.01', 'PARTICIPAÇÕES NOS LUCROS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (383, '3.01.05.01.01', 'PARTICIPAÇÕES DE EMPREGADOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (384, '3.01.05.01.01.01.00', '(,) Participações de Empregados',
        'Contas que registram as participações atribuídas a empregados segundo disposição legal, estatutária, contratual ou por deliberação da assembléia de acionistas ou sócios.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (385, '3.01.05.01.01.02.00', '(,) Contribuições para Assistência ou Previdência de Empregados',
        'Contas que registram as contribuições para instituições ou fundos de assistência ou previdência de empregados, baseadas nos lucros. Não indicar, nesta conta, aquelas contribuições já deduzidas como custo ou despesa operacional.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (386, '3.01.05.01.01.03.00', '(,) Outras Participações de Empregados',
        'Contas que registram outras participações de empregados', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (387, '3.01.05.01.03', 'OUTRAS PARTICIPAÇÕES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (388, '3.01.05.01.03.01.00', '(,) Participações de Administradores e Partes Beneficiárias',
        'Contas que registram quaisquer participações nos lucros atribuídas a administradores, sócio, titular de empresa individual e a portadores de partes beneficiárias, durante o período de apuração.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (389, '3.01.05.01.03.02.00', '(,) Participações de Debêntures',
        'Contas que representam as participações nos lucros da companhia atribuídas a debêntures de sua emissão', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (390, '3.01.05.01.03.03.00', '(,) Outras', 'Contas que registram outras participações', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (391, '3.02', 'PROVISÃO PARA CSLL E IRPJ (ATIVIDADES EM GERAL)', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (392, '3.02.01', 'PROVISÃO PARA CSLL E IRPJ', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (393, '3.02.01.01', 'PROVISÃO PARA CSLL E IRPJ', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (394, '3.02.01.01.01', 'PROVISÃO PARA CSLL E IRPJ', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (395, '3.02.01.01.01.01.00', '(,) Contribuição Social sobre o Lucro Líquido',
        'Contas que registram as provisões para a CSLL calculadas sobre a base de cálculo correspondente ao período de apuração e sobre os lucros diferidos da atividade geral, se for o caso. A sua constituição é obrigatória para todas as pessoas jurídicas tributadas com base no lucro real. As cooperativas devem informar, nesta conta, a provisão da CSLL sobre os resultados das operações realizadas com os não,associados. Atenção: para as empresas com atividades mistas, os valores da CSLL relativos às atividades em geral e atividade rural devem ser informados nas contas específicas de cada atividade (Atividades em Geral e Atividade Rural, respectivamente).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (396, '3.02.01.01.01.02.00', '(,) Provisão para Imposto de Renda , Pessoa Jurídica',
        'Contas que registram as provisões para o IRPJ calculadas sobre a base de cálculo correspondente ao período de apuração e sobre os lucros diferidos da atividade geral, se for o caso. A sua constituição é obrigatória para todas as pessoas jurídicas tributadas com base no lucro real. As cooperativas devem informar, nesta conta, a provisão para o IRPJ sobre os resultados das operações realizadas com os não,associados. Atenção: para as empresas com atividades mistas, os valores do IRPJ relativos às atividades em geral e atividade rural devem ser informados nas contas específicas de cada atividade (Atividades em Geral e Atividade Rural, respectivamente).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (397, '3.05', 'RESULTADO ANTES DO IRPJ E DA CSLL , ATIVIDADE RURAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (398, '3.05.01', 'RESULTADO OPERACIONAL DA ATIVIDADE RURAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (399, '3.05.01.01', 'RECEITA OPERACIONAL LÍQUIDA DA ATIVIDADE RURAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (400, '3.05.01.01.01', 'RECEITA BRUTA DA ATIVIDADE RURAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (401, '3.05.01.01.01.01.00', 'Receita da Atividade Rural', 'Contas que registram a receita da atividade rural.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (402, '3.05.01.01.03', 'DEDUÇÕES DA RECEITA BRUTA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (403, '3.05.01.01.03.01.00', '(,) Vendas Canceladas, Devoluções e Descontos Incondicionais',
        'Contas representativas das vendas canceladas, a devoluções de vendas e a descontos incondicionais concedidos sobre  receitas constantes da conta Receita da Atividade Rural.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (404, '3.05.01.01.03.02.00', '(,) ICMS',
        'Contas que registram o total do Imposto Sobre Operações Relativas à Circulação de Mercadorias e Sobre Prestação de Serviços de Transporte Interestadual e Intermunicipal e de Comunicação (ICMS) calculado sobre as receitas das vendas e de serviços constantes da conta Receita da Atividade Rural. Informar o resultado da aplicação das alíquotas sobre as respectivas receitas, e não o montante recolhido, durante o período de apuração, pela pessoa jurídica.O valor referente ao ICMS pago como substituto não deve ser incluído nesta conta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (405, '3.05.01.01.03.03.00', '(,) Cofins',
        'Contas que registram a Cofins apurada sobre a receita de vendas em consonância com a legislação vigente à época da ocorrência dos fatos geradores, incidente sobre as receitas da conta Receita da Atividade Rural. O valor informado deve ser apurado de forma centralizada pelo estabelecimento matriz, quando a pessoa jurídica possuir mais de um estabelecimento (Lei no 9.779, de 1999, art. 15, III).  Não incluir a Cofins incidente sobre as demais receitas operacionais, que deverá ser informada em conta distinta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (406, '3.05.01.01.03.04.00', '(,) PIS/Pasep',
        'Contas que registram as contribuições para o PIS/Pasep apurado sobre a receita de vendas em consonância com a legislação vigente à época da ocorrência dos fatos geradores, incidente sobre as receitas da conta Receita da Atividade Rural. O valor informado deve ser apurado de forma centralizada pelo estabelecimento matriz, quando a pessoa jurídica possuir mais de um estabelecimento (Lei no 9.779, de 1999, art. 15, III). Não incluir o PIS/Pasep incidente sobre as demais receitas operacionais, que deverá ser informada em conta distinta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (407, '3.05.01.01.03.05.00', '(,) ISS',
        'Contas que registram o Imposto sobre Serviço de qualquer Natureza (ISS) relativo às receitas de serviços, conforme legislação específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (408, '3.05.01.01.03.06.00', '(,) Demais Impostos e Contribuições Incidentes sobre Vendas e Serviços',
        'Contas que registrem os demais impostos e contribuições incidentes sobre as receitas das vendas de que trata a conta Receita da Atividade Rural, que guardem proporcionalidade com o preço e sejam considerados redutores das receitas de vendas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (409, '3.05.01.03', 'CUSTO DOS BENS E SERVIÇOS VENDIDOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (410, '3.05.01.03.01', 'CUSTO DOS PRODUTOS DA ATIVIDADE RURAL VENDIDOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (411, '3.05.01.03.01.00.00', 'Custo dos Produtos Vendidos da Atividade Rural', '', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (412, '3.05.01.03.01.01.01', 'Estoques Iniciais de Insumos Agropecuários',
        'Contas que registram os estoques de insumos agropecuários existentes no início do período de apuração.', null,
        '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (413, '3.05.01.03.01.01.02', 'Estoques Iniciais de Produtos Agropecuários Acabados',
        'Contas que registram os estoques de produtos agropecuários acabados existentes no início do período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (414, '3.05.01.03.01.01.03', 'Estoques Iniciais de Produtos Agropecuários em Formação',
        'Contas que registram os estoques de produtos agropecuários em formação existentes no início do período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (415, '3.05.01.03.01.02.00', 'Compras de Insumos Agropecuários à Vista',
        'Contas que registram as aquisições à vista, durante o período de apuração, de insumos agropecuários, no mercado interno e externo, para utilização na formação de produtos agropecuários. Também compõem os valores de compras desses insumos os valores referentes aos custos com transporte e seguro até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (416, '3.05.01.03.01.03.00', 'Compras de Insumos Agropecuários a Prazo',
        'Contas que registram as aquisições a prazo, durante o período de apuração, de insumos agropecuários, no mercado interno e externo, para utilização na formação de produtos agropecuários. Também compõem os valores de compras desses insumos, os valores referentes aos custos com transporte e seguro até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (417, '3.05.01.03.01.04.00', 'Remuneração a Dirigentes da Produção',
        'Contas que registram: a) a remuneração mensal e fixa dos dirigentes diretamente ligados à produção, pelo valor total do custo incorrido no período de apuração, exceto os encargos sociais (Previdência Social e FGTS) que são informados em conta distinta;b) o valor relativo aos custos incorridos com salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores, se ligados diretamente à produção (PN Cosit no 11, de 30 de setembro de 1992). Atenção: deve ser incluído nesta conta o valor das gratificações dos dirigentes ligados à produção, inclusive o 13o salário.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (418, '3.05.01.03.01.05.00', 'Custo do Pessoal Aplicado na Produção',
        'Contas que representem do custo com ordenados, salários e outros custos com empregados ligados à produção da empresa, tais como: seguro de vida, contribuições ao plano PAIT, custos com programa de previdência privada, contribuições para os Fundos de Aposentadoria Programada Individual (Fapi), e outras de caráter remuneratório. Inclusive os custos com supervisão direta, manutenção e guarda das instalações, decorrentes de vínculo empregatício com a pessoa jurídica.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (419, '3.05.01.03.01.06.00', 'Encargos Sociais , Previdência Social',
        'Contas que registram as contribuições para a Previdência Social, relativas ao pessoal ligado diretamente à produção, inclusive dirigentes.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (420, '3.05.01.03.01.07.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para o FGTS, relativas ao pessoal ligado diretamente à produção, inclusive dirigentes.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (421, '3.05.01.03.01.08.00', 'Encargos Sociais  Outros',
        'Contas que registram encargos sociais, relativos ao pessoal ligado diretamente à produção, não classificados nas contas Encargos Sociais , Previdência Social  e Encargos Sociais , FGTS.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (422, '3.05.01.03.01.09.00', 'Alimentação do Trabalhador',
        'Contas que registram os custos realizados com alimentação do pessoal ligado diretamente à produção.', null,
        '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (423, '3.05.01.03.01.10.00', 'Manutenção e Reparo de Bens Aplicados na Produção',
        'Contas que representam somente os custos realizados com reparos que não implicaram aumento superior a um ano da vida útil prevista no ato da aquisição do bem.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (424, '3.05.01.03.01.11.00', 'Arrendamento Mercantil',
        'Contas que representam o valor do custo incorrido a título de contraprestação de arrendamento mercantil de bens alocados na produção, segundo contratos celebrados com observância da Lei no 6.099, de 12 de setembro de 1974, com as alterações da Lei no 7.132, de 26 de outubro de 1983. Os custos com aluguel de outros bens alocados à produção, mediante contrato diferente do de arrendamento mercantil, devem ser indicados em Outros Custos. Os valores referentes a bens que não sejam intrinsecamente relacionados com a produção devem ser informados na conta Arrendamento Mercantil do grupo DESPESAS OPERACIONAIS DA ATIVIDADE RURAL.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (425, '3.05.01.03.01.12.00', 'Encargos de Depreciação, Amortização e Exaustão',
        'Contas que registram os encargos a esses títulos com bens aplicados diretamente na produção. Os encargos que não forem decorrentes de bens intrinsecamente relacionados com a produção devem ser informados na conta Encargos de Depreciação, Amortização e Exaustão do grupo DESPESAS OPERACIONAIS DA ATIVIDADE RURAL.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (426, '3.05.01.03.01.13.00', 'Constituição de Provisões',
        'Contas que registram os encargos com a constituição de provisões que devam ser imputados aos custos de produção da empresa no período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (427, '3.05.01.03.01.14.00', 'Serviços Prestados por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados à pessoa jurídica por pessoa física sem vínculo empregatício, relacionados com a atividade rural da pessoa jurídica.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (428, '3.05.01.03.01.15.00', 'Serviços Prestados por Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados por pessoa jurídica à pessoa jurídica declarante, relacionados com sua atividade rural.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (429, '3.05.01.03.01.16.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (430, '3.05.01.03.01.17.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (431, '3.05.01.03.01.18.00', 'Outros Custos',
        'Contas que representam os demais custos da empresa no processo de produção, para os quais não haja conta mais específica ou cujas classificações contábeis não se adaptem à nomenclatura específica desta ficha, tais como: custo referente ao valor de bens de consumo eventual, as quebras ou perdas de estoque, e as ocorridas na fabricação, no transporte e manuseio.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (432, '3.05.01.03.01.19.01', '(,) Estoques Finais de Insumos Agropecuários',
        'Contas que registram os estoques de insumos agropecuários existentes no final do período de apuração.', null,
        '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (433, '3.05.01.03.01.19.02', '(,) Estoques Finais de Produtos Agropecuários em Formação',
        'Contas que registram os estoques de produtos agropecuários em formação existentes no final do período de apuração.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (434, '3.05.01.03.01.19.03', '(,) Estoques Finais de Produtos Agropecuários Acabados',
        'Contas que registram os estoques de produtos agropecuários acabados existentes no final do período de apuração',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (435, '3.05.01.03.09', 'AJUSTES DE ESTOQUES DECORRENTES DE ARBITRAMENTO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (436, '3.05.01.03.09.01.00', 'Ajustes de Estoques Decorrentes de Arbitramento',
        'Contas que, na pessoa jurídica submetida à apuração anual do imposto e que teve seu lucro arbitrado em um ou mais trimestres do ano,calendário, representam o valor, positivo ou negativo, correspondente à diferença entre os estoques iniciais do período imediatamente subseqüente ao arbitramento e os estoques finais do período imediatamente anterior ao arbitramento. Caso haja arbitramento em mais de um trimestre do ano,calendário, não consecutivos, as contas devem representar a soma algébrica das diferenças apuradas em relação a cada período arbitrado.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (437, '3.05.01.05', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (438, '3.05.01.05.01', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (439, '3.05.01.05.01.01.00', 'Variações Cambiais Ativas',
        'Contas que registram os ganhos apurados em razão de variações ativas decorrentes da atualização dos direitos de crédito e obrigações, calculados com base nas variações nas taxas de câmbio. Atenção: 1) as variações cambiais ativas decorrentes dos direitos de crédito e de obrigações, em função da taxa de câmbio, são consideradas como receita financeira, inclusive para fins de cálculo do lucro da exploração (Lei no 9.718, art. 9o c/c art. 17); 2) nas atividades de compra e venda, loteamento, incorporação e construção de imóveis, as variações cambiais ativas são reconhecidas como receita segundo as normas constantes da IN SRF no 84/79, de 20 de dezembro de 1979, da IN SRF no 23/83, de 25 de março de 1983, e da IN SRF no 67/88, de 21 de abril de 1988 (IN SRF no 25/99, de 25 de fevereiro de 1999).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (440, '3.05.01.05.01.02.00', 'Ganhos Auferidos no Mercado de Renda Variável, exceto Day,Trade',
        'Contas que registram: a) o somatório dos ganhos auferidos, em cada mês do período de apuração, em operações realizadas nas bolsas de valores, de mercadorias, de futuros e assemelhadas, existentes no País; b) os ganhos auferidos nas alienações, fora de bolsa, de ouro, ativo financeiro, e de participações societárias, exceto as alienações de participações societárias permanentes em sociedades coligadas e controladas e de participações societárias que permanecerem no ativo da pessoa jurídica até o término do ano,calendário seguinte ao de suas aquisições; e c) os rendimentos auferidos em operações de swap e no resgate de quota de fundo de investimento cujas carteiras sejam constituídas, no mínimo, por 67% (sessenta e sete por cento) de ações no mercado à vista de bolsa de valores ou entidade assemelhada (Lei no 9.532, de 1997, art. 28, alterado pela MP no 1.636, de 1998, art. 2o, e reedições). Considera,se ganho o resultado positivo auferido nas operações citadas acima, realizadas em cada mês, admitida a dedução dos custos e despesas incorridos, necessários à realização das operações. Atenção: 1) os ganhos auferidos em operações day,trade devem ser informados em conta específica; 2) o valor correspondente às perdas incorridas no mercado de renda variável, exceto day,trade, deve ser informado em conta específica. 3) são consideradas assemelhadas às bolsas de valores, de mercadorias e de futuros as entidades cujo objeto social seja análogo ao das referidas bolsas e que funcionem sob a supervisão e fiscalização da Comissão de Valores Mobiliários (CVM).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (441, '3.05.01.05.01.03.00', 'Ganhos em Operações Day,Trade',
        'Contas que registram os ganhos diários auferidos, em cada mês do período de apuração, em operações day,trade. Considera,se ganho o resultado positivo auferido nas operações citadas acima, realizadas em cada mês, admitida a dedução dos custos e despesas incorridos, necessários à realização das operações. Não se caracteriza como day,trade o exercício da opção e a venda ou compra do ativo no mercado à vista, no mesmo dia. Também não se caracterizam como day,trade as operações iniciadas por intermédio de uma instituição e encerradas em outra, quando houver a liquidação física mediante movimentação de títulos ou valores mobiliários em custódia. Atenção: o valor correspondente às perdas incorridas nas operações day,trade deve ser informado em conta específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (442, '3.05.01.05.01.04.00', 'Receitas de Juros sobre o Capital Próprio',
        'Contas que registram os juros recebidos, a título de remuneração do capital próprio, em conformidade com o art. 9o da Lei no 9.249, de 1995. O valor  informado deve corresponder ao total dos juros recebidos antes do desconto do imposto de renda na fonte. O valor do imposto de renda retido na fonte, para as pessoas jurídicas tributadas pelo lucro real, é considerado antecipação do imposto devido no encerramento do período de apuração ou, ainda, pode ser compensado com aquele que for retido, pela beneficiária, por ocasião do pagamento ou crédito de juros a título de remuneração do capital próprio, ao seu titular ou aos seus sócios.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (443, '', '', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (444, '3.05.01.05.01.05.00', 'Outras Receitas Financeiras',
        'Contas que registram receitas auferidas no período de apuração relativas a juros, descontos, lucro na operação de reporte, prêmio de resgate de títulos ou debêntures e rendimento nominal auferido em aplicações financeiras de renda fixa, não incluídas em contas precedentes deste grupo. As receitas dessa natureza, derivadas de operações com títulos vencíveis após o encerramento do período de apuração, serão rateadas segundo o regime de competência. Atenção: 1) as variações monetárias ativas decorrentes da atualização dos direitos de crédito e das obrigações, em função de índices ou coeficientes aplicáveis por disposição legal ou contratual, devem ser informadas como receita financeira; 2) As variações cambiais ativas devem ser informadas na conta Variações Cambiais Ativas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (445, '3.05.01.05.01.06.00', 'Ganhos na Alienação de Participações Não Integrantes do Ativo Permanente',
        'Contas que registram os ganhos auferidos na alienação de ações, títulos ou quotas de capital não integrantes do ativo permanente, desde que não incluídos na conta Ganhos Auferidos no Mercado de Renda Variável, exceto Day,Trade.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (446, '3.05.01.05.01.07.00', 'Resultados Positivos em Participações Societárias',
        'Contas que registram: a) os lucros e dividendos derivados de investimentos avaliados pelo custo de aquisição; b) os ganhos por ajustes no valor de investimentos relevantes avaliados pelo método da equivalência patrimonial, decorrentes de lucros apurados nas controladas e coligadas; Atenção: considera,se controlada a filial, a agência, a sucursal, a dependência ou o escritório de representação no exterior, sempre que os respectivos ativos e passivos não estejam incluídos na contabilidade da investidora, por força de normatização específica. c) as bonificações recebidas; Atenção: 1) as bonificações recebidas, decorrentes da incorporação de lucros ou reservas não tributados na forma do art. 35 da Lei no 7.713, de 1988, ou apurados nos anos,calendário de 1994 ou 1995, são consideradas a custo zero, não afetando o valor do investimento nem o resultado do período de apuração (art. 3o da Lei no 8.849, de 1994, e art. 3o da Lei no 9.064, de 1995).; 2) no caso de investimento avaliado pelo custo de aquisição, as bonificações recebidas, decorrentes da incorporação de lucros ou reservas tributados na forma do art. 35 da Lei no 7.713, de 1988, e de lucros ou reservas apurados no ano,calendário de 1993 ou a partir do ano,calendário de 1996, são registradas tomando,se como custo o valor da parcela dos lucros ou reservas capitalizados. e) os lucros e dividendos de participações societárias avaliadas pelo custo de aquisição; Atenção:os lucros ou dividendos recebidos em decorrência de participações societárias avaliadas pelo custo de aquisição adquiridas até 6 (seis) meses antes da data do recebimento devem ser registrados como diminuição do valor do custo, não sendo incluídos nesta conta. f) os resultados positivos decorrentes de participações societárias no exterior avaliadas pelo patrimônio líquido, os dividendos de participações avaliadas pelo custo de aquisição e os resultados de equivalência patrimonial relativos a filiais, sucursais ou agências da pessoa jurídica localizadas no exterior, em decorrência de operações realizadas naquelas filiais, sucursais ou agências.Os lucros auferidos no exterior serão adicionados ao lucro líquido, para efeito de determinação do lucro real, no período de apuração correspondente ao balanço levantado em 31 de dezembro do ano,calendário em que tiverem sido disponibilizados, observando,se o disposto nos arts. 394 e 395 do Decreto no 3.000, de 1999, e no art. 74 da Medida Provisória no 2.158,35, de 24 de agosto de 2001.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (447, '3.05.01.05.01.07.10',
        'Amortização de Deságio nas Aquisições de Investimentos Avaliados pelo Patrimônio Líquido',
        'Contas que registram as amortizações de deságios nas aquisições de investimentos avaliados pelo patrimônio líquido. O valor amortizado que for excluído do lucro líquido para determinação do lucro real deve ser controlado na Parte B do Livro de Apuração do Lucro Real até a alienação ou baixa da participação societária, quando, então, deve ser adicionado ao lucro líquido para determinação do lucro real no período de apuração em que for computado o ganho ou perda de capital havido.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (448, '3.05.01.05.01.08.00', 'Resultados Positivos em SCP',
        'Conta utilizada pelas pessoas jurídicas que forem sócias ostensivas de sociedades em conta de participação, para a registro: a) de lucros derivados de participação em SCP, avaliadas pelo custo de aquisição; b) dos ganhos por ajustes no valor de participação em SCP, avaliadas pelo método da equivalência patrimonial. Atenção:os lucros recebidos de investimento em SCP, avaliado pelo custo de aquisição, ou a contrapartida do ajuste do investimento ao valor do patrimônio líquido da SCP, no caso de investimento avaliado por esse método, podem ser excluídos na determinação do lucro real dos sócios, pessoas jurídicas, das referidas sociedades (Decreto no 3.000, de 1999, art. 149).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (449, '3.05.01.05.01.09.00', 'Rendimentos e Ganhos de Capital Auferidos no Exterior',
        'Contas que registram os rendimentos e ganhos de capital auferidos no exterior diretamente pela pessoa jurídica domiciliada no Brasil, pelos seus valores antes de descontado o tributo pago no país de origem. Esses valores podem, no caso de apuração trimestral do imposto, ser excluídos na apuração do lucro real do 1o ao 3o trimestres, devendo ser adicionados ao lucro líquido na apuração do lucro real referente ao 4o trimestre. Atenção:Os ganhos de capital referentes a alienações de bens e direitos do ativo permanente situados no exterior devem ser informados na conta Outras Receitas Não Operacionais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (450, '3.05.01.05.01.10.00', 'Reversão dos Saldos das Provisões Operacionais',
        'Contas que registram a reversão de  saldos não utilizados das provisões constituídas no balanço do período de apuração imediatamente anterior para fins de apuração do lucro real (Lei no 9.430, de 1996, art. 14).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (451, '3.05.01.05.01.11.00', 'Outras Receitas Operacionais',
        'Contas que registram todas as demais receitas que, por definição legal, sejam consideradas operacionais, tais como: a) aluguéis de bens por empresa que não tenha por objeto a locação de móveis e imóveis; b) recuperações de despesas operacionais de períodos de apuração anteriores, tais como: prêmios de seguros, importâncias levantadas das contas vinculadas do FGTS, ressarcimento de desfalques, roubos e furtos, etc. As recuperações de custos e despesas no decurso do próprio período de apuração devem ser creditadas diretamente às contas de resultado em que foram debitadas; c) os créditos presumidos do IPI para ressarcimento do valor da Contribuição ao PIS/Pasep e Cofins; d) multas ou vantagens a título de indenização em virtude de rescisão contratual (Lei no 9.430, de 1996, art. 70, § 3o, II);e) o crédito presumido da contribuição para o PIS/Pasep e da Cofins concedido na forma do art. 3o da Lei no 10.147, de 2000.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (452, '3.05.01.05.01.12.00', 'Prêmios Recebidos na Emissão de Debêntures',
        'Contas que registram, a partir de 01.01.2008, os prêmios recebidos na emissão de debêntures.', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (453, '3.05.01.05.01.13.00', 'Doações e Subvenções para Investimentos',
        'Contas que registram, a partir de 01.01.2008, as doações e subvenções para investimento.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (454, '3.05.01.05.01.14.00', 'Contrapartida dos Ajustes ao Valor Presente',
        'Contrapartida do ajuste ao valor presente dos elementos do ativo e do passivo (art. 183, inciso VIII, e art. 184, inciso III da Lei 6.404/76)',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (455, '3.05.01.05.01.15.00', 'Contrapartida de outros Ajustes às Normas Internacionais de Contabilidade',
        'Contrapartida de outros ajustes decorrentes da adequação às Normas Internacionais de Contabilidade',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (456, '3.05.01.07', 'DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (457, '3.05.01.07.01', 'DESPESAS OPERACIONAIS DA ATIVIDADE RURAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (458, '3.05.01.07.01.01.00', 'Remuneração a Dirigentes e a Conselho de Administração',
        'Contas que registram a despesa incorrida relativa à remuneração mensal e fixa atribuída ao titular de firma individual, aos sócios, diretores e administradores de sociedades, ou aos representantes legais de sociedades estrangeiras, as despesas incorridas com os salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores (PN Cosit no 11, de 1992), e o valor referente às remunerações atribuídas aos membros do conselho fiscal ou consultivo. Atenção: os valores das gratificações aos dirigentes que estejam ligados à área de produção rural devem ser informados na conta Remuneração a Dirigentes da Produção.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (459, '3.05.01.07.01.02.00', 'Ordenados, Salários, Gratificações e Outras Remunerações a Empregados',
        'Contas que registram a remuneração de empregados, tais como: comissões, moradia, seguro de vida, contribuições pagas ao plano PAIT, despesas com programa de previdência privada, contribuições para os Fundos de Aposentadoria Programada Individual (Fapi), e outras de caráter remuneratório. Atenção: 1) as despesas correspondentes a salários, ordenados, gratificações e outras remunerações referentes à área de saúde, tais como assistência médica, odontológica e farmacêutica, devem ser indicadas na conta Assistência Médica, Odontológica e Farmacêutica a Empregados; 2) não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (460, '3.05.01.07.01.02.01', 'Ordenados, Salários Gratificações e Outras Remunerações a Empregados',
        'Contas que registram as despesas com ordenados, salários, gratificações e outras despesas com empregados, tais como: comissões, moradia, seguro de vida e outras de caráter remuneratório. Atenção: 1) As despesas correspondentes a salários, ordenados, gratificações e outras remunerações referentes à área de saúde, tais como assistência médica, odontológica e farmacêutica, devem ser indicadas na conta específica. 2) Não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (461, '3.05.01.07.01.02.03', 'Planos de Poupança e Investimentos de Empregados',
        'Contas que registram o valor total dos gastos efetuados com Planos de Poupança e Investimentos (PAIT).',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (462, '3.05.01.07.01.02.05', 'Fundo de Aposentadoria Programada Individual de Empregados',
        'Contas que registram o valor total dos gastos efetuados com Fundos de Aposentadoria Programada Individual (FAPI).',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (463, '3.05.01.07.01.02.07', 'Plano de Previdência Privada de Empregados',
        'Contas que registram o valor total dos gastos efetuados com Planos de Previdência Privada.', '2008,1,01', null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (464, '3.05.01.07.01.02.09', 'Outros Gastos com Pessoal',
        'Contas que registram os gastos com empregados não enquadrados nas contas precedentes Atenção: 1) As despesas correspondentes a salários, ordenados, gratificações e outras remunerações referentes à área de saúde, tais como assistência médica, odontológica e farmacêutica, devem ser indicadas na conta  Assistência Médica, Odontológica e Farmacêutica a Empregados; 2) não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (465, '3.05.01.07.01.03.00', 'Prestação de Serviços por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica, as despesas correspondentes aos serviços prestados por pessoa física que não tenha vínculo empregatício com a pessoa jurídica, tais como: comissões, corretagens, gratificações, honorários, direitos autorais e outras remunerações, inclusive as relativas a empreitadas de obras exclusivamente de trabalho e as decorrentes de fretes e carretos em geral.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (466, '3.05.01.07.01.04.00', 'Prestação de Serviço Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica, o valor das despesas correspondentes aos serviços prestados por outra pessoa jurídica à pessoa jurídica declarante.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (467, '3.05.01.07.01.04.01', 'Serviços Prestados por Cooperativa de Trabalho',
        'Contas que registram os serviços prestados por cooperativa de trabalho', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (468, '3.05.01.07.01.04.02', 'Locação de Mão,de,obra',
        'Contas que registram o valor total dos gastos efetuados no período com a contratação de serviços executados mediante cessão de mão,de,obra ou empreitada, inclusive em regime temporário, sujeitos à retenção de contribuição previdenciária, nos termos do art. 219 do Regulamento da Previdência Social , RPS, aprovado pelo Decreto nº 3.048, de 1999',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (469, '3.05.01.07.01.05.00', 'Encargos Sociais , Previdência Social',
        'Contas que registram as contribuições para a Previdência Social, não computadas nos custos (inclusive dos dirigentes , PN CST no 35, de 31 de agosto de 1981).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (470, '3.05.01.07.01.06.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para a o FGTS, não computadas nos custos (inclusive dos dirigentes , PN CST no 35, de 31 de agosto de 1981).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (471, '3.05.01.07.01.07.00', 'Encargos Sociais  Outros',
        'Contas que registram os demais encargos sociais, não computados nos custos ou nas contas Encargos Sociais , Previdência Social ou Encargos Sociais , FGTS',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (472, '3.05.01.07.01.08.00', 'Doações e Patrocínios de Caráter Cultural e Artístico (Lei no 8.313/1991)',
        'Contas que registram as doações e patrocínios efetuados no período de apuração em favor de projetos culturais previamente aprovados pelo Ministério da Cultura ou pela Agência Nacional do Cinema (Ancine), observada a legislação de concessão dos projetos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (473, '3.05.01.07.01.09.00', 'Doações a Instituições de Ensino e Pesquisa (Lei no 9.249/1995, art.13, § 2o)',
        'Contas que registram as doações a instituições de ensino e pesquisa cuja criação tenha sido autorizada por lei federal e que preencham os requisitos dos incisos I e II do art. 213 da Constituição Federal, de 1988, que são: a) comprovação de finalidade não,lucrativa e aplicação dos excedentes financeiros em educação; b) assegurar a destinação do seu patrimônio a outra escola comunitária, filantrópica ou confessional, ou ao Poder Público, no caso de encerramento de suas atividades.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (474, '3.05.01.07.01.10.00', 'Doações a Entidades Civis',
        'Contas que registram as doações efetuadas a: a) entidades civis, legalmente constituídas no Brasil, sem fins lucrativos, que prestem serviços gratuitos em benefício de empregados da pessoa jurídica doadora, e respectivos dependentes, ou em benefício da comunidade na qual atuem; e b) Organizações da Sociedade Civil de Interesse Público (OSCIP), qualificadas segundo as normas estabelecidas na Lei no 9.790, de 23 de março de 1999.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (475, '3.05.01.07.01.11.00', 'Outras Contribuições e Doações',
        'Contas que registram as doações feitas, entre outras, aos Fundos controlados pelos Conselhos Municipais, Estaduais e Nacional dos Direitos da Criança e do Adolescente.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (476, '3.05.01.07.01.12.00', 'Alimentação do Trabalhador',
        'Contas que registram as despesas com alimentação do pessoal não ligado à produção, realizadas durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (477, '3.05.01.07.01.13.00', 'PIS/Pasep',
        'Contas que registram as Contribuições para o PIS/Pasep incidente sobre as demais receitas operacionais.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (478, '3.05.01.07.01.14.00', 'Cofins',
        'Contas que registram a parcela da Cofins incidente sobre as demais receitas operacionais.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (479, '3.05.01.07.01.15.00', 'CPMF',
        'Contas que registram a Contribuição Provisória sobre Movimentação ou Transmissão de Valores e de Créditos de Natureza Financeira.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (480, '3.05.01.07.01.16.00', 'Demais Impostos, Taxas e Contribuições, exceto IR e CSLL',
        'Contas que registram os demais Impostos, Taxas e Contribuições, exceto: a) incorporadas ao custo de bens do ativo permanente; b) correspondentes aos impostos não recuperáveis, incorporados ao custo das matérias,primas, materiais secundários, materiais de embalagem e mercadorias destinadas à revenda; c) correspondentes aos impostos recuperáveis; d) correspondentes aos impostos e contribuições redutores da receita bruta ; e) correspondentes às Contribuições para o PIS/Pasep e à Cofins incidentes sobre as demais receitas operacionais, e à CPMF, indicados em contas específicas; f) correspondentes à contribuição social sobre o lucro líquido e ao imposto de renda devidos, que são informados em contas específicas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (481, '3.05.01.07.01.17.00', 'Arrendamento Mercantil',
        'Contas que registram as despesas, não computadas nos custos, pagas ou creditadas a título de contraprestação de arrendamento mercantil, decorrentes de contrato celebrado com observância da Lei no 6.099, de 12 de setembro de 1974, com as alterações da Lei no 7.132, de 26 de outubro de 1983, e da Portaria MF no 140, de 1984',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (482, '3.05.01.07.01.18.00', 'Aluguéis',
        'Contas que registram as despesas com aluguéis não decorrentes de arrendamento mercantil.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (483, '3.05.01.07.01.19.00', 'Despesas com Veículos e de Conservação de Bens e Instalações',
        'Contas que registram as despesas relativas aos bens que não estejam ligados diretamente à produção, as realizadas com reparos que não impliquem aumento superior a um ano da vida útil do bem, prevista no ato de sua aquisição, e as relativas a combustíveis e lubrificantes para veículos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (484, '3.05.01.07.01.20.00', 'Propaganda e Publicidade',
        'Contas que registram as despesas com propaganda e publicidade.', null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (485, '3.05.01.07.01.20.01',
        'Propaganda, Publicidade e Patrocínio (Associações Desportivas que Mantenham Equipe de Futebol Profissional)',
        'Contas que registram as despesas relativas a propaganda publicidade e patrocínio com associações desportivas que mantenham equipe de futebol profissional e possuam registro na Federação de Futebol do respectivo Estado, a título de propaganda, publicidade e patrocínio.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (486, '3.05.01.07.01.20.02', 'Propaganda, Publicidade e Patrocínio',
        'Contas que registram de propaganda, publicidade, exceto as classificadas na conta precedente', '2008,1,01',
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (487, '3.05.01.07.01.21.00', 'Multas', 'Contas que registram as despesas com multas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (488, '3.05.01.07.01.22.00', 'Encargos de Depreciação e Amortização',
        'Contas que registram apenas os encargos a esses títulos, com bens não aplicados diretamente na produção. Inclui a amortização dos ajustes de variação cambial contabilizada no ativo diferido, relativa à atividade geral da pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (489, '3.05.01.07.01.23.00', 'Perdas em Operações de Crédito',
        'Contas que registram as perdas no recebimento de créditos decorrentes das atividades da pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (490, '3.05.01.07.01.24.00', 'Provisões para Férias e 13o Salário de Empregados',
        'Contas que registram as despesas com a constituição de provisões para: a) pagamento de remuneração correspondente a férias e adicional de férias de empregados, inclusive encargos sociais (Decreto no 3.000, de 1999, art. 337, e PN CST no 7, de 1980); b) o 13o salário, no caso de apuração trimestral do imposto, inclusive encargos sociais (Decreto no 3.000, de 1999, art. 338).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (491, '3.05.01.07.01.25.00', 'Provisão para Perda de Estoque',
        'Contas que registram as despesas com a constituição de provisão para perda de estoque', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (492, '3.05.01.07.01.26.00', 'Demais Provisões',
        'Contas que registram as despesas com provisões não relacionadas em contas específicas', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (493, '3.05.01.07.01.27.00', 'Gratificações a Administradores',
        'Contas que registram as gratificações a administradores.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (494, '3.05.01.07.01.28.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram as despesas correspondentes às importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que não estejam relacionados com a produção.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (495, '3.05.01.07.01.29.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as despesas correspondentes às importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que não estejam relacionados com a produção.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (496, '3.05.01.07.01.30.00', 'Assistência Médica, Odontológica e Farmacêutica a Empregados',
        'Indicar o valor das despesas com assistência médica, odontológica e farmacêutica. Atenção: o valor referente à contratação de serviços de profissionais liberais sem vínculo empregatício ou de sociedades civis deve ser informado nas contas Prestação de Serviços por Pessoa Física sem Vínculo Empregatício ou Prestação de Serviço Pessoa Jurídica, conforme o caso.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (497, '3.05.01.07.01.31.00', 'Pesquisas Científicas e Tecnológicas',
        'Contas que registram as despesas efetuadas a esse título, inclusive a contrapartida das amortizações daquelas registradas no ativo diferido',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (498, '3.05.01.07.01.32.00', 'Bens de Natureza Permanente Deduzidos como Despesa',
        'Contas que registram as despesas com aquisição de bens do ativo imobilizado cujo prazo de vida útil não ultrapasse um ano, ou, caso exceda esse prazo, tenha valor unitário igual ou inferior ao fixado no art. 301 do Decreto no 3.000, de 1999.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (499, '3.05.01.07.01.33.00', 'Outras Despesas Operacionais',
        'Contas que registram as demais despesas operacionais, cujos títulos não se adaptem à nomenclatura específica desta ficha, tais como: a) contribuição sindical; b) prêmios de seguro; c) fretes e carretos que não componham os custos; d) despesas com viagens, diárias e ajudas de custo; e) transporte de empregados.',
        null, '2008,12,31', 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (500, '3.05.01.07.01.33.01', 'Despesas com viagens, diárias e ajusta de custo',
        'Contas que registram as despesas operacionais com viagens, diárias e ajuda de custo', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (501, '3.05.01.07.01.33.90', 'Outras Despesas Operacionais',
        'Contas que registram as demais despesas operacionais, cujos títulos não se adaptem à nomenclatura específica desta ficha, tais como: a) contribuição sindical; b) prêmios de seguro; c) fretes e carretos que não componham os custos; d) transporte de empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (502, '3.05.01.09', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (503, '3.05.01.09.01', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (504, '3.05.01.09.01.01.00', '(,) Variações Cambiais Passivas',
        'Contas que registram as perdas monetárias passivas resultantes da atualização dos direitos de créditos e das obrigações, calculadas com base nas variações nas taxas de câmbio (Lei no 9.069, de 1995, art. 52, e Lei no 9.249, de 1995, art. 8o).Inclusive a variação cambial passiva correspondente: a) à atualização das obrigações e dos créditos em moeda estrangeira, registrada em qualquer data e apurada no encerramento do período de apuração em função da taxa de câmbio vigente; b) às operações com moeda estrangeira e conversão de obrigações para moeda nacional, ou novação dessas obrigações, ou sua extinção, total ou parcial, em virtude de capitalização,dação em pagamento, compensação, ou qualquer outro modo, desde que observadas as condições fixadas pelo Banco Central do Brasil. Atenção: a amortização dos ajustes de variação cambial contabilizada no ativo diferido deve ser informada na conta Encargos de Depreciação e Amortização (Lei no 9.816, de 1999, art. 2o, e Lei no 10.305, de 2001).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (505, '3.05.01.09.01.02.00', '(,) Perdas Incorridas no Mercado de Renda Variável, exceto Day,Trade',
        'Contas que registram: a) o somatório das perdas incorridas, em cada mês do período de apuração, em operações realizadas nas bolsas de valores, de mercadorias, de futuros e assemelhadas, existentes no País; b) as perdas incorridas nas alienações, fora de bolsa, de ouro, ativo financeiro, e de participações societárias, exceto as alienações de participações societárias permanentes em sociedades coligadas e controladas e de participações societárias que permanecerem no ativo da pessoa jurídica até o término do ano,calendário seguinte ao de suas aquisições; e c) as perdas em operações de swap e no resgate de quota de fundo de investimento que mantenha, no mínimo, 67% (sessenta e sete por cento) de ações negociadas no mercado à vista de bolsa de valores ou entidade assemelhada (Lei no 9.532, de 1997, art. 28, alterado pela MP no 1.636, de 1998, art. 2o, e reedições). São consideradas assemelhadas às bolsas de valores, de mercadorias e de futuros as entidades cujo objeto social seja análogo ao das referidas bolsas e que funcionem sob a supervisão e fiscalização da Comissão de Valores Mobiliários (CVM). Devem, também, ser indicados nesta conta os resultados negativos derivados de participações societárias no exterior, avaliadas pelo patrimônio líquido. Incluem,se, nestas informações, as perdas apuradas em filiais, sucursais e agências da pessoa jurídica localizadas no exterior.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (506, '3.05.01.09.01.03.00', '(,) Perdas em Operações Day,Trade',
        'Contas que registram o somatório das perdas diárias apuradas, em cada mês do período de apuração, em operações day,trade.Não se caracteriza como day,trade o exercício da opção e a venda ou compra do ativo no mercado à vista, no mesmo dia.Também não se caracterizam como day,trade as operações iniciadas por intermédio de uma instituição e encerradas em outra, quando houver a liquidação física mediante movimentação de títulos ou valores mobiliários em custódia.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (507, '3.05.01.09.01.04.00', '(,) Juros sobre o Capital Próprio',
        'Contas que registram as despesas com juros pagos ou creditados individualizadamente a titular, sócios ou acionistas, a título de remuneração do capital próprio, calculados sobre as contas do patrimônio líquido e limitados à variação, pro rata dia, da Taxa de Juros de Longo Prazo (TJLP), observando,se o regime de competência (Lei no 9.249, de 1995, art. 9o).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (508, '3.05.01.09.01.05.00', '(,) Outras Despesas Financeiras',
        'Contas que registram as despesas relativas a juros, não incluídas nas em outras contas, a descontos de títulos de crédito e ao deságio na colocação de debêntures ou outros títulos. Tais despesas serão obrigatoriamente rateadas, segundo o regime de competência. Atenção: 1) as variações monetárias passivas decorrentes da atualização das obrigações, em função de índices ou coeficientes aplicáveis por disposição legal ou contratual, devem ser informadas como despesa financeira. 2) As variações cambiais passivas não devem ser informadas nesta conta, e sim na conta Variações Cambiais Passivas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (509, '3.05.01.09.01.06.00', '(,) Prejuízos na Alienação de Participações Não Integrantes do Ativo Permanente',
        'Contas que registram os prejuízos havidos em virtude de alienação de ações, títulos ou quotas de capital não integrantes do ativo permanente, desde que não incluídos nas contas Perdas Incorridas no Mercado de Renda Variável, exceto Day,Trade ou Perdas em Operações Day,Trade.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (510, '3.05.01.09.01.07.00', '(,) Resultados Negativos em Participações Societárias',
        'Contas que registram as perdas por ajustes no valor de investimentos relevantes avaliados pelo método da equivalência patrimonial, decorrentes de prejuízos apurados nas controladas e coligadas. Atenção: considera,se controlada a filial, a agência, a sucursal, a dependência ou o escritório de representação no exterior, sempre que os respectivos ativos e passivos não estejam incluídos na contabilidade da investidora, por força de normatização específica. Devem, também, ser indicados nesta conta os resultados negativos derivados de participações societárias no exterior, avaliadas pelo patrimônio líquido. Incluem,se, nestas informações, as perdas apuradas em filiais, sucursais e agências da pessoa jurídica localizadas no exterior.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (511, '3.05.01.09.01.07.10',
        '(,) Amortização de Ágio nas Aquisições de Investimentos Avaliados pelo Patrimônio Líquido',
        'Contas que registram o valor da amortização registrada no período, referente ao ágio nas aquisições de investimentos avaliados pelo método da equivalência patrimonial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (512, '', '',
        'Atenção: O valor amortizado deve ser adicionado ao lucro líquido, para determinação do lucro real, e controlado na Parte B do Livro de Apuração do Lucro Real até a alienação ou baixa da participação societária, quando, então, pode ser excluído do lucro líquido, para determinação do lucro real.',
        null, null, '');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (513, '3.05.01.09.01.08.00', '(,) Resultados Negativos em SCP',
        'Conta utilizada pelos sócios ostensivos, pessoas jurídicas, de sociedades em conta de participação, para indicar as perdas por ajustes no valor de participação em SCP, avaliada pelo método da equivalência patrimonial.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (514, '3.05.01.09.01.09.00', '(,) Perdas em Operações Realizadas no Exterior',
        'Contas que registram as perdas em operações realizadas no exterior diretamente pela pessoa jurídica domiciliada no Brasil, com exceção das perdas de capital decorrentes da alienação de bens e direitos do ativo permanente situados no exterior, que devem ser indicadas na conta Outras Despesas Não Operacionais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (515, '3.05.01.09.01.10.00', '(,) Contrapartida dos Ajustes ao Valor Presente',
        'Contrapartida do ajuste ao valor presente dos elementos do ativo e do passivo (art. 183, inciso VIII, e art. 184, inciso III da Lei 6.404/76)',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (516, '3.05.01.09.01.11.00', '(,) Contrapartida de outros Ajustes às Normas Internacionais de Contabilidade',
        'Contrapartida de outros ajustes decorrentes da adequação às Normas Internacionais de Contabilidade',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (517, '3.05.01.09.01.12.00', '(,) Contrapartida dos ajustes de valor do imobilizado e intangível',
        'Contrapartida dos ajustes decorrentes da análise de recuperação dos valores registrados no imobilizado e no  intangível (art. 183, § 3º, da Lei 6.404/76)',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (518, '3.05.03', 'PARTICIPAÇÕES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (519, '3.05.03.01', 'PARTICIPAÇÕES NOS LUCROS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (520, '3.05.03.01.01', 'PARTICIPAÇÕES DE EMPREGADOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (521, '3.05.03.01.01.01.00', '(,) Participações de Empregados',
        'Contas que registram as participações atribuídas a empregados segundo disposição legal, estatutária, contratual ou por deliberação da assembléia de acionistas ou sócios.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (522, '3.05.03.01.01.02.00', '(,) Contribuições para Assistência ou Previdência de Empregados',
        'Contas que registram as contribuições para instituições ou fundos de assistência ou previdência de empregados, baseadas nos lucros. Não indicar, nesta conta, aquelas contribuições já deduzidas como custo ou despesa operacional.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (523, '3.05.03.01.01.03.00', '(,) Outras Participações de Empregados',
        'Contas que registram outras participações de empregados', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (524, '3.05.03.01.03', 'OUTRAS PARTICIPAÇÕES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (525, '3.05.03.01.03.01.00', '(,) Participações de Administradores e Partes Beneficiárias',
        'Contas que registram quaisquer participações nos lucros atribuídas a administradores, sócio, titular de empresa individual e a portadores de partes beneficiárias, durante o período de apuração.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (526, '3.05.03.01.03.02.00', '(,) Participações de Debêntures',
        'Contas que representam as participações nos lucros da companhia atribuídas a debêntures de sua emissão', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (527, '3.05.03.01.03.05.00', '(,) Outras', 'Contas que registram outras participações', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (528, '3.06', 'PROVISÃO PARA CSLL E IRPJ (ATIVIDADE RURAL)', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (529, '3.06.01', 'PROVISÃO PARA CSLL E IRPJ', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (530, '3.06.01.01', 'PROVISÃO PARA CSLL E IRPJ  ', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (531, '3.06.01.01.01', 'PROVISÃO PARA CSLL E IRPJ  ', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (532, '3.06.01.01.01.01.00', '(,) Contribuição Social sobre o Lucro Líquido',
        'Contas que registram as provisões para a CSLL calculadas sobre a base de cálculo correspondente ao período de apuração e sobre os lucros diferidos da atividade rural.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (533, '3.06.01.01.01.02.00', '(,) Provisão para Imposto de Renda , Pessoa Jurídica',
        'Contas que registram as provisões para o IRPJ calculadas sobre a base de cálculo correspondente ao período de apuração e sobre os lucros diferidos da atividade rural.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (534, '4', 'SUPERÁVIT/DÉFICIT LÍQUIDO DO PERÍODO',
        'GRUPO DESTINADO EXCLUSIVAMENTE ÀS SOCIEDADES SIMPLES, SEM FINS LUCRATIVOS', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (535, '4.01', 'RESULTADO OPERACIONAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (536, '4.01.01', 'RECEITA OPERACIONAL LÍQUIDA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (537, '4.01.01.01', 'RECEITA BRUTA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (538, '4.01.01.01.01', 'RECEITA DE VENDA DE PRODUTOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (539, '4.01.01.01.01.01.00', 'Da atividade de Educação',
        'Contas que registram a receita de venda dos produtos da atividade de educação.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (540, '4.01.01.01.01.02.00', 'Da atividade de Saúde',
        'Contas que registram a receita de venda dos produtos da atividade de saúde.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (541, '4.01.01.01.01.03.00', 'Da atividade de Assistência Social',
        'Contas que registram a receita de venda dos produtos da atividade de assistência social.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (542, '4.01.01.01.01.04.00', 'Outras', 'Contas que registram as demais receitas de vendas de produtos.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (543, '4.01.01.01.02', 'RECEITA DE PRESTAÇÃO DOS SERVIÇOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (544, '4.01.01.01.02.01.00', 'Serviços Educacionais',
        'Contas que registram as receitas de prestação de serviços na atividade educacional.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (545, '4.01.01.01.02.02.00', 'Doações/Subvenções Vinculadas',
        'Contas que registram as receitas recebidas como doações/subvenções vinculadas (Dec. 2.536/1998, art. 3, inciso V),  com destinação à prestação de serviços, preferencialmente segregadas por níveis federal, estadual e municipal.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (546, '4.01.01.01.02.03.00', 'Doações',
        'Contas que registram as receitas recebidas como doações particulares não vinculadas,  com destinação à prestação de serviços.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (547, '4.01.01.01.02.04.00', 'Contribuições',
        'Contas que registram as receitas recebidas como contribuições com destinação à prestação de serviços.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (548, '4.01.01.01.02.05.00', 'Outras', 'Contas que registram as demais receitas de prestação de serviços.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (549, '4.01.01.01.03', 'RECEITA DE SERVIÇOS DE SAÚDE', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (550, '4.01.01.01.03.01.00', 'Pacientes Particulares',
        'Contas que registram as receitas de serviços de saúde prestados a pacientes particulares.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (551, '4.01.01.01.03.02.00', 'Convênios  SUS',
        'Contas que registram as receitas de serviços de saúde prestados a pacientes conveniados do SUS.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (552, '4.01.01.01.03.03.00', 'Convênios  Outros',
        'Contas que registram as receitas de serviços de saúde prestados a outros pacientes conveniados.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (553, '4.01.01.01.03.04.00', 'Doações/Subvenções Vinculadas',
        'Contas que registram as receitas recebidas como doações/subvenções vinculadas (Dec. 2.536/1998, art. 3, inciso V),  com destinação à área de saúde, preferencialmente segregadas por níveis federal, estadual e municipal.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (554, '4.01.01.01.03.05.00', 'Doações',
        'Contas que registram as receitas recebidas como doações particulares não vinculadas,  com destinação à área da saúde.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (555, '4.01.01.01.03.06.00', 'Contribuições',
        'Contas que registram as receitas recebidas como contribuições com destinação à área de saúde.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (556, '4.01.01.01.03.07.00', 'Outras', 'Contas que registram as demais receitas de serviços de saúde.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (557, '4.01.01.01.04', 'RECEITAS DE SERVIÇOS DE ASSISTÊNCIA SOCIAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (558, '4.01.01.01.04.01.00', 'Pacientes Particulares',
        'Contas que registram as receitas de serviços na área de assistência social a pacientes particulares.', null,
        null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (559, '4.01.01.01.04.02.00', 'Convênios , Outros',
        'Contas que registram as receitas de serviços na área de assistência social a pacientes particulares através de convênios/contratos/termos de parcerias.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (560, '4.01.01.01.04.03.00', 'Doações/Subvenções Vinculadas',
        'Contas que registram as receitas recebidas como doações/subvenções vinculadas (Dec. 2.536/1998, art. 3, inciso V),  com destinação à área de assistência social, preferencialmente segregadas por níveis federal, estadual e municipal.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (561, '4.01.01.01.04.04.00', 'Doações',
        'Contas que registram as receitas recebidas como doações particulares não vinculadas,  com destinação à área de assistência social.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (562, '4.01.01.01.04.05.00', 'Contribuições',
        'Contas que registram as receitas recebidas como contribuições com destinação à área de assistência social.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (563, '4.01.01.01.04.06.00', 'Outras',
        'Contas que registram as demais receitas de serviços na área de assistência social.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (564, '4.01.01.01.05', 'RECEITAS DE OUTRAS ATIVIDADES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (565, '4.01.01.01.05.01.00', 'Contribuições Sindicais',
        'Contas que registram receitas com a natureza de contribuições sindicais.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (566, '4.01.01.01.05.02.00', 'Contribuições Confederativas/Associativas',
        'Contas que registram receitas com a natureza de contribuições confederativas e/ou associativas.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (567, '4.01.01.01.05.03.00', 'Mensalidades',
        'Contas que registram receitas com a natureza de mensalidades revertidas por seus associados.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (568, '4.01.01.01.05.04.00', 'Doações/Subvenções',
        'Contas que registram receitas com a natureza de doações e/ou subvenções recebidas de entidades públicas e/ou privadas, e de pessoas físicas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (569, '4.01.01.01.05.05.00', 'Outras Contribuições',
        'Demais contas que registram contribuições não especificadas anteriormente.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (570, '4.01.01.01.05.06.00', 'Outras', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (571, '4.01.01.01.09', 'DEDUÇÕES DA RECEITA BRUTA', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (572, '4.01.01.01.09.01.00', '(,) Vendas Canceladas',
        'Contas que registram vendas das prestações de serviços canceladas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (573, '4.01.01.01.09.02.00', '(,) Devoluções e Descontos Incondicionais',
        'Contas que registram as devoluções e descontos incondicionais nas atividades da entidade.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (574, '4.01.01.01.09.03.00', 'Outras', 'Contas que registram as demais deduções da receita bruta.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (575, '4.01.03', 'CUSTO DOS PRODUTOS E SERVIÇOS VENDIDOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (576, '4.01.03.01', 'CUSTO DOS PRODUTOS VENDIDOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (577, '4.01.03.01.01', 'CUSTO DOS PRODUTOS VENDIDOS PARA EDUCAÇÃO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (578, '4.01.03.01.01.01.00', 'Custos dos Produtos para Educação , Vendidos',
        'Contas que registram o custo do produto vendido na área de educação.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (579, '4.01.03.01.01.02.00', 'Custos dos Produtos para Educação , Gratuidades',
        'Contas que registram o custo do produto dado em gratuidade na área de educação.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (580, '4.01.03.01.01.03.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (581, '4.01.03.01.02', 'CUSTO DOS PRODUTOS VENDIDOS PARA SAÚDE', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (582, '4.01.03.01.02.01.00', 'Custos dos Produtos para Saúde  Vendidos',
        'Contas que registram o custo do produto vendido na área de saúde.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (583, '4.01.03.01.02.02.00', 'Custos dos Produtos para Saúde , Gratuidades',
        'Contas que registram o custo do produto dado em gratuidade na área de saúde.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (584, '4.01.03.01.02.03.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (585, '4.01.03.01.03', 'CUSTO DOS PRODUTOS VENDIDOS PARA ASSISTÊNCIA SOCIAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (586, '4.01.03.01.03.01.00', 'Custos dos Produtos para Assistência Social , Vendidos',
        'Contas que registram o custo do produto vendido na área de assistência social.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (587, '4.01.03.01.03.02.00', 'Custos dos Produtos para Assistência Social , Gratuidades',
        'Contas que registram o custo do produto dado em gratuidade na área de assistência social.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (588, '4.01.03.01.03.03.00', 'Outras', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (589, '4.01.03.01.04', 'CUSTO DOS PRODUTOS VENDIDOS PARA AS DEMAIS ATIVIDADES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (590, '4.01.03.01.04.01.00', 'Custos dos Produtos Vendidos em Geral',
        'Contas que registram o custo do produto vendido nas atividades não abrangidas anteriormente.', null, null,
        'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (591, '4.01.03.01.04.02.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (592, '4.01.03.02', 'CUSTO DOS SERVIÇOS PRESTADOS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (593, '4.01.03.02.01', 'CUSTO DOS SERVIÇOS PRESTADOS PARA EDUCAÇÃO', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (594, '4.01.03.02.01.01.00', 'Custo dos Serviços Prestados a Alunos Não Bolsistas',
        'Contas que registram o custo da prestação do serviço para os alunos não bolsistas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (595, '4.01.03.02.01.02.00', 'Custo dos Serviços Prestados a Convênios/Contratos/Parcerias (Exceto PROUNI)',
        'Contas que registram o custo da prestação do serviço para os alunos vinculados aos convênios/contratos/parcerias, exceto àqueles que estão no PROUNI.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (596, '4.01.03.02.01.03.00', 'Custo dos Serviços Prestados a Doações/Subvenções Vinculadas',
        'Contas que registram o custo da prestação do serviço para os alunos vinculados à doações/subvenções vinculadas (Dec. 2.536/1998, art. 3, inciso V),  com destinação à área de educação, preferencialmente segregadas por níveis federal, estadual e municipal.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (597, '4.01.03.02.01.04.00', 'Custo dos Serviços Prestados a Doações',
        'Contas que registram o custo da prestação do serviço para os alunos vinculados às demais doações,  com destinação à área de educação, exceto àquelas doações vinculadas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (598, '4.01.03.02.01.05.00', 'Custo dos Serviços Prestados ao PROUNI',
        'Contas que registram o custo da prestação do serviço para os alunos vinculados ao PROUNI.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (599, '4.01.03.02.01.06.00', 'Custo dos Serviços Prestados a Gratuidade',
        'Contas que registram o custo da prestação do serviço para os alunos com gratuidades de bolsas parciais e/ou integrais, exceto às vinculadas ao PROUNI, sendo que para as bolsas parciais, o custo deverá ser lançado com o valor parcial, o restante do custo deste aluno, será lançado na conta dos alunos não bolsistas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (600, '4.01.03.02.01.07.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (601, '4.01.03.02.02', 'CUSTO DOS SERVIÇOS PRESTADOS PARA SAÚDE', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (602, '4.01.03.02.02.01.00', 'Custo dos Serviços Prestados a Pacientes Particulares',
        'Contas que registram o custo da prestação do serviço para os pacientes particulares.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (603, '4.01.03.02.02.02.00', 'Custo dos Serviços Prestados a Convênios SUS',
        'Contas que registram o custo da prestação do serviço para os pacientes atendidos através do convênio do SUS.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (604, '4.01.03.02.02.03.00', 'Custo dos Serviços Prestados a Convênios/Contratos/Parcerias',
        'Contas que registram o custo da prestação do serviço para os pacientes vinculados aos convênios/contratos/parcerias, exceto àqueles que estão no SUS.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (605, '4.01.03.02.02.04.00', 'Custo dos Serviços Prestados a Doações/Subvenções Vinculadas',
        'Contas que registram o custo da prestação do serviço para os pacientes vinculados à doações/subvenções vinculadas (Dec. 2.536/1998, art. 3, inciso V),  com destinação à área de saúde, preferencialmente segregadas por níveis federal, estadual e municipal.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (606, '4.01.03.02.02.05.00', 'Custo dos Serviços Prestados a Doações',
        'Contas que registram o custo da prestação do serviço para os pacientes vinculados às demais doações,  com destinação à área de saúde, exceto àquelas doações vinculadas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (607, '4.01.03.02.02.06.00', 'Custo dos Serviços Prestados a Gratuidade',
        'Contas que registram o custo da prestação do serviço para os pacientes com gratuidades do pagamento, exceto às vinculadas ao SUS.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (608, '4.01.03.02.02.07.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (609, '4.01.03.02.03', 'CUSTO DOS SERVIÇOS PRESTADOS PARA ASSISTÊNCIA SOCIAL', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (610, '4.01.03.02.03.01.00', 'Custo dos Serviços Prestados a Pacientes Particulares',
        'Contas que registram o custo da prestação do serviço para os usuários particulares.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (611, '4.01.03.02.03.02.00', 'Custo dos Serviços Prestados a Convênios/Contratos/Parcerias',
        'Contas que registram o custo da prestação do serviço para os usuários vinculados aos convênios/contratos/parcerias, exceto àqueles que estão vinculados por doações e por subvenções.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (612, '4.01.03.02.03.03.00', 'Custo dos Serviços Prestados a Doações/Subvenções Vinculadas',
        'Contas que registram o custo da prestação do serviço para os usuários vinculados a doações/subvenções vinculadas (Dec. 2.536/1998, art. 3, inciso V),  com destinação à área de assistência social, preferencialmente segregadas por níveis federal, estadual e municipal.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (613, '4.01.03.02.03.04.00', 'Custo dos Serviços Prestados a Doações',
        'Contas que registram o custo da prestação do serviço para os pacientes vinculados às demais doações,  com destinação à área de saúde, exceto àquelas doações vinculadas.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (614, '4.01.03.02.03.05.00', 'Custo dos Serviços Prestados a Gratuidade',
        'Contas que registram o custo da prestação do serviço para os usuários com gratuidades do pagamento, exceto às atividades vinculadas por doações e por subvenções. Em especial, ao publico alvo da política nacional de assistência social.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (615, '4.01.03.02.03.06.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (616, '4.01.03.02.04', 'CUSTO DOS SERVIÇOS PRESTADOS PARA AS DEMAIS ATIVIDADES', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (617, '4.01.03.02.04.01.00', 'Custo dos Serviços Prestados em Geral',
        'Contas que registram o custo da prestação do serviço para as demais atividades, não informadas anteriormente.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (618, '4.01.03.02.04.02.00', 'Outros Custos', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (619, '4.01.05', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (620, '4.01.05.01', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (621, '4.01.05.01.01', 'OUTRAS RECEITAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (622, '4.01.05.01.01.01.00', 'Variações Cambiais Ativas',
        'Contas que registram os ganhos apurados em razão de variações ativas decorrentes da atualização dos direitos de crédito e obrigações, calculados com base nas variações das taxas de câmbio. Atenção: 1) as variações cambiais ativas decorrentes dos direitos de crédito e de obrigações, em função da taxa de câmbio, são consideradas como receita financeira, inclusive para fins de cálculo do lucro da exploração (Lei no 9.718, art. 9o c/c art. 17); 2) nas atividades de compra e venda, loteamento, incorporação e construção de imóveis, as variações cambiais ativas são reconhecidas como receita segundo as normas constantes da IN SRF no 84/79, de 20 de dezembro de 1979, da IN SRF no 23/83, de 25 de março de 1983, e da IN SRF no 67/88, de 21 de abril de 1988 (IN SRF no 25/99, de 25 de fevereiro de 1999).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (623, '4.01.05.01.01.02.00', 'Ganhos Auferidos no Mercado de Renda Variável, exceto Day,Trade',
        'Contas que registram: a) o somatório dos ganhos auferidos, em cada mês do período de apuração, em operações realizadas nas bolsas de valores, de mercadorias, de futuros e assemelhadas, existentes no País; b) os ganhos auferidos nas alienações, fora de bolsa, de ouro, ativo financeiro, e de participações societárias, exceto as alienações de participações societárias permanentes em sociedades coligadas e controladas e de participações societárias que permanecerem no ativo da pessoa jurídica até o término do ano,calendário seguinte ao de suas aquisições; e c) os rendimentos auferidos em operações de swap e no resgate de quota de fundo de investimento cujas carteiras sejam constituídas, no mínimo, por 67% (sessenta e sete por cento) de ações no mercado à vista de bolsa de valores ou entidade assemelhada (Lei no 9.532, de 1997, art. 28, alterado pela MP no 1.636, de 1998, art. 2o, e reedições). Considera,se ganho o resultado positivo auferido nas operações citadas acima, realizadas em cada mês, admitida a dedução dos custos e despesas incorridos, necessários à realização das operações. Atenção: 1) os ganhos auferidos em operações day,trade devem ser informados em conta específica; 2) o valor correspondente às perdas incorridas no mercado de renda variável, exceto day,trade, deve ser informado em conta específica;  3) são consideradas assemelhadas às bolsas de valores, de mercadorias e de futuros, as entidades cujo objeto social seja análogo ao das referidas bolsas e que funcionem sob a supervisão e fiscalização da Comissão de Valores Mobiliários (CVM).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (624, '4.01.05.01.01.03.00', 'Ganhos em Operações Day,Trade',
        'Contas que registram os ganhos diários auferidos, em cada mês do período de apuração, em operações day,trade. Considera,se ganho o resultado positivo auferido nas operações citadas acima, realizadas em cada mês, admitida a dedução dos custos e despesas incorridos, necessários à realização das operações.Não se caracteriza como day,trade o exercício da opção e a venda ou compra do ativo no mercado a vista, no mesmo dia.Também não se caracterizam como day,trade as operações iniciadas por intermédio de uma instituição e encerradas em outra, quando houver a liquidação física mediante movimentação de títulos ou valores mobiliários em custódia. Atenção: o valor correspondente às perdas incorridas nas operações day,trade deve ser informado em conta específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (625, '4.01.05.01.01.04.00', 'Outras Receitas de Aplicações Financeiras',
        'Contas que registram receitas auferidas no período de apuração relativas a juros, descontos, lucro na operação de reporte, prêmio de resgate de títulos ou debêntures e rendimento nominal auferido em aplicações financeiras de renda fixa, não incluídas em outras contas. As receitas dessa natureza, derivadas de operações com títulos vencíveis após o encerramento do período de apuração, serão rateadas segundo o regime de competência. Atenção: 1) as variações monetárias ativas decorrentes da atualização dos direitos de crédito e das obrigações, em função de índices ou coeficientes aplicáveis por disposição legal ou contratual, devem ser informadas como receita financeira; 2) as variações cambiais ativas devem ser informadas na conta específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (626, '4.01.05.01.01.05.00', 'Ganhos na Alienação de Participações Não Integrantes do Ativo Permanente',
        'Contas que registram os ganhos auferidos na alienação de ações, títulos ou quotas de capital não integrantes do ativo permanente, desde que não incluídos em outra conta específica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (627, '4.01.05.01.01.06.00', 'Resultados Positivos em Participações Societárias',
        'Contas que registram: a) os lucros e dividendos derivados de investimentos avaliados pelo custo de aquisição; b) os ganhos por ajustes no valor de investimentos relevantes avaliados pelo método da equivalência patrimonial, decorrentes de lucros apurados nas controladas e coligadas; Atenção: considera,se controlada a filial, a agência, a sucursal, a dependência ou o escritório de representação no exterior, sempre que os respectivos ativos e passivos não estejam incluídos na contabilidade da investidora, por força de normatização específica. c) as amortizações de deságios nas aquisições de investimentos avaliados pelo patrimônio líquido. O valor amortizado que for excluído do lucro líquido para determinação do lucro real deve ser controlado na Parte B do Livro de Apuração do Lucro Real até a alienação ou baixa da participação societária, quando, então, deve ser adicionado ao lucro líquido para determinação do lucro real no período de apuração em que for computado o ganho ou perda de capital havido. d) as bonificações recebidas;  Atenção: 1) as bonificações recebidas, decorrentes da incorporação de lucros ou reservas não tributados na forma do art. 35 da Lei no 7.713, de 1988, ou apurados nos anos,calendário de 1994 ou 1995, são  consideradas a custo zero, não afetando o valor do investimento nem o resultado do período de apuração (art. 3o da Lei no 8.849, de 1994, e art. 3o da Lei no 9.064, de 1995);  2) no caso de investimento avaliado pelo custo de aquisição, as bonificações recebidas, decorrentes da incorporação de lucros ou reservas tributados na forma do art. 35 da Lei no 7.713, de 1988, e de lucros ou reservas apurados no ano,calendário de 1993 ou a partir do ano,calendário de 1996, são registradas tomando,se como custo o valor da parcela dos lucros ou reservas capitalizados. e) os lucros e dividendos de participações societárias avaliadas pelo custo de aquisição; Atenção: os lucros ou dividendos recebidos em decorrência de participações societárias avaliadas pelo custo de aquisição adquiridas até 6 (seis) meses antes da data do recebimento devem ser registrados como diminuição do valor do custo, não sendo incluídos nesta conta. f) os resultados positivos decorrentes de participações societárias no exterior avaliadas pelo patrimônio líquido, os dividendos de participações avaliadas pelo custo de aquisição e os resultados de equivalência patrimonial relativos a filiais, sucursais ou agências da pessoa jurídica localizadas no exterior, em decorrência de operações realizadas naquelas filiais, sucursais ou agências.Os lucros auferidos no exterior serão adicionados ao lucro líquido, para efeito de determinação do lucro real, no período de apuração correspondente ao balanço levantado em 31 de dezembro do ano,calendário em que tiverem sido disponibilizados, observando,se o disposto nos arts. 394 e 395 do Decreto no 3.000, de 1999, e no art. 74 da Medida Provisória no 2.158,35, de 24 de agosto de 2001.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (628, '4.01.05.01.01.07.00', 'Rendimentos e Ganhos de Capital Auferidos no Exterior',
        'Contas que registram os rendimentos e ganhos de capital auferidos no exterior diretamente pela pessoa jurídica domiciliada no Brasil, pelos seus valores antes de descontado o tributo pago no país de origem.  Atenção:Os ganhos de capital referentes a alienações de bens e direitos do ativo permanente situados no exterior devem ser informados na conta Outras Receitas Não Operacionais ',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (629, '4.01.05.01.01.08.00', 'Reversão dos Saldos das Provisões Operacionais',
        'Contas que registram a reversão de  saldos não utilizados das provisões constituídas no balanço do período de apuração imediatamente anterior.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (630, '4.01.05.01.01.09.00', 'Outras Receitas Operacionais',
        'Contas que registram todas as demais receitas que, por definição legal, sejam consideradas operacionais, tais como: a) aluguéis de bens por empresa que não tenha por objeto a locação de móveis e imóveis; b) recuperações de despesas operacionais de períodos de apuração anteriores, tais como: prêmios de seguros, importâncias levantadas das contas vinculadas do FGTS, ressarcimento de desfalques, roubos e furtos, etc. As recuperações de custos e despesas no decurso do próprio período de apuração devem ser creditadas diretamente às contas de resultado em que foram debitadas; c) os créditos presumidos do IPI para ressarcimento do valor da Contribuição ao PIS/Pasep e Cofins; d) multas ou vantagens a título de indenização em virtude de rescisão contratual (Lei no 9.430, de 1996, art. 70, § 3o, II); e) o crédito presumido da contribuição para o PIS/Pasep e da Cofins concedido na  forma do art. 3o da Lei no 10.147, de 2000.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (631, '4.01.05.01.01.10.00', 'Outras', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (632, '4.01.07', 'DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (633, '4.01.07.01', 'DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (634, '4.01.07.01.01', 'DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (635, '4.01.07.01.01.00', 'Remunerações a Empregados',
        'Contas que registram os valores lançados como salários, gratificações, horas extras, adicionais e similares pag0s a empregados da entidade.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (636, '4.01.07.01.02.00', 'Indenizações Trabalhistas',
        'Contas que registram os valores lançados como abonos pecuniários, indenização de 40% do FGTS, indenizações determinadas pelo Juiz e similares pagas aos empregados.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (637, '4.01.07.01.03.00', 'Remuneração a Dirigentes e a Conselho de Administração/Fiscal',
        'Contas que registram a despesa incorrida relativa à remuneração mensal e fixa atribuída ao titular de firma individual, aos sócios, diretores e administradores de sociedades, ou aos representantes legais de sociedades estrangeiras, as despesas incorridas com os salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores (PN Cosit no 11, de 1992), e o valor referente às remunerações atribuídas aos membros do conselho fiscal/administração/consultivo.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (638, '4.01.07.01.04.00', 'Prestação de Serviços por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram as despesas correspondentes aos serviços prestados por pessoa física que não tenha vínculo empregatício com a pessoa jurídica declarante, tais como: comissões, corretagens, gratificações, honorários e outras remunerações, inclusive as relativas a empreitadas de obras exclusivamente de trabalho e as decorrentes de fretes e carretos em geral.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (639, '4.01.07.01.05.00', 'Prestação de Serviço por Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica, o valor das despesas correspondentes aos serviços prestados por outra pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (640, '4.01.07.01.06.00', 'Doações e Patrocínios de Caráter Cultural e Artístico (Lei no 8.313/1991)',
        'Contas que registram as doações e patrocínios efetuados no período de apuração em favor de projetos culturais previamente aprovados pelo Ministério da Cultura ou pela Agência Nacional do Cinema (Ancine), observada a legislação de concessão dos projetos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (641, '4.01.07.01.07.00', 'Doações a Instituições de Ensino e Pesquisa (Lei no 9.249/1995, art.13, § 2o)',
        'Contas que registram as doações a instituições de ensino e pesquisa cuja criação tenha sido autorizada por lei federal e que preencham os requisitos dos incisos I e II do art. 213 da Constituição Federal, de 1988, que são: a) comprovação de finalidade não,lucrativa e aplicação dos excedentes financeiros em educação; b) assegurar a destinação do seu patrimônio a outra escola comunitária, filantrópica ou confessional, ou ao Poder Público, no caso de encerramento de suas atividades.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (642, '4.01.07.01.08.00', 'Doações a Entidades Civis',
        'Contas que registram as doações efetuadas a: a) entidades civis, legalmente constituídas no Brasil, sem fins lucrativos, que prestem serviços gratuitos em benefício de empregados da pessoa jurídica doadora, e respectivos dependentes, ou em benefício da comunidade na qual atuem; e b) Organizações da Sociedade Civil de Interesse Público (OSCIP), qualificadas segundo as normas estabelecidas na Lei no 9.790, de 23 de março de 1999.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (643, '4.01.07.01.09.00', 'Outras Contribuições e Doações',
        'Contas que registram as doações feitas, entre outras, aos Fundos controlados pelos Conselhos Municipais, Estaduais e Nacional dos Direitos da Criança e do Adolescente.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (644, '4.01.07.01.10.00', 'FGTS (sem indenização 40%)',
        'Contas que registram o FGTS, inclusive os valores do FGTS do 13º. salário. Não informar os valores de indenização da multa de 40% do FGTS nesse item, e sim, na conta Indenizações Trabalhistas .',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (645, '4.01.07.01.11.00', 'Assistência Médica, Odontológica, Medicamentos, Aparelhos Ortopédicos e Similares',
        'Contas que registram as despesas com assistência médica, odontológica e farmacêutica.  Atenção: o valor referente à contratação de serviços de profissionais liberais sem vínculo empregatício ou de sociedades civis deve ser informado nas contas Prestação de Serviços por Pessoa Física sem Vínculo Empregatício  ou Prestação de Serviço por Pessoa Jurídica , conforme o caso.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (646, '4.01.07.01.12.00', 'Provisões para Férias e 13o Salário de Empregados',
        'Contas que registram as despesas com a constituição de provisões para: a) pagamento de remuneração correspondente a férias e adicional de férias de empregados, inclusive encargos sociais (Decreto no 3.000, de 1999, art. 337, e PN CST no 7, de 1980); b) o 13o salário, inclusive encargos sociais (Decreto no 3.000, de 1999, art. 338).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (647, '4.01.07.01.13.00', 'Demais Provisões',
        'Contas que registram as despesas com provisões não relacionadas nas contas específicas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (648, '4.01.07.01.14.00', 'Arrendamento Mercantil',
        'Contas que registram as despesas, não computadas nos custos, pagas ou creditadas a título de contraprestação de arrendamento mercantil, decorrentes de contrato celebrado com observância da Lei no 6.099, de 12 de setembro de 1974, com as alterações da Lei no 7.132, de 26 de outubro de 1983, e da Portaria MF no 140, de 1984',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (649, '4.01.07.01.15.00', 'Aluguéis',
        'Contas que registram as despesas com aluguéis não decorrentes de arrendamento mercantil.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (650, '4.01.07.01.16.00', 'Despesas com Veículos e de Conservação de Bens e Instalações',
        'Contas que registram as despesas relativas aos bens que não estejam ligados diretamente à produção, as realizadas com reparos que não impliquem aumento superior a um ano da vida útil do bem, prevista no ato de sua aquisição, e as relativas a combustíveis e lubrificantes para veículos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (651, '4.01.07.01.17.00', 'Propaganda e Publicidade',
        'Contas que registram as despesas com propaganda e publicidade.  Atenção: o valor referente à contratação de serviços de profissionais liberais sem vínculo empregatício ou de sociedades civis deve ser informado nas contas Prestação de Serviços por Pessoa Física sem Vínculo Empregatício  ou Prestação de Serviço por Pessoa Jurídica , conforme o caso.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (652, '4.01.07.01.18.00', 'Multas', 'Contas que registram as despesas com multas.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (653, '4.01.07.01.19.00', 'Encargos de Depreciação e Amortização',
        'Contas que registram apenas os encargos a esses títulos, com bens não aplicados diretamente na produção. Inclui a amortização dos ajustes de variação cambial contabilizada no ativo diferido, relativa à atividade geral da pessoa jurídica.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (654, '4.01.07.01.20.00', 'Repasses para Outras Entidades (Sindicatos/Federações/Confederações)',
        'Contas que foram repassadas parte das contribuições/doações/mensalidades e similares para Sindicatos/Federações/Confederações.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (655, '4.01.07.01.21.00', 'Contribuições Previdenciárias Patronais',
        'Contas que registram as contribuições previdenciárias devidas. No caso de imunes/isentas, informar o valor da contribuição previdenciária patronal devida como se sem isenção estivesse, devendo fazer um novo lançamento de reversão para evidenciar que é isenta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (656, '4.01.07.01.22.00', 'COFINS',
        'Contas que registram a Cofins devida. No  caso de imunes/isentas, informar o valor da Cofins devida como se sem isenção estivesse, devendo fazer um novo lançamento de reversão para evidenciar que é isenta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (657, '4.01.07.01.23.00', 'CSLL',
        'Contas que registram a CSLL devida. No caso de imunes/isentas, informar o valor da CSLL devida como se sem isenção estivesse, devendo fazer um novo lançamento de reversão para evidenciar que é isenta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (658, '4.01.07.01.24.00', 'PIS/PASEP', 'Contas que registram o valor da contribuição para o PIS/PASEP devida.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (659, '4.01.07.01.25.00', 'CPMF', 'Contas que registram o valor da CPMF devida.', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (660, '4.01.07.01.26.00', 'Demais Impostos, Taxas e Contribuições, exceto as citadas acima.',
        'Contas que registram os demais impostos, taxas e contribuições, exceto:  a) incorporadas ao custo de bens do ativo permanente; b) correspondentes aos impostos não recuperáveis, incorporados ao custo das matérias,primas, materiais secundários, materiais de embalagem e mercadorias destinadas à revenda; c) correspondentes aos impostos recuperáveis; d) correspondentes aos impostos e contribuições redutores da receita bruta.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (661, '4.01.07.01.27.00', 'Outras Despesas Operacionais', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (662, '4.01.09', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (663, '4.01.09.01', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (664, '4.01.09.01.01', 'OUTRAS DESPESAS OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (665, '4.01.09.01.01.01.00', '(,) Variações Cambiais Passivas',
        'Contas que registram as perdas monetárias passivas resultantes da atualização dos direitos de créditos e das obrigações, calculadas com base nas variações nas taxas de câmbio (Lei no 9.069, de 1995, art.52, e Lei no 9.249, de 1995, art. 8o), inclusive a variação cambial passiva correspondente:  a) à atualização das obrigações e dos créditos em moeda estrangeira, registrada em qualquer data e apurada no encerramento do período de apuração em função da taxa de câmbio vigente; b) às operações com moeda estrangeira e conversão de obrigações para moeda nacional, ou novação dessas obrigações, ou sua extinção, total ou parcial, em virtude de capitalização, dação em pagamento, compensação, ou qualquer outro modo, desde que observadas as condições fixadas pelo Banco Central do Brasil.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (666, '4.01.09.01.01.02.00', '(,) Perdas Incorridas no Mercado de Renda Variável, exceto Day,Trade',
        'Contas que registram: a) o somatório das perdas incorridas, em cada mês do período de apuração, em operações realizadas nas bolsas de valores, de mercadorias, de futuros e assemelhadas, existentes no País; b) as perdas incorridas nas alienações, fora de bolsa, de ouro, ativo financeiro e de participações societárias, exceto as alienações de participações societárias permanentes em sociedades  coligadas e controladas e de participações societárias, que permanecerem no ativo da pessoa jurídica até o término do ano,calendário seguinte ao de suas aquisições; e c) as perdas em operações de swap e no resgate de quota de fundo de investimento que mantenha, no mínimo, 67% (sessenta e sete por cento) de ações negociadas no mercado a vista de bolsa de valores ou entidade assemelhada (Lei no 9.532, de 1997, art. 28, alterado pela MP no 1.636, de 1998, art. 2o, e reedições).São consideradas assemelhadas às bolsas de valores, de mercadorias e de futuros, as entidades cujo objeto social seja análogo ao das referidas bolsas e que funcionem sob a supervisão e fiscalização da Comissão de Valores Mobiliários (CVM). Atenção: As perdas apuradas em operações day,trade devem ser informadas em conta própria.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (667, '4.01.09.01.01.03.00', '(,) Perdas em Operações Day,Trade',
        'Contas que registram o somatório das perdas diárias apuradas, em cada mês do período de apuração, em operações day,trade.Não se caracteriza como day,trade o exercício da opção e a venda ou compra do ativo no mercado a vista, no mesmo dia.Também não se caracterizam como day,trade as operações iniciadas por intermédio de uma instituição e encerradas em outra, quando houver a liquidação física mediante movimentação de títulos ou valores mobiliários em custódia.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (668, '4.01.09.01.01.04.00', '(,) Outras Despesas de Aplicações',
        'Contas que registram as despesas relativas a juros, não incluídas  em outras contas, a descontos de títulos de crédito e outros títulos. Tais despesas serão obrigatoriamente rateadas, segundo o regime de competência. Atenção:  1) as variações monetárias passivas decorrentes da atualização das  obrigações, em função de índices ou coeficientes aplicáveis por disposição legal ou contratual, devem ser informadas como despesas financeiras; 2) as variações cambiais passivas não devem ser informadas nesta conta, e sim na conta Variações Cambiais Passivas .',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (669, '4.01.09.01.01.05.00', '(,) Prejuízos na Alienação de Participações Não Integrantes do Ativo Permanente',
        'Contas que registram os prejuízos havidos em virtude de alienação, títulos não integrantes do ativo permanente, desde que não incluídos nas contas acima.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (670, '4.01.09.01.01.06.00', '(,) Resultados Negativos em Participações Societárias',
        'Contas que registram as perdas por ajustes no valor de investimentos relevantes, avaliados pelo método da equivalência patrimonial, decorrentes de prejuízos apurados nas controladas e coligadas.  Atenção: considera,se controlada a filial, a agência, a sucursal, a dependência ou o escritório de representação no exterior, sempre que os respectivos ativos e passivos não estejam incluídos na contabilidade da investidora, por força de normatização específica. Devem, também, ser indicados nesta conta os resultados negativos derivados de participações societárias no exterior, avaliadas pelo patrimônio líquido. Incluem,se, nestas informações, as perdas apuradas em filiais, sucursais e agências da pessoa jurídica localizadas no exterior.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (671, '4.01.09.01.01.07.00', '(,) Perdas em Operações Realizadas no Exterior',
        'Contas que registram as perdas em operações realizadas no exterior diretamente pela pessoa jurídica domiciliada no Brasil, com exceção das perdas de capital decorrentes da alienação de bens e direitos do ativo permanente situados no exterior, que devem ser indicadas na conta Outras Despesas Não Operacionais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (672, '4.01.09.01.01.08.00', 'Outras Despesas Operacionais', '', null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (673, '4.03', 'OUTRAS RECEITAS E DESPESAS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (674, '4.03.01', 'RECEITAS E DESPESAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (675, '4.03.01.01', 'RECEITAS E DESPESAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (676, '4.03.01.01.01', 'RECEITAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (677, '4.03.01.01.01.01.00', 'Receitas de Alienações de Bens e Direitos do Ativo Permanente.',
        'Contas que registram as receitas auferidas por meio de alienações, inclusive por desapropriação de bens e direitos do ativo permanente. O valor relativo às receitas obtidas pela venda de sucata e de bens ou direitos do ativo permanente baixados em virtude de terem se tornado imprestáveis, obsoletos ou caído em desuso deve ser informado na conta Outras Receitas Não Operacionais . Os valores correspondentes ao ganho ou perda de capital decorrente da alienação de bens e direitos do ativo permanente situados no exterior devem ser indicados, pelo seu resultado, nas contas Outras Receitas Não Operacionais  ou Outras Despesas Não Operacionais , conforme o caso.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (678, '4.03.01.01.01.02.00', 'Outras Receitas Não Operacionais',
        'Contas que registram: a) todas as demais receitas decorrentes de operações não incluídas nas atividades principais e acessórias da empresa, tais como: a reversão do saldo da provisão para perdas prováveis na realização de investimentos e a reserva de reavaliação realizada no período de apuração, quando computada em conta de resultado;  b) os ganhos de capital por variação na percentagem de participação no capital social de coligada ou controlada, quando o investimento for avaliado pela  equivalência patrimonial (Decreto no 3.000, de 1999, art. 428); c) os ganhos de capital decorrentes da alienação de bens e direitos do ativo permanente situados no exterior.  Devem ser indicadas tanto as contas que registram as receitas quanto as que registram os custos.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (679, '4.03.02.01.01', 'DESPESAS NÃO OPERACIONAIS', '', null, null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (680, '4.03.02.01.01.01.00', '(,) Valor Contábil dos Bens e Direitos Alienados',
        'Contas que registram o valor contábil dos bens do ativo permanente baixados no curso do período de apuração, cuja receita da venda tenha sido indicada na conta Receitas de Alienações de Bens e Direitos do Ativo Permanente . O valor contábil de bens ou direitos baixados em virtude de terem se tornado imprestáveis, obsoletos ou caído em desuso e o valor contábil de bens ou direitos situados no exterior devem ser informados na conta Outras Receitas Não Operacionais.',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (681, '4.03.02.01.01.02.00', '(,) Outras Despesas Não Operacionais',
        'Contas que registram: a) o valor contábil dos bens do ativo permanente baixados no curso do período de apuração não incluídos na conta precedente e a despesa com a constituição da provisão para perdas prováveis na realização de investimentos;  Atenção: sobre a definição de valor contábil, consultar o § 1o do art. 418 e o art. 426, ambos do Decreto no 3.000, de 1999. b) as perdas de capital por variação na percentagem de participação no capital social de coligada ou controlada no Brasil, quando o investimento for avaliado pela equivalência patrimonial (Decreto no 3.000, de 1999, art. 428).',
        null, null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (682, '5', 'CUSTOS DE PRODUÇÃO', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (683, '5.01', 'CUSTO DOS BENS E SERVIÇOS PRODUZIDOS', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (684, '5.01.01', 'CUSTO DOS PRODUTOS DE FABRICAÇÃO PRÓPRIA PRODUZIDOS', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (685, '5.01.01.01.00', 'Consumo de Insumos',
        'Contas que registram o consumo, durante o período de apuração, de matéria,prima, material direto e material de embalagem, no mercado interno e externo, para utilização no processo produtivo, os valores referentes aos custos com transporte e seguro até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (686, '5.01.01.04.00', 'Remuneração a Dirigentes de Ligados à Produção',
        'Contas que registram: a) a remuneração mensal e fixa dos dirigentes diretamente ligados à produção, pelo valor total do custo incorrido no período de apuração, exceto os encargos sociais (Previdência Social e FGTS) que são informados em conta distinta; b) o valor relativo aos custos incorridos com salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores, se ligados diretamente à produção (PN Cosit nº 11, de 30 de setembro de 1992). Atenção: deve ser incluído nesta conta o valor das gratificações dos dirigentes ligados à produção, inclusive o 13º salário.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (687, '5.01.01.05.00', 'Custo do Pessoal Aplicado na Produção',
        'Contas que representem do custo com ordenados, salários e outros custos com empregados ligados à produção da empresa, tais como: moradia, seguro de vida e outras de caráter remuneratório. Inclusive os custos com supervisão direta, manutenção e guarda das instalações, decorrentes de vínculo empregatício com a pessoa jurídica.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (688, '5.01.01.05.03', 'Planos de Poupança e Investimentos de Empregados Ligados à Produção',
        'Contas que registram o valor total dos gastos efetuados com Planos de Poupança e Investimentos (PAIT), relativos ao pessoal ligado à produção',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (689, '5.01.01.05.05', 'Fundo de Aposentadoria Programada Individual de Empregados Ligados à Produção',
        'Contas que registram o valor total dos gastos efetuados com Fundos de Aposentadoria Programada Individual (FAPI), relativos ao pessoal ligado à produção',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (690, '5.01.01.05.07', 'Plano de Previdência Privada de Empregados Ligados à Produção',
        'Contas que registram o valor total dos gastos efetuados com Planos de Previdência Privada, relativos ao pessoal ligado à produção.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (691, '5.01.01.05.09', 'Outros Gastos com Pessoal Ligado à Produção',
        'Contas que registram os gastos com empregados, computados nos custos, não enquadrados nas contas precedentes. Atenção:  não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (692, '5.01.01.06.00', 'Prestação de Serviços por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica, os gastos correspondentes aos serviços prestados por pessoa física que não tenha vínculo empregatício com a pessoa jurídica, tais como: comissões, corretagens, gratificações, honorários, direitos autorais e outras remunerações, inclusive as relativas a empreitadas de obras exclusivamente de trabalho e as decorrentes de fretes e carretos em gera, computadas nos custos.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (693, '5.01.01.07.00', 'Prestação de Serviço Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica, o valor dos gastos correspondentes aos serviços prestados por outra pessoa jurídica à pessoa jurídica declarante, computados nos custos',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (694, '5.01.01.08.00', 'Serviços Prestados por Cooperativa de Trabalho',
        'Contas que registram os serviços prestados por cooperativa de trabalho', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (695, '5.01.01.09.00', 'Locação de Mão,de,obra',
        'Contas que registram o valor total dos gastos efetuados no período com a contratação de serviços executados mediante cessão de mão,de,obra ou empreitada, inclusive em regime temporário, sujeitos à retenção de contribuição previdenciária, nos termos do art. 219 do Regulamento da Previdência Social , RPS, aprovado pelo Decreto nº 3.048, de 1999',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (696, '5.01.01.10.00', 'Encargos Sociais  Previdência Social',
        'Contas que registram as contribuições para a Previdência Social (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (697, '5.01.01.11.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para o FGTS (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (698, '5.01.01.12.00', 'Encargos Sociais  Outros',
        'Contas que registram encargos sociais, relativos ao pessoal ligado diretamente à produção, não classificados nas contas Encargos Sociais  Previdência Social ou Encargos Sociais  FGTS.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (699, '5.01.01.13.00', 'Alimentação do Trabalhador',
        'Contas que registram os custos com alimentação do pessoal ligado diretamente à produção, realizados durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (700, '5.01.01.14.00', 'Manutenção e Reparo de Bens Aplicados na Produção',
        'Contas que representam somente os custos realizados com reparos que não implicaram aumento superior a um ano da vida útil prevista no ato da aquisição do bem.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (701, '5.01.01.15.00', 'Arrendamento Mercantil',
        'Contas que representam o valor do custo incorrido a título de contraprestação de arrendamento mercantil de bens alocados na produção, segundo contratos celebrados com observância da Lei nº 6.099, de 12 de setembro de 1974, com as alterações da Lei nº 7.132, de 26 de outubro de 1983. Os custos com aluguel de outros bens alocados à produção, mediante contrato diferente do de arrendamento mercantil, devem ser indicados em Outros Custos. Os valores referentes a bens que não sejam intrinsecamente relacionados com a produção devem ser informados na conta Arrendamento Mercantil do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (702, '5.01.01.16.00', 'Encargos de Depreciação, Amortização e Exaustão',
        'Contas que registram os encargos a esses títulos com bens aplicados diretamente na produção. Os encargos que não forem decorrentes de bens intrinsecamente relacionados com a produção devem ser informados na conta Encargos de Depreciação e Amortização do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (703, '5.01.01.17.00', 'Constituição de Provisões',
        'Contas que registram os encargos com a constituição de provisões que devam ser imputados aos custos de produção da empresa no período de apuração.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (704, '5.01.01.18.00', 'Serviços Prestados por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados à pessoa jurídica por pessoa física sem vínculo empregatício, relacionados com a atividade industrial da pessoa jurídica .',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (705, '5.01.01.19.00', 'Serviços Prestados Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados por pessoa jurídica, relacionados com atividade industrial da pessoa jurídica declarante.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (706, '5.01.01.20.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram  as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (707, '5.01.01.21.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (708, '5.01.01.90.00', 'Outros Custos',
        'Contas que representam os demais custos da empresa no processo de produção, para os quais não haja conta maIs específica ou cujas classificações contábeis não se adaptem à nomenclatura específica, tais como: custo referente ao valor de bens de consumo eventual; as quebras ou perdas de estoque, e as ocorridas na fabricação, no transporte e manuseio.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (709, '5.01.03', 'CUSTO DOS SERVIÇOS PRODUZIDOS', '', '2008,1,01', null, 'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (710, '5.01.03.02.00', 'Material Aplicado na Produção de Serviços',
        'Contas correspondentes aos materiais aplicados diretamente na produção de serviços dos serviços durante o período de apuração.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (711, '5.01.03.04.00', 'Remuneração a Dirigentes ligados à Produção de Serviços',
        'Contas que registram: a) a remuneração mensal e fixa dos dirigentes diretamente ligados à produção de serviços, pelo valor total do custo incorrido no período de apuração, exceto os encargos sociais (Previdência Social e FGTS) que são informados em conta distinta; b) o valor relativo aos custos incorridos com salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores, se ligados diretamente à produção de serviços (PN Cosit nº 11, de 30 de setembro de 1992). Atenção: deve ser incluído nesta conta o valor das gratificações dos dirigentes ligados à produção de serviços, inclusive o 13º salário.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (712, '5.01.03.05.00', 'Custo do Pessoal Aplicado na Produção de Serviços',
        'Contas que representem do custo com ordenados, salários e outros custos com empregados ligados à produção de serviços da empresa, tais como: moradia, seguro de vida e outras de caráter remuneratório. Inclusive os custos com supervisão direta, manutenção e guarda das instalações, decorrentes de vínculo empregatício com a pessoa jurídica.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (713, '5.01.03.05.03', 'Planos de Poupança e Investimentos de Empregados Ligados à Produção de Serviços',
        'Contas que registram o valor total dos gastos efetuados com Planos de Poupança e Investimentos (PAIT), relativos ao pessoal ligado à produção de serviços',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (714, '5.01.03.05.05',
        'Fundo de Aposentadoria Programada Individual de Empregados Ligados à Produção de Serviços',
        'Contas que registram o valor total dos gastos efetuados com Fundos de Aposentadoria Programada Individual (FAPI), relativos ao pessoal ligado à produção de serviços',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (715, '5.01.03.05.07', 'Plano de Previdência Privada de Empregados Ligados à Produção de Serviços',
        'Contas que registram o valor total dos gastos efetuados com Planos de Previdência Privada, relativos ao pessoal ligado à produção de serviços.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (716, '5.01.03.05.09', 'Outros Gastos com Pessoal Ligado à Produção de Serviços',
        'Contas que registram os gastos com empregados, computados nos custos, não enquadrados nas contas precedentes Atenção:  não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (717, '5.01.03.06.00', 'Prestação de Serviços por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica, os gastos correspondentes aos serviços prestados por pessoa física que não tenha vínculo empregatício com a pessoa jurídica, tais como: comissões, corretagens, gratificações, honorários, direitos autorais e outras remunerações, inclusive as relativas a empreitadas de obras exclusivamente de trabalho e as decorrentes de fretes e carretos em gera, computadas nos custos.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (718, '5.01.03.07.00', 'Prestação de Serviço Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica, o valor dos gastos correspondentes aos serviços prestados por outra pessoa jurídica à pessoa jurídica declarante, computados nos custos',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (719, '5.01.03.08.00', 'Serviços Prestados por Cooperativa de Trabalho',
        'Contas que registram os serviços prestados por cooperativa de trabalho', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (720, '5.01.03.09.00', 'Locação de Mão,de,obra',
        'Contas que registram o valor total dos gastos efetuados no período com a contratação de serviços executados mediante cessão de mão,de,obra ou empreitada, inclusive em regime temporário, sujeitos à retenção de contribuição previdenciária, nos termos do art. 219 do Regulamento da Previdência Social , RPS, aprovado pelo Decreto nº 3.048, de 1999',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (721, '5.01.03.10.00', 'Encargos Sociais  Previdência Social',
        'Contas que registram as contribuições para a Previdência Social (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção de serviços.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (722, '5.01.03.11.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para o FGTS (inclusive dos dirigentes de indústria  PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção de serviços.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (723, '5.01.03.12.00', 'Encargos Sociais  Outros',
        'Contas que registram encargos sociais, relativos ao pessoal ligado diretamente à produção de serviços, não classificados nas contas Encargos Sociais , Previdência Social ou Encargos Sociais , FGTS.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (724, '5.01.03.13.00', 'Alimentação do Trabalhador',
        'Contas que registram os custos com alimentação do pessoal ligado diretamente à produção de serviços, realizados durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (725, '5.01.03.14.00', 'Manutenção e Reparo de Bens Aplicados na Produção de Serviços',
        'Contas que representam somente os custos realizados com reparos que não implicaram aumento superior a um ano da vida útil prevista no ato da aquisição do bem.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (726, '5.01.03.15.00', 'Arrendamento Mercantil',
        'Contas que representam o valor do custo incorrido a título de contraprestação de arrendamento mercantil de bens alocados na produção de serviços, segundo contratos celebrados com observância da Lei nº 6.099, de 12 de setembro de 1974, com as alterações da Lei nº 7.132, de 26 de outubro de 1983. Os custos com aluguel de outros bens alocados à produção de serviços, mediante contrato diferente do de arrendamento mercantil, devem ser indicados em Outros Custos. Os valores referentes a bens que não sejam intrinsecamente relacionados com a produção de serviços devem ser informados na conta Arrendamento Mercantil do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (727, '5.01.03.16.00', 'Encargos de Depreciação, Amortização e Exaustão',
        'Contas que registram os encargos a esses títulos com bens aplicados diretamente na produção de serviços. Os encargos que não forem decorrentes de bens intrinsecamente relacionados com a produção de serviços devem ser informados na conta Encargos de Depreciação e Amortização do grupo DESPESAS OPERACIONAIS DAS ATIVIDADES EM GERAL.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (728, '5.01.03.17.00', 'Constituição de Provisões',
        'Contas que registram os encargos com a constituição de provisões que devam ser imputados aos custos de produção de serviços da empresa no período de apuração.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (729, '5.01.03.18.00', 'Serviços Prestados por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados à pessoa jurídica por pessoa física sem vínculo empregatício, relacionados com a atividade industrial da pessoa jurídica .',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (730, '5.01.03.19.00', 'Serviços Prestados Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados por pessoa jurídica, relacionados com atividade industrial da pessoa jurídica declarante.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (731, '5.01.03.20.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (732, '5.01.03.21.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (733, '5.01.03.90.00', 'Outros Custos',
        'Contas que representam os demais custos da empresa no processo de produção de serviços, para os quais não haja conta mais específica ou cujas classificações contábeis não se adaptem à nomenclatura específica, tais como: custo referente ao valor de bens de consumo eventual; as quebras ou perdas de estoque, e as ocorridas na fabricação, no transporte e manuseio.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (734, '5.01.05', 'CUSTO DOS PRODUTOS DE FABRICAÇÃO PRÓPRIA PRODUZIDOS DA ATIVIDADE RURAL', '', '2008,1,01', null,
        'S');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (735, '5.01.05.01.00', 'Consumo de Insumos',
        'Contas que registram o consumo, durante o período de apuração, de matéria,prima, material secundário e material de embalagem, no mercado interno e externo, para utilização no processo produtivo, os valores referentes aos custos com transporte e seguro até o estabelecimento do contribuinte, os tributos não recuperáveis devidos na importação e o custo relativo ao desembaraço aduaneiro.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (736, '5.01.05.04.00', 'Remuneração a Dirigentes de Ligados à Produção',
        'Contas que registram: a) a remuneração mensal e fixa dos dirigentes diretamente ligados à produção, pelo valor total do custo incorrido no período de apuração, exceto os encargos sociais (Previdência Social e FGTS) que são informados em conta distinta; b) o valor relativo aos custos incorridos com salários indiretos concedidos pela empresa a administradores, diretores, gerentes e seus assessores, se ligados diretamente à produção (PN Cosit nº 11, de 30 de setembro de 1992). Atenção: deve ser incluído nesta conta o valor das gratificações dos dirigentes ligados à produção, inclusive o 13º salário.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (737, '5.01.05.05.00', 'Custo do Pessoal Aplicado na Produção',
        'Contas que representem do custo com ordenados, salários e outros custos com empregados ligados à produção da empresa, tais como: moradia, seguro de vida e outras de caráter remuneratório. Inclusive os custos com supervisão direta, manutenção e guarda das instalações, decorrentes de vínculo empregatício com a pessoa jurídica.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (738, '5.01.05.05.03', 'Planos de Poupança e Investimentos de Empregados Ligados à Produção',
        'Contas que registram o valor total dos gastos efetuados com Planos de Poupança e Investimentos (PAIT), relativos ao pessoal ligado à produção',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (739, '5.01.05.05.05', 'Fundo de Aposentadoria Programada Individual de Empregados Ligados à Produção',
        'Contas que registram o valor total dos gastos efetuados com Fundos de Aposentadoria Programada Individual (FAPI), relativos ao pessoal ligado à produção',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (740, '5.01.05.05.07', 'Plano de Previdência Privada de Empregados Ligados à Produção',
        'Contas que registram o valor total dos gastos efetuados com Planos de Previdência Privada, relativos ao pessoal ligado à produção.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (741, '5.01.05.05.09', 'Outros Gastos com Pessoal Ligado à Produção',
        'Contas que registram os gastos com empregados, computados nos custos, não enquadrados nas contas precedentes. Atenção:  não deve ser informado nesta conta o valor referente às participações dos empregados no lucro da pessoa jurídica. Esse valor deve ser informado na conta Participações de Empregados.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (742, '5.01.05.06.00', 'Prestação de Serviços por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica, os gastos correspondentes aos serviços prestados por pessoa física que não tenha vínculo empregatício com a pessoa jurídica, tais como: comissões, corretagens, gratificações, honorários, direitos autorais e outras remunerações, inclusive as relativas a empreitadas de obras exclusivamente de trabalho e as decorrentes de fretes e carretos em gera, computadas nos custos.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (743, '5.01.05.07.00', 'Prestação de Serviço Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica, o valor dos gastos correspondentes aos serviços prestados por outra pessoa jurídica à pessoa jurídica declarante, computados nos custos',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (744, '5.01.05.08.00', 'Serviços Prestados por Cooperativa de Trabalho',
        'Contas que registram os serviços prestados por cooperativa de trabalho', '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (745, '5.01.05.09.00', 'Locação de Mão,de,obra',
        'Contas que registram o valor total dos gastos efetuados no período com a contratação de serviços executados mediante cessão de mão,de,obra ou empreitada, inclusive em regime temporário, sujeitos à retenção de contribuição previdenciária, nos termos do art. 219 do Regulamento da Previdência Social , RPS, aprovado pelo Decreto nº 3.048, de 1999',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (746, '5.01.05.10.00', 'Encargos Sociais  Previdência Social',
        'Contas que registram as contribuições para a Previdência Social (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (747, '5.01.05.11.00', 'Encargos Sociais  FGTS',
        'Contas que registram as contribuições para o FGTS (inclusive dos dirigentes de indústria , PN CST no 35, de 31 de agosto de 1981), relativas ao pessoal ligado diretamente à produção.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (748, '5.01.05.12.00', 'Encargos Sociais  Outros',
        'Contas que registram encargos sociais, relativos ao pessoal ligado diretamente à produção, não classificados nas contas Encargos Sociais  Previdência Social ou Encargos Sociais  FGTS.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (749, '5.01.05.13.00', 'Alimentação do Trabalhador',
        'Contas que registram os custos com alimentação do pessoal ligado diretamente à produção, realizados durante o período de apuração, ainda que a pessoa jurídica não tenha Programa de Alimentação do Trabalhador aprovado pelo Ministério do Trabalho.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (750, '5.01.05.14.00', 'Manutenção e Reparo de Bens Aplicados na Produção',
        'Contas que representam somente os custos realizados com reparos que não implicaram aumento superior a um ano da vida útil prevista no ato da aquisição do bem.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (751, '5.01.05.15.00', 'Arrendamento Mercantil',
        'Contas que representam o valor do custo incorrido a título de contraprestação de arrendamento mercantil de bens alocados na produção, segundo contratos celebrados com observância da Lei nº 6.099, de 12 de setembro de 1974, com as alterações da Lei nº 7.132, de 26 de outubro de 1983. Os custos com aluguel de outros bens alocados à produção, mediante contrato diferente do de arrendamento mercantil, devem ser indicados em Outros Custos. Os valores referentes a bens que não sejam intrinsecamente relacionados com a produção devem ser informados na conta Arrendamento Mercantil do grupo DESPESAS OPERACIONAIS DA ATIVIDADE RURAL.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (752, '5.01.05.16.00', 'Encargos de Depreciação, Amortização e Exaustão',
        'Contas que registram os encargos a esses títulos com bens aplicados diretamente na produção. Os encargos que não forem decorrentes de bens intrinsecamente relacionados com a produção devem ser informados na conta Encargos de Depreciação e Amortização do grupo DESPESAS OPERACIONAIS DA ATIVIDADE RURAL.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (753, '5.01.05.17.00', 'Constituição de Provisões',
        'Contas que registram os encargos com a constituição de provisões que devam ser imputados aos custos de produção da empresa no período de apuração.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (754, '5.01.05.18.00', 'Serviços Prestados por Pessoa Física sem Vínculo Empregatício',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados à pessoa jurídica por pessoa física sem vínculo empregatício, relacionados com a atividade industrial da pessoa jurídica .',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (755, '5.01.05.19.00', 'Serviços Prestados Pessoa Jurídica',
        'Contas que registram, salvo se houver conta mais específica neste plano referencial, os custos correspondentes aos serviços prestados por pessoa jurídica, relacionados com atividade industrial da pessoa jurídica declarante.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (756, '5.01.05.20.00', 'Royalties e Assistência Técnica  PAÍS',
        'Contas que registram  as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no Brasil, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (757, '5.01.05.21.00', 'Royalties e Assistência Técnica  EXTERIOR',
        'Contas que registram as importâncias pagas a beneficiário pessoa física ou jurídica, residente ou domiciliado no exterior, a título de royalties e assistência técnica, científica ou assemelhada, que estejam relacionadas com a atividade industrial.',
        '2008,1,01', null, 'A');
insert into PLANO_CONTA_REF_SPED(ID, COD_CTA_REF, DESCRICAO, ORIENTACOES, INICIO_VALIDADE, FIM_VALIDADE, TIPO)
values (758, '5.01.05.90.00', 'Outros Custos',
        'Contas que representam os demais custos da empresa no processo de produção, para os quais não haja conta maIs específica ou cujas classificações contábeis não se adaptem à nomenclatura específica, tais como: custo referente ao valor de bens de consumo eventual; as quebras ou perdas de estoque, e as ocorridas na fabricação, no transporte e manuseio.',
        '2008,1,01', null, 'A');

--Plano de Contas

INSERT INTO plano_conta (id, id_empresa, nome, data_inclusao, mascara, niveis)
VALUES (1, 1, 'PLANO PADRAO', NULL, '#.##.###.###', 4);

-- Contabil

INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (3, 1, NULL, 1, '1', NULL, 'ATIVO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (6, 1, NULL, 1, '2', NULL, 'PASSIVO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (7, 1, NULL, 1, '3', NULL, 'DESPESAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (8, 1, NULL, 1, '4', NULL, 'RECEITAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (9, 1, NULL, 1, '5', NULL, 'RESULTADO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (13, 1, 3, 1, '1.1 ', NULL, 'CIRCULANTE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (14, 1, 3, 1, '1.1.1', NULL, 'DISPONÍVEL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (15, 1, 14, 1, '1.1.1.001', NULL, 'CAIXA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (16, 1, 14, 1, '1.1.1.002', NULL, 'BANCOS CONTA MOVIMENTO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (17, 1, 13, 1, '1.1.2', NULL, 'REALIZAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (18, 1, 17, 1, '1.1.2.001', NULL, 'CLIENTES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (19, 1, 17, 1, '1.1.2.002', NULL, '( - ) DUPLICATAS DESCONTADAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (20, 1, 17, 1, '1.1.2.003', NULL, 'APLICAÇÕES FINANCEIRAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (21, 1, 17, 1, '1.1.2.004', NULL, 'IMPOSTOS A RECUPERAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (22, 1, 17, 1, '1.1.2.005', NULL, 'DESPESAS DO EXERCÍCIO SEGUINTE', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (23, 1, 17, 1, '1.1.2.006', NULL, '( - ) PROVISÃO PARA DEVEDORES DUVIDOSOS', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (24, 1, 13, 1, '1.1.3', NULL, 'ESTOQUES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (25, 1, 24, 1, '1.1.3.001', NULL, 'ESTOQUE DE MERCADORIAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (26, 1, 24, 1, '1.1.3.002', NULL, '( - ) PROVISÃO PARA AJUSTE AO VALOR DE MERCADO', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (27, 1, 3, 1, '1.2', NULL, 'ATIVO REALIZÁVEL A LONGO PRAZO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (28, 1, 27, 1, '1.2.1', NULL, 'REALIZAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (29, 1, 28, 1, '1.2.1.001', NULL, 'TÍTULOS A RECEBER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (30, 1, 3, 1, '1.3', NULL, 'ATIVO PERMANENTE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (31, 1, 30, 1, '1.3.1', NULL, 'INVESTIMENTOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (32, 1, 31, 1, '1.3.1.001', NULL, 'PARTICIPAÇÕES EM OUTRAS CIAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (33, 1, 31, 1, '1.3.1.002', NULL, 'IMÓVEIS PARA RENDA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (34, 1, 30, 1, '1.3.2', NULL, 'IMOBILIZADO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (35, 1, 34, 1, '1.3.2.001', NULL, 'EQUIPAMENTOS DE INFORMÁTICA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (36, 1, 34, 1, '1.3.2.002', NULL, 'IMÓVEIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (37, 1, 34, 1, '1.3.2.003', NULL, 'INSTALAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (38, 1, 34, 1, '1.3.2.004', NULL, 'MÁQUINAS E EQUIPAMENTOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (39, 1, 34, 1, '1.3.2.005', NULL, 'MÓVEIS E UTENSÍLIOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (40, 1, 34, 1, '1.3.2.006', NULL, 'VEÍCULOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (41, 1, 34, 1, '1.3.2.007', NULL, '( - ) DEPRECIAÇÃO ACUMULADA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (42, 1, 30, 1, '1.3.3', NULL, 'DIFERIDO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (43, 1, 42, 1, '1.3.3.001', NULL, 'DESPESAS PRÉ-OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (44, 1, 42, 1, '1.3.3.002', NULL, 'DESPESAS COM DESENVOLVIMENTO DE SISTEMAS', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (45, 1, 42, 1, '1.2.2.002', NULL, 'DESPESAS COM DESENVOLVIMENTO DE NOVOS PRODUTOS', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (46, 1, 42, 1, '1.3.3.002', NULL, '( - ) AMORTIZAÇÃO ACUMULADA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (47, 1, NULL, 1, '2.1', NULL, 'CIRCULANTE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (48, 1, NULL, 1, '2.1.1', NULL, 'OBRIGAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (49, 1, NULL, 1, '2.1.1.001', NULL, 'FORNECEDORES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (50, 1, NULL, 1, '2.1.1.002', NULL, 'ALUGUÉIS A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (51, 1, NULL, 1, '2.1.1.003', NULL, 'EMPRÉSTIMOS A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (52, 1, NULL, 1, '2.1.1.004', NULL, 'ICMS A RECOLHER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (53, 1, NULL, 1, '2.1.1.005', NULL, 'IMPOSTO DE RENDA A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (54, 1, NULL, 1, '2.1.1.006', NULL, 'IR FONTE A RECOLHER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (55, 1, NULL, 1, '2.1.1.007', NULL, 'CONTRIBUIÇÕES PREVIDENCIÁRIAS  A RECOLHER', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (56, 1, NULL, 1, '2.1.1.008', NULL, 'FGTS A RECOLHER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (57, 1, NULL, 1, '2.1.1.009', NULL, 'HONORÁRIOS DA DIRETORIA A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (58, 1, NULL, 1, '2.1.1.010', NULL, 'SALÁRIOS A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (59, 1, NULL, 1, '2.1.1.011', NULL, 'DIVIDENDOS A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (60, 1, NULL, 1, '2.1.1.012', NULL, 'OUTRAS OBRIGAÇÕES A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (61, 1, NULL, 1, '2.2', NULL, 'EXIGÍVEL A LONGO PRAZO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (62, 1, NULL, 1, '2.2.1', NULL, 'OBRIGAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (63, 1, NULL, 1, '2.2.1.001', NULL, 'FINANCIAMENTOS A PAGAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (64, 1, NULL, 1, '2.3', NULL, 'RESULTADO DE EXERCÍCIOS FUTUROS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (65, 1, NULL, 1, '2.3.1', NULL, 'RESULTADOS FUTUROS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (66, 1, NULL, 1, '2.3.1.001', NULL, 'RECEITAS DE EXERCÍCIOS FUTUROS', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (67, 1, NULL, 1, '2.3.1.002', NULL, '( - ) CUSTOS E DESPESAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (68, 1, NULL, 1, '2.4', NULL, 'PATRIMÔNIO LÍQUIDO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (69, 1, NULL, 1, '2.4.1', NULL, 'CAPITAL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (70, 1, NULL, 1, '2.4.1.001', NULL, 'CAPITAL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (71, 1, NULL, 1, '2.4.1.002', NULL, '( - ) CAPITAL A REALIZAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (72, 1, NULL, 1, '2.4.2', NULL, 'RESERVAS DE CAPITAL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (73, 1, NULL, 1, '2.4.2.001', NULL, 'ÁGIO NA EMISSÃO DE AÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (74, 1, NULL, 1, '2.4.3', NULL, 'RESERVAS DE REAVALIAÇÃO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (75, 1, NULL, 1, '2.4.3.001', NULL, 'REAVALIAÇÃO DO PERMANENTE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (76, 1, NULL, 1, '2.4.4', NULL, 'RESERVAS DE LUCROS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (77, 1, NULL, 1, '2.4.4.001', NULL, 'RESERVA LEGAL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (78, 1, NULL, 1, '2.4.5', NULL, 'LUCROS OU PREJUÍZOS ACUMULADOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (79, 1, NULL, 1, '2.4.5.001', NULL, 'LUCROS ACUMULADOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (80, 1, NULL, 1, '2.4.5.002', NULL, '( - ) PREJUÍZOS ACUMULADOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (81, 1, NULL, 1, '3.3', NULL, 'DESPESAS OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (82, 1, NULL, 1, '3.3.1', NULL, 'DESPESAS COM VENDAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (83, 1, NULL, 1, '3.3.1.001', NULL, 'COMISSÕES SOBRE VENDAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (84, 1, NULL, 1, '3.3.1.006', NULL, 'FRETES E CARRETOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (85, 1, NULL, 1, '3.3.1.007', NULL, 'MATERIAL DE EMBALAGEM', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (86, 1, NULL, 1, '3.3.1.008', NULL, 'PROPAGANDA E PUBLICIDADE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (87, 1, NULL, 1, '3.3.1.009', NULL, 'DESPESAS C/ DEVEDORES  DUVIDOSOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (88, 1, NULL, 1, '3.3.2', NULL, 'DESPESAS ADMINISTRATIVAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (89, 1, NULL, 1, '3.3.2.001', NULL, 'ALUGUEL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (90, 1, NULL, 1, '3.3.2.002', NULL, 'ENERGIA ELÉTRICA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (91, 1, NULL, 1, '3.3.2.003', NULL, 'ÁGUA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (92, 1, NULL, 1, '3.3.2.004', NULL, 'CORREIOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (93, 1, NULL, 1, '3.3.2.005', NULL, 'DEPRECIAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (94, 1, NULL, 1, '3.3.2.006', NULL, 'AMORTIZAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (95, 1, NULL, 1, '3.3.2.007', NULL, 'FRETES E CARRETOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (96, 1, NULL, 1, '3.3.2.008', NULL, 'MATERIAL DE EXPEDIENTE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (97, 1, NULL, 1, '3.3.2.009', NULL, 'PRÊMIOS DE SEGURO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (98, 1, NULL, 1, '3.3.2.010', NULL, 'COMUNICAÇÕES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (99, 1, NULL, 1, '3.3.2.011', NULL, 'IMPOSTOS E TAXAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (100, 1, NULL, 1, '3.3.2.012', NULL, 'SERVIÇOS DE TERCEIROS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (101, 1, NULL, 1, '3.3.2.013', NULL, 'MULTAS FISCAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (102, 1, NULL, 1, '3.3.2.014', NULL, 'SALÁRIOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (103, 1, NULL, 1, '3.3.2.015', NULL, 'HONORÁRIOS DA DIRETORIA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (104, 1, NULL, 1, '3.3.2.016', NULL, 'DÉCIMO TERCEIRO SALÁRIO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (105, 1, NULL, 1, '3.3.2.017', NULL, 'ENCARGOS SOCIAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (106, 1, NULL, 1, '3.3.2.018', NULL, 'FÉRIAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (107, 1, NULL, 1, '3.3.3', NULL, 'DESPESAS FINANCEIRAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (108, 1, NULL, 1, '3.3.3.001', NULL, 'DESPESAS BANCÁRIAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (109, 1, NULL, 1, '3.3.3.002', NULL, 'JUROS PASSIVOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (110, 1, NULL, 1, '3.3.3.003', NULL, 'DESCONTOS CONCEDIDOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (111, 1, NULL, 1, '3.3.4', NULL, 'OUTRAS DESPESAS OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (112, 1, NULL, 1, '3.3.4.001', NULL, 'PREJUÍZO DE PARTICIPAÇÃO EM OUTRAS CIAS', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (113, 1, NULL, 1, '3.3.4.002', NULL, 'DESPESAS EVENTUAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (114, 1, NULL, 1, '3.4', NULL, 'DESPESAS NÃO OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (115, 1, NULL, 1, '3.4.1', NULL, 'PERDAS  NÃO OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (116, 1, NULL, 1, '3.4.1.001', NULL, 'PERDAS NA ALIENAÇÃO DE BENS', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (117, 1, NULL, 1, '4.1', NULL, 'RECEITAS OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (118, 1, NULL, 1, '4.1.1', NULL, 'RECEITAS DE VENDAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (119, 1, NULL, 1, '4.1.1.001', NULL, 'VENDA DE MERCADORIAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (120, 1, NULL, 1, '4.1.1.002', NULL, '( - ) VENDAS ANULADAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (121, 1, NULL, 1, '4.1.1.003', NULL, '( - ) ICMS SOBRE VENDAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (122, 1, NULL, 1, '4.1.1.004', NULL, '( - ) PIS SOBRE FATURAMENTO', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (123, 1, NULL, 1, '4.1.2', NULL, 'RECEITAS FINANCEIRAS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (124, 1, NULL, 1, '4.1.2.001', NULL, 'RENDIMENTOS DE APLICAÇÕES FINANCEIRAS', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (125, 1, NULL, 1, '4.1.2.002', NULL, 'DESCONTOS OBTIDOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (126, 1, NULL, 1, '4.1.2.003', NULL, 'JUROS ATIVOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (127, 1, NULL, 1, '4.1.3', NULL, 'OUTRAS RECEITAS OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (128, 1, NULL, 1, '4.1.3.001', NULL, 'LUCROS DE PARTICIPAÇÕES EM OUTRAS CIAS', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (129, 1, NULL, 1, '4.1.3.002', NULL, 'REVERSÃO DE PROVISÃO PARA DEVEDORES DUVIDOSOS', NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (130, 1, NULL, 1, '4.1.3.003', NULL, 'RECEITAS EVENTUAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (131, 1, NULL, 1, '4.2', NULL, 'RECEITAS NÃO OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (132, 1, NULL, 1, '4.2.1', NULL, 'GANHOS NÃO OPERACIONAIS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (133, 1, NULL, 1, '4.2.1.001', NULL, 'GANHO NA ALIENAÇÃO DE BENS', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (134, 1, NULL, 1, '5', NULL, 'CONTAS DE APURAÇÃO DE RESULTADOS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (135, 1, NULL, 1, '5.1.1', NULL, 'APURAÇÃO DE RESULTADO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (136, 1, NULL, 1, '5.1.1.001', NULL, 'CUSTO DAS MERCADORIAS VENDIDAS (CMV)', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (137, 1, NULL, 1, '5.1.1.002', NULL, 'RESULTADO COM VENDAS DE MERCADORIAS (RVM)', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contabil_conta (id, id_plano_conta, id_contabil_conta, id_plano_conta_ref_sped, classificacao, tipo,
                            descricao, data_inclusao, situacao, natureza, patrimonio_resultado, livro_caixa, dfc, ordem,
                            codigo_reduzido, codigo_efd)
VALUES (138, 1, NULL, 1, '5.1.1.003', NULL, 'APURAÇÃO DO RESULTADO DO EXERCÍCIO (ARE) ', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);