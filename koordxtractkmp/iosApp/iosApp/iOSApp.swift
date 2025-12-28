import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            ContentView().onOpenURL { url in
                appDelegate.handleURL(url)
            }
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate, ObservableObject {
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        handleURL(url)
        return true
    }
    
    func handleURL(_ url: URL) {
        print("DEBUG: AppDelegate - handleURL called with: \(url)")
        if url.scheme == "koordxtract",
           let components = URLComponents(string: url.absoluteString),
           let queryItem = components.queryItems?.first(where: { $0.name == "link" }),
           let linkURL = queryItem.value.flatMap(URL.init) {
            print("DEBUG: AppDelegate - Processing koordxtract link: \(linkURL)")
            
            MainViewControllerKt.handlingUrlFromiOS(url: linkURL.absoluteString)
            return
        }
    }
}
