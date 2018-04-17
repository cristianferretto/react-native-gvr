/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React from 'react'
import { StyleSheet, Text, View, Button } from 'react-native'
import { VideoView } from 'react-native-gvr'

export default class App extends React.Component {
  state = {
    paused: false,
    displayMode: 'embedded'
  }

  togglePlay = () => {
    this.setState({ paused: !this.state.paused })
  }

  setDisplayMode = displayMode => {
    this.setState({ displayMode })
  }

  onContentLoad = event => {
    console.log("Content load", event.nativeEvent)
  }

  onTap = () => {
    console.log("On tap")
  }

  onUpdatePosition = event => {
    console.log(event.nativeEvent)
  }

  onChangeDisplayMode = event => {
    console.log(event.nativeEvent)
  }

  render () {
    const { paused, displayMode } = this.state
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <VideoView
          style={{ height: 300, width: 200 }}
          source={{
            uri: 'https://raw.githubusercontent.com/googlevr/gvr-ios-sdk/master/Samples/VideoWidgetDemo/resources/congo.mp4',
            type: 'mono'
          }}
          displayMode={displayMode}
          volume={1}
          paused={paused}
          enableTouchTracking
          enableFullscreenButton={displayMode !== 'embedded'}
          enableCardboardButton={displayMode !== 'embedded'}
          hidesTransitionView
          enableInfoButton={false}
          onContentLoad={this.onContentLoad}
          onTap={this.onTap}
          onUpdatePosition={this.onUpdatePosition}
          onChangeDisplayMode={this.onChangeDisplayMode}
        />
        <Button
          onPress={this.togglePlay}
          title={ paused ? 'Play' : 'Pause'} />
        <Button
          onPress={() => { this.setDisplayMode('fullscreen') }}
          title='Fullscreen' />
        <Button
          onPress={() => { this.setDisplayMode('cardboard') }}
          title='VR' />
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
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5
  }
})
