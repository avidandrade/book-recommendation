import React, { useState, useEffect } from 'react';
import './Bookcards.css';

const BookCards = () => {
    const [books, setBooks] = useState([]);

    useEffect(() => {
        fetchBooks();
    }, []);

    const fetchBooks = async () => {
        try {
            const response = await fetch('http://localhost:8080/searchbooks?query=fun');
            const data = await response.json();
            setBooks(data);
        } catch (error) {
            console.error('Error fetching books:', error);
        }
    };

    return (
        <div className="book-cards-container">
            {books.map((book, index) => (
                <div key={index} className="book-card">
                    <h3>{book.title}</h3>
                    <p><strong>Authors:</strong> {book.authors}</p>
                    <p><strong>Description:</strong> {book.description}</p>
                    <p><strong>ISBN:</strong> {book.isbn}</p>
                    <p><strong>Genre:</strong> {book.genre}</p>
                </div>
            ))}
        </div>
    );
};

export default BookCards;