
#import "RNSmaato.h"
#import "RNSmaatoBannerView.h"


@implementation RNSmaato

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (UIView *)view
{
  return [[RNSmaatoBannerView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

- (NSArray *) customDirectEventTypes
{
  return @[
           @"onDidFailToReceiveAdWithError",
           @"onAdViewDidLoadAd",
           @"onSizeChange"
           ];
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}


RCT_EXPORT_VIEW_PROPERTY(publisherID, NSString);
RCT_EXPORT_VIEW_PROPERTY(adSpaceID, NSString);
RCT_EXPORT_VIEW_PROPERTY(adDimension, NSString);

@end
