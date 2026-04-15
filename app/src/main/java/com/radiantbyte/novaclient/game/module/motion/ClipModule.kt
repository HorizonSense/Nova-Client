package com.radiantbyte.novaclient.game.module.motion

import com.radiantbyte.novaclient.game.InterceptablePacket
import com.radiantbyte.novaclient.game.Module
import com.radiantbyte.novaclient.game.ModuleCategory
import kotlin.math.cos
import kotlin.math.sin

class ClipModule : Module("Clip", ModuleCategory.Motion) {

    private var verticalValue by floatValue("Vertical", 3f, -10f..10f)
    private var horizontalValue by floatValue("Horizontal", 3f, -10f..10f)

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) return

        val player = session.localPlayer
        val yaw = Math.toRadians(player.rotationYaw.toDouble()).toFloat()
        player.teleport(
            player.posX - sin(yaw) * horizontalValue,
            player.posY + verticalValue,
            player.posZ + cos(yaw) * horizontalValue
        )
        isEnabled = false
    }
}