package com.cervalid.platform.common.enums;
//tipos de tokens de autenticación
public enum TokenType {
    ACCESS, //Vida corta (minutos)
    REFRESH //Vida larga (días), para pedir un nuevo access token
}
