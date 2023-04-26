package com.anaandreis.minhapesquisa_trellocloneapp.models

data class User (
    val Id: String,
    val name: String,
    val email: String,
    val projects: MutableList<String?>? = null
        )