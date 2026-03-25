package inorganic.elytrahud

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth

class HudRenderer(private val client: Minecraft) {
    private var displayedSpeed = 0.0
    private var displayedDur = 1.0
    private var displayedHeight = 0.0
    private var displayedVertical = 0.0
    private var displayedYaw = 180.0
    private var displayedPitch = 0.0
    private var displayedRoll = 0.0f

    fun render(graphics: GuiGraphicsExtractor, tickDelta: net.minecraft.client.DeltaTracker) {
        val config = Common.CONFIG ?: return
        if (!config.modEnabled) return

        val hudData = Common.hudData

        var rateX = 10
        var durX = 10
        var vertX = 10

        displayedSpeed = Mth.lerp(tickDelta.gameTimeDeltaTicks.toDouble(), displayedSpeed, hudData.speed)
        displayedDur = hudData.durability
        displayedHeight = hudData.height
        displayedVertical = hudData.verticalSpeed
        displayedYaw = hudData.yaw * -1.0 + 180.0
        displayedPitch = hudData.pitch
        displayedRoll = hudData.roll

        val intAirspeed = kotlin.math.round(displayedSpeed).toInt()
        val intDur = hudData.currentDurability
        val intHeight = kotlin.math.round(displayedHeight).toInt()
        val intVertical = kotlin.math.round(displayedVertical).toInt()
        val intPitch = kotlin.math.round(displayedPitch).toInt()

        val defaultY = if (config.renderValues) 15 else 10

        val scaledWidth = client.window.guiScaledWidth
        val scaledHeight = client.window.guiScaledHeight

        if (config.renderAirspeed) {
            rateX += 102
            durX += 102
            val speedometerX = 10
            val speedometerY = 100 + defaultY
            val yPos = scaledHeight - speedometerY

            val speedToRender = minOf(displayedSpeed, 80.0)

            renderMeter(graphics, speedometerX + 50, yPos + 50, (speedToRender * 4.5).toFloat(), intAirspeed, config)
        }

        if (config.renderHorizon) {
            durX += 52
            val rateY = 50 + defaultY
            val yPos = scaledHeight - rateY
            val intPitchY = maxOf(-44, minOf(44, intPitch / 2))

            renderHorizon(graphics, rateX + 25, yPos + 25, displayedRoll, intPitch, config, intPitchY)
        }

        if (config.renderDurability) {
            val durY = 50 + defaultY
            val topPoint = scaledHeight - durY + 2
            val bottomPoint = topPoint + 44
            val yCoordinate = (topPoint + (1.0 - displayedDur) * (bottomPoint - topPoint)).toInt()

            renderBar(graphics, durX, scaledHeight - durY, yCoordinate, intDur, config)
        }

        if (config.renderAltitude) {
            vertX += 102
            val altitudeX = 10 + 100
            val altitudeY = 100 + defaultY
            val xPos = scaledWidth - altitudeX
            val yPos = scaledHeight - altitudeY

            renderDoubleMeter(graphics, xPos + 50, yPos + 50,
                (displayedHeight * 0.36).toFloat(),
                (displayedHeight * 3.6).toFloat(),
                intHeight, config)
        }

        if (config.renderVertical) {
            val vertSize = 50
            vertX += vertSize
            val vertY = vertSize + defaultY
            val xPos = scaledWidth - vertX
            val yPos = scaledHeight - vertY

            val verticalToRender = maxOf(-5.0, minOf(5.0, displayedVertical))

            renderVerticalMeter(graphics, xPos + 25, yPos + 25,
                ((verticalToRender + 5.0) * 25.0 + 145.0).toFloat() % 360.0f,
                intVertical, config)
        }

        if (config.renderCompass) {
            val compassScreen = scaledWidth - 120
            val compassOffset = config.compassDefaultX / 100.0 * compassScreen
            val compassX = kotlin.math.round(compassOffset).toInt() + 10
            val compassY = 15

            renderCompass(graphics, compassX + 50, compassY + 50, Math.toRadians(displayedYaw).toFloat())

            val intYaw = ((kotlin.math.round(hudData.yaw).toInt() + 180) % 360 + 360) % 360
            graphics.text(client.font, String.format("%3d°", intYaw), compassX + 37, compassY - 6, -1)
        }
    }

    private fun renderMeter(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float, valueInt: Int, config: ElytraHudConfig) {
        val defaultY = if (config.renderValues) 15 else 10
        val scaledHeight = client.window.guiScaledHeight
        val yPos = scaledHeight - (100 + defaultY)

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 50, yPos, 0f, 0f, 100, 100, 256, 256, -1)

        HudRenderHelper.renderMeter(graphics, centerX, centerY, angleDegrees)

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 5, centerY - 5, 215f, 73f, 10, 10, 256, 256, -1)

        if (config.renderTitles) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 17, yPos - 10, 215f, 0f, 34, 9, 256, 256, -1)
        }

        if (config.renderValues) {
            val valueX = centerX - 10
            val valueY = yPos + 101
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, valueX, valueY, 215f, 165f, 21, 11, 256, 256, -1)
            graphics.text(client.font, String.format("%3d", valueInt), valueX + 2, valueY + 2, -1)
        }
    }

    private fun renderVerticalMeter(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float, valueInt: Int, config: ElytraHudConfig) {
        val defaultY = if (config.renderValues) 15 else 10
        val scaledHeight = client.window.guiScaledHeight
        val yPos = scaledHeight - (50 + defaultY)

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 25, yPos, 100f, 50f, 50, 50, 256, 256, -1)

        HudRenderHelper.renderVerticalMeter(graphics, centerX, centerY, angleDegrees)

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 3, centerY - 3, 215f, 99f, 6, 6, 256, 256, -1)

        if (config.renderTitles) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 14, yPos - 10, 215f, 27f, 28, 9, 256, 256, -1)
        }

        if (config.renderValues) {
            val valueX = centerX - 10
            val valueY = yPos + 51
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, valueX, valueY, 215f, 165f, 21, 11, 256, 256, -1)
            graphics.text(client.font, String.format("%3d", valueInt), valueX + 2, valueY + 2, -1)
        }
    }

    private fun renderHorizon(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleRoll: Float, valueInt: Int, config: ElytraHudConfig, pitchOffset: Int) {
        val defaultY = if (config.renderValues) 15 else 10
        val scaledHeight = client.window.guiScaledHeight
        val yPos = scaledHeight - (50 + defaultY)

        val horizonY = 41 + pitchOffset
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 25, yPos, 100f, 100f, 50, 50, 256, 256, -1)
        
        val horizonList = listOf(
            Triple(1, 10, 20),
            Triple(1, 16, 17),
            Triple(1, 22, 14),
            Triple(1, 24, 13),
            Triple(1, 26, 12),
            Triple(1, 30, 10),
            Triple(1, 32, 9),
            Triple(2, 34, 8),
            Triple(1, 36, 7),
            Triple(1, 38, 6),
            Triple(3, 40, 5),
            Triple(3, 42, 4),
            Triple(5, 44, 3)
        )
        
        var horizonX = 0
        for (row in horizonList) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 22 + horizonX, yPos + 3 - 3 + row.third, 170f + horizonX, (horizonY + row.third).toFloat(), row.first, row.second, 256, 256, -1)
            horizonX += row.first
        }
        
        for (row in horizonList.reversed()) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 22 + horizonX, yPos + 3 - 3 + row.third, 170f + horizonX, (horizonY + row.third).toFloat(), row.first, row.second, 256, 256, -1)
            horizonX += row.first
        }

        HudRenderHelper.renderHorizon(graphics, centerX, centerY, angleRoll)

        if (config.renderTitles) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 15, yPos - 10, 215f, 179f, 29, 9, 256, 256, -1)
        }

        if (config.renderValues) {
            val valueX = centerX - 10
            val valueY = yPos + 51
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, valueX, valueY, 215f, 165f, 21, 11, 256, 256, -1)
            graphics.text(client.font, String.format("%3d", valueInt), valueX + 2, valueY + 2, -1)
        }
    }

private fun renderBar(graphics: GuiGraphicsExtractor, x: Int, yPos: Int, yCoordinate: Int, valueInt: Int, config: ElytraHudConfig) {
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, x, yPos, 150f, 0f, 15, 50, 256, 256, -1)
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, x + 4, yCoordinate, 215f, 56f, 4, 3, 256, 256, -1)

        if (config.renderTitles) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, x, yPos - 10, 215f, 18f, 15, 9, 256, 256, -1)
        }

        if (config.renderValues) {
            val valueX = x - 3
            val valueY = yPos + 51
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, valueX, valueY, 215f, 165f, 21, 11, 256, 256, -1)
            graphics.text(client.font, String.format("%3d", valueInt), valueX + 2, valueY + 2, -1)
        }
    }

    private fun renderDoubleMeter(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angle1: Float, angle2: Float, valueInt: Int, config: ElytraHudConfig) {
        val defaultY = if (config.renderValues) 15 else 10
        val scaledHeight = client.window.guiScaledHeight
        val yPos = scaledHeight - (100 + defaultY)

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 50, yPos, 0f, 100f, 100, 100, 256, 256, -1)

        HudRenderHelper.renderDoubleMeterNeedle1(graphics, centerX, centerY, angle1)
        HudRenderHelper.renderDoubleMeterNeedle2(graphics, centerX, centerY, angle2)

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 5, centerY - 5, 215f, 83f, 10, 10, 256, 256, -1)

        if (config.renderTitles) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 16, yPos - 10, 215f, 36f, 32, 9, 256, 256, -1)
        }

        if (config.renderValues) {
            val valueX = centerX - 10
            val valueY = yPos + 101
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, valueX, valueY, 215f, 165f, 21, 11, 256, 256, -1)
            graphics.text(client.font, String.format("%3d", valueInt), valueX + 2, valueY + 2, -1)
        }
    }

    private fun renderCompass(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleRadians: Float) {
        val matrices = graphics.pose()
        
        matrices.pushMatrix()
        matrices.translate(centerX.toFloat(), centerY.toFloat())
        matrices.rotate(angleRadians)
        matrices.translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 50, centerY - 50, 100f, 150f, 100, 100, 256, 256, -1)
        matrices.popMatrix()

        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 5, centerY - 47, 215f, 59f, 6, 5, 256, 256, -1)
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 5, centerY - 6, 215f, 64f, 10, 9, 256, 256, -1)
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 15, centerY - 58, 215f, 45f, 26, 11, 256, 256, -1)
    }

    companion object {
        private val WIDGETS_TEXTURE = Identifier.fromNamespaceAndPath("elytrahud", "textures/hud_widgets.png")
    }
}
