package com.nrohpos.NIDScanner.model

import java.io.Serializable

data class CitizenModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var issuingState: String? = null,
    var nationality: String? = null,
    var gender: String? = null,
    var dob: String? = null,
    var travelDocType: String? = null,
    var cardExp: String? = null,
    var personalNumber: String? = null,
    var idNumber: String? = null
) : Serializable {
    val fullName: String
        get() = "$firstName $lastName"
}
