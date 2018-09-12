var NFe = (function () {

    var url = "127.0.0.1:9002";


    var enviar = function enviar(xml) {

        return xml;
    };

    var cancelar = function cancelar() {


    };

    var inutilizar = function inutilizar() {


    };

    var imprimir = function imprimir(xml) {
        printNFCe(xml);
    };


    return {
        inutilizar: inutilizar,
        cancelar: cancelar,
        imprimir: imprimir,
        enviar: enviar
    };


    function printNFCe(xml) {
        $.ajax({
            url: url,
            method: 'GET',
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({xml: xml}),
            success: onPrintSucesso.bind(this),
            error: onErrorPrint.bind(this)
        });
    }


    function onPrintSucesso(e) {

    }

    function onErrorPrint(e) {
        console.log('ahahahah', e.responseText);

    }

}());