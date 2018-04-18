//
//  PanoramaManager.m
//  panorama
//
//  Created by Marco Argentieri on 28/12/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "PanoramaManager.h"
#import "PanoramaView.h"
#import <UIKit/UIKit.h>

@implementation PanoramaManager

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (UIView *)view {
    PanoramaView *view = [PanoramaView new];
    view.bridge = self.bridge;
    return view;
}

- (dispatch_queue_t)methodQueue {
    return _bridge.uiManager.methodQueue;
}


RCT_EXPORT_VIEW_PROPERTY(enableFullscreenButton, BOOL);
RCT_EXPORT_VIEW_PROPERTY(enableCardboardButton, BOOL);
RCT_EXPORT_VIEW_PROPERTY(enableTouchTracking, BOOL);
RCT_EXPORT_VIEW_PROPERTY(hidesTransitionView, BOOL);
RCT_EXPORT_VIEW_PROPERTY(enableInfoButton, BOOL);
RCT_EXPORT_VIEW_PROPERTY(displayMode, NSString);
RCT_EXPORT_VIEW_PROPERTY(src, NSDictionary);

RCT_EXPORT_VIEW_PROPERTY(onContentLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTap, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeDisplayMode, RCTBubblingEventBlock)

@end
