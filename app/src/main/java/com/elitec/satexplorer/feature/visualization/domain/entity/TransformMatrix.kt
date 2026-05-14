package com.elitec.satexplorer.feature.visualization.domain.entity

data class TransformMatrix(
    val values: FloatArray // 4x4 matrix
) {
    override fun equals(
        other: Any?
    ): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass)
            return false
        other as TransformMatrix
        return values.contentEquals(
            other.values
        )
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }
}