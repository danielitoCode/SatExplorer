package com.elitec.satexplorer.feature.visualization.domain.entity

sealed class RenderCommand {
    data class DrawMesh(
        val objectId: Long,
        val objectType: RenderObjectType,
        val tint: FloatArray
    ) : RenderCommand() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DrawMesh

            if (objectId != other.objectId) return false
            if (objectType != other.objectType) return false
            if (!tint.contentEquals(other.tint)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = objectId.hashCode()
            result = 31 * result + objectType.hashCode()
            result = 31 * result + tint.contentHashCode()
            return result
        }
    }

    data class SetMatrix(val matrix: TransformMatrix) : RenderCommand()
    data class SetTexture(val textureId: String) : RenderCommand()
}