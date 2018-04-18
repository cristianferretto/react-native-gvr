//
//  PanoramaManager.m
//  panorama
//
//  Created by Marco Argentieri on 28/12/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "VrVideoManager.h"

#import "VrVideoView.h"

@implementation VrVideoManager

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (UIView *)view
{
  return [VrVideoView new];
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
RCT_EXPORT_VIEW_PROPERTY(paused, BOOL);
RCT_EXPORT_VIEW_PROPERTY(volume, float);

RCT_EXPORT_VIEW_PROPERTY(onContentLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTap, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onUpdatePosition, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeDisplayMode, RCTBubblingEventBlock)

RCT_EXPORT_METHOD(seekTo:(nonnull NSNumber *)reactTag position:(float)position)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
        VrVideoView *videoView = (VrVideoView*)viewRegistry[reactTag];
        if ([videoView isKindOfClass:[VrVideoView class]]) {
            [videoView seekTo:position];
        } else {
            RCTLogError(@"Cannot setProgress: %@ (tag #%@) is not VrVideoView", videoView, reactTag);
        }
    }];
}

@end
