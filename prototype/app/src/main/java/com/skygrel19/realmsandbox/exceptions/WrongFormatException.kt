package com.skygrel19.realmsandbox.exceptions

class WrongFormatException(hint_message: String) : Exception(hint_message) {
    constructor() : this("Wrong format")
}