import React, { useState, useEffect } from 'react';
import './Bookcards.css';

const BookCards = () => {
    const[books, setBooks] = useState([]);
    const[query, setQuery] = useState('');


    const fetchBooks = async(SearchQuery) => {
        try{
            const response = await fetch(`http://localhost:8080/searchbooks?query=${SearchQuery}`);
            const data = await response.json();
            console.log(data);
            setBooks(data);
        }catch(error){
            console.error('Error fetching books from api', error);
        }
    }

    const handleInputChange = (event) => {
        setQuery(event.target.value);
    }

    const handleFormSubmit = (event) => {
        event.preventDefault();
        fetchBooks(query);
    };

    const handleSaveBook = async (book) =>{
        try{
            const response = await fetch(`http://localhost:8080/saveBook`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(book),
            })

            if(response.ok){
                console.log('Book saved successfully');
            }else{
                console.error('Error saving book');
            }
        }catch(error){
            console.error('Error running function', error);
        }
    };

    return (
        <div className="book-cards-container">
            <form onSubmit={handleFormSubmit}>
                <input
                    type="text"
                    value={query}
                    onChange={handleInputChange}
                    placeholder="Search for books"
                />
                <button type="submit">Search</button>
            </form>
            {books.map((book, index) => (
                <div key={index} className="book-card">
                     <img src={book.coverImageUrl} alt="Book Cover" className="book-cover" />
                    <h3>{book.title}</h3>
                    <p><strong>Authors:</strong> {book.authors}</p>
                    <p><strong>Description:</strong> {book.description}</p>
                    <p><strong>ISBN:</strong> {book.isbn}</p>
                    <p><strong>Genre:</strong> {book.genre}</p>
                    <button onClick={() => handleSaveBook(book)}>Save</button>
                </div>
            ))}
        </div>
    );
};

export default BookCards;