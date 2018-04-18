import {
  StackNavigator
} from 'react-navigation'
import HomeScreen from './HomeScreen'
import VideoScreen from './VideoScreen'
import PanoramaScreen from './PanoramaScreen'

const App = StackNavigator({
  HomeScreen: { screen: HomeScreen },
  VideoScreen: { screen: VideoScreen },
  PanoramaScreen: { screen: PanoramaScreen }
})

export default App
