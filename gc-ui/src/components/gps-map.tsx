"use client";

import { MapContainer, TileLayer, Polyline, Marker, Popup, useMap } from "react-leaflet";
import { LatLngExpression, LatLngBounds } from "leaflet";
import { useEffect } from "react";

function FitBoundsHandler({ gps }: { gps: LatLngExpression[] }) {
  const map = useMap();

  useEffect(() => {
    if (gps.length > 0) {
      const bounds = new LatLngBounds(gps);
      map.fitBounds(bounds, { padding: [5, 5] }); // Optional: padding für etwas Abstand
    }
  }, [gps, map]);

  return null;
}

// Beispielhafte GPS-Koordinaten
const gpsTrack: LatLngExpression[] = [[52.970917, 8.83997], [52.970882, 8.839995], [52.97085, 8.840022], [52.970818, 8.840051], [52.97077, 8.840091], [52.970722, 8.840125], [52.970676, 8.840165], [52.970642, 8.840211], [52.97062, 8.840236], [52.970585, 8.840267], [52.970547, 8.840301], [52.9705, 8.840339], [52.970455, 8.840366], [52.970406, 8.840381], [52.970356, 8.840385], [52.9703, 8.840389], [52.97023, 8.840412], [52.97018, 8.840436], [52.970146, 8.840481], [52.970127, 8.840549]];

type GpsMapProps = {
  gps: LatLngExpression[];
}

export default function GpsMap({ gps }: GpsMapProps) {
  let data = gps.length > 0 ? gps : [[52.52, 13.405]]
  const center = data.length > 0 ? data[0] : [52.52, 13.405]; // Berlin als Default
  console.log("data Track:", JSON.stringify(data));

  return (
    <div className="w-full h-[1100px] rounded-xl overflow-hidden">
      <MapContainer key={JSON.stringify(data)} center={center} zoom={15} className="h-full w-full">
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        <FitBoundsHandler gps={data} />
        <Polyline positions={data} color="blue" weight={4} />
        <Marker position={data[0]}>
          <Popup>Start</Popup>
        </Marker>
        <Marker position={data[data.length - 1]}>
          <Popup>Ziel</Popup>
        </Marker>
      </MapContainer>
    </div>
  );
}
