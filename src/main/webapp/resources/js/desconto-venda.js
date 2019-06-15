function calcularValor(obj) {

    var tipoDesconto = +$('#formOutrasTelas5\\:tipo-desconto').val();
    var valorDesconto = parseFloat(obj.value);
    var total = parseFloat($('#valorTotal').val());

    var txtDesconto = $('#formOutrasTelas5\\:total-desconto');

    if (tipoDesconto && tipoDesconto === 1) {
        txtDesconto.val((total - valorDesconto).toFixed(2));
    } else {
        var result = (valorDesconto * total) / 100
        txtDesconto.val(result.toFixed(2))
    }
}



