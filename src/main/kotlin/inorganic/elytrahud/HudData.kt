package inorganic.elytrahud

import net.minecraft.client.Minecraft
import net.minecraft.world.entity.EquipmentSlot

class HudData {
    @JvmField var speed = 0.0
    @JvmField var verticalSpeed = 0.0
    @JvmField var durability = 1.0
    @JvmField var currentDurability = 0
    @JvmField var height = 0.0
    @JvmField var yaw = 180.0
    @JvmField var pitch = 0.0
    @JvmField var roll = 0.0f

    private var prevYPosition: Double? = null

    fun update() {
        val client = Common.client ?: return
        val player = client.player ?: return

        speed = player.deltaMovement.length() * 20.0

        val currentYPosition = player.getY()
        if (prevYPosition != null) {
            verticalSpeed = (currentYPosition - prevYPosition!!) * 20.0
        } else {
            verticalSpeed = 0.0
        }
        prevYPosition = currentYPosition

        height = player.getY()
        yaw = player.getYRot().toDouble()
        pitch = player.getXRot().toDouble()
        roll = getRoll(client)

        val chestSlot = player.getItemBySlot(EquipmentSlot.CHEST)
        if (!chestSlot.isEmpty && chestSlot.item == net.minecraft.world.item.Items.ELYTRA) {
            val maxDamage = chestSlot.maxDamage
            if (maxDamage > 0) {
                currentDurability = maxDamage - chestSlot.damageValue
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
        private var getRollMethod: java.lang.reflect.Method? = null

        private fun checkForDoABarrelRollMethod(client: Minecraft) {
            try {
                val cameraEntity = client.cameraEntity
                if (cameraEntity != null) {
                    getRollMethod = cameraEntity.javaClass.getMethod("getRoll")
                }
            } catch (e: NoSuchMethodException) {
                getRollMethod = null
            }
            methodChecked = true
        }

        fun getRoll(client: Minecraft): Float {
            if (!methodChecked) {
                checkForDoABarrelRollMethod(client)
            }

            if (getRollMethod != null) {
                try {
                    val cameraEntity = client.cameraEntity
                    if (cameraEntity != null) {
                        return getRollMethod!!.invoke(cameraEntity) as Float
                    }
                } catch (e: Exception) {
                    // Ignore
                }
            }
            return 0.0f
        }
    }
}
