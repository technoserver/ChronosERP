package com.chronos.cadastros;

import com.chronos.modelo.entidades.ProdutoAtributo;
import com.chronos.modelo.entidades.ProdutoAtributoDetalhe;
import com.chronos.modelo.entidades.ProdutoGrade;
import com.chronos.modelo.entidades.ProdutoGradeDetalhe;
import com.chronos.util.Biblioteca;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoGradeTest {

    private ProdutoGrade grade;


    @Before
    public void setUp() {

        grade = new ProdutoGrade();
        ProdutoGradeDetalhe gradeDetalhe1 = new ProdutoGradeDetalhe();
        ProdutoGradeDetalhe gradeDetalhe2 = new ProdutoGradeDetalhe();


        ProdutoAtributo atributoCor = new ProdutoAtributo();
        atributoCor.setId(1);
        ProdutoAtributo atributoTam = new ProdutoAtributo();
        atributoTam.setId(2);

        ProdutoAtributoDetalhe atributoDetalheCor1 = new ProdutoAtributoDetalhe();
        ProdutoAtributoDetalhe atributoDetalheCor2 = new ProdutoAtributoDetalhe();
        ProdutoAtributoDetalhe atributoDetalheCor3 = new ProdutoAtributoDetalhe();
        ProdutoAtributoDetalhe atributoDetalheCor4 = new ProdutoAtributoDetalhe();

        ProdutoAtributoDetalhe atributoDetalheTam1 = new ProdutoAtributoDetalhe();
        ProdutoAtributoDetalhe atributoDetalheTam2 = new ProdutoAtributoDetalhe();
        ProdutoAtributoDetalhe atributoDetalheTam3 = new ProdutoAtributoDetalhe();


        grade.setNome("CAMISA");
        grade.setListaProdutoGradeDetalhe(new ArrayList<>());
        gradeDetalhe1.setProdutoAtributo(atributoCor);
        gradeDetalhe2.setProdutoAtributo(atributoTam);
        grade.getListaProdutoGradeDetalhe().add(gradeDetalhe1);
        grade.getListaProdutoGradeDetalhe().add(gradeDetalhe2);


        atributoDetalheTam1.setNome("P");
        atributoDetalheTam2.setNome("M");
        atributoDetalheTam3.setNome("G");
        atributoDetalheTam1.setProdutoAtributo(atributoTam);
        atributoDetalheTam2.setProdutoAtributo(atributoTam);
        atributoDetalheTam3.setProdutoAtributo(atributoTam);

        atributoDetalheCor1.setNome("AZUL");
        atributoDetalheCor2.setNome("VERDE");
        atributoDetalheCor3.setNome("VERMELHO");
        atributoDetalheCor4.setNome("ROSA");

        atributoDetalheCor1.setProdutoAtributo(atributoCor);
        atributoDetalheCor2.setProdutoAtributo(atributoCor);
        atributoDetalheCor3.setProdutoAtributo(atributoCor);
        atributoDetalheCor4.setProdutoAtributo(atributoCor);

        atributoTam.setListaProdutoAtributoDetalhe(new ArrayList<>());
        atributoTam.setSigla("TAM");
        atributoTam.getListaProdutoAtributoDetalhe().add(atributoDetalheTam1);
        atributoTam.getListaProdutoAtributoDetalhe().add(atributoDetalheTam2);
        atributoTam.getListaProdutoAtributoDetalhe().add(atributoDetalheTam3);

        atributoCor.setListaProdutoAtributoDetalhe(new ArrayList<>());
        atributoCor.setSigla("COR");
        atributoCor.getListaProdutoAtributoDetalhe().add(atributoDetalheCor1);
        atributoCor.getListaProdutoAtributoDetalhe().add(atributoDetalheCor2);
        atributoCor.getListaProdutoAtributoDetalhe().add(atributoDetalheCor3);
        atributoCor.getListaProdutoAtributoDetalhe().add(atributoDetalheCor4);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_todas_as_combinacoes() {

        List<List<String>> listC = new ArrayList<>();
        List<String> result = new ArrayList<>();

        for (ProdutoGradeDetalhe g : grade.getListaProdutoGradeDetalhe()) {

            List<String> list = g.getProdutoAtributo().getListaProdutoAtributoDetalhe()
                    .stream()
                    .map(a -> (a.getProdutoAtributo().getSigla() + "=" + a.getNome() + "; "))
                    .collect(Collectors.toList());
            listC.add(list);

        }


        Biblioteca.generateCombination(listC, result, 0, "");


    }
}
