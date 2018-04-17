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
}


- (instancetype)initWithFrame:(CGRect)frame
{
  self = [super initWithFrame:frame];
  _videoView = [[GVRVideoView alloc] init];
  _videoView.delegate = self;
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
  BOOL isNetwork = [src objectForKey:@"isNetwork"];
  
  GVRVideoType videoType = kGVRVideoTypeMono;
  if ([strType isEqualToString:@"stereo"]) {
    videoType = kGVRVideoTypeStereoOverUnder;
  }
  
  //play from remote url
  if ( isNetwork ) {
    
    [_videoView loadFromUrl:url ofType:videoType];
    
  } else { // play from local
    //Local asset: Can be in the bundle or the uri can be an absolute path of a stored video in the application
    
    //Check whether the file loaded from the Bundle,
    NSString *localPath = [[NSBundle mainBundle] pathForResource:uri ofType:@"mp4"];
    if (localPath) {
      //Let's replace the `uri` to the full path'
      uri = localPath;
    }
    url = [NSURL fileURLWithPath:uri];
    // [_videoView loadFromUrl:[[NSURL alloc] initFileURLWithPath:videoPath]
    //                   ofType:videoType];
    [_videoView loadFromUrl:url ofType:videoType];
  }
  
  [_videoView pause];
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


#pragma mark - GVRVideoViewDelegate

- (void)widgetViewDidTap:(GVRWidgetView *)widgetView {
    if (self.onTap != nil) {
        self.onTap(@{});
    }
}

- (void)widgetView:(GVRWidgetView *)widgetView didLoadContent:(id)content {
  RCTLogInfo(@"Finished loading video");
    if (self.onContentLoad != nil) {
        self.onContentLoad(@{});
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
        self.onChangeDisplayMode(@{@"mode": [NSNumber numberWithInt:displayMode]});
    }
}

- (void)videoView:(GVRVideoView*)videoView didUpdatePosition:(NSTimeInterval)position {
    if (self.onUpdatePosition != nil) {
        self.onUpdatePosition(@{@"position": [NSNumber numberWithDouble:position]});
    }
}


@end
