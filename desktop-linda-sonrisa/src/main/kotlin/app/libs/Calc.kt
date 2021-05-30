package app.libs

import kotlin.math.abs

fun closestNumber(n: Int?, m: Int): Int {
    // find the quotient
    val q = n?.div(m)

    // 1st possible closest number
    val n1 = m * q!!

    // 2nd possible closest number
    val n2 = if (n * m > 0) m * (q + 1) else m * (q - 1)

    // if true, then n1 is the required closest number
    return if (abs(n - n1) < abs(n - n2)) n1 else n2

    // else n2 is the required closest number
}

fun maxPercentageOf(n: Int, m: Int): Int {
    var percentage = (n.toDouble() / m) * 100
    return percentage.toInt()
}