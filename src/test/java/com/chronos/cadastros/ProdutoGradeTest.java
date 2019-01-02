package com.chronos.cadastros;

import com.chronos.modelo.entidades.ProdutoAtributo;
import com.chronos.modelo.entidades.ProdutoAtributoDetalhe;
import com.chronos.modelo.entidades.ProdutoGrade;
import com.chronos.modelo.entidades.ProdutoGradeDetalhe;
import org.junit.Before;
import org.junit.Test;

public class ProdutoGradeTest {

    private ProdutoGrade grade;
    private ProdutoGradeDetalhe gradeDetalhe;

    private ProdutoAtributo atributo;
    private ProdutoAtributoDetalhe atributoDetalhe;

    @Before
    public void setUp() {

        grade = new ProdutoGrade();
        gradeDetalhe = new ProdutoGradeDetalhe();

        atributo = new ProdutoAtributo();

    }

    @Test
    public void devemos_garantir_que_seja_gerado_todas_as_combinacoes() {

    }
}
