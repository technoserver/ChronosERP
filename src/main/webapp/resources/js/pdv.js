/**
 * Created by john on 19/09/17.
 */



$(document).bind('keydown', function (event) {


    if($('#formCentro\\:tela-inicial').css('display') == 'block'){
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

    if($('#formCentro\\:tela-pagamento').css('display') == 'block'){
        if(event.which == 116){
           var formarPagamento =  $('#formCentro\\:PedidoFormaPagamentoId');
           var opcao = $('#formCentro\\:PedidoFormaPagamentoId > option:selected');
           var primeiroRegistro = 1;
           var proximoRegistro;

           proximoRegistro = opcao.next().val();
           if(proximoRegistro){
               formarPagamento.focus().select().val(proximoRegistro).change();
           }else{
               formarPagamento.focus().select().val(primeiroRegistro).change();
           }

            return false;
        }
    }




});
$(document).ready(function () {
    //$('.navbar-minimalize').trigger('click');
    $('.inputs input').addClass('quantidade col-sm-12 col-md-12 col-lg-12 form-control-table');
});
