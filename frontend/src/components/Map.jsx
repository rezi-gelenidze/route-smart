import {MapContainer, Marker, Popup, TileLayer, Polyline, Tooltip} from 'react-leaflet';
import L from 'leaflet';

const Map = ({ graphData }) => {
    const initialGeorgiaCenter = [42.2, 43.5];
    const georgiaBounds = [
        [38.4386, 39.1181],
        [45.6458, 48.5299],
    ];

    if (!graphData) return null;

    const { nodes, edges } = graphData;

    const getColorByComplexity = (complexity) => {
        const ratio = (complexity - 1) / 9;
        const r = Math.floor(255 * ratio);
        const g = Math.floor(255 * (1 - ratio));
        return `rgb(${r},${g},0)`;
    };

    return (
        <MapContainer
            className="map-container"
            zoom={8}
            minZoom={8}
            maxZoom={18}
            center={initialGeorgiaCenter}
            maxBounds={georgiaBounds}
        >
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            {/* Render nodes */}
            {nodes.map(node => (
                <Marker
                    key={node.id}
                    icon={L.divIcon({
                        className: 'node-icon',
                    })}
                    position={[node.location[1], node.location[0]]} // [lat, lon]
                >
                    <Tooltip>{node.name} [{node.id}]</Tooltip>
                </Marker>
            ))}

            {/* Render edges */}
            {edges.map(edge => (
                <Polyline
                    key={edge.id}
                    positions={edge.coordinates.map(coord => [coord[1], coord[0]])} // [lat, lon]
                    pathOptions={{ color: getColorByComplexity(edge.complexity), weight: 3 }}
                >
                    <Tooltip>
                        Complexity: {edge.complexity}/10<br />
                        Length: {edge.distance} km
                    </Tooltip>
                </Polyline>
            ))}
        </MapContainer>
    );
};

export default Map;
