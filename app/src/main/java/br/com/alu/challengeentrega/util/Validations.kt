package br.com.alu.challengeentrega.util

class Validations{
    companion object{
        fun validadeEmail(email: String): Boolean{
            return Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[cC][oO][mM]\$").matches(email)
        }

        fun validateTwoStrings(s1: String, s2: String): Boolean{
            return s1 == s2
        }
    }
}


