//
//  NetworkModule.m
//  NetworkBridge
//
//  Created by Corina Ferencz on 05.05.2022.
//

#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(NetworkModule, NSObject)

RCT_EXTERN_METHOD(getBooks: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end



