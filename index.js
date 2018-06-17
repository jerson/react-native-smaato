import React from "react";
import { requireNativeComponent } from "react-native";

const RNBannerView = requireNativeComponent("RNSmaato", BannerView);

export class BannerView extends React.Component {
  render() {
    const { publisherID, adSpaceID, adDimension, style } = this.props;

    return (
      <RNBannerView
        style={style}
        publisherID={publisherID.toString()}
        adSpaceID={adSpaceID.toString()}
        adDimension={adDimension || "default"}
      />
    );
  }
}
