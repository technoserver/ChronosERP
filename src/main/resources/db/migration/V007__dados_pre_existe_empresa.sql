
/*
	Dados de teste para tributação
*/

insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('1','1','VENDA A CONSUMIDOR FINAL','VENDA',NULL,'1. Venda a  não contribuinte. Ex. Consumidor final, Construtora ou empresas que comprem os produtos para uso próprio.');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('2','1','VENDA PARA LOJISTAS - REVENDEDOR','VENDA',NULL,'2. Venda para Lojistas (Revendedor)');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('3','1','VENDA PARA CONSUMIDOR FORA DO ESTADO','VENDA',NULL,'3. Venda Consumidor fora do Estado');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('4','1','VENDA A CONTRIBUINTE USUARIO FINAL','VENDA',NULL,'4. Venda a Contribuinte Usuário final (ex. construtora, empresa que comprem para consumo)');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('5','1','VENDA LOJISTA FORA DO ESTADO','VENDA',NULL,'5. Venda Lojista fora do Estado');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('6','1','SERVICO DENTRO DO ESTADO','PRESTACAO DE SERVICO','5933','NFE DE SERVICOS PARA DENTRO DO ESTADO');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('7','1','SERVICO FORA DO ESTADO','PRESTACAO DE SERVICO','6933','NFE DE SERVICOS PARA FORA DO ESTADO');
insert into tribut_operacao_fiscal (ID, ID_EMPRESA, DESCRICAO, DESCRICAO_NA_NF, CFOP, OBSERVACAO) values('8','1','VENDA PARA CONSUMIDOR DENTRO DO ESTADO','VENDA',NULL,'3. Venda Consumidor fora do Estado');




insert into tribut_grupo_tributario (ID, ID_EMPRESA, DESCRICAO, ORIGEM_MERCADORIA, OBSERVACAO) values('1','1','PRODUTO FABRICACAO PROPRIA SUJEITO AO ICMS ST','0','1. Produtos de fabricação própria (sujeitos ao ICMS ST)');
insert into tribut_grupo_tributario (ID, ID_EMPRESA, DESCRICAO, ORIGEM_MERCADORIA, OBSERVACAO) values('2','1','PRODUTO PARA REVENDA SUJEITO AO ICMS ST','0','2. Produtos de Revenda (sujeitos ao Regime do ICMS ST)');
insert into tribut_grupo_tributario (ID, ID_EMPRESA, DESCRICAO, ORIGEM_MERCADORIA, OBSERVACAO) values('3','1','PRODUTO PARA REVENDA NAO SUJEITO AO ICMS ST','0','3. Produtos de Revenda Não sujeitos ao ICMS ST');
insert into tribut_grupo_tributario (ID, ID_EMPRESA, DESCRICAO, ORIGEM_MERCADORIA, OBSERVACAO) values('4','1','SERVICO','0','SERVICO');


/*
  CONTA_CAIXA
  -----------
*/
INSERT INTO CONTA_CAIXA (ID, ID_EMPRESA, ID_AGENCIA_BANCO, CODIGO, NOME, DESCRICAO, TIPO) VALUES (1, 1, NULL, 'CXINT', 'CAIXA INTERNO', 'CONTA INTERNA PARA MOVIMENTACAO DO CAIXA DA EMPRESA', 'X');


/*
	FINANCEIRO
*/
insert into fin_status_parcela (ID, ID_EMPRESA, SITUACAO, DESCRICAO, PROCEDIMENTO) values('1','1','01','Aberto',NULL);
insert into fin_status_parcela (ID, ID_EMPRESA, SITUACAO, DESCRICAO, PROCEDIMENTO) values('2','1','02','Quitado',NULL);
insert into fin_status_parcela (ID, ID_EMPRESA, SITUACAO, DESCRICAO, PROCEDIMENTO) values('3','1','03','Quitado Parcial',NULL);
insert into fin_status_parcela (ID, ID_EMPRESA, SITUACAO, DESCRICAO, PROCEDIMENTO) values('4','1','04','Vencido',NULL);

insert into fin_tipo_pagamento (ID, ID_EMPRESA, TIPO, DESCRICAO) values('1','1','01','Dinheiro');
insert into fin_tipo_pagamento (ID, ID_EMPRESA, TIPO, DESCRICAO) values('2','1','02','Cheque');
insert into fin_tipo_pagamento (ID, ID_EMPRESA, TIPO, DESCRICAO) values('3','1','03','Cartao');

insert into fin_tipo_recebimento (ID, ID_EMPRESA, ID_CONTA_CAIXA, TIPO, DESCRICAO) values('1','1','1','01','Dinheiro');
insert into fin_tipo_recebimento (ID, ID_EMPRESA, ID_CONTA_CAIXA, TIPO, DESCRICAO) values('2','1','1','02','Cheque');
insert into fin_tipo_recebimento (ID, ID_EMPRESA, ID_CONTA_CAIXA, TIPO, DESCRICAO) values('3','1','1','03','Cartao');



insert into plano_centro_resultado (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('1','1','PRIMEIRO NIVEL','2016-01-01','#','1');
insert into plano_centro_resultado (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('2','1','SEGUNDO NIVEL','2016-01-01','#.##','2');
insert into plano_centro_resultado (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('3','1','TERCEIRO NIVEL','2016-01-01','#.##.###','3');
insert into plano_centro_resultado (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('4','1','QUARTO NIVEL','2016-01-01','#.##.###.####','4');


insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(1,3,'1.01.000','Diretoria Adm','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(2,3,'1.01.001','Diretor adm','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(3,3,'1.01.002','Secretaria adm','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(4,3,'1.02.000','Financeiro','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(5,3,'1.02.001','Gerencia Financeira','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(6,3,'1.02.002','Operacional financeiro','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(7,3,'2.00.000','Produção','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(8,3,'2.01.000','Diretoria Produção','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(9,3,'2.01.001','Diretor','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(10,3,'2.02.000','Gerencia','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(11,3,'2.02.001','gerente','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(12,3,'2.03.000','Linhas/Barracoes/Fluxo de Produção','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(13,3,'2.03.001','Linha de Produção-1','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(14,3,'2.03.002','Linha de Produção-2','N');
insert into centro_resultado(id,id_plano_centro_resultado,classificacao,descricao,sofre_rateiro) values(15,3,'2.03.003','Equipamento especial','N');


insert into plano_natureza_financeira (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('1','1','PRIMEIRO NIVEL','2016-01-01','#','1');
insert into plano_natureza_financeira (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('2','1','SEGUNDO NIVEL','2016-01-01','#.##','2');
insert into plano_natureza_financeira (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('3','1','TERCEIRO NIVEL','2016-01-01','#.##.###','3');
insert into plano_natureza_financeira (ID, ID_EMPRESA, NOME, DATA_INCLUSAO, MASCARA, NIVEIS) values('4','1','QUARTO NIVEL','2016-01-01','#.##.###.####','4');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(1,1,'1','DESPESAS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(2,2,1,'1.01','DESPESAS ADMINISTRATIVAS E COMERCIAIS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(3,3,2,'1.01.001','ALUGUEL','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(4,3,2,'1.01.002','ASSESSORIAS E ASSOCIAÇÕES','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(5,3,2,'1.01.003','CARTÓRIO','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(6,3,2,'1.01.004','COMBUSTIVEL E TRANSLADOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(7,3,2,'1.01.005','CONFRATERNIZAÇÕES','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(8,3,2,'1.01.006','CONTABILIDADE','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(9,3,2,'1.01.007','CORREIOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(10,3,2,'1.01.008','CURSOS E TREINAMENTOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(11,3,2,'1.01.009','DISTRIBUIÇÃO DE LUCROS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(12,3,2,'1.01.010','EMPRÉSTIMOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(13,3,2,'1.01.011','ENCARGOS FUNCIONÁRIOS - 13O SALÁRIO','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(14,3,2,'1.01.012','ENCARGOS FUNCIONÁRIOS - ALIMENTAÇÃO','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(15,3,2,'1.01.013','ENCARGOS FUNCIONÁRIOS - ASSIST. MÉDICA E ODONTOL.','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(16,3,2,'1.01.014','ENCARGOS FUNCIONÁRIOS - EXAMES PRÉ E DEMISSIONAIS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(17,3,2,'1.01.015','ENCARGOS FUNCIONÁRIOS - FGTS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(18,3,2,'1.01.016','ENCARGOS FUNCIONARIOS - HORAS EXTRAS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(19,3,2,'1.01.017','ENCARGOS FUNCIONÁRIOS - INSS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(20,3,2,'1.01.018','ENCARGOS FUNCIONÁRIOS - VALE TRANSPORTE','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(21,3,2,'1.01.019','ENCARGOS - RESCISÕES TRABALHISTAS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(22,3,2,'1.01.020','ENERGIA ELÉTRICA + ÁGUA','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(23,3,2,'1.01.021','IMPOSTOS - ALVARÁ','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(24,3,2,'1.01.022','IMPOSTOS - COLETA DE LIXO','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(25,3,2,'1.01.023','IMPOSTOS - IPTU','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(26,3,2,'1.01.024','IMPOSTOS - PIS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(27,3,2,'1.01.025','LICENÇA OU ALUGUEL DE SOFTWARES','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(28,3,2,'1.01.026','LIMPEZA','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(29,3,2,'1.01.027','MANUTENÇÃO EQUIPAMENTOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(30,3,2,'1.01.028','MARKETING E PUBLICIDADE','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(31,3,2,'1.01.029','MATERIAL DE ESCRITÓRIO','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(32,3,2,'1.01.030','MATERIAL REFORMA','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(33,3,2,'1.01.031','REMUNERAÇÃO FUNCIONÁRIOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(34,3,2,'1.01.032','SEGURANÇA','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(35,3,2,'1.01.033','SUPERMERCADO','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(36,3,2,'1.01.034','TELEFONIA E INTERNET','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(37,3,2,'1.01.035','TRANSPORTADORA','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(38,3,2,'1.01.036','VIAGENS','D','','S','N');



INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(39,2,1,'1.02','DESPESAS DE PRODUTOS VENDIDOS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(40,3,39,'1.02.001','COMISSÃO DE VENDEDORES','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(41,3,39,'1.02.002','COMPRAS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(42,3,39,'1.02.003','IMPOSTOS - COFINS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(43,3,39,'1.02.004','IMPOSTOS - CSSL','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(44,3,39,'1.02.005','IMPOSTOS - ICMS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(45,3,39,'1.02.006','IMPOSTOS - IMPORTAÇÃO IPI','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(46,3,39,'1.02.007','IMPOSTOS - IRPJ','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(47,3,39,'1.02.008','IMPOSTOS - ISS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(48,2,1,'1.03','DESPESAS FINANCEIRAS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(49,3,48,'1.03.001','DESPESAS BANCÁRIAS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(50,2,1,'1.04','INVESTIMENTOS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(51,3,50,'1.04.001','AQUISIÇÃO DE EQUIPAMENTOS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(52,2,1,'1.05','OUTRAS DESPESAS','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(53,3,52,'1.05.001','ADIANTAMENTO - FUNCIONÁRIOS','D','','S','N');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(54,3,52,'1.05.002','AJUSTE DE CAIXA','D','','S','N');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(55,1,'2','RECEITAS','R','','N','S');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(56,2,55,'2.01','RECEITAS DE VENDAS','R','','N','S');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(57,3,56,'2.01.001','VENDAS DE PRODUTOS','R','','N','S');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(58,3,56,'2.01.002','VENDAS NO BALCÃO','R','','N','S');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(59,3,56,'2.01.003','PRESTAÇÕES DE SERVIÇOS','R','','N','S');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(60,3,56,'2.01.004','CONTRATOS DE SERVIÇOS','R','','N','S');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(61,3,56,'2.01.005','LOCAÇÃO DE EQUIPAMENTOS','R','','N','S');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(62,2,55,'2.02','RECEITAS FINANCEIRAS','R','','N','S');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(63,3,62,'2.02.001','APLICAÇÕES FINANCEIRAS','R','','N','S');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(64,2,55,'2.03','OUTRAS RECEITAS','R','','N','S');

INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(65,3,64,'2.03.001','AJUSTE DE CAIXA','R','','N','S');
INSERT INTO NATUREZA_FINANCEIRA (ID, ID_PLANO_NATUREZA_FINANCEIRA,ID_NATUREZA_FINANCEIRA, CLASSIFICACAO, DESCRICAO, TIPO, APLICACAO, APARECE_A_PAGAR, APARECE_A_RECEBER) VALUES(66,3,64,'2.03.002','DEVOLUÇÃO DE ADIANTAMENTO','R','','N','S');


/*
	ADM
*/
insert into adm_parametro (ID, ID_EMPRESA, FIN_PARCELA_ABERTO, FIN_PARCELA_QUITADO, FIN_PARCELA_QUITADO_PARCIAL, FIN_TIPO_RECEBIMENTO_EDI, COMPRA_FIN_DOC_ORIGEM, COMPRA_CONTA_CAIXA) values('1','1','1','2','3','2','63','1');



/*
 Condições de Pagamento
*/

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (1,1,1,'A VISTA',0,0);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (2,1,1,'30 DIAS',30,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(2,1,30,100);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (3,1,1,'30/60 DIAS',45,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(3,1,30,50);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(3,2,60,50);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (4,1,1,'30/60/90 DIAS',60,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(4,1,30,33.34);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(4,2,60,33.33);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(4,3,90,33.33);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (5,1,1,'30/60/90/120 DIAS',75,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(5,1,30,25);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(5,2,60,25);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(5,3,90,25);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(5,4,120,25);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (6,1,1,'30/60/90/120/150 DIAS',90,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(6,1,30,20);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(6,2,60,20);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(6,3,90,20);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(6,4,120,20);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(6,5,150,20);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (7,1,1,'30/60/90/120/150/180 DIAS',105,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(7,1,30,17);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(7,2,60,16.6);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(7,3,90,16.6);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(7,4,120,16.6);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(7,5,150,16.6);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(7,6,180,16.6);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (8,1,1,'30/60/90/120/150/210 DIAS',120,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,1,30,14.32);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,2,60,14.28);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,3,90,14.28);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,4,120,14.28);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,5,150,14.28);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,6,180,14.28);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(8,7,210,14.28);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (9,1,1,'30/60/90/120/150/210/240 DIAS',135,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,1,30,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,2,60,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,3,90,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,4,120,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,5,150,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,6,180,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,7,210,12.5);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(9,8,240,12.5);


insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (10,1,1,'30/60/90/120/150/210/240/270 DIAS',150,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,1,30,11.12);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,2,60,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,3,90,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,4,120,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,5,150,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,6,180,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,7,210,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,8,240,11.11);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(10,9,270,11.11);

insert into venda_condicoes_pagamento(id,id_empresa,id_fin_tipo_recebimento,nome,prazo_medio,vista_prazo) values (11,1,1,'30/60/90/120/150/210/240/270/300 DIAS',165,1);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,1,30,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,2,60,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,3,90,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,4,120,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,5,150,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,6,180,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,7,210,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,8,240,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,9,270,10);
insert into venda_condicoes_parcelas(id_venda_condicoes_pagamento,parcela,dias,taxa) values(11,10,270,10);
