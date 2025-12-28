//
//  ShareViewController.swift
//  KoordXTract
//
//  Created by Kirill Olenev on 27/12/25.
//

import UIKit
import Social
import UniformTypeIdentifiers
import os.log

class ShareViewController: SLComposeServiceViewController {
    
    let logger = OSLog(subsystem: "com.shashlikmap.koordxtract", category: "shareextension")


    private let scheme = "koordxtract"

    override func viewDidLoad() {
        super.viewDidLoad()
        print("kiol view did load")
        os_log("viewDidLoad", log: logger, type: .info)
        guard let extensionItems = extensionContext?.inputItems as? [NSExtensionItem],
              let attachments = extensionItems.compactMap({ $0.attachments }).flatMap({ $0 }) as? [NSItemProvider],
              let itemProvider = attachments.first(where: { $0.hasItemConformingToTypeIdentifier(UTType.url.identifier) }) else {
            os_log("[KoordXTract Extension] - URL ItemProvider is missing", log: logger, type: .error)
//            extensionContext?.completeRequest(returningItems: [], completionHandler: nil)
            return
        }
        itemProvider.loadItem(forTypeIdentifier: UTType.url.identifier) { [weak self] data, error in
            guard let url = data as? URL else {
                os_log("[KoordXTract Extension] - Invalid URL", log: self!.logger, type: .error)
                //                self?.extensionContext?.completeRequest(returningItems: [], completionHandler: nil)
                return
            }
            
            print("[KoordXTract Extension] - Shared URL \(url)")
            self?.openContainerApp(with: url)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        os_log("viewDidAppear", log: logger, type: .info)
        extensionContext?.completeRequest(returningItems: [], completionHandler: nil)
    }
    
    private func openContainerApp(with sharedURL: URL) {
        guard var urlComponents = URLComponents(string: scheme + "://share") else { return }
        let queryItem = URLQueryItem(name: "link", value: sharedURL.absoluteString.removingPercentEncoding)
        urlComponents.queryItems = [queryItem]
        guard let url = urlComponents.url else { return }

        var responder: UIResponder? = self
        while responder != nil {
            if let application = responder as? UIApplication {
                print("app open fron extension \(application)")
                application.open(url, options: [:], completionHandler: nil)
                break
            }
            responder = responder?.next
        }
    }
    override func isContentValid() -> Bool {
        return true
    }

    override func didSelectPost() {
        self.extensionContext!.completeRequest(returningItems: [], completionHandler: nil)
    }

    override func configurationItems() -> [Any]! {
        return []
    }
}
