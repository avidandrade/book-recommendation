import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";



const BookCards = () => {
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState("");
  const [userBooks, setUserBooks] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchBooks = async (SearchQuery) => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8080/recommend?input=${SearchQuery}`);
      const data = await response.json();
      setBooks(data);
    } catch (error) {
      console.error("Error fetching books from API", error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (event) => {
    setQuery(event.target.value);
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();
    fetchBooks(query);
  };

  const handleSaveBook = async (book) => {
    try {
      const response = await fetch(`http://localhost:8080/saveBook`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(book),
      });
      if (response.ok) {
        console.log("Book saved successfully", book);
      } else {
        console.error("Error saving book");
      }
    } catch (error) {
      console.error("Error running function", error);
    }
  };

  const handleRetrieveBooks = async () => {
    try {
      const response = await fetch(`http://localhost:8080/books`, {
        method: "GET",
        headers: {
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

  return (
    <div className="p-6 max-w-4xl mx-auto">
      {/* Search Bar */}
      <form onSubmit={handleFormSubmit} className="flex gap-2 mb-6">
        <Input placeholder="Search for books" value={query} onChange={handleInputChange} />
        <Button type="submit" disabled={loading}>
          {loading ? "Searching..." : "Search"}
        </Button>
      </form>

      {/* Books Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {loading ? (
          <p>Loading books...</p>
        ) : books.length === 0 ? (
          <p>No books found.</p>
        ) : (
          books.map((book) => (
            <Card key={book.id || book.isbn || book.title} className="shadow-md">
              <CardHeader>
                <CardTitle>{book.title}</CardTitle>
                <CardDescription>By {Array.isArray(book.authors) ? book.authors.join(", ") : "Unknown"}</CardDescription>
              </CardHeader>
              <CardContent>
                <img src={book.coverImageUrl} alt="Book Cover" className="w-full h-40 object-cover rounded-md mb-2" />
                <p className="text-sm text-white-600 line-clamp-6">{book.description}</p>
                <p className="text-sm">
                  <strong>Genre:</strong> {book.genre || "N/A"}
                </p>
                <p className="text-sm">
                  <strong>ISBN:</strong> {book.isbn || "N/A"}
                </p>
                <Button className="mt-2 w-full" onClick={() => handleSaveBook(book)}>
                  Save Book
                </Button>
              </CardContent>
            </Card>
          ))
        )}
      </div>

      {/* Retrieve Saved Books */}
      <div className="mt-8">
        <Button onClick={handleRetrieveBooks} className="w-full">
          Retrieve Saved Books
        </Button>
      </div>

      {/* Display Saved Books */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mt-4">
        {userBooks.length === 0 ? (
          <p>No saved books yet.</p>
        ) : (
          userBooks.map((userBook) => (
            <Card key={userBook.id} className="shadow-md">
              <CardHeader>
                <CardTitle>{userBook.title}</CardTitle>
                <CardDescription>
                  By {Array.isArray(userBook.authors) ? userBook.authors.join(", ") : "Unknown"}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <img src={userBook.coverImageUrl} alt="Book Cover" className="w-full h-40 object-cover rounded-md mb-2" />
                <p className="text-sm text-white-600 line-clamp-6">
                  {userBook.description}
                </p>
                <p className="text-sm">
                  <strong>Genre:</strong> {userBook.genre || "N/A"}
                </p>
                <p className="text-sm">
                  <strong>ISBN:</strong> {userBook.isbn || "N/A"}
                </p>
              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  );
};

export default BookCards
