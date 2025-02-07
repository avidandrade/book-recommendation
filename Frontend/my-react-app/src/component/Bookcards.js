import React, { useState, useEffect } from 'react';
import './Bookcards.css';

const BookCards = () => {
    const[books, setBooks] = useState([]);
    const[query, setQuery] = useState('');
    const[userBooks, setUserBooks] = useState([]);


    const fetchBooks = async(SearchQuery) => {
        try{
            const response = await fetch(`http://localhost:8080/searchbooks?query=${SearchQuery}`);
            const data = await response.json();
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
            setBooks([userBooks]);
            if(response.ok){
                console.log('Book saved successfully', book);
            }else{
                console.error('Error saving book');
            }
        }catch(error){
            console.error('Error running function', error);
        }
    };

    const handleRetrieveBooks = async () => {
        try{
            const response = await fetch(`http://localhost:8080/books`,{
                method: 'GET',
                headers: { 
                    'Content-Type': 'application/json'
                }
            });

            if(response.ok){
                const data = await response.json();
                setUserBooks(data);
            }else{
                console.error('Error retrieving books');
            }
        }catch(error){
            console.error('Error retrieving books from database;');
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
            {books.map((book) => (
                <div key={book.id} className="book-card">
                     <img src={book.coverImageUrl} alt="Book Cover" className="book-cover" />
                    <h3>{book.title}</h3>
                    <p><strong>Authors:</strong> {book.authors}</p>
                    <p><strong>Description:</strong> {book.description}</p>
                    <p><strong>ISBN:</strong> {book.isbn}</p>
                    <p><strong>Genre:</strong> {book.genre}</p>
                    <button onClick={() => handleSaveBook(book)}>Save</button>
                </div>
            ))}

            <button onClick={handleRetrieveBooks}>Retrieve save books!</button>

            {userBooks.map((userBook) => (
                <div key={userBook.id} className="book-card">
                     <img src={userBook.coverImageUrl} alt="Book Cover" className="book-cover" />
                    <h3>{userBook.title}</h3>
                    <p><strong>Authors:</strong> {userBook.authors}</p>
                    <p><strong>Description:</strong> {userBook.description}</p>
                    <p><strong>ISBN:</strong> {userBook.isbn}</p>
                    <p><strong>Genre:</strong> {userBook.genre}</p>
                </div>
            ))}
        </div>
    );
};

export default BookCards;