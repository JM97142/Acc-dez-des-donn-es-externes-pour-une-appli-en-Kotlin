package com.aura.data.model.transfer

data class TransferModelRequest (
    val sender: String,
    val recipient: String,
    val amount: Double
)