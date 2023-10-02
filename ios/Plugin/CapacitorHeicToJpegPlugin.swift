import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(CapacitorHeicToJpegPlugin)
public class CapacitorHeicToJpegPlugin: CAPPlugin {
    private let implementation = CapacitorHeicToJpeg()

    @objc func convertToJpeg(_ call: CAPPluginCall) {
            guard let path = call.getString("path") else {
                call.reject("Must provide a path")
                return
            }
            
            if let image = UIImage(contentsOfFile: path), let jpegData = image.jpegData(compressionQuality: 0.8) {
                let jpegPath = path.replacingOccurrences(of: ".heic", with: ".jpg")
                try? jpegData.write(to: URL(fileURLWithPath: jpegPath))
                call.resolve(["path": jpegPath])
            } else {
                call.reject("Conversion failed")
            }
        }
}
