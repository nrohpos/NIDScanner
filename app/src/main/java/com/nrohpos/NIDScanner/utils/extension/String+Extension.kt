package com.nrohpos.NIDScanner.utils.extension

import android.text.Editable

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)