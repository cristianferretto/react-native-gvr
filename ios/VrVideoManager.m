//
//  PanoramaManager.m
//  panorama
//
//  Created by Marco Argentieri on 28/12/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "VrVideoManager.h"

#import "VrVideoView.h"
#import <UIKit/UIKit.h>

@implementation VrVideoManager

RCT_EXPORT_MODULE();

- (UIView *)view
{
  return [VrVideoView new];
}

RCT_EXPORT_VIEW_PROPERTY(enableFullscreenButton, BOOL);
RCT_EXPORT_VIEW_PROPERTY(enableCardboardButton, BOOL);
RCT_EXPORT_VIEW_PROPERTY(enableTouchTracking, BOOL);
RCT_EXPORT_VIEW_PROPERTY(hidesTransitionView, BOOL);
RCT_EXPORT_VIEW_PROPERTY(enableInfoButton, BOOL);
RCT_EXPORT_VIEW_PROPERTY(displayMode, NSString);
RCT_EXPORT_VIEW_PROPERTY(src, NSDictionary);
RCT_EXPORT_VIEW_PROPERTY(volume, float)

RCT_EXPORT_VIEW_PROPERTY(onContentLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTap, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onUpdatePosition, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeDisplayMode, RCTBubblingEventBlock)


@end
