package com.example.chit_chat.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

fun getQrCodeBitmap(id: String, context: Context): Bitmap {
    val size = dpToPx(dp = 256f, context)
    val hints = hashMapOf<EncodeHintType, Int>().also {
        it[EncodeHintType.MARGIN] = 1
    }
    val bits = QRCodeWriter().encode(id, BarcodeFormat.QR_CODE, size, size, hints)
    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
    }
}

private val processor = object : Detector.Processor<Barcode> {

    override fun receiveDetections(detections: Detector.Detections<Barcode>) {
        detections.apply {
            if (detectedItems.isNotEmpty()) {
                val qr = detectedItems.valueAt(0)
                qr.let {
                    Log.d("Chit Chat Debug", it.rawValue) // вызвать обработчик расшифрованного QR здесь
                }
            }
        }
    }

    override fun release() {}
}

