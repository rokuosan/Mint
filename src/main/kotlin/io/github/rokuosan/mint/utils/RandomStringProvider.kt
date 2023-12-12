package io.github.rokuosan.mint.utils

object RandomStringProvider {
    fun getShortString(): String{
        val arr = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d",
            "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z", "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "-", "_")
        val sb = StringBuilder()
        for (i in 0..7) {
            val index = (arr.indices).random()
            sb.append(arr[index])
        }
        return sb.toString()
    }
}
