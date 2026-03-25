package inorganic.elytrahud

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.resources.Identifier
import net.minecraft.client.renderer.RenderPipelines

object HudRenderHelper {
    private val WIDGETS_TEXTURE = Identifier.fromNamespaceAndPath("elytrahud", "textures/hud_widgets.png")

    fun renderMeter(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float) {
        val angleRad = Math.toRadians(angleDegrees.toDouble()).toFloat()
        
        graphics.pose().pushMatrix()
        graphics.pose().translate(centerX.toFloat(), centerY.toFloat())
        graphics.pose().rotate(angleRad)
        graphics.pose().translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 1, centerY - 42, 215f, 105f, 2, 42, 256, 256, -1)
        graphics.pose().popMatrix()
    }

    fun renderVerticalMeter(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float) {
        val angleRad = Math.toRadians(angleDegrees.toDouble()).toFloat()
        
        graphics.pose().pushMatrix()
        graphics.pose().translate(centerX.toFloat(), centerY.toFloat())
        graphics.pose().rotate(angleRad)
        graphics.pose().translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 1, centerY - 18, 215f, 147f, 2, 18, 256, 256, -1)
        graphics.pose().popMatrix()
    }

    fun renderHorizon(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float) {
        val angleRad = Math.toRadians(angleDegrees.toDouble()).toFloat()
        
        graphics.pose().pushMatrix()
        graphics.pose().translate(centerX.toFloat(), centerY.toFloat())
        graphics.pose().rotate(angleRad)
        graphics.pose().translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 13, centerY - 2, 215f, 176f, 26, 3, 256, 256, -1)
        graphics.pose().popMatrix()
    }

    fun renderDoubleMeterNeedle1(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float) {
        val angleRad = Math.toRadians(angleDegrees.toDouble()).toFloat()
        
        graphics.pose().pushMatrix()
        graphics.pose().translate(centerX.toFloat(), centerY.toFloat())
        graphics.pose().rotate(angleRad)
        graphics.pose().translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 1, centerY - 18, 215f, 147f, 2, 18, 256, 256, -1)
        graphics.pose().popMatrix()
    }

    fun renderDoubleMeterNeedle2(graphics: GuiGraphicsExtractor, centerX: Int, centerY: Int, angleDegrees: Float) {
        val angleRad = Math.toRadians(angleDegrees.toDouble()).toFloat()
        
        graphics.pose().pushMatrix()
        graphics.pose().translate(centerX.toFloat(), centerY.toFloat())
        graphics.pose().rotate(angleRad)
        graphics.pose().translate((-centerX).toFloat(), (-centerY).toFloat())
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, centerX - 1, centerY - 42, 215f, 105f, 2, 42, 256, 256, -1)
        graphics.pose().popMatrix()
    }
}