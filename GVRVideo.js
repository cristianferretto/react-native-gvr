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
    this.rctView && VrVideoManager && VrVideoManager.seekTo(findNodeHandle(this.rctView), position)
  }

  render () {
    const source = resolveAssetSource(this.props.source) || {}
    let uri = source.uri || ''
    if (uri && uri.match(/^\//)) {
      uri = `file://${uri}`
    }

    const isNetwork = !!(uri && uri.match(/^https?:/))
    const isAsset = !!(uri && uri.match(/^(assets-library|content|ms-appx|ms-appdata):/))

    return <RCTViedoView
      {...this.props}
      ref={this.setRef}
      src={{
        uri,
        isNetwork,
        isAsset,
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
    }),
    // Opaque type returned by require('./video.mp4')
    PropTypes.number
  ]),
  paused: PropTypes.bool,
  videoType: PropTypes.string,
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
