
#if __has_include("iSoma.h")
#import "iSoma.h"
#else
#import <iSoma/iSoma.h>
#endif

@class RCTEventDispatcher;

@interface RNSmaatoBannerView : UIView <SOMAAdViewDelegate>

@property (nonatomic, copy) NSString *publisherID;
@property (nonatomic, copy) NSString *adSpaceID;
@property (nonatomic, copy) NSString *adDimension;


- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher NS_DESIGNATED_INITIALIZER;
- (SOMAAdDimension)getAdSizeFromString:(NSString *)bannerSize;
- (void)loadBanner;

@end
