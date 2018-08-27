$(document).ready(function () {


});

function consultarCEP() {
    var cep_code = $('#cep').val();
    if (cep_code.length <= 0)
        return;

    $.ajax({
        url: "http://apps.widenet.com.br/busca-cep/api/cep.json",
        data: {code: cep_code},
        success: function (result) {

            setarValores(result)

        }
    });

}

function setarValores(result) {

    if (result.status != 1) {
        alert(result.message || "Houve um erro desconhecido");

    }
    var cep = result.code;
    var estado = result.state;
    var cidade = result.city;
    var bairro = result.district;
    var endereco = result.address;

    setarValoresCEP([{
        name: 'cep',
        value: cep
    }, {
        name: 'estado',
        value: estado
    }, {
        name: 'cidade',
        value: cidade
    }, {
        name: 'bairro',
        value: bairro
    }, {
        name: 'endereco',
        value: endereco
    }]);
}

