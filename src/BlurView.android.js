import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { View, requireNativeComponent } from 'react-native';

class BlurView extends Component {
  render() {
    return (
      <NativeBlurView
        {...this.props}
        style={[{
          backgroundColor: 'transparent',
        }, this.props.style
        ]}
      />
    );
  }
}

BlurView.propTypes = {
  ...View.propTypes,
  blurRadius: PropTypes.number,
  overlayColor: PropTypes.string,
  downsampleFactor: PropTypes.number,
  viewRef: PropTypes.number,
  blurSpeed: PropTypes.number
};

const NativeBlurView = requireNativeComponent('BlurView', BlurView);

module.exports = BlurView;
