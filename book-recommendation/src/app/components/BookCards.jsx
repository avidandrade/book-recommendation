import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { toast, Toaster } from 'sonner';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardImage } from "@/components/ui/card";
import {Link} from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { supabase } from '../routes/supabaseClient';
import { useBooks } from "./BookCalls";

const BookCards = () => {
  const [query, setQuery] = useState("");
  const [initialSearchDone, setInitialSearchDone] = useState(false);
  const navigate = useNavigate();
  const backend_url = import.meta.env.VITE_BACKEND_URL;

  const{
    books,
    userBooks,
    fetchBooks,
    handleDeleteBook,
    fetchLoadMore,
    handleRetrieveBooks,
    handleSaveBook,
    loading,
  } = useBooks();

  const handleLogout = async () => {
    try{
      await supabase.auth.signOut();
      const response = await fetch(`${backend_url}/auth/logout`,{
        method: "POST",
        credentials: 'include',
      });

      if(response.ok){
        navigate('/login');
        toast.success('Logged out Successfully!');
      }
    }
    catch(error){
      console.error('Error Logging out:' + error.message);
      toast.error('Eror Logging out. Please try again.');
    }
  };

  const handleLoadMore = () => {
    const titles = books.map((book) => book.title).join(",");
    fetchLoadMore(query, titles);
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
    setInitialSearchDone(true);
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
          Array.isArray(books) &&
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
                <Link to={`/books/${book.isbn}`}>
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
                <Link to={`/books/${userBook.isbn}`}>
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
