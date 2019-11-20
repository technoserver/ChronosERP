create table nfe_det_especifico_grade
(
    id               SERIAL  NOT NULL,
    id_nfe_detalhe   INTEGER NOT NULL,
    id_estoque_grade INTEGER NOT NULL,
    quantidade       NUMERIC(18, 6) default 0,
    PRIMARY KEY (id),
    FOREIGN KEY (id_nfe_detalhe) REFERENCES nfe_detalhe (id),
    FOREIGN KEY (id_estoque_grade) REFERENCES estoque_grade (id)
);

-- ------------------------------------------------------------
-- O cadastro do tipos de aquisição é utilizado para vincular a formas de aquisição de um patrimônio pela empresa.
--
-- Valores padrões cadastrados para todas as empresas:
-- 01=Compra | 02=Permuta | 03=Doação | 04=Locação | 05=Comodato | 06=Leasing | 07=Transferência
-- ------------------------------------------------------------

CREATE TABLE PATRIM_TIPO_AQUISICAO_BEM
(
    ID         SERIAL  NOT NULL,
    ID_EMPRESA INTEGER NOT NULL,
    TIPO       CHAR(2),
    NOME       VARCHAR(50),
    DESCRICAO  TEXT,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID)
);

-- ------------------------------------------------------------
-- Armazena os tipos de estado de conservação do bem.
--
-- Valores padrões cadastrados para todas as empresas:
-- 01=Novo | 02=Bom | 03=Recuperável | 04=Inservível
-- ------------------------------------------------------------

CREATE TABLE PATRIM_ESTADO_CONSERVACAO
(
    ID         SERIAL  NOT NULL,
    ID_EMPRESA INTEGER NOT NULL,
    CODIGO     CHAR(2),
    NOME       VARCHAR(50),
    DESCRICAO  TEXT,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID)
);

-- ------------------------------------------------------------
-- Armazena os dados do grupo do Bem. Não confundir com o grupo do produto que é comercializado pela empresa.
-- ------------------------------------------------------------

CREATE TABLE PATRIM_GRUPO_BEM
(
    ID                          SERIAL  NOT NULL,
    ID_EMPRESA                  INTEGER NOT NULL,
    CODIGO                      CHAR(3),
    NOME                        VARCHAR(50),
    DESCRICAO                   TEXT,
    CONTA_ATIVO_IMOBILIZADO     VARCHAR(30),
    CONTA_DEPRECIACAO_ACUMULADA VARCHAR(30),
    CONTA_DESPESA_DEPRECIACAO   VARCHAR(30),
    CODIGO_HISTORICO            INTEGER,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID)
);

-- ------------------------------------------------------------
-- Cadastro dos bens móveis da empresa.
-- O vinculo com CENTRO_RESULTADO tem por objetivo gerar informações para os registros 0305 e 0600 do Sped Fiscal.
-- ------------------------------------------------------------

CREATE TABLE PATRIM_BEM
(
    ID                           SERIAL  NOT NULL,
    ID_CENTRO_RESULTADO          INTEGER NOT NULL,
    ID_PATRIM_TIPO_AQUISICAO_BEM INTEGER NOT NULL,
    ID_PATRIM_ESTADO_CONSERVACAO INTEGER NOT NULL,
    ID_PATRIM_GRUPO_BEM          INTEGER NOT NULL,
    ID_SETOR                     INTEGER NOT NULL,
    ID_FORNECEDOR                INTEGER NOT NULL,
    ID_COLABORADOR               INTEGER NOT NULL,
    NUMERO_NB                    VARCHAR(20),
    NOME                         VARCHAR(100),
    DESCRICAO                    TEXT,
    NUMERO_SERIE                 VARCHAR(50),
    DATA_AQUISICAO               DATE,
    DATA_ACEITE                  DATE,
    DATA_CADASTRO                DATE,
    DATA_CONTABILIZADO           DATE,
    DATA_VISTORIA                DATE,
    DATA_MARCACAO                DATE,
    DATA_BAIXA                   DATE,
    VENCIMENTO_GARANTIA          DATE,
    NUMERO_NOTA_FISCAL           VARCHAR(50),
    CHAVE_NFE                    VARCHAR(44),
    VALOR_ORIGINAL               DECIMAL(18, 6),
    VALOR_COMPRA                 DECIMAL(18, 6),
    VALOR_ATUALIZADO             DECIMAL(18, 6),
    VALOR_BAIXA                  DECIMAL(18, 6),
    DEPRECIA                     CHAR(1),
    METODO_DEPRECIACAO           CHAR(1),
    INICIO_DEPRECIACAO           DATE,
    ULTIMA_DEPRECIACAO           DATE,
    TIPO_DEPRECIACAO             CHAR(1),
    TAXA_ANUAL_DEPRECIACAO       DECIMAL(18, 6),
    TAXA_MENSAL_DEPRECIACAO      DECIMAL(18, 6),
    TAXA_DEPRECIACAO_ACELERADA   DECIMAL(18, 6),
    TAXA_DEPRECIACAO_INCENTIVADA DECIMAL(18, 6),
    FUNCAO                       TEXT,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_PATRIM_GRUPO_BEM) REFERENCES PATRIM_GRUPO_BEM (ID),
    FOREIGN KEY (ID_PATRIM_TIPO_AQUISICAO_BEM) REFERENCES PATRIM_TIPO_AQUISICAO_BEM (ID),
    FOREIGN KEY (ID_PATRIM_ESTADO_CONSERVACAO) REFERENCES PATRIM_ESTADO_CONSERVACAO (ID),
    FOREIGN KEY (ID_COLABORADOR) REFERENCES COLABORADOR (ID),
    FOREIGN KEY (ID_FORNECEDOR) REFERENCES FORNECEDOR (ID),
    FOREIGN KEY (ID_SETOR) REFERENCES SETOR (ID),
    FOREIGN KEY (ID_CENTRO_RESULTADO) REFERENCES CENTRO_RESULTADO (ID)
);

CREATE TABLE PCP_INSTRUCAO
(
    ID        SERIAL NOT NULL,
    CODIGO    CHAR(3),
    DESCRICAO VARCHAR(100),
    PRIMARY KEY (ID)
);

CREATE TABLE PCP_OP_CABECALHO
(
    ID                    SERIAL  NOT NULL,
    ID_EMPRESA            INTEGER NOT NULL,
    INICIO                DATE,
    PREVISAO_ENTREGA      DATE,
    TERMINO               DATE,
    CUSTO_TOTAL_PREVISTO  DECIMAL(18, 6),
    CUSTO_TOTAL_REALIZADO DECIMAL(18, 6),
    PORCENTO_VENDA        DECIMAL(18, 6),
    PORCENTO_ESTOQUE      DECIMAL(18, 6),
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID)
);

CREATE TABLE PCP_INSTRUCAO_OP
(
    ID                  SERIAL  NOT NULL,
    ID_PCP_INSTRUCAO    INTEGER NOT NULL,
    ID_PCP_OP_CABECALHO INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_PCP_OP_CABECALHO) REFERENCES PCP_OP_CABECALHO (ID),
    FOREIGN KEY (ID_PCP_INSTRUCAO) REFERENCES PCP_INSTRUCAO (ID)
);

CREATE TABLE PCP_OP_DETALHE
(
    ID                   SERIAL  NOT NULL,
    ID_PRODUTO           INTEGER NOT NULL,
    ID_ESTOQUE_GRADE     INTEGER NOT NULL,
    ID_PCP_OP_CABECALHO  INTEGER NOT NULL,
    QUANTIDADE_PRODUZIR  DECIMAL(18, 6),
    QUANTIDADE_PRODUZIDA DECIMAL(18, 6),
    QUANTIDADE_ENTREGUE  DECIMAL(18, 6),
    CUSTO_PREVISTO       DECIMAL(18, 6),
    CUSTO_REALIZADO      DECIMAL(18, 6),
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_PCP_OP_CABECALHO) REFERENCES PCP_OP_CABECALHO (ID),
    FOREIGN KEY (ID_PRODUTO) REFERENCES PRODUTO (ID),
    FOREIGN KEY (ID_ESTOQUE_GRADE) REFERENCES ESTOQUE_GRADE (ID)
);

CREATE TABLE PCP_SERVICO
(
    ID                 SERIAL  NOT NULL,
    ID_PCP_OP_DETALHE  INTEGER NOT NULL,
    INICIO_REALIZADO   DATE,
    TERMINO_REALIZADO  DATE,
    HORAS_REALIZADO    INTEGER,
    MINUTOS_REALIZADO  INTEGER,
    SEGUNDOS_REALIZADO INTEGER,
    CUSTO_REALIZADO    DECIMAL(18, 6),
    INICIO_PREVISTO    DATE,
    TERMINO_PREVISTO   DATE,
    HORAS_PREVISTO     INTEGER,
    MINUTOS_PREVISTO   INTEGER,
    SEGUNDOS_PREVISTO  INTEGER,
    CUSTO_PREVISTO     DECIMAL(18, 6),
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_PCP_OP_DETALHE) REFERENCES PCP_OP_DETALHE (ID)
);

CREATE TABLE PCP_SERVICO_EQUIPAMENTO
(
    ID             SERIAL  NOT NULL,
    ID_PATRIM_BEM  INTEGER NOT NULL,
    ID_PCP_SERVICO INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_PCP_SERVICO) REFERENCES PCP_SERVICO (ID),
    FOREIGN KEY (ID_PATRIM_BEM) REFERENCES PATRIM_BEM (ID)
);

CREATE TABLE PCP_SERVICO_COLABORADOR
(
    ID             SERIAL  NOT NULL,
    ID_PCP_SERVICO INTEGER NOT NULL,
    ID_COLABORADOR INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_COLABORADOR) REFERENCES COLABORADOR (ID),
    FOREIGN KEY (ID_PCP_SERVICO) REFERENCES PCP_SERVICO (ID)
);

