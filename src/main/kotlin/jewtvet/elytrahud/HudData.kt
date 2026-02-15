package jewtvet.elytrahud

import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.EquipmentSlot
import java.lang.reflect.Method

class HudData {
    var speed = 0.0
        private set
    var verticalSpeed = 0.0
        private set
    var durability = 1.0
        private set
    var currentDurability = 0
        private set
    var height = 0.0
        private set
    var yaw = 180.0
        private set
    var pitch = 0.0
        private set
    var roll = 0.0f
        private set

    private var prevYPosition: Double? = null

    fun update() {
        val client = Common.client ?: return
        val player = client.player ?: return

        speed = player.velocity.length() * 20.0
        
        val currentYPosition = player.y
        verticalSpeed = prevYPosition?.let { (currentYPosition - it) * 20.0 } ?: 0.0
        prevYPosition = currentYPosition

        height = player.y
        yaw = player.yaw.toDouble()
        pitch = player.pitch.toDouble()
        roll = getRoll(client)
        
        val chestSlot = player.getEquippedStack(EquipmentSlot.CHEST)
        if (!chestSlot.isEmpty && chestSlot.contains(DataComponentTypes.GLIDER)) {
            val maxDamage = chestSlot.maxDamage
            if (maxDamage > 0) {
                currentDurability = maxDamage - chestSlot.damage
                durability = currentDurability.toDouble() / maxDamage
            } else {
                currentDurability = 0
                durability = 1.0
            }
        } else {
            durability = 0.0
            currentDurability = 0
        }
    }

    companion object {
        private var methodChecked = false
        private var getRollMethod: Method? = null

        private fun checkForDoABarrelRollMethod(client: MinecraftClient) {
            try {
                val cameraEntity = client.cameraEntity
                if (cameraEntity != null) {
                    getRollMethod = cameraEntity.javaClass.getMethod("doABarrelRoll\$getRoll")
                }
            } catch (e: NoSuchMethodException) {
                getRollMethod = null
            }
            methodChecked = true
        }

        fun getRoll(client: MinecraftClient): Float {
            if (!methodChecked) {
                checkForDoABarrelRollMethod(client)
            }

            return getRollMethod?.let { method ->
                try {
                    client.cameraEntity?.let { method.invoke(it) as Float }
                } catch (e: Exception) {
                    null
                }
            } ?: 0.0f
        }
    }
}
