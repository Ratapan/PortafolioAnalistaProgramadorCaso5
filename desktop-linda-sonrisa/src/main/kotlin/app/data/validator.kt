package app.data

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Validación de RUT Chileno
 * algoritmo Modulo 11
 */
object validator {
    /**
     * Valida rut de la forma XXXXXXXX-X
     */
    fun validaRut(rut: String): Boolean {
        val pattern: Pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$")
        val matcher: Matcher = pattern.matcher(rut)
        if (matcher.matches() == false) return false
        val stringRut = rut.split("-".toRegex()).toTypedArray()
        return stringRut[1].toLowerCase() == dv(stringRut[0])
    }

    /**
     * Valida el dígito verificador
     */
    fun dv(rut: String): String {
        var M = 0.toLong()
        var S = 1.toLong()
        var T = rut.toLong()
        while (T != 0.toLong()) {
            S = (S + T % 10 * (9 - M++ % 6)) % 11
            T = Math.floor(10.let { T /= it; T }.toDouble()).toLong()
        }
        return if (S > 0) (S - 1).toString() else "k"
    }

    /**
     *
     * Valida el email en formato email@dominio.com
     *
     */
    fun email(email: String): Boolean {
        return Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        ).matcher(email).matches()
    }

    fun phoneNumberValidator(number: String): Boolean {
        if (number == "+") return false
        return Pattern.compile(
            "^\\+(?:[0-9]\\x20?){6,14}[0-9]\$"
        ).matcher(number.replace("\\s".toRegex(), "")).matches()
    }
}