import React, { useState } from 'react';

const Recommendations = () => {
    const [preferences, setPreferences] = useState('');
    const [recommendations, setRecommendations] = useState('');

    const fetchRecommendations = async () => {
        try {
            const response = await fetch(`http://localhost:8080/recommendations?preferences=${encodeURIComponent(preferences)}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.text();
            setRecommendations(data);
        } catch (error) {
            console.error("Error fetching recommendations:", error);
        }
    };

    return (
        <div>
            <h1>Book Recommendations</h1>
            <textarea
                value={preferences}
                onChange={(e) => setPreferences(e.target.value)}
                placeholder="Enter your favorite genres, books, or authors"
            />
            <button onClick={fetchRecommendations}>Get Recommendations</button>
            <div>
                <h2>Recommended Books:</h2>
                <p>{recommendations}</p>
            </div>
        </div>
    );
}

export default Recommendations;