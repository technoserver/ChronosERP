CREATE OR REPLACE VIEW view_nfe_resumo AS
select n.id,
       n.id_empresa,
       n.numero,
       n.codigo_modelo,
       n.data_hora_emissao,
       n.chave_acesso,
       n.digito_chave_acesso,
       (n.chave_acesso || n.digito_chave_acesso)    as chave_acesso_completa,
       COALESCE(d.nome, 'Cliente não identificado') as destinatario,
       n.valor_total,
       n.status_nota,
       n.finalidade_emissao,
       n.tipo_operacao
from nfe_cabecalho n
         left join nfe_destinatario d on d.id_nfe_cabecalho = n.id