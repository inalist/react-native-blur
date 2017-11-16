import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';
import PropTypes from 'prop-types';

class VibrancyView extends Component {
  render() {
    return (
      <NativeVibrancyView
        {...this.props}
        style={[{
          backgroundColor: 'transparent',
        }, this.props.style
        ]}
      />
    );
  }
}

VibrancyView.propTypes = {
  blurType: PropTypes.string,
};

const NativeVibrancyView = requireNativeComponent('VibrancyView', VibrancyView);

module.exports = VibrancyView;
