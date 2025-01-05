[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## What

Kotlin Multiplatform/Compose Multiplatformで地図を表示し、スライダーを使った加速度付きUIで移動するデモプログラムです。 ジャイロのデータを使ってスマホを傾けて左右に回転できます。

## 開発環境

- macOS 15
- JetBrains Fleet
- Xcode 16.2
- Java openjdk 17.0.13
- Kotlin 2.1.0
- Compose Multiplatform 1.7.0

#### 実機確認

- iPhone Xs Max(iOS 18.2)
- Pixel 3a(Android 12)

## デモプログラムについて

#### デモプログラムの機能と利用技術

- 地図の表示（外部ライブラリの採用とKotlinから呼び出し）
  - 地図はOpenStreetMapを使用。iOS/AndroidでMapLibreを使用
  - 初期表示は東京駅付近です
- スライダーによる地図の移動（KMP/CMPによる共通UIの実装、独自UIの実現）
  - 右側のスライダーを上下に動かすことで地図を前後に移動します
    - 移動量に応じて移動速度が変わります
  - 左側のスライダーを左右に動かすことで地図が回転します
    - 移動量に応じて回転速度が変わります
- ジャイロデータによる地図の回転方向の変更（KMP/CMPによる共通UIの実装、iOS/Androudのデバイス固有機能の呼び出し）
  - デバイスの左右傾きに合わせて地図の回転方向が変わります

##### 既知の不具合・課題

- [Android] 横向き（Landscape）で左右傾けたときの回転が逆になる（iOSが正）
- [iOS/Android] スライダーによるスクロールがカクつく・もっさりしてる。指で操作したときのように滑らかでない
  - スライダーやジャイロからの値変更通知 → SDK操作でカメラ位置を都度セットしているのでオーバヘッドが大きいと思われる
- [iOS/Android] Zoom率を上げると前後進行した後、拡大されたままのマップ画像が更新されない時がある
