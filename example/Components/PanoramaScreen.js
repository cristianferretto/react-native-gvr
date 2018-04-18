/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React from 'react'
import { StyleSheet, Text, View, Button } from 'react-native'
import { PanoramaView } from 'react-native-gvr'

export default class PanoramaScreen extends React.Component {
  static navigationOptions = {
    title: 'PanoramaView'
  }

  state = {
    displayMode: 'embedded',
  }

  setDisplayMode = displayMode => {
    this.setState({ displayMode })
  }

  sliderValueChange = value => {
    this.videoView && this.videoView.seekTo(value)
  }

  onContentLoad = event => {
    console.log("Content load", event.nativeEvent)
  }

  onTap = () => {
    if (this.state.displayMode !== 'embedded') {
      this.setState({ paused: !this.state.paused })
    }
  }

  onChangeDisplayMode = event => {
    if (this.state.paused && event.nativeEvent.mode !== 'embedded') {
      this.setState({ paused: false })
    }
    this.setState({ displayMode: event.nativeEvent.mode })
  }

  render () {
    const { paused, displayMode, progress } = this.state
    return (
      <View style={styles.container}>
        <View style={styles.contentContainer}>
          <PanoramaView
            style={styles.content}
            source={{
              uri: 'https://upload.wikimedia.org/wikipedia/commons/1/18/Rheingauer_Dom%2C_Geisenheim%2C_360_Panorama_%28Equirectangular_projection%29.jpg',
              type: 'mono'
            }}
            displayMode={displayMode}
            enableTouchTracking
            enableFullscreenButton={displayMode !== 'embedded'}
            enableCardboardButton={displayMode !== 'embedded'}
            hidesTransitionView
            enableInfoButton={false}
            onContentLoad={this.onContentLoad}
            onTap={this.onTap}
            onChangeDisplayMode={this.onChangeDisplayMode}
          />
          <Button
            onPress={() => { this.setDisplayMode('fullscreen') }}
            title='Fullscreen' />
          <Button
            onPress={() => { this.setDisplayMode('cardboard') }}
            title='VR' />
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF'
  },
  contentContainer: {
    width: '100%',
    paddingHorizontal: 10
  },
  content: {
    width: '100%',
    aspectRatio: 16 / 9,
  }
})
