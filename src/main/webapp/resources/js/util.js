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


function imprimirNFCe() {
    var xml = $('#xml').val();
    var obj = {xml: xml};
    $.ajax({
        url: "http://localhost:9002/monitor/PrintNFCe",
        contentType: "application/json",
        dataType: "json",
        method: "POST",
        data: JSON.stringify(obj),
        success: function (result) {
            testResultPrint([{name: 'sucesso', value: result.Sucesso}, {name: "mensagem", value: result.Mensagem}]);
        }
    });
}

function enviarNFCe(xml) {
    var obj = {xml: xml};
    $.ajax({
        url: "http://localhost:9002/monitor/Enviar",
        contentType: "application/json",
        dataType: "json",
        method: "POST",
        data: JSON.stringify(obj),
        success: function (result) {
            testResultPrint([{name: 'sucesso', value: result.Sucesso}, {name: "mensagem", value: result.Mensagem}]);
        }
    });
}

