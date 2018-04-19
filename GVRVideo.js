/*
 * @Author: tiero
 * @Date: 2017-01-05 17:39:15
 * @Last Modified by: tiero
 * @Last Modified time: 2017-01-05 17:40:04
 */
import React from 'react'
import PropTypes from 'prop-types'
import { requireNativeComponent, ViewPropTypes, NativeModules, UIManager, findNodeHandle, Platform } from 'react-native'
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource'

const IS_IOS = Platform.OS === 'ios'
const VrVideoModule = IS_IOS ? NativeModules['VrVideoManager'] : NativeModules['VrVideoModule']

class VideoView extends React.Component {
  setRef = view => {
    this.rctView = view
  }

  seekTo (position) {
    if (IS_IOS) {
      VrVideoModule.seekTo(findNodeHandle(this.rctView), position)
    } else {
     UIManager.dispatchViewManagerCommand(
       findNodeHandle(this.rctView),
       UIManager.VrVideo.Commands.seekTo,
       [position]
     )
    }
  }

  getDuration () {
    return VrVideoModule.getDuration(findNodeHandle(this.rctView))
  }

  getPlayableDuration () {
    if (IS_IOS) {
      return VrVideoModule.getPlayableDuration(findNodeHandle(this.rctView))
    } else {
      return new Promise((resolve, reject) => {
        reject('getPlayableDuration not supported on Android')
      })
    }
  }

  getFormat(uri) {
    if (uri.includes('.m3u8')) {
      return 'hls'
    } else if (uri.includes('.mpeg')) {
      return 'mpeg'
    } else if (uri.includes('.mp4')) {
      return 'mp4'
    }
    return 'mp4'
  }

  render () {
    const { source } = this.props
    return <RCTViedoView
      {...this.props}
      ref={this.setRef}
      src={{
        uri: source.uri,
        type: source.type || '',
        format: this.getFormat(source.uri)
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
