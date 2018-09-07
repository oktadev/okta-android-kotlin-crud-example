package com.example.karl.oktakotlinretry

import android.app.PendingIntent
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_login.*
import com.okta.appauth.android.OktaAppAuth
import net.openid.appauth.AuthorizationException

class LoginActivity : AppCompatActivity() {

    private var mOktaAuth: OktaAppAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mOktaAuth = OktaAppAuth.getInstance(this)

        setContentView(R.layout.activity_login)

        mOktaAuth!!.init(
                this,
                object : OktaAppAuth.OktaAuthListener {
                    override fun onSuccess() {
                        auth_button.visibility = View.VISIBLE
                        auth_message.visibility = View.GONE
                        progress_bar.visibility = View.GONE
                    }

                    override fun onTokenFailure(ex: AuthorizationException) {
                        auth_message.text = ex.toString()
                        progress_bar.visibility = View.GONE
                        auth_button.visibility = View.GONE
                    }
                }
        )

        val button = findViewById(R.id.auth_button) as Button
        button.setOnClickListener { v ->
            val completionIntent = Intent(v.context, MainActivity::class.java)
            val cancelIntent = Intent(v.context, LoginActivity::class.java)

            cancelIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            mOktaAuth!!.login(
                    v.context,
                    PendingIntent.getActivity(v.context, 0, completionIntent, 0),
                    PendingIntent.getActivity(v.context, 0, cancelIntent, 0)
            )
        }
    }
}
