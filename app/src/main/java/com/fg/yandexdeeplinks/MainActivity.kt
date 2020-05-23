package com.fg.yandexdeeplinks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yandex.metrica.DeferredDeeplinkParametersListener
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import android.R.attr.keySet
import android.net.Uri
import android.util.Log
import android.content.Intent
import android.text.TextUtils
import com.yandex.metrica.push.YandexMetricaPush
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.keySet


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var q = 0

        initMetrika()

        Log.d("CLICK", "HELLo")

//        val action: String? = intent?.action
//        val data: Uri? = intent?.data
//
//

        val intent = intent

        handleDeeplink(intent)
        handlePayload(intent)


        Log.d("CLICK", "HELLo2")

        button.setOnClickListener {
            q++
                YandexMetrica.reportEvent("btn_click $q")
                YandexMetrica.reportEvent("btn_click")
                Log.d("CLICK", "QQ $q")

        }



    }


    private fun initMetrika(){
        try {
            val config = YandexMetricaConfig.newConfigBuilder("a43ac8c5-d5c2-4385-a29a-cfde06e650a1")
                .withLogs()
                .build()
            // Initializing the AppMetrica SDK.
            YandexMetrica.activate(applicationContext, config)
            // Automatic tracking of user activity.
            YandexMetrica.enableActivityAutoTracking(application)

            YandexMetrica.reportAppOpen(this);

            YandexMetrica.setStatisticsSending(applicationContext, true);

            Log.d("INITMETRIKA", "Successful")
        }catch (e: Exception){
            Log.d("INITMETRIKA", "$e ERROR")
        }

    }



    private fun handleDeeplink(intent: Intent) {
        val uri = intent.data
        if (uri != null && !TextUtils.isEmpty(uri.host)) {
            qq.append(String.format("\nDeeplink host: %s", uri.host!!))
            YandexMetrica.reportEvent("Open deeplink")
        }
    }

    private fun handlePayload(intent: Intent) {
        val payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD)
        if (!TextUtils.isEmpty(payload)) {
            qq.append(String.format("\nPayload: %s", payload!!))
            YandexMetrica.reportEvent("Handle payload")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        YandexMetrica.reportAppOpen(this)
        YandexMetrica.reportEvent("external_link", "intent")
        handleDeeplink(intent)
        handlePayload(intent)
    }


    override fun onPause() {
        YandexMetrica.pauseSession(this)
        super.onPause()
    }

}
