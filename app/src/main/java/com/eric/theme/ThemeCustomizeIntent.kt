package com.eric.theme

sealed class ThemeCustomizeIntent {
    data class UpdateBrightness(val value: Float) : ThemeCustomizeIntent()
    data class UpdateOpacity(val value: Float) : ThemeCustomizeIntent()
    data class SwitchTheme(val isDark: Boolean) : ThemeCustomizeIntent()
    object Apply : ThemeCustomizeIntent()
}

data class ThemeCustomizeState(
    val brightness: Float = 1f,
    val borderOpacity: Float = 1f,
    val isDarkTheme: Boolean = false
)

class ThemeCustomizeViewModel : BaseMviViewModel<ThemeCustomizeIntent, ThemeCustomizeState>(
    ThemeCustomizeState()
) {
    override fun handleIntent(intent: ThemeCustomizeIntent) {
        when (intent) {
            is ThemeCustomizeIntent.UpdateBrightness ->
                setState { it.copy(brightness = intent.value) }
            is ThemeCustomizeIntent.UpdateOpacity ->
                setState { it.copy(borderOpacity = intent.value) }
            is ThemeCustomizeIntent.SwitchTheme ->
                setState { it.copy(isDarkTheme = intent.isDark) }
            is ThemeCustomizeIntent.Apply -> {
                // 保存配置逻辑可以放这里
            }
        }
    }
}
