/*
 * @Author: tiero
 * @Date: 2017-01-05 17:39:05
 * @Last Modified by: tiero
 * @Last Modified time: 2017-01-05 17:40:19
 */
import React from 'react'
import PropTypes from 'prop-types'
import { requireNativeComponent, ViewPropTypes } from 'react-native'

class PanoramaView extends React.Component {
  render () {
    const { source } = this.props
    return <RCTPanoramaView
      {...this.props}
      src={{
        uri: source.uri,
        type: source.type || ''
      }} />
  }
}

PanoramaView.propTypes = {
  ...ViewPropTypes,
  src: PropTypes.object,
  source: PropTypes.oneOfType([
    PropTypes.shape({
      uri: PropTypes.string,
      type: PropTypes.string
    })
  ]),
  displayMode: PropTypes.string,
  enableFullscreenButton: PropTypes.bool,
  enableCardboardButton: PropTypes.bool,
  enableInfoButton: PropTypes.bool,
  enableTouchTracking: PropTypes.bool,
  hidesTransitionView: PropTypes.bool,
  onContentLoad: PropTypes.func,
  onTap: PropTypes.func,
  onChangeDisplayMode: PropTypes.func
}

// requireNativeComponent automatically resolves this to "PanoramaManager"
var RCTPanoramaView = requireNativeComponent('Panorama', PanoramaView)
export default PanoramaView
