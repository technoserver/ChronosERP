/**
 * Created by john on 25/12/17.
 */

function printElement(elemento) {

    var mywindow = window.open('', 'PRINT');

    mywindow.document.write('<html><head> <title>' + document.title + '</title>');
    mywindow.document.write('<style type="text/css">');
    mywindow.document.write('p{margin:0;}p + p{margin-top:5px;}');
    mywindow.document.write('</style>');
    mywindow.document.write('</head><body >');
    mywindow.document.write('<div class="container"> ');
    mywindow.document.write(document.getElementById(elemento).innerHTML);
    mywindow.document.write('</div> ');
    mywindow.document.write('</body></html>');

    mywindow.document.close(); // necessary for IE >= 10
    mywindow.focus(); // necessary for IE >= 10*/

    mywindow.print();
    mywindow.close();

    return true;

    // $(elemento).printElement();
}
