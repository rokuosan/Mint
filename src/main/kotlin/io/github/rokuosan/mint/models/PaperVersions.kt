package io.github.rokuosan.mint.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PaperVersions(
    @SerialName("project_id")
    val projectId: String,
    @SerialName("project_name")
    val projectName: String,
    @SerialName("version_groups")
    val versionGroups: List<String>,
    @SerialName("versions")
    val versions: List<String>
)