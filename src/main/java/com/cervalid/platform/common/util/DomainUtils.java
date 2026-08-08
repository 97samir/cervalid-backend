package com.cervalid.platform.common.util;

public class DomainUtils {

    // extrae dominio de email
    public static String extractDomainFromEmail(String email) {
        if (email == null || email.isBlank()) return null;

        int atIndex = email.indexOf("@");
        if (atIndex == -1) return null;

        return email.substring(atIndex + 1).toLowerCase();
    }

    // extrae dominio de sitio web
    public static String extractDomainFromUrl(String url) {
        if (url == null || url.isBlank()) return null;

        url = url.toLowerCase().trim();

        // quitar protocolo
        url = url.replace("https://", "")
                .replace("http://", "");

        // quitar www
        if (url.startsWith("www.")) {
            url = url.substring(4);
        }

        // quitar path (/algo)
        int slashIndex = url.indexOf("/");
        if (slashIndex != -1) {
            url = url.substring(0, slashIndex);
        }

        return url;
    }

}
