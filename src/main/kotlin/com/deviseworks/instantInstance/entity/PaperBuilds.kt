package com.deviseworks.instantInstance.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaperBuilds(
    @SerialName("project_id")
    val projectId: String,
    @SerialName("project_name")
    val projectName: String,
    @SerialName("version")
    val version: String,
    @SerialName("builds")
    val builds: List<Int>
)