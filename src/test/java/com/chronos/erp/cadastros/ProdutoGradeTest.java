package com.chronos.erp.cadastros;

import com.chronos.erp.dto.GradeDTO;
import com.chronos.erp.modelo.entidades.ProdutoAtributo;
import com.chronos.erp.modelo.entidades.ProdutoAtributoDetalhe;
import com.chronos.erp.modelo.entidades.ProdutoGrade;
import com.chronos.erp.modelo.entidades.ProdutoGradeDetalhe;
import com.chronos.erp.util.Biblioteca;
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
        grade.setId(1);
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
        atributoDetalheTam1.setId(1);
        atributoDetalheTam2.setNome("M");
        atributoDetalheTam2.setId(2);
        atributoDetalheTam3.setNome("G");
        atributoDetalheTam3.setId(3);

        atributoDetalheTam1.setProdutoAtributo(atributoTam);
        atributoDetalheTam2.setProdutoAtributo(atributoTam);
        atributoDetalheTam3.setProdutoAtributo(atributoTam);

        atributoDetalheCor1.setNome("AZUL");
        atributoDetalheCor2.setNome("VERDE");
        atributoDetalheCor3.setNome("VERMELHO");
        atributoDetalheCor4.setNome("ROSA");

        atributoDetalheCor1.setProdutoAtributo(atributoCor);
        atributoDetalheCor1.setId(4);
        atributoDetalheCor2.setProdutoAtributo(atributoCor);
        atributoDetalheCor2.setId(5);
        atributoDetalheCor3.setProdutoAtributo(atributoCor);
        atributoDetalheCor3.setId(6);
        atributoDetalheCor4.setProdutoAtributo(atributoCor);
        atributoDetalheCor4.setId(7);

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

        List<List<GradeDTO>> listC = new ArrayList<>();
        List<GradeDTO> result = new ArrayList<>();

        for (ProdutoGradeDetalhe g : grade.getListaProdutoGradeDetalhe()) {

            List<GradeDTO> list = g.getProdutoAtributo().getListaProdutoAtributoDetalhe()
                    .stream()
                    .map(a -> new GradeDTO("A" + a.getProdutoAtributo().getId() + "." + a.getId().toString(), a.getProdutoAtributo().getSigla() + "=" + a.getNome() + "; "))
                    .collect(Collectors.toList());

            listC.add(list);

        }


        Biblioteca.generateCombinations(listC, result, 0, new GradeDTO("", ""));


    }
}
