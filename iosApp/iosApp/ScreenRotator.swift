//
//  ScreenRotator.swift
//  iosApp
//
//  Created by tetsuyak on 2025/01/03.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit

@objcMembers
class ScreenRotator: NSObject {
    
    public static func rotateScreen(isPortrait: Bool) {

        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene else {
            print("No UIWindowScene found")
            return
        }

        let orientationMask: UIInterfaceOrientationMask = isPortrait ? .portrait : .landscapeRight
        if #available(iOS 16.0, *) {
            let geometryPreferences = UIWindowScene.GeometryPreferences.iOS(interfaceOrientations: orientationMask)
            windowScene.requestGeometryUpdate(geometryPreferences) { error in
                NSLog("Failed to update geometry: \(error.localizedDescription)")
            }
        } else {
            // Fallback on earlier versions
        }
        
    }
}
