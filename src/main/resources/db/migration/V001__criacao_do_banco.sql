-- ------------------------------------------------------------
-- 4.3.14 - Tabela Operações com Isenção da Contribuição Social (CST 07) 
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_4314 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT    ,
  INICIO_VIGENCIA DATE    ,
  FIM_VIGENCIA DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela 4.3.13 Produtos Sujeitos à Alíquota Zero da Contribuição Social (CST 06)
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_4313 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT    ,
  INICIO_VIGENCIA DATE    ,
  FIM_VIGENCIA DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela 4.3.10 – Produtos Sujeitos a Incidência Monofásica da Contribuição Social – Alíquotas Diferenciadas (CST 02 e 04)
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_4310 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT    ,
  INICIO_VIGENCIA DATE    ,
  FIM_VIGENCIA DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- 4.3.16 – Tabela Operações com Suspensão da Contribuição Social (CST 09)
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_4316 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT    ,
  INICIO_VIGENCIA DATE    ,
  FIM_VIGENCIA DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- 4.3.15 - Tabela Operações sem Incidência da Contribuição Social (CST 08)
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_4315 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT    ,
  INICIO_VIGENCIA DATE    ,
  FIM_VIGENCIA DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Cadastro das regiões. Pode ser utilizado para definir a região de atuação de vendedores, por exemplo.
-- ------------------------------------------------------------

CREATE TABLE REGIAO (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os tipos de pagamentos de determinada venda.
-- ------------------------------------------------------------

CREATE TABLE ECF_TOTAL_TIPO_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_VENDA_CABECALHO INTEGER    ,
  ID_ECF_TIPO_PAGAMENTO INTEGER    ,
  SERIE_ECF VARCHAR(20)    ,
  COO INTEGER    ,
  CCF INTEGER    ,
  GNF INTEGER    ,
  VALOR DECIMAL(18,6)    ,
  NSU VARCHAR(30)    ,
  ESTORNO CHAR(1)    ,
  REDE VARCHAR(10)    ,
  CARTAO_DC CHAR(1)    ,
  DATA_VENDA DATE    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));






-- ------------------------------------------------------------
-- Armazena os possíveis tipos de pagamento:
-- 
-- Dinheiro
-- Cartão
-- Cheque
-- Etc.
-- ------------------------------------------------------------

CREATE TABLE ECF_TIPO_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  DESCRICAO VARCHAR(20)    ,
  TEF CHAR(1)    ,
  IMPRIME_VINCULADO CHAR(1)    ,
  PERMITE_TROCO CHAR(1)    ,
  TEF_TIPO_GP CHAR(1)    ,
  GERA_PARCELAS CHAR(1)      ,
PRIMARY KEY(ID));








-- ------------------------------------------------------------
-- Armazena todos os suprimentos que são feitos no caixa. Tem relacionamento direto com a tabela de movimento, já que um suprimento só pode ser feito se houver um movimento aberto.
-- ------------------------------------------------------------

CREATE TABLE ECF_SUPRIMENTO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_MOVIMENTO INTEGER    ,
  DATA_SUPRIMENTO DATE    ,
  VALOR DECIMAL(18,6)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena o detallhe das vendas.
-- ------------------------------------------------------------

CREATE TABLE ECF_VENDA_DETALHE (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_PRODUTO INTEGER    ,
  ID_ECF_VENDA_CABECALHO INTEGER    ,
  CFOP INTEGER    ,
  GTIN VARCHAR(14)    ,
  CCF INTEGER    ,
  COO INTEGER    ,
  SERIE_ECF VARCHAR(20)    ,
  ITEM INTEGER    ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  TOTAL_ITEM DECIMAL(18,6)    ,
  BASE_ICMS DECIMAL(18,6)    ,
  TAXA_ICMS DECIMAL(18,6)    ,
  ICMS DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  DESCONTO DECIMAL(18,6)    ,
  TAXA_ISSQN DECIMAL(18,6)    ,
  ISSQN DECIMAL(18,6)    ,
  TAXA_PIS DECIMAL(18,6)    ,
  PIS DECIMAL(18,6)    ,
  TAXA_COFINS DECIMAL(18,6)    ,
  COFINS DECIMAL(18,6)    ,
  TAXA_ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO_RATEIO DECIMAL(18,6)    ,
  DESCONTO_RATEIO DECIMAL(18,6)    ,
  TOTALIZADOR_PARCIAL VARCHAR(10)    ,
  CST CHAR(3)    ,
  CANCELADO CHAR(1)    ,
  MOVIMENTA_ESTOQUE CHAR(1)    ,
  ECF_ICMS_ST VARCHAR(4)    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Armazena o cabeçalho das vendas.
-- ------------------------------------------------------------

CREATE TABLE ECF_VENDA_CABECALHO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_CLIENTE INTEGER    ,
  ID_ECF_FUNCIONARIO INTEGER    ,
  ID_ECF_MOVIMENTO INTEGER    ,
  ID_ECF_DAV INTEGER    ,
  ID_ECF_PRE_VENDA_CABECALHO INTEGER    ,
  SERIE_ECF VARCHAR(20)    ,
  CFOP INTEGER    ,
  COO INTEGER    ,
  CCF INTEGER    ,
  DATA_VENDA DATE    ,
  HORA_VENDA VARCHAR(8)    ,
  VALOR_VENDA DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  DESCONTO DECIMAL(18,6)    ,
  TAXA_ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO DECIMAL(18,6)    ,
  VALOR_FINAL DECIMAL(18,6)    ,
  VALOR_RECEBIDO DECIMAL(18,6)    ,
  TROCO DECIMAL(18,6)    ,
  VALOR_CANCELADO DECIMAL(18,6)    ,
  TOTAL_PRODUTOS DECIMAL(18,6)    ,
  TOTAL_DOCUMENTO DECIMAL(18,6)    ,
  BASE_ICMS DECIMAL(18,6)    ,
  ICMS DECIMAL(18,6)    ,
  ICMS_OUTRAS DECIMAL(18,6)    ,
  ISSQN DECIMAL(18,6)    ,
  PIS DECIMAL(18,6)    ,
  COFINS DECIMAL(18,6)    ,
  ACRESCIMO_ITENS DECIMAL(18,6)    ,
  DESCONTO_ITENS DECIMAL(18,6)    ,
  STATUS_VENDA CHAR(1)    ,
  NOME_CLIENTE VARCHAR(100)    ,
  CPF_CNPJ_CLIENTE VARCHAR(14)    ,
  CUPOM_CANCELADO CHAR(1)    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Armazena os turnos que podem ser utilizados em determinado movimento. Exemplos:
-- 
-- Manha
-- Tarde
-- Noite
-- ------------------------------------------------------------

CREATE TABLE ECF_TURNO (
  ID SERIAL  NOT NULL ,
  DESCRICAO VARCHAR(10)    ,
  HORA_INICIO VARCHAR(8)    ,
  HORA_FIM VARCHAR(8)      ,
PRIMARY KEY(ID));



CREATE TABLE LOG_IMPORTACAO (
  ID SERIAL  NOT NULL ,
  DATA_IMPORTACAO DATE    ,
  HORA_IMPORTACAO VARCHAR(8)    ,
  ERRO TEXT    ,
  REGISTRO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Controla a marca dos produtos.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_MARCA (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(4)    ,
  NOME VARCHAR(20)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Controla a cor dos produtos.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_COR (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(4)    ,
  NOME VARCHAR(20)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Controla o tamanho dos produtos.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_TAMANHO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(4)    ,
  NOME VARCHAR(20)    ,
  ALTURA DECIMAL(18,6)    ,
  COMPRIMENTO DECIMAL(18,6)    ,
  LARGURA DECIMAL(18,6)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Controla o sabor dos produtos.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_SABOR (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(4)    ,
  NOME VARCHAR(20)      ,
PRIMARY KEY(ID));



CREATE TABLE LOG_EXPORTACAO (
  ID SERIAL  NOT NULL ,
  DATA_EXPORTACAO DATE    ,
  HORA_IMPORTACAO VARCHAR(8)    ,
  ERRO TEXT    ,
  REGISTRO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela 4.3.9 – Tabela de Alíquotas de Créditos Presumidos da Agroindústria
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_439 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT    ,
  INICIO_VIGENCIA DATE    ,
  FIM_VIGENCIA DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- EFD Contribuições - Tabela 4.3.7 - Tabela Código de Base de Cálculo do Crédito
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_437 (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(250)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- 4.3.6 – Tabela Código de Tipo de Crédito
-- ------------------------------------------------------------

CREATE TABLE EFD_TABELA_436 (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  DESCRICAO VARCHAR(250)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os possíveis estados civis. De acordo com as leis brasileiras, os possíveis estados civis são:
-- 
--     * Solteiro(a) - quem nunca se casou, ou que teve o casamento anulado
--     * Casado(a) - quem contraiu matrimônio, independente do regime de bens adotado
--     * Separado(a) judicialmente - quem não vive mais com o cônjuge (vive em separação física dele), mas que ainda não obteve o divórcio, todavia obteve sentença que deliberou por decretar a separação judicial dos cônjuges, cessando, assim, os deveres oriundos da sociedade conjugal.
--     * Divorciado(a) - após a homologação do divórcio pela justiça
--     * Viúvo(a) - pessoa cujo cônjuge faleceu.
-- ------------------------------------------------------------

CREATE TABLE ESTADO_CIVIL (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela com a Nomeclatura Comum do Mercosul (NCM)
-- ------------------------------------------------------------

CREATE TABLE NCM (
  ID SERIAL  NOT NULL ,
  CODIGO VARCHAR(8)    ,
  DESCRICAO TEXT    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Armazena os dados dos terminais de caixa.
-- ------------------------------------------------------------

CREATE TABLE NFCE_CAIXA (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(30)    ,
  DATA_CADASTRO DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os movimentos para determinado caixa. Podem haver vários movimentos durante um dia. Um movimento deve ter obrigatoriamente:
-- 
-- -Operador
-- -Caixa (terminal)
-- -Impressora
-- -Turno
-- -Status
-- 
-- É através dessa tabela que o caixa deve funcionar. Sem um movimento aberto não pode haver movimentação no caixa.
-- ------------------------------------------------------------

CREATE TABLE ECF_MOVIMENTO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_ECF_EMPRESA INTEGER    ,
  ID_ECF_TURNO INTEGER    ,
  ID_ECF_IMPRESSORA INTEGER    ,
  ID_ECF_OPERADOR INTEGER    ,
  ID_ECF_CAIXA INTEGER    ,
  ID_GERENTE_SUPERVISOR INTEGER   NOT NULL ,
  DATA_ABERTURA DATE    ,
  HORA_ABERTURA VARCHAR(8)    ,
  DATA_FECHAMENTO DATE    ,
  HORA_FECHAMENTO VARCHAR(8)    ,
  TOTAL_SUPRIMENTO DECIMAL(18,6)    ,
  TOTAL_SANGRIA DECIMAL(18,6)    ,
  TOTAL_NAO_FISCAL DECIMAL(18,6)    ,
  TOTAL_VENDA DECIMAL(18,6)    ,
  TOTAL_DESCONTO DECIMAL(18,6)    ,
  TOTAL_ACRESCIMO DECIMAL(18,6)    ,
  TOTAL_FINAL DECIMAL(18,6)    ,
  TOTAL_RECEBIDO DECIMAL(18,6)    ,
  TOTAL_TROCO DECIMAL(18,6)    ,
  TOTAL_CANCELADO DECIMAL(18,6)    ,
  STATUS_MOVIMENTO CHAR(1)   NOT NULL ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Armazena os dados de cabeçalho da Nota Fiscal Modelo 2.
-- ------------------------------------------------------------

CREATE TABLE ECF_NOTA_FISCAL_CABECALHO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_FUNCIONARIO INTEGER    ,
  ID_CLIENTE INTEGER    ,
  CPF_CNPJ_CLIENTE VARCHAR(14)    ,
  CFOP INTEGER    ,
  NUMERO VARCHAR(6)    ,
  DATA_EMISSAO DATE    ,
  HORA_EMISSAO VARCHAR(8)    ,
  SERIE CHAR(2)    ,
  SUBSERIE CHAR(2)    ,
  TOTAL_PRODUTOS DECIMAL(18,6)    ,
  TOTAL_NF DECIMAL(18,6)    ,
  BASE_ICMS DECIMAL(18,6)    ,
  ICMS DECIMAL(18,6)    ,
  ICMS_OUTRAS DECIMAL(18,6)    ,
  ISSQN DECIMAL(18,6)    ,
  PIS DECIMAL(18,6)    ,
  COFINS DECIMAL(18,6)    ,
  IPI DECIMAL(18,6)    ,
  TAXA_ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO_ITENS DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  DESCONTO DECIMAL(18,6)    ,
  DESCONTO_ITENS DECIMAL(18,6)    ,
  CANCELADA CHAR(1)    ,
  TIPO_NOTA CHAR(1)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Armazena os dados de detalhe da Nota Fiscal Modelo 2.
-- ------------------------------------------------------------

CREATE TABLE ECF_NOTA_FISCAL_DETALHE (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_NF_CABECALHO INTEGER    ,
  ID_PRODUTO INTEGER    ,
  CFOP INTEGER   NOT NULL ,
  ITEM INTEGER    ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  BASE_ICMS DECIMAL(18,6)    ,
  TAXA_ICMS DECIMAL(18,6)    ,
  ICMS DECIMAL(18,6)    ,
  ICMS_OUTRAS DECIMAL(18,6)    ,
  ICMS_ISENTO DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  DESCONTO DECIMAL(18,6)    ,
  TAXA_ISSQN DECIMAL(18,6)    ,
  ISSQN DECIMAL(18,6)    ,
  TAXA_PIS DECIMAL(18,6)    ,
  PIS DECIMAL(18,6)    ,
  TAXA_COFINS DECIMAL(18,6)    ,
  COFINS DECIMAL(18,6)    ,
  TAXA_ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO DECIMAL(18,6)    ,
  TAXA_IPI DECIMAL(18,6)    ,
  IPI DECIMAL(18,6)    ,
  CANCELADO CHAR(1)    ,
  CST CHAR(3)    ,
  MOVIMENTA_ESTOQUE CHAR(1)    ,
  ECF_ICMS_ST VARCHAR(4)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Armazena os dados necessários para a geração do Registro E3 do PAF. REGISTRO TIPO E3 - IDENTIFICAÇÃO DO ECF QUE EMITIU O DOCUMENTO BASE PARA A ATUALIZAÇÃO DO ESTOQUE
-- ------------------------------------------------------------

CREATE TABLE ECF_E3 (
  ID SERIAL  NOT NULL ,
  SERIE_ECF VARCHAR(20)    ,
  MF_ADICIONAL CHAR(1)    ,
  TIPO_ECF VARCHAR(7)    ,
  MARCA_ECF VARCHAR(20)    ,
  MODELO_ECF VARCHAR(20)    ,
  DATA_ESTOQUE DATE    ,
  HORA_ESTOQUE VARCHAR(8)      ,
PRIMARY KEY(ID));










CREATE TABLE ECF_FECHAMENTO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_MOVIMENTO INTEGER    ,
  TIPO_PAGAMENTO VARCHAR(20)    ,
  VALOR DECIMAL(18,6)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os dados das impressoras (ECF).
-- ------------------------------------------------------------

CREATE TABLE ECF_IMPRESSORA (
  ID SERIAL  NOT NULL ,
  NUMERO INTEGER    ,
  CODIGO VARCHAR(10)    ,
  SERIE VARCHAR(25)    ,
  IDENTIFICACAO VARCHAR(250)    ,
  MC CHAR(2)    ,
  MD CHAR(2)    ,
  VR CHAR(2)    ,
  TIPO VARCHAR(7)    ,
  MARCA VARCHAR(30)    ,
  MODELO VARCHAR(30)    ,
  MODELO_ACBR VARCHAR(30)    ,
  MODELO_DOCUMENTO_FISCAL CHAR(2)    ,
  VERSAO VARCHAR(30)    ,
  LE CHAR(1)    ,
  LEF CHAR(1)    ,
  MFD CHAR(1)    ,
  LACRE_NA_MFD CHAR(1)    ,
  DATA_INSTALACAO_SB DATE    ,
  HORA_INSTALACAO_SB VARCHAR(8)    ,
  DOCTO VARCHAR(60)    ,
  ECF_IMPRESSORA VARCHAR(3)      ,
PRIMARY KEY(ID));











-- ------------------------------------------------------------
-- Demais documentos emitidos pelo ECF;
-- ------------------------------------------------------------

CREATE TABLE ECF_R06 (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_OPERADOR INTEGER   NOT NULL ,
  ID_IMPRESSORA INTEGER   NOT NULL ,
  ID_ECF_CAIXA INTEGER    ,
  SERIE_ECF VARCHAR(20)    ,
  COO INTEGER    ,
  GNF INTEGER    ,
  GRG INTEGER    ,
  CDC INTEGER    ,
  DENOMINACAO CHAR(2)    ,
  DATA_EMISSAO DATE    ,
  HORA_EMISSAO VARCHAR(8)    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Detalhe do Cupom Fiscal e do Documento Não Fiscal - Meio de Pagamento;
-- ------------------------------------------------------------

CREATE TABLE ECF_R07 (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(8)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_R06 INTEGER    ,
  CCF INTEGER    ,
  MEIO_PAGAMENTO VARCHAR(20)    ,
  VALOR_PAGAMENTO DECIMAL(18,6)    ,
  ESTORNO CHAR(1)    ,
  VALOR_ESTORNO DECIMAL(18,6)    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os recebimentos que ocorrem via ECF, mas que não tem natureza fiscal.
-- ------------------------------------------------------------

CREATE TABLE ECF_RECEBIMENTO_NAO_FISCAL (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_MOVIMENTO INTEGER    ,
  DATA_RECEBIMENTO DATE    ,
  DESCRICAO VARCHAR(50)    ,
  VALOR DECIMAL(18,6)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os dados dos componentes para que sejam arrumados na tela de acordo com a resolução selecionada.
-- ------------------------------------------------------------

CREATE TABLE ECF_POSICAO_COMPONENTES (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_RESOLUCAO INTEGER    ,
  NOME VARCHAR(100)    ,
  ALTURA INTEGER    ,
  LARGURA INTEGER    ,
  TOPO INTEGER    ,
  ESQUERDA INTEGER    ,
  TAMANHO_FONTE INTEGER  DEFAULT 0  ,
  TEXTO VARCHAR(250)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Relação de Reduções Z;
-- ------------------------------------------------------------

CREATE TABLE ECF_R02 (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_OPERADOR INTEGER   NOT NULL ,
  ID_IMPRESSORA INTEGER   NOT NULL ,
  ID_ECF_CAIXA INTEGER    ,
  SERIE_ECF VARCHAR(20)    ,
  CRZ INTEGER    ,
  COO INTEGER    ,
  CRO INTEGER    ,
  DATA_MOVIMENTO DATE    ,
  DATA_EMISSAO DATE    ,
  HORA_EMISSAO VARCHAR(8)    ,
  VENDA_BRUTA DECIMAL(18,6)    ,
  GRANDE_TOTAL DECIMAL(18,6)    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Detalhe da Redução Z;
-- ------------------------------------------------------------

CREATE TABLE ECF_R03 (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_R02 INTEGER    ,
  SERIE_ECF VARCHAR(20)    ,
  TOTALIZADOR_PARCIAL VARCHAR(10)    ,
  VALOR_ACUMULADO DECIMAL(18,6)    ,
  CRZ INTEGER    ,
  LOGSS VARCHAR(32)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Representa o registro 60A do sintegra.
-- ------------------------------------------------------------

CREATE TABLE ECF_SINTEGRA_60A (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(8)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_SINTEGRA_60M INTEGER    ,
  SITUACAO_TRIBUTARIA VARCHAR(4)    ,
  VALOR DECIMAL(18,6)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(32)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena todas as sangrias que são feitas no caixa. Tem relacionamento direto com a tabela de movimento, já que uma sangria só pode ser feita se houver um 
-- movimento aberto.
-- ------------------------------------------------------------

CREATE TABLE ECF_SANGRIA (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_MOVIMENTO INTEGER    ,
  DATA_SANGRIA DATE    ,
  VALOR DECIMAL(18,6)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena as resoluções que podem ser trabalhadas pelo terminal de caixa. Pode ser importado da retaguarda ou armazenado localmente através do sistema configurador.
-- ------------------------------------------------------------

CREATE TABLE NFCE_RESOLUCAO (
  ID SERIAL  NOT NULL ,
  RESOLUCAO_TELA VARCHAR(20)    ,
  LARGURA INTEGER    ,
  ALTURA INTEGER    ,
  IMAGEM_TELA VARCHAR(50)    ,
  IMAGEM_MENU VARCHAR(50)    ,
  IMAGEM_SUBMENU VARCHAR(50)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Representa o registro 60M do sintegra.
-- ------------------------------------------------------------

CREATE TABLE ECF_SINTEGRA_60M (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(8)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  DATA_EMISSAO DATE    ,
  NUMERO_SERIE_ECF VARCHAR(20)    ,
  NUMERO_EQUIPAMENTO INTEGER    ,
  MODELO_DOCUMENTO_FISCAL CHAR(2)    ,
  COO_INICIAL INTEGER    ,
  COO_FINAL INTEGER    ,
  CRZ INTEGER    ,
  CRO INTEGER    ,
  VALOR_VENDA_BRUTA DECIMAL(18,6)    ,
  VALOR_GRANDE_TOTAL DECIMAL(18,6)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os turnos que podem ser utilizados em determinado movimento. Exemplos:
-- 
-- Manha
-- Tarde
-- Noite
-- ------------------------------------------------------------

CREATE TABLE NFCE_TURNO (
  ID SERIAL  NOT NULL ,
  DESCRICAO VARCHAR(10)    ,
  HORA_INICIO VARCHAR(8)    ,
  HORA_FIM VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os possíveis tipos de pagamento:
-- 
-- Dinheiro
-- Cartão
-- Cheque
-- Etc.
-- ------------------------------------------------------------

CREATE TABLE NFCE_TIPO_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(20)    ,
  PERMITE_TROCO CHAR(1)    ,
  GERA_PARCELAS CHAR(1)      ,
PRIMARY KEY(ID));






-- ------------------------------------------------------------
-- Armazena os dados dos terminais de caixa.
-- ------------------------------------------------------------

CREATE TABLE ECF_CAIXA (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(30)    ,
  DATA_CADASTRO DATE      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena as configurações do terminal de caixa.
-- ------------------------------------------------------------

CREATE TABLE ECF_CONFIGURACAO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_IMPRESSORA INTEGER    ,
  ID_ECF_RESOLUCAO INTEGER    ,
  ID_ECF_CAIXA INTEGER    ,
  ID_ECF_EMPRESA INTEGER    ,
  MENSAGEM_CUPOM VARCHAR(250)    ,
  PORTA_ECF CHAR(10)    ,
  IP_SERVIDOR VARCHAR(15)    ,
  IP_SITEF VARCHAR(15)    ,
  TIPO_TEF CHAR(2)    ,
  TITULO_TELA_CAIXA VARCHAR(100)    ,
  CAMINHO_IMAGENS_PRODUTOS VARCHAR(250)    ,
  CAMINHO_IMAGENS_MARKETING VARCHAR(250)    ,
  CAMINHO_IMAGENS_LAYOUT VARCHAR(250)    ,
  COR_JANELAS_INTERNAS VARCHAR(20)    ,
  MARKETING_ATIVO CHAR(1)    ,
  CFOP_ECF INTEGER    ,
  CFOP_NF2 INTEGER    ,
  TIMEOUT_ECF INTEGER    ,
  INTERVALO_ECF INTEGER    ,
  DESCRICAO_SUPRIMENTO VARCHAR(20)    ,
  DESCRICAO_SANGRIA VARCHAR(20)    ,
  TEF_TIPO_GP INTEGER    ,
  TEF_TEMPO_ESPERA INTEGER    ,
  TEF_ESPERA_STS INTEGER    ,
  TEF_NUMERO_VIAS INTEGER    ,
  DECIMAIS_QUANTIDADE INTEGER    ,
  DECIMAIS_VALOR INTEGER    ,
  BITS_POR_SEGUNDO INTEGER    ,
  QTDE_MAXIMA_CARTOES INTEGER    ,
  PESQUISA_PARTE CHAR(1)    ,
  CONFIGURACAO_BALANCA VARCHAR(100)    ,
  PARAMETROS_DIVERSOS VARCHAR(250)    ,
  ULTIMA_EXCLUSAO INTEGER    ,
  LAUDO VARCHAR(10)    ,
  INDICE_GERENCIAL VARCHAR(100)    ,
  DATA_ATUALIZACAO_ESTOQUE DATE    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));













-- ------------------------------------------------------------
-- Armazena os documentos que são emitidos durante o movimento.
-- ------------------------------------------------------------

CREATE TABLE ECF_DOCUMENTOS_EMITIDOS (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_ECF_MOVIMENTO INTEGER    ,
  DATA_EMISSAO DATE    ,
  HORA_EMISSAO VARCHAR(8)    ,
  TIPO CHAR(2)    ,
  COO INTEGER    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Armazena as resoluções que podem ser trabalhadas pelo terminal de caixa.
-- ------------------------------------------------------------

CREATE TABLE ECF_RESOLUCAO (
  ID SERIAL  NOT NULL ,
  NOME_CAIXA VARCHAR(30)    ,
  ID_GERADO_CAIXA INTEGER    ,
  ID_EMPRESA INTEGER    ,
  RESOLUCAO_TELA VARCHAR(20)    ,
  LARGURA INTEGER    ,
  ALTURA INTEGER    ,
  IMAGEM_TELA VARCHAR(50)    ,
  IMAGEM_MENU VARCHAR(50)    ,
  IMAGEM_SUBMENU VARCHAR(50)    ,
  HOTTRACK_COLOR VARCHAR(20)    ,
  ITEM_STYLE_FONT_NAME VARCHAR(20)    ,
  ITEM_STYLE_FONT_COLOR VARCHAR(20)    ,
  ITEM_SEL_STYLE_COLOR VARCHAR(20)    ,
  LABEL_TOTAL_GERAL_FONT_COLOR VARCHAR(20)    ,
  ITEM_STYLE_FONT_STYLE VARCHAR(20)    ,
  EDITS_COLOR VARCHAR(20)    ,
  EDITS_FONT_COLOR VARCHAR(20)    ,
  EDITS_DISABLED_COLOR VARCHAR(20)    ,
  EDITS_FONT_NAME VARCHAR(20)    ,
  EDITS_FONT_STYLE VARCHAR(20)    ,
  DATA_SINCRONIZACAO DATE    ,
  HORA_SINCRONIZACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os operadores de caixa. Tem relacionamento com a tabela de funcionários, que vem do banco de dados principal.
-- ------------------------------------------------------------

CREATE TABLE NFCE_OPERADOR (
  ID SERIAL  NOT NULL ,
  LOGIN VARCHAR(20)    ,
  SENHA VARCHAR(20)    ,
  NIVEL_AUTORIZACAO CHAR(1)      ,
PRIMARY KEY(ID));




CREATE TABLE ECF_ALIQUOTAS (
  ID SERIAL  NOT NULL ,
  TOTALIZADOR_PARCIAL VARCHAR(10)    ,
  ECF_ICMS_ST VARCHAR(4)    ,
  PAF_P_ST CHAR(1)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela que armazena os dados da PESSOA. Demais tabelas devem especializar esta tabela com seus próprios dados: FORNECEDOR, TRANSPORTADORA, CLIENTE, etc.
-- ------------------------------------------------------------

CREATE TABLE PESSOA (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(150)   NOT NULL ,
  TIPO CHAR(1)   NOT NULL ,
  EMAIL VARCHAR(250)    ,
  SITE VARCHAR(250)    ,
  CLIENTE CHAR(1)    ,
  FORNECEDOR CHAR(1)    ,
  COLABORADOR CHAR(1)    ,
  TRANSPORTADORA CHAR(1)      ,
PRIMARY KEY(ID));








-- ------------------------------------------------------------
-- Armazena as cotações de compra
-- ------------------------------------------------------------

CREATE TABLE COMPRA_COTACAO (
  ID SERIAL  NOT NULL ,
  DATA_COTACAO DATE    ,
  DESCRICAO VARCHAR(100)    ,
  SITUACAO CHAR(1)      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Armazena as marcas dos produtos.
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_MARCA (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tipos de colaboradores
-- ------------------------------------------------------------

CREATE TABLE TIPO_COLABORADOR (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(20)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena a informação do tipo de item que é gerado para o arquivo do SPED: Registro 200 - Campo 7.
-- Tipos:
-- 
-- 00-Mercadoria para Revenda;
-- 01-Matéria-Prima;
-- 02-Embalagem;
-- 03-Produto em Processo;
-- 04-Produto Acabado;
-- 05-Subproduto;
-- 06-Produto Intermediário;
-- 07-Material de Uso e Consumo;
-- 08-Ativo Imobilizado;
-- 09-Serviços;
-- 10-Outros insumos;
-- 99-Outras
-- ------------------------------------------------------------

CREATE TABLE TIPO_ITEM_SPED (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(50)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Situações possíveis para o colaborador dentro da empresa.
-- ------------------------------------------------------------

CREATE TABLE SITUACAO_COLABORADOR (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Tabela que define os papéis do sistema.
-- ------------------------------------------------------------

CREATE TABLE PAPEL (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(20)    ,
  DESCRICAO TEXT    ,
  ACESSO_COMPLETO CHAR(1)      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Tabela com as funções do sistema.
-- ------------------------------------------------------------

CREATE TABLE FUNCAO (
  ID SERIAL  NOT NULL ,
  DESCRICAO_MENU VARCHAR(30)    ,
  IMAGEM_MENU VARCHAR(30)    ,
  METODO VARCHAR(30)    ,
  NOME VARCHAR(100)    ,
  FORMULARIO VARCHAR(100)      ,
PRIMARY KEY(ID));



CREATE TABLE IBPT (
  ID SERIAL  NOT NULL ,
  NCM VARCHAR(8)    ,
  EX CHAR(2)    ,
  TIPO CHAR(1)    ,
  DESCRICAO TEXT    ,
  NACIONAL_FEDERAL DECIMAL(18,6)    ,
  IMPORTADOS_FEDERAL DECIMAL(18,6)    ,
  ESTADUAL DECIMAL(18,6)    ,
  MUNICIPAL DECIMAL(18,6)    ,
  VIGENCIA_INICIO DATE    ,
  VIGENCIA_FIM DATE    ,
  CHAVE VARCHAR(6)    ,
  VERSAO VARCHAR(6)    ,
  FONTE VARCHAR(34)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Contém os modelos definidos no Ato Cotepe 46/10.
-- ------------------------------------------------------------

CREATE TABLE NOTA_FISCAL_MODELO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(100)    ,
  MODELO VARCHAR(10)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela de países.
-- ------------------------------------------------------------

CREATE TABLE PAIS (
  ID SERIAL  NOT NULL ,
  CODIGO INTEGER    ,
  NOME_EN VARCHAR(100)    ,
  NOME_PTBR VARCHAR(100)    ,
  SIGLA2 CHAR(2)    ,
  SIGLA3 CHAR(3)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela das operadoras de plano de saúde.
-- ------------------------------------------------------------

CREATE TABLE OPERADORA_PLANO_SAUDE (
  ID SERIAL  NOT NULL ,
  REGISTRO_ANS VARCHAR(20)    ,
  NOME VARCHAR(100)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela com as unidades de produto.
-- ------------------------------------------------------------

CREATE TABLE UNIDADE_PRODUTO (
  ID SERIAL  NOT NULL ,
  SIGLA VARCHAR(10)    ,
  DESCRICAO TEXT    ,
  PODE_FRACIONAR CHAR(1)      ,
PRIMARY KEY(ID));





-- ------------------------------------------------------------
-- Tabela com os grupos de produtos
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_GRUPO (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela com a relação de bancos.
-- ------------------------------------------------------------

CREATE TABLE BANCO (
  ID SERIAL  NOT NULL ,
  CODIGO VARCHAR(10)    ,
  NOME VARCHAR(100)    ,
  URL VARCHAR(250)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Cadastro de lacres para cada unidade do produto. Serve para rastreabilidade. Por exemplo, ao comprar 5 unidades de banana, cada banana recebe um número de lacre:
-- 
-- 001
-- 002
-- 003
-- 004
-- 005
-- 
-- Dessa forma sabe-se o que ocorre com cada banana individual:
-- 
-- 001 - Comida pelo João
-- 002 - Comida pelo Pedro
-- 003 - Doada
-- 004 - Estragada
-- 005 - Vencida, foi pro lixo
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_LACRE (
  ID SERIAL  NOT NULL ,
  CODIGO VARCHAR(50)    ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os dados dos módulos do ERP
-- ------------------------------------------------------------

CREATE TABLE ADM_MODULO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT    ,
  ATIVO CHAR(1)      ,
PRIMARY KEY(ID));




CREATE TABLE PRODUTO_COMBO (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)    ,
  VALIDADE_INICIO DATE    ,
  VALIDADE_FIM DATE      ,
PRIMARY KEY(ID));






-- ------------------------------------------------------------
-- Permite agrupar clientes e fornecedores por segmentos e ramos de atividades.
-- ------------------------------------------------------------

CREATE TABLE ATIVIDADE_FOR_CLI (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena as tabelas de preço da aplicação.
-- ------------------------------------------------------------

CREATE TABLE TABELA_PRECO (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)    ,
  COEFICIENTE DECIMAL(18,6)    ,
  PRINCIPAL CHAR(1)    ,
  BASE_CUSTO CHAR(1)    ,
  METODO_UTILIZACAO CHAR(1)    ,
  COMISSAO_VENDEDOR DECIMAL(18,6)      ,
PRIMARY KEY(ID));







CREATE TABLE SITUACAO_DOCUMENTO (
  ID SERIAL  NOT NULL ,
  CODIGO VARCHAR(2)    ,
  DESCRICAO VARCHAR(100)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os tipos de admissão do colaborador.
-- ------------------------------------------------------------

CREATE TABLE TIPO_ADMISSAO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- A CNAE - Classificação Nacional de Atividades Econômicas é o instrumento de padronização nacional dos códigos de atividade econômica e dos critérios de enquadramento utilizados pelos diversos órgãos da Administração Tributária do país.
-- 
-- EX.:
--    Seção:  A  AGRICULTURA, PECUÁRIA, PRODUÇÃO FLORESTAL, PESCA E AQÜICULTURA
--    Divisão:  01  AGRICULTURA, PECUÁRIA E SERVIÇOS RELACIONADOS
--    Grupo:  011  PRODUÇÃO DE LAVOURAS TEMPORÁRIAS
--    Classe:  0111-3  CULTIVO DE CEREAIS
--    Subclasse  0111-3/01  CULTIVO DE ARROZ
-- ------------------------------------------------------------

CREATE TABLE CNAE (
  ID SERIAL  NOT NULL ,
  CODIGO VARCHAR(7)    ,
  DENOMINACAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Identifica a situação (ativo, inativo, bloqueado, desbloqueado...)
-- ------------------------------------------------------------

CREATE TABLE SITUACAO_FOR_CLI (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(20)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));



CREATE TABLE CEST (
  ID SERIAL  NOT NULL ,
  ITEM VARCHAR(10)    ,
  CEST VARCHAR(10)    ,
  NCM_SH VARCHAR(15)    ,
  DESCRICAO_SEGMENTO VARCHAR(100)    ,
  DESCRICAO_CEST TEXT    ,
  SIGLA_UNIDADE CHAR(2)    ,
  DESCRICAO_UNIDADE VARCHAR(20)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- CÓDIGO FISCAL DE OPERAÇÕES E PRESTAÇÕES - CFOP
-- ------------------------------------------------------------

CREATE TABLE CFOP (
  ID SERIAL  NOT NULL ,
  CFOP INTEGER    ,
  DESCRICAO TEXT    ,
  APLICACAO TEXT      ,
PRIMARY KEY(ID));



CREATE TABLE LOGSS (
  ID SERIAL  NOT NULL ,
  DAVC INTEGER    ,
  DAVD INTEGER      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela das sindicatos patronais e de empregados.
-- ------------------------------------------------------------

CREATE TABLE SINDICATO (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)    ,
  CODIGO_BANCO INTEGER    ,
  CODIGO_AGENCIA INTEGER    ,
  CONTA_BANCO VARCHAR(20)    ,
  CODIGO_CEDENTE VARCHAR(30)    ,
  LOGRADOURO VARCHAR(100)    ,
  NUMERO VARCHAR(10)    ,
  BAIRRO VARCHAR(100)    ,
  MUNICIPIO_IBGE INTEGER    ,
  UF CHAR(2)    ,
  FONE1 VARCHAR(14)    ,
  FONE2 VARCHAR(14)    ,
  EMAIL VARCHAR(100)    ,
  TIPO_SINDICATO CHAR(1)    ,
  DATA_BASE DATE    ,
  PISO_SALARIAL DECIMAL(18,6)    ,
  CNPJ VARCHAR(14)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)      ,
PRIMARY KEY(ID));




CREATE TABLE CSOSN_B (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



CREATE TABLE CST_COFINS (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela com os códigos do CST.
-- O Código de Situação Tributária foi instituído com a finalidade de identificar a origem da mercadoria e identificar o regime de tributação a que esta sujeita a mercadoria, na operação praticada. É composto por três dígitos, onde o 1° dígito indicará a origem da mercadoria, com base na Tabela A e os dois últimos dígitos a tributação pelo ICMS, com base na Tabela B.
-- ------------------------------------------------------------

CREATE TABLE CST_ICMS_A (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(1)   NOT NULL ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



CREATE TABLE CST_PIS (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(250)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Criado de acordo com sugestão no EAD do T2Ti ERP Lite:
-- 
-- http://t2ti.com/ead2/mod/forum/discuss.php?d=2125
-- "
-- Irei exemplificar com um caso de uso simplificado:
-- 
-- Uma empresa tem alguns grupos de recebimento mensal, que vencem em períodos distintos e de bancos similares ou distintos. Cada grupo contém N clientes e cada cliente tem um valor fixo (que só seria alterado por algum reajuste ou qualquer outro motivo), então o usuário do sistema apenas faria o "lançamento" destes grupos e geraria seus respectivos boletos e arquivos de remessa em lote.
-- "
-- ------------------------------------------------------------

CREATE TABLE FIN_CLIENTE_GRUPO_CAB (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Tabela com os códigos do CST.
-- O Código de Situação Tributária foi instituído com a finalidade de identificar a origem da mercadoria e identificar o regime de tributação a que esta sujeita a mercadoria, na operação praticada. É composto por três dígitos, onde o 1° dígito indicará a origem da mercadoria, com base na Tabela A e os dois últimos dígitos a tributação pelo ICMS, com base na Tabela B.
-- ------------------------------------------------------------

CREATE TABLE CST_ICMS_B (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)   NOT NULL ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



CREATE TABLE CST_IPI (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os dados do contador da empresa. 
-- ------------------------------------------------------------

CREATE TABLE CONTADOR (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(150)    ,
  CPF VARCHAR(11)    ,
  CNPJ VARCHAR(14)    ,
  INSCRICAO_CRC VARCHAR(15)    ,
  UF_CRC CHAR(2)    ,
  FONE VARCHAR(14)    ,
  FAX VARCHAR(14)    ,
  LOGRADOURO VARCHAR(100)    ,
  NUMERO VARCHAR(10)    ,
  COMPLEMENTO VARCHAR(100)    ,
  BAIRRO VARCHAR(60)    ,
  CIDADE VARCHAR(60)    ,
  CEP VARCHAR(8)    ,
  MUNICIPIO_IBGE INTEGER    ,
  UF CHAR(2)    ,
  EMAIL VARCHAR(250)    ,
  SITE VARCHAR(250)      ,
PRIMARY KEY(ID));



CREATE TABLE TIPO_RECEITA_DIPI (
  ID SERIAL  NOT NULL ,
  DESCRICAO VARCHAR(100)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Contas contábeis da empresa.
-- ------------------------------------------------------------

CREATE TABLE CONTABIL_CONTA (
  ID SERIAL  NOT NULL ,
  CLASSIFICACAO VARCHAR(30)    ,
  TIPO CHAR(1)    ,
  DESCRICAO VARCHAR(100)    ,
  DATA_INCLUSAO DATE    ,
  SITUACAO CHAR(1)    ,
  NATUREZA CHAR(1)    ,
  PATRIMONIO_RESULTADO CHAR(1)    ,
  LIVRO_CAIXA CHAR(1)    ,
  DFC CHAR(1)    ,
  ORDEM VARCHAR(20)    ,
  CODIGO_REDUZIDO VARCHAR(10)    ,
  CODIGO_EFD CHAR(2)      ,
PRIMARY KEY(ID));









-- ------------------------------------------------------------
-- Pedido Normal: Quando nao existe nenhuma programacao de entrega ou seja, a entrega do material é feita de uma unica vez.
-- 
-- Pedido Planejado: O comprador negocia um determinado material mas não quer armazenar o consumo do ano em seu estoque, neste caso o comprador poderia fazer o pedido para o ano todo porém as entregas seriam mensais já que o comprador sabe o consumo mensal. 
-- 
-- Pedido Aberto: O Comprador negocia um determinado material mas não sabe o consumo mensal durante o ano, neste caso ele gera liberacoes conforme necessidade.
-- ------------------------------------------------------------

CREATE TABLE COMPRA_TIPO_PEDIDO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  NOME VARCHAR(30)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));




-- ------------------------------------------------------------
-- Tabela com os níveis de formação. Tabela somente para consulta.
-- ------------------------------------------------------------

CREATE TABLE NIVEL_FORMACAO (
  ID SERIAL  NOT NULL ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT    ,
  GRAU_INSTRUCAO_CAGED INTEGER    ,
  GRAU_INSTRUCAO_SEFIP INTEGER    ,
  GRAU_INSTRUCAO_RAIS INTEGER      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Armazena os feriados.
-- ------------------------------------------------------------

CREATE TABLE FERIADOS (
  ID SERIAL  NOT NULL ,
  ANO CHAR(4)    ,
  NOME VARCHAR(50)    ,
  ABRANGENCIA CHAR(1)    ,
  UF CHAR(2)    ,
  MUNICIPIO_IBGE INTEGER    ,
  TIPO CHAR(1)    ,
  DATA_FERIADO DATE      ,
PRIMARY KEY(ID));







CREATE TABLE INTEGRACAO_PDV (
  ID SERIAL  NOT NULL ,
  IDENTIFICA VARCHAR(50)    ,
  DATA_INTEGRACAO DATE    ,
  HORA_INTEGRACAO VARCHAR(8)      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela com os tipos de relacionamento.
-- ------------------------------------------------------------

CREATE TABLE TIPO_RELACIONAMENTO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID));




CREATE TABLE CSOSN_A (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(1)    ,
  DESCRICAO VARCHAR(250)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID));



-- ------------------------------------------------------------
-- Tabela com os índices econômicos de determinado país.
-- ------------------------------------------------------------

CREATE TABLE INDICE_ECONOMICO (
  ID SERIAL  NOT NULL ,
  ID_PAIS INTEGER   NOT NULL ,
  SIGLA VARCHAR(50)    ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PAIS)
    REFERENCES PAIS(ID));


CREATE INDEX FK_PAIS_INDICE_ECONOMICO ON INDICE_ECONOMICO (ID_PAIS);



-- ------------------------------------------------------------
-- Tabela com os sub-grupos dos produtos.
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_SUBGRUPO (
  ID SERIAL  NOT NULL ,
  ID_GRUPO INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_GRUPO)
    REFERENCES PRODUTO_GRUPO(ID));


CREATE INDEX FK_SUB_GRUPO_GRUPO ON PRODUTO_SUBGRUPO (ID_GRUPO);



CREATE TABLE PESSOA_TELEFONE (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  TIPO INTEGER    ,
  NUMERO VARCHAR(14)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_PESSOA_TELEFONE ON PESSOA_TELEFONE (ID_PESSOA);




-- ------------------------------------------------------------
-- Armazena os dados específicos de pessoa jurídica.
-- ------------------------------------------------------------

CREATE TABLE PESSOA_JURIDICA (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  FANTASIA VARCHAR(150)    ,
  INSCRICAO_MUNICIPAL VARCHAR(30)    ,
  INSCRICAO_ESTADUAL VARCHAR(30)    ,
  DATA_CONSTITUICAO DATE    ,
  TIPO_REGIME CHAR(1)    ,
  CRT CHAR(1)    ,
  SUFRAMA VARCHAR(9)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_PESSOA_JURIDICA ON PESSOA_JURIDICA (ID_PESSOA);





-- ------------------------------------------------------------
-- Tabela de endereços da PESSOA.
-- ------------------------------------------------------------

CREATE TABLE PESSOA_ENDERECO (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(10)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CIDADE VARCHAR(60)    ,
  CEP VARCHAR(8)    ,
  FONE VARCHAR(14)    ,
  MUNICIPIO_IBGE INTEGER    ,
  UF CHAR(2)    ,
  PRINCIPAL CHAR(1)    ,
  ENTREGA CHAR(1)    ,
  COBRANCA CHAR(1)    ,
  CORRESPONDENCIA CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_PESSOA_ENDERECO ON PESSOA_ENDERECO (ID_PESSOA);







-- ------------------------------------------------------------
-- Tabela de contatos da PESSOA.
-- ------------------------------------------------------------

CREATE TABLE PESSOA_CONTATO (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  EMAIL VARCHAR(250)    ,
  FONE_COMERCIAL VARCHAR(14)    ,
  FONE_RESIDENCIAL VARCHAR(14)    ,
  FONE_CELULAR VARCHAR(14)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_PESSOA_CONTATO ON PESSOA_CONTATO (ID_PESSOA);



-- ------------------------------------------------------------
-- Essa tabela tem por objetivo armazenar todas as alterações realizadas em PESSOA, PESSOA_FISICA, PESSOA_JURIDICA, ENDERECO para que seja possível gerar o registro 0175 do Sped Fiscal. Deve-se armazenar os objetos serializados e realizar os devidos testes no momento da geração do arquivo.
-- ------------------------------------------------------------

CREATE TABLE PESSOA_ALTERACAO (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  DATA_ALTERACAO DATE    ,
  OBJETO_ANTIGO TEXT    ,
  OBJETO_NOVO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_PESSOA_ALTERACAO ON PESSOA_ALTERACAO (ID_PESSOA);





-- ------------------------------------------------------------
-- Armazena os dados dos componentes para que sejam arrumados na tela de acordo com a resolução selecionada. Pode ser importado da retaguarda ou armazenado localmente através do sistema configurador.
-- ------------------------------------------------------------

CREATE TABLE NFCE_POSICAO_COMPONENTES (
  ID SERIAL  NOT NULL ,
  ID_NFCE_RESOLUCAO INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  ALTURA INTEGER    ,
  LARGURA INTEGER    ,
  TOPO INTEGER    ,
  ESQUERDA INTEGER    ,
  TAMANHO_FONTE INTEGER  DEFAULT 0  ,
  TEXTO VARCHAR(250)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFCE_RESOLUCAO)
    REFERENCES NFCE_RESOLUCAO(ID));


CREATE INDEX FK_RES_POSICAO_COMPONENTES ON NFCE_POSICAO_COMPONENTES (ID_NFCE_RESOLUCAO);



-- ------------------------------------------------------------
-- Tabela com a relação de CEPs do Brasil.
-- ------------------------------------------------------------

CREATE TABLE CEP (
  ID SERIAL  NOT NULL ,
  ID_REGIAO INTEGER    ,
  CEP VARCHAR(8)    ,
  LOGRADOURO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CODIGO_IBGE_MUNICIPIO INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_REGIAO)
    REFERENCES REGIAO(ID));


CREATE INDEX FK_REGIAO_CEP ON CEP (ID_REGIAO);



-- ------------------------------------------------------------
-- Tabela para armazenar as agências bancárias.
-- ------------------------------------------------------------

CREATE TABLE AGENCIA_BANCO (
  ID SERIAL  NOT NULL ,
  ID_BANCO INTEGER   NOT NULL ,
  CODIGO VARCHAR(10)    ,
  DIGITO CHAR(1)    ,
  NOME VARCHAR(100)    ,
  LOGRADOURO VARCHAR(100)    ,
  NUMERO VARCHAR(10)    ,
  CEP VARCHAR(8)    ,
  BAIRRO VARCHAR(60)    ,
  MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  TELEFONE VARCHAR(14)    ,
  GERENTE VARCHAR(30)    ,
  CONTATO VARCHAR(30)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_BANCO)
    REFERENCES BANCO(ID));


CREATE INDEX FK_BANCO_AGENCIA ON AGENCIA_BANCO (ID_BANCO);



-- ------------------------------------------------------------
-- Tabela com os Estados de determinado Pais.
-- ------------------------------------------------------------

CREATE TABLE UF (
  ID SERIAL  NOT NULL ,
  ID_PAIS INTEGER   NOT NULL ,
  SIGLA CHAR(2)    ,
  NOME VARCHAR(50)    ,
  CODIGO_IBGE INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PAIS)
    REFERENCES PAIS(ID));


CREATE INDEX FK_PAIS_UF ON UF (ID_PAIS);



-- ------------------------------------------------------------
-- Tabela com as contribuições para os sindicados patronais.
-- ------------------------------------------------------------

CREATE TABLE CONTRIB_SIND_PATRONAL_CAB (
  ID SERIAL  NOT NULL ,
  ID_SINDICATO INTEGER   NOT NULL ,
  VIGENCIA_ANO CHAR(4)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_SINDICATO)
    REFERENCES SINDICATO(ID));


CREATE INDEX FK_SIND_PATRO_CONTR ON CONTRIB_SIND_PATRONAL_CAB (ID_SINDICATO);




-- ------------------------------------------------------------
-- Tabela de transportadoras
-- ------------------------------------------------------------

CREATE TABLE TRANSPORTADORA (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  DATA_CADASTRO DATE    ,
  OBSERVACAO TEXT    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_PESSOA_TRANSPORTADORA ON TRANSPORTADORA (ID_PESSOA);



-- ------------------------------------------------------------
-- Tabela com as empresas que utilizam o sistema. ERP multi-empresa.
-- ------------------------------------------------------------

CREATE TABLE EMPRESA (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER    ,
  ID_SINDICATO_PATRONAL INTEGER    ,
  ID_CONTADOR INTEGER    ,
  RAZAO_SOCIAL VARCHAR(150)    ,
  NOME_FANTASIA VARCHAR(150)    ,
  CNPJ VARCHAR(14)    ,
  INSCRICAO_ESTADUAL VARCHAR(30)    ,
  INSCRICAO_ESTADUAL_ST VARCHAR(30)    ,
  INSCRICAO_MUNICIPAL VARCHAR(30)    ,
  INSCRICAO_JUNTA_COMERCIAL VARCHAR(30)    ,
  DATA_INSC_JUNTA_COMERCIAL DATE    ,
  TIPO CHAR(1)    ,
  DATA_CADASTRO DATE    ,
  DATA_INICIO_ATIVIDADES DATE    ,
  SUFRAMA VARCHAR(9)    ,
  EMAIL VARCHAR(250)    ,
  IMAGEM_LOGOTIPO TEXT    ,
  CRT CHAR(1)    ,
  TIPO_REGIME CHAR(1)    ,
  ALIQUOTA_PIS DECIMAL(18,6)    ,
  CONTATO VARCHAR(50)    ,
  ALIQUOTA_COFINS DECIMAL(18,6)    ,
  CODIGO_IBGE_CIDADE INTEGER    ,
  CODIGO_IBGE_UF INTEGER    ,
  CODIGO_TERCEIROS INTEGER    ,
  CODIGO_GPS INTEGER    ,
  ALIQUOTA_SAT DECIMAL(18,6)    ,
  CEI VARCHAR(12)    ,
  CODIGO_CNAE_PRINCIPAL VARCHAR(7)    ,
  TIPO_CONTROLE_ESTOQUE CHAR(1)      ,
PRIMARY KEY(ID)      ,
  FOREIGN KEY(ID_CONTADOR)
    REFERENCES CONTADOR(ID),
  FOREIGN KEY(ID_SINDICATO_PATRONAL)
    REFERENCES SINDICATO(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_CONTADOR_EMPRESA ON EMPRESA (ID_CONTADOR);
CREATE INDEX FK_SINDICATO_EMPRESA ON EMPRESA (ID_SINDICATO_PATRONAL);
CREATE INDEX FK_AUTO_REL_EMPRESA ON EMPRESA (ID_EMPRESA);














-- ------------------------------------------------------------
-- Armazena o cabeçalho do DAV.
-- ------------------------------------------------------------

CREATE TABLE DAV_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_PESSOA INTEGER    ,
  NUMERO_DAV VARCHAR(10)    ,
  NUMERO_ECF VARCHAR(4)    ,
  CCF INTEGER    ,
  COO INTEGER    ,
  NOME_DESTINATARIO VARCHAR(100)    ,
  CPF_CNPJ_DESTINATARIO VARCHAR(14)    ,
  DATA_EMISSAO DATE    ,
  HORA_EMISSAO VARCHAR(8)    ,
  SITUACAO CHAR(1)    ,
  TAXA_ACRESCIMO DECIMAL(18,6)    ,
  ACRESCIMO DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  DESCONTO DECIMAL(18,6)    ,
  SUBTOTAL DECIMAL(18,6)    ,
  VALOR DECIMAL(18,6)    ,
  IMPRESSO CHAR(1)    ,
  LOGSS VARCHAR(32)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_PESSOA_DAV_CAB ON DAV_CABECALHO (ID_PESSOA);
CREATE INDEX FK_EMPRESA_DAV_CAB ON DAV_CABECALHO (ID_EMPRESA);






-- ------------------------------------------------------------
-- Armazenas as contas de banco e de caixa interno da empresa.
-- ------------------------------------------------------------

CREATE TABLE CONTA_CAIXA (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_AGENCIA_BANCO INTEGER    ,
  CODIGO VARCHAR(20)    ,
  DIGITO CHAR(1)    ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT    ,
  TIPO CHAR(1)    ,
  LIMITE_CREDITO DECIMAL(18,6)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)    ,
  TAXA_MULTA DECIMAL(18,6)    ,
  TAXA_JURO DECIMAL(18,6)    ,
  DESCONTO_MAXIMO_PERMITIDO DECIMAL(18,6)    ,
  LIMITE_COBRANCA_JURO INTEGER      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_AGENCIA_BANCO)
    REFERENCES AGENCIA_BANCO(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_AGENCIA_CONTA ON CONTA_CAIXA (ID_AGENCIA_BANCO);
CREATE INDEX FK_EMPRESA_CONTA_CAIXA ON CONTA_CAIXA (ID_EMPRESA);









-- ------------------------------------------------------------
-- Tabela que armazena os tipos de recebimento: DINHEIRO, CARTÃO, CHEQUE, etc. 
-- Tipos padões já cadastrados pelo sistema para toda empresa: 
-- 01 = Dinheiro | 02 = Cheque | 03 = Cartao
-- ------------------------------------------------------------

CREATE TABLE FIN_TIPO_RECEBIMENTO (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  TIPO CHAR(2)    ,
  DESCRICAO VARCHAR(100)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_EMPRESA_TIPO_RCTO ON FIN_TIPO_RECEBIMENTO (ID_EMPRESA);
CREATE INDEX FK_CONTA_CAIXA_TIPO_REC ON FIN_TIPO_RECEBIMENTO (ID_CONTA_CAIXA);





-- ------------------------------------------------------------
-- Armazena o cabeçalho da pre-venda.
-- ------------------------------------------------------------

CREATE TABLE PRE_VENDA_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_PESSOA INTEGER    ,
  DATA_EMISSAO DATE    ,
  HORA_EMISSAO VARCHAR(8)    ,
  SITUACAO CHAR(1)    ,
  CCF INTEGER    ,
  VALOR DECIMAL(18,6)    ,
  NOME_DESTINATARIO VARCHAR(100)    ,
  CPF_CNPJ_DESTINATARIO VARCHAR(14)    ,
  SUBTOTAL DECIMAL(18,6)    ,
  DESCONTO DECIMAL(18,6)    ,
  ACRESCIMO DECIMAL(18,6)    ,
  TAXA_ACRESCIMO DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_PESSOA_PV_CAB ON PRE_VENDA_CABECALHO (ID_PESSOA);
CREATE INDEX FK_EMPRESA_PV_CAB ON PRE_VENDA_CABECALHO (ID_EMPRESA);





-- ------------------------------------------------------------
-- Armazena os dados específicos de pessoa física.
-- ------------------------------------------------------------

CREATE TABLE PESSOA_FISICA (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  ID_ESTADO_CIVIL INTEGER   NOT NULL ,
  CPF VARCHAR(11)    ,
  RG VARCHAR(20)    ,
  ORGAO_RG VARCHAR(20)    ,
  DATA_EMISSAO_RG DATE    ,
  DATA_NASCIMENTO DATE    ,
  SEXO CHAR(1)    ,
  NATURALIDADE VARCHAR(100)    ,
  NACIONALIDADE VARCHAR(100)    ,
  RACA CHAR(1)    ,
  TIPO_SANGUE CHAR(3)    ,
  CNH_NUMERO VARCHAR(20)    ,
  CNH_CATEGORIA CHAR(2)    ,
  CNH_VENCIMENTO DATE    ,
  TITULO_ELEITORAL_NUMERO VARCHAR(20)    ,
  TITULO_ELEITORAL_ZONA INTEGER    ,
  TITULO_ELEITORAL_SECAO INTEGER    ,
  RESERVISTA_NUMERO VARCHAR(20)    ,
  RESERVISTA_CATEGORIA INTEGER    ,
  NOME_MAE VARCHAR(100)    ,
  NOME_PAI VARCHAR(100)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_ESTADO_CIVIL)
    REFERENCES ESTADO_CIVIL(ID),
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_ESTADO_CIVIL_PF ON PESSOA_FISICA (ID_ESTADO_CIVIL);
CREATE INDEX FK_PESSOA_FISICA ON PESSOA_FISICA (ID_PESSOA);







-- ------------------------------------------------------------
-- Vinculo entre PAPEL e FUNCAO pare definir os níveis de acesso do sistema.
-- ------------------------------------------------------------

CREATE TABLE PAPEL_FUNCAO (
  ID SERIAL  NOT NULL ,
  ID_PAPEL INTEGER   NOT NULL ,
  ID_FUNCAO INTEGER   NOT NULL ,
  PODE_CONSULTAR CHAR(1)    ,
  PODE_INSERIR CHAR(1)    ,
  PODE_ALTERAR CHAR(1)    ,
  PODE_EXCLUIR CHAR(1)    ,
  HABILITADO CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PAPEL)
    REFERENCES PAPEL(ID),
  FOREIGN KEY(ID_FUNCAO)
    REFERENCES FUNCAO(ID));


CREATE INDEX FK_PAPEL_FUNCAO ON PAPEL_FUNCAO (ID_PAPEL);
CREATE INDEX FK_FUNCAO_PAPEL ON PAPEL_FUNCAO (ID_FUNCAO);







-- ------------------------------------------------------------
-- Armazena os tipos de NF utilizados pela empresa e controla a numeração das notas.
-- ------------------------------------------------------------

CREATE TABLE NOTA_FISCAL_TIPO (
  ID SERIAL  NOT NULL ,
  ID_NOTA_FISCAL_MODELO INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT    ,
  SERIE CHAR(3)    ,
  SERIE_SCAN CHAR(3)    ,
  ULTIMO_NUMERO INTEGER      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_NOTA_FISCAL_MODELO)
    REFERENCES NOTA_FISCAL_MODELO(ID));


CREATE INDEX FK_EMPRESA_TIPO_NF ON NOTA_FISCAL_TIPO (ID_EMPRESA);
CREATE INDEX FK_NF_TIPO_MODELO ON NOTA_FISCAL_TIPO (ID_NOTA_FISCAL_MODELO);







-- ------------------------------------------------------------
-- Armazena as configurações do terminal de caixa. Esses dados podem ser importados da retaguarda ou armazenados diretamente no banco local através do sistema configurador.
-- ------------------------------------------------------------

CREATE TABLE NFCE_CONFIGURACAO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_NFCE_CAIXA INTEGER   NOT NULL ,
  ID_NFCE_RESOLUCAO INTEGER   NOT NULL ,
  MENSAGEM_CUPOM VARCHAR(250)    ,
  TITULO_TELA_CAIXA VARCHAR(100)    ,
  CAMINHO_IMAGENS_PRODUTOS VARCHAR(250)    ,
  CAMINHO_IMAGENS_MARKETING VARCHAR(250)    ,
  CAMINHO_IMAGENS_LAYOUT VARCHAR(250)    ,
  COR_JANELAS_INTERNAS VARCHAR(20)    ,
  MARKETING_ATIVO CHAR(1)    ,
  CFOP INTEGER    ,
  DECIMAIS_QUANTIDADE INTEGER    ,
  DECIMAIS_VALOR INTEGER    ,
  QUANTIDADE_MAXIMA_PARCELA INTEGER    ,
  IMPRIME_PARCELA CHAR(1)    ,
  CODIGO_CSC VARCHAR(32)    ,
  ID_TOKEN_CSC VARCHAR(6)    ,
  CERTIFICADO_DIGITAL_SERIE VARCHAR(100)    ,
  CERTIFICADO_DIGITAL_CAMINHO TEXT    ,
  CERTIFICADO_DIGITAL_SENHA VARCHAR(100)    ,
  TIPO_EMISSAO INTEGER    ,
  FORMATO_IMPRESSAO_DANFE INTEGER    ,
  PROCESSO_EMISSAO INTEGER    ,
  VERSAO_PROCESSO_EMISSAO VARCHAR(20)    ,
  CAMINHO_LOGOMARCA TEXT    ,
  SALVAR_XML CHAR(1)    ,
  CAMINHO_SALVAR_XML TEXT    ,
  CAMINHO_SCHEMAS TEXT    ,
  CAMINHO_ARQUIVO_DANFE TEXT    ,
  CAMINHO_SALVAR_PDF TEXT    ,
  WEBSERVICE_UF            CHAR(2)    ,
  WEBSERVICE_AMBIENTE      INTEGER    ,
  WEBSERVICE_PROXY_HOST    VARCHAR(100)    ,
  WEBSERVICE_PROXY_PORTA   INTEGER    ,
  WEBSERVICE_PROXY_USUARIO VARCHAR(100)    ,
  WEBSERVICE_PROXY_SENHA   VARCHAR(100)    ,
  WEBSERVICE_VISUALIZAR    CHAR(1)    ,
  EMAIL_SERVIDOR_SMTP      VARCHAR(100)    ,
  EMAIL_PORTA              INTEGER    ,
  EMAIL_USUARIO            VARCHAR(100)    ,
  EMAIL_SENHA              VARCHAR(100)    ,
  EMAIL_ASSUNTO            VARCHAR(100)    ,
  EMAIL_AUTENTICA_SSL      CHAR(1)    ,
  EMAIL_TEXTO              TEXT      ,
  OBERVACAO_PADRAO         TEXT,
PRIMARY KEY(ID)      ,
  FOREIGN KEY(ID_NFCE_RESOLUCAO)
    REFERENCES NFCE_RESOLUCAO(ID),
  FOREIGN KEY(ID_NFCE_CAIXA)
    REFERENCES NFCE_CAIXA(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_RESOLUCAO_CONFIGURACAO ON NFCE_CONFIGURACAO (ID_NFCE_RESOLUCAO);
CREATE INDEX FK_NFCE_CAIXA_CONFIG ON NFCE_CONFIGURACAO (ID_NFCE_CAIXA);
CREATE INDEX FK_EMPRESA_NFCE_CONFIG ON NFCE_CONFIGURACAO (ID_EMPRESA);





























-- ------------------------------------------------------------
-- Tabela com a relaçao de fornecedores das empresas.
-- ------------------------------------------------------------

CREATE TABLE FORNECEDOR (
  ID SERIAL  NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  ID_ATIVIDADE_FOR_CLI INTEGER   NOT NULL ,
  ID_SITUACAO_FOR_CLI INTEGER   NOT NULL ,
  DESDE DATE    ,
  OPTANTE_SIMPLES_NACIONAL CHAR(1)    ,
  LOCALIZACAO CHAR(1)    ,
  DATA_CADASTRO DATE    ,
  SOFRE_RETENCAO CHAR(1)    ,
  CHEQUE_NOMINAL_A VARCHAR(100)    ,
  OBSERVACAO TEXT    ,
  CONTA_REMETENTE VARCHAR(30)    ,
  PRAZO_MEDIO_ENTREGA INTEGER    ,
  GERA_FATURAMENTO CHAR(1)    ,
  NUM_DIAS_PRIMEIRO_VENCIMENTO INTEGER    ,
  NUM_DIAS_INTERVALO INTEGER    ,
  QUANTIDADE_PARCELAS INTEGER    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)      ,
PRIMARY KEY(ID)      ,
  FOREIGN KEY(ID_SITUACAO_FOR_CLI)
    REFERENCES SITUACAO_FOR_CLI(ID),
  FOREIGN KEY(ID_ATIVIDADE_FOR_CLI)
    REFERENCES ATIVIDADE_FOR_CLI(ID),
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_SITUACAO_FORNECEDOR ON FORNECEDOR (ID_SITUACAO_FOR_CLI);
CREATE INDEX FK_ATIVIDADE_FORNECEDOR ON FORNECEDOR (ID_ATIVIDADE_FOR_CLI);
CREATE INDEX FK_PESSOA_FORNECEDOR ON FORNECEDOR (ID_PESSOA);











-- ------------------------------------------------------------
-- Armazena os movimentos para determinado caixa. Podem haver vários movimentos durante um dia. Um movimento deve ter obrigatoriamente:
-- 
-- -Operador
-- -Caixa (terminal)
-- -Impressora
-- -Turno
-- -Status
-- 
-- É através dessa tabela que o caixa deve funcionar. Sem um movimento aberto não pode haver movimentação no caixa.
-- ------------------------------------------------------------

CREATE TABLE NFCE_MOVIMENTO (
  ID SERIAL  NOT NULL ,
  ID_NFCE_CAIXA INTEGER   NOT NULL ,
  ID_NFCE_OPERADOR INTEGER   NOT NULL ,
  ID_NFCE_TURNO INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_GERENTE_SUPERVISOR INTEGER   NOT NULL ,
  DATA_ABERTURA DATE    ,
  HORA_ABERTURA VARCHAR(8)    ,
  DATA_FECHAMENTO DATE    ,
  HORA_FECHAMENTO VARCHAR(8)    ,
  TOTAL_SUPRIMENTO DECIMAL(18,6)    ,
  TOTAL_SANGRIA DECIMAL(18,6)    ,
  TOTAL_NAO_FISCAL DECIMAL(18,6)    ,
  TOTAL_VENDA DECIMAL(18,6)    ,
  TOTAL_DESCONTO DECIMAL(18,6)    ,
  TOTAL_ACRESCIMO DECIMAL(18,6)    ,
  TOTAL_FINAL DECIMAL(18,6)    ,
  TOTAL_RECEBIDO DECIMAL(18,6)    ,
  TOTAL_TROCO DECIMAL(18,6)    ,
  TOTAL_CANCELADO DECIMAL(18,6)    ,
  STATUS_MOVIMENTO CHAR(1)      ,
PRIMARY KEY(ID)        ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_NFCE_TURNO)
    REFERENCES NFCE_TURNO(ID),
  FOREIGN KEY(ID_NFCE_OPERADOR)
    REFERENCES NFCE_OPERADOR(ID),
  FOREIGN KEY(ID_NFCE_CAIXA)
    REFERENCES NFCE_CAIXA(ID));


CREATE INDEX FK_EMPRESA_NFCE_MOV ON NFCE_MOVIMENTO (ID_EMPRESA);
CREATE INDEX FK_NFCE_TURNO_MOV ON NFCE_MOVIMENTO (ID_NFCE_TURNO);
CREATE INDEX FK_NFCE_OPERADOR_MOV ON NFCE_MOVIMENTO (ID_NFCE_OPERADOR);
CREATE INDEX FK_NFCE_CAIXA_MOV ON NFCE_MOVIMENTO (ID_NFCE_CAIXA);




-- ------------------------------------------------------------
-- Tabela que armazena os tipos de pagamento: DINHEIRO, CARTÃO, CHEQUE, etc.
-- Tipos padões já cadastrados pelo sistema para toda empresa:
-- 01 = Dinheiro | 02 = Cheque | 03 = Cartao
-- ------------------------------------------------------------

CREATE TABLE FIN_TIPO_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  TIPO CHAR(2)    ,
  DESCRICAO VARCHAR(30)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_TIPO_PGTO ON FIN_TIPO_PAGAMENTO (ID_EMPRESA);





-- ------------------------------------------------------------
-- Tabela que armazena as possíveis situações de uma parcela: PAGO, BAIXA PARCIAL, EM ABERTO,  RETENCAO, etc.
-- Status padrões (já são criados para cada empresa):
-- 01 = Aberto | 02 = Quitado | 03 = Quitado Parcial | 04 = Vencido
-- ------------------------------------------------------------

CREATE TABLE FIN_STATUS_PARCELA (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  SITUACAO CHAR(2)   NOT NULL ,
  DESCRICAO VARCHAR(30)    ,
  PROCEDIMENTO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_STATUS_PARCELA ON FIN_STATUS_PARCELA (ID_EMPRESA);






-- ------------------------------------------------------------
-- Armazena o cabeçalho de uma contagem de produtos.
-- ------------------------------------------------------------

CREATE TABLE INVENTARIO_CONTAGEM_CAB (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  DATA_CONTAGEM DATE    ,
  ESTOQUE_ATUALIZADO CHAR(1)    ,
  TIPO CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_CONT_ESTOQUE ON INVENTARIO_CONTAGEM_CAB (ID_EMPRESA);






-- ------------------------------------------------------------
-- Armazena as configurações do leitor serial.
-- ------------------------------------------------------------

CREATE TABLE NFCE_CONFIGURACAO_LEITOR_SER (
  ID SERIAL  NOT NULL ,
  ID_NFCE_CONFIGURACAO INTEGER   NOT NULL ,
  USA CHAR(1)    ,
  PORTA CHAR(4)    ,
  BAUD INTEGER    ,
  HAND_SHAKE INTEGER    ,
  PARITY INTEGER    ,
  STOP_BITS INTEGER    ,
  DATA_BITS INTEGER    ,
  INTERVALO INTEGER    ,
  USAR_FILA CHAR(1)    ,
  HARD_FLOW CHAR(1)    ,
  SOFT_FLOW CHAR(1)    ,
  SUFIXO VARCHAR(20)    ,
  EXCLUIR_SUFIXO CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFCE_CONFIGURACAO)
    REFERENCES NFCE_CONFIGURACAO(ID));


CREATE INDEX FK_CONF_LEITOR_SER ON NFCE_CONFIGURACAO_LEITOR_SER (ID_NFCE_CONFIGURACAO);











CREATE TABLE NFCE_FECHAMENTO (
  ID SERIAL  NOT NULL ,
  ID_NFCE_MOVIMENTO INTEGER   NOT NULL ,
  TIPO_PAGAMENTO VARCHAR(20)    ,
  VALOR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFCE_MOVIMENTO)
    REFERENCES NFCE_MOVIMENTO(ID));


CREATE INDEX FK_NFCE_MOV_FECHA ON NFCE_FECHAMENTO (ID_NFCE_MOVIMENTO);



-- ------------------------------------------------------------
-- Armazena as configurações da balança.
-- ------------------------------------------------------------

CREATE TABLE NFCE_CONFIGURACAO_BALANCA (
  ID SERIAL  NOT NULL ,
  ID_NFCE_CONFIGURACAO INTEGER   NOT NULL ,
  MODELO INTEGER    ,
  IDENTIFICADOR CHAR(1)    ,
  HAND_SHAKE INTEGER    ,
  PARITY INTEGER    ,
  STOP_BITS INTEGER    ,
  DATA_BITS INTEGER    ,
  BAUD_RATE INTEGER    ,
  PORTA CHAR(4)    ,
  TIMEOUT INTEGER    ,
  TIPO_CONFIGURACAO VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFCE_CONFIGURACAO)
    REFERENCES NFCE_CONFIGURACAO(ID));


CREATE INDEX FK_CONF_BALANCA ON NFCE_CONFIGURACAO_BALANCA (ID_NFCE_CONFIGURACAO);








-- ------------------------------------------------------------
-- Tabela com as cidades de determinado Estado.
-- ------------------------------------------------------------

CREATE TABLE MUNICIPIO (
  ID SERIAL  NOT NULL ,
  ID_UF INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  CODIGO_IBGE INTEGER    ,
  CODIGO_RECEITA_FEDERAL INTEGER    ,
  CODIGO_ESTADUAL INTEGER    ,
  UF_SIGLA CHAR(2)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_UF)
    REFERENCES UF(ID));


CREATE INDEX FK_UF_MUNICIPIO ON MUNICIPIO (ID_UF);



-- ------------------------------------------------------------
-- Tabela de setores/departamentos.
-- ------------------------------------------------------------

CREATE TABLE SETOR (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_SETOR ON SETOR (ID_EMPRESA);



-- ------------------------------------------------------------
-- Guarda o número da última nota emitida
-- ------------------------------------------------------------

CREATE TABLE NFE_NUMERO_INUTILIZADO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  SERIE CHAR(3)    ,
  NUMERO INTEGER    ,
  DATA_INUTILIZACAO DATE    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_NFE_INUT ON NFE_NUMERO_INUTILIZADO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Guarda o número da última nota emitida
-- ------------------------------------------------------------

CREATE TABLE NFE_NUMERO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  SERIE CHAR(3)    ,
  NUMERO INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_NFE_NUMERO ON NFE_NUMERO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Dados do quadro societário da empresa.
-- ------------------------------------------------------------

CREATE TABLE QUADRO_SOCIETARIO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  DATA_REGISTRO DATE    ,
  CAPITAL_SOCIAL DECIMAL(18,6)    ,
  VALOR_QUOTA DECIMAL(18,6)    ,
  QUANTIDADE_COTAS INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_QUADRO_SOCIO ON QUADRO_SOCIETARIO (ID_EMPRESA);






-- ------------------------------------------------------------
-- Armazena o Plano de Contas do Centro de Resultado.
-- ------------------------------------------------------------

CREATE TABLE PLANO_CENTRO_RESULTADO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  MASCARA VARCHAR(50)    ,
  NIVEIS INTEGER    ,
  DATA_INCLUSAO DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMP_PL_CT_RESULTADO ON PLANO_CENTRO_RESULTADO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Plano de contas da natureza financeira
-- ------------------------------------------------------------

CREATE TABLE PLANO_NATUREZA_FINANCEIRA (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  DATA_INCLUSAO DATE    ,
  MASCARA VARCHAR(50)    ,
  NIVEIS INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_PL_NT_FIN ON PLANO_NATUREZA_FINANCEIRA (ID_EMPRESA);



-- ------------------------------------------------------------
-- Tabela das operadoras de cartão.
-- ------------------------------------------------------------

CREATE TABLE OPERADORA_CARTAO (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  BANDEIRA VARCHAR(30)    ,
  NOME VARCHAR(50)    ,
  TAXA_ADM DECIMAL(18,6)    ,
  TAXA_ADM_DEBITO DECIMAL(18,6)    ,
  VALOR_ALUGUEL_POS_PIN DECIMAL(18,6)    ,
  VENCIMENTO_ALUGUEL INTEGER    ,
  FONE1 VARCHAR(14)    ,
  FONE2 VARCHAR(14)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_CONTA_OP_CARTAO ON OPERADORA_CARTAO (ID_CONTA_CAIXA);



CREATE TABLE NFE_CONFIGURACAO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  CERTIFICADO_DIGITAL_SERIE VARCHAR(100)    ,
  CERTIFICADO_DIGITAL_CAMINHO TEXT    ,
  CERTIFICADO_DIGITAL_SENHA VARCHAR(100)    ,
  TIPO_EMISSAO INTEGER    ,
  FORMATO_IMPRESSAO_DANFE INTEGER    ,
  PROCESSO_EMISSAO INTEGER    ,
  VERSAO_PROCESSO_EMISSAO VARCHAR(20)    ,
  CAMINHO_LOGOMARCA TEXT    ,
  SALVAR_XML CHAR(1)    ,
  CAMINHO_SALVAR_XML TEXT    ,
  CAMINHO_SCHEMAS TEXT    ,
  CAMINHO_ARQUIVO_DANFE TEXT    ,
  CAMINHO_SALVAR_PDF TEXT    ,
  WEBSERVICE_UF            CHAR(2)    ,
  WEBSERVICE_AMBIENTE      INTEGER    ,
  WEBSERVICE_PROXY_HOST    VARCHAR(100)    ,
  WEBSERVICE_PROXY_PORTA   INTEGER    ,
  WEBSERVICE_PROXY_USUARIO VARCHAR(100)    ,
  WEBSERVICE_PROXY_SENHA   VARCHAR(100)    ,
  WEBSERVICE_VISUALIZAR    CHAR(1)    ,
  EMAIL_SERVIDOR_SMTP      VARCHAR(100)    ,
  EMAIL_PORTA              INTEGER    ,
  EMAIL_USUARIO            VARCHAR(100)    ,
  EMAIL_SENHA              VARCHAR(100)    ,
  EMAIL_ASSUNTO            VARCHAR(100)    ,
  EMAIL_AUTENTICA_SSL      CHAR(1)    ,
  EMAIL_TEXTO              TEXT      ,
  OBSERVACAO_PADRAO        TEXT,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_NFE_CONFIG ON NFE_CONFIGURACAO (ID_EMPRESA);























-- ------------------------------------------------------------
-- Armazena todos os suprimentos que são feitos no caixa. Tem relacionamento direto com a tabela de movimento, já que um suprimento só pode ser feito se houver um movimento aberto.
-- ------------------------------------------------------------

CREATE TABLE NFCE_SUPRIMENTO (
  ID SERIAL  NOT NULL ,
  ID_NFCE_MOVIMENTO INTEGER   NOT NULL ,
  DATA_SUPRIMENTO DATE    ,
  VALOR DECIMAL(18,6)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFCE_MOVIMENTO)
    REFERENCES NFCE_MOVIMENTO(ID));


CREATE INDEX FK_NFCE_MOV_SUPRIMENTO ON NFCE_SUPRIMENTO (ID_NFCE_MOVIMENTO);



-- ------------------------------------------------------------
-- Armazena todas as sangrias que são feitas no caixa. Tem relacionamento direto com a tabela de movimento, já que uma sangria só pode ser feita se houver um 
-- movimento aberto.
-- ------------------------------------------------------------

CREATE TABLE NFCE_SANGRIA (
  ID SERIAL  NOT NULL ,
  ID_NFCE_MOVIMENTO INTEGER   NOT NULL ,
  DATA_SANGRIA DATE    ,
  VALOR DECIMAL(18,6)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFCE_MOVIMENTO)
    REFERENCES NFCE_MOVIMENTO(ID));


CREATE INDEX FK_NFCE_MOV_SANGRIA ON NFCE_SANGRIA (ID_NFCE_MOVIMENTO);



-- ------------------------------------------------------------
-- Dados do sócio.
-- ------------------------------------------------------------

CREATE TABLE SOCIO (
  ID SERIAL  NOT NULL ,
  ID_QUADRO_SOCIETARIO INTEGER   NOT NULL ,
  LOGRADOURO VARCHAR(100)    ,
  NUMERO INTEGER    ,
  COMPLEMENTO VARCHAR(100)    ,
  BAIRRO VARCHAR(100)    ,
  MUNICIPIO VARCHAR(100)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  FONE VARCHAR(10)    ,
  CELULAR VARCHAR(10)    ,
  EMAIL VARCHAR(250)    ,
  PARTICIPACAO DECIMAL(18,6)    ,
  QUOTAS INTEGER    ,
  INTEGRALIZAR DECIMAL(18,6)    ,
  DATA_INGRESSO DATE    ,
  DATA_SAIDA DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_QUADRO_SOCIETARIO)
    REFERENCES QUADRO_SOCIETARIO(ID));


CREATE INDEX FK_QUADRO_SOCIO ON SOCIO (ID_QUADRO_SOCIETARIO);






CREATE TABLE CTE_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  UF_EMITENTE INTEGER    ,
  CODIGO_NUMERICO VARCHAR(8)    ,
  CFOP INTEGER    ,
  NATUREZA_OPERACAO VARCHAR(60)    ,
  MODELO CHAR(2)    ,
  SERIE CHAR(3)    ,
  NUMERO VARCHAR(9)    ,
  DATA_HORA_EMISSAO TIMESTAMP    ,
  FORMATO_IMPRESSAO_DACTE INTEGER    ,
  TIPO_EMISSAO INTEGER    ,
  CHAVE_ACESSO VARCHAR(44)    ,
  DIGITO_CHAVE_ACESSO CHAR(1)    ,
  AMBIENTE INTEGER    ,
  TIPO_CTE INTEGER    ,
  PROCESSO_EMISSAO INTEGER    ,
  VERSAO_PROCESSO_EMISSAO VARCHAR(20)    ,
  CODIGO_MUNICIPIO_ENVIO INTEGER    ,
  NOME_MUNICIPIO_ENVIO VARCHAR(60)    ,
  UF_ENVIO CHAR(2)    ,
  MODAL CHAR(2)    ,
  TIPO_SERVICO INTEGER    ,
  CODIGO_MUNICIPIO_INI_PRESTACAO INTEGER    ,
  NOME_MUNICIPIO_INI_PRESTACAO VARCHAR(60)    ,
  UF_INI_PRESTACAO CHAR(2)    ,
  CODIGO_MUNICIPIO_FIM_PRESTACAO INTEGER    ,
  NOME_MUNICIPIO_FIM_PRESTACAO VARCHAR(60)    ,
  UF_FIM_PRESTACAO CHAR(2)    ,
  RETIRA INTEGER    ,
  RETIRA_DETALHE VARCHAR(160)    ,
  TOMADOR INTEGER    ,
  DATA_ENTRADA_CONTINGENCIA TIMESTAMP    ,
  JUSTIFICATIVA_CONTINGENCIA VARCHAR(255)    ,
  CARAC_ADICIONAL_TRANSPORTE VARCHAR(15)    ,
  CARAC_ADICIONAL_SERVICO VARCHAR(30)    ,
  FUNCIONARIO_EMISSOR VARCHAR(20)    ,
  FLUXO_ORIGEM VARCHAR(15)    ,
  ENTREGA_TIPO_PERIODO INTEGER    ,
  ENTREGA_DATA_PROGRAMADA DATE    ,
  ENTREGA_DATA_INICIAL DATE    ,
  ENTREGA_DATA_FINAL DATE    ,
  ENTREGA_TIPO_HORA INTEGER    ,
  ENTREGA_HORA_PROGRAMADA VARCHAR(8)    ,
  ENTREGA_HORA_INICIAL VARCHAR(8)    ,
  ENTREGA_HORA_FINAL VARCHAR(8)    ,
  MUNICIPIO_ORIGEM_CALCULO VARCHAR(40)    ,
  MUNICIPIO_DESTINO_CALCULO VARCHAR(40)    ,
  OBSERVACOES_GERAIS TEXT    ,
  VALOR_TOTAL_SERVICO DECIMAL(18,6)    ,
  VALOR_RECEBER DECIMAL(18,6)    ,
  CST CHAR(2)    ,
  BASE_CALCULO_ICMS DECIMAL(18,6)    ,
  ALIQUOTA_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS DECIMAL(18,6)    ,
  PERCENTUAL_REDUCAO_BC_ICMS DECIMAL(18,6)    ,
  VALOR_BC_ICMS_ST_RETIDO DECIMAL(18,6)    ,
  VALOR_ICMS_ST_RETIDO DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_ST_RETIDO DECIMAL(18,6)    ,
  VALOR_CREDITO_PRESUMIDO_ICMS DECIMAL(18,6)    ,
  PERCENTUAL_BC_ICMS_OUTRA_UF DECIMAL(18,6)    ,
  VALOR_BC_ICMS_OUTRA_UF DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_OUTRA_UF DECIMAL(18,6)    ,
  VALOR_ICMS_OUTRA_UF DECIMAL(18,6)    ,
  SIMPLES_NACIONAL_INDICADOR INTEGER    ,
  SIMPLES_NACIONAL_TOTAL DECIMAL(18,6)    ,
  INFORMACOES_ADD_FISCO TEXT    ,
  VALOR_TOTAL_CARGA DECIMAL(18,6)    ,
  PRODUTO_PREDOMINANTE VARCHAR(60)    ,
  CARGA_OUTRAS_CARACTERISTICAS VARCHAR(30)    ,
  MODAL_VERSAO_LAYOUT INTEGER    ,
  CHAVE_CTE_SUBSTITUIDO VARCHAR(44)    ,
  INDICADOR_CTE_GLOBALIZADO INTEGER    ,
  INDICADOR_PAPEL_TOMADOR INTEGER    ,
  VALOR_BC_UF_FIM DECIMAL(18,6)    ,
  PERCENTUAL_FCP_UF_FIM DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_UF_FIM DECIMAL(18,6)    ,
  ALIQUOTA_INTERESTADUAL_UF_FIM DECIMAL(18,6)    ,
  PERCENTUAL_PARTILHA_UF_FIM DECIMAL(18,6)    ,
  VALOR_ICMS_FCP_UF_FIM DECIMAL(18,6)    ,
  VALOR_ICMS_PARTILHA_UF_FIM DECIMAL(18,6)    ,
  VALOR_ICMS_PARTILHA_UF_INI DECIMAL(18,6)    ,
  VALOR_CARGA_AVERBACAO DECIMAL(18,6)    ,
  VERSAO_APLICATIVO VARCHAR(20)    ,
  DATA_HORA_PROCESSAMENTO TIMESTAMP    ,
  NUMERO_PROTOCOLO VARCHAR(15)    ,
  DIGEST_VALUE VARCHAR(28)    ,
  CODIGO_STATUS_RESPOSTA CHAR(3)    ,
  DESCRICAO_MOTIVO_RESPOSTA TEXT    ,
  JUSTIFICATIVA_CANCELAMENTO VARCHAR(255)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_CTE_CABECALHO ON CTE_CABECALHO (ID_EMPRESA);



























































































CREATE TABLE CTE_COMPONENTE (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  NOME VARCHAR(15)    ,
  VALOR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_COMPONENTE ON CTE_COMPONENTE (ID_CTE_CABECALHO);





CREATE TABLE CTE_DESTINATARIO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  IE VARCHAR(20)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  EMAIL VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_DESTINATARIO ON CTE_DESTINATARIO (ID_CTE_CABECALHO);


















CREATE TABLE CTE_CONFIGURACAO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  CERTIFICADO_DIGITAL_SERIE VARCHAR(100)    ,
  CERTIFICADO_DIGITAL_CAMINHO TEXT    ,
  CERTIFICADO_DIGITAL_SENHA VARCHAR(100)    ,
  TIPO_EMISSAO INTEGER    ,
  FORMATO_IMPRESSAO_DANFE INTEGER    ,
  PROCESSO_EMISSAO INTEGER    ,
  VERSAO_PROCESSO_EMISSAO VARCHAR(20)    ,
  CAMINHO_LOGOMARCA TEXT    ,
  SALVAR_XML CHAR(1)    ,
  CAMINHO_SALVAR_XML TEXT    ,
  CAMINHO_SCHEMAS TEXT    ,
  CAMINHO_ARQUIVO_DANFE TEXT    ,
  CAMINHO_SALVAR_PDF TEXT    ,
  WEBSERVICE_UF            CHAR(2)    ,
  WEBSERVICE_AMBIENTE      INTEGER    ,
  WEBSERVICE_PROXY_HOST    VARCHAR(100)    ,
  WEBSERVICE_PROXY_PORTA   INTEGER    ,
  WEBSERVICE_PROXY_USUARIO VARCHAR(100)    ,
  WEBSERVICE_PROXY_SENHA   VARCHAR(100)    ,
  WEBSERVICE_VISUALIZAR    CHAR(1)    ,
  EMAIL_SERVIDOR_SMTP      VARCHAR(100)    ,
  EMAIL_PORTA              INTEGER    ,
  EMAIL_USUARIO            VARCHAR(100)    ,
  EMAIL_SENHA              VARCHAR(100)    ,
  EMAIL_ASSUNTO            VARCHAR(100)    ,
  EMAIL_AUTENTICA_SSL      CHAR(1)    ,
  EMAIL_TEXTO              TEXT      ,
  OBSERVACAO_PADRAO        TEXT,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_CTE_CONFIG ON CTE_CONFIGURACAO (ID_EMPRESA);























CREATE TABLE CTE_EXPEDIDOR (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  IE VARCHAR(20)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  EMAIL VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_EXPEDIDOR ON CTE_EXPEDIDOR (ID_CTE_CABECALHO);


















-- ------------------------------------------------------------
-- Tabela que armazena o cabeçalho das notas fiscais eletrônicas.
-- ------------------------------------------------------------

CREATE TABLE CTE_INFORMACAO_NF_OUTROS (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  NUMERO_ROMANEIO VARCHAR(20)    ,
  NUMERO_PEDIDO VARCHAR(20)    ,
  CHAVE_ACESSO_NFE VARCHAR(44)    ,
  CODIGO_MODELO CHAR(2)    ,
  SERIE CHAR(3)    ,
  NUMERO VARCHAR(20)    ,
  DATA_EMISSAO DATE    ,
  UF_EMITENTE INTEGER    ,
  BASE_CALCULO_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS DECIMAL(18,6)    ,
  BASE_CALCULO_ICMS_ST DECIMAL(18,6)    ,
  VALOR_ICMS_ST DECIMAL(18,6)    ,
  VALOR_TOTAL_PRODUTOS DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  CFOP_PREDOMINANTE INTEGER    ,
  PESO_TOTAL_KG DECIMAL(18,6)    ,
  PIN_SUFRAMA INTEGER    ,
  DATA_PREVISTA_ENTREGA DATE    ,
  OUTRO_TIPO_DOC_ORIG CHAR(2)    ,
  OUTRO_DESCRICAO VARCHAR(100)    ,
  OUTRO_VALOR_DOCUMENTO DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_INFORMACAO_NF ON CTE_INFORMACAO_NF_OUTROS (ID_CTE_CABECALHO);
























CREATE TABLE CTE_LOCAL_COLETA (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  NOME VARCHAR(60)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_COLETA ON CTE_LOCAL_COLETA (ID_CTE_CABECALHO);











CREATE TABLE CTE_FERROVIARIO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  TIPO_TRAFEGO INTEGER    ,
  RESPONSAVEL_FATURAMENTO INTEGER    ,
  FERROVIA_EMITENTE_CTE INTEGER    ,
  FLUXO VARCHAR(10)    ,
  ID_TREM VARCHAR(7)    ,
  VALOR_FRETE DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_FERROVIARIO ON CTE_FERROVIARIO (ID_CTE_CABECALHO);









-- ------------------------------------------------------------
-- Grupo da Fatura
-- ------------------------------------------------------------

CREATE TABLE CTE_FATURA (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  NUMERO VARCHAR(60)    ,
  VALOR_ORIGINAL DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_LIQUIDO DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_FATURA ON CTE_FATURA (ID_CTE_CABECALHO);







CREATE TABLE CTE_FERROVIARIO_FERROVIA (
  ID SERIAL  NOT NULL ,
  ID_CTE_FERROVIARIO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CODIGO_INTERNO VARCHAR(10)    ,
  IE VARCHAR(20)    ,
  NOME VARCHAR(60)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_FERROVIARIO)
    REFERENCES CTE_FERROVIARIO(ID));


CREATE INDEX FK_CTE_FERRO_VIA ON CTE_FERROVIARIO_FERROVIA (ID_CTE_FERROVIARIO);













CREATE TABLE CTE_INFORMACAO_NF_CARGA (
  ID SERIAL  NOT NULL ,
  ID_CTE_INFORMACAO_NF INTEGER   NOT NULL ,
  TIPO_UNIDADE_CARGA INTEGER    ,
  ID_UNIDADE_CARGA VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_INFORMACAO_NF)
    REFERENCES CTE_INFORMACAO_NF_OUTROS(ID));


CREATE INDEX FK_CTE_INF_NF_CARGA ON CTE_INFORMACAO_NF_CARGA (ID_CTE_INFORMACAO_NF);





CREATE TABLE CTE_FERROVIARIO_VAGAO (
  ID SERIAL  NOT NULL ,
  ID_CTE_FERROVIARIO INTEGER   NOT NULL ,
  NUMERO_VAGAO INTEGER    ,
  CAPACIDADE DECIMAL(18,6)    ,
  TIPO_VAGAO CHAR(3)    ,
  PESO_REAL DECIMAL(18,6)    ,
  PESO_BC DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_FERROVIARIO)
    REFERENCES CTE_FERROVIARIO(ID));


CREATE INDEX FK_CTE_FERRO_VAGAO ON CTE_FERROVIARIO_VAGAO (ID_CTE_FERROVIARIO);








-- ------------------------------------------------------------
-- Tabela com o cadastro do Centro de Resultado. Seria o Centro de Custo.   
--   
-- Quando se menciona CUSTO, associa-se apenas às despesas e afins. Quando se menciona RESULTADO, pode-se associar a receitas e despesas. Ex: O departamento de  
-- produção praticamente só aparece nas despesas, mas o setor comercial gera receitas e despesas, daí o conceito de resultado.  
-- ------------------------------------------------------------

CREATE TABLE CENTRO_RESULTADO (
  ID SERIAL  NOT NULL ,
  ID_PLANO_CENTRO_RESULTADO INTEGER   NOT NULL ,
  CLASSIFICACAO VARCHAR(30)    ,
  DESCRICAO VARCHAR(100)    ,
  SOFRE_RATEIRO CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PLANO_CENTRO_RESULTADO)
    REFERENCES PLANO_CENTRO_RESULTADO(ID));


CREATE INDEX FK_PLANO_CENTRO ON CENTRO_RESULTADO (ID_PLANO_CENTRO_RESULTADO);



-- ------------------------------------------------------------
-- Caso não queira utilizaros perfis de comissão, basta usar a tabela VENDA_COMISSAO, forma mais simplificada.
-- ------------------------------------------------------------

CREATE TABLE COMISSAO_PERFIL (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  CODIGO CHAR(3)    ,
  NOME VARCHAR(100)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_COMISSAO_PERFIL ON COMISSAO_PERFIL (ID_EMPRESA);



-- ------------------------------------------------------------
-- Parametros diversos para o ERP.
-- ------------------------------------------------------------

CREATE TABLE ADM_PARAMETRO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  FIN_PARCELA_ABERTO INTEGER    ,
  FIN_PARCELA_QUITADO INTEGER    ,
  FIN_PARCELA_QUITADO_PARCIAL INTEGER    ,
  FIN_TIPO_RECEBIMENTO_EDI INTEGER    ,
  COMPRA_FIN_DOC_ORIGEM INTEGER    ,
  COMPRA_CONTA_CAIXA INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_PARAMETRO ON ADM_PARAMETRO (ID_EMPRESA);









-- ------------------------------------------------------------
-- Armazena os almoxarifados vinculados ao depósito ou à própria empresa. Deve existir pelo menos um almoxarifado padrão.
-- ------------------------------------------------------------

CREATE TABLE ALMOXARIFADO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(50)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_ALMOXARIFADO ON ALMOXARIFADO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Tabela de cargos.
-- ------------------------------------------------------------

CREATE TABLE CARGO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT    ,
  SALARIO DECIMAL(18,6)    ,
  CBO_1994 VARCHAR(10)    ,
  CBO_2002 VARCHAR(10)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_CARGO ON CARGO (ID_EMPRESA);





-- ------------------------------------------------------------
-- Tabela de convênios.
-- ------------------------------------------------------------

CREATE TABLE CONVENIO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(150)    ,
  CNPJ VARCHAR(14)    ,
  EMAIL VARCHAR(250)    ,
  SITE VARCHAR(250)    ,
  DESCONTO DECIMAL(18,6)    ,
  DATA_VENCIMENTO DATE    ,
  LOGRADOURO VARCHAR(100)    ,
  NUMERO VARCHAR(10)    ,
  BAIRRO VARCHAR(60)    ,
  CIDADE VARCHAR(60)    ,
  MUNICIPIO_IBGE INTEGER    ,
  UF CHAR(2)    ,
  TELEFONE VARCHAR(14)    ,
  DATA_CADASTRO DATE    ,
  DESCRICAO TEXT    ,
  CEP VARCHAR(8)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)    ,
  CONTATO VARCHAR(100)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_CONVENIO ON CONVENIO (ID_EMPRESA);



CREATE TABLE CONTRIB_SIND_PATRONAL_DET (
  ID SERIAL  NOT NULL ,
  ID_CONTRIB_SIND_PATRONAL_CAB INTEGER   NOT NULL ,
  DE DECIMAL(18,6)    ,
  ATE DECIMAL(18,6)    ,
  PERCENTUAL DECIMAL(18,6)    ,
  VALOR_ADICIONAR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CONTRIB_SIND_PATRONAL_CAB)
    REFERENCES CONTRIB_SIND_PATRONAL_CAB(ID));


CREATE INDEX FK_CONTR_SIND_PATRONAL ON CONTRIB_SIND_PATRONAL_DET (ID_CONTRIB_SIND_PATRONAL_CAB);



CREATE TABLE CTE_AEREO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  NUMERO_MINUTA INTEGER    ,
  NUMERO_CONHECIMENTO INTEGER    ,
  DATA_PREVISTA_ENTREGA DATE    ,
  ID_EMISSOR VARCHAR(20)    ,
  ID_INTERNA_TOMADOR VARCHAR(14)    ,
  TARIFA_CLASSE CHAR(1)    ,
  TARIFA_CODIGO VARCHAR(4)    ,
  TARIFA_VALOR DECIMAL(18,6)    ,
  CARGA_DIMENSAO VARCHAR(14)    ,
  CARGA_INFORMACAO_MANUSEIO INTEGER    ,
  CARGA_ESPECIAL CHAR(3)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_AEREO ON CTE_AEREO (ID_CTE_CABECALHO);














CREATE TABLE CTE_AQUAVIARIO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  VALOR_PRESTACAO DECIMAL(18,6)    ,
  AFRMM DECIMAL(18,6)    ,
  NUMERO_BOOKING VARCHAR(10)    ,
  NUMERO_CONTROLE VARCHAR(10)    ,
  ID_NAVIO VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_AQUAVIARIO ON CTE_AQUAVIARIO (ID_CTE_CABECALHO);








-- ------------------------------------------------------------
-- Armazena os tipos de requisição:
-- 
-- 01=INTERNA = requisição onde os itens são utilizados pela própria empresa
-- 02=EXTERNA = requisição onde os itens são utilizados para venda ao consumidor
-- ------------------------------------------------------------

CREATE TABLE COMPRA_TIPO_REQUISICAO (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(2)    ,
  NOME VARCHAR(30)    ,
  DESCRICAO TEXT      ,
  PRIMARY KEY (ID)
);







-- ------------------------------------------------------------
-- Esta tabela deve irá armazenar as diferentes operações presentes na empresa conforme cada tratamento tributário nela existente do ponto de vista do Cliente ou Destinatário. 
-- 
-- Exemplos: 
-- 1. Venda a  não contribuinte. Ex. Consumidor final, Construtora ou empresas que comprem os produtos para uso próprio.
-- 2. Venda para Lojistas (Revendedor)
-- 3. Venda Consumidor fora do Estado
-- 4. Venda a Contribuinte Usuário final (ex. construtora, empresa que comprem para consumo)
-- 5. Venda Lojista fora do Estado
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_OPERACAO_FISCAL (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  DESCRICAO VARCHAR(100)    ,
  DESCRICAO_NA_NF VARCHAR(100)    ,
  CFOP INTEGER    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_OPERACAO_FISCAL ON TRIBUT_OPERACAO_FISCAL (ID_EMPRESA);





-- ------------------------------------------------------------
-- Esta tabela irá armazenar os Perfis tributários diferentes existentes dentro do mix de produtos da empresa. Ou seja, os diferentes conjuntos de produtos cuja tributação sofrem variações. Definindo o GRUPO TRIBUTÁRIO (independente da classificação de grupo gerencial empregada pelo cliente). Isto é, produtos com tributações idênticas ficariam todos em um grupo independentemente suas características intrínsecas.
-- 
-- Exemplos:
-- 1. Produtos de fabricação própria (sujeitos ao ICMS ST)
-- 2. Produtos de Revenda (sujeitos ao Regime do ICMS ST)
-- 3. Produtos de Revenda Não sujeitos ao ICMS ST
-- 4. Produtos com Suspensao de IPI
-- 5. Produtos com Suspensao de PIS, COFINS
-- 6. etc.
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_GRUPO_TRIBUTARIO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  DESCRICAO VARCHAR(100)    ,
  ORIGEM_MERCADORIA CHAR(1)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_GRUPO_TRIBUTARIO ON TRIBUT_GRUPO_TRIBUTARIO (ID_EMPRESA);





-- ------------------------------------------------------------
-- Tabela de endereços da EMPRESA.
-- ------------------------------------------------------------

CREATE TABLE EMPRESA_ENDERECO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(10)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CIDADE VARCHAR(60)    ,
  CEP VARCHAR(8)    ,
  FONE VARCHAR(14)    ,
  MUNICIPIO_IBGE INTEGER    ,
  UF CHAR(2)    ,
  PRINCIPAL CHAR(1)    ,
  ENTREGA CHAR(1)    ,
  COBRANCA CHAR(1)    ,
  CORRESPONDENCIA CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_ENDERECO ON EMPRESA_ENDERECO (ID_EMPRESA);







CREATE TABLE EMPRESA_TELEFONE (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  TIPO INTEGER    ,
  NUMERO VARCHAR(14)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMRESA_TELEFONE ON EMPRESA_TELEFONE (ID_EMPRESA);




-- ------------------------------------------------------------
-- Tabela de contatos da EMPRESA.
-- ------------------------------------------------------------

CREATE TABLE EMPRESA_CONTATO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  EMAIL VARCHAR(250)    ,
  FONE_COMERCIAL VARCHAR(14)    ,
  FONE_RESIDENCIAL VARCHAR(14)    ,
  FONE_CELULAR VARCHAR(14)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_CONTATO ON EMPRESA_CONTATO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Dados referentes à participação do sócio em outras empresas.
-- ------------------------------------------------------------

CREATE TABLE SOCIO_PARTICIPACAO_SOCIETARIA (
  ID SERIAL  NOT NULL ,
  ID_SOCIO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  RAZAO_SOCIAL VARCHAR(100)    ,
  DATA_ENTRADA DATE    ,
  DATA_SAIDA DATE    ,
  PARTICIPACAO DECIMAL(18,6)    ,
  DIRIGENTE CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_SOCIO)
    REFERENCES SOCIO(ID));


CREATE INDEX FK_SOCIO_PART_SOCIETARIA ON SOCIO_PARTICIPACAO_SOCIETARIA (ID_SOCIO);




-- ------------------------------------------------------------
-- Caso a configuração do ICMS precise ser "customizada" para determinado produto, utiliza-se essa tabela e sua "filha".
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_ICMS_CUSTOM_CAB (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  DESCRICAO VARCHAR(100)    ,
  ORIGEM_MERCADORIA CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_EMPRESA_ICMS_CUSTOM ON TRIBUT_ICMS_CUSTOM_CAB (ID_EMPRESA);




-- ------------------------------------------------------------
-- Caso a configuração do ICMS precise ser "customizada" para determinado produto, utiliza-se essa tabela e sua "mãe".
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_ICMS_CUSTOM_DET (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_ICMS_CUSTOM_CAB INTEGER   NOT NULL ,
  UF_DESTINO CHAR(2)    ,
  CFOP INTEGER    ,
  CSOSN_B CHAR(3)    ,
  CST_B CHAR(2)    ,
  MODALIDADE_BC CHAR(1)    ,
  ALIQUOTA DECIMAL(18,6)    ,
  VALOR_PAUTA DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO DECIMAL(18,6)    ,
  MVA DECIMAL(18,6)    ,
  PORCENTO_BC DECIMAL(18,6)    ,
  MODALIDADE_BC_ST CHAR(1)    ,
  ALIQUOTA_INTERNA_ST DECIMAL(18,6)    ,
  ALIQUOTA_INTERESTADUAL_ST DECIMAL(18,6)    ,
  PORCENTO_BC_ST DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_ST DECIMAL(18,6)    ,
  VALOR_PAUTA_ST DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO_ST DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_TRIBUT_ICMS_CUSTOM_CAB)
    REFERENCES TRIBUT_ICMS_CUSTOM_CAB(ID));


CREATE INDEX FK_ICMS_CUSTOM_CAB_DET ON TRIBUT_ICMS_CUSTOM_DET (ID_TRIBUT_ICMS_CUSTOM_CAB);




















-- ------------------------------------------------------------
-- Tabela para cadastro dos tipo de documentos que podem gerar contas a pagar ou receber: nota fiscal, boleto, recibo, etc.
-- ------------------------------------------------------------

CREATE TABLE FIN_DOCUMENTO_ORIGEM (
  ID SERIAL  NOT NULL ,
  CODIGO CHAR(3)    ,
  SIGLA_DOCUMENTO CHAR(10)    ,
  DESCRICAO TEXT      ,
  PRIMARY KEY (ID)
);


-- ------------------------------------------------------------
-- Armazena os dados do extrato bancário.
-- ------------------------------------------------------------

CREATE TABLE FIN_EXTRATO_CONTA_BANCO (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  MES_ANO VARCHAR(7)    ,
  MES CHAR(2)    ,
  ANO CHAR(4)    ,
  DATA_MOVIMENTO DATE    ,
  DATA_BALANCETE DATE    ,
  HISTORICO VARCHAR(250)    ,
  DOCUMENTO VARCHAR(50)    ,
  VALOR DECIMAL(18,6)    ,
  CONCILIADO CHAR(1)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_CONTA_CX_EXTRATO ON FIN_EXTRATO_CONTA_BANCO (ID_CONTA_CAIXA);





-- ------------------------------------------------------------
-- Armazena os dados de um fechamento mensal.
-- ------------------------------------------------------------

CREATE TABLE FIN_FECHAMENTO_CAIXA_BANCO (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  DATA_FECHAMENTO DATE    ,
  MES_ANO VARCHAR(7)    ,
  MES CHAR(2)    ,
  ANO CHAR(4)    ,
  SALDO_ANTERIOR DECIMAL(18,6)    ,
  RECEBIMENTOS DECIMAL(18,6)    ,
  PAGAMENTOS DECIMAL(18,6)    ,
  SALDO_CONTA DECIMAL(18,6)    ,
  CHEQUE_NAO_COMPENSADO DECIMAL(18,6)    ,
  SALDO_DISPONIVEL DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_CONTA_CX_FECHAMENTO ON FIN_FECHAMENTO_CAIXA_BANCO (ID_CONTA_CAIXA);




CREATE TABLE CTE_RODOVIARIO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  RNTRC VARCHAR(8)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_RODOVIARIO ON CTE_RODOVIARIO (ID_CTE_CABECALHO);



CREATE TABLE CTE_REMETENTE (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  IE VARCHAR(20)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  EMAIL VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_REMETENTE ON CTE_REMETENTE (ID_CTE_CABECALHO);


















CREATE TABLE CTE_RODOVIARIO_MOTORISTA (
  ID SERIAL  NOT NULL ,
  ID_CTE_RODOVIARIO INTEGER   NOT NULL ,
  NOME VARCHAR(60)    ,
  CPF VARCHAR(11)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_RODOVIARIO)
    REFERENCES CTE_RODOVIARIO(ID));


CREATE INDEX FK_CTE_ROD_MOTORISTA ON CTE_RODOVIARIO_MOTORISTA (ID_CTE_RODOVIARIO);



CREATE TABLE CTE_RODOVIARIO_LACRE (
  ID SERIAL  NOT NULL ,
  ID_CTE_RODOVIARIO INTEGER   NOT NULL ,
  NUMERO VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_RODOVIARIO)
    REFERENCES CTE_RODOVIARIO(ID));


CREATE INDEX FK_CTE_ROD_LACRE ON CTE_RODOVIARIO_LACRE (ID_CTE_RODOVIARIO);



CREATE TABLE CTE_RECEBEDOR (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  IE VARCHAR(20)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  EMAIL VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_RECEBEDOR ON CTE_RECEBEDOR (ID_CTE_CABECALHO);


















CREATE TABLE CTE_MULTIMODAL (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  COTM VARCHAR(20)    ,
  INDICADOR_NEGOCIAVEL INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_MULTI ON CTE_MULTIMODAL (ID_CTE_CABECALHO);




CREATE TABLE CTE_LOCAL_ENTREGA (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  NOME VARCHAR(60)    ,
  LOGRADOURO VARCHAR(250)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_ENTREGA ON CTE_LOCAL_ENTREGA (ID_CTE_CABECALHO);











CREATE TABLE CTE_PERIGOSO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  NUMERO_ONU VARCHAR(4)    ,
  NOME_APROPRIADO VARCHAR(150)    ,
  CLASSE_RISCO VARCHAR(40)    ,
  GRUPO_EMBALAGEM VARCHAR(6)    ,
  QUANTIDADE_TOTAL_PRODUTO VARCHAR(20)    ,
  QUANTIDADE_TIPO_VOLUME VARCHAR(60)    ,
  PONTO_FULGOR VARCHAR(6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_PERIGO ON CTE_PERIGOSO (ID_CTE_CABECALHO);










CREATE TABLE CTE_PASSAGEM (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  SIGLA_PASSAGEM VARCHAR(15)    ,
  SIGLA_DESTINO VARCHAR(15)    ,
  ROTA VARCHAR(10)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_PASSAGEM ON CTE_PASSAGEM (ID_CTE_CABECALHO);






CREATE TABLE CTE_RODOVIARIO_OCC (
  ID SERIAL  NOT NULL ,
  ID_CTE_RODOVIARIO INTEGER   NOT NULL ,
  SERIE CHAR(3)    ,
  NUMERO INTEGER    ,
  DATA_EMISSAO DATE    ,
  CNPJ VARCHAR(14)    ,
  CODIGO_INTERNO VARCHAR(10)    ,
  IE VARCHAR(14)    ,
  UF CHAR(2)    ,
  TELEFONE VARCHAR(14)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_RODOVIARIO)
    REFERENCES CTE_RODOVIARIO(ID));


CREATE INDEX FK_CTE_ROD_OCC ON CTE_RODOVIARIO_OCC (ID_CTE_RODOVIARIO);







CREATE TABLE CTE_TOMADOR (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  IE VARCHAR(14)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  LOGRADOURO VARCHAR(255)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  EMAIL VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_TOMADOR ON CTE_TOMADOR (ID_CTE_CABECALHO);




















CREATE TABLE CTE_SEGURO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  RESPONSAVEL INTEGER    ,
  SEGURADORA VARCHAR(30)    ,
  APOLICE VARCHAR(20)    ,
  AVERBACAO VARCHAR(20)    ,
  VALOR_CARGA DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_SEGURO ON CTE_SEGURO (ID_CTE_CABECALHO);








-- ------------------------------------------------------------
-- Grupo do detalhamento de Veículos novos. Informar apenas quando se tratar de veículos novos
-- ------------------------------------------------------------

CREATE TABLE CTE_VEICULO_NOVO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CHASSI VARCHAR(17)    ,
  COR VARCHAR(4)    ,
  DESCRICAO_COR VARCHAR(40)    ,
  CODIGO_MARCA_MODELO VARCHAR(6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_FRETE DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_VEICULO_NOVO ON CTE_VEICULO_NOVO (ID_CTE_CABECALHO);









-- ------------------------------------------------------------
-- Armazena os dados de ISS - Imposto Sobre Serviços.
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_ISS (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_OPERACAO_FISCAL INTEGER   NOT NULL ,
  MODALIDADE_BASE_CALCULO CHAR(1)    ,
  PORCENTO_BASE_CALCULO DECIMAL(18,6)    ,
  ALIQUOTA_PORCENTO DECIMAL(18,6)    ,
  ALIQUOTA_UNIDADE DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO DECIMAL(18,6)    ,
  VALOR_PAUTA_FISCAL DECIMAL(18,6)    ,
  ITEM_LISTA_SERVICO INTEGER    ,
  CODIGO_TRIBUTACAO CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_TRIBUT_OPERACAO_FISCAL)
    REFERENCES TRIBUT_OPERACAO_FISCAL(ID));


CREATE INDEX FK_TRIBUT_OP_FISCAL_ISS ON TRIBUT_ISS (ID_TRIBUT_OPERACAO_FISCAL);





CREATE TABLE CTE_RODOVIARIO_PEDAGIO (
  ID SERIAL  NOT NULL ,
  ID_CTE_RODOVIARIO INTEGER   NOT NULL ,
  CNPJ_FORNECEDOR VARCHAR(14)    ,
  COMPROVANTE_COMPRA VARCHAR(20)    ,
  CNPJ_RESPONSAVEL VARCHAR(14)    ,
  VALOR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_RODOVIARIO)
    REFERENCES CTE_RODOVIARIO(ID));


CREATE INDEX FK_CTE_ROD_PEDAGIO ON CTE_RODOVIARIO_PEDAGIO (ID_CTE_RODOVIARIO);







CREATE TABLE CTE_RODOVIARIO_VEICULO (
  ID SERIAL  NOT NULL ,
  ID_CTE_RODOVIARIO INTEGER   NOT NULL ,
  CODIGO_INTERNO VARCHAR(10)    ,
  RENAVAM VARCHAR(11)    ,
  PLACA VARCHAR(7)    ,
  TARA INTEGER    ,
  CAPACIDADE_KG INTEGER    ,
  CAPACIDADE_M3 INTEGER    ,
  TIPO_PROPRIEDADE CHAR(1)    ,
  TIPO_VEICULO INTEGER    ,
  TIPO_RODADO CHAR(2)    ,
  TIPO_CARROCERIA CHAR(2)    ,
  UF CHAR(2)    ,
  PROPRIETARIO_CPF VARCHAR(11)    ,
  PROPRIETARIO_CNPJ VARCHAR(14)    ,
  PROPRIETARIO_RNTRC VARCHAR(8)    ,
  PROPRIETARIO_NOME VARCHAR(60)    ,
  PROPRIETARIO_IE VARCHAR(14)    ,
  PROPRIETARIO_UF CHAR(2)    ,
  PROPRIETARIO_TIPO INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_RODOVIARIO)
    REFERENCES CTE_RODOVIARIO(ID));


CREATE INDEX FK_CTE_ROD_VEICULO ON CTE_RODOVIARIO_VEICULO (ID_CTE_RODOVIARIO);













-- ------------------------------------------------------------
-- Dependentes do sócio.
-- ------------------------------------------------------------

CREATE TABLE SOCIO_DEPENDENTE (
  ID SERIAL  NOT NULL ,
  ID_SOCIO INTEGER   NOT NULL ,
  ID_TIPO_RELACIONAMENTO INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  DATA_NASCIMENTO DATE    ,
  DATA_INICIO_DEPEDENCIA DATE    ,
  DATA_FIM_DEPENDENCIA DATE    ,
  CPF VARCHAR(11)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_TIPO_RELACIONAMENTO)
    REFERENCES TIPO_RELACIONAMENTO(ID),
  FOREIGN KEY(ID_SOCIO)
    REFERENCES SOCIO(ID));


CREATE INDEX FK_RELACIONA_SOCIO_DEP ON SOCIO_DEPENDENTE (ID_TIPO_RELACIONAMENTO);
CREATE INDEX FK_SOCIO_DEPENDENTE ON SOCIO_DEPENDENTE (ID_SOCIO);



-- ------------------------------------------------------------
-- Tabela que armazena os talonario de cheque de determinada empresa.
-- ------------------------------------------------------------

CREATE TABLE TALONARIO_CHEQUE (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  TALAO VARCHAR(10)    ,
  NUMERO INTEGER    ,
  STATUS_TALAO CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_EMPRESA_TALONARIO_CHEQUE ON TALONARIO_CHEQUE (ID_EMPRESA);
CREATE INDEX FK_CONTA_TALONARIO ON TALONARIO_CHEQUE (ID_CONTA_CAIXA);




-- ------------------------------------------------------------
-- Tabela que relaciona os municípios atendidos por determinada transportadora.
-- ------------------------------------------------------------

CREATE TABLE TRANSPORTADORA_MUNICIPIO (
  ID SERIAL  NOT NULL ,
  ID_MUNICIPIO INTEGER   NOT NULL ,
  ID_TRANSPORTADORA INTEGER   NOT NULL   ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_TRANSPORTADORA)
    REFERENCES TRANSPORTADORA(ID),
  FOREIGN KEY(ID_MUNICIPIO)
    REFERENCES MUNICIPIO(ID));


CREATE INDEX FK_TRANSPORTADORA_MUNICIPIO ON TRANSPORTADORA_MUNICIPIO (ID_TRANSPORTADORA);
CREATE INDEX FK_MUNICIPIO_TRANSPORTADORA ON TRANSPORTADORA_MUNICIPIO (ID_MUNICIPIO);



-- ------------------------------------------------------------
-- Configura Operação Fiscal / Grupo Tributário
-- 
-- Armazena as configurações dos tributos da Operação Fiscal relacionada com o Grupo Tributário
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_CONFIGURA_OF_GT (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_GRUPO_TRIBUTARIO INTEGER   NOT NULL ,
  ID_TRIBUT_OPERACAO_FISCAL INTEGER   NOT NULL   ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_TRIBUT_OPERACAO_FISCAL)
    REFERENCES TRIBUT_OPERACAO_FISCAL(ID),
  FOREIGN KEY(ID_TRIBUT_GRUPO_TRIBUTARIO)
    REFERENCES TRIBUT_GRUPO_TRIBUTARIO(ID));


CREATE INDEX FK_OP_FISCAL_CONFIGURA ON TRIBUT_CONFIGURA_OF_GT (ID_TRIBUT_OPERACAO_FISCAL);
CREATE INDEX FK_GRUPO_TRIB_CONFIGURA ON TRIBUT_CONFIGURA_OF_GT (ID_TRIBUT_GRUPO_TRIBUTARIO);



-- ------------------------------------------------------------
-- Esta tabela irá armazenar as tributações de IPI usadas pelas empresas.
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_IPI_DIPI (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_CONFIGURA_OF_GT INTEGER   NOT NULL ,
  ID_TIPO_RECEITA_DIPI INTEGER    ,
  CST_IPI CHAR(2)    ,
  MODALIDADE_BASE_CALCULO CHAR(1)    ,
  PORCENTO_BASE_CALCULO DECIMAL(18,6)    ,
  ALIQUOTA_PORCENTO DECIMAL(18,6)    ,
  ALIQUOTA_UNIDADE DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO DECIMAL(18,6)    ,
  VALOR_PAUTA_FISCAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_TIPO_RECEITA_DIPI)
    REFERENCES TIPO_RECEITA_DIPI(ID),
  FOREIGN KEY(ID_TRIBUT_CONFIGURA_OF_GT)
    REFERENCES TRIBUT_CONFIGURA_OF_GT(ID));


CREATE INDEX FK_IPI_RECEITA_DIPI ON TRIBUT_IPI_DIPI (ID_TIPO_RECEITA_DIPI);
CREATE INDEX FK_CONFIG_OF_GT_IPI ON TRIBUT_IPI_DIPI (ID_TRIBUT_CONFIGURA_OF_GT);




-- ------------------------------------------------------------
-- Armazena as condições de pagamento.
-- ------------------------------------------------------------

CREATE TABLE VENDA_CONDICOES_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  ID_FIN_TIPO_RECEBIMENTO INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  NOME VARCHAR(50)    ,
  DESCRICAO TEXT    ,
  FATURAMENTO_MINIMO DECIMAL(18,6)    ,
  FATURAMENTO_MAXIMO DECIMAL(18,6)    ,
  INDICE_CORRECAO DECIMAL(18,6)    ,
  DIAS_TOLERANCIA INTEGER    ,
  VALOR_TOLERANCIA DECIMAL(18,6)    ,
  PRAZO_MEDIO INTEGER    ,
  VISTA_PRAZO CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_FIN_TIPO_RECEBIMENTO)
    REFERENCES FIN_TIPO_RECEBIMENTO(ID));


CREATE INDEX FK_EMPRESA_COND_PGTO ON VENDA_CONDICOES_PAGAMENTO (ID_EMPRESA);
CREATE INDEX FK_TIPO_REC_COND_PAG ON VENDA_CONDICOES_PAGAMENTO (ID_FIN_TIPO_RECEBIMENTO);












-- ------------------------------------------------------------
-- Uma pessoa pode pertencer a várias empresas
-- ------------------------------------------------------------

CREATE TABLE EMPRESA_PESSOA (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  RESPONSAVEL_LEGAL CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID));


CREATE INDEX FK_EMPRESA_PESSOA ON EMPRESA_PESSOA (ID_EMPRESA);
CREATE INDEX FK_PESSOA_EMPRESA ON EMPRESA_PESSOA (ID_PESSOA);




-- ------------------------------------------------------------
-- Armazena as informações dos CNAE da empresa, inclusive marcando qual dele é o principal.
-- ------------------------------------------------------------

CREATE TABLE EMPRESA_CNAE (
  ID SERIAL  NOT NULL ,
  ID_CNAE INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  PRINCIPAL CHAR(1)    ,
  RAMO_ATIVIDADE VARCHAR(50)    ,
  OBJETO_SOCIAL TEXT      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_CNAE)
    REFERENCES CNAE(ID));


CREATE INDEX FK_EMPRESA_CNAE ON EMPRESA_CNAE (ID_EMPRESA);
CREATE INDEX FK_CNAE_EMPRESA ON EMPRESA_CNAE (ID_CNAE);




-- ------------------------------------------------------------
-- Tabela de lançamentos das contas a pagar.
-- ------------------------------------------------------------

CREATE TABLE FIN_LANCAMENTO_PAGAR (
  ID                       SERIAL  NOT NULL ,
  ID_FIN_DOCUMENTO_ORIGEM  INTEGER NOT NULL ,
  ID_EMPRESA               INTEGER NOT NULL,
  ID_FORNECEDOR            INTEGER NOT NULL ,
  PAGAMENTO_COMPARTILHADO  CHAR(1)    ,
  QUANTIDADE_PARCELA       INTEGER    ,
  VALOR_TOTAL              DECIMAL(18,6)    ,
  VALOR_A_PAGAR            DECIMAL(18,6)    ,
  DATA_LANCAMENTO          DATE    ,
  NUMERO_DOCUMENTO         VARCHAR(50)    ,
  IMAGEM_DOCUMENTO         TEXT    ,
  PRIMEIRO_VENCIMENTO      DATE    ,
  CODIGO_MODULO_LCTO       CHAR(3)    ,
  INTERVALO_ENTRE_PARCELAS INTEGER    ,
  MESCLADO_PARA            INTEGER    ,
  HASH_MODULO_LCTO         VARCHAR(32)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_FIN_DOCUMENTO_ORIGEM)
    REFERENCES FIN_DOCUMENTO_ORIGEM(ID),
  FOREIGN KEY (ID_EMPRESA)
  REFERENCES EMPRESA (ID),
  FOREIGN KEY(ID_FORNECEDOR)
    REFERENCES FORNECEDOR(ID));


CREATE INDEX FK_DOC_ORIG_LCTO_PAGAR ON FIN_LANCAMENTO_PAGAR (ID_FIN_DOCUMENTO_ORIGEM);
CREATE INDEX FK_FORNECEDOR_LCTO_PAGAR ON FIN_LANCAMENTO_PAGAR (ID_FORNECEDOR);
CREATE INDEX FK_EMPRESA_LCTO_PAGAR
  ON FIN_LANCAMENTO_PAGAR (ID_EMPRESA);










-- ------------------------------------------------------------
-- Armazena as configurações dos boletos.
-- ------------------------------------------------------------

CREATE TABLE FIN_CONFIGURACAO_BOLETO (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  INSTRUCAO01 VARCHAR(100)    ,
  INSTRUCAO02 VARCHAR(100)    ,
  CAMINHO_ARQUIVO_REMESSA VARCHAR(250)    ,
  CAMINHO_ARQUIVO_RETORNO VARCHAR(250)    ,
  CAMINHO_ARQUIVO_LOGOTIPO VARCHAR(250)    ,
  CAMINHO_ARQUIVO_PDF VARCHAR(250)    ,
  MENSAGEM VARCHAR(250)    ,
  LOCAL_PAGAMENTO VARCHAR(100)    ,
  LAYOUT_REMESSA CHAR(3)    ,
  ACEITE CHAR(1)    ,
  ESPECIE CHAR(2)    ,
  CARTEIRA CHAR(3)    ,
  CODIGO_CONVENIO VARCHAR(20)    ,
  CODIGO_CEDENTE VARCHAR(20)    ,
  TAXA_MULTA DECIMAL(18,6)    ,
  TAXA_JURO DECIMAL(18,6)    ,
  DIAS_PROTESTO INTEGER    ,
  NOSSO_NUMERO_ANTERIOR VARCHAR(50)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_EMPRESA_CONF_BOLETO ON FIN_CONFIGURACAO_BOLETO (ID_EMPRESA);
CREATE INDEX FK_CTA_CX_CONF_BOLETO ON FIN_CONFIGURACAO_BOLETO (ID_CONTA_CAIXA);















-- ------------------------------------------------------------
-- Armazenar os dados dos cheques recebidos identicando a pessoa que emitiu o cheque, estando cadastrada no BD ou não.
-- ------------------------------------------------------------

CREATE TABLE FIN_CHEQUE_RECEBIDO (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  ID_PESSOA INTEGER    ,
  CPF_CNPJ VARCHAR(14)    ,
  NOME VARCHAR(100)    ,
  CODIGO_BANCO VARCHAR(10)    ,
  CODIGO_AGENCIA VARCHAR(10)    ,
  CONTA VARCHAR(20)    ,
  NUMERO INTEGER    ,
  DATA_EMISSAO DATE    ,
  BOM_PARA DATE    ,
  DATA_COMPENSACAO DATE    ,
  VALOR DECIMAL(18,6)    ,
  CUSTODIA_DATA DATE    ,
  CUSTODIA_TARIFA DECIMAL(18,6)    ,
  CUSTODIA_COMISSAO DECIMAL(18,6)    ,
  DESCONTO_DATA DATE    ,
  DESCONTO_TARIFA DECIMAL(18,6)    ,
  DESCONTO_COMISSAO DECIMAL(18,6)    ,
  VALOR_RECEBIDO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_PESSOA_EMIT_CHEQUE ON FIN_CHEQUE_RECEBIDO (ID_PESSOA);
CREATE INDEX FK_CONTA_CAIXA_CHEQUE_REC ON FIN_CHEQUE_RECEBIDO (ID_CONTA_CAIXA);










-- ------------------------------------------------------------
-- Uma cotação pode ter vários fornecedores e um fornecedor pode fazer parte de várias cotações.
-- ------------------------------------------------------------

CREATE TABLE COMPRA_FORNECEDOR_COTACAO (
  ID SERIAL  NOT NULL ,
  ID_COMPRA_COTACAO INTEGER   NOT NULL ,
  ID_FORNECEDOR INTEGER   NOT NULL ,
  PRAZO_ENTREGA VARCHAR(30)    ,
  VENDA_CONDICOES_PAGAMENTO VARCHAR(30)    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  TOTAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_COTACAO)
    REFERENCES COMPRA_COTACAO(ID),
  FOREIGN KEY(ID_FORNECEDOR)
    REFERENCES FORNECEDOR(ID));


CREATE INDEX FK_COTACAO_FORNECEDOR ON COMPRA_FORNECEDOR_COTACAO (ID_COMPRA_COTACAO);
CREATE INDEX FK_FORNECEDOR_COTACAO ON COMPRA_FORNECEDOR_COTACAO (ID_FORNECEDOR);



-- ------------------------------------------------------------
-- Tabela que armazena os pedidos de compra.
-- ------------------------------------------------------------

CREATE TABLE COMPRA_PEDIDO (
  ID                       SERIAL  NOT NULL ,
  ID_COMPRA_TIPO_PEDIDO    INTEGER NOT NULL ,
  ID_EMPRESA               INTEGER NOT NULL,
  ID_FORNECEDOR            INTEGER NOT NULL ,
  DATA_PEDIDO              DATE    ,
  DATA_PREVISTA_ENTREGA    DATE    ,
  DATA_PREVISAO_PAGAMENTO  DATE    ,
  LOCAL_ENTREGA            VARCHAR(100)    ,
  LOCAL_COBRANCA           VARCHAR(100)    ,
  CONTATO                  VARCHAR(30)    ,
  VALOR_SUBTOTAL           DECIMAL(18,6)    ,
  TAXA_DESCONTO            DECIMAL(18,6)    ,
  VALOR_DESCONTO           DECIMAL(18,6)    ,
  VALOR_TOTAL_PEDIDO       DECIMAL(18,6)    ,
  TIPO_FRETE               CHAR(1)    ,
  FORMA_PAGAMENTO          CHAR(1)    ,
  BASE_CALCULO_ICMS        DECIMAL(18,6)    ,
  VALOR_ICMS               DECIMAL(18,6)    ,
  BASE_CALCULO_ICMS_ST     DECIMAL(18,6)    ,
  VALOR_ICMS_ST            DECIMAL(18,6)    ,
  VALOR_TOTAL_PRODUTOS     DECIMAL(18,6)    ,
  VALOR_FRETE              DECIMAL(18,6)    ,
  VALOR_SEGURO             DECIMAL(18,6)    ,
  VALOR_OUTRAS_DESPESAS    DECIMAL(18,6)    ,
  VALOR_IPI                DECIMAL(18,6)    ,
  VALOR_TOTAL_NF           DECIMAL(18,6)    ,
  QUANTIDADE_PARCELAS      INTEGER    ,
  DIAS_PRIMEIRO_VENCIMENTO INTEGER    ,
  DIAS_INTERVALO           INTEGER      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_TIPO_PEDIDO)
    REFERENCES COMPRA_TIPO_PEDIDO(ID),
  FOREIGN KEY (ID_EMPRESA)
  REFERENCES EMPRESA (ID),
  FOREIGN KEY(ID_FORNECEDOR)
    REFERENCES FORNECEDOR(ID));


CREATE INDEX FK_TIPO_PEDIDO_COMPRA ON COMPRA_PEDIDO (ID_COMPRA_TIPO_PEDIDO);
CREATE INDEX FK_FORNECEDOR_PEDIDO_COMPRA ON COMPRA_PEDIDO (ID_FORNECEDOR);
CREATE INDEX FK_EMPRESA_PEDIDO_COMPRA
  ON COMPRA_PEDIDO (ID_EMPRESA);





-- ------------------------------------------------------------
-- Tabela de pagamentos fixos. Os registros dessa tabela serão transformados em lançamentos a pagar.
-- ------------------------------------------------------------

CREATE TABLE FIN_PAGAMENTO_FIXO (
  ID SERIAL  NOT NULL ,
  ID_FORNECEDOR INTEGER   NOT NULL ,
  ID_FIN_DOCUMENTO_ORIGEM INTEGER   NOT NULL ,
  PAGAMENTO_COMPARTILHADO CHAR(1)    ,
  QUANTIDADE_PARCELA INTEGER    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  VALOR_A_PAGAR DECIMAL(18,6)    ,
  DATA_LANCAMENTO DATE    ,
  NUMERO_DOCUMENTO VARCHAR(50)    ,
  IMAGEM_DOCUMENTO TEXT    ,
  PRIMEIRO_VENCIMENTO DATE    ,
  INTERVALO_ENTRE_PARCELAS INTEGER      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_FIN_DOCUMENTO_ORIGEM)
    REFERENCES FIN_DOCUMENTO_ORIGEM(ID),
  FOREIGN KEY(ID_FORNECEDOR)
    REFERENCES FORNECEDOR(ID));


CREATE INDEX FK_DOC_ORIG_PAGA_FIXO ON FIN_PAGAMENTO_FIXO (ID_FIN_DOCUMENTO_ORIGEM);
CREATE INDEX FK_FORNECEDOR_PAGA_FIXO ON FIN_PAGAMENTO_FIXO (ID_FORNECEDOR);









-- ------------------------------------------------------------
-- Tabela que garda as parcelas para pagamento. Caso o pagamento seja efetuado de uma vez, a tabela LANCAMENTO_PAGAR gerará uma parcela para ser paga e a mesma será armazenada nesta tabela.
-- ------------------------------------------------------------

CREATE TABLE FIN_PARCELA_PAGAR (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  ID_FIN_LANCAMENTO_PAGAR INTEGER   NOT NULL ,
  ID_FIN_STATUS_PARCELA INTEGER   NOT NULL ,
  NUMERO_PARCELA INTEGER    ,
  DATA_EMISSAO DATE    ,
  DATA_VENCIMENTO DATE    ,
  DESCONTO_ATE DATE    ,
  SOFRE_RETENCAO CHAR(1)    ,
  VALOR DECIMAL(18,6)    ,
  TAXA_JURO DECIMAL(18,6)    ,
  TAXA_MULTA DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_JURO DECIMAL(18,6)    ,
  VALOR_MULTA DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)      ,
PRIMARY KEY(ID)      ,
  FOREIGN KEY(ID_FIN_STATUS_PARCELA)
    REFERENCES FIN_STATUS_PARCELA(ID),
  FOREIGN KEY(ID_FIN_LANCAMENTO_PAGAR)
    REFERENCES FIN_LANCAMENTO_PAGAR(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_STATUS_PARCELA_PAGAR ON FIN_PARCELA_PAGAR (ID_FIN_STATUS_PARCELA);
CREATE INDEX FK_LANCAMENTO_PARCELA ON FIN_PARCELA_PAGAR (ID_FIN_LANCAMENTO_PAGAR);
CREATE INDEX FK_CTA_CX_PAR_PAG ON FIN_PARCELA_PAGAR (ID_CONTA_CAIXA);









-- ------------------------------------------------------------
-- Tabela que armazena os produtos.
-- ------------------------------------------------------------

CREATE TABLE PRODUTO (
  ID SERIAL  NOT NULL ,
  ID_SUBGRUPO INTEGER   NOT NULL ,
  ID_TRIBUT_ICMS_CUSTOM_CAB INTEGER    ,
  ID_UNIDADE_PRODUTO INTEGER   NOT NULL ,
  ID_ALMOXARIFADO INTEGER    ,
  ID_GRUPO_TRIBUTARIO INTEGER    ,
  ID_MARCA_PRODUTO INTEGER    ,
  GTIN VARCHAR(14)    ,
  CODIGO_INTERNO VARCHAR(60)    ,
  NCM VARCHAR(8)    ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT    ,
  DESCRICAO_PDV VARCHAR(30)    ,
  VALOR_COMPRA DECIMAL(18,6)    ,
  VALOR_VENDA DECIMAL(18,6)    ,
  PRECO_VENDA_MINIMO DECIMAL(18,6)    ,
  PRECO_SUGERIDO DECIMAL(18,6)    ,
  CUSTO_UNITARIO DECIMAL(18,6)    ,
  CUSTO_PRODUCAO DECIMAL(18,6)    ,
  CUSTO_MEDIO_LIQUIDO DECIMAL(18,6)    ,
  PRECO_LUCRO_ZERO DECIMAL(18,6)    ,
  PRECO_LUCRO_MINIMO DECIMAL(18,6)    ,
  PRECO_LUCRO_MAXIMO DECIMAL(18,6)    ,
  MARKUP DECIMAL(18,6)    ,
  QUANTIDADE_ESTOQUE DECIMAL(18,6)    ,
  QUANTIDADE_ESTOQUE_ANTERIOR DECIMAL(18,6)    ,
  ESTOQUE_MINIMO DECIMAL(18,6)    ,
  ESTOQUE_MAXIMO DECIMAL(18,6)    ,
  ESTOQUE_IDEAL DECIMAL(18,6)    ,
  EXCLUIDO CHAR(1)    ,
  INATIVO CHAR(1)    ,
  DATA_CADASTRO DATE    ,
  IMAGEM TEXT    ,
  EX_TIPI CHAR(3)    ,
  CODIGO_LST CHAR(4)    ,
  CLASSE_ABC CHAR(1)    ,
  IAT CHAR(1)    ,
  IPPT CHAR(1)    ,
  TIPO_ITEM_SPED CHAR(2)    ,
  PESO DECIMAL(18,6)    ,
  PORCENTO_COMISSAO DECIMAL(18,6)    ,
  PONTO_PEDIDO DECIMAL(18,6)    ,
  LOTE_ECONOMICO_COMPRA DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_PAF DECIMAL(18,6)    ,
  ALIQUOTA_ISSQN_PAF DECIMAL(18,6)    ,
  TOTALIZADOR_PARCIAL VARCHAR(10)    ,
  CODIGO_BALANCA INTEGER    ,
  DATA_ALTERACAO DATE    ,
  TIPO CHAR(1)    ,
  SERVICO CHAR(1)    ,
  CEST VARCHAR(10)      ,
PRIMARY KEY(ID)            ,
  FOREIGN KEY(ID_UNIDADE_PRODUTO)
    REFERENCES UNIDADE_PRODUTO(ID),
  FOREIGN KEY(ID_SUBGRUPO)
    REFERENCES PRODUTO_SUBGRUPO(ID),
  FOREIGN KEY(ID_MARCA_PRODUTO)
    REFERENCES PRODUTO_MARCA(ID),
  FOREIGN KEY(ID_GRUPO_TRIBUTARIO)
    REFERENCES TRIBUT_GRUPO_TRIBUTARIO(ID),
  FOREIGN KEY(ID_ALMOXARIFADO)
    REFERENCES ALMOXARIFADO(ID),
  FOREIGN KEY(ID_TRIBUT_ICMS_CUSTOM_CAB)
    REFERENCES TRIBUT_ICMS_CUSTOM_CAB(ID));


CREATE INDEX FK_PRODUTO_UNIDADE ON PRODUTO (ID_UNIDADE_PRODUTO);
CREATE INDEX FK_PRODUTO_SUBGRUPO ON PRODUTO (ID_SUBGRUPO);
CREATE INDEX FK_MARCA_PRODUTO ON PRODUTO (ID_MARCA_PRODUTO);
CREATE INDEX FK_GRUPO_TRIB_PRODUTO ON PRODUTO (ID_GRUPO_TRIBUTARIO);
CREATE INDEX FK_ALMOX_PRODUTO ON PRODUTO (ID_ALMOXARIFADO);
CREATE INDEX FK_ICMS_CUSTOM_PROD ON PRODUTO (ID_TRIBUT_ICMS_CUSTOM_CAB);


























-- ------------------------------------------------------------
-- Tabela com a relação dos clientes.
-- ------------------------------------------------------------

CREATE TABLE CLIENTE (
  ID SERIAL  NOT NULL ,
  ID_REGIAO INTEGER    ,
  ID_TABELA_PRECO INTEGER    ,
  ID_CONVENIO INTEGER    ,
  ID_OPERACAO_FISCAL INTEGER    ,
  ID_PESSOA INTEGER   NOT NULL ,
  ID_ATIVIDADE_FOR_CLI INTEGER   NOT NULL ,
  ID_SITUACAO_FOR_CLI INTEGER   NOT NULL ,
  DESDE DATE    ,
  DATA_CADASTRO DATE    ,
  OBSERVACAO TEXT    ,
  CONTA_TOMADOR VARCHAR(30)    ,
  GERA_FINANCEIRO CHAR(1)    ,
  INDICADOR_PRECO CHAR(1)    ,
  PORCENTO_DESCONTO DECIMAL(18,6)    ,
  FORMA_DESCONTO CHAR(1)    ,
  LIMITE_CREDITO DECIMAL(18,6)    ,
  TIPO_FRETE CHAR(1)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)    ,
  BLOQUEADO CHAR(1)    ,
  DIAS_BLOQUEIO INTEGER      ,
PRIMARY KEY(ID)              ,
  FOREIGN KEY(ID_SITUACAO_FOR_CLI)
    REFERENCES SITUACAO_FOR_CLI(ID),
  FOREIGN KEY(ID_ATIVIDADE_FOR_CLI)
    REFERENCES ATIVIDADE_FOR_CLI(ID),
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID),
  FOREIGN KEY(ID_OPERACAO_FISCAL)
    REFERENCES TRIBUT_OPERACAO_FISCAL(ID),
  FOREIGN KEY(ID_CONVENIO)
    REFERENCES CONVENIO(ID),
  FOREIGN KEY(ID_TABELA_PRECO)
    REFERENCES TABELA_PRECO(ID),
  FOREIGN KEY(ID_REGIAO)
    REFERENCES REGIAO(ID));


CREATE INDEX FK_SITUACAO_CLIENTE ON CLIENTE (ID_SITUACAO_FOR_CLI);
CREATE INDEX FK_ATIVIDADE_CLIENTE ON CLIENTE (ID_ATIVIDADE_FOR_CLI);
CREATE INDEX FK_PESSOA_CLIENTE ON CLIENTE (ID_PESSOA);
CREATE INDEX FK_OP_FISCAL_CLIENTE ON CLIENTE (ID_OPERACAO_FISCAL);
CREATE INDEX FK_CONVENIO_CLIENTE ON CLIENTE (ID_CONVENIO);
CREATE INDEX FK_TABELA_PRECO_CLIENTE ON CLIENTE (ID_TABELA_PRECO);
CREATE INDEX FK_REGIAO_CLIENTE ON CLIENTE (ID_REGIAO);










-- ------------------------------------------------------------
-- Tabela com os colaboradores da empresa.
-- ------------------------------------------------------------

CREATE TABLE COLABORADOR (
  ID SERIAL  NOT NULL ,
  ID_SINDICATO INTEGER    ,
  ID_TIPO_ADMISSAO INTEGER    ,
  ID_SITUACAO_COLABORADOR INTEGER   NOT NULL ,
  ID_PESSOA INTEGER   NOT NULL ,
  ID_TIPO_COLABORADOR INTEGER   NOT NULL ,
  ID_NIVEL_FORMACAO INTEGER   NOT NULL ,
  ID_CARGO INTEGER   NOT NULL ,
  ID_SETOR INTEGER   NOT NULL ,
  MATRICULA VARCHAR(10)    ,
  FOTO_34 TEXT    ,
  DATA_CADASTRO DATE    ,
  DATA_ADMISSAO DATE    ,
  VENCIMENTO_FERIAS DATE    ,
  DATA_TRANSFERENCIA DATE    ,
  FGTS_OPTANTE CHAR(1)    ,
  FGTS_DATA_OPCAO DATE    ,
  FGTS_CONTA INTEGER    ,
  PAGAMENTO_FORMA CHAR(1)    ,
  PAGAMENTO_BANCO VARCHAR(10)    ,
  PAGAMENTO_AGENCIA VARCHAR(10)    ,
  PAGAMENTO_AGENCIA_DIGITO CHAR(1)    ,
  PAGAMENTO_CONTA VARCHAR(10)    ,
  PAGAMENTO_CONTA_DIGITO CHAR(1)    ,
  EXAME_MEDICO_ULTIMO DATE    ,
  EXAME_MEDICO_VENCIMENTO DATE    ,
  PIS_DATA_CADASTRO DATE    ,
  PIS_NUMERO VARCHAR(12)    ,
  PIS_BANCO VARCHAR(10)    ,
  PIS_AGENCIA VARCHAR(10)    ,
  PIS_AGENCIA_DIGITO CHAR(1)    ,
  CTPS_NUMERO VARCHAR(10)    ,
  CTPS_SERIE VARCHAR(10)    ,
  CTPS_DATA_EXPEDICAO DATE    ,
  CTPS_UF CHAR(2)    ,
  DESCONTO_PLANO_SAUDE CHAR(1)    ,
  SAI_NA_RAIS CHAR(1)    ,
  CATEGORIA_SEFIP CHAR(2)    ,
  OBSERVACAO TEXT    ,
  OCORRENCIA_SEFIP INTEGER    ,
  CODIGO_ADMISSAO_CAGED INTEGER    ,
  CODIGO_DEMISSAO_CAGED INTEGER    ,
  CODIGO_DEMISSAO_SEFIP INTEGER    ,
  DATA_DEMISSAO DATE    ,
  CODIGO_TURMA_PONTO CHAR(5)    ,
  CAGED_APRENDIZ CHAR(1)    ,
  CAGED_DEFICIENCIA CHAR(1)    ,
  CLASSIFICACAO_CONTABIL_CONTA VARCHAR(30)      ,
PRIMARY KEY(ID)                ,
  FOREIGN KEY(ID_SETOR)
    REFERENCES SETOR(ID),
  FOREIGN KEY(ID_CARGO)
    REFERENCES CARGO(ID),
  FOREIGN KEY(ID_TIPO_COLABORADOR)
    REFERENCES TIPO_COLABORADOR(ID),
  FOREIGN KEY(ID_NIVEL_FORMACAO)
    REFERENCES NIVEL_FORMACAO(ID),
  FOREIGN KEY(ID_PESSOA)
    REFERENCES PESSOA(ID),
  FOREIGN KEY(ID_SITUACAO_COLABORADOR)
    REFERENCES SITUACAO_COLABORADOR(ID),
  FOREIGN KEY(ID_TIPO_ADMISSAO)
    REFERENCES TIPO_ADMISSAO(ID),
  FOREIGN KEY(ID_SINDICATO)
    REFERENCES SINDICATO(ID));


CREATE INDEX FK_COLABORADOR_SETOR ON COLABORADOR (ID_SETOR);
CREATE INDEX FK_CARGO_COLABORADOR ON COLABORADOR (ID_CARGO);
CREATE INDEX FK_TIPO_COLABORADOR ON COLABORADOR (ID_TIPO_COLABORADOR);
CREATE INDEX FK_NIVEL_FORMACAO_COLABORADOR ON COLABORADOR (ID_NIVEL_FORMACAO);
CREATE INDEX FK_PESSOA_COLABORADOR ON COLABORADOR (ID_PESSOA);
CREATE INDEX FK_SITUACAO_COLABORADOR ON COLABORADOR (ID_SITUACAO_COLABORADOR);
CREATE INDEX FK_TIPO_ADMISSAO_COLAB ON COLABORADOR (ID_TIPO_ADMISSAO);
CREATE INDEX FK_SINDICATO_COLAB ON COLABORADOR (ID_SINDICATO);





-- ------------------------------------------------------------
-- Relação de contas de natureza financeira. 
--  
-- A natureza financeira armazena as contas mais perto do dia-a-dia dos usuarios. No módulo contábil faremos o link delas com as devidas contas contábeis.
-- ------------------------------------------------------------

CREATE TABLE NATUREZA_FINANCEIRA (
  ID                           SERIAL  NOT NULL ,
  ID_PLANO_NATUREZA_FINANCEIRA INTEGER   NOT NULL ,
  ID_NATUREZA_FINANCEIRA       INTEGER,
  CLASSIFICACAO                VARCHAR(30)    ,
  DESCRICAO                    VARCHAR(100)    ,
  TIPO                         CHAR(1)    ,
  APLICACAO                    VARCHAR(250)    ,
  APARECE_A_PAGAR              CHAR(1)    ,
  APARECE_A_RECEBER            CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY (ID_NATUREZA_FINANCEIRA) REFERENCES NATUREZA_FINANCEIRA (ID),
  FOREIGN KEY(ID_PLANO_NATUREZA_FINANCEIRA)
    REFERENCES PLANO_NATUREZA_FINANCEIRA(ID));


CREATE INDEX FK_PLANO_NATUREZA_FINAN ON NATUREZA_FINANCEIRA (ID_PLANO_NATUREZA_FINANCEIRA);
CREATE INDEX FK_NATUREZA_FINAN
  ON NATUREZA_FINANCEIRA (ID_NATUREZA_FINANCEIRA);







-- ------------------------------------------------------------
-- Códigos adicionais para produtos.
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_CODIGO_ADICIONAL (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  CODIGO VARCHAR(14)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_PRODUTO_CODIGO_ADD ON PRODUTO_CODIGO_ADICIONAL (ID_PRODUTO);



-- ------------------------------------------------------------
-- Registro 0205 do Sped Fiscal. Este registro tem por objetivo informar alterações ocorridas na descrição do produto, desde que não o descaracterize ou haja modificação que o identifique como sendo novo produto. Caso não tenha ocorrido movimentação no período da alteração do item, deverá ser informada no primeiro período em que houver movimentação do item ou no inventário. Deverá ser ainda informado quando ocorrer alteração na codificação do produto.
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_ALTERACAO_ITEM (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  CODIGO VARCHAR(14)    ,
  NOME VARCHAR(100)    ,
  DATA_INICIAL DATE    ,
  DATA_FINAL DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_PRODUTO_ALTERACAO_ITEM ON PRODUTO_ALTERACAO_ITEM (ID_PRODUTO);







-- ------------------------------------------------------------
-- Tabela de promoções. 
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_PROMOCAO (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  DATA_INICIO DATE    ,
  DATA_FIM DATE    ,
  QUANTIDADE_EM_PROMOCAO DECIMAL(18,6)    ,
  QUANTIDADE_MAXIMA_CLIENTE DECIMAL(18,6)    ,
  VALOR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_PRODUTO_PROMOCAO ON PRODUTO_PROMOCAO (ID_PRODUTO);



-- ------------------------------------------------------------
-- Tabela para armazenar os dados das requisições internas de produto.
-- ------------------------------------------------------------

CREATE TABLE REQUISICAO_INTERNA_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_COLABORADOR INTEGER   NOT NULL ,
  DATA_REQUISICAO DATE   NOT NULL ,
  SITUACAO CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID));


CREATE INDEX FK_COLAB_REQ_INTERNA ON REQUISICAO_INTERNA_CABECALHO (ID_COLABORADOR);




CREATE TABLE CTE_DOCUMENTO_ANTERIOR (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  IE VARCHAR(20)    ,
  UF CHAR(2)    ,
  NOME VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_DOC_ANTERIOR ON CTE_DOCUMENTO_ANTERIOR (ID_CTE_CABECALHO);





-- ------------------------------------------------------------
-- Informações das duplicatas.
-- ------------------------------------------------------------

CREATE TABLE CTE_DUPLICATA (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  NUMERO VARCHAR(60)    ,
  DATA_VENCIMENTO DATE    ,
  VALOR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_DUPLICATA ON CTE_DUPLICATA (ID_CTE_CABECALHO);






CREATE TABLE CTE_EMITENTE (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  IE VARCHAR(14)    ,
  IEST VARCHAR(14)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  TELEFONE VARCHAR(14)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_EMITENTE ON CTE_EMITENTE (ID_CTE_CABECALHO);















CREATE TABLE CTE_DUTOVIARIO (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  VALOR_TARIFA DECIMAL(18,6)    ,
  DATA_INICIO DATE    ,
  DATA_FIM DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_DUTO ON CTE_DUTOVIARIO (ID_CTE_CABECALHO);




CREATE TABLE CTE_CARGA (
  ID SERIAL  NOT NULL ,
  ID_CTE_CABECALHO INTEGER   NOT NULL ,
  CODIGO_UNIDADE_MEDIDA CHAR(2)    ,
  TIPO_MEDIDA VARCHAR(20)    ,
  QUANTIDADE DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_CABECALHO)
    REFERENCES CTE_CABECALHO(ID));


CREATE INDEX FK_CTE_CAB_CARGA ON CTE_CARGA (ID_CTE_CABECALHO);






CREATE TABLE CTE_INFORMACAO_NF_TRANSPORTE (
  ID SERIAL  NOT NULL ,
  ID_CTE_INFORMACAO_NF INTEGER   NOT NULL ,
  TIPO_UNIDADE_TRANSPORTE INTEGER    ,
  ID_UNIDADE_TRANSPORTE VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_INFORMACAO_NF)
    REFERENCES CTE_INFORMACAO_NF_OUTROS(ID));


CREATE INDEX FK_CTE_INF_NF_TRANSP ON CTE_INFORMACAO_NF_TRANSPORTE (ID_CTE_INFORMACAO_NF);





CREATE TABLE CTE_INF_NF_CARGA_LACRE (
  ID SERIAL  NOT NULL ,
  ID_CTE_INFORMACAO_NF_CARGA INTEGER   NOT NULL ,
  NUMERO VARCHAR(20)    ,
  QUANTIDADE_RATEADA DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_INFORMACAO_NF_CARGA)
    REFERENCES CTE_INFORMACAO_NF_CARGA(ID));


CREATE INDEX FK_CTE_INF_CARGA_LACRE ON CTE_INF_NF_CARGA_LACRE (ID_CTE_INFORMACAO_NF_CARGA);





CREATE TABLE CTE_INF_NF_TRANSPORTE_LACRE (
  ID SERIAL  NOT NULL ,
  ID_CTE_INFORMACAO_NF_TRANSP INTEGER   NOT NULL ,
  NUMERO VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_INFORMACAO_NF_TRANSP)
    REFERENCES CTE_INFORMACAO_NF_TRANSPORTE(ID));


CREATE INDEX FK_CTE_INF_NF_TRANS_LACRE ON CTE_INF_NF_TRANSPORTE_LACRE (ID_CTE_INFORMACAO_NF_TRANSP);




-- ------------------------------------------------------------
-- Tabela com a relação dos cheques vinculados a determinado talão.
-- ------------------------------------------------------------

CREATE TABLE CHEQUE (
  ID SERIAL  NOT NULL ,
  ID_TALONARIO_CHEQUE INTEGER   NOT NULL ,
  NUMERO INTEGER    ,
  STATUS_CHEQUE CHAR(1)    ,
  DATA_STATUS DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_TALONARIO_CHEQUE)
    REFERENCES TALONARIO_CHEQUE(ID));


CREATE INDEX FK_TALONARIO_CHEQUE ON CHEQUE (ID_TALONARIO_CHEQUE);




-- ------------------------------------------------------------
-- Armazena os dados de romaneio de entrega.
-- ------------------------------------------------------------

CREATE TABLE VENDA_ROMANEIO_ENTREGA (
  ID SERIAL  NOT NULL ,
  ID_COLABORADOR INTEGER   NOT NULL ,
  DESCRICAO VARCHAR(100)    ,
  DATA_EMISSAO DATE    ,
  DATA_PREVISTA DATE    ,
  DATA_SAIDA DATE    ,
  SITUACAO CHAR(1)    ,
  DATA_ENCERRAMENTO DATE    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID));


CREATE INDEX FK_COLABORADOR_ROMANEIO ON VENDA_ROMANEIO_ENTREGA (ID_COLABORADOR);




-- ------------------------------------------------------------
-- Tabela matriz para geração de parcelas.
-- ------------------------------------------------------------

CREATE TABLE VENDA_CONDICOES_PARCELAS (
  ID SERIAL  NOT NULL ,
  ID_VENDA_CONDICOES_PAGAMENTO INTEGER   NOT NULL ,
  PARCELA INTEGER    ,
  DIAS INTEGER    ,
  TAXA DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_VENDA_CONDICOES_PAGAMENTO)
    REFERENCES VENDA_CONDICOES_PAGAMENTO(ID));


CREATE INDEX FK_CONDICOES_PARCELAS ON VENDA_CONDICOES_PARCELAS (ID_VENDA_CONDICOES_PAGAMENTO);






CREATE TABLE CTE_AQUAVIARIO_BALSA (
  ID SERIAL  NOT NULL ,
  ID_CTE_AQUAVIARIO INTEGER   NOT NULL ,
  ID_BALSA VARCHAR(60)    ,
  NUMERO_VIAGEM INTEGER    ,
  DIRECAO CHAR(1)    ,
  PORTO_EMBARQUE VARCHAR(60)    ,
  PORTO_TRANSBORDO VARCHAR(60)    ,
  PORTO_DESTINO VARCHAR(60)    ,
  TIPO_NAVEGACAO INTEGER    ,
  IRIN VARCHAR(10)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_AQUAVIARIO)
    REFERENCES CTE_AQUAVIARIO(ID));


CREATE INDEX FK_CTE_AQUA_BALSA ON CTE_AQUAVIARIO_BALSA (ID_CTE_AQUAVIARIO);











-- ------------------------------------------------------------
-- O objetivo é relacionar para cada CST_PIS um codigo de apuração conforme a tabela acima, informando ainda a modalidade calculo do PIS que pode ser por percentual ou por unidade de produto.
-- Com esta tabela podemos armazenar as inúmeras formas de calculo, combinada com suas respectivas alíquotas e CST_PIS possíves e imagináveis que existem ou venha a existir na nossa complexa legislação.
-- Desta forma, podemos fazer este vínculo em TABELA DE GRUPO_TRIBUTACAO e depois vincular este ID_GRUPO_TRIBUTACAO no cadastro de cada um dos produtosp ossibilitando um calculo totalmente automatizado em todo o sistema.
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_PIS_COD_APURACAO (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_CONFIGURA_OF_GT INTEGER   NOT NULL ,
  CST_PIS CHAR(2)    ,
  EFD_TABELA_435 CHAR(2)    ,
  MODALIDADE_BASE_CALCULO CHAR(1)    ,
  PORCENTO_BASE_CALCULO DECIMAL(18,6)    ,
  ALIQUOTA_PORCENTO DECIMAL(18,6)    ,
  ALIQUOTA_UNIDADE DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO DECIMAL(18,6)    ,
  VALOR_PAUTA_FISCAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_TRIBUT_CONFIGURA_OF_GT)
    REFERENCES TRIBUT_CONFIGURA_OF_GT(ID));


CREATE INDEX FK_CONFIG_OF_GT_PIS ON TRIBUT_PIS_COD_APURACAO (ID_TRIBUT_CONFIGURA_OF_GT);






-- ------------------------------------------------------------
-- O objetivo é relacionar para cada CST_COFINS um codigo de apuração, informando ainda a modalidade calculo do COFINS que pode ser por percentual ou por unidade de produto.
-- Com esta tabela podemos armazenar as inúmeras formas de calculo, combinada com suas respectivas alíquotas e CST_COFINS possíves e imagináveis que existem ou venha a existir na nossa complexa legislação.
-- Desta forma, podemos fazer este vínculo em TABELA DE GRUPO_TRIBUTACAO e depois vincular este ID_GRUPO_TRIBUTACAO no cadastro de cada um dos produtos possibilitando um calculo totalmente automatizado em todo o sistema.
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_COFINS_COD_APURACAO (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_CONFIGURA_OF_GT INTEGER   NOT NULL ,
  CST_COFINS CHAR(2)    ,
  EFD_TABELA_435 CHAR(2)    ,
  MODALIDADE_BASE_CALCULO CHAR(1)    ,
  PORCENTO_BASE_CALCULO DECIMAL(18,6)    ,
  ALIQUOTA_PORCENTO DECIMAL(18,6)    ,
  ALIQUOTA_UNIDADE DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO DECIMAL(18,6)    ,
  VALOR_PAUTA_FISCAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_TRIBUT_CONFIGURA_OF_GT)
    REFERENCES TRIBUT_CONFIGURA_OF_GT(ID));


CREATE INDEX FK_CONFIG_OF_GT_COFINS ON TRIBUT_COFINS_COD_APURACAO (ID_TRIBUT_CONFIGURA_OF_GT);






-- ------------------------------------------------------------
-- Tabela que faz o controle dos cheque emitidos.
-- ------------------------------------------------------------

CREATE TABLE FIN_CHEQUE_EMITIDO (
  ID SERIAL  NOT NULL ,
  ID_CHEQUE INTEGER   NOT NULL ,
  DATA_EMISSAO DATE    ,
  BOM_PARA DATE    ,
  DATA_COMPENSACAO DATE    ,
  VALOR DECIMAL(18,6)    ,
  NOMINAL_A VARCHAR(100)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CHEQUE)
    REFERENCES CHEQUE(ID));


CREATE INDEX FK_CHEQUE_EMITIDO ON FIN_CHEQUE_EMITIDO (ID_CHEQUE);






CREATE TABLE FIN_COBRANCA (
  ID SERIAL  NOT NULL ,
  ID_CLIENTE INTEGER   NOT NULL ,
  DATA_CONTATO DATE    ,
  HORA_CONTATO VARCHAR(8)    ,
  TELEFONE_CONTATO VARCHAR(14)    ,
  DATA_ACERTO_PAGAMENTO DATE    ,
  TOTAL_RECEBER_NA_DATA DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CLIENTE)
    REFERENCES CLIENTE(ID));


CREATE INDEX FK_CLIENTE_COBRANCA ON FIN_COBRANCA (ID_CLIENTE);








CREATE TABLE FIN_COBRANCA_PARCELA_RECEBER (
  ID SERIAL  NOT NULL ,
  ID_FIN_COBRANCA INTEGER   NOT NULL ,
  ID_FIN_LANCAMENTO_RECEBER INTEGER    ,
  ID_FIN_PARCELA_RECEBER INTEGER    ,
  DATA_VENCIMENTO DATE    ,
  VALOR_PARCELA DECIMAL(18,6)    ,
  VALOR_JURO_SIMULADO DECIMAL(18,6)    ,
  VALOR_MULTA_SIMULADO DECIMAL(18,6)    ,
  VALOR_RECEBER_SIMULADO DECIMAL(18,6)    ,
  VALOR_JURO_ACORDO DECIMAL(18,6)    ,
  VALOR_MULTA_ACORDO DECIMAL(18,6)    ,
  VALOR_RECEBER_ACORDO DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_FIN_COBRANCA)
    REFERENCES FIN_COBRANCA(ID));


CREATE INDEX FK_COBRANCA_PARCELA ON FIN_COBRANCA_PARCELA_RECEBER (ID_FIN_COBRANCA);











-- ------------------------------------------------------------
-- Tabela de ficha técnica para produtos produzidos pelo proprio estabelecimento.
-- ------------------------------------------------------------

CREATE TABLE FICHA_TECNICA (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  DESCRICAO VARCHAR(100)    ,
  ID_PRODUTO_FILHO INTEGER    ,
  QUANTIDADE DECIMAL(18,6)    ,
  SEQUENCIA_PRODUCAO INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_PRODUTO_FICHA_TECNICA ON FICHA_TECNICA (ID_PRODUTO);



-- ------------------------------------------------------------
-- Tabela para armazenar o histórico de reajustes realizados.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_REAJUSTE_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_COLABORADOR INTEGER   NOT NULL ,
  DATA_REAJUSTE DATE    ,
  PORCENTAGEM DECIMAL(18,6)    ,
  TIPO_REAJUSTE CHAR(1)    ,
  JUSTIFICATIVA VARCHAR(100)    ,
  QUANTIDADE_FIXA DECIMAL(18,6)    ,
  CAMPO_REAJUSTE CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID));


CREATE INDEX FK_COLAB_HIST_REAJUSTE ON ESTOQUE_REAJUSTE_CABECALHO (ID_COLABORADOR);





-- ------------------------------------------------------------
-- Esta tabela ira armazenar a tributaçao para cada UF em cada GRUPO TRIBUTARIO dentro de cada operação cadastrada.
-- ------------------------------------------------------------

CREATE TABLE TRIBUT_ICMS_UF (
  ID SERIAL  NOT NULL ,
  ID_TRIBUT_CONFIGURA_OF_GT INTEGER   NOT NULL ,
  UF_DESTINO CHAR(2)    ,
  CFOP INTEGER    ,
  CSOSN_B CHAR(3)    ,
  CST_B CHAR(2)    ,
  MODALIDADE_BC CHAR(1)    ,
  ALIQUOTA DECIMAL(18,6)    ,
  VALOR_PAUTA DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO DECIMAL(18,6)    ,
  MVA DECIMAL(18,6)    ,
  PORCENTO_BC DECIMAL(18,6)    ,
  MODALIDADE_BC_ST CHAR(1)    ,
  ALIQUOTA_INTERNA_ST DECIMAL(18,6)    ,
  ALIQUOTA_INTERESTADUAL_ST DECIMAL(18,6)    ,
  PORCENTO_BC_ST DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_ST DECIMAL(18,6)    ,
  VALOR_PAUTA_ST DECIMAL(18,6)    ,
  VALOR_PRECO_MAXIMO_ST DECIMAL(18,6)    ,
  FCP DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_TRIBUT_CONFIGURA_OF_GT)
    REFERENCES TRIBUT_CONFIGURA_OF_GT(ID));


CREATE INDEX FK_CONFIG_OF_GT_ICMS ON TRIBUT_ICMS_UF (ID_TRIBUT_CONFIGURA_OF_GT);




















-- ------------------------------------------------------------
-- A vinculação do lacre para cada produto que entra na empresa
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_LACRE_ENTRADA (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  ID_LACRE_PRODUTO INTEGER   NOT NULL   ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_LACRE_PRODUTO)
    REFERENCES PRODUTO_LACRE(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_LACRE_PRODUTO_ENTRADA ON PRODUTO_LACRE_ENTRADA (ID_LACRE_PRODUTO);
CREATE INDEX FK_PRODUTO_LACRE_ENTRADA ON PRODUTO_LACRE_ENTRADA (ID_PRODUTO);



-- ------------------------------------------------------------
-- Vincula a tabela de preço aos produtos que fazem parte dela.
-- ------------------------------------------------------------

CREATE TABLE TABELA_PRECO_PRODUTO (
  ID SERIAL  NOT NULL ,
  ID_TABELA_PRECO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  PRECO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_TABELA_PRECO)
    REFERENCES TABELA_PRECO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_TABELA_PRECO ON TABELA_PRECO_PRODUTO (ID_TABELA_PRECO);
CREATE INDEX FK_PRODUTO_PRECO ON TABELA_PRECO_PRODUTO (ID_PRODUTO);



-- ------------------------------------------------------------
-- Tabela que armazena as unidades secundárias vinculadas a uma unidade principal e a tabela de produtos ja com o fator de conversão.
-- ------------------------------------------------------------

CREATE TABLE UNIDADE_CONVERSAO (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  ID_UNIDADE_PRODUTO INTEGER   NOT NULL ,
  SIGLA VARCHAR(10)    ,
  DESCRICAO TEXT    ,
  FATOR_CONVERSAO DECIMAL(18,6)    ,
  ACAO CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_UNIDADE_PRODUTO)
    REFERENCES UNIDADE_PRODUTO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_UNIDADE_CONVERSAO ON UNIDADE_CONVERSAO (ID_UNIDADE_PRODUTO);
CREATE INDEX FK_PRODUTO_CONVERSAO_UND ON UNIDADE_CONVERSAO (ID_PRODUTO);




-- ------------------------------------------------------------
-- Tabela para armazenar os itens das requisições internas de produtos.
-- ------------------------------------------------------------

CREATE TABLE REQUISICAO_INTERNA_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_REQ_INTERNA_CABECALHO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  QUANTIDADE DECIMAL(18,6)   NOT NULL   ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_REQ_INTERNA_CABECALHO)
    REFERENCES REQUISICAO_INTERNA_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_REQ_INTERNA_CAB_DET ON REQUISICAO_INTERNA_DETALHE (ID_REQ_INTERNA_CABECALHO);
CREATE INDEX FK_PRODUTO_REQ_INTERNA ON REQUISICAO_INTERNA_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Tabela com os usuarios do sistema
-- ------------------------------------------------------------

CREATE TABLE USUARIO (
  ID             SERIAL  NOT NULL ,
  ID_COLABORADOR INTEGER   NOT NULL ,
  ID_PAPEL       INTEGER   NOT NULL ,
  LOGIN          VARCHAR(20)    ,
  SENHA          VARCHAR(100),
  DATA_CADASTRO  DATE    ,
  ADMINISTRADOR  CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID),
  FOREIGN KEY(ID_PAPEL)
    REFERENCES PAPEL(ID));


CREATE INDEX FK_COLABORADOR_USUARIO ON USUARIO (ID_COLABORADOR);
CREATE INDEX FK_PAPEL_USUARIO ON USUARIO (ID_PAPEL);



CREATE TABLE PRODUTO_COMBO_ITEM (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO_COMBO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  TAXA_DESCONTO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID),
  FOREIGN KEY(ID_PRODUTO_COMBO)
    REFERENCES PRODUTO_COMBO(ID));


CREATE INDEX FK_PROD_COMBO_ITEM ON PRODUTO_COMBO_ITEM (ID_PRODUTO);
CREATE INDEX FK_COMBO_PROD_ITEM ON PRODUTO_COMBO_ITEM (ID_PRODUTO_COMBO);




CREATE TABLE EMPRESA_PRODUTO (
  ID                 SERIAL  NOT NULL ,
  ID_PRODUTO         INTEGER   NOT NULL ,
  ID_EMPRESA         INTEGER   NOT NULL ,
  QUANTIDADE_ESTOQUE DECIMAL(18,6)      ,
  CONTROLE           DECIMAL(18, 6),
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_PRODUTO_EMPRESA ON EMPRESA_PRODUTO (ID_PRODUTO);
CREATE INDEX FK_EMPRESA_PRODUTO ON EMPRESA_PRODUTO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Armazena os detalhes do DAV.
-- ------------------------------------------------------------

CREATE TABLE DAV_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  ID_DAV_CABECALHO INTEGER   NOT NULL ,
  NUMERO_DAV VARCHAR(10)    ,
  DATA_EMISSAO DATE    ,
  ITEM INTEGER    ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  CANCELADO CHAR(1)    ,
  MESCLA_PRODUTO CHAR(1)    ,
  GTIN_PRODUTO VARCHAR(14)    ,
  NOME_PRODUTO VARCHAR(100)    ,
  UNIDADE_PRODUTO VARCHAR(10)    ,
  TOTALIZADOR_PARCIAL VARCHAR(10)    ,
  LOGSS VARCHAR(32)    ,
  SERVICO_FORMULA TEXT      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_DAV_CABECALHO)
    REFERENCES DAV_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_DAV_CAB_DET ON DAV_DETALHE (ID_DAV_CABECALHO);
CREATE INDEX FK_PRODUTO_DAV_DETALHE ON DAV_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Tabela para armazenar os produtos que sofreram reajuste.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_REAJUSTE_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_ESTOQUE_REAJUSTE_CABECALHO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  VALOR_ORIGINAL DECIMAL(18,6)    ,
  VALOR_REAJUSTE DECIMAL(18,6)    ,
  QUANTIDADE_ORIGINAL DECIMAL(18,6)    ,
  QUANTIDADE_REAJUSTE DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_ESTOQUE_REAJUSTE_CABECALHO)
    REFERENCES ESTOQUE_REAJUSTE_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_ESTOQUE_REAJUSTE_CAB_DET ON ESTOQUE_REAJUSTE_DETALHE (ID_ESTOQUE_REAJUSTE_CABECALHO);
CREATE INDEX FK_PRODUTO_REAJUSTE ON ESTOQUE_REAJUSTE_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Tabela de lançamentos das contas a receber.
-- ------------------------------------------------------------

CREATE TABLE FIN_LANCAMENTO_RECEBER (
  ID                       SERIAL  NOT NULL ,
  ID_FIN_DOCUMENTO_ORIGEM  INTEGER NOT NULL ,
  ID_EMPRESA               INTEGER NOT NULL,
  ID_CLIENTE               INTEGER NOT NULL ,
  QUANTIDADE_PARCELA       INTEGER    ,
  VALOR_TOTAL              DECIMAL(18,6)    ,
  VALOR_DESCONTO_CONVENIO  DECIMAL(18,6)    ,
  VALOR_A_RECEBER          DECIMAL(18,6)    ,
  DATA_LANCAMENTO          DATE    ,
  NUMERO_DOCUMENTO         VARCHAR(50)    ,
  PRIMEIRO_VENCIMENTO      DATE    ,
  TAXA_COMISSAO            DECIMAL(18,6)    ,
  VALOR_COMISSAO           DECIMAL(18,6)    ,
  INTERVALO_ENTRE_PARCELAS INTEGER    ,
  CODIGO_MODULO_LCTO       CHAR(3)    ,
  MESCLADO_PARA            INTEGER    ,
  HASH_MODULO_LCTO         VARCHAR(32)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_FIN_DOCUMENTO_ORIGEM)
    REFERENCES FIN_DOCUMENTO_ORIGEM(ID),
  FOREIGN KEY (ID_EMPRESA)
  REFERENCES EMPRESA (ID),
  FOREIGN KEY(ID_CLIENTE)
    REFERENCES CLIENTE(ID));


CREATE INDEX FK_DOC_ORI_LANC_RECEBER ON FIN_LANCAMENTO_RECEBER (ID_FIN_DOCUMENTO_ORIGEM);
CREATE INDEX FK_CLIENTE_LCTO_RECEBER ON FIN_LANCAMENTO_RECEBER (ID_CLIENTE);
CREATE INDEX FK_EMPRESA_LCTO_RECEBER
  ON FIN_LANCAMENTO_RECEBER (ID_EMPRESA);








CREATE TABLE FIN_CLIENTE_GRUPO_DET (
  ID SERIAL  NOT NULL ,
  ID_CLIENTE INTEGER   NOT NULL ,
  ID_FIN_CLIENTE_GRUPO_CAB INTEGER   NOT NULL ,
  DIA_LANCAMENTO CHAR(2)    ,
  VALOR DECIMAL(18,6)    ,
  GERA_BOLETO CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_FIN_CLIENTE_GRUPO_CAB)
    REFERENCES FIN_CLIENTE_GRUPO_CAB(ID),
  FOREIGN KEY(ID_CLIENTE)
    REFERENCES CLIENTE(ID));


CREATE INDEX FK_FIN_CLIENTE_GRUPO ON FIN_CLIENTE_GRUPO_DET (ID_FIN_CLIENTE_GRUPO_CAB);
CREATE INDEX FK_CLIENTE_FIN_CLIENTE_GRUPO ON FIN_CLIENTE_GRUPO_DET (ID_CLIENTE);






-- ------------------------------------------------------------
-- Tabela com os relacionamentos dos colaboradores.
-- ------------------------------------------------------------

CREATE TABLE COLABORADOR_RELACIONAMENTO (
  ID SERIAL  NOT NULL ,
  ID_COLABORADOR INTEGER   NOT NULL ,
  ID_TIPO_RELACIONAMENTO INTEGER   NOT NULL ,
  NOME VARCHAR(100)    ,
  DATA_NASCIMENTO DATE    ,
  CPF VARCHAR(11)    ,
  REGISTRO_MATRICULA VARCHAR(50)    ,
  REGISTRO_CARTORIO VARCHAR(50)    ,
  REGISTRO_CARTORIO_NUMERO VARCHAR(50)    ,
  REGISTRO_NUMERO_LIVRO VARCHAR(10)    ,
  REGISTRO_NUMERO_FOLHA VARCHAR(10)    ,
  DATA_ENTREGA_DOCUMENTO DATE    ,
  SALARIO_FAMILIA CHAR(1)    ,
  SALARIO_FAMILIA_IDADE_LIMITE INTEGER    ,
  SALARIO_FAMILIA_DATA_FIM DATE    ,
  IMPOSTO_RENDA_IDADE_LIMITE INTEGER    ,
  IMPOSTO_RENDA_DATA_FIM INTEGER      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID),
  FOREIGN KEY(ID_TIPO_RELACIONAMENTO)
    REFERENCES TIPO_RELACIONAMENTO(ID));


CREATE INDEX FK_COLABORADOR_RELACIONAMENTO ON COLABORADOR_RELACIONAMENTO (ID_COLABORADOR);
CREATE INDEX FK_TIPO_REL_COLABORADOR ON COLABORADOR_RELACIONAMENTO (ID_TIPO_RELACIONAMENTO);














-- ------------------------------------------------------------
-- Tabela que armazena os itens da cotação. Uma cotação pode ser gerada a partir de várias requisições e uma requisição pode gerar várias cotações.
-- ------------------------------------------------------------

CREATE TABLE COMPRA_COTACAO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_COMPRA_FORNECEDOR_COTACAO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  QUANTIDADE DECIMAL(18,6)    ,
  QUANTIDADE_PEDIDA DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_FORNECEDOR_COTACAO)
    REFERENCES COMPRA_FORNECEDOR_COTACAO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_FORNECEDOR_COTACAO_DETALHE ON COMPRA_COTACAO_DETALHE (ID_COMPRA_FORNECEDOR_COTACAO);
CREATE INDEX FK_PRODUTO_COTACAO_DETALHE ON COMPRA_COTACAO_DETALHE (ID_PRODUTO);




-- ------------------------------------------------------------
-- Tabela que armazena os itens do pedido. Uma cotação poderá gerar vários pedidos e um pedido pode ser gerado a partir de várias cotações.
-- ------------------------------------------------------------

CREATE TABLE COMPRA_PEDIDO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_COMPRA_PEDIDO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  CST_CSOSN CHAR(3)    ,
  CFOP INTEGER    ,
  BASE_CALCULO_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS DECIMAL(18,6)    ,
  VALOR_IPI DECIMAL(18,6)    ,
  ALIQUOTA_ICMS DECIMAL(18,6)    ,
  ALIQUOTA_IPI DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_PEDIDO)
    REFERENCES COMPRA_PEDIDO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_PEDIDO_COMPRA_DETALHE ON COMPRA_PEDIDO_DETALHE (ID_COMPRA_PEDIDO);
CREATE INDEX FK_PROD_PEDIDO_DETALHE ON COMPRA_PEDIDO_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Faz o meio de campo entre CENTRO_RESULTADO e NATUREZA_FINANCEIRA. Relacionamento N:M.
-- ------------------------------------------------------------

CREATE TABLE CT_RESULTADO_NT_FINANCEIRA (
  ID SERIAL  NOT NULL ,
  ID_CENTRO_RESULTADO INTEGER   NOT NULL ,
  ID_NATUREZA_FINANCEIRA INTEGER   NOT NULL ,
  PERCENTUAL_RATEIO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_NATUREZA_FINANCEIRA)
    REFERENCES NATUREZA_FINANCEIRA(ID),
  FOREIGN KEY(ID_CENTRO_RESULTADO)
    REFERENCES CENTRO_RESULTADO(ID));


CREATE INDEX FK_NF_CR ON CT_RESULTADO_NT_FINANCEIRA (ID_NATUREZA_FINANCEIRA);
CREATE INDEX CT_RESULTADO_NT_FINANCEIRA_FKIndex2 ON CT_RESULTADO_NT_FINANCEIRA (ID_CENTRO_RESULTADO);



-- ------------------------------------------------------------
-- Armazena as requisições de compra.
-- ------------------------------------------------------------

CREATE TABLE COMPRA_REQUISICAO (
  ID                        SERIAL  NOT NULL ,
  ID_COMPRA_TIPO_REQUISICAO INTEGER NOT NULL ,
  ID_EMPRESA                INTEGER NOT NULL,
  ID_COLABORADOR            INTEGER NOT NULL ,
  DATA_REQUISICAO           DATE    ,
  OBSERVACAO                TEXT      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_TIPO_REQUISICAO)
    REFERENCES COMPRA_TIPO_REQUISICAO(ID),
  FOREIGN KEY (ID_EMPRESA)
  REFERENCES EMPRESA (ID),
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID));


CREATE INDEX FK_TIPO_REQ_COMPRA ON COMPRA_REQUISICAO (ID_COMPRA_TIPO_REQUISICAO);
CREATE INDEX FK_COL_REQ_COMPRA ON COMPRA_REQUISICAO (ID_COLABORADOR);
CREATE INDEX FK_EMPRESA_REQ_COMPRA
  ON COMPRA_REQUISICAO (ID_EMPRESA);



-- ------------------------------------------------------------
-- Tabela com os detalhes da requisição de compra
-- ------------------------------------------------------------

CREATE TABLE COMPRA_REQUISICAO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_COMPRA_REQUISICAO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  QUANTIDADE DECIMAL(18,6)    ,
  QUANTIDADE_COTADA DECIMAL(18,6)    ,
  ITEM_COTADO CHAR(1)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_REQUISICAO)
    REFERENCES COMPRA_REQUISICAO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_REQUISICAO_COMPRA_DETALHE ON COMPRA_REQUISICAO_DETALHE (ID_COMPRA_REQUISICAO);
CREATE INDEX FK_PRODUTO_REQ_COMPRA_DET ON COMPRA_REQUISICAO_DETALHE (ID_PRODUTO);





-- ------------------------------------------------------------
-- Vinculo entre fornecedor e produto
-- ------------------------------------------------------------

CREATE TABLE FORNECEDOR_PRODUTO (
  ID SERIAL  NOT NULL ,
  ID_FORNECEDOR INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  CODIGO_FORNECEDOR_PRODUTO VARCHAR(20)    ,
  DATA_ULTIMA_COMPRA DATE    ,
  PRECO_ULTIMA_COMPRA DECIMAL(18,6)    ,
  LOTE_MINIMO_COMPRA DECIMAL(18,6)    ,
  MENOR_EMBALAGEM_COMPRA DECIMAL(18,6)    ,
  CUSTO_ULTIMA_COMPRA DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID),
  FOREIGN KEY(ID_FORNECEDOR)
    REFERENCES FORNECEDOR(ID));


CREATE INDEX FK_PRODUTO_FORNECEDOR ON FORNECEDOR_PRODUTO (ID_PRODUTO);
CREATE INDEX FK_FORNECEDOR_PRODUTO ON FORNECEDOR_PRODUTO (ID_FORNECEDOR);







-- ------------------------------------------------------------
-- Armazena os detalhes de uma contagem de produtos e já faz os calculos de acuracidade e divergência.
-- ------------------------------------------------------------

CREATE TABLE INVENTARIO_CONTAGEM_DET (
  ID SERIAL  NOT NULL ,
  ID_INVENTARIO_CONTAGEM_CAB INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  CONTAGEM01 DECIMAL(18,6)    ,
  CONTAGEM02 DECIMAL(18,6)    ,
  CONTAGEM03 DECIMAL(18,6)    ,
  FECHADO_CONTAGEM CHAR(2)    ,
  QUANTIDADE_SISTEMA DECIMAL(18,6)    ,
  ACURACIDADE DECIMAL(18,6)    ,
  DIVERGENCIA DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_INVENTARIO_CONTAGEM_CAB)
    REFERENCES INVENTARIO_CONTAGEM_CAB(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_CONTAGEM_CAB_DET ON INVENTARIO_CONTAGEM_DET (ID_INVENTARIO_CONTAGEM_CAB);
CREATE INDEX FK_PRODUTO_CONTAGEM_INVENTARIO ON INVENTARIO_CONTAGEM_DET (ID_PRODUTO);










-- ------------------------------------------------------------
-- Esta tabela é para permitir que um lançamento a receber possa ter varias naturezas financeiras e varias lançamentos contábeis a elas vinculadas.
-- ------------------------------------------------------------

CREATE TABLE FIN_LCTO_RECEBER_NT_FINANCEIRA (
  ID SERIAL  NOT NULL ,
  ID_FIN_LANCAMENTO_RECEBER INTEGER   NOT NULL ,
  ID_NATUREZA_FINANCEIRA INTEGER   NOT NULL ,
  DATA_INCLUSAO DATE    ,
  VALOR DECIMAL(18,6)    ,
  PERCENTUAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_FIN_LANCAMENTO_RECEBER)
    REFERENCES FIN_LANCAMENTO_RECEBER(ID),
  FOREIGN KEY(ID_NATUREZA_FINANCEIRA)
    REFERENCES NATUREZA_FINANCEIRA(ID));


CREATE INDEX FK_LANC_REC_NAT_FIN ON FIN_LCTO_RECEBER_NT_FINANCEIRA (ID_FIN_LANCAMENTO_RECEBER);
CREATE INDEX FK_NAT_FIN_LANC_RECEBER ON FIN_LCTO_RECEBER_NT_FINANCEIRA (ID_NATUREZA_FINANCEIRA);



-- ------------------------------------------------------------
-- Esta tabela é para permitir que um lançamento a pagar possa ter varias naturezas financeiras e varias lançamentos contábeis a elas vinculadas.
-- ------------------------------------------------------------

CREATE TABLE FIN_LCTO_PAGAR_NT_FINANCEIRA (
  ID SERIAL  NOT NULL ,
  ID_FIN_LANCAMENTO_PAGAR INTEGER   NOT NULL ,
  ID_NATUREZA_FINANCEIRA INTEGER   NOT NULL ,
  DATA_INCLUSAO DATE    ,
  VALOR DECIMAL(18,6)    ,
  PERCENTUAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_FIN_LANCAMENTO_PAGAR)
    REFERENCES FIN_LANCAMENTO_PAGAR(ID),
  FOREIGN KEY(ID_NATUREZA_FINANCEIRA)
    REFERENCES NATUREZA_FINANCEIRA(ID));


CREATE INDEX FK_LANC_PG_NAT_FIN ON FIN_LCTO_PAGAR_NT_FINANCEIRA (ID_FIN_LANCAMENTO_PAGAR);
CREATE INDEX FK_NAT_FIN_LANC_PAGAR ON FIN_LCTO_PAGAR_NT_FINANCEIRA (ID_NATUREZA_FINANCEIRA);



-- ------------------------------------------------------------
-- Armazena os detalhes da pre-venda.
-- ------------------------------------------------------------

CREATE TABLE PRE_VENDA_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  ID_PRE_VENDA_CABECALHO INTEGER   NOT NULL ,
  ITEM INTEGER    ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  CANCELADO CHAR(1)    ,
  GTIN_PRODUTO VARCHAR(14)    ,
  NOME_PRODUTO VARCHAR(100)    ,
  UNIDADE_PRODUTO VARCHAR(10)    ,
  ECF_ICMS_ST VARCHAR(4)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PRE_VENDA_CABECALHO)
    REFERENCES PRE_VENDA_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_PRE_VENDA_CAB_DET ON PRE_VENDA_DETALHE (ID_PRE_VENDA_CABECALHO);
CREATE INDEX FK_PRODUTO_PV_DETALHE ON PRE_VENDA_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Os objetivos serão previamente cadastrados. Códigos:
-- 
-- 001 - Venda Bruta - valor bruto vendido dentro do mês
-- 002 - Recebimento Bruto - valor bruto recebido dentro do mês
-- 003 - Recebimento Liquido - valor liquido recebido dentro do mês
-- 004 - Valor Produto - valor bruto de determinado produto vendido dentro do mês
-- 005 - Quantidade Produto - Quantidade de determinado prodito vendido dentro do mês
-- 
-- Caso exista o vínculo com TABELA_PRECO deve-se pegar a taxa de comissão definida naquela tabela.
-- ------------------------------------------------------------

CREATE TABLE COMISSAO_OBJETIVO (
  ID SERIAL  NOT NULL ,
  ID_TABELA_PRECO INTEGER    ,
  ID_PRODUTO INTEGER    ,
  ID_COMISSAO_PERFIL INTEGER   NOT NULL ,
  CODIGO CHAR(3)    ,
  NOME VARCHAR(100)    ,
  DESCRICAO TEXT    ,
  FORMA_PAGAMENTO CHAR(1)    ,
  TAXA_PAGAMENTO DECIMAL(18,6)    ,
  VALOR_PAGAMENTO DECIMAL(18,6)    ,
  VALOR_META DECIMAL(18,6)    ,
  QUANTIDADE DECIMAL(18,6)      ,
PRIMARY KEY(ID)      ,
  FOREIGN KEY(ID_COMISSAO_PERFIL)
    REFERENCES COMISSAO_PERFIL(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID),
  FOREIGN KEY(ID_TABELA_PRECO)
    REFERENCES TABELA_PRECO(ID));


CREATE INDEX FK_COMISSAO_PERFIL_OBJETIVO ON COMISSAO_OBJETIVO (ID_COMISSAO_PERFIL);
CREATE INDEX FK_PRODUTO_COMISSAO_OBJETIVO ON COMISSAO_OBJETIVO (ID_PRODUTO);
CREATE INDEX FK_TABELA_PRECO_COMISSAO ON COMISSAO_OBJETIVO (ID_TABELA_PRECO);









-- ------------------------------------------------------------
-- Tabela que garda as parcelas para recebimento. Caso o recebimento seja efetuado de uma vez, a tabela LANCAMENTO_RECEBER gerará uma parcela para ser recebida e a mesma será armazenada nesta tabela.
-- ------------------------------------------------------------

CREATE TABLE FIN_PARCELA_RECEBER (
  ID SERIAL  NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  ID_FIN_LANCAMENTO_RECEBER INTEGER   NOT NULL ,
  ID_FIN_STATUS_PARCELA INTEGER   NOT NULL ,
  NUMERO_PARCELA INTEGER    ,
  DATA_EMISSAO DATE    ,
  DATA_VENCIMENTO DATE    ,
  DESCONTO_ATE DATE    ,
  VALOR DECIMAL(18,6)    ,
  TAXA_JURO DECIMAL(18,6)    ,
  TAXA_MULTA DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_JURO DECIMAL(18,6)    ,
  VALOR_MULTA DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  EMITIU_BOLETO CHAR(1)    ,
  BOLETO_NOSSO_NUMERO VARCHAR(50)      ,
PRIMARY KEY(ID)      ,
  FOREIGN KEY(ID_FIN_LANCAMENTO_RECEBER)
    REFERENCES FIN_LANCAMENTO_RECEBER(ID),
  FOREIGN KEY(ID_FIN_STATUS_PARCELA)
    REFERENCES FIN_STATUS_PARCELA(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_LANCAMENTO_PARCELA_RECEBER ON FIN_PARCELA_RECEBER (ID_FIN_LANCAMENTO_RECEBER);
CREATE INDEX FK_STATUS_PARCELA_RECEBER ON FIN_PARCELA_RECEBER (ID_FIN_STATUS_PARCELA);
CREATE INDEX FK_CTA_CX_PAR_REC ON FIN_PARCELA_RECEBER (ID_CONTA_CAIXA);









-- ------------------------------------------------------------
-- Tabela que controla o recebimento de pagamentos das parcelas.
-- ------------------------------------------------------------

CREATE TABLE FIN_PARCELA_RECEBIMENTO (
  ID SERIAL  NOT NULL ,
  ID_FIN_PARCELA_RECEBER INTEGER   NOT NULL ,
  ID_FIN_TIPO_RECEBIMENTO INTEGER   NOT NULL ,
  ID_FIN_CHEQUE_RECEBIDO INTEGER    ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  DATA_RECEBIMENTO DATE    ,
  TAXA_JURO DECIMAL(18,6)    ,
  TAXA_MULTA DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_JURO DECIMAL(18,6)    ,
  VALOR_MULTA DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_RECEBIDO DECIMAL(18,6)    ,
  HISTORICO VARCHAR(250)      ,
PRIMARY KEY(ID)        ,
  FOREIGN KEY(ID_FIN_PARCELA_RECEBER)
    REFERENCES FIN_PARCELA_RECEBER(ID),
  FOREIGN KEY(ID_FIN_TIPO_RECEBIMENTO)
    REFERENCES FIN_TIPO_RECEBIMENTO(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID),
  FOREIGN KEY(ID_FIN_CHEQUE_RECEBIDO)
    REFERENCES FIN_CHEQUE_RECEBIDO(ID));


CREATE INDEX FK_PARCELA_RECEB ON FIN_PARCELA_RECEBIMENTO (ID_FIN_PARCELA_RECEBER);
CREATE INDEX FK_TIPO_PARC_RECEBIMENTO ON FIN_PARCELA_RECEBIMENTO (ID_FIN_TIPO_RECEBIMENTO);
CREATE INDEX FK_CONTA_CAIXA_PARC_REC ON FIN_PARCELA_RECEBIMENTO (ID_CONTA_CAIXA);
CREATE INDEX FK_CHEQUE_RECEBIMENTO ON FIN_PARCELA_RECEBIMENTO (ID_FIN_CHEQUE_RECEBIDO);





-- ------------------------------------------------------------
-- Tabela que controla o recebimento de pagamentos das parcelas. Não se aproveita a tabela PARCELA_PAGAR porque podem existir pagamentos parciais de parcela, neste caso o controle deve ser feito em tabelas separadas. No caso de uma parcela que é paga de forma integral, a mesma será replicada da tabela PARCELA_PAGAR para a tabela PARCELA_PAGAMENTO e os dados dos campos adicionais serão informados.
-- ------------------------------------------------------------

CREATE TABLE FIN_PARCELA_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  ID_FIN_PARCELA_PAGAR INTEGER   NOT NULL ,
  ID_FIN_CHEQUE_EMITIDO INTEGER    ,
  ID_FIN_TIPO_PAGAMENTO INTEGER   NOT NULL ,
  ID_CONTA_CAIXA INTEGER   NOT NULL ,
  DATA_PAGAMENTO DATE    ,
  TAXA_JURO DECIMAL(18,6)    ,
  TAXA_MULTA DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_JURO DECIMAL(18,6)    ,
  VALOR_MULTA DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_PAGO DECIMAL(18,6)    ,
  HISTORICO VARCHAR(250)      ,
PRIMARY KEY(ID)        ,
  FOREIGN KEY(ID_FIN_PARCELA_PAGAR)
    REFERENCES FIN_PARCELA_PAGAR(ID),
  FOREIGN KEY(ID_FIN_CHEQUE_EMITIDO)
    REFERENCES FIN_CHEQUE_EMITIDO(ID),
  FOREIGN KEY(ID_FIN_TIPO_PAGAMENTO)
    REFERENCES FIN_TIPO_PAGAMENTO(ID),
  FOREIGN KEY(ID_CONTA_CAIXA)
    REFERENCES CONTA_CAIXA(ID));


CREATE INDEX FK_PARCELA_PAGAMENTO ON FIN_PARCELA_PAGAMENTO (ID_FIN_PARCELA_PAGAR);
CREATE INDEX FK_CHEQUE_PARCELA ON FIN_PARCELA_PAGAMENTO (ID_FIN_CHEQUE_EMITIDO);
CREATE INDEX FK_TIPO_PAGAMENTO_PARCELA ON FIN_PARCELA_PAGAMENTO (ID_FIN_TIPO_PAGAMENTO);
CREATE INDEX FK_CONTA_CX_PARC_PGTO ON FIN_PARCELA_PAGAMENTO (ID_CONTA_CAIXA);




-- ------------------------------------------------------------
-- Tabela que armazena  os dados dos colaboradores que são vendedores.
-- ------------------------------------------------------------

CREATE TABLE VENDEDOR (
  ID SERIAL  NOT NULL ,
  ID_TABELA_PRECO INTEGER    ,
  ID_REGIAO INTEGER    ,
  ID_COMISSAO_PERFIL INTEGER    ,
  ID_COLABORADOR INTEGER    ,
  COMISSAO DECIMAL(18,6)    ,
  META_VENDAS DECIMAL(18,6)    ,
  GERENTE CHAR(1)    ,
  TAXA_GERENTE DECIMAL(18,6)      ,
PRIMARY KEY(ID)        ,
  FOREIGN KEY(ID_COLABORADOR)
    REFERENCES COLABORADOR(ID),
  FOREIGN KEY(ID_COMISSAO_PERFIL)
    REFERENCES COMISSAO_PERFIL(ID),
  FOREIGN KEY(ID_REGIAO)
    REFERENCES REGIAO(ID),
  FOREIGN KEY(ID_TABELA_PRECO)
    REFERENCES TABELA_PRECO(ID));


CREATE INDEX FK_COLABORADOR_VENDEDOR ON VENDEDOR (ID_COLABORADOR);
CREATE INDEX FK_COMISSAO_PERFIL_VENDEDOR ON VENDEDOR (ID_COMISSAO_PERFIL);
CREATE INDEX FK_REGIAO_VENDEDOR ON VENDEDOR (ID_REGIAO);
CREATE INDEX FK_TABELA_PRECO_VENDEDOR ON VENDEDOR (ID_TABELA_PRECO);





-- ------------------------------------------------------------
-- Armazena o cabeçalho do orçamento/pedido de venda.
-- 
-- O usuário informa aqui as condições de pagamentos, mas não gera neste momento as parcelas. As mesmas só serão geradas no momento da confirmação da venda.
-- ------------------------------------------------------------

CREATE TABLE VENDA_ORCAMENTO_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA INTEGER   NOT NULL ,
  ID_VENDA_CONDICOES_PAGAMENTO INTEGER   NOT NULL ,
  ID_VENDEDOR INTEGER   NOT NULL ,
  ID_TRANSPORTADORA INTEGER    ,
  ID_CLIENTE INTEGER   NOT NULL ,
  TIPO CHAR(1)    ,
  CODIGO VARCHAR(20)    ,
  DATA_CADASTRO DATE    ,
  DATA_ENTREGA DATE    ,
  VALIDADE DATE    ,
  TIPO_FRETE CHAR(1)    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  VALOR_FRETE DECIMAL(18,6)    ,
  TAXA_COMISSAO DECIMAL(18,6)    ,
  VALOR_COMISSAO DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  OBSERVACAO TEXT    ,
  SITUACAO CHAR(1)      ,
PRIMARY KEY(ID)          ,
  FOREIGN KEY(ID_VENDA_CONDICOES_PAGAMENTO)
    REFERENCES VENDA_CONDICOES_PAGAMENTO(ID),
  FOREIGN KEY(ID_TRANSPORTADORA)
    REFERENCES TRANSPORTADORA(ID),
  FOREIGN KEY(ID_CLIENTE)
    REFERENCES CLIENTE(ID),
  FOREIGN KEY(ID_VENDEDOR)
    REFERENCES VENDEDOR(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_COND_PGTO_ORC_PED_VENDA ON VENDA_ORCAMENTO_CABECALHO (ID_VENDA_CONDICOES_PAGAMENTO);
CREATE INDEX FK_TRANSP_ORC_PED_VENDA ON VENDA_ORCAMENTO_CABECALHO (ID_TRANSPORTADORA);
CREATE INDEX FK_CLIENTE_ORC_PED_VENDA ON VENDA_ORCAMENTO_CABECALHO (ID_CLIENTE);
CREATE INDEX FK_VENDEDOR_ORC_PED_VENDA ON VENDA_ORCAMENTO_CABECALHO (ID_VENDEDOR);
CREATE INDEX FK_EMPRESA_ORCAMENTO_CAB ON VENDA_ORCAMENTO_CABECALHO (ID_EMPRESA);







-- ------------------------------------------------------------
-- Faz o controle da grade de produtos.
-- ------------------------------------------------------------

CREATE TABLE ESTOQUE_GRADE (
  ID SERIAL  NOT NULL ,
  ID_EMPRESA_PRODUTO INTEGER    ,
  ID_ESTOQUE_MARCA INTEGER   NOT NULL ,
  ID_ESTOQUE_SABOR INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER    ,
  ID_ESTOQUE_TAMANHO INTEGER   NOT NULL ,
  ID_ESTOQUE_COR INTEGER   NOT NULL ,
  CODIGO VARCHAR(50)    ,
  QUANTIDADE DECIMAL(18,6)      ,
PRIMARY KEY(ID)            ,
  FOREIGN KEY(ID_ESTOQUE_COR)
    REFERENCES ESTOQUE_COR(ID),
  FOREIGN KEY(ID_ESTOQUE_TAMANHO)
    REFERENCES ESTOQUE_TAMANHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID),
  FOREIGN KEY(ID_ESTOQUE_SABOR)
    REFERENCES ESTOQUE_SABOR(ID),
  FOREIGN KEY(ID_ESTOQUE_MARCA)
    REFERENCES ESTOQUE_MARCA(ID),
  FOREIGN KEY(ID_EMPRESA_PRODUTO)
    REFERENCES EMPRESA_PRODUTO(ID));


CREATE INDEX FK_ESTOQUE_COR_GRADE ON ESTOQUE_GRADE (ID_ESTOQUE_COR);
CREATE INDEX FK_ESTOQUE_TAMANHO_GRADE ON ESTOQUE_GRADE (ID_ESTOQUE_TAMANHO);
CREATE INDEX FK_PRODUTO_ESTOQUE_GRADE ON ESTOQUE_GRADE (ID_PRODUTO);
CREATE INDEX FK_ESTOQUE_SABOR ON ESTOQUE_GRADE (ID_ESTOQUE_SABOR);
CREATE INDEX FK_ESTOQUE_MARCA ON ESTOQUE_GRADE (ID_ESTOQUE_MARCA);
CREATE INDEX FK_EMPRESA_PRODUTO_GRADE ON ESTOQUE_GRADE (ID_EMPRESA_PRODUTO);






-- ------------------------------------------------------------
-- Tabela que armazena o cabeçalho das vendas do sistema.
-- ------------------------------------------------------------

CREATE TABLE VENDA_CABECALHO (
  ID                           SERIAL  NOT NULL ,
  ID_EMPRESA                   INTEGER   NOT NULL ,
  ID_VENDA_ROMANEIO_ENTREGA    INTEGER    ,
  ID_VENDA_ORCAMENTO_CABECALHO INTEGER    ,
  ID_VENDA_CONDICOES_PAGAMENTO INTEGER   NOT NULL ,
  ID_TRANSPORTADORA            INTEGER    ,
  ID_TIPO_NOTA_FISCAL          INTEGER,
  ID_CLIENTE                   INTEGER   NOT NULL ,
  ID_VENDEDOR                  INTEGER   NOT NULL ,
  DATA_VENDA                   DATE    ,
  DATA_SAIDA                   DATE    ,
  HORA_SAIDA                   VARCHAR(8)    ,
  NUMERO_FATURA                INTEGER    ,
  LOCAL_ENTREGA                VARCHAR(100)    ,
  LOCAL_COBRANCA               VARCHAR(100)    ,
  VALOR_SUBTOTAL               DECIMAL(18,6)    ,
  TAXA_COMISSAO                DECIMAL(18,6)    ,
  VALOR_COMISSAO               DECIMAL(18,6)    ,
  TAXA_DESCONTO                DECIMAL(18,6)    ,
  VALOR_DESCONTO               DECIMAL(18,6)    ,
  VALOR_TOTAL                  DECIMAL(18,6)    ,
  TIPO_FRETE                   CHAR(1)    ,
  FORMA_PAGAMENTO              CHAR(1)    ,
  VALOR_FRETE DECIMAL(18,6)    ,
  VALOR_SEGURO DECIMAL(18,6)    ,
  OBSERVACAO TEXT    ,
  SITUACAO CHAR(1)      ,
PRIMARY KEY(ID)                ,
  FOREIGN KEY(ID_VENDA_ORCAMENTO_CABECALHO)
    REFERENCES VENDA_ORCAMENTO_CABECALHO(ID),
  FOREIGN KEY(ID_VENDA_CONDICOES_PAGAMENTO)
    REFERENCES VENDA_CONDICOES_PAGAMENTO(ID),
  FOREIGN KEY(ID_CLIENTE)
    REFERENCES CLIENTE(ID),
  FOREIGN KEY(ID_VENDEDOR)
    REFERENCES VENDEDOR(ID),
  FOREIGN KEY(ID_TIPO_NOTA_FISCAL)
    REFERENCES NOTA_FISCAL_TIPO(ID),
  FOREIGN KEY(ID_TRANSPORTADORA)
    REFERENCES TRANSPORTADORA(ID),
  FOREIGN KEY(ID_VENDA_ROMANEIO_ENTREGA)
    REFERENCES VENDA_ROMANEIO_ENTREGA(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID));


CREATE INDEX FK_ORCAMENTO_VENDA ON VENDA_CABECALHO (ID_VENDA_ORCAMENTO_CABECALHO);
CREATE INDEX FK_VENDA_CAB_CONDICOES ON VENDA_CABECALHO (ID_VENDA_CONDICOES_PAGAMENTO);
CREATE INDEX FK_CLIENTE_VENDA_CAB ON VENDA_CABECALHO (ID_CLIENTE);
CREATE INDEX FK_VENDEDOR_VENDA_CAB ON VENDA_CABECALHO (ID_VENDEDOR);
CREATE INDEX FK_TIPO_NF_VENDA_CAB ON VENDA_CABECALHO (ID_TIPO_NOTA_FISCAL);
CREATE INDEX FK_TRANSP_VENDA_CAB ON VENDA_CABECALHO (ID_TRANSPORTADORA);
CREATE INDEX FK_VENDA_ROMANEIO ON VENDA_CABECALHO (ID_VENDA_ROMANEIO_ENTREGA);
CREATE INDEX FK_EMPRESA_VENDA_CAB ON VENDA_CABECALHO (ID_EMPRESA);







CREATE TABLE CTE_DOCUMENTO_ANTERIOR_ID (
  ID SERIAL  NOT NULL ,
  ID_CTE_DOCUMENTO_ANTERIOR INTEGER   NOT NULL ,
  TIPO CHAR(2)    ,
  SERIE CHAR(3)    ,
  SUBSERIE CHAR(2)    ,
  NUMERO VARCHAR(20)    ,
  DATA_EMISSAO DATE    ,
  CHAVE_CTE VARCHAR(44)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_CTE_DOCUMENTO_ANTERIOR)
    REFERENCES CTE_DOCUMENTO_ANTERIOR(ID));


CREATE INDEX FK_CTE_DOC_ANTERIOR_ID ON CTE_DOCUMENTO_ANTERIOR_ID (ID_CTE_DOCUMENTO_ANTERIOR);









-- ------------------------------------------------------------
-- Aramazena os dados de auditoria da aplicação
-- ------------------------------------------------------------

CREATE TABLE AUDITORIA (
  ID SERIAL  NOT NULL ,
  ID_USUARIO INTEGER   NOT NULL ,
  DATA_REGISTRO DATE    ,
  HORA_REGISTRO VARCHAR(8)    ,
  JANELA_CONTROLLER VARCHAR(50)    ,
  ACAO VARCHAR(50)    ,
  CONTEUDO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_USUARIO)
    REFERENCES USUARIO(ID));


CREATE INDEX FK_USUARIO_AUDITORIA ON AUDITORIA (ID_USUARIO);



-- ------------------------------------------------------------
-- Para geração do registro tipo D4 do PAF.
-- ------------------------------------------------------------

CREATE TABLE DAV_DETALHE_ALTERACAO (
  ID SERIAL  NOT NULL ,
  ID_DAV_DETALHE INTEGER   NOT NULL ,
  DATA_ALTERACAO DATE    ,
  HORA_ALTERACAO VARCHAR(8)    ,
  TIPO_ALTERACAO CHAR(1)    ,
  OBJETO TEXT      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_DAV_DETALHE)
    REFERENCES DAV_DETALHE(ID));


CREATE INDEX DAV_DETALHE_ALTERACAO_FKIndex1 ON DAV_DETALHE_ALTERACAO (ID_DAV_DETALHE);




-- ------------------------------------------------------------
-- Armazena os dados de frete da venda
-- ------------------------------------------------------------

CREATE TABLE VENDA_FRETE (
  ID SERIAL  NOT NULL ,
  ID_TRANSPORTADORA INTEGER   NOT NULL ,
  ID_VENDA_CABECALHO INTEGER   NOT NULL ,
  CONHECIMENTO INTEGER    ,
  RESPONSAVEL CHAR(1)    ,
  PLACA VARCHAR(7)    ,
  UF_PLACA CHAR(2)    ,
  SELO_FISCAL INTEGER    ,
  QUANTIDADE_VOLUME DECIMAL(18,6)    ,
  MARCA_VOLUME VARCHAR(50)    ,
  ESPECIE_VOLUME VARCHAR(20)    ,
  PESO_BRUTO DECIMAL(18,6)    ,
  PESO_LIQUIDO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_VENDA_CABECALHO)
    REFERENCES VENDA_CABECALHO(ID),
  FOREIGN KEY(ID_TRANSPORTADORA)
    REFERENCES TRANSPORTADORA(ID));


CREATE INDEX FK_VENDA_CABECALHO_FRETE ON VENDA_FRETE (ID_VENDA_CABECALHO);
CREATE INDEX FK_TRANSPORTADORA_FRETE_VENDA ON VENDA_FRETE (ID_TRANSPORTADORA);







-- ------------------------------------------------------------
-- Armazena os itens do orçamento de venda
-- ------------------------------------------------------------

CREATE TABLE VENDA_ORCAMENTO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_VENDA_ORCAMENTO_CABECALHO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_VENDA_ORCAMENTO_CABECALHO)
    REFERENCES VENDA_ORCAMENTO_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_VENDA_ORCAMENTO_CAB_DET ON VENDA_ORCAMENTO_DETALHE (ID_VENDA_ORCAMENTO_CABECALHO);
CREATE INDEX FK_PRODUTO_ORC_VENDA_DET ON VENDA_ORCAMENTO_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Armazena os itens da venda.
-- ------------------------------------------------------------

CREATE TABLE VENDA_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  ID_VENDA_CABECALHO INTEGER   NOT NULL ,
  QUANTIDADE DECIMAL(18,6)    ,
  VALOR_UNITARIO DECIMAL(18,6)    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  TAXA_DESCONTO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  TAXA_COMISSAO DECIMAL(18,6)    ,
  VALOR_COMISSAO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_VENDA_CABECALHO)
    REFERENCES VENDA_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_VENDA_CAB_DET ON VENDA_DETALHE (ID_VENDA_CABECALHO);
CREATE INDEX FK_PRODUTO_VENDA_DET ON VENDA_DETALHE (ID_PRODUTO);



-- ------------------------------------------------------------
-- Esta tabela deve armazenar as comissões calculadas em cada venda. Desta forma toda vez que houver uma venda com indicaçao de comissao este valor será lançado nesta tabela, com indicaçao do VALOR_VENDA, VALOR_COMISSAO, TIPO (liquidez ou faturamento), e indicador se o lançamento é a debito ou a credito, pois se houver uma devoluaçao desta venda deve haver o debito (estorno da comissao) mediante um lançamento de saldo DEVEDOR para aquele vendedor. Essa tabela é do primeiro ciclo e serve para calcular a comissão dessa forma. Existe a opção de utilizar as tabelas para gerenciamento das comissões através dos perfis cadastrados, feitas no segundo ciclo do ERP.
-- ------------------------------------------------------------

CREATE TABLE VENDA_COMISSAO (
  ID SERIAL  NOT NULL ,
  ID_VENDEDOR INTEGER   NOT NULL ,
  ID_VENDA_CABECALHO INTEGER   NOT NULL ,
  VALOR_VENDA DECIMAL(18,6)    ,
  TIPO_CONTABIL CHAR(1)    ,
  VALOR_COMISSAO DECIMAL(18,6)    ,
  SITUACAO CHAR(1)    ,
  DATA_LANCAMENTO DATE      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_VENDA_CABECALHO)
    REFERENCES VENDA_CABECALHO(ID),
  FOREIGN KEY(ID_VENDEDOR)
    REFERENCES VENDEDOR(ID));


CREATE INDEX FK_VENDA_COMISSAO ON VENDA_COMISSAO (ID_VENDA_CABECALHO);
CREATE INDEX FK_VENDEDOR_COMISSAO ON VENDA_COMISSAO (ID_VENDEDOR);





-- ------------------------------------------------------------
-- Faz o controle de quantidade de itens que saem da cotação e entram no pedido
-- ------------------------------------------------------------

CREATE TABLE COMPRA_COTACAO_PEDIDO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_COMPRA_PEDIDO INTEGER   NOT NULL ,
  ID_COMPRA_COTACAO_DETALHE INTEGER   NOT NULL ,
  QUANTIDADE_PEDIDA DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_COTACAO_DETALHE)
    REFERENCES COMPRA_COTACAO_DETALHE(ID),
  FOREIGN KEY(ID_COMPRA_PEDIDO)
    REFERENCES COMPRA_PEDIDO(ID));


CREATE INDEX FK_COT_PEDIDO_DET ON COMPRA_COTACAO_PEDIDO_DETALHE (ID_COMPRA_COTACAO_DETALHE);
CREATE INDEX FK_PEDIDO_COTACAO_DETALHE ON COMPRA_COTACAO_PEDIDO_DETALHE (ID_COMPRA_PEDIDO);



-- ------------------------------------------------------------
-- Faz o controle de quantidade de itens que saem da requisição e entram na cotação
-- ------------------------------------------------------------

CREATE TABLE COMPRA_REQ_COTACAO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_COMPRA_COTACAO INTEGER   NOT NULL ,
  ID_COMPRA_REQUISICAO_DETALHE INTEGER   NOT NULL ,
  QUANTIDADE_COTADA DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_COMPRA_REQUISICAO_DETALHE)
    REFERENCES COMPRA_REQUISICAO_DETALHE(ID),
  FOREIGN KEY(ID_COMPRA_COTACAO)
    REFERENCES COMPRA_COTACAO(ID));


CREATE INDEX FK_REQUISICAO_COTACAO_DETALHE ON COMPRA_REQ_COTACAO_DETALHE (ID_COMPRA_REQUISICAO_DETALHE);
CREATE INDEX FK_COTACAO_REQ_DETALHE ON COMPRA_REQ_COTACAO_DETALHE (ID_COMPRA_COTACAO);



-- ------------------------------------------------------------
-- Tabela que armazena o cabeçalho das notas fiscais eletrônicas.
-- ------------------------------------------------------------

CREATE TABLE NFE_CABECALHO (
  ID SERIAL  NOT NULL ,
  ID_NFCE_MOVIMENTO INTEGER    ,
  ID_VENDEDOR INTEGER    ,
  ID_TRIBUT_OPERACAO_FISCAL INTEGER    ,
  ID_VENDA_CABECALHO INTEGER    ,
  ID_EMPRESA INTEGER    ,
  ID_FORNECEDOR INTEGER    ,
  ID_CLIENTE INTEGER    ,
  UF_EMITENTE INTEGER    ,
  CODIGO_NUMERICO VARCHAR(8)    ,
  NATUREZA_OPERACAO VARCHAR(60)    ,
  CODIGO_MODELO CHAR(2)    ,
  SERIE CHAR(3)    ,
  NUMERO VARCHAR(9)    ,
  DATA_HORA_EMISSAO TIMESTAMP    ,
  DATA_HORA_ENTRADA_SAIDA TIMESTAMP    ,
  TIPO_OPERACAO INTEGER    ,
  LOCAL_DESTINO INTEGER    ,
  CODIGO_MUNICIPIO INTEGER    ,
  FORMATO_IMPRESSAO_DANFE INTEGER    ,
  TIPO_EMISSAO INTEGER    ,
  CHAVE_ACESSO VARCHAR(44)    ,
  DIGITO_CHAVE_ACESSO CHAR(1)    ,
  AMBIENTE INTEGER    ,
  FINALIDADE_EMISSAO INTEGER    ,
  CONSUMIDOR_OPERACAO INTEGER    ,
  PROCESSO_EMISSAO INTEGER    ,
  VERSAO_PROCESSO_EMISSAO VARCHAR(20)    ,
  DATA_ENTRADA_CONTINGENCIA TIMESTAMP    ,
  JUSTIFICATIVA_CONTINGENCIA VARCHAR(255)    ,
  BASE_CALCULO_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS_DESONERADO DECIMAL(18,6)    ,
  BASE_CALCULO_ICMS_ST DECIMAL(18,6)    ,
  VALOR_ICMS_ST DECIMAL(18,6)    ,
  VALOR_TOTAL_PRODUTOS DECIMAL(18,6)    ,
  VALOR_FRETE DECIMAL(18,6)    ,
  VALOR_SEGURO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_IMPOSTO_IMPORTACAO DECIMAL(18,6)    ,
  VALOR_IPI DECIMAL(18,6)    ,
  VALOR_PIS DECIMAL(18,6)    ,
  VALOR_COFINS DECIMAL(18,6)    ,
  VALOR_DESPESAS_ACESSORIAS DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  VALOR_SERVICOS DECIMAL(18,6)    ,
  BASE_CALCULO_ISSQN DECIMAL(18,6)    ,
  VALOR_ISSQN DECIMAL(18,6)    ,
  VALOR_PIS_ISSQN DECIMAL(18,6)    ,
  VALOR_COFINS_ISSQN DECIMAL(18,6)    ,
  DATA_PRESTACAO_SERVICO DATE    ,
  VALOR_DEDUCAO_ISSQN DECIMAL(18,6)    ,
  OUTRAS_RETENCOES_ISSQN DECIMAL(18,6)    ,
  DESCONTO_INCONDICIONADO_ISSQN DECIMAL(18,6)    ,
  DESCONTO_CONDICIONADO_ISSQN DECIMAL(18,6)    ,
  TOTAL_RETENCAO_ISSQN DECIMAL(18,6)    ,
  REGIME_ESPECIAL_TRIBUTACAO INTEGER    ,
  VALOR_RETIDO_PIS DECIMAL(18,6)    ,
  VALOR_RETIDO_COFINS DECIMAL(18,6)    ,
  VALOR_RETIDO_CSLL DECIMAL(18,6)    ,
  BASE_CALCULO_IRRF DECIMAL(18,6)    ,
  VALOR_RETIDO_IRRF DECIMAL(18,6)    ,
  BASE_CALCULO_PREVIDENCIA DECIMAL(18,6)    ,
  VALOR_RETIDO_PREVIDENCIA DECIMAL(18,6)    ,
  TROCO DECIMAL(18,6)    ,
  COMEX_UF_EMBARQUE CHAR(2)    ,
  COMEX_LOCAL_EMBARQUE VARCHAR(60)    ,
  COMEX_LOCAL_DESPACHO VARCHAR(60)    ,
  COMPRA_NOTA_EMPENHO VARCHAR(22)    ,
  COMPRA_PEDIDO VARCHAR(60)    ,
  COMPRA_CONTRATO VARCHAR(60)    ,
  INFORMACOES_ADD_FISCO TEXT    ,
  INFORMACOES_ADD_CONTRIBUINTE TEXT    ,
  STATUS_NOTA INTEGER    ,
  QUANTIDADE_IMPRESSAO_DANFE INTEGER    ,
  INDICADOR_PRESENCA CHAR(1)    ,
  VALOR_FCP DECIMAL(18,6)    ,
  VALOR_FCP_ST DECIMAL(18,6)    ,
  VALOR_FCP_ST_RETIDO DECIMAL(18,6)    ,
  VALOR_IPI_DEVOLVIDO DECIMAL(18,6)    ,
  VERSAO_APLICATIVO VARCHAR(20)    ,
  DATA_HORA_PROCESSAMENTO TIMESTAMP    ,
  NUMERO_PROTOCOLO VARCHAR(15)    ,
  DIGEST_VALUE VARCHAR(28)    ,
  CODIGO_STATUS_RESPOSTA CHAR(3)    ,
  DESCRICAO_MOTIVO_RESPOSTA TEXT    ,
  JUSTIFICATIVA_CANCELAMENTO VARCHAR(255)    ,
  VALOR_ICMS_FCP_UF_DESTINO DECIMAL(18,6)    ,
  VALOR_ICMS_INTER_UF_DESTINO DECIMAL(18,6)    ,
  VALOR_ICMS_INTER_UF_REMETENTE DECIMAL(18,6)    ,
  QRCODE TEXT    ,
  URL_CHAVE VARCHAR(85)    ,
  INDICADOR_FORMA_PAGAMENTO INTEGER      ,
PRIMARY KEY(ID)              ,
  FOREIGN KEY(ID_CLIENTE)
    REFERENCES CLIENTE(ID),
  FOREIGN KEY(ID_FORNECEDOR)
    REFERENCES FORNECEDOR(ID),
  FOREIGN KEY(ID_EMPRESA)
    REFERENCES EMPRESA(ID),
  FOREIGN KEY(ID_VENDA_CABECALHO)
    REFERENCES VENDA_CABECALHO(ID),
  FOREIGN KEY(ID_TRIBUT_OPERACAO_FISCAL)
    REFERENCES TRIBUT_OPERACAO_FISCAL(ID),
  FOREIGN KEY(ID_VENDEDOR)
    REFERENCES VENDEDOR(ID),
  FOREIGN KEY(ID_NFCE_MOVIMENTO)
    REFERENCES NFCE_MOVIMENTO(ID));


CREATE INDEX FK_CLIENTE_NFE ON NFE_CABECALHO (ID_CLIENTE);
CREATE INDEX FK_FORNECEDOR_NFE ON NFE_CABECALHO (ID_FORNECEDOR);
CREATE INDEX FK_EMPRESA_NFE ON NFE_CABECALHO (ID_EMPRESA);
CREATE INDEX FK_VENDA_CAB_NFE ON NFE_CABECALHO (ID_VENDA_CABECALHO);
CREATE INDEX FK_TRIBUT_NFE ON NFE_CABECALHO (ID_TRIBUT_OPERACAO_FISCAL);
CREATE INDEX FK_VENDEDOR_NFECAB ON NFE_CABECALHO (ID_VENDEDOR);
CREATE INDEX FK_NFCE_MOV_NFE_CAB ON NFE_CABECALHO (ID_NFCE_MOVIMENTO);
























































































-- ------------------------------------------------------------
-- Grupo de informações da NF de produtor rural referenciada
-- ------------------------------------------------------------

CREATE TABLE NFE_PROD_RURAL_REFERENCIADA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CODIGO_UF INTEGER    ,
  ANO_MES VARCHAR(4)    ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)    ,
  INSCRICAO_ESTADUAL VARCHAR(14)    ,
  MODELO CHAR(2)    ,
  SERIE CHAR(3)    ,
  NUMERO_NF INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_RURAL_REFERENCIADO ON NFE_PROD_RURAL_REFERENCIADA (ID_NFE_CABECALHO);











-- ------------------------------------------------------------
-- Grupo do processo referenciado
-- ------------------------------------------------------------

CREATE TABLE NFE_PROCESSO_REFERENCIADO (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  IDENTIFICADOR VARCHAR(60)    ,
  ORIGEM CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX NFE_CAB_PROC_REF ON NFE_PROCESSO_REFERENCIADO (ID_NFE_CABECALHO);

-- ------------------------------------------------------------
-- aguarda o xml da nfe
-- ------------------------------------------------------------


CREATE TABLE NFE_XML (
  id               SERIAL  NOT NULL,
  ID_NFE_CABECALHO INTEGER NOT NULL,
  XML              BLOB,
  PRIMARY KEY (id),
  FOREIGN KEY (ID_NFE_CABECALHO)
  REFERENCES NFE_CABECALHO (ID)
);


CREATE INDEX nfe_xml_FKIndex1
  ON NFE_XML (ID_NFE_CABECALHO);

-- ------------------------------------------------------------
-- Grupo de informação das NF/NF-e referenciadas. Grupo com as informações das NF/NF-e /NF de produtor/ Cupom Fiscal referenciadas. Esta informação será utilizada nas hipóteses previstas na legislação. (Ex.: Devolução de Mercadorias, Substituição de NF cancelada, Complementação de NF, etc.).
-- ------------------------------------------------------------

CREATE TABLE NFE_REFERENCIADA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CHAVE_ACESSO VARCHAR(44)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_REFERENCIADA ON NFE_REFERENCIADA (ID_NFE_CABECALHO);




-- ------------------------------------------------------------
-- Grupo da Fatura
-- ------------------------------------------------------------

CREATE TABLE NFE_FATURA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  NUMERO VARCHAR(60)    ,
  VALOR_ORIGINAL DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_LIQUIDO DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_CAB_FATURA ON NFE_FATURA (ID_NFE_CABECALHO);







-- ------------------------------------------------------------
-- Grupo de informação da NF modelo 1/1A referenciada. 
-- ------------------------------------------------------------

CREATE TABLE NFE_NF_REFERENCIADA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CODIGO_UF INTEGER    ,
  ANO_MES VARCHAR(4)    ,
  CNPJ VARCHAR(14)    ,
  MODELO CHAR(2)    ,
  SERIE CHAR(3)    ,
  NUMERO_NF INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_NF_REFERENCIADA ON NFE_NF_REFERENCIADA (ID_NFE_CABECALHO);









-- ------------------------------------------------------------
-- Grupo de identificação do Local de retirada. Informar apenas quando for diferente do endereço do remetente.
-- ------------------------------------------------------------

CREATE TABLE NFE_LOCAL_RETIRADA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CPF_CNPJ VARCHAR(14)    ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_LOCAL_RETIRADA ON NFE_LOCAL_RETIRADA (ID_NFE_CABECALHO);










-- ------------------------------------------------------------
-- Grupo de identificação do Local de entrega. Informar apenas quando for diferente do endereço do destinatário.
-- ------------------------------------------------------------

CREATE TABLE NFE_LOCAL_ENTREGA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CPF_CNPJ VARCHAR(14)    ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_LOCAL_ENTREGA ON NFE_LOCAL_ENTREGA (ID_NFE_CABECALHO);










-- ------------------------------------------------------------
-- Grupo de informação das NF/NF-e referenciadas. Grupo com as informações das NF/NF-e /NF de produtor/ Cupom Fiscal referenciadas. Esta informação será utilizada nas hipóteses previstas na legislação. (Ex.: Devolução de Mercadorias, Substituição de NF cancelada, Complementação de NF, etc.).
-- ------------------------------------------------------------

CREATE TABLE NFE_CTE_REFERENCIADO (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CHAVE_ACESSO VARCHAR(44)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_CTE_REFERENCIADO ON NFE_CTE_REFERENCIADO (ID_NFE_CABECALHO);




-- ------------------------------------------------------------
-- Grupo de identificação do Destinatário da NF-e
-- ------------------------------------------------------------

CREATE TABLE NFE_DESTINATARIO (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CPF_CNPJ VARCHAR(14)    ,
  ESTRANGEIRO_IDENTIFICACAO VARCHAR(20)    ,
  NOME VARCHAR(60)    ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  INDICADOR_IE INTEGER    ,
  INSCRICAO_ESTADUAL VARCHAR(14)    ,
  INSCRICAO_MUNICIPAL VARCHAR(15)    ,
  SUFRAMA INTEGER    ,
  EMAIL VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_DESTINATARIO ON NFE_DESTINATARIO (ID_NFE_CABECALHO);





















-- ------------------------------------------------------------
-- Informações do Cupom Fiscal referenciado
-- ------------------------------------------------------------

CREATE TABLE NFE_CUPOM_FISCAL_REFERENCIADO (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  MODELO_DOCUMENTO_FISCAL CHAR(2)    ,
  NUMERO_ORDEM_ECF INTEGER    ,
  COO INTEGER    ,
  DATA_EMISSAO_CUPOM DATE    ,
  NUMERO_CAIXA INTEGER    ,
  NUMERO_SERIE_ECF VARCHAR(21)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_CF_REFERENCIADO ON NFE_CUPOM_FISCAL_REFERENCIADO (ID_NFE_CABECALHO);









-- ------------------------------------------------------------
-- Grupo de cana - Informações de registro aquisições de cana v2.0
-- ------------------------------------------------------------

CREATE TABLE NFE_CANA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  SAFRA VARCHAR(9)    ,
  MES_ANO_REFERENCIA VARCHAR(9)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_CAB_CANA ON NFE_CANA (ID_NFE_CABECALHO);





-- ------------------------------------------------------------
-- Criado um novo grupo de informações para que a empresa emitente possa indicar outras pessoas autorizadas a ter acesso ao arquivo XML da NF-e. Nesta alternativa, a empresa emitente poderá indicar o seu Contador, outras pessoas envolvidas no transporte da mercadoria, etc. [GA - Autorização para obter XML]
-- ------------------------------------------------------------

CREATE TABLE NFE_ACESSO_XML (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CNPJ VARCHAR(14)    ,
  CPF VARCHAR(11)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_ACESSO_XML ON NFE_ACESSO_XML (ID_NFE_CABECALHO);



CREATE TABLE NFE_EMITENTE (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  CPF_CNPJ VARCHAR(14)    ,
  NOME VARCHAR(60)    ,
  FANTASIA VARCHAR(60)    ,
  LOGRADOURO VARCHAR(60)    ,
  NUMERO VARCHAR(60)    ,
  COMPLEMENTO VARCHAR(60)    ,
  BAIRRO VARCHAR(60)    ,
  CODIGO_MUNICIPIO INTEGER    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  CEP VARCHAR(8)    ,
  CODIGO_PAIS INTEGER    ,
  NOME_PAIS VARCHAR(60)    ,
  TELEFONE VARCHAR(14)    ,
  INSCRICAO_ESTADUAL VARCHAR(14)    ,
  INSCRICAO_ESTADUAL_ST VARCHAR(14)    ,
  INSCRICAO_MUNICIPAL VARCHAR(15)    ,
  CNAE VARCHAR(7)    ,
  CRT CHAR(1)    ,
  EMAIL VARCHAR(60)    ,
  SUFRAMA INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_EMITENTE ON NFE_EMITENTE (ID_NFE_CABECALHO);





















-- ------------------------------------------------------------
-- Informações das duplicatas.
-- ------------------------------------------------------------

CREATE TABLE NFE_DUPLICATA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  NUMERO VARCHAR(60)    ,
  DATA_VENCIMENTO DATE    ,
  VALOR DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_NFE_CAB_DUPLICATA ON NFE_DUPLICATA (ID_NFE_CABECALHO);






-- ------------------------------------------------------------
-- Lote dos produtos.
-- ------------------------------------------------------------

CREATE TABLE PRODUTO_LOTE (
  ID SERIAL  NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  ID_PRODUTO INTEGER   NOT NULL ,
  CODIGO VARCHAR(50)    ,
  DATA_CADASTRO DATE    ,
  DATA_COMPRA DATE    ,
  DATA_FABRICACAO DATE    ,
  DATA_VALIDADE DATE    ,
  QUANTIDADE DECIMAL(18,6)    ,
  PRECO_MAXIMO_CONSUMIDOR DECIMAL(18,6)    ,
  OBSERVACAO TEXT      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID),
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID));


CREATE INDEX FK_PRODUTO_LOTE ON PRODUTO_LOTE (ID_PRODUTO);
CREATE INDEX FK_NFE_CAB_PROD_LOTE ON PRODUTO_LOTE (ID_NFE_CABECALHO);



-- ------------------------------------------------------------
-- Armazena os itens da nota fiscal.
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_PRODUTO INTEGER    ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  NUMERO_ITEM INTEGER    ,
  CODIGO_PRODUTO VARCHAR(60)    ,
  GTIN VARCHAR(14)    ,
  NOME_PRODUTO VARCHAR(120)    ,
  NCM VARCHAR(8)    ,
  NVE VARCHAR(6)    ,
  EX_TIPI INTEGER    ,
  CFOP INTEGER    ,
  UNIDADE_COMERCIAL VARCHAR(6)    ,
  QUANTIDADE_COMERCIAL DECIMAL(18,6)    ,
  VALOR_UNITARIO_COMERCIAL DECIMAL(18,6)    ,
  VALOR_BRUTO_PRODUTO DECIMAL(18,6)    ,
  GTIN_UNIDADE_TRIBUTAVEL VARCHAR(14)    ,
  UNIDADE_TRIBUTAVEL VARCHAR(6)    ,
  QUANTIDADE_TRIBUTAVEL DECIMAL(18,6)    ,
  VALOR_UNITARIO_TRIBUTAVEL DECIMAL(18,6)    ,
  VALOR_FRETE DECIMAL(18,6)    ,
  VALOR_SEGURO DECIMAL(18,6)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  VALOR_OUTRAS_DESPESAS DECIMAL(18,6)    ,
  ENTRA_TOTAL INTEGER    ,
  VALOR_SUBTOTAL DECIMAL(18,6)    ,
  VALOR_TOTAL DECIMAL(18,6)    ,
  NUMERO_PEDIDO_COMPRA VARCHAR(15)    ,
  ITEM_PEDIDO_COMPRA INTEGER    ,
  INFORMACOES_ADICIONAIS TEXT    ,
  NUMERO_FCI VARCHAR(36)    ,
  NUMERO_RECOPI VARCHAR(20)    ,
  VALOR_TOTAL_TRIBUTOS DECIMAL(18,6)    ,
  PERCENTUAL_DEVOLVIDO DECIMAL(18,6)    ,
  VALOR_IPI_DEVOLVIDO DECIMAL(18,6)    ,
  CEST VARCHAR(10)    ,
  INDICADOR_ESCALA_RELEVANTE CHAR(1)    ,
  CNPJ_FABRICANTE VARCHAR(14)    ,
  CODIGO_BENEFICIO_FISCAL VARCHAR(10)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID),
  FOREIGN KEY(ID_PRODUTO)
    REFERENCES PRODUTO(ID));


CREATE INDEX FK_NFE_CAB_DET ON NFE_DETALHE (ID_NFE_CABECALHO);
CREATE INDEX FK_PRODUTO_NFE ON NFE_DETALHE (ID_PRODUTO);




































-- ------------------------------------------------------------
-- Obrigatório o preenchimento do Grupo Informações de Pagamento para NF-e e NFC-e. Para as notas com finalidade de Ajuste ou Devolução o campo Forma de Pagamento deve ser preenchido com 90=Sem Pagamento.
-- ------------------------------------------------------------

CREATE TABLE NFE_FORMA_PAGAMENTO (
  ID SERIAL  NOT NULL ,
  ID_NFCE_TIPO_PAGAMENTO INTEGER   NOT NULL ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  FORMA CHAR(2)    ,
  VALOR DECIMAL(18,6)    ,
  CARTAO_TIPO_INTEGRACAO CHAR(1)    ,
  CNPJ_OPERADORA_CARTAO VARCHAR(14)    ,
  BANDEIRA CHAR(2)    ,
  NUMERO_AUTORIZACAO VARCHAR(20)    ,
  ESTORNO CHAR(1)    ,
  TROCO DECIMAL(18,6)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID),
  FOREIGN KEY(ID_NFCE_TIPO_PAGAMENTO)
    REFERENCES NFCE_TIPO_PAGAMENTO(ID));


CREATE INDEX FK_NFE_FORMA_PAGAMENTO ON NFE_FORMA_PAGAMENTO (ID_NFE_CABECALHO);
CREATE INDEX FK_NFCE_TOTAL_PGTO ON NFE_FORMA_PAGAMENTO (ID_NFCE_TIPO_PAGAMENTO);











-- ------------------------------------------------------------
-- Informações dos transportes.
-- ------------------------------------------------------------

CREATE TABLE NFE_TRANSPORTE (
  ID SERIAL  NOT NULL ,
  ID_TRANSPORTADORA INTEGER    ,
  ID_NFE_CABECALHO INTEGER   NOT NULL ,
  MODALIDADE_FRETE INTEGER    ,
  CPF_CNPJ VARCHAR(14)    ,
  NOME VARCHAR(60)    ,
  INSCRICAO_ESTADUAL VARCHAR(14)    ,
  EMPRESA_ENDERECO VARCHAR(60)    ,
  NOME_MUNICIPIO VARCHAR(60)    ,
  UF CHAR(2)    ,
  VALOR_SERVICO DECIMAL(18,6)    ,
  VALOR_BC_RETENCAO_ICMS DECIMAL(18,6)    ,
  ALIQUOTA_RETENCAO_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS_RETIDO DECIMAL(18,6)    ,
  CFOP INTEGER    ,
  MUNICIPIO INTEGER    ,
  PLACA_VEICULO VARCHAR(7)    ,
  UF_VEICULO CHAR(2)    ,
  RNTC_VEICULO VARCHAR(20)    ,
  VAGAO VARCHAR(20)    ,
  BALSA VARCHAR(20)      ,
PRIMARY KEY(ID)    ,
  FOREIGN KEY(ID_NFE_CABECALHO)
    REFERENCES NFE_CABECALHO(ID),
  FOREIGN KEY(ID_TRANSPORTADORA)
    REFERENCES TRANSPORTADORA(ID));


CREATE INDEX FK_NFE_TRANSPORTE ON NFE_TRANSPORTE (ID_NFE_CABECALHO);
CREATE INDEX FK_TRANSPORTADORA_NFE_TRANSP ON NFE_TRANSPORTE (ID_TRANSPORTADORA);





















-- ------------------------------------------------------------
-- Grupo Reboque - Informar os reboques/Dolly (v2.0)
-- ------------------------------------------------------------

CREATE TABLE NFE_TRANSPORTE_REBOQUE (
  ID SERIAL  NOT NULL ,
  ID_NFE_TRANSPORTE INTEGER   NOT NULL ,
  PLACA VARCHAR(8)    ,
  UF CHAR(2)    ,
  RNTC VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_TRANSPORTE)
    REFERENCES NFE_TRANSPORTE(ID));


CREATE INDEX FK_NFE_TRANSP_REBOQUE ON NFE_TRANSPORTE_REBOQUE (ID_NFE_TRANSPORTE);






-- ------------------------------------------------------------
-- Informações dos transportes - Grupo Volumes
-- ------------------------------------------------------------

CREATE TABLE NFE_TRANSPORTE_VOLUME (
  ID SERIAL  NOT NULL ,
  ID_NFE_TRANSPORTE INTEGER   NOT NULL ,
  QUANTIDADE INTEGER    ,
  ESPECIE VARCHAR(60)    ,
  MARCA VARCHAR(60)    ,
  NUMERACAO VARCHAR(60)    ,
  PESO_LIQUIDO DECIMAL(18,6)    ,
  PESO_BRUTO DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_TRANSPORTE)
    REFERENCES NFE_TRANSPORTE(ID));


CREATE INDEX FK_TRANSP_VOLUME ON NFE_TRANSPORTE_VOLUME (ID_NFE_TRANSPORTE);









-- ------------------------------------------------------------
-- Foi criado  um  grupo  específico para controle de Exportação por  item da NF-e, conforme documentado abaixo  e foram estabelecidas algumas regras de 
-- validação para a verificação da integridade da informação prestada, entre elas:
-- ?  Obrigatoriedade de informação do grupo de detalhes de exportação por item, para alguns CFOP;
-- ?  Obrigatoriedade de informação do número do Drawback para alguns CFOP;
-- ?  No caso da exportação indireta:
-- o  Obrigatoriedade de informação deste grupo, também conforme o CFOP;
-- o  Obrigatoriedade de informação da Chave de Acesso no grupo de NF-e referenciada;
-- o  Obrigatoriedade de existência da Chave de Acesso no banco de dados da SEFAZ.
-- ------------------------------------------------------------

CREATE TABLE NFE_EXPORTACAO (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  DRAWBACK INTEGER    ,
  NUMERO_REGISTRO INTEGER    ,
  CHAVE_ACESSO VARCHAR(44)    ,
  QUANTIDADE DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_EXPORTACAO ON NFE_EXPORTACAO (ID_NFE_DETALHE);






-- ------------------------------------------------------------
-- Grupo de Fornecimento diário de cana - Informar os fornecimentos diários de cana v2.0
-- ------------------------------------------------------------

CREATE TABLE NFE_CANA_FORNECIMENTO_DIARIO (
  ID SERIAL  NOT NULL ,
  ID_NFE_CANA INTEGER   NOT NULL ,
  DIA CHAR(2)    ,
  QUANTIDADE DECIMAL(18,6)    ,
  QUANTIDADE_TOTAL_MES DECIMAL(18,6)    ,
  QUANTIDADE_TOTAL_ANTERIOR DECIMAL(18,6)    ,
  QUANTIDADE_TOTAL_GERAL DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CANA)
    REFERENCES NFE_CANA(ID));


CREATE INDEX FK_NFE_CANA_FORNECIMENTO ON NFE_CANA_FORNECIMENTO_DIARIO (ID_NFE_CANA);








-- ------------------------------------------------------------
-- Declaração de Importação - Informar dados da importação (NT 2011/004)
-- ------------------------------------------------------------

CREATE TABLE NFE_DECLARACAO_IMPORTACAO (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  NUMERO_DOCUMENTO VARCHAR(12)    ,
  DATA_REGISTRO DATE    ,
  LOCAL_DESEMBARACO VARCHAR(60)    ,
  UF_DESEMBARACO CHAR(2)    ,
  DATA_DESEMBARACO DATE    ,
  CODIGO_EXPORTADOR VARCHAR(60)    ,
  VIA_TRANSPORTE INTEGER    ,
  VALOR_AFRMM DECIMAL(18,6)    ,
  FORMA_INTERMEDIACAO INTEGER    ,
  CNPJ VARCHAR(14)    ,
  UF_TERCEIRO CHAR(2)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_DEC_IMPORTACAO ON NFE_DECLARACAO_IMPORTACAO (ID_NFE_DETALHE);














-- ------------------------------------------------------------
-- Grupo de Deduções , Taxas e Contribuições
-- ------------------------------------------------------------

CREATE TABLE NFE_CANA_DEDUCOES_SAFRA (
  ID SERIAL  NOT NULL ,
  ID_NFE_CANA INTEGER   NOT NULL ,
  DECRICAO VARCHAR(60)    ,
  VALOR_DEDUCAO DECIMAL(18,6)    ,
  VALOR_FORNECIMENTO DECIMAL(18,6)    ,
  VALOR_TOTAL_DEDUCAO DECIMAL(18,6)    ,
  VALOR_LIQUIDO_FORNECIMENTO DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_CANA)
    REFERENCES NFE_CANA(ID));


CREATE INDEX FK_NFE_CANA_DEDUCOES ON NFE_CANA_DEDUCOES_SAFRA (ID_NFE_CANA);








-- ------------------------------------------------------------
-- Grupo do detalhamento de Medicamentos e de matérias-primas farmacêuticas - Informar apenas quando se tratar de medicamentos ou de matérias-primas farmacêuticas, permite múltiplas ocorrências (ilimitado)
-- ------------------------------------------------------------

CREATE TABLE NFE_DET_ESPECIFICO_MEDICAMENTO (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  PRECO_MAXIMO_CONSUMIDOR DECIMAL(18,6)    ,
  CODIGO_PRODUTO_ANVISA VARCHAR(13)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_ESP_MEDICAMENTO ON NFE_DET_ESPECIFICO_MEDICAMENTO (ID_NFE_DETALHE);





-- ------------------------------------------------------------
-- Grupo de informações específicas para combustíveis líquidos e lubrificantes - Informar apenas para operações com combustíveis líquidos e lubrificantes.
-- ------------------------------------------------------------

CREATE TABLE NFE_DET_ESPECIFICO_COMBUSTIVEL (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  CODIGO_ANP INTEGER    ,
  CODIF VARCHAR(21)    ,
  QUANTIDADE_TEMP_AMBIENTE DECIMAL(18,6)    ,
  UF_CONSUMO CHAR(2)    ,
  BASE_CALCULO_CIDE DECIMAL(18,6)    ,
  ALIQUOTA_CIDE DECIMAL(18,6)    ,
  VALOR_CIDE DECIMAL(18,6)    ,
  DESCRICAO_PRODUTO_ANP VARCHAR(95)    ,
  PERCENTUAL_PETROLEO DECIMAL(18,6)    ,
  PERCENTUAL_NACIONAL DECIMAL(18,6)    ,
  PERCENTUAL_IMPORTADO DECIMAL(18,6)    ,
  VALOR_PARTIDA DECIMAL(18,6)    ,
  NUMERO_IDENTIFICACAO_BICO INTEGER    ,
  NUMERO_IDENTIFICACAO_BOMBA INTEGER    ,
  NUMERO_IDENTIFICACAO_TANQUE INTEGER    ,
  VALOR_ENCERRANTE_INICIO DECIMAL(18,6)    ,
  VALOR_ENCERRANTE_FIM DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_ESP_COMBUSTIVEL ON NFE_DET_ESPECIFICO_COMBUSTIVEL (ID_NFE_DETALHE);




















-- ------------------------------------------------------------
-- Grupo do detalhamento de Armamento - Informar apenas quando se tratar de armamento, permite múltiplas ocorrências (ilimitado)
-- ------------------------------------------------------------

CREATE TABLE NFE_DET_ESPECIFICO_ARMAMENTO (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  TIPO_ARMA INTEGER    ,
  NUMERO_SERIE_ARMA VARCHAR(15)    ,
  NUMERO_SERIE_CANO VARCHAR(15)    ,
  DESCRICAO VARCHAR(250)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_ESP_ARMAMENTO ON NFE_DET_ESPECIFICO_ARMAMENTO (ID_NFE_DETALHE);







-- ------------------------------------------------------------
-- Grupo do detalhamento de Veículos novos. Informar apenas quando se tratar de veículos novos
-- ------------------------------------------------------------

CREATE TABLE NFE_DET_ESPECIFICO_VEICULO (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  TIPO_OPERACAO CHAR(1)    ,
  CHASSI VARCHAR(17)    ,
  COR VARCHAR(4)    ,
  DESCRICAO_COR VARCHAR(40)    ,
  POTENCIA_MOTOR VARCHAR(4)    ,
  CILINDRADAS VARCHAR(4)    ,
  PESO_LIQUIDO VARCHAR(9)    ,
  PESO_BRUTO VARCHAR(9)    ,
  NUMERO_SERIE VARCHAR(9)    ,
  TIPO_COMBUSTIVEL CHAR(2)    ,
  NUMERO_MOTOR VARCHAR(21)    ,
  CAPACIDADE_MAXIMA_TRACAO VARCHAR(9)    ,
  DISTANCIA_EIXOS VARCHAR(4)    ,
  ANO_MODELO CHAR(4)    ,
  ANO_FABRICACAO CHAR(4)    ,
  TIPO_PINTURA CHAR(1)    ,
  TIPO_VEICULO CHAR(2)    ,
  ESPECIE_VEICULO CHAR(1)    ,
  CONDICAO_VIN CHAR(1)    ,
  CONDICAO_VEICULO CHAR(1)    ,
  CODIGO_MARCA_MODELO VARCHAR(6)    ,
  CODIGO_COR CHAR(2)    ,
  LOTACAO INTEGER    ,
  RESTRICAO CHAR(1)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_ESPECIFICO_VEICULO ON NFE_DET_ESPECIFICO_VEICULO (ID_NFE_DETALHE);



























-- ------------------------------------------------------------
-- Grupo I80. Rastreabilidade de produto
-- Criação de novo grupo para permitir a rastreabilidade de qualquer produto sujeito a regulações sanitárias, casos de recolhimento/recall, além de
-- defensivos agrícolas, produtos veterinários, odontológicos, medicamentos, bebidas, águas envasadas, embalagens, etc., a partir da indicação de
-- informações de número de lote, data de fabricação/produção, data de validade, etc.
-- Obrigatório o preenchimento deste grupo no caso de medicamentos e produtos farmacêuticos.
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_RASTRO (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  NUMERO_LOTE VARCHAR(20)    ,
  QUANTIDADE_LOTE DECIMAL(18,6)    ,
  DATA_FABRICACAO DATE    ,
  DATA_VALIDADE DATE    ,
  CODIGO_AGREGACAO VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DETALHE_RASTRO ON NFE_DETALHE_RASTRO (ID_NFE_DETALHE);








-- ------------------------------------------------------------
-- Armazena os itens da venda.
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_IMPOSTO_II (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  VALOR_BC_II DECIMAL(18,6)    ,
  VALOR_DESPESAS_ADUANEIRAS DECIMAL(18,6)    ,
  VALOR_IMPOSTO_IMPORTACAO DECIMAL(18,6)    ,
  VALOR_IOF DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_II ON NFE_DETALHE_IMPOSTO_II (ID_NFE_DETALHE);







-- ------------------------------------------------------------
-- Imposto - ICMS
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_IMPOSTO_ICMS (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  ORIGEM_MERCADORIA INTEGER    ,
  CST_ICMS CHAR(2)    ,
  CSOSN CHAR(3)    ,
  MODALIDADE_BC_ICMS INTEGER    ,
  TAXA_REDUCAO_BC_ICMS DECIMAL(18,6)    ,
  BASE_CALCULO_ICMS DECIMAL(18,6)    ,
  ALIQUOTA_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS DECIMAL(18,6)    ,
  VALOR_ICMS_OPERACAO DECIMAL(18,6)    ,
  PERCENTUAL_DIFERIMENTO DECIMAL(18,6)    ,
  VALOR_ICMS_DIFERIDO DECIMAL(18,6)    ,
  MOTIVO_DESONERACAO_ICMS INTEGER    ,
  VALOR_ICMS_DESONERADO DECIMAL(18,6)    ,
  MODALIDADE_BC_ICMS_ST INTEGER    ,
  PERCENTUAL_MVA_ICMS_ST DECIMAL(18,6)    ,
  PERCENTUAL_REDUCAO_BC_ICMS_ST DECIMAL(18,6)    ,
  VALOR_BASE_CALCULO_ICMS_ST DECIMAL(18,6)    ,
  ALIQUOTA_ICMS_ST DECIMAL(18,6)    ,
  VALOR_ICMS_ST DECIMAL(18,6)    ,
  VALOR_BC_ICMS_ST_RETIDO DECIMAL(18,6)    ,
  VALOR_ICMS_ST_RETIDO DECIMAL(18,6)    ,
  VALOR_BC_ICMS_ST_DESTINO DECIMAL(18,6)    ,
  VALOR_ICMS_ST_DESTINO DECIMAL(18,6)    ,
  ALIQUOTA_CREDITO_ICMS_SN DECIMAL(18,6)    ,
  VALOR_CREDITO_ICMS_SN DECIMAL(18,6)    ,
  PERCENTUAL_BC_OPERACAO_PROPRIA DECIMAL(18,6)    ,
  UF_ST CHAR(2)    ,
  VALOR_BC_FCP DECIMAL(18,6)    ,
  PERCENTUAL_FCP DECIMAL(18,6)    ,
  VALOR_FCP DECIMAL(18,6)    ,
  VALOR_BC_FCP_ST DECIMAL(18,6)    ,
  PERCENTUAL_BC_FCP_ST DECIMAL(18,6)    ,
  VALOR_FCP_ST DECIMAL(18,6)    ,
  FCP_CONSUMIDOR_FINAL DECIMAL(18,6)    ,
  VALOR_BC_FCP_RETIDO DECIMAL(18,6)    ,
  PERCENTUAL_BC_FCP_RETIDO DECIMAL(18,6)    ,
  VALOR_FCP_RETIDO DECIMAL(18,6)    ,
  VALOR_BC_FCP_UF_DESTINO DECIMAL(18,6)    ,
  VALOR_BC_ICMS_UF_DESTINO DECIMAL(18,6)    ,
  PERCENTUAL_FCP_UF_DESTINO DECIMAL(18,6)    ,
  ALIQUOTA_INTERNA_UF_DESTINO DECIMAL(18,6)    ,
  ALIQUOTA_INTERESTADUAL_UFS DECIMAL(18,6)    ,
  PERCENTUAL_PROVISORIO_PARTILHA DECIMAL(18,6)    ,
  VALOR_ICMS_FCP_UF_DESTINO DECIMAL(18,6)    ,
  VALOR_ICMS_INTER_UF_DESTINO DECIMAL(18,6)    ,
  VALOR_ICMS_INTER_UF_REMETENTE DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_ICMS ON NFE_DETALHE_IMPOSTO_ICMS (ID_NFE_DETALHE);

















































-- ------------------------------------------------------------
-- Imposto - COFINS
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_IMPOSTO_COFINS (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  CST_COFINS CHAR(2)    ,
  QUANTIDADE_VENDIDA DECIMAL(18,6)    ,
  BASE_CALCULO_COFINS DECIMAL(18,6)    ,
  ALIQUOTA_COFINS_PERCENTUAL DECIMAL(18,6)    ,
  ALIQUOTA_COFINS_REAIS DECIMAL(18,6)    ,
  VALOR_COFINS DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_COFINS ON NFE_DETALHE_IMPOSTO_COFINS (ID_NFE_DETALHE);









-- ------------------------------------------------------------
-- Imposto - PIS
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_IMPOSTO_PIS (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  CST_PIS CHAR(2)    ,
  QUANTIDADE_VENDIDA DECIMAL(18,6)    ,
  VALOR_BASE_CALCULO_PIS DECIMAL(18,6)    ,
  ALIQUOTA_PIS_PERCENTUAL DECIMAL(18,6)    ,
  ALIQUOTA_PIS_REAIS DECIMAL(18,6)    ,
  VALOR_PIS DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_PIS ON NFE_DETALHE_IMPOSTO_PIS (ID_NFE_DETALHE);









-- ------------------------------------------------------------
-- Imposto - ISSQN
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_IMPOSTO_ISSQN (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  BASE_CALCULO_ISSQN DECIMAL(18,6)    ,
  ALIQUOTA_ISSQN DECIMAL(18,6)    ,
  VALOR_ISSQN DECIMAL(18,6)    ,
  MUNICIPIO_ISSQN INTEGER    ,
  ITEM_LISTA_SERVICOS INTEGER    ,
  VALOR_DEDUCAO DECIMAL(18,6)    ,
  VALOR_OUTRAS_RETENCOES DECIMAL(18,6)    ,
  VALOR_DESCONTO_INCONDICIONADO DECIMAL(18,6)    ,
  VALOR_DESCONTO_CONDICIONADO DECIMAL(18,6)    ,
  VALOR_RETENCAO_ISS DECIMAL(18,6)    ,
  INDICADOR_EXIGIBILIDADE_ISS INTEGER    ,
  CODIGO_SERVICO VARCHAR(20)    ,
  MUNICIPIO_INCIDENCIA INTEGER    ,
  PAIS_SEVICO_PRESTADO INTEGER    ,
  NUMERO_PROCESSO VARCHAR(30)    ,
  INDICADOR_INCENTIVO_FISCAL INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_ISSQN ON NFE_DETALHE_IMPOSTO_ISSQN (ID_NFE_DETALHE);



















-- ------------------------------------------------------------
-- Imposto - IPI
-- ------------------------------------------------------------

CREATE TABLE NFE_DETALHE_IMPOSTO_IPI (
  ID SERIAL  NOT NULL ,
  ID_NFE_DETALHE INTEGER   NOT NULL ,
  ENQUADRAMENTO_IPI VARCHAR(5)    ,
  CNPJ_PRODUTOR VARCHAR(14)    ,
  CODIGO_SELO_IPI VARCHAR(60)    ,
  QUANTIDADE_SELO_IPI INTEGER    ,
  ENQUADRAMENTO_LEGAL_IPI CHAR(3)    ,
  CST_IPI CHAR(2)    ,
  VALOR_BASE_CALCULO_IPI DECIMAL(18,6)    ,
  ALIQUOTA_IPI DECIMAL(18,6)    ,
  QUANTIDADE_UNIDADE_TRIBUTAVEL DECIMAL(18,6)    ,
  VALOR_UNIDADE_TRIBUTAVEL DECIMAL(18,6)    ,
  VALOR_IPI DECIMAL(18,6)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DETALHE)
    REFERENCES NFE_DETALHE(ID));


CREATE INDEX FK_NFE_DET_IPI ON NFE_DETALHE_IMPOSTO_IPI (ID_NFE_DETALHE);














-- ------------------------------------------------------------
-- Adições
-- ------------------------------------------------------------

CREATE TABLE NFE_IMPORTACAO_DETALHE (
  ID SERIAL  NOT NULL ,
  ID_NFE_DECLARACAO_IMPORTACAO INTEGER   NOT NULL ,
  NUMERO_ADICAO INTEGER    ,
  NUMERO_SEQUENCIAL INTEGER    ,
  CODIGO_FABRICANTE_ESTRANGEIRO VARCHAR(60)    ,
  VALOR_DESCONTO DECIMAL(18,6)    ,
  DRAWBACK INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_DECLARACAO_IMPORTACAO)
    REFERENCES NFE_DECLARACAO_IMPORTACAO(ID));


CREATE INDEX FK_NFE_IMPORTACAO_DETALHE ON NFE_IMPORTACAO_DETALHE (ID_NFE_DECLARACAO_IMPORTACAO);








-- ------------------------------------------------------------
-- Informações dos transportes - Grupo de Lacres
-- ------------------------------------------------------------

CREATE TABLE NFE_TRANSPORTE_VOLUME_LACRE (
  ID SERIAL  NOT NULL ,
  ID_NFE_TRANSPORTE_VOLUME INTEGER   NOT NULL ,
  NUMERO VARCHAR(60)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID_NFE_TRANSPORTE_VOLUME)
    REFERENCES NFE_TRANSPORTE_VOLUME(ID));


CREATE INDEX FK_NFE_TRANSP_VOL_LACRE ON NFE_TRANSPORTE_VOLUME_LACRE (ID_NFE_TRANSPORTE_VOLUME);


CREATE TABLE OS_STATUS (
  ID     SERIAL NOT NULL,
  CODIGO CHAR(2),
  NOME   VARCHAR(100),
  PRIMARY KEY (ID)
);

-- ------------------------------------------------------------
-- Cadastro do equipamentos que são atendidos pela empresa.
--
-- Exemplo:
--
-- Nome: Notebook Dell
-- Desrição: Intel i3, 8GB RAM, 1TB HD, Tela 15,6
-- ------------------------------------------------------------

CREATE TABLE OS_EQUIPAMENTO (
  ID        SERIAL NOT NULL,
  NOME      VARCHAR(100),
  DESCRICAO TEXT,
  PRIMARY KEY (ID)
);

-- ------------------------------------------------------------
-- Aberta da ordem de Servico
-- ------------------------------------------------------------

CREATE TABLE OS_ABERTURA (
  ID                           SERIAL  NOT NULL,
  ID_OS_STATUS                 INTEGER NOT NULL,
  ID_COLABORADOR               INTEGER NOT NULL,
  ID_CLIENTE                   INTEGER NOT NULL,
  ID_EMPRESA                   INTEGER NOT NULL,
  ID_VENDA_CONDICOES_PAGAMENTO INTEGER NOT NULL,
  NUMERO                       VARCHAR(20),
  DATA_INICIO                  DATE,
  HORA_INICIO                  VARCHAR(8),
  DATA_PREVISAO                DATE,
  HORA_PREVISAO                VARCHAR(8),
  DATA_FIM                     DATE,
  HORA_FIM                     VARCHAR(8),
  NOME_CONTATO                 VARCHAR(50),
  FONE_CONTATO                 VARCHAR(15),
  OBSERVACAO_CLIENTE           TEXT,
  OBSERVACAO_ABERTURA          TEXT,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTE (ID),
  FOREIGN KEY (ID_COLABORADOR) REFERENCES COLABORADOR (ID),
  FOREIGN KEY (ID_OS_STATUS) REFERENCES OS_STATUS (ID),
  FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID),
  FOREIGN KEY (ID_VENDA_CONDICOES_PAGAMENTO) REFERENCES VENDA_CONDICOES_PAGAMENTO (ID)
);


CREATE INDEX FK_CLIENTE_OS
  ON OS_ABERTURA (ID_CLIENTE);
CREATE INDEX FK_COLABORADOR_OS
  ON OS_ABERTURA (ID_COLABORADOR);
CREATE INDEX FK_OS_STATUS_ABERTURA
  ON OS_ABERTURA (ID_OS_STATUS);
CREATE INDEX FK_EMPRESA_ABERTURA
  ON OS_ABERTURA (ID_EMPRESA);
;
CREATE INDEX FK_CONDICOES_OS_ABERTURA
  ON OS_ABERTURA (ID_VENDA_CONDICOES_PAGAMENTO);

-- ------------------------------------------------------------
-- Equipamentos que estão sendo atendidos na OS
-- ------------------------------------------------------------

CREATE TABLE OS_ABERTURA_EQUIPAMENTO (
  ID                SERIAL  NOT NULL,
  ID_OS_EQUIPAMENTO INTEGER NOT NULL,
  ID_OS_ABERTURA    INTEGER NOT NULL,
  NUMERO_SERIE      VARCHAR(50),
  TIPO_COBERTURA    INTEGER,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_OS_ABERTURA)
  REFERENCES OS_ABERTURA (ID),
  FOREIGN KEY (ID_OS_EQUIPAMENTO)
  REFERENCES OS_EQUIPAMENTO (ID)
);


CREATE INDEX FK_OS_ABERT_EQUIP
  ON OS_ABERTURA_EQUIPAMENTO (ID_OS_ABERTURA);
CREATE INDEX FK_OS_EQUIP_ABERT
  ON OS_ABERTURA_EQUIPAMENTO (ID_OS_EQUIPAMENTO);

CREATE TABLE OS_EVOLUCAO (
  ID             SERIAL  NOT NULL,
  ID_OS_ABERTURA INTEGER NOT NULL,
  DATA_REGISTRO  DATE,
  HORA_REGISTRO  VARCHAR(8),
  OBSERVACAO     TEXT,
  ENVIAR_EMAIL   CHAR(1),
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_OS_ABERTURA)
  REFERENCES OS_ABERTURA (ID)
);


CREATE INDEX OS_EVOLUCAO_FKIndex1
  ON OS_EVOLUCAO (ID_OS_ABERTURA);


CREATE TABLE OS_PRODUTO_SERVICO (
  ID             SERIAL  NOT NULL,
  ID_PRODUTO     INTEGER NOT NULL,
  ID_OS_ABERTURA INTEGER NOT NULL,
  TIPO           INTEGER,
  COMPLEMENTO    TEXT,
  QUANTIDADE     DECIMAL(18, 6),
  VALOR_UNITARIO DECIMAL(18, 6),
  VALOR_SUBTOTAL DECIMAL(18, 6),
  TAXA_DESCONTO  DECIMAL(18, 6),
  VALOR_DESCONTO DECIMAL(18, 6),
  VALOR_TOTAL    DECIMAL(18, 6),
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_OS_ABERTURA)
  REFERENCES OS_ABERTURA (ID),
  FOREIGN KEY (ID_PRODUTO)
  REFERENCES PRODUTO (ID)
);


CREATE INDEX FK_OS_ABERT_PROD_SERV
  ON OS_PRODUTO_SERVICO (ID_OS_ABERTURA);
CREATE INDEX FK_PROD_OS
  ON OS_PRODUTO_SERVICO (ID_PRODUTO);

-- ------------------------------------------------------------
-- fiscal_apuracao_icms guarda os dados referente a apurtação o ICMS
-- ------------------------------------------------------------

CREATE TABLE fiscal_apuracao_icms
(
  id                          SERIAL  NOT NULL,
  id_empresa                  INTEGER NOT NULL,
  competencia                 CHARACTER VARYING(7),
  valor_total_debito          NUMERIC(18, 6),
  valor_ajuste_debito         NUMERIC(18, 6),
  valor_total_ajuste_debito   NUMERIC(18, 6),
  valor_estorno_credito       NUMERIC(18, 6),
  valor_total_credito         NUMERIC(18, 6),
  valor_ajuste_credito        NUMERIC(18, 6),
  valor_total_ajuste_credito  NUMERIC(18, 6),
  valor_estorno_debito        NUMERIC(18, 6),
  valor_saldo_credor_anterior NUMERIC(18, 6),
  valor_saldo_apurado         NUMERIC(18, 6),
  valor_total_deducao         NUMERIC(18, 6),
  valor_icms_recolher         NUMERIC(18, 6),
  valor_saldo_credor_transp   NUMERIC(18, 6),
  valor_debito_especial       NUMERIC(18, 6),
  CONSTRAINT fiscal_apuracao_icms_pkey PRIMARY KEY (id),
  CONSTRAINT fiscal_apuracao_icms_id_empresa_fkey FOREIGN KEY (id_empresa)
  REFERENCES public.empresa (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);