//
//  PanoramaView.h
//  panorama
//
//  Created by Marco Argentieri on 28/12/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTView.h>
#import "GVRVideoView.h"

@interface VrVideoView : RCTView <GVRVideoViewDelegate>


@property (nonatomic, assign) float volume;
@property (nonatomic, assign) NSDictionary* src;
@property (nonatomic, assign) BOOL paused;
@property (nonatomic, assign) NSString* displayMode;
@property (nonatomic, assign) BOOL enableFullscreenButton;
@property (nonatomic, assign) BOOL enableCardboardButton;
@property (nonatomic, assign) BOOL enableInfoButton;
@property (nonatomic, assign) BOOL hidesTransitionView;
@property (nonatomic, assign) BOOL enableTouchTracking;

@property (nonatomic, copy) RCTBubblingEventBlock onContentLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onTap;
@property (nonatomic, copy) RCTBubblingEventBlock onFinish;
@property (nonatomic, copy) RCTBubblingEventBlock onUpdatePosition;
@property (nonatomic, copy) RCTBubblingEventBlock onChangeDisplayMode;

- (void)seekTo:(float)progress;
- (NSTimeInterval)getDuration;
- (NSTimeInterval)getPlayableDuration;
@end
