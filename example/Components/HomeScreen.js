import React from 'react'
import { View, Button, StyleSheet } from 'react-native'

export default class HomeScreen extends React.Component {
  static navigationOptions = {
    title: 'Welcome!'
  }

  render () {
    const { navigate } = this.props.navigation
    return <View style={styles.container}>
      <View style={styles.buttonContainer}>
        <Button onPress={() => { navigate('VideoScreen') }} title='Video' />
      </View>
      <View style={styles.buttonContainer}>
        <Button onPress={() => { navigate('PanoramaScreen') }} title='Panorama' />
      </View>
    </View>
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center'
  },
  buttonContainer: {
    paddingVertical: 10
  }
})
