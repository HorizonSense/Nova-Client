package com.radiantbyte.novaclient.game


import android.content.Context
import android.net.Uri
import com.radiantbyte.novaclient.application.AppContext
import com.radiantbyte.novaclient.game.module.combat.*
import com.radiantbyte.novaclient.game.module.misc.*
import com.radiantbyte.novaclient.game.module.world.*
import com.radiantbyte.novaclient.game.module.motion.*
import com.radiantbyte.novaclient.game.module.visual.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File

object ModuleManager {

    private val _modules: MutableList<Module> = ArrayList()

    val modules: List<Module> = _modules

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    init {
        with(_modules) {
            // Combat
            add(KillauraModule())
            add(AntiKnockbackModule())
            add(AntiCrystalModule())
            add(HitAndRunModule())
            add(HitboxModule())
            add(CrystalSmashModule())
            add(TriggerBotModule())
            
            // Motion
            add(UnifiedFlyModule())
            add(FlyModule())
            add(SpeedModule())
            add(AirJumpModule())
            add(ClickTPModule())
            add(ClipModule())
            add(NoClipModule())
            add(JetPackModule())
            add(HighJumpModule())
            add(BhopModule())
            add(SprintModule())
            add(AutoWalkModule())
            add(AntiAFKModule())
            add(MotionFlyModule())
            add(SpiderModule())
            
            // Visual
            add(ESPModule())
            add(FullbrightModule())
            add(ZoomModule())
            add(NoHurtCameraModule())
            add(SpeedDisplayModule())
            add(PositionDisplayModule())
            add(NetworkInfoModule())
            add(WorldStateModule())
            add(MinimapModule())
            add(CrosshairModule())
            add(TargetHudModule())
            
            // World
            add(FreeCameraModule())
            add(TimeShiftModule())
            add(WeatherControllerModule())
            add(EffectsModule())
            add(ParticlesModule())
            add(AntiDebuffModule())
            add(ChestStealerModule())
            
            // Misc
            add(AutoDisconnectModule())
            add(DesyncModule())
            add(PositionLoggerModule())
            add(NoChatModule())
            add(CommandHandlerModule())
            add(ReplayModule())
            add(BaritoneModule())
            add(ArrayListModule())
            add(WaterMarkModule())
            add(KeyStrokesModule())
            add(CoordinatesModule())
            add(PieChartModule())
            add(FakeDeathModule())
            add(FakeXPModule())
            add(MinerModule())
        }
    }

    fun saveConfig() {
        val configsDir = AppContext.instance.filesDir.resolve("configs")
        configsDir.mkdirs()

        val config = configsDir.resolve("UserConfig.json")
        val jsonObject = buildJsonObject {
            put("modules", buildJsonObject {
                _modules.forEach {
                    if (it.private) {
                        return@forEach
                    }
                    put(it.name, it.toJson())
                }
            })
        }

        config.writeText(json.encodeToString(jsonObject))
    }

    fun loadConfig() {
        val configsDir = AppContext.instance.filesDir.resolve("configs")
        configsDir.mkdirs()

        val config = configsDir.resolve("UserConfig.json")
        if (!config.exists()) {
            return
        }

        val jsonString = config.readText()
        if (jsonString.isEmpty()) {
            return
        }

        val jsonObject = json.parseToJsonElement(jsonString).jsonObject
        val modules = jsonObject["modules"]!!.jsonObject
        _modules.forEach { module ->
            (modules[module.name] as? JsonObject)?.let {
                module.fromJson(it)
            }
        }
    }

    fun exportConfig(): String {
        val jsonObject = buildJsonObject {
            put("modules", buildJsonObject {
                _modules.forEach {
                    if (it.private) {
                        return@forEach
                    }
                    put(it.name, it.toJson())
                }
            })
        }
        return json.encodeToString(jsonObject)
    }

    fun importConfig(configStr: String) {
        try {
            val jsonObject = json.parseToJsonElement(configStr).jsonObject
            val modules = jsonObject["modules"]?.jsonObject ?: return

            _modules.forEach { module ->
                modules[module.name]?.let {
                    if (it is JsonObject) {
                        module.fromJson(it)
                    }
                }
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid config format")
        }
    }

    fun exportConfigToFile(context: Context, fileName: String): Boolean {
        return try {
            val configsDir = context.getExternalFilesDir("configs")
            configsDir?.mkdirs()

            val configFile = File(configsDir, "$fileName.json")
            configFile.writeText(exportConfig())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun importConfigFromFile(context: Context, uri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val configStr = input.bufferedReader().readText()
                importConfig(configStr)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}