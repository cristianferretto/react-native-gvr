
# react-native-gvr

## Getting started

`$ npm install react-native-gvr --save`

### Mostly automatic installation

`$ react-native link react-native-gvr`

### Manual installation

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-gvr` and add `RNGvr.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNGvr.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNGvrPackage;` to the imports at the top of the file
  - Add `new RNGvrPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-gvr'
  	project(':react-native-gvr').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-gvr/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-gvr')
  	```

## Setup
#### iOS

- Copy `./node_modules/react-native-gvr/pod_post_install.sh` to ios folder

- Create a **Podfile** in ios folder

```shell
target 'myProject' do
  pod 'GVRSDK'

  # Execute every pod install
  post_install do |installer|
      system(". ./pod_post_install.sh")
  end
end

```

Still in ios folder install pods locally

```shell
pod install
pod update
```

- Open `myProject.xcworkspace` and under `myProject` > `Build Settings` under `Build Options` set **ENABLE BITCODE** to **NO**

### Android
- Open `./android/app/build.gradle` then set `minSdkVersion 19`

## Usage
### VideoView
```javascript
import { VideoView } from 'react-native-gvr'

<VideoView
  style={styles.content}
  source={{
    uri: 'https://raw.githubusercontent.com/googlevr/gvr-ios-sdk/master/Samples/VideoWidgetDemo/resources/congo.mp4',
    type: 'stereo'
  }}
  ref={this.setRef}
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
  onFinish={this.onFinish}
  onUpdatePosition={this.onUpdatePosition}
  onChangeDisplayMode={this.onChangeDisplayMode}
/>
```
### PanoramaView
```javascript
import { PanoramaView } from 'react-native-gvr'

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
```
