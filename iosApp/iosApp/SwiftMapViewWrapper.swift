import Foundation
import MapLibre
import CoreLocation

@objcMembers
public class SwiftMapViewWrapper: NSObject {

    private let mapView: MLNMapView = MLNMapView(frame: .zero, styleURL: nil)
    private var mapDelegate: MapViewDelegateProxy!

    // タイルの初期設定
    var tileURLTemplate: String?
    var minZoom: Float = 0
    var maxZoom: Float = 25
    var tileSize: Int = 256
    var sourceID: String = "osm-source"
    var layerID: String = "osm-layer"

    public override init() {

        MLNLoggingConfiguration.shared.loggingLevel = .verbose

        super.init()

        self.mapDelegate = MapViewDelegateProxy(wrapper: self)
        mapView.delegate = mapDelegate

        // ツール表示
        mapView.showsUserLocation = true

    }

    // タイルや初期カメラ設定を受け取ってセットアップ
    public func setupMapStyle(
        tileURLTemplate: String,
        minZoom: Float,
        maxZoom: Float,
        tileSize: Int,
        sourceID: String,
        layerID: String,
        initialLatitude: Double,
        initialLongitude: Double,
        initialZoom: Double,
        initialBearing: Double,
        initialPitch: Double
    ) {
        // パラメータを保持
        self.tileURLTemplate = tileURLTemplate
        self.minZoom = minZoom
        self.maxZoom = maxZoom
        self.tileSize = tileSize
        self.sourceID = sourceID
        self.layerID = layerID

        let center = CLLocationCoordinate2D(latitude: initialLatitude, longitude: initialLongitude)
        let altitude = altitudeForZoom(initialZoom)  // ズーム→高度変換

        let camera = MLNMapCamera(
            lookingAtCenter: center,
            altitude: altitude,
            pitch: initialPitch,
            heading: initialBearing
        )

        // 微妙に遅らせる。初期化後のマップ表示と角度などがおかしくなるため
        // TODO: おそらくBad Know How
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.125) {
            self.mapView.setCamera(camera, animated: false)
        }

    }

    public func getMapView() -> MLNMapView {
        return mapView
    }

    // 現在のカメラ位置・ベアリング・ピッチを返す
    public func getCurrentCameraPosition() -> [String: Double] {
        let camera = mapView.camera
        let center = camera.centerCoordinate
        let bearing = camera.heading
        let pitch = camera.pitch

        // ズームを計算するには altitude->zoom 変換が必要
        // ここで逆変換
        let zoom = zoomForAltitude(camera.altitude)

        return [
            "latitude": center.latitude,
            "longitude": center.longitude,
            "bearing": bearing,
            "pitch": pitch,
            "zoom": zoom
        ]
    }

    // 新しいカメラ位置を設定
    public func setCenter(
        latitude: Double,
        longitude: Double,
        zoom: Double,
        heading: Double,
        pitch: Double
    ) {
        let center = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)

        // ズーム→高度変換
        let altitude = altitudeForZoom(zoom)

        // カメラの作成
        let camera = MLNMapCamera(
            lookingAtCenter: center,
            altitude: altitude,
            pitch: pitch,
            heading: heading
        )

        // カメラ設定を一度に適用
        mapView.setCamera(camera, animated: false)
    }

    private func altitudeForZoom(_ zoom: Double) -> Double {
        // Mapbox/MapLibre 公式ドキュメントに近い例
        // zoom=0 -> ~44330km, zoom=20 -> ~42.4m
        return 44330000.0 / pow(2.0, zoom)
    }

    private func zoomForAltitude(_ altitude: Double) -> Double {
        // Mapbox/MapLibre 公式ドキュメントに近い例
        // altitude=44330000 at zoom=0 => log2(44330000 / altitude)
        let ratio = 44330000.0 / altitude
        return log2(ratio)
    }

}

// bridging header に <MLNMapViewDelegate> が出力されないようにするため、privateのProxyとして扱う
private final class MapViewDelegateProxy: NSObject, MLNMapViewDelegate {

    weak var wrapper: SwiftMapViewWrapper?

    init(wrapper: SwiftMapViewWrapper? = nil) {
        self.wrapper = wrapper
    }


    func mapView(_ mapView: MLNMapView, didFinishLoading style: MLNStyle) {
        guard let wrapper else {
            print("wrapper が存在しません。")
            return
        }

        guard let tileURLTemplate = wrapper.tileURLTemplate else {
            print("タイルURLテンプレートが設定されていません。")
            return
        }

        let rasterSource = MLNRasterTileSource(
            identifier: wrapper.sourceID,
            tileURLTemplates: [tileURLTemplate],
            options: [
                .minimumZoomLevel: NSNumber(value: wrapper.minZoom),
                .maximumZoomLevel: NSNumber(value: wrapper.maxZoom),
                .tileSize: NSNumber(value: wrapper.tileSize)
            ]
        )
        style.addSource(rasterSource)

        let rasterLayer = MLNRasterStyleLayer(identifier: wrapper.layerID, source: rasterSource)
        style.addLayer(rasterLayer)

        print("スタイル読み込み完了。ソースとレイヤーを追加しました。")

    }
}

