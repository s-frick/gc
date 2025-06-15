"use client";

import { MapContainer, TileLayer, Polyline, Marker, Popup, useMap, CircleMarker } from "react-leaflet";
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

type GpsMapProps = {
  gps: LatLngExpression[];
  currentPos?: LatLngExpression;
}

export default function GpsMap({ gps, currentPos }: GpsMapProps) {
  let data = gps.length > 0 ? gps : [[52.52, 13.405]]
  const center = data.length > 0 ? data[0] : [52.52, 13.405]; // Berlin als Default

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

        {currentPos && (
          <CircleMarker
            center={currentPos}
            radius={4}
            pathOptions={{ color: "red", fillColor: "red", fillOpacity: 0.8 }}
          />
        )}
      </MapContainer>
    </div>
  );
}
