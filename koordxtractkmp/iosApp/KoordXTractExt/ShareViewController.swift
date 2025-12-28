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
    
    private let scheme = "koordxtract"

    override func viewDidLoad() {
        super.viewDidLoad()
        guard let extensionItems = extensionContext?.inputItems as? [NSExtensionItem],
              let attachments = extensionItems.compactMap({ $0.attachments }).flatMap({ $0 }) as? [NSItemProvider],
              let itemProvider = attachments.first(where: { $0.hasItemConformingToTypeIdentifier(UTType.url.identifier) }) else {
            extensionContext?.completeRequest(returningItems: [], completionHandler: nil)
            return
        }
        itemProvider.loadItem(forTypeIdentifier: UTType.url.identifier) { [weak self] data, error in
            guard let url = data as? URL else {
                self?.extensionContext?.completeRequest(returningItems: [], completionHandler: nil)
                return
            }
            
            print("[KoordXTract Extension] - Shared URL \(url)")
            self?.openContainerApp(with: url)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
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
