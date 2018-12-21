package com.gmail.mtswetkov.ocrraces

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig



class myApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val api_yandex_key = "72360211-0289-404f-9d7c-2cd454c7149f"
        // Создание расширенной конфигурации библиотеки.
        val config = YandexMetricaConfig.newConfigBuilder(api_yandex_key).build()
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this)
    }

}