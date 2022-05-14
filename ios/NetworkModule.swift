//
//  NetworkModule.swift
//  NetworkBridge
//
//  Created by Corina Ferencz on 29.04.2022.
//

import Foundation

@objc(NetworkModule)
class NetworkModule: NSObject {
  
  @objc
  func getBooks(_ resolve: @escaping RCTPromiseResolveBlock,
                rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    
    let url = URL(string: "https://www.googleapis.com/books/v1/volumes?q=harry+potter")
    guard let requestUrl = url else { fatalError() }

    var request = URLRequest(url: requestUrl)
    request.httpMethod = "GET"
    
    let task = URLSession.shared.dataTask(with: request) { (data, response, error) in

        if let error = error {
            print("Error took place \(error)")
            return
        }

        // Read HTTP Response Status code
        if let response = response as? HTTPURLResponse {
            print("Response HTTP Status code: \(response.statusCode)")
        }

        do {
            if let file = url {
                let data = try Data(contentsOf: file)
                let json = try JSONSerialization.jsonObject(with: data, options: [])
              
                if let object = json as? [String: Any] {
                    if let books = object["items"] as? NSArray {
                    let volumeInfo = books.value(forKey: "volumeInfo") as? NSArray;
                    let titles = volumeInfo?.value(forKey: "title") as? NSArray;

                    if (titles?.count != 0) {
                      resolve(titles);
                    } else {
                      reject("event_failure", "no data returned", nil);
                    }
                }
                    } else {
                        print("JSON is invalid")
                    }
                } else {
                    print("no file")
                }

            } catch {
                print(error.localizedDescription)
            }
    }
    task.resume()
}
  
  @objc
      static func requiresMainQueueSetup() -> Bool {
          return true
  }
}
