package jewtvet.elytrahud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class HudRenderer(private val client: MinecraftClient) {
    private var displayedSpeed = 0.0
    private var displayedDur = 1.0
    private var displayedHeight = 0.0
    private var displayedVertical = 0.0
    private var displayedYaw = 180.0
    private var displayedPitch = 0.0
    private var displayedRoll = 0.0

    data class TextureRegion(val x: Int, val y: Int, val u: Float, val v: Float, val width: Int, val height: Int)

    fun render(graphics: DrawContext, tickDelta: Float) {
        val hudData = Common.hudData
        val config = Common.CONFIG ?: return

        var rateX = 10
        var durX = 10
        var vertX = 10
        displayedSpeed = MathHelper.lerp(tickDelta.toDouble(), displayedSpeed, hudData.speed)
        displayedDur = hudData.durability
        displayedHeight = hudData.height
        displayedVertical = hudData.verticalSpeed
        displayedYaw = hudData.yaw * -1.0 + 180.0
        displayedPitch = hudData.pitch
        displayedRoll = hudData.roll.toDouble()

        val intAirspeed = Math.round(displayedSpeed).toInt()
        val intDur = hudData.currentDurability
        val intHeight = Math.round(displayedHeight).toInt()
        val intVertical = Math.round(displayedVertical).toInt()
        val intPitch = Math.round(displayedPitch).toInt()

        val defaultY = if (config.renderValues) 15 else 10

        val scaledWidth = client.window.scaledWidth
        val scaledHeight = client.window.scaledHeight

        if (config.renderAirspeed) {
            rateX += 102
            durX += 102
            val speedometerSize = 100
            val speedometerX = 10
            val speedometerY = speedometerSize + defaultY
            val yPos = scaledHeight - speedometerY
            
            val backGr = TextureRegion(speedometerX, yPos, 0f, 0f, 100, 100)
            val pointer = TextureRegion(speedometerX + 49, yPos + 8, 215f, 105f, 2, 42)
            val foreGr = TextureRegion(speedometerX + 45, yPos + 45, 215f, 73f, 10, 10)
            val title = TextureRegion(speedometerX + 33, yPos - 10, 215f, 0f, 34, 9)
            val value = TextureRegion(speedometerX + 40, yPos + 101, 215f, 165f, 21, 11)
            
            val speedToRender = displayedSpeed.coerceAtMost(80.0)

            renderMeter(
                graphics,
                speedometerX + 50,
                yPos + 50,
                Math.toRadians(speedToRender * 4.5).toFloat(),
                backGr, pointer, foreGr, title, value,
                intAirspeed
            )
        }

        if (config.renderHorizon) {
            durX += 52
            val rateSize = 50
            val rateY = rateSize + defaultY
            val yPos = scaledHeight - rateY
            val intPitchY = (intPitch / 2).coerceIn(-44, 44)

            val horizonY = 41 + intPitchY
            val horizon = TextureRegion(rateX + 3, yPos + 3, 170f, horizonY.toFloat(), 44, 44)
            val backGr = TextureRegion(rateX, yPos, 100f, 100f, 50, 50)
            val pointer = TextureRegion(rateX + 12, yPos + 24, 215f, 176f, 26, 3)
            val title = TextureRegion(rateX + 10, yPos - 10, 215f, 179f, 29, 9)
            val value = TextureRegion(rateX + 15, yPos + 51, 215f, 165f, 21, 11)
            
            renderHorizon(
                graphics,
                rateX + 25,
                yPos + 25,
                Math.toRadians(displayedRoll).toFloat(),
                horizon, backGr, pointer, title, value,
                intPitch
            )
        }

        if (config.renderDurability) {
            val durSize = 50
            val durY = durSize + defaultY
            val topPoint = scaledHeight - durY + 2
            val bottomPoint = topPoint + 44
            val yCoordinate = (topPoint + (1.0 - displayedDur) * (bottomPoint - topPoint)).toInt()
            val yPos = scaledHeight - durY
            
            val backGr = TextureRegion(durX, yPos, 150f, 0f, 15, 50)
            val pointer = TextureRegion(durX + 8, yCoordinate, 215f, 56f, 4, 3)
            val title = TextureRegion(durX, yPos - 10, 215f, 18f, 15, 9)
            val value = TextureRegion(durX - 3, yPos + 51, 215f, 165f, 21, 11)
            
            renderBar(graphics, backGr, pointer, title, value, intDur)
        }

        if (config.renderAltitude) {
            vertX += 102
            val altitudeSize = 100
            val altitudeX = 10 + altitudeSize
            val altitudeY = altitudeSize + defaultY
            val xPos = scaledWidth - altitudeX
            val yPos = scaledHeight - altitudeY
            
            val backGr = TextureRegion(xPos, yPos, 0f, 100f, 100, 100)
            val pointer1 = TextureRegion(xPos + 49, yPos + 32, 215f, 147f, 2, 18)
            val pointer2 = TextureRegion(xPos + 49, yPos + 8, 215f, 105f, 2, 42)
            val foreGr = TextureRegion(xPos + 45, yPos + 45, 215f, 83f, 10, 10)
            val title = TextureRegion(xPos + 34, yPos - 10, 215f, 36f, 32, 9)
            val value = TextureRegion(xPos + 40, yPos + 101, 215f, 165f, 21, 11)
            
            renderDoubleMeter(
                graphics,
                xPos + 50,
                yPos + 50,
                Math.toRadians(displayedHeight * 0.36).toFloat(),
                Math.toRadians(displayedHeight * 3.6).toFloat(),
                backGr, pointer1, pointer2, foreGr, title, value,
                intHeight
            )
        }

        if (config.renderVertical) {
            val vertSize = 50
            vertX += vertSize
            val vertY = vertSize + defaultY
            val xPos = scaledWidth - vertX
            val yPos = scaledHeight - vertY
            
            val backGr = TextureRegion(xPos, yPos, 100f, 50f, 50, 50)
            val pointer = TextureRegion(xPos + 24, yPos + 7, 215f, 147f, 2, 18)
            val foreGr = TextureRegion(xPos + 22, yPos + 22, 215f, 99f, 6, 6)
            val title = TextureRegion(xPos + 11, yPos - 10, 215f, 27f, 28, 9)
            val value = TextureRegion(xPos + 15, yPos + 51, 215f, 165f, 21, 11)
            
            val verticalToRender = displayedVertical.coerceIn(-5.0, 5.0)

            renderMeter(
                graphics,
                xPos + 25,
                yPos + 25,
                Math.toRadians((verticalToRender + 5.0) * 25.0 + 145.0).toFloat() % 360.0f,
                backGr, pointer, foreGr, title, value,
                intVertical
            )
        }

        if (config.renderCompass) {
            val compassScreen = scaledWidth - 120
            val compassOffset = config.compassDefaultX / 100.0 * compassScreen
            val compassX = Math.round(compassOffset).toInt() + 10
            val compassY = 15
            
            val backGr = TextureRegion(compassX, compassY, 100f, 150f, 100, 100)
            val pointer = TextureRegion(compassX + 45, compassY + 3, 215f, 59f, 6, 5)
            val foreGr = TextureRegion(compassX + 45, compassY + 44, 215f, 64f, 10, 9)
            val title = TextureRegion(compassX + 35, compassY - 8, 215f, 45f, 26, 11)
            
            renderCompass(
                graphics,
                compassX + 50,
                compassY + 50,
                Math.toRadians(displayedYaw).toFloat(),
                backGr, pointer, foreGr, title
            )
            val intYaw = (Math.round(hudData.yaw).toInt() + 180) % 360
            graphics.drawText(client.textRenderer, String.format("%3d\u00b0", intYaw), compassX + 35 + 2, compassY - 8 + 2, 0xFFFFFF, false)
        }
    }

    private fun DrawContext.drawRegion(region: TextureRegion) {
        this.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, region.x, region.y, region.u, region.v, region.width, region.height, 256, 256, -1)
    }

    private fun renderDoubleMeter(
        graphics: DrawContext,
        centerX: Int,
        centerY: Int,
        angle1: Float,
        angle2: Float,
        backGr: TextureRegion,
        pointer1: TextureRegion,
        pointer2: TextureRegion,
        foreGr: TextureRegion,
        title: TextureRegion,
        value: TextureRegion,
        valueInt: Int
    ) {
        graphics.drawRegion(backGr)
        val matrices = graphics.matrices
        
        matrices.pushMatrix()
        matrices.translate(centerX.toFloat(), centerY.toFloat())
        matrices.rotate(angle1)
        matrices.translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.drawRegion(pointer1)
        matrices.popMatrix()
        
        matrices.pushMatrix()
        matrices.translate(centerX.toFloat(), centerY.toFloat())
        matrices.rotate(angle2)
        matrices.translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.drawRegion(pointer2)
        matrices.popMatrix()
        
        graphics.drawRegion(foreGr)
        
        if (Common.CONFIG!!.renderTitles) {
            graphics.drawRegion(title)
        }

        if (Common.CONFIG!!.renderValues) {
            graphics.drawRegion(value)
            graphics.drawText(client.textRenderer, String.format("%3d", valueInt), value.x + 2, value.y + 2, 0xFFFFFF, false)
        }
    }

    private fun renderMeter(
        graphics: DrawContext,
        centerX: Int,
        centerY: Int,
        angle: Float,
        backGr: TextureRegion,
        pointer: TextureRegion,
        foreGr: TextureRegion,
        title: TextureRegion,
        value: TextureRegion,
        valueInt: Int
    ) {
        graphics.drawRegion(backGr)
        val matrices = graphics.matrices
        matrices.pushMatrix()
        matrices.translate(centerX.toFloat(), centerY.toFloat())
        matrices.rotate(angle)
        matrices.translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.drawRegion(pointer)
        matrices.popMatrix()
        
        graphics.drawRegion(foreGr)
        
        if (Common.CONFIG!!.renderTitles) {
            graphics.drawRegion(title)
        }

        if (Common.CONFIG!!.renderValues) {
            graphics.drawRegion(value)
            graphics.drawText(client.textRenderer, String.format("%3d", valueInt), value.x + 2, value.y + 2, 0xFFFFFF, false)
        }
    }

    private fun renderHorizon(
        graphics: DrawContext,
        centerX: Int,
        centerY: Int,
        angleRoll: Float,
        horizon: TextureRegion,
        backGr: TextureRegion,
        pointer: TextureRegion,
        title: TextureRegion,
        value: TextureRegion,
        valueInt: Int
    ) {
        val horizonList = arrayOf(
            intArrayOf(1, 10, 20),
            intArrayOf(1, 16, 17),
            intArrayOf(1, 22, 14),
            intArrayOf(1, 24, 13),
            intArrayOf(1, 26, 12),
            intArrayOf(1, 30, 10),
            intArrayOf(1, 32, 9),
            intArrayOf(2, 34, 8),
            intArrayOf(1, 36, 7),
            intArrayOf(1, 38, 6),
            intArrayOf(3, 40, 5),
            intArrayOf(3, 42, 4),
            intArrayOf(5, 44, 3)
        )
        var horizonX = 0

        for (row in horizonList) {
            graphics.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                WIDGETS_TEXTURE,
                horizon.x + horizonX,
                horizon.y - 3 + row[2],
                (horizon.u + horizonX),
                (horizon.v + row[2]),
                row[0],
                row[1],
                256,
                256,
                -1
            )
            horizonX += row[0]
        }

        for (row in horizonList.reversed()) {
            graphics.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                WIDGETS_TEXTURE,
                horizon.x + horizonX,
                horizon.y - 3 + row[2],
                (horizon.u + horizonX),
                (horizon.v + row[2]),
                row[0],
                row[1],
                256,
                256,
                -1
            )
            horizonX += row[0]
        }

        graphics.drawRegion(backGr)
        val matrices = graphics.matrices
        matrices.pushMatrix()
        matrices.translate(centerX.toFloat(), centerY.toFloat())
        matrices.rotate(angleRoll)
        matrices.translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.drawRegion(pointer)
        matrices.popMatrix()
        
        if (Common.CONFIG!!.renderTitles) {
            graphics.drawRegion(title)
        }

        if (Common.CONFIG!!.renderValues) {
            graphics.drawRegion(value)
            graphics.drawText(client.textRenderer, String.format("%3d", valueInt), value.x + 2, value.y + 2, 0xFFFFFF, false)
        }
    }

    private fun renderCompass(
        graphics: DrawContext,
        centerX: Int,
        centerY: Int,
        angle: Float,
        backGr: TextureRegion,
        pointer: TextureRegion,
        foreGr: TextureRegion,
        title: TextureRegion
    ) {
        val matrices = graphics.matrices
        matrices.pushMatrix()
        matrices.translate(centerX.toFloat(), centerY.toFloat())
        matrices.rotate(angle)
        matrices.translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.drawRegion(backGr)
        matrices.popMatrix()
        
        graphics.drawRegion(pointer)
        graphics.drawRegion(foreGr)
        graphics.drawRegion(title)
    }

    private fun renderBar(
        graphics: DrawContext,
        backGr: TextureRegion,
        pointer: TextureRegion,
        title: TextureRegion,
        value: TextureRegion,
        valueInt: Int
    ) {
        graphics.drawRegion(backGr)
        graphics.drawRegion(pointer)
        
        if (Common.CONFIG!!.renderTitles) {
            graphics.drawRegion(title)
        }

        if (Common.CONFIG!!.renderValues) {
            graphics.drawRegion(value)
            graphics.drawText(client.textRenderer, String.format("%3d", valueInt), value.x + 2, value.y + 2, 0xFFFFFF, false)
        }
    }

    companion object {
        private val WIDGETS_TEXTURE = Identifier.of("elytrahud", "textures/hud_widgets.png")
    }
}
