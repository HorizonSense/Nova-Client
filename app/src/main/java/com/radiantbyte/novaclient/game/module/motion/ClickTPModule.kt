package com.radiantbyte.novaclient.game.module.motion

import com.radiantbyte.novaclient.game.InterceptablePacket
import com.radiantbyte.novaclient.game.Module
import com.radiantbyte.novaclient.game.ModuleCategory
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket

class ClickTPModule : Module("ClickTP", ModuleCategory.Motion) {
    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) {
            return
        }
        var packet = interceptablePacket.packet

        if(packet is InventoryTransactionPacket) {
            if (packet.transactionType == InventoryTransactionType.ITEM_USE && packet.actionType == 0) {
                val teleportPosition = Vector3f.from(
                    packet.blockPosition.x.toDouble() + 0.5,
                    packet.blockPosition.y.toDouble() + 2.62,
                    packet.blockPosition.z.toDouble() + 0.5
                )
                session.localPlayer.teleport(teleportPosition)
            }
        }
    }
}