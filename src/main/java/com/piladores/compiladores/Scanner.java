package com.piladores.compiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source + " ";
    }

    static boolean esRango(char c, String patt) {
        int estado = 0;
        char rangoInicial = '\0';
        for (char ca : patt.toCharArray()) {
            switch (estado) {
                case 0:
                    switch (ca) {
                        case '\\':
                            estado = 3;
                            break;
                        case '>':
                            estado = 1;
                            break;
                        default:
                            if (c == ca) {
                                return true;
                            }
                            break;
                    }
                    break;
                case 1:
                    rangoInicial = ca;
                    estado = 2;
                    break;
                case 2:
                    for (; rangoInicial <= ca; rangoInicial++) {
                        if (c == rangoInicial) {
                            return true;
                        }
                    }
                    estado = 0;
                    break;
                case 3:
                    if (c == ca) {
                        return true;
                    }
                    estado = 0;
                    break;
            }
        }
        return false;
    }

    public List<Token> scan() throws Exception {
        int estado = 0;
        int linea = 0;
        String lexema = "";
        char c;

        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);
            int estadonuevo = -1;
            switch (estado) {
                case 0:
                    if (esRango(c, "\\>")) {
                        lexema += c;
                        estadonuevo = 1;
                        break;
                    }
                    if(esRango(c, "<")) {
                        lexema += c;
                        estadonuevo = 4;
                        break;
                    }
                    if(esRango(c, "=")) {
                        lexema += c;
                        estadonuevo = 7;
                        break;
                    }
                    if(esRango(c, "!")) {
                        lexema += c;
                        estadonuevo = 10;
                        break;
                    }
                    if(esRango(c, ">az>AZ")) {
                        lexema += c;
                        estadonuevo = 13;
                        break;
                    }
                    if(esRango(c, ">09")){
                        lexema += c;
                        estadonuevo = 15;
                        break;
                    }
                    if(esRango(c, "\"")){
                        lexema += c;
                        estadonuevo = 24;
                        break;
                    }
                    if(esRango(c, ";")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.SEMICOLON, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, ",")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.COMMA, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, "(")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.LEFT_PAREN, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, ")")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.RIGHT_PAREN, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    
                    if(esRango(c, "{")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.LEFT_BRACE, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, "}")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.RIGHT_BRACE, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, "+")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.PLUS, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, "-")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.MINUS, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    if(esRango(c, "*")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.STAR, lexema));
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    
                    if(esRango(c, "/")){
                        estadonuevo = 26;
                        break;
                    }
                    //Desechar
                    if(esRango(c, "\n\r ")){
                        linea++;
                        lexema = "";
                        estadonuevo = 0;
                        break;
                    }
                    break;
                case 1:
                    estadonuevo = 0;
                    if(esRango(c, "=")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.GREATER_EQUAL, lexema));
                        lexema = "";
                        break;
                    }
                    tokens.add(new Token(TipoToken.GREATER, lexema));
                    i--;
                    lexema = "";
                    break;
                    
                case 4:
                    estadonuevo = 0;
                    if(esRango(c, "=")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.LESS_EQUAL, lexema));
                        lexema = "";
                        break;
                    }
                    tokens.add(new Token(TipoToken.LESS, lexema));
                    i--;
                    lexema = "";
                break;
                
                case 7:
                    estadonuevo = 0;
                    if(esRango(c, "=")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.EQUAL_EQUAL, lexema));
                        lexema = "";
                        break;
                    }
                    tokens.add(new Token(TipoToken.EQUAL, lexema));
                    i--;
                    lexema = "";
                break;
                
                case 10:
                    estadonuevo = 0;
                    if(esRango(c, "=")){
                        lexema += c;
                        tokens.add(new Token(TipoToken.BANG_EQUAL, lexema));
                        lexema = "";
                        break;
                    }
                    tokens.add(new Token(TipoToken.BANG, lexema));
                    i--;
                    lexema = "";
                break;
                    
                case 13:
                    if (esRango(c, ">az>AZ>09")) {
                        estadonuevo = 13;
                        lexema += c;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if (tt == null) {
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        } else {
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estadonuevo = 0;
                        lexema = "";
                        i--;

                    }
                    break;

                case 15:
                    if (esRango(c, ">09")) {
                        estadonuevo = 15;
                        lexema += c;
                        break;
                    }
                    if (esRango(c, ".")) {
                        estadonuevo = 16;
                        lexema += c;
                        break;
                    }
                    if (esRango(c, "Ee")) {
                        estadonuevo = 18;
                        lexema += c;
                        break;
                    } 
                    tokens.add(new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema)));
                    estadonuevo = 0;
                    lexema = "";
                    i--;
                    break;
                case 16:
                    if(esRango(c, ">09")) {
                        estadonuevo = 17;
                        lexema += c;
                        break;
                    }
                    break;
                case 17:
                    if(esRango(c, ">09")){
                        estadonuevo = 17;
                        lexema += c;
                        break;
                    }
                    if(esRango(c, "Ee")){
                        estadonuevo = 18;
                        lexema += c;
                        break;
                    }
                    tokens.add(new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema)));
                    estadonuevo = 0;
                    lexema = "";
                    i--;
                    break;
                case 18:
                    if(esRango(c, "+-")){
                        estadonuevo = 19;
                        lexema += c;
                        break;
                    }
                    if(esRango(c, ">09")){
                        estadonuevo = 20;
                        lexema += c;
                        break;
                    }
                    break;
                case 19:
                    if(esRango(c, ">09")){
                        estadonuevo = 20;
                        lexema += c;
                        break;
                    }
                    break;
                case 20:
                    if(esRango(c, ">09")){
                        estadonuevo = 20;
                        lexema += c;
                        break;
                    }
                    break;
                case 24:
                    if(esRango(c, "\"")){
                        estadonuevo = 0;
                        lexema += c;
                        tokens.add(new Token(TipoToken.STRING, 
                                lexema,
                                lexema.substring(1, lexema.length() - 1)));
                        lexema = "";
                        break;
                        
                    }
                    if(esRango(c, "\n\r")){
                        linea++;
                        estadonuevo = 0;
                        Compiladores.error(linea, "Salto de línea inesperado dentro de String");
                    }
                    
                    estadonuevo = 24;
                    lexema += c;
                    break;
                case 26:
                    if(esRango(c, "*")){
                        estadonuevo = 27;
                        lexema += c;
                        break;
                    }
                    
                    if(esRango(c, "/")){
                        estadonuevo = 30;
                        lexema += c;
                        break;
                    }
                    
                    estadonuevo = 0;
                    tokens.add(new Token(TipoToken.SLASH, lexema));
                    lexema = "";
                    i--;
                    break;
                    
                case 27:
                    if(esRango(c, "*")){
                        estadonuevo = 28;
                        lexema += c;
                        break;
                    }
                    estadonuevo = 27;
                    lexema += c;
                    break;
                case 28:
                    if(esRango(c, "*")){
                        estadonuevo = 28;
                        lexema += c;
                        break;
                    }
                    
                    if(esRango(c, "/")){
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    estadonuevo = 27;
                    lexema += c;
                    break;
                case 30:
                    if(esRango(c, "\n\r")){
                        estadonuevo = 0;
                        lexema = "";
                        break;
                    }
                    estadonuevo = 30;
                    lexema += c;
                    break;
                
            }
            
            if(estadonuevo == -1){
                Compiladores.error(linea, "No hay transiciones para el caractér en " + linea);
                estadonuevo = 0;
            }
            
            estado = estadonuevo;

        }

        return tokens;
    }
}
