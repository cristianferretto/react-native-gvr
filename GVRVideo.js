/*
 * @Author: tiero
 * @Date: 2017-01-05 17:39:15
 * @Last Modified by: tiero
 * @Last Modified time: 2017-01-05 17:40:04
 */
import React from 'react'
import PropTypes from 'prop-types'
import { requireNativeComponent, ViewPropTypes, NativeModules, findNodeHandle } from 'react-native'
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource'

const VrVideoManager = NativeModules['VrVideoManager']

class VideoView extends React.Component {
  setRef = view => {
    this.rctView = view
  }

  seekTo (position) {
    VrVideoManager.seekTo(findNodeHandle(this.rctView), position)
  }

  getDuration () {
    return VrVideoManager.getDuration(findNodeHandle(this.rctView))
  }

  getPlayableDuration () {
    return VrVideoManager.getPlayableDuration(findNodeHandle(this.rctView))
  }

  render () {
    const { source } = this.props
    return <RCTViedoView
      {...this.props}
      ref={this.setRef}
      src={{
        uri: source.uri,
        type: source.type || ''
      }}
    />
  }
}

VideoView.propTypes = {
  ...ViewPropTypes,
  src: PropTypes.object,
  source: PropTypes.oneOfType([
    PropTypes.shape({
      uri: PropTypes.string,
      type: PropTypes.string
    })
  ]),
  paused: PropTypes.bool,
  volume: PropTypes.number,
  displayMode: PropTypes.string,
  enableFullscreenButton: PropTypes.bool,
  enableCardboardButton: PropTypes.bool,
  enableInfoButton: PropTypes.bool,
  enableTouchTracking: PropTypes.bool,
  hidesTransitionView: PropTypes.bool,
  onContentLoad: PropTypes.func,
  onTap: PropTypes.func,
  onUpdatePosition: PropTypes.func,
  onChangeDisplayMode: PropTypes.func
}

// requireNativeComponent automatically resolves this to "VideoManager"
var RCTViedoView = requireNativeComponent('VrVideo', VideoView)
export default VideoView
