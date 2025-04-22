import {useEffect, useState} from "react";
import Map from "./components/Map";

import 'leaflet/dist/leaflet.css';
import './App.css';


function App() {
    const [graphData, setGraphData] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('http://localhost:8080/graph');
                const data = await response.json();

                setGraphData(data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, []);


    return (<>
            <Map graphData={graphData}/>
        </>);
}

export default App;
