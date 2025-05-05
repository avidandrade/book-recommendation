import { useState } from "react";
import { toast, Toaster } from 'sonner';

export const useBooks = () => {
  const [books, setBooks] = useState([]);
  const [userBooks, setUserBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const backend_url = import.meta.env.VITE_BACKEND_URL;


    const fetchBooks = async (SearchQuery) => {
        try {
          setLoading(true);
          const response = await fetch(`${backend_url}/recommend?input=${SearchQuery}`, {
            method: "GET",
            credentials: 'include',
          });
          const data = await response.json();
          setBooks(data);
    
        } catch (error) {
          console.error("Error fetching books from API", error);
        } finally {
          setLoading(false);
        }
      };

    const fetchLoadMore = async (query,titles) => {
        try {
          const response = await fetch(`${backend_url}/moreBooks?input=${query}&titles=${titles}`,{
            credentials: 'include',
          });
          const data = await response.json();
          console.log(titles);
            if (Array.isArray(data)) {
              setBooks((prevBooks) => [...prevBooks, ...data]);
            } else {
              console.error("Error: data is not an array");
            }
    
        }catch(error){
          console.error("Error fetching more books from API", error);
        }
      };
    
      const handleSaveBook = async (book) => {
        try {
          const response = await fetch(`${backend.url}/saveBook`, {
            method: "POST",
            headers: {
              "Content-Type" : "application/json",
            },
            credentials: 'include',
            body: JSON.stringify(book),
          });
          if (response.ok) {
            const savedBook = await response.json();
            setUserBooks((prevuserBooks) => [...prevuserBooks, savedBook]);
            toast.success("Book saved successfully!");
            console.log("Book saved successfully", savedBook);
          } else {
            console.error("Error saving book");
            toast.error("Error saving book");
          }
        } catch (error) {
          console.error("Error running function", error);
        }
      };
    
      const handleRetrieveBooks = async () => {
        try {
          const response = await fetch(`${backend_url}/books`, {
            method: "GET",
            credentials: 'include',
          });
          if (response.ok) {
            const data = await response.json();
            setUserBooks(data);
          } else {
            console.error("Error retrieving books");
          }
        } catch (error) {
          console.error("Error retrieving books from database");
        }
      };
    
      const handleDeleteBook = async (bookId) => {
        try {
          const response = await fetch(`${backend_url}/books/${bookId}`, {
            method: "DELETE",
            credentials: 'include',
          });
          if (response.ok) {
            console.log("Book deleted successfully");
            toast.success("Book deleted successfully!");
            setUserBooks((prevBooks) => prevBooks.filter((book) => book.id !== bookId));
          } else {
            console.error("Error deleting book");
            toast.error("Error deleting book");
          }
        } catch (error) {
          console.error("Error deleting book from database");
        }
      };

      return {
        books,
        userBooks,
        fetchBooks,
        handleDeleteBook,
        fetchLoadMore,
        handleRetrieveBooks,
        handleSaveBook,
        loading,
      };
};