import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { toast, Toaster } from 'sonner';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardImage } from "@/components/ui/card";
import {Link} from "react-router-dom";
import { supabase } from './routes/supabaseClient';
import { useNavigate } from "react-router-dom";

//add skeleton for loading books.

const BookCards = () => {
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState("");
  const [userBooks, setUserBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [initialSearchDone, setInitialSearchDone] = useState(false);
  const navigate = useNavigate();

  const handleLogout = async () => {
    try{
       const {error} = await supabase.auth.signOut();

       if(error){
        throw new error(error.message);
       }

       localStorage.removeItem('token');
       navigate('/login');
       toast.success('Logged out Successfully!');
    }
    catch(error){
      console.error('Error Logging out:' + error.message);
      toast.error('Eror Logging out. Please try again.');
    }
  };

  const fetchBooks = async (SearchQuery) => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/recommend?input=${SearchQuery}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });
      const data = await response.json();
      setInitialSearchDone(true);
      setQuery(SearchQuery);
      setBooks(data);
      console.log(data);

    } catch (error) {
      console.error("Error fetching books from API", error);
    } finally {
      setLoading(false);
    }
  };

  const getBookTitles = () => {
    return books.map(book => book.title);
  };

  const handleInputChange = (event) => {
    setQuery(event.target.value);
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();
    if(query.trim() == ""){
      toast.error("Please enter a search query!");
      return;
    }
    fetchBooks(query);
  };

  const handleLoadMore = async () => {
    try {
      const token = localStorage.getItem('token');
      const titles = getBookTitles().join(",");
      const response = await fetch(`http://localhost:8080/moreBooks?input=${query}&titles=${titles}`,{
        headers:{
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
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
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/saveBook`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
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
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/books`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
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
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/books/${bookId}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
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

  return (
    <div className="p-6 max-w-4xl mx-auto">

      <div className="flex justify-end mb-4">
        <Button onClick={handleLogout} className="bg-red-500 hover:bg-red-600 text-white">
          Logout
        </Button>
      </div>

      <h1>BookWise!</h1>
      <p className='p-3'>Discover book recommendations based on your feelings!</p>
      <Toaster />

      {/* Search Bar */}
      <form onSubmit={handleFormSubmit} className="flex gap-2 mb-6 p-4">
        <Input placeholder="Try searching for books!" value={query} onChange={handleInputChange} />
        <Button type="submit" disabled={loading}>
          {loading ? "Searching..." : "Search"}
        </Button>
      </form>
       
      {/* Books Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      {loading ? (
          Array.from({ length: 6 }).map((_, index) => (
            <Card key={index} className="shadow-md fade-in">
              <CardHeader>
                <CardTitle><Skeleton width={150} /></CardTitle>
                <CardDescription><Skeleton width={100} /></CardDescription>
              </CardHeader>
              <CardContent>
                <Skeleton height={200} />
                <div className="text-sm text-white-600 line-clamp-6"><Skeleton count={3} /></div>
                <div className="text-sm m-2"><Skeleton width={80} /></div>
                <div className="text-sm mb-2"><Skeleton width={80} /></div>
                <Button className="mt-2 w-full size text-sm" disabled>
                  <Skeleton width={60} />
                </Button>
              </CardContent>
            </Card>
          ))
        ) : books.length === 0 ? (
          <p>No books found.</p>
        ) : (
          books.map((book) => (
            <Card key={book.id || book.isbn || book.title} className="shadow-md fade-in">
              <CardHeader>
                <CardTitle>{book.title}</CardTitle>
                <CardDescription>By {Array.isArray(book.authors) ? book.authors.join(", ") : "Unknown"}</CardDescription>
              </CardHeader>
              <CardContent>
              <CardImage src={book.coverImageUrl} alt="Book Cover" className="h-30 object-contain rounded-md mb-2" />
                <p className="text-sm text-white-600 line-clamp-6">{book.description}</p>
                <p className="text-sm m-2">
                  <strong>Genre:</strong> {book.genre || "N/A"}
                </p>
                <p className="text-sm mb-2">
                  <strong>ISBN:</strong> {book.isbn || "N/A"}
                </p>
                <Link to={`/books/${book.id}`}>
                  <Button className="mt-2 w-full size text-sm">
                    View Details
                  </Button>
                </Link>
                <Button className="mt-2 w-full size text-sm" onClick={() => handleSaveBook(book)}>
                  Save Book
                </Button>
              </CardContent>
            </Card>
          ))
        )}
      </div>

      {initialSearchDone && (
        <div className="mt-8 flex justify-center">
          <Button onClick={handleLoadMore} disabled={loading}>
            {loading ? "Loading more..." : "Load More"}
          </Button>
        </div>
      )}

      {/* Retrieve Saved Books */}
      <div className="mt-8">
        <Button onClick={handleRetrieveBooks} className="w-full">
          Retrieve Saved Books
        </Button>
      </div>

      {/* Display Saved Books */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-4">
        {userBooks.length === 0 ? (
          <p>No saved books yet.</p>
        ) : (
          userBooks.map((userBook) => (
            <Card key={userBook.id} className="shadow-md fade-in">
              <CardHeader>
                <CardTitle>{userBook.title}</CardTitle>
                <CardDescription>
                  By {Array.isArray(userBook.authors) ? userBook.authors.join(", ") : "Unknown"}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <CardImage src={userBook.coverImageUrl} alt="Book Cover" className="h-30 object-contain rounded-lg mb-2" />
                <p className="text-sm text-white-600 line-clamp-6">
                  {userBook.description}
                </p>
                <p className="text-sm m-2">
                  <strong>Genre:</strong> {userBook.genre || "N/A"}
                </p>
                <p className="text-sm mb-2">
                  <strong>ISBN:</strong> {userBook.isbn || "N/A"}
                </p>
                <Link to={`/books/${userBook.id}`}>
                  <Button className="mt-2 w-full size text-sm">
                    View Details
                  </Button>
                </Link>
                <Button className="mt-2 w-full text-sm" onClick={() => handleDeleteBook(userBook.id)}>
                  Delete
                </Button>
              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  );
};

export default BookCards
