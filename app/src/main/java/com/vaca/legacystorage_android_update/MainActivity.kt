package com.vaca.legacystorage_android_update

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
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


        Log.e("gaga",rootPath)



        val nn = file.readBytes()

        FileUtils.copyFileToDownloads(MainApplication.myApplication,file)
        Log.e("gaga",nn.size.toString())


        createDocument()


    }


    private fun createFile(title: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_TITLE, title)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("/Documents"))
        }
        createInvoiceActivityResultLauncher.launch(intent)
    }
    lateinit var invoice_html: String
    lateinit var createInvoiceActivityResultLauncher: ActivityResultLauncher<Intent>

    private fun createInvoice(uri: Uri) {
        try {
            val pfd = contentResolver.openFileDescriptor(uri, "w")
            if (pfd != null) {
                val fileOutputStream = FileOutputStream(pfd.fileDescriptor)
                fileOutputStream.write(invoice_html.toByteArray())
                fileOutputStream.close()
                pfd.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun createDocument(){
        invoice_html = "<h1>Just for testing received...</h1>"
        createInvoiceActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                var uri: Uri? = null
                if (result.data != null) {
                    uri = result.data!!.data
                    createInvoice(uri!!)
                }
            }
        }
        createFile("hehe")
    }


}