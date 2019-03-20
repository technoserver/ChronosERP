update funcao
set nome = 'OBJETIVO DE COMISSAO'
where nome = 'COBJETIVO DE COMISSAO';

CREATE OR REPLACE FUNCTION retira_acentuacao(p_texto text)
  RETURNS text AS
$BODY$
Select translate($1,
                 'áàâãäåaaaÁÂÃÄÅAAAÀéèêëeeeeeEEEÉEEÈìíîïìiiiÌÍÎÏÌIIIóôõöoooòÒÓÔÕÖOOOùúûüuuuuÙÚÛÜUUUUçÇñÑýÝ',
                 'aaaaaaaaaAAAAAAAAAeeeeeeeeeEEEEEEEiiiiiiiiIIIIIIIIooooooooOOOOOOOOuuuuuuuuUUUUUUUUcCnNyY'
           );
$BODY$
LANGUAGE sql
VOLATILE
COST 100;

update municipio
set nome = (select retira_acentuacao(nome));

ALTER TABLE vendedor
  ADD COLUMN tipo char(1) DEFAULT 'I';