package com.lwwo.tkkmp_sample

/**
 * 共通パラメータを持つ定数置き場
 * Swift側とは直接リンクしにくいので、必要な値は KMP から引数で渡す
 */
object MapConstants {
    // タイルURLテンプレート
    const val TILE_URL_TEMPLATE: String = "https://tile.openstreetmap.org/{z}/{x}/{y}.png"
    const val MIN_ZOOM: Float = 0f
    const val MAX_ZOOM: Float = 25f
    const val TILE_SIZE_PX: Int = 256

    // ソースID/レイヤーID（Android/iOS共通で使いたい場合）
    const val TILE_SOURCE_ID: String = "osm-source"
    const val TILE_LAYER_ID: String = "osm-layer"

    // 初期位置・初期カメラ設定
    const val INITIAL_LAT: Double = 35.6895
    const val INITIAL_LNG: Double = 139.6917
    const val INITIAL_ZOOM: Double = 17.5
    const val INITIAL_BEARING: Double = 0.0
    const val INITIAL_PITCH: Double = 50.0

    // 地図更新のインターバル（ms）
    const val UPDATE_INTERVAL_MS: Long = 30L
}
