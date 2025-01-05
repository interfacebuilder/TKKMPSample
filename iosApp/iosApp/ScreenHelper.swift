//
//  DeviceUtils.swift
//  iosApp
//
//  Created by tetsuyak on 2025/01/01.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import UIKit

@objcMembers
public class ScreenHelper: NSObject {

    public static func getScreenWidth() -> Double {
        return UIScreen.main.bounds.width
    }

    public static func getScreenHeight() -> Double {
        return UIScreen.main.bounds.height
    }

    public static func getSafeAreaInsets() -> [String: CGFloat] {
        let windowScene = UIApplication.shared.connectedScenes.first {
            guard let scene = $0 as? UIWindowScene else { return false }
            return scene.activationState == .foregroundInactive || scene.activationState == .foregroundActive
        } as? UIWindowScene
        
        guard let window = windowScene?.keyWindow else {
            return ["top": 0, "bottom": 0, "left": 0, "right": 0]
        }
        let insets = window.safeAreaInsets
        return [
            "top": insets.top,
            "bottom": insets.bottom,
            "left": insets.left,
            "right": insets.right
        ]
    }

}
