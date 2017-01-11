#import "BlurView.h"
#import "BlurAmount.m"


@implementation BlurView {
  UIVisualEffectView *_visualEffectView;
  UIBlurEffect * blurEffect;
  BOOL enabled;
}

- (void)setBlurEnabled:(BOOL)blurEnabled
{
    if (blurEnabled) {
        dispatch_async(dispatch_get_main_queue(), ^{
            if (_visualEffectView) {
                [_visualEffectView removeFromSuperview];
            }
            _visualEffectView = [[UIVisualEffectView alloc] initWithEffect:blurEffect];
            _visualEffectView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
            _visualEffectView.frame = self.bounds;
            _visualEffectView.alpha = 0;
            [self.superview.superview insertSubview:_visualEffectView atIndex:self.superview.subviews.count];
            [UIView animateWithDuration:0.5 animations:^{
                _visualEffectView.alpha = 1.0;
            }];
        });
    } else {
        if (enabled && _visualEffectView) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [UIView animateWithDuration:0.5 animations:^{
                    _visualEffectView.alpha = 0;
                } completion:^(BOOL finished) {
                    [_visualEffectView removeFromSuperview];
                    _visualEffectView = nil;
                }];
            });
        }
    }
    
    enabled = blurEnabled;
}

- (void)setBlurType:(NSString *)blurType
{
  self.clipsToBounds = true;
  if ([blurType isEqual: @"xlight"]) {
    blurEffect = [BlurAmount effectWithStyle:UIBlurEffectStyleExtraLight];
  } else if ([blurType isEqual: @"light"]) {
    blurEffect = [BlurAmount effectWithStyle:UIBlurEffectStyleLight];
  } else if ([blurType isEqual: @"dark"]) {
    blurEffect = [BlurAmount effectWithStyle:UIBlurEffectStyleDark];
  } else {
    blurEffect = [BlurAmount effectWithStyle:UIBlurEffectStyleDark];
  }
}

- (void)setBlurAmount:(NSNumber *)blurAmount
{
    [BlurAmount updateBlurAmount:blurAmount];
}

@end
