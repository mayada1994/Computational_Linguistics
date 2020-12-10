package com.mayada1994.zipfslaw.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mayada1994.zipfslaw.R
import com.mayada1994.zipfslaw.entities.TextFile
import com.mayada1994.zipfslaw.fragments.TableFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ZipfsLaw)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.monte_carlo)
        }

        btnSelectFile.setOnClickListener { checkPermission() }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog()

                } else {
                    ActivityCompat
                        .requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            EXTERNAL_STORAGE_PERMISSIONS_REQUEST
                        )
                }
                return false
            } else {
                showFileChooser()
                return true
            }

        } else {
            showFileChooser()
            return true
        }
    }

    private fun showDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Необхідний дозвіл")
        alertBuilder.setMessage("Необхідний дозвіл на доступ до файлової системи")
        alertBuilder.setPositiveButton(
            android.R.string.yes
        ) { _, _ ->
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_PERMISSIONS_REQUEST
            )
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            EXTERNAL_STORAGE_PERMISSIONS_REQUEST -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFileChooser()
            }
            else -> super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.type = "*/*"

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

        startActivityForResult(intent, SELECT_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_FILE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let {
                readText(it, getFileName(it))
            }
        }
    }

    private fun readText(uri: Uri, filename: String?) {
        val text = StringBuilder()
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val br = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                text.append(line)
                text.append('\n')
            }
            br.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Обраний файл не є текстовим", Toast.LENGTH_SHORT).show()
        }

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.container, TableFragment.newInstance(TextFile(filename, text.toString())))
            .commit()
    }

    private fun getFileName(uri: Uri): String? {
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.count <= 0) {
                cursor.close()
                return null
            }
            cursor.moveToFirst()
            val fileName: String =
                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            cursor.close()
            return fileName
        }
        return null
    }

    companion object {
        private const val EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 123
        private const val SELECT_FILE_REQUEST = 234
    }

}