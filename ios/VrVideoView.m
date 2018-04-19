//
//  PanoramaView.m
//  panorama
//
//  Created by Marco Argentieri on 28/12/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "VrVideoView.h"
#import "GVRWidgetView.h"
#import <React/RCTConvert.h>

@implementation RCTConvert(GVRWidgetDisplayMode)

RCT_ENUM_CONVERTER(GVRWidgetDisplayMode, (@{
                                            @"fullscreen": @(kGVRWidgetDisplayModeFullscreen),
                                            @"embedded": @(kGVRWidgetDisplayModeEmbedded),
                                            @"cardboard": @(kGVRWidgetDisplayModeFullscreenVR),
                                            }), NSNotFound, integerValue)

@end



@implementation VrVideoView {
  GVRVideoView *_videoView;
  GVRVideoType __videoType;
  BOOL _isPaused;
}


- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    _videoView = [[GVRVideoView alloc] init];
    _videoView.delegate = self;
    _isPaused = true;
    [self addSubview:_videoView];
  
    return self;
}

- (void)layoutSubviews
{
    float rootViewWidth = self.frame.size.width;
    float rootViewHeight = self.frame.size.height;
    [_videoView setFrame:CGRectMake(0, 0, rootViewWidth, rootViewHeight)];
}

-(void)setVolume:(float)volume
{
    _videoView.volume = volume;
}

-(void)setSrc:(NSDictionary *)src
{
    NSString *uri = [src objectForKey:@"uri"];
    NSURL *url = [NSURL URLWithString:uri];
    NSString *strType = [src objectForKey:@"type"];

    GVRVideoType videoType = kGVRVideoTypeMono;
    if ([strType isEqualToString:@"stereo"]) {
        videoType = kGVRVideoTypeStereoOverUnder;
    }

    [_videoView loadFromUrl:url ofType:videoType];
    [_videoView pause];
}

- (void)setPaused:(BOOL)paused
{
    _isPaused = paused;
    if (_videoView != nil) {
        if (paused) {
            [_videoView pause];
        } else {
            [_videoView play];
        }
    }
}

- (void)setDisplayMode:(NSString *)displayMode
{
    //Display mode default Embedded
    _videoView.displayMode = [RCTConvert GVRWidgetDisplayMode:displayMode];
}


- (void)setEnableFullscreenButton:(BOOL)enableFullscreenButton
{
    _videoView.enableFullscreenButton = enableFullscreenButton;
}

-(void)setEnableInfoButton:(BOOL)enableInfoButton
{
    _videoView.enableInfoButton = enableInfoButton;
}

-(void)setEnableTouchTracking:(BOOL)enableTouchTracking
{
    _videoView.enableTouchTracking = enableTouchTracking;
}

-(void)setHidesTransitionView:(BOOL)hidesTransitionView
{
    _videoView.hidesTransitionView = hidesTransitionView;
}

-(void)setEnableCardboardButton:(BOOL)enableCardboardButton
{
    _videoView.enableCardboardButton = enableCardboardButton;
}

-(void)seekTo:(float)progress
{
    if (_videoView != nil) {
        float position = progress * _videoView.duration;
        [_videoView seekTo:position];
    }
}

-(NSTimeInterval)getDuration
{
    if (_videoView != nil) {
        return _videoView.duration;
    }
    return 0;
}

-(NSTimeInterval)getPlayableDuration
{
    if (_videoView != nil) {
        return _videoView.playableDuration;
    }
    return 0;
}


#pragma mark - GVRVideoViewDelegate

- (void)widgetViewDidTap:(GVRWidgetView *)widgetView {
    if (self.onTap != nil) {
        self.onTap(@{});
    }
}

- (void)widgetView:(GVRWidgetView *)widgetView didLoadContent:(id)content {
    if (self.onContentLoad != nil) {
        self.onContentLoad(@{});
    }
    if (!_isPaused) {
        [_videoView play];
    }
}

- (void)widgetView:(GVRWidgetView *)widgetView
didFailToLoadContent:(id)content
  withErrorMessage:(NSString *)errorMessage {
    if (self.onContentLoad != nil) {
        self.onContentLoad(@{@"error": errorMessage});
    }
}

- (void)widgetView:(GVRWidgetView *)widgetView didChangeDisplayMode:(GVRWidgetDisplayMode)displayMode {
    if (self.onChangeDisplayMode != nil) {
        NSString *mode = nil;
        switch (displayMode) {
            case kGVRWidgetDisplayModeEmbedded:
                mode = @"embedded";
                break;
            case kGVRWidgetDisplayModeFullscreen:
                mode = @"fullscreen";
                break;
            case kGVRWidgetDisplayModeFullscreenVR:
                mode = @"cardboard";
                break;
            default:
                break;
        }
        if (mode != nil) {
            self.onChangeDisplayMode(@{@"mode": mode});
        }
    }
}

- (void)videoView:(GVRVideoView*)videoView didUpdatePosition:(NSTimeInterval)position {
    if (self.onUpdatePosition != nil) {
        float progress = position / _videoView.duration;
        self.onUpdatePosition(@{
                                @"position": [NSNumber numberWithDouble:progress],
                                @"duration": [NSNumber numberWithDouble:_videoView.duration]
                                });
    }
}


@end
