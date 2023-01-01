import React, { Component } from 'react';
import { GoogleMap, LoadScript } from '@react-google-maps/api';
import { Marker } from '@react-google-maps/api';

const containerStyle = {
  width: '800px',
  height: '800px'
};

const center = {
    lat: 30,
    lng: 120
};

const position = {
    lat: 37.337725744957105,
    lng: -121.88976815988778
}


class MyComponents extends Component {
  render() {
    return (
      <LoadScript
        googleMapsApiKey="AIzaSyBZ3HssfEXZH3kWCEflRr6T9NBWG_GiZsY"
      >
        <GoogleMap
          mapContainerStyle={containerStyle}
          center={position}
          zoom={10}
          
        >
          { <Marker
                    icon={"https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png"}
                    position={position}
                />/* Child components, such as markers, info windows, etc. */ }
          <></>
        </GoogleMap>
      </LoadScript>
    )
  }
}
export default MyComponents