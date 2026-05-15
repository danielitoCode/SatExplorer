package com.elitec.satexplorer.feature.visualization.data.renderEngine

import android.opengl.GLES20
import android.opengl.Matrix
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderCommand
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.domain.entity.TransformMatrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.collections.listOf
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class OpenGlRendererEngine {
    private var currentModelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val mvMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private var cameraDistance = 3.6f
    private var cameraYaw = 0f
    private var cameraPitch = 18f
    private var programId: Int = 0
    private var positionHandle: Int = -1
    private var colorHandle: Int = -1
    private var mvpHandle: Int = -1
    private var planetDetailHandle: Int = -1
    private lateinit var sphereVertices: FloatBuffer
    private var sphereVertexCount: Int = 0
    private lateinit var satelliteVertices: FloatBuffer
    private var satelliteVertexCount: Int = 0

    fun init() {
        Matrix.setIdentityM(currentModelMatrix, 0)
        updateCameraView()

        programId = createProgram(VERTEX_SHADER, FRAGMENT_SHADER)
        positionHandle = GLES20.glGetAttribLocation(programId, "aPosition")
        colorHandle = GLES20.glGetUniformLocation(programId, "uColor")
        mvpHandle = GLES20.glGetUniformLocation(programId, "uMvp")
        planetDetailHandle = GLES20.glGetUniformLocation(programId, "uPlanetDetail")

        sphereVertices = createSphereVertexBuffer(48, 48)
        sphereVertexCount = sphereVertices.limit() / COORDS_PER_VERTEX
        satelliteVertices = createCubeVertexBuffer()
        satelliteVertexCount = satelliteVertices.limit() / COORDS_PER_VERTEX

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_BACK)
    }

    fun onViewportChanged(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val aspect = width.toFloat() / height.coerceAtLeast(1)
        Matrix.perspectiveM(projectionMatrix, 0, 45f, aspect, 0.1f, 100f)
    }

    fun setCameraZoom(scaleFactor: Float) {
        cameraDistance = (cameraDistance / max(scaleFactor, 0.2f)).coerceIn(1.4f, 12f)
        updateCameraView()
    }

    fun orbitCamera(deltaX: Float, deltaY: Float) {
        cameraYaw += deltaX * 0.22f
        cameraPitch = (cameraPitch + deltaY * 0.18f).coerceIn(-80f, 80f)
        updateCameraView()
    }

    fun render(commands: List<RenderCommand>) {
        GLES20.glUseProgram(programId)
        commands.forEach { command ->
            when (command) {
                is RenderCommand.SetMatrix -> setMatrix(command.matrix)
                is RenderCommand.SetTexture -> Unit
                is RenderCommand.DrawMesh -> drawMesh(command.objectType, command.tint)
            }
        }
    }

    private fun setMatrix(matrix: TransformMatrix) {
        currentModelMatrix = matrix.values
    }

    private fun drawMesh(type: RenderObjectType, tint: FloatArray) {
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, currentModelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0)
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniform4fv(colorHandle, 1, tint, 0)
        val meshSelection = when (type) {
            RenderObjectType.GLOBE -> MeshSelection(sphereVertices, sphereVertexCount, GLES20.GL_TRIANGLES, 1f)
            RenderObjectType.SATELLITE -> MeshSelection(satelliteVertices, satelliteVertexCount, GLES20.GL_TRIANGLES, 0f)
            else -> MeshSelection(satelliteVertices, satelliteVertexCount, GLES20.GL_TRIANGLES, 0f)
        }
        GLES20.glUniform1f(planetDetailHandle, meshSelection.planetDetail)
        meshSelection.buffer.position(0)
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, STRIDE_BYTES, meshSelection.buffer)
        GLES20.glDrawArrays(meshSelection.mode, 0, meshSelection.count)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun updateCameraView() {
        val yawRad = Math.toRadians(cameraYaw.toDouble())
        val pitchRad = Math.toRadians(cameraPitch.toDouble())
        val x = (cameraDistance * cos(pitchRad) * sin(yawRad)).toFloat()
        val y = (cameraDistance * sin(pitchRad)).toFloat()
        val z = (cameraDistance * cos(pitchRad) * cos(yawRad)).toFloat()
        Matrix.setLookAtM(viewMatrix, 0, x, y, z, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    private fun createCubeVertexBuffer(): FloatBuffer {
        val vertices = floatArrayOf(
            -1f,-1f, 1f, 1f,-1f, 1f, 1f, 1f, 1f,  -1f,-1f, 1f, 1f, 1f, 1f, -1f, 1f, 1f,
            -1f,-1f,-1f, -1f, 1f,-1f, 1f, 1f,-1f,  -1f,-1f,-1f, 1f, 1f,-1f, 1f,-1f,-1f,
            -1f,-1f,-1f, -1f,-1f, 1f, -1f, 1f, 1f,  -1f,-1f,-1f, -1f, 1f, 1f, -1f, 1f,-1f,
            1f,-1f,-1f, 1f, 1f,-1f, 1f, 1f, 1f,   1f,-1f,-1f, 1f, 1f, 1f, 1f,-1f, 1f,
            -1f, 1f,-1f, -1f, 1f, 1f, 1f, 1f, 1f,  -1f, 1f,-1f, 1f, 1f, 1f, 1f, 1f,-1f,
            -1f,-1f,-1f, 1f,-1f,-1f, 1f,-1f, 1f,  -1f,-1f,-1f, 1f,-1f, 1f, -1f,-1f, 1f
        )
        return createFloatBuffer(vertices)
    }

    private fun createSphereVertexBuffer(stacks: Int, slices: Int): FloatBuffer {
        val vertices = mutableListOf<Float>()
        for (stack in 0 until stacks) {
            val phi1 = Math.PI * stack / stacks - Math.PI / 2.0
            val phi2 = Math.PI * (stack + 1) / stacks - Math.PI / 2.0
            for (slice in 0 until slices) {
                val theta1 = 2.0 * Math.PI * slice / slices
                val theta2 = 2.0 * Math.PI * (slice + 1) / slices
                val p1 = point(phi1, theta1)
                val p2 = point(phi2, theta1)
                val p3 = point(phi2, theta2)
                val p4 = point(phi1, theta2)
                vertices.addAll(listOf(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2], p3[0], p3[1], p3[2]))
                vertices.addAll(listOf(p1[0], p1[1], p1[2], p3[0], p3[1], p3[2], p4[0], p4[1], p4[2]))
            }
        }
        return createFloatBuffer(vertices.toFloatArray())
    }

    private fun point(phi: Double, theta: Double): FloatArray {
        val x = (cos(phi) * cos(theta)).toFloat()
        val y = sin(phi).toFloat()
        val z = (cos(phi) * sin(theta)).toFloat()
        return floatArrayOf(x, y, z)
    }

    private fun createFloatBuffer(data: FloatArray): FloatBuffer {
        return ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
            put(data)
            position(0)
        }
    }

    private fun createProgram(vertexSrc: String, fragmentSrc: String): Int {
        val vertex = compileShader(GLES20.GL_VERTEX_SHADER, vertexSrc)
        val fragment = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentSrc)
        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertex)
        GLES20.glAttachShader(program, fragment)
        GLES20.glLinkProgram(program)
        return program
    }

    private fun compileShader(type: Int, src: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, src)
        GLES20.glCompileShader(shader)
        return shader
    }

    private data class MeshSelection(
        val buffer: FloatBuffer,
        val count: Int,
        val mode: Int,
        val planetDetail: Float
    )

    companion object {
        private const val COORDS_PER_VERTEX = 3
        private const val STRIDE_BYTES = COORDS_PER_VERTEX * 4

        private const val VERTEX_SHADER = "attribute vec3 aPosition; uniform mat4 uMvp; varying vec3 vPos; void main(){ vPos = normalize(aPosition); gl_Position = uMvp * vec4(aPosition,1.0); }"
        private const val FRAGMENT_SHADER = "precision mediump float; uniform vec4 uColor; uniform float uPlanetDetail; varying vec3 vPos; void main(){ if (uPlanetDetail < 0.5) { gl_FragColor = uColor; return; } vec3 n = normalize(vPos); float light = clamp(dot(n, normalize(vec3(0.5,0.4,1.0))), 0.12, 1.0); float lat = asin(n.y); float lon = atan(n.z, n.x); float continents = sin(lon*3.0)*cos(lat*4.0) + sin(lon*7.0 + lat*2.0)*0.35; vec3 ocean = vec3(0.06,0.22,0.55); vec3 land = vec3(0.18,0.47,0.20); float mask = smoothstep(0.18, 0.34, continents); vec3 base = mix(ocean, land, mask); float ice = smoothstep(1.05, 1.3, abs(lat)*2.0); base = mix(base, vec3(0.88,0.92,0.96), ice*0.7); vec3 color = base * light; gl_FragColor = vec4(color, 1.0); }"
    }
}