/**
 * Created by john on 19/09/17.
 */



$(document).bind('keydown', function (event) {
    if (event.which == 112) {
        $('#painel-teclas-atalho').modal('show');
        return false;
    }
    //buscar produto
    if (event.which == 113) {
        $('#token-input-BuscaProduto').val('').focus().select();
        $('#BuscaProduto').tokenInput('clearCache');
        return false;
    }
    //alterar quantidadfe
    if (event.which == 114) {
        $('#PedidoQuantidade').focus().select();
        return false;
    }
    //adcionar produto
    if (event.which == 115) {
        $('#adicionar-produto').find('#salva-produto').click();
        $('#token-input-BuscaProduto').val('').focus().select();
        $('#BuscaProduto').tokenInput('clearCache');
        return false;
    }
    //pagar
    if (event.which == 117) {
        $('#botao-pagar').click();
        return false;
    }
    //aguardar
    if (event.which == 118) {
        $('#aguardar').click();
        return false;
    }
    //cancelar venda
    if (event.which == 119) {
        $('#cancelar').click();
        return false;
    }
    //indicar cliente
    if (event.which === 67 && event.shiftKey) {
        if (!$('input').is(':focus')) {
            // $('#botao-indicar-cliente').click();
            document.getElementById('formCentro:botao-indicar-cliente').click();
            $('#ClientePfCpf').focus().select();
            return false;
        }
    }
    //indicar vendedor
    if (event.which === 86 && event.shiftKey) {
        if (!$('input').is(':focus')) {
            document.getElementById('formCentro:botao-indicar-vendedor').click();
            $('#PedidoVendedorId').focus().select();
            return false;
        }
    }


});
$(document).ready(function () {
    //$('.navbar-minimalize').trigger('click');
    $('.inputs input').addClass('quantidade col-sm-12 col-md-12 col-lg-12 form-control-table');
});
