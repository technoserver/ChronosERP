/**
 * Created by john on 19/09/17.
 */



$(document).bind('keydown', function (event) {


    if ($('#formCentro\\:tela-inicial').css('display') == 'block') {

        if (event.which == 27) {
            $('#formMenu\\:url-pdv')[0].click();
            return false;
        }

        if (event.which == 112) {
            $('#painel-teclas-atalho').modal('show');
            return false;
        }
        //buscar produto
        if (event.which == 113) {
            $('#formCentro\\:produto_input').val('').focus().select();
            return false;
        }
        //alterar quantidadfe
        if (event.which == 114) {
            $('#PedidoQuantidade').focus().select();
            return false;
        }
        //adcionar produto
        if (event.which == 115) {
            $('#pdv').find('#formCentro\\:salva-produto').click();
            return false;
        }
        //pagar
        if (event.which == 117) {
            $('#formCentro\\:botao-pagar').click();
            return false;
        }
        //aguardar
        if (event.which == 118) {
            $('#aguardar').click();
            return false;
        }
        //cancelar venda
        if (event.which == 119) {
            $('#pdv').find('#formCentro\\:cancelar-venda').click();
            return false;
        }
        //indicar cliente
        if (event.which === 67 && event.shiftKey) {
            $('#pdv').find('#formCentro\\:indicar-cliente').click();
            $('#ClientePfCpf').focus().select();
            return false;

        }
        //indicar vendedor
        if (event.which === 86 && event.shiftKey) {

            $('#pdv').find('#formCentro\\:indicar-vendedor').click();
            $('#PedidoVendedorId').focus().select();
            return false;

        }
    }

    if ($('#formCentro\\:tela-pagamento').css('display') == 'block') {
        if (event.which == 116) {
            var formarPagamento = $('#formCentro\\:PedidoFormaPagamentoId');
            var opcao = $('#formCentro\\:PedidoFormaPagamentoId > option:selected');
            var primeiroRegistro = 1;
            var proximoRegistro;

            proximoRegistro = opcao.next().val();
            if (proximoRegistro) {
                formarPagamento.focus().select().val(proximoRegistro).change();
            } else {
                formarPagamento.focus().select().val(primeiroRegistro).change();
            }

            return false;
        }
        if (event.which == 117) {
            $('#pdv').find('#formCentro\\:botao-confirma-pagamento').click();
            return false;
        }

        if (event.which == 118) {
            $('#pdv').find('#formCentro\\:botao-finalizar').click();
            return false;
        }

        if (event.which == 119) {
            $('#pdv').find('#formCentro\\:botao-cancelar').click();
            return false;
        }


    }

    if ($('#formCentro\\:tela-impressao').css('display') == 'block') {


        if (event.which == 116) {
            var $link = $('#pdv').find('#formCentro\\:imprimir-cupom');
            window.open($link.attr('href'), '_blank');
            return false;
        }

        if (event.which == 117) {
            $('#pdv').find('#formCentro\\:emitir-nfce').click();
            return false;
        }

        if (event.which == 118) {
            $('#pdv').find('#formCentro\\:nova-venda').click();
            return false;
        }
    }


});
$(document).ready(function () {
    //$('.navbar-minimalize').trigger('click');
    $('.inputs input').addClass('quantidade col-sm-12 col-md-12 col-lg-12 form-control-table');
});


function loadLocalStorage() {
    var tipoPesquisa = localStorage.getItem("tipoPesquiaPDV");
    tipoPesquisa = tipoPesquisa ? +tipoPesquisa : 1;
    // var input = tipoPesquisa === 1 ? $('#formCentro\\:tipo-pesquisa\\:0') : $('#formCentro\\:tipo-pesquisa\\:1');
    // input.val(tipoPesquisa)
    // var radio = input.parent().parent().children(".ui-radiobutton-box");
    // radio.trigger("click.selectOneRadio");
    definirTipoPesquisa([{name: 'tipoPesquiaPDV', value: tipoPesquisa}])
}


function setLocalStorage(tipoPesquisa) {

    localStorage.setItem("tipoPesquiaPDV", tipoPesquisa);
}

