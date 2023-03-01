package com.vaca.legacystorage_android_update

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    fun checkPermission(array:Array<String>):Boolean{
        for (i in array){
            if(ContextCompat.checkSelfPermission(
                    this,
                    i
                ) != PackageManager.PERMISSION_GRANTED
            ){
                return false
            }
        }
        return true

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestVoicePermission= registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {

        }
        val pArray=arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH,
            )
        if (!checkPermission(pArray)
        ) {
            requestVoicePermission.launch(pArray )
        }


        var rootPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//有SD卡
            rootPath = Environment.getExternalStorageDirectory().absolutePath + "/";
        } else {
            rootPath = Environment.getDataDirectory().absolutePath + "/";
        }

        val file= File("/storage/emulated/0/1/Emu48.exe")
        val uri = FileProvider.getUriForFile(
            applicationContext,
            "com.adsale.registersite.fileprovider",
            file
        )
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION


        Log.e("gaga",rootPath)


//        val nn=readUri(applicationContext,uri)
        val nn = file.readBytes()

        Log.e("gaga",nn.size.toString())


    }

    @Throws(IOException::class)
    fun readUri(context: Context, uri: Uri): ByteArray{
        val pdf: ParcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")!!
        assert(pdf.statSize <= Int.MAX_VALUE)
        val data = ByteArray(pdf.statSize.toInt())
        val fd = pdf.fileDescriptor
        val fileStream = FileInputStream(fd)
        fileStream.read(data)
        return data
    }
}