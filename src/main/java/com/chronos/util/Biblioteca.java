/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import com.chronos.dto.MapDTO;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author john
 */
public class Biblioteca {


    public static void generateCombination(List<List<String>> Lists, List<String> result, int depth, String current) {
        if (depth == Lists.size()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < Lists.get(depth).size(); ++i) {
            generateCombination(Lists, result, depth + 1, current + Lists.get(depth).get(i));
        }
    }


    public static boolean renavamValido(String renavam) {
        //Completa com zeros a esquerda caso o renavam seja com 9 digitos
        if (renavam.matches("^([0-9]{9})$")) {
            renavam = "00" + renavam;
        }

        // Valida se possui 11 digitos pos formatacao
        if (!renavam.matches("[0-9]{11}")) {
            return false;
        }

        // Remove o digito (11 posicao)
        // renavamSemDigito = 0063988496
        String renavamSemDigito = renavam.substring(0, 10);

        // Inverte os caracteres (reverso)
        // renavamReversoSemDigito = 69488936
        String renavamReversoSemDigito = new StringBuffer(renavamSemDigito).reverse().toString();

        int soma = 0;

        // Multiplica as strings reversas do renavam pelos numeros multiplicadores
        // para apenas os primeiros 8 digitos de um total de 10
        // Exemplo: renavam reverso sem digito = 69488936
        // 6, 9, 4, 8, 8, 9, 3, 6
        // * * * * * * * *
        // 2, 3, 4, 5, 6, 7, 8, 9 (numeros multiplicadores - sempre os mesmos [fixo])
        // 12 + 27 + 16 + 40 + 48 + 63 + 24 + 54
        // soma = 284
        for (int i = 0; i < 8; i++) {
            Integer algarismo = Integer.parseInt(renavamReversoSemDigito.substring(i, i + 1));
            Integer multiplicador = i + 2;
            soma += algarismo * multiplicador;
        }

        // Multiplica os dois ultimos digitos e soma
        soma += Character.getNumericValue(renavamReversoSemDigito.charAt(8)) * 2;
        soma += Character.getNumericValue(renavamReversoSemDigito.charAt(9)) * 3;

        // mod11 = 284 % 11 = 9 (resto da divisao por 11)
        int mod11 = soma % 11;

        // Faz-se a conta 11 (valor fixo) - mod11 = 11 - 9 = 2
        int ultimoDigitoCalculado = 11 - mod11;

        // ultimoDigito = Caso o valor calculado anteriormente seja 10 ou 11, transformo ele em 0
        // caso contrario, eh o proprio numero
        ultimoDigitoCalculado = (ultimoDigitoCalculado >= 10 ? 0 : ultimoDigitoCalculado);

        // Pego o ultimo digito do renavam original (para confrontar com o calculado)
        int digitoRealInformado = Integer.valueOf(renavam.substring(renavam.length() - 1));

        // Comparo os digitos calculado e informado
        if (ultimoDigitoCalculado == digitoRealInformado) {
            return true;
        }

        return true;
    }

    public static boolean isChaveAcesso(String chave, String modelo) {

        if (StringUtils.isEmpty(chave)) {
            return false;
        }

        if (chave.length() != 44) {
            return false;
        }

        int uf = Integer.valueOf(chave.substring(0, 2));
        if (!((uf >= 11 && uf <= 17) || (uf >= 21 && uf <= 35) || (uf >= 41 && uf <= 43) || (uf >= 50 && uf <= 53))) {
            return false;
        }

        int mes = Integer.valueOf(chave.substring(4, 6));
        if (mes < 1 || mes > 12) {
            return false;
        }
        int ano = Integer.valueOf(chave.substring(2, 4));
        String cnpj = chave.substring(6, 20);
        if (!cnpjValido(cnpj)) {
            return false;
        }
        int mod = Integer.valueOf(chave.substring(20, 22));
        ModeloDocumento modeloDoc = ModeloDocumento.getByCodigo(Integer.valueOf(modelo));
        if (!modelo.equals(modeloDoc.getCodigo().toString())) {
            return false;
        }
        String chaveSemDigito = chave.substring(0, 43);
        String digito = modulo11(chaveSemDigito).toString();
        chaveSemDigito += digito;
        return chave.equals(chaveSemDigito);
    }

    public static Map getMap(List<MapDTO> list) {
        Map<String, BigDecimal> mapa = new LinkedHashMap<>();
        BigDecimal valor = BigDecimal.ZERO;
        String descricao = "";
        String descricaoAux = "";
        int i = 0;
        for (MapDTO item : list) {
            i++;
            descricao = item.getDescricao();

            if (descricaoAux.equals("")) {
                descricaoAux = descricao;
            }
            if (descricao.equals(descricaoAux)) {
                valor = valor.add(item.getValor());
                if (i == list.size()) {
                    mapa.put(descricao, valor);
                }
            } else {
                mapa.put(descricaoAux, valor);
                valor = item.getValor();
                descricaoAux = descricao;
                if (i == list.size()) {
                    mapa.put(descricao, valor);
                }
            }

        }
        return mapa;
    }

    public static String repete(String string, int quantidade) {
        return String.join("", Collections.nCopies(quantidade, string));
    }

    /**
     * Busca os bytes de um determinado arquivo
     *
     * @param file Arquivo
     * @return byte[]
     */
    public static byte[] getBytesFromFile(File file) throws Exception {
        // Create the byte array to hold the data
        byte[] documento;
        InputStream is = new FileInputStream(file);
        try {
            // Get the size of the file
            long length = file.length();
            documento = new byte[(int) length];
            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < documento.length && (numRead = is.read(documento, offset, documento.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < documento.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            // Close the input stream and return bytes
        } finally {
            is.close();
        }
        return documento;
    }

    /**
     * Formata o  CPF ou CNPJ dependendo do  tamanho
     *
     * @param cpfCnpj
     * @return
     */
    public static String cpfCnpjFormatado(String cpfCnpj) {
        if (cpfCnpj != null) {
            cpfCnpj = cpfCnpj.replaceAll("\\D", "");
            if (cpfCnpj.length() == 11) {
                String strFormatado = cpfCnpj.substring(0, 3) + "."
                        + cpfCnpj.substring(3, 6) + "."
                        + cpfCnpj.substring(6, 9) + "-"
                        + cpfCnpj.substring(9, 11);

                return strFormatado;
            }
            if (cpfCnpj.length() == 14) {
                String strFormatado = cpfCnpj.substring(0, 2) + "."
                        + cpfCnpj.substring(2, 5) + "."
                        + cpfCnpj.substring(5, 8) + "/"
                        + cpfCnpj.substring(8, 12) + "-"
                        + cpfCnpj.substring(12, 14);

                return strFormatado;
            }
        }
        return cpfCnpj;
    }

    /**
     * Verifica se o cnpj informado e valido
     *
     * @param cnpj CNPJ a validar
     * @return boolean
     */
    public static boolean cnpjValido(String cnpj) {
        try {
            if (cnpj == null) {
                throw new Exception("Valor nulo");
            }
            if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222") || cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555") || cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888") || cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
                return (false);
            }

            char dig13, dig14;
            int sm, i, r, num, peso;
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-esimo caractere do CNPJ em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = cnpj.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = cnpj.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos
            // informados.
            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
        } catch (Exception e) {
            return (false);
        }
    }

    /**
     * Verifica se o cpf informado e valido
     *
     * @param cpf CPF a validar
     * @return boolean
     */
    public static boolean cpfValido(String cpf) {
        try {
            if (cpf == null) {
                throw new Exception("Valor nulo");
            }
            if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") || cpf.equals("99999999999") || (cpf.length() != 11)) {
                return (false);
            }

            char dig10, dig11;
            int sm, i, r, num, peso;

            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = cpf.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere
                // numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = cpf.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos
            // informados.
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    public static Object nullToEmpty(Object objeto, boolean relacionamentos) {
        Object atributo;
        try {
            Field fields[] = objeto.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (!(f.getName().equals("serialVersionUID") || f.getName().equals("bag"))) {
                    if (f.getType() == String.class || f.getType() == Integer.class || f.getType() == BigDecimal.class || f.getType() == Double.class || f.getType() == Date.class || f.getType() == Long.class) {

                        Method metodo = objeto.getClass().getDeclaredMethod("get" + primeiraMaiuscula(f.getName()));
                        atributo = metodo.invoke(objeto);
                        if (atributo == null) {
                            if (f.getType() == String.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), String.class);
                                metodo.invoke(objeto, "");
                            }
                            if (f.getType() == Integer.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Integer.class);
                                metodo.invoke(objeto, 0);
                            }
                            if (f.getType() == BigDecimal.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), BigDecimal.class);
                                metodo.invoke(objeto, BigDecimal.ZERO);
                            }
                            if (f.getType() == Double.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Double.class);
                                metodo.invoke(objeto, 0.0);
                            }
                            if (f.getType() == Date.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Date.class);
                                metodo.invoke(objeto, new Date(0l));
                            }
                            if (f.getType() == Long.class) {
                                metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), Long.class);
                                metodo.invoke(objeto, 0l);
                            }
                        }
                    } else if (relacionamentos) {
                        if (!objeto.getClass().getName().equals(f.getType().getName())) {
                            Method metodo = objeto.getClass().getDeclaredMethod("get" + primeiraMaiuscula(f.getName()));
                            atributo = metodo.invoke(objeto);
                            if (atributo != null) {
                                if (atributo.getClass() != ArrayList.class) {
                                    nullToEmpty(atributo, relacionamentos);
                                }
                            } else {
                                if (f.getType() != ArrayList.class || !(f.getType().isArray())) {
                                    metodo = objeto.getClass().getDeclaredMethod("set" + primeiraMaiuscula(f.getName()), f.getType());
                                    metodo.invoke(objeto, Class.forName(f.getType().getName()).newInstance());

                                    metodo = objeto.getClass().getDeclaredMethod("get" + primeiraMaiuscula(f.getName()));
                                    atributo = metodo.invoke(objeto);

                                    nullToEmpty(atributo, relacionamentos);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        return objeto;
    }

    public static String primeiraMaiuscula(String texto) {
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    public static BigDecimal soma(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.add(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static BigDecimal subtrai(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.subtract(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static BigDecimal multiplica(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.multiply(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static BigDecimal divide(BigDecimal valor1, BigDecimal valor2) {
        BigDecimal resultado = valor1.divide(valor2, MathContext.DECIMAL64);
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);
        return resultado;
    }

    public static Date addDay(Date dt, int numberDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, numberDay);
        return c.getTime();
    }

    /**
     * Retorna o periodo imediatamente anterior ao informado(mm/aaaa).
     *
     * @param periodo periodo
     * @return String
     */
    public static String periodoAnterior(String periodo) {
        String periodoAnterior = "";
        int mesPeriodo = Integer.valueOf(periodo.substring(0, 2));
        int anoPeriodo = Integer.valueOf(periodo.substring(3, 7));
        mesPeriodo--;
        if (mesPeriodo == 0) {
            mesPeriodo = 1;
            anoPeriodo--;
        }
        if (mesPeriodo < 10) {
            periodoAnterior = "0" + String.valueOf(mesPeriodo) + "/" + String.valueOf(mesPeriodo);
        } else {
            periodoAnterior = String.valueOf(mesPeriodo) + "/" + String.valueOf(anoPeriodo);
        }
        return periodoAnterior;
    }

    public static Date getDataInicial(Date periodo) {
        try {
            if (periodo == null) {
                return null;
            }
            Calendar dataValida = Calendar.getInstance();
            dataValida.setTime(periodo);
            dataValida.setLenient(false);

            dataValida.set(Calendar.DAY_OF_MONTH, 1);

            dataValida.getTime();

            return dataValida.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date ultimoDiaMes(Date periodo) {
        Calendar dataF = Calendar.getInstance();
        dataF.setTime(periodo);
        dataF.setLenient(false);
        dataF.set(Calendar.DAY_OF_MONTH, dataF.getActualMaximum(Calendar.DAY_OF_MONTH));

        return dataF.getTime();
    }

    public static Date dataPagamento(int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1); //Soma 1 dia pra cair na segunda feira

        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2); //Soma 2 dias pra cair na segunda feira

        }
        return calendar.getTime();
    }

    /**
     * Retorna o mes e ano no formato MM/AAAA
     *
     * @param dataA data
     * @return String
     */
    public static String mesAno(Date dataA) {
        return new SimpleDateFormat("MM/yyyy").format(dataA.getTime());
    }

    public static boolean verificaDataMaior(Date dataInicial, Integer qtdeDias) {
        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(dataInicial);

        Calendar dataCalculo = Calendar.getInstance();
        dataCalculo.add(Calendar.DAY_OF_MONTH, qtdeDias);

        return dataFinal.after(dataCalculo);

    }

    public static BigDecimal calcularValorPercentual(BigDecimal total, BigDecimal taxa) {
        BigDecimal valor = multiplica(total, taxa);
        valor = divide(valor, BigDecimal.valueOf(100));
        return valor;
    }

    public static Integer modulo11(String codigo) {
        int total = 0;
        int peso = 2;

        for (int i = 0; i < codigo.length(); i++) {
            total += (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }
        int resto = total % 11;
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);
    }

    public static boolean validarEmail(String email) {
        final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String removerAcentos(String str) {
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\t", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll("&", "E");
        str = str.replaceAll(">\\s+<", "><");
        CharSequence cs = new StringBuilder(str == null ? "" : str);
        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public String retiraAcentos(String string) {
        String aux = new String(string);
        aux = aux.replaceAll("[èëÈéêÉÊË]", "e");
        aux = aux.replaceAll("[ûùüúÛÚÙÜ]", "u");
        aux = aux.replaceAll("[ïîíìÏÎÍÌ]", "i");
        aux = aux.replaceAll("[àâáäãÁÀÂÄ]", "a");
        aux = aux.replaceAll("[óòôöõÓÒÔÖ]", "o");
        aux = aux.replaceAll("[çÇ]", "c");
        return aux;
    }


}
