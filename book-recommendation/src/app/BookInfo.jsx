import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const BookInfo = () => {
  const { id } = useParams();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBookInfo = async () => {
      try {
        const response = await fetch(`http://localhost:8080/books/${id}`);
        const data = await response.json();
        setBook(data);
      } catch (error) {
        console.error('Error fetching book information', error);
      } finally {
        setLoading(false);
      }
    };

    fetchBookInfo();
  }, [id]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!book) {
    return <div>Book not found</div>;
  }

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold">{book.title}</h1>
      <p className="text-lg">By {Array.isArray(book.authors) ? book.authors.join(', ') : 'Unknown'}</p>
      <img src={book.coverImageUrl} alt="Book Cover" className="h-30 object-contain rounded-md mb-2" />
      <p className="text-sm text-white-600 line-clamp-6">{book.description}</p>
      <p className="text-sm m-2">
        <strong>Genre:</strong> {book.genre || 'N/A'}
      </p>
      <p className="text-sm mb-2">
        <strong>ISBN:</strong> {book.isbn || 'N/A'}
      </p>
    </div>
  );
};

export default BookInfo;