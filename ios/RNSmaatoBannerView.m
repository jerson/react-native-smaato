#import "RNSmaatoBannerView.h"
#import "UIView+React.h"

@implementation RNSmaatoBannerView {
  SOMAAdView  *_bannerView;
  RCTEventDispatcher *_eventDispatcher;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher
{
  if ((self = [super initWithFrame:CGRectZero])) {
    _eventDispatcher = eventDispatcher;
  }
  return self;
}

RCT_NOT_IMPLEMENTED(- (instancetype)initWithFrame:(CGRect)frame)
RCT_NOT_IMPLEMENTED(- (instancetype)initWithCoder:coder)

- (void)insertReactSubview:(UIView *)view atIndex:(NSInteger)atIndex
{
  RCTLogError(@"AdMob Banner cannot have any subviews");
  return;
}

- (void)removeReactSubview:(UIView *)subview
{
  RCTLogError(@"AdMob Banner cannot have any subviews");
  return;
}

- (SOMAAdDimension)getAdSizeFromString:(NSString *)bannerSize
{
  
  if ([bannerSize isEqualToString:@"mediumrectangle"]) {
    return SOMAAdDimensionMedRect;
  } else if ([bannerSize isEqualToString:@"skyscraper"]) {
    return SOMAAdDimensionSky;
  } else if ([bannerSize isEqualToString:@"leaderboard"]) {
    return SOMAAdDimensionLeader;
  } else {
    return SOMAAdDimensionDefault;
  }
}
- (CGRect)getCGRectFromDimension:(SOMAAdDimension)dimension
{
  
  
  switch (dimension) {
    case SOMAAdDimensionMedRect:
      return CGRectMake(
                        self.bounds.origin.x,
                        self.bounds.origin.x,
                        300,
                        250);
    case SOMAAdDimensionSky:
      return CGRectMake(
                        self.bounds.origin.x,
                        self.bounds.origin.x,
                        120,
                        600);
      
    case SOMAAdDimensionDefault:
    default:
      return CGRectMake(
                        self.bounds.origin.x,
                        self.bounds.origin.x,
                        320,
                        50);
  }
  
}

-(void)loadBanner {
  NSLog(@"loadBanner");
  if (_adSpaceID && _adDimension && _publisherID) {
    SOMAAdDimension size = [self getAdSizeFromString:_adDimension];
    _bannerView = [SOMAAdView new];
    _bannerView.frame = [self getCGRectFromDimension:size];

    if(!CGRectEqualToRect(self.bounds, _bannerView.bounds)) {
      [_eventDispatcher
        sendInputEventWithName:@"onSizeChange"
        body:@{
               @"target": self.reactTag,
               @"width": [NSNumber numberWithFloat: _bannerView.bounds.size.width],
               @"height": [NSNumber numberWithFloat: _bannerView.bounds.size.height]
               }];
    }
    _bannerView.delegate = self;
    
    _bannerView.adSettings.publisherId = [_publisherID integerValue];
    _bannerView.adSettings.adSpaceId = [_adSpaceID integerValue];
    _bannerView.adSettings.dimension = size;

    _bannerView.rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
    
    NSLog(@"loadBanner _bannerView load");
    [_bannerView load];
  }
}

- (void)setAdDimension:(NSString *)adDimension
{
  if(![adDimension isEqual:_adDimension]) {
    _adDimension = adDimension;
    if (_bannerView) {
      [_bannerView removeFromSuperview];
    }
    [self loadBanner];
  }
}


- (void)setAdSpaceID:(NSString *)adSpaceID
{
  if(![adSpaceID isEqual:_adSpaceID]) {
    _adSpaceID = adSpaceID;
    if (_bannerView) {
      [_bannerView removeFromSuperview];
    }
    
    [self loadBanner];
  }
}

- (void)setPublisherID:(NSString *)publisherID
{
  if(![publisherID isEqual:_publisherID]) {
    _publisherID = publisherID;
    if (_bannerView) {
      [_bannerView removeFromSuperview];
    }

    [self loadBanner];
  }
}

-(void)layoutSubviews
{
  [super layoutSubviews ];

  _bannerView.frame = CGRectMake(
    self.bounds.origin.x,
    self.bounds.origin.x,
    _bannerView.frame.size.width,
    _bannerView.frame.size.height);
  [self addSubview:_bannerView];
}

- (void)removeFromSuperview
{
  _eventDispatcher = nil;
  [super removeFromSuperview];
}


-(void)somaAdView:(SOMAAdView *)adview didFailToReceiveAdWithError:(NSError *)error{
  NSLog(@"loadBanner - %@",error);
  [_eventDispatcher sendInputEventWithName:@"onDidFailToReceiveAdWithError" body:@{ @"target": self.reactTag, @"error": [error localizedDescription] }];
  
}

- (void)somaAdViewDidLoadAd:(SOMAAdView *)adview{
  [adview show];

  [_eventDispatcher sendInputEventWithName:@"onAdViewDidLoadAd" body:@{ @"target": self.reactTag }];
}


- (void)somaAdViewWillHide:(SOMAAdView *)adview{
  //[_eventDispatcher sendInputEventWithName:@"onAdViewDidReceiveAd" body:@{ @"target": self.reactTag }];
}

- (void)somaAdViewWillLoadAd:(SOMAAdView *)adview{
 // [_eventDispatcher sendInputEventWithName:@"onAdViewDidReceiveAd" body:@{ @"target": self.reactTag }];
}
- (void)somaAdViewDidExitFullscreen:(SOMAAdView *)adview{
  //[_eventDispatcher sendInputEventWithName:@"onAdViewDidReceiveAd" body:@{ @"target": self.reactTag }];
}
- (void)somaAdViewWillEnterFullscreen:(SOMAAdView *)adview{
  //[_eventDispatcher sendInputEventWithName:@"onAdViewDidReceiveAd" body:@{ @"target": self.reactTag }];
}
- (void)somaAdViewAutoRedrectionDetected:(SOMAAdView *)adview{
 // [_eventDispatcher sendInputEventWithName:@"onAdViewDidReceiveAd" body:@{ @"target": self.reactTag }];
}
@end