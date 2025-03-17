import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const BookInfo = () => {
  const { id } = useParams();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    const fetchBookInfo = async () => {
      try {
        const response = await fetch(`http://localhost:8080/books/${id}`);
        const data = await response.json();
        setBook(data);

        if(data.title && data.rating){
          const reviewResponse = await fetch(`http://localhost:8080/review?title=${data.title}&rating=${data.rating}`);
          const reviewText = await reviewResponse.text();
          const reviewData = reviewText.split('.').map((sentence, index) => ({
            id: index,
            comment: sentence.trim(),
            rating: data.rating 
          }));
          setReviews(reviewData);
        }
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
      <div className="mt-4">
        <h2 className="text-xl font-bold">Reviews</h2>
        {reviews.length > 0 ? (
          reviews.map((review, index) => (
            <div key={index} className="mt-2">
              <p className="text-sm"><strong>Rating:</strong> {review.rating}</p>
              <p className="text-sm">{review.comment}</p>
            </div>
          ))
        ) : (
          <p>No reviews available.</p>
        )}
      </div>
    </div>
  );
};

export default BookInfo;