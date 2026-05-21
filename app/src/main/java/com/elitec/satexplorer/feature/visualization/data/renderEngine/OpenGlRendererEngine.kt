package com.elitec.satexplorer.feature.visualization.data.renderEngine

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderCommand
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.domain.entity.TransformMatrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class OpenGlRendererEngine(
    private val context: Context
) {
    private var currentModelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val mvMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)

    private var cameraDistance = 3.2f
    private var cameraYaw = 0f
    private var cameraPitch = 18f
    private var cameraPanX = 0f
    private var cameraPanY = 0f

    private var programId: Int = 0
    private var positionHandle: Int = -1
    private var texCoordHandle: Int = -1
    private var mvpHandle: Int = -1
    private var planetDetailHandle: Int = -1
    private var sunDirectionHandle: Int = -1
    private var earthDayTexHandle: Int = -1
    private var earthNightTexHandle: Int = -1
    private var objectColorHandle: Int = -1
    private val globeAlignedModelMatrix = FloatArray(16)

    private lateinit var sphereInterleaved: FloatBuffer
    private var sphereVertexCount: Int = 0
    private lateinit var satelliteVertices: FloatBuffer
    private var satelliteVertexCount: Int = 0
    private lateinit var orbitPathVertices: FloatBuffer
    private var orbitPathVertexCount: Int = 0
    private var earthDayTextureId: Int = 0
    private var earthNightTextureId: Int = 0

    fun init() {
        Matrix.setIdentityM(currentModelMatrix, 0)
        updateCameraView()

        programId = createProgram(VERTEX_SHADER, FRAGMENT_SHADER)
        positionHandle = GLES20.glGetAttribLocation(programId, "aPosition")
        texCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord")
        mvpHandle = GLES20.glGetUniformLocation(programId, "uMvp")
        planetDetailHandle = GLES20.glGetUniformLocation(programId, "uPlanetDetail")
        sunDirectionHandle = GLES20.glGetUniformLocation(programId, "uSunDirection")
        earthDayTexHandle = GLES20.glGetUniformLocation(programId, "uEarthDayTex")
        earthNightTexHandle = GLES20.glGetUniformLocation(programId, "uEarthNightTex")
        objectColorHandle = GLES20.glGetUniformLocation(programId, "uObjectColor")

        sphereInterleaved = createSphereInterleavedBuffer(stacks = 96, slices = 128)
        sphereVertexCount = sphereInterleaved.limit() / SPHERE_STRIDE_FLOATS
        satelliteVertices = createSatelliteModelVertexBuffer()
        satelliteVertexCount = satelliteVertices.limit() / COORDS_PER_VERTEX
        orbitPathVertices = createOrbitPathVertexBuffer(segments = 240)
        orbitPathVertexCount = orbitPathVertices.limit() / COORDS_PER_VERTEX
        earthDayTextureId = loadEarthTexture("earth_day_8k", "earth_daymap")
        earthNightTextureId = loadEarthTexture("earth_night_8k", "earth_nightmap")

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_BACK)
    }

    fun onViewportChanged(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val aspect = width.toFloat() / height.coerceAtLeast(1)
        Matrix.perspectiveM(projectionMatrix, 0, 42f, aspect, 0.1f, 100f)
    }

    fun setCameraDistance(distance: Float) {
        cameraDistance = distance.coerceIn(1.25f, 10f)
        updateCameraView()
    }

    fun setCameraOrbit(yaw: Float, pitch: Float) {
        cameraYaw = yaw
        cameraPitch = pitch.coerceIn(-85f, 85f)
        updateCameraView()
    }

    fun setCameraPan(panX: Float, panY: Float) {
        cameraPanX = panX
        cameraPanY = panY
        updateCameraView()
    }
    fun setCameraZoom(scaleFactor: Float) {
        cameraDistance = (cameraDistance / max(scaleFactor, 0.2f)).coerceIn(1.25f, 10f)
        updateCameraView()
    }

    fun orbitCamera(deltaX: Float, deltaY: Float) {
        cameraYaw += deltaX * 0.18f
        cameraPitch = (cameraPitch + deltaY * 0.14f).coerceIn(-85f, 85f)
        updateCameraView()
    }

    fun render(commands: List<RenderCommand>) {
        GLES20.glUseProgram(programId)
        val sunDirection = calculateSunDirection()
        GLES20.glUniform3f(sunDirectionHandle, sunDirection[0], sunDirection[1], sunDirection[2])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, earthDayTextureId)
        GLES20.glUniform1i(earthDayTexHandle, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, earthNightTextureId)
        GLES20.glUniform1i(earthNightTexHandle, 1)
        commands.forEach { command ->
            when (command) {
                is RenderCommand.SetMatrix -> setMatrix(command.matrix)
                is RenderCommand.SetTexture -> Unit
                is RenderCommand.DrawMesh -> drawMesh(command.objectType, command.tint)
            }
        }
    }

    private fun setMatrix(matrix: TransformMatrix) { currentModelMatrix = matrix.values }

    private fun drawMesh(type: RenderObjectType, tint: FloatArray) {
        val modelMatrix = if (type == RenderObjectType.GLOBE) {
            Matrix.rotateM(globeAlignedModelMatrix, 0, currentModelMatrix, 0, INITIAL_GLOBE_YAW_DEGREES, 0f, 1f, 0f)
            globeAlignedModelMatrix
        } else {
            currentModelMatrix
        }

        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0)
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniform4f(objectColorHandle, tint[0], tint[1], tint[2], tint[3])

        when (type) {
            RenderObjectType.GLOBE -> {
                GLES20.glUniform1f(planetDetailHandle, 1f)
                sphereInterleaved.position(0)
                GLES20.glEnableVertexAttribArray(positionHandle)
                GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, SPHERE_STRIDE_BYTES, sphereInterleaved)
                sphereInterleaved.position(3)
                GLES20.glEnableVertexAttribArray(texCoordHandle)
                GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, SPHERE_STRIDE_BYTES, sphereInterleaved)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, sphereVertexCount)
            }
            RenderObjectType.ORBIT_PATH -> {
                GLES20.glUniform1f(planetDetailHandle, 0f)
                GLES20.glDisable(GLES20.GL_DEPTH_TEST)
                orbitPathVertices.position(0)
                GLES20.glEnableVertexAttribArray(positionHandle)
                GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, STRIDE_BYTES, orbitPathVertices)
                GLES20.glDisableVertexAttribArray(texCoordHandle)
                GLES20.glVertexAttrib2f(texCoordHandle, 0f, 0f)
                GLES20.glLineWidth(4f)
                GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, orbitPathVertexCount)
                GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            }
            else -> {
                GLES20.glUniform1f(planetDetailHandle, 0f)
                satelliteVertices.position(0)
                GLES20.glEnableVertexAttribArray(positionHandle)
                GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, STRIDE_BYTES, satelliteVertices)
                GLES20.glDisableVertexAttribArray(texCoordHandle)
                GLES20.glVertexAttrib2f(texCoordHandle, 0f, 0f)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, satelliteVertexCount)
            }
        }
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    private fun updateCameraView() {
        val yawRad = Math.toRadians(cameraYaw.toDouble())
        val pitchRad = Math.toRadians(cameraPitch.toDouble())
        val x = (cameraDistance * cos(pitchRad) * sin(yawRad)).toFloat()
        val y = (cameraDistance * sin(pitchRad)).toFloat()
        val z = (cameraDistance * cos(pitchRad) * cos(yawRad)).toFloat()
        Matrix.setLookAtM(viewMatrix, 0, x + cameraPanX, y + cameraPanY, z, cameraPanX, cameraPanY, 0f, 0f, 1f, 0f)
    }

    private fun calculateSunDirection(): FloatArray {
        val millisInDay = 86_400_000L
        val utcMillis = System.currentTimeMillis() % millisInDay
        val angle = ((utcMillis.toFloat() / millisInDay.toFloat()) * Math.PI * 2.0).toFloat()
        val x = cos(angle)
        val y = 0.18f
        val z = sin(angle)
        val len = kotlin.math.sqrt(x * x + y * y + z * z).coerceAtLeast(0.0001f)
        return floatArrayOf(x / len, y / len, z / len)
    }

    private fun loadEarthTexture(vararg resourceNames: String): Int {
        val selectedName = resourceNames.firstOrNull {
            context.resources.getIdentifier(it, "drawable", context.packageName) != 0
        } ?: error("Missing drawable resource. Tried: ${resourceNames.joinToString()} (equirectangular Earth map).")

        val resId = context.resources.getIdentifier(selectedName, "drawable", context.packageName)

        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            ?: error("Failed to decode $selectedName texture")

        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ids[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        return ids[0]
    }

    private fun createSphereInterleavedBuffer(stacks: Int, slices: Int): FloatBuffer {
        val data = mutableListOf<Float>()
        for (stack in 0 until stacks) {
            val v1 = stack.toFloat() / stacks.toFloat()
            val v2 = (stack + 1).toFloat() / stacks.toFloat()
            val phi1 = Math.PI * v1 - Math.PI / 2.0
            val phi2 = Math.PI * v2 - Math.PI / 2.0
            for (slice in 0 until slices) {
                val u1 = slice.toFloat() / slices.toFloat()
                val u2 = (slice + 1).toFloat() / slices.toFloat()
                val theta1 = 2.0 * Math.PI * u1
                val theta2 = 2.0 * Math.PI * u2

                val p1 = point(phi1, theta1)
                val p2 = point(phi2, theta1)
                val p3 = point(phi2, theta2)
                val p4 = point(phi1, theta2)

                addVertex(data, p1, u1, 1f - v1)
                addVertex(data, p2, u1, 1f - v2)
                addVertex(data, p3, u2, 1f - v2)

                addVertex(data, p1, u1, 1f - v1)
                addVertex(data, p3, u2, 1f - v2)
                addVertex(data, p4, u2, 1f - v1)
            }
        }
        return createFloatBuffer(data.toFloatArray())
    }

    private fun addVertex(target: MutableList<Float>, p: FloatArray, u: Float, v: Float) {
        target.add(p[0]); target.add(p[1]); target.add(p[2]); target.add(1f - u); target.add(v)
    }

    private fun createSatelliteModelVertexBuffer(): FloatBuffer {
        val vertices = mutableListOf<Float>()
        addBox(vertices, -0.55f, -0.55f, -0.35f, 0.55f, 0.55f, 0.35f)
        addBox(vertices, -2.15f, -0.08f, -0.04f, -0.65f, 0.08f, 0.04f)
        addBox(vertices, 0.65f, -0.08f, -0.04f, 2.15f, 0.08f, 0.04f)
        addBox(vertices, -3.4f, -0.35f, -0.03f, -2.15f, 0.35f, 0.03f)
        addBox(vertices, 2.15f, -0.35f, -0.03f, 3.4f, 0.35f, 0.03f)
        addBox(vertices, -0.18f, 0.55f, -0.18f, 0.18f, 1.15f, 0.18f)
        return createFloatBuffer(vertices.toFloatArray())
    }

    private fun addBox(target: MutableList<Float>, minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float) {
        fun v(x: Float, y: Float, z: Float) {
            target.add(x); target.add(y); target.add(z)
        }

        v(minX, minY, maxZ); v(maxX, minY, maxZ); v(maxX, maxY, maxZ); v(minX, minY, maxZ); v(maxX, maxY, maxZ); v(minX, maxY, maxZ)
        v(minX, minY, minZ); v(minX, maxY, minZ); v(maxX, maxY, minZ); v(minX, minY, minZ); v(maxX, maxY, minZ); v(maxX, minY, minZ)
        v(minX, minY, minZ); v(minX, minY, maxZ); v(minX, maxY, maxZ); v(minX, minY, minZ); v(minX, maxY, maxZ); v(minX, maxY, minZ)
        v(maxX, minY, minZ); v(maxX, maxY, minZ); v(maxX, maxY, maxZ); v(maxX, minY, minZ); v(maxX, maxY, maxZ); v(maxX, minY, maxZ)
        v(minX, maxY, minZ); v(minX, maxY, maxZ); v(maxX, maxY, maxZ); v(minX, maxY, minZ); v(maxX, maxY, maxZ); v(maxX, maxY, minZ)
        v(minX, minY, minZ); v(maxX, minY, minZ); v(maxX, minY, maxZ); v(minX, minY, minZ); v(maxX, minY, maxZ); v(minX, minY, maxZ)
    }

    private fun createOrbitPathVertexBuffer(segments: Int): FloatBuffer {
        val vertices = FloatArray(segments * COORDS_PER_VERTEX)
        for (index in 0 until segments) {
            val angle = (index.toDouble() / segments.toDouble()) * Math.PI * 2.0
            val base = index * COORDS_PER_VERTEX
            vertices[base] = cos(angle).toFloat()
            vertices[base + 1] = sin(angle).toFloat()
            vertices[base + 2] = 0f
        }
        return createFloatBuffer(vertices)
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

    companion object {
        private const val COORDS_PER_VERTEX = 3
        private const val STRIDE_BYTES = COORDS_PER_VERTEX * 4
        private const val SPHERE_STRIDE_FLOATS = 5
        private const val SPHERE_STRIDE_BYTES = SPHERE_STRIDE_FLOATS * 4
        private const val INITIAL_GLOBE_YAW_DEGREES = -90f

        private const val VERTEX_SHADER = "attribute vec3 aPosition; attribute vec2 aTexCoord; uniform mat4 uMvp; varying vec3 vWorld; varying vec2 vTexCoord; void main(){ vWorld = normalize(aPosition); vTexCoord = aTexCoord; gl_Position = uMvp * vec4(aPosition,1.0); }"
        private const val FRAGMENT_SHADER = "precision mediump float; uniform float uPlanetDetail; uniform vec3 uSunDirection; uniform vec4 uObjectColor; uniform sampler2D uEarthDayTex; uniform sampler2D uEarthNightTex; varying vec3 vWorld; varying vec2 vTexCoord; void main(){ if (uPlanetDetail < 0.5) { gl_FragColor = uObjectColor; return; } vec3 dayAlbedo = texture2D(uEarthDayTex, vTexCoord).rgb; vec3 nightAlbedo = texture2D(uEarthNightTex, vTexCoord).rgb; vec3 n = normalize(vWorld); float ndotl = dot(n, normalize(uSunDirection)); float dayFactor = smoothstep(-0.1, 0.22, ndotl); float diffuse = clamp(ndotl, 0.0, 1.0); float ambient = 0.06; vec3 litDay = dayAlbedo * (ambient + diffuse * 0.94); float nightGlow = smoothstep(0.2, -0.25, ndotl); vec3 litNight = nightAlbedo * (0.22 + nightGlow * 0.85); vec3 color = mix(litNight, litDay, dayFactor); gl_FragColor = vec4(color, 1.0); }"
    }
}
