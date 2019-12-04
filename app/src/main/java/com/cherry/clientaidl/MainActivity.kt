package com.cherry.clientaidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import com.cherry.messagerservice.IMyAidlInterface
import kotlinx.android.synthetic.main.activity_main.*

/**
 * AIDL
 * Client simple case.
 */
class MainActivity : AppCompatActivity() {

    // the IBinder
    private var mService: IMyAidlInterface? = null

    //ServiceConnection
    private val serviceConnection = object : ServiceConnection {

        // this method invoke only crash!!!
        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            connectButton.text = "Start Connect.."
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = IMyAidlInterface.Stub.asInterface(service)
            connectButton.text = "DisConnect.."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectButton.text = if (mService != null) "DisConnect.." else "Start Connect.."
        connectButton.setOnClickListener {
            if (mService != null) { //disconnect
                unbindService(serviceConnection)
                mService = null
                connectButton.text = "Start Connect.."
            } else { // connect the server service
                Intent(AIDL_SERVICE_ACTION).also { intent ->
                    intent.setPackage(AIDL_SERVICE_PACKAGE_NAME)
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
                }

            }
        }

        readButton.setOnClickListener {
            if (mService != null)
                Toast.makeText(
                    this,
                    "read from server: ${mService?.read()}",
                    Toast.LENGTH_SHORT
                ).show()
        }
        writeButton.setOnClickListener {
            if (mService != null)
                mService?.write(5201314)
        }
    }
}
