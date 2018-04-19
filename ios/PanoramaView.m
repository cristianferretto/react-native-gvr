//
//  PanoramaView.m
//  panorama
//
//  Created by Marco Argentieri on 28/12/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "PanoramaView.h"
#import <React/RCTImageLoader.h>



@implementation RCTConvert (GVRWidgetDisplayMode)

RCT_ENUM_CONVERTER(GVRWidgetDisplayMode, (@{
                                            @"fullscreen": @(kGVRWidgetDisplayModeFullscreen),
                                            @"embedded": @(kGVRWidgetDisplayModeEmbedded),
                                            @"cardboard": @(kGVRWidgetDisplayModeFullscreenVR),
                                            }), NSNotFound, integerValue)

@end


@implementation PanoramaView {
    UIImage *_image;
    NSString *__imageType;
    GVRPanoramaView *_panoView;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    _panoView = [[GVRPanoramaView alloc] init];
    _panoView.delegate = self;
    [self addSubview:_panoView];
    
    return self;
}


- (void)layoutSubviews
{
  float rootViewWidth = self.frame.size.width;
  float rootViewHeight = self.frame.size.height;
  [_panoView setFrame:CGRectMake(0, 0, rootViewWidth, rootViewHeight)];
}

-(void)setSrc:(NSDictionary *)src
{
    NSString *uri = [src objectForKey:@"uri"];
    NSURL *url = [NSURL URLWithString:uri];
    NSString *strType = [src objectForKey:@"type"];
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];

    GVRPanoramaImageType imageType = kGVRPanoramaImageTypeMono;
    if ([strType isEqualToString:@"stereo"]) {
        imageType = kGVRPanoramaImageTypeStereoOverUnder;
    }
    
    __weak PanoramaView *weakSelf = self;

    [self.bridge.imageLoader loadImageWithURLRequest:request callback:^(NSError *error, UIImage *networkImage) {
        if (!error) {
            [_panoView loadImage:networkImage ofType:imageType];
        }
        dispatch_async([weakSelf methodQueue], ^{
            [_panoView loadImage:networkImage ofType:imageType];
            
        });
    }];
}

- (void)setDisplayMode:(NSString *)displayMode
{
  //Display mode default Embedded
  _panoView.displayMode = [RCTConvert GVRWidgetDisplayMode:displayMode];
}


- (void)setEnableFullscreenButton:(BOOL)enableFullscreenButton
{
  _panoView.enableFullscreenButton = enableFullscreenButton;
}

-(void)setEnableInfoButton:(BOOL)enableInfoButton
{
  _panoView.enableInfoButton = enableInfoButton;
}

-(void)setEnableTouchTracking:(BOOL)enableTouchTracking
{
  _panoView.enableTouchTracking = enableTouchTracking;
}

-(void)setHidesTransitionView:(BOOL)hidesTransitionView
{
  _panoView.hidesTransitionView = hidesTransitionView;
}

-(void)setEnableCardboardButton:(BOOL)enableCardboardButton
{
  _panoView.enableCardboardButton = enableCardboardButton;
}


#pragma mark - GVRWidgetViewDelegate

- (void)widgetView:(GVRWidgetView *)widgetView didLoadContent:(id)content {
    if (self.onContentLoad != nil) {
        self.onContentLoad(@{});
    }
}
- (void)widgetView:(GVRWidgetView *)widgetView didFailToLoadContent:(id)content withErrorMessage:(NSString *)errorMessage {
    if (self.onContentLoad != nil) {
        self.onContentLoad(@{@"error": errorMessage});
    }
}
- (void)widgetView:(GVRWidgetView *)widgetView didChangeDisplayMode:(GVRWidgetDisplayMode)displayMode  {
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
- (void)widgetViewDidTap {
    if (self.onTap != nil) {
        self.onTap(@{});
    }
}
          
@end
