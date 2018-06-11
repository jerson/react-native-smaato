# react-native-smaato

Smaato support for **Android** and **iOS**

## Getting started

`$ npm install react-native-smaato --save`

### Mostly automatic installation

`$ react-native link react-native-smaato`

### Manual installation

#### iOS

1.  In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2.  Go to `node_modules` ➜ `react-native-smaato` and add `RNSmaato.xcodeproj`
3.  In XCode, in the project navigator, select your project. Add `libRNSmaato.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4.  Go to `Podfile` and add `"SmaatoSDK", "~> 9.0.1"` 
5.  Run your project (`Cmd+R`)<

#### Android

1.  Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import me.jerson.mobile.ads.airpush.RNSmaatoPackage;` to the imports at the top of the file
- Add `new RNSmaatoPackage()` to the list returned by the `getPackages()` method

2.  Append the following lines to `android/settings.gradle`:
    ```
    include ':react-native-smaato'
    project(':react-native-smaato').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-smaato/android')
    ```
3.  Insert the following lines inside the dependencies block in `android/app/build.gradle`:
    ```
      compile project(':react-native-smaato')
    ```
4.  Add `SOMAAndroidSDKX.X.X.jar` to your Android libs

## Usage

```javascript
import { BannerView } from "react-native-smaato";

<BannerView publisherID={11111} publisherID={22222} adDimension={"default"} />;
```
